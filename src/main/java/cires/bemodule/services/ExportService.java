package cires.bemodule.services;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.io.IOException;
import java.util.List;
import java.util.function.Function;

@Slf4j
@Service
public class ExportService {
    /**
     * Generic method to export any list of objects as CSV.
     *
     * @param response HttpServletResponse to write to
     * @param filename  output filename
     * @param headers   CSV headers (e.g., ["Id", "Name", "Email"])
     * @param data      list of objects to export
     * @param mapper    function that converts an object to a CSV row (array of strings)
     */
    public <T> void exportToCsv(HttpServletResponse response,
                                String filename,
                                String[] headers,
                                List<T> data,
                                Function<T, String[]> mapper) throws IOException {

        log.info("Starting CSV export to file: {}, number of records: {}", filename, data != null ? data.size() : 0);

        response.setContentType("text/csv; charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");

        try (PrintWriter writer = response.getWriter()) {
            writer.println(String.join(",", headers));

            for (T item : data) {
                String[] fields = mapper.apply(item);
                String row = String.join(",", escapeCsvFields(fields));
                writer.println(row);
            }
            log.info("CSV export completed successfully: {}", filename);
        } catch (IOException e) {
            log.error("Error during CSV export: {}", filename, e);
            throw e;
        }
    }

    /**
     * Generic method to export any list of objects as Excel (.xlsx).
     *
     * @param response HttpServletResponse
     * @param filename  output filename (should end with .xlsx)
     * @param sheetName name of the Excel sheet
     * @param headers   column headers
     * @param data      list of objects
     * @param mapper    function to convert object to row cells (array of strings)
     */
    public <T> void exportToExcel(HttpServletResponse response,
                                  String filename,
                                  String sheetName,
                                  String[] headers,
                                  List<T> data,
                                  Function<T, String[]> mapper) throws IOException {

        log.info("Starting Excel export to file: {}, sheet: {}, records: {}", filename, sheetName, data != null ? data.size() : 0);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet(sheetName);

            Row headerRow = sheet.createRow(0);
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
                sheet.autoSizeColumn(i);
            }

            int rowNum = 1;
            for (T item : data) {
                Row row = sheet.createRow(rowNum++);
                String[] cells = mapper.apply(item);
                for (int i = 0; i < cells.length; i++) {
                    row.createCell(i).setCellValue(cells[i] != null ? cells[i] : "");
                }
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(response.getOutputStream());
            response.getOutputStream().flush();
            log.info("Excel export completed successfully: {}", filename);
        } catch (IOException e) {
            log.error("Error during Excel export: {}", filename, e);
            throw e;
        }
    }

    /**
     * Escaping for CSV: wrap fields containing comma, newline, or quotes in double quotes,
     * and escape existing double quotes by doubling them.
     */
    private String[] escapeCsvFields(String[] fields) {
        String[] escaped = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            if (fields[i] == null) {
                escaped[i] = "";
                continue;
            }
            boolean needsQuotes = fields[i].contains(",") || fields[i].contains("\"") || fields[i].contains("\n") || fields[i].contains("\r");
            if (needsQuotes) {
                String escapedField = fields[i].replace("\"", "\"\"");
                escaped[i] = "\"" + escapedField + "\"";
            } else {
                escaped[i] = fields[i];
            }
        }
        return escaped;
    }
}