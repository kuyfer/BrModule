package cires.bemodule.services;

import cires.bemodule.dtos.imports.*;
import cires.bemodule.entities.TrainingSession;
import cires.bemodule.enums.TrainingSessionStatus;
import cires.bemodule.exceptions.imports.FileProcessingException;
import cires.bemodule.exceptions.imports.ImportRowException;
import cires.bemodule.exceptions.imports.ImportValidationException;
import cires.bemodule.repositories.ParticipantBulkRepository;
import cires.bemodule.repositories.TrainingSessionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Statement;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParticipantImportService {

    private final TrainingSessionRepository sessionRepository;
    private final ParticipantBulkRepository bulkRepository;

    private static final List<String> EXPECTED_HEADERS =
            List.of("firstname", "lastname", "email", "phone", "formation");

    // ─── TEMPLATE ─────────────────────────────────────────────────────────────

    /**
     * Generates an Excel template with:
     * - Headers on row 1
     * - An example row
     * - A dropdown on the formation column pre-filled from active sessions
     */
    public byte[] generateExcelTemplate() {
        List<TrainingSession> sessions = sessionRepository
                .findByStatusIn(List.of(
                        TrainingSessionStatus.SCHEDULED,
                        TrainingSessionStatus.ONGOING));

        log.info("Generating Excel template with {} active sessions", sessions.size());

        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            // Hidden sheet — source of the dropdown values
            Sheet refSheet = workbook.createSheet("formations_ref");
            workbook.setSheetHidden(workbook.getSheetIndex("formations_ref"), true);
            for (int i = 0; i < sessions.size(); i++) {
                TrainingSession s = sessions.get(i);
                refSheet.createRow(i).createCell(0)
                        .setCellValue(s.getId() + " - " + s.getTitle());
            }

            // Main sheet
            Sheet sheet = workbook.createSheet("participants");
            CellStyle bold = boldStyle(workbook);

            // Header row
            String[] headers = {"firstName", "lastName", "email", "phone", "formation"};
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(bold);
                sheet.setColumnWidth(i, 6000);
            }

            // Example row so the user knows the format
            Row example = sheet.createRow(1);
            example.createCell(0).setCellValue("John");
            example.createCell(1).setCellValue("Doe");
            example.createCell(2).setCellValue("john.doe@email.com");
            example.createCell(3).setCellValue("0600000000");
            // formation cell — user picks from dropdown

            // Dropdown validation on column E (index 4), rows 2-1001
            if (!sessions.isEmpty()) {
                String formula = "formations_ref!$A$1:$A$" + sessions.size();
                DataValidationHelper dvHelper = sheet.getDataValidationHelper();
                DataValidationConstraint constraint =
                        dvHelper.createFormulaListConstraint(formula);
                CellRangeAddressList range = new CellRangeAddressList(1, 1000, 4, 4);
                DataValidation validation = dvHelper.createValidation(constraint, range);
                validation.setShowErrorBox(true);
                validation.createErrorBox("Invalid formation",
                        "Please select a formation from the list.");
                sheet.addValidationData(validation);
            }

            workbook.write(out);
            byte[] template = out.toByteArray();
            log.info("Excel template generated successfully, size: {} bytes", template.length);
            return template;

        } catch (IOException e) {
            log.error("Failed to generate Excel template", e);
            throw new FileProcessingException("Failed to generate Excel template: " + e.getMessage());
        }
    }

    // ─── CSV IMPORT ───────────────────────────────────────────────────────────

    public ImportResult importFromCsv(MultipartFile file, Long sessionId) {
        log.info("Importing participants from CSV file: {}", file.getOriginalFilename());
        validateFile(file, "text/csv");
        List<RawImportRow> rows = parseCsv(file);
        log.debug("Parsed {} rows from CSV", rows.size());
        return process(rows, sessionId);
    }

// ─── EXCEL IMPORT ─────────────────────────────────────────────────────────

    public ImportResult importFromExcel(MultipartFile file, Long sessionId) {
        log.info("Importing participants from Excel file: {}", file.getOriginalFilename());
        validateFile(file, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        List<RawImportRow> rows = parseExcel(file);
        log.debug("Parsed {} rows from Excel", rows.size());
        return process(rows, sessionId);
    }
    // ─── CORE PIPELINE ────────────────────────────────────────────────────────

    /**
     * 1. Validate every row — collect errors, never abort early
     * 2. Resolve and cache session IDs
     * 3. Batch insert participants   (one DB round trip)
     * 4. Batch insert session links  (one DB round trip)
     */
    /**
     * Original processing without a forced session (delegates to the new method).
     */
    @Transactional
    protected ImportResult process(List<RawImportRow> rawRows) {
        return process(rawRows, null);
    }

    /**
     * Processes the raw rows.
     * <p>
     * If {@code forcedSessionId} is not {@code null}, every valid row is linked to
     * that session and the file's "formation" column is ignored.
     * Otherwise the session ID is extracted from the formation column.
     * </p>
     */
    @Transactional
    protected ImportResult process(List<RawImportRow> rawRows, Long forcedSessionId) {
        log.debug("Processing {} import rows", rawRows.size());
        List<ImportRowError>     errors   = new ArrayList<>();
        List<ValidatedImportRow> valid    = new ArrayList<>();
        int skippedCount = 0;

        // Cache session existence checks — avoids N+1 queries
        Map<Long, Boolean> sessionCache = new HashMap<>();

        for (RawImportRow raw : rawRows) {
            try {
                validate(raw);

                String email     = raw.getEmail().trim().toLowerCase();
                Long   sessionId;

                if (forcedSessionId != null) {
                    // Force all rows into this session, ignore formationRaw
                    sessionId = forcedSessionId;
                } else {
                    sessionId = resolveSessionId(raw.getFormationRaw(), raw.getRowNumber());
                }

                boolean sessionExists = sessionCache.computeIfAbsent(
                        sessionId, sessionRepository::existsById);

                if (!sessionExists) {
                    throw new ImportRowException(
                            "Formation ID " + sessionId + " does not exist.");
                }

                valid.add(ValidatedImportRow.builder()
                        .firstName(raw.getFirstName().trim())
                        .lastName(raw.getLastName().trim())
                        .email(email)
                        .phone(raw.getPhone() != null ? raw.getPhone().trim() : null)
                        .sessionId(sessionId)
                        .build());

            } catch (ImportRowException e) {
                errors.add(ImportRowError.builder()
                        .row(raw.getRowNumber())
                        .email(raw.getEmail())
                        .reason(e.getMessage())
                        .formationRaw(raw.getFormationRaw())
                        .build());
                skippedCount++;
            }
        }

        int insertedCount = 0;

        if (!valid.isEmpty()) {
            // Batch 1 — upsert participants
            try {
                int[] participantResults = bulkRepository.bulkInsertParticipants(valid);
                insertedCount = (int) Arrays.stream(participantResults)
                        .filter(r -> r >= 0 || r == Statement.SUCCESS_NO_INFO)
                        .count();
            } catch (DataAccessException e) {
                log.error("Bulk insert failed: {}", e.getMostSpecificCause().getMessage());
                throw new FileProcessingException("Database error during import. Check table constraints.");
            }
            // Batch 2 — link participants to their session
            List<SessionParticipantLink> links = valid.stream()
                    .map(r -> SessionParticipantLink.builder()
                            .sessionId(r.getSessionId())
                            .email(r.getEmail())
                            .build())
                    .toList();

            bulkRepository.bulkInsertSessionLinks(links);
        }

        log.info("Import complete — total={}, inserted={}, skipped={}, errors={}",
                rawRows.size(), insertedCount, skippedCount, errors.size());

        return ImportResult.builder()
                .totalRows(rawRows.size())
                .successCount(insertedCount)
                .skippedCount(skippedCount)
                .errorCount(errors.size())
                .errors(errors)
                .build();
    }
    // ─── PARSERS ──────────────────────────────────────────────────────────────

    private List<RawImportRow> parseCsv(MultipartFile file) {
        List<RawImportRow> rows = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
             CSVParser parser = CSVFormat.DEFAULT.builder()
                     .setHeader()
                     .setSkipHeaderRecord(true)
                     .setIgnoreHeaderCase(true)
                     .setTrim(true)
                     .build()
                     .parse(reader)) {

            validateHeaders(parser.getHeaderNames());

            int rowNum = 2; // row 1 = header
            for (CSVRecord rec : parser) {
                rows.add(RawImportRow.builder()
                        .rowNumber(rowNum++)
                        .firstName(rec.get("firstName"))
                        .lastName(rec.get("lastName"))
                        .email(rec.get("email"))
                        .phone(safeGet(rec, "phone"))
                        .formationRaw(rec.get("formation"))
                        .build());
            }

        } catch (IOException e) {
            log.error("Failed to parse CSV", e);
            throw new FileProcessingException("Failed to parse CSV: " + e.getMessage());
        }

        return rows;
    }

    private List<RawImportRow> parseExcel(MultipartFile file) {
        List<RawImportRow> rows = new ArrayList<>();

        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheet("participants");
            if (sheet == null) sheet = workbook.getSheetAt(0);

            Row headerRow = sheet.getRow(0);
            if (headerRow == null) throw new ImportValidationException("File has no header row.");
            validateHeaders(readExcelHeaders(headerRow));

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null || isRowBlank(row)) continue;

                rows.add(RawImportRow.builder()
                        .rowNumber(i + 1)
                        .firstName(cellValue(row, 0))
                        .lastName(cellValue(row, 1))
                        .email(cellValue(row, 2))
                        .phone(cellValue(row, 3))
                        .formationRaw(cellValue(row, 4))
                        .build());
            }

        } catch (IOException e) {
            log.error("Failed to parse Excel", e);
            throw new FileProcessingException("Failed to parse Excel: " + e.getMessage());
        }

        return rows;
    }

    // ─── VALIDATION ───────────────────────────────────────────────────────────

    private void validate(RawImportRow row) {
        requireField(row.getFirstName(),   "firstName",  row.getRowNumber());
        requireField(row.getLastName(),    "lastName",   row.getRowNumber());
        requireField(row.getEmail(),       "email",      row.getRowNumber());
        requireField(row.getFormationRaw(),"formation",  row.getRowNumber());

        String email = row.getEmail().trim();
        if (!email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            throw new ImportRowException(
                    "Row " + row.getRowNumber() + ": invalid email format '" + email + "'.");
        }
    }

    private void requireField(String value, String field, int rowNumber) {
        if (value == null || value.isBlank()) {
            throw new ImportRowException(
                    "Row " + rowNumber + ": '" + field + "' is required.");
        }
    }

    private void validateHeaders(List<String> actual) {
        List<String> normalized = actual.stream()
                .map(String::toLowerCase)
                .toList();
        List<String> missing = EXPECTED_HEADERS.stream()
                .filter(h -> !normalized.contains(h))
                .toList();
        if (!missing.isEmpty()) {
            log.warn("Invalid file headers. Missing columns: {}", missing);
            throw new ImportValidationException("Invalid file. Missing columns: " + missing);
        }
    }

    // ─── HELPERS ──────────────────────────────────────────────────────────────

    /**
     * Parses "42 - Java Bootcamp" → 42L
     * Also accepts plain "42"
     */
    private Long resolveSessionId(String raw, int rowNumber) {
        try {
            String idPart = raw.contains("-") ? raw.split("-")[0].trim() : raw.trim();
            return Long.parseLong(idPart);
        } catch (NumberFormatException e) {
            log.warn("Failed to parse session id from '{}' at row {}", raw, rowNumber);
            throw new ImportRowException(
                    "Row " + rowNumber + ": cannot read formation ID from '" + raw + "'.");
        }
    }

    private void validateFile(MultipartFile file, String expectedType) {
        if (file == null || file.isEmpty()) {
            log.error("File is null or empty");
            throw new FileProcessingException("File is empty.");
        }
    }

    private List<String> readExcelHeaders(Row row) {
        List<String> headers = new ArrayList<>();
        for (Cell cell : row) headers.add(cellValue(cell));
        return headers;
    }

    private String cellValue(Row row, int col) {
        Cell cell = row.getCell(col);
        return cell == null ? null : cellValue(cell);
    }

    private String cellValue(Cell cell) {
        return switch (cell.getCellType()) {
            case STRING  -> cell.getStringCellValue().trim();
            case NUMERIC -> String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            default      -> null;
        };
    }

    private String safeGet(CSVRecord rec, String column) {
        return rec.isMapped(column) ? rec.get(column) : null;
    }

    private boolean isRowBlank(Row row) {
        for (Cell cell : row) {
            if (cell.getCellType() != CellType.BLANK) return false;
        }
        return true;
    }

    private CellStyle boldStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        return style;
    }
}