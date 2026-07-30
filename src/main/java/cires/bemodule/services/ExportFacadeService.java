package cires.bemodule.services;

import cires.bemodule.enums.ExportFormat;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Facade that encapsulates the complete export flow for any entity type.
 * <p>
 * Responsible for:
 * <ul>
 *   <li>Building a descriptive file name that includes active filters</li>
 *   <li>Calling {@link ExportService} to write CSV or Excel</li>
 *   <li>Recording the export in {@link ExportHistoryService}</li>
 * </ul>
 * </p>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ExportFacadeService {

    private final ExportService exportService;
    private final ExportHistoryService exportHistoryService;

    /**
     * Executes an export for the given entity type.
     *
     * @param response    HTTP response to write the file to
     * @param entityType  label identifying what is exported (e.g. "participants")
     * @param format      CSV or EXCEL
     * @param filters     non‑null filter values to include in the file name
     * @param headers     column headers
     * @param dataSupplier provides the list of objects to export
     * @param rowMapper   converts a single object to an array of cell values
     * @param sheetName   sheet name (only relevant for Excel exports)
     * @param <T>         type of the exported objects
     * @throws IOException if writing the file fails
     */
    public <T> void export(HttpServletResponse response,
                           String entityType,
                           ExportFormat format,
                           Map<String, String> filters,
                           String[] headers,
                           List<T> dataSupplier,
                           Function<T, String[]> rowMapper,
                           String sheetName) throws IOException {

        // 1. Build descriptive file name
        String fileName = buildExportFileName(entityType, format, filters);

        // 2. Write the file
        if (format == ExportFormat.EXCEL) {
            exportService.exportToExcel(response, fileName, sheetName, headers, dataSupplier, rowMapper);
        } else {
            exportService.exportToCsv(response, fileName, headers, dataSupplier, rowMapper);
        }

        // 3. Record export history
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        exportHistoryService.recordExport(username, entityType, format, fileName);
    }

    private String buildExportFileName(String entity, ExportFormat format,
                                       Map<String, String> filters) {
        StringBuilder sb = new StringBuilder(entity);
        sb.append("_").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")));

        for (Map.Entry<String, String> filter : filters.entrySet()) {
            if (filter.getValue() != null && !filter.getValue().isEmpty()) {
                sb.append("_").append(filter.getKey()).append("-").append(filter.getValue());
            }
        }

        String extension = format == ExportFormat.EXCEL ? "xlsx" : "csv";
        return sb.append(".").append(extension).toString();
    }
}