package cires.bemodule.services;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.io.IOException;
import java.util.List;
import java.util.function.Function;


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

        response.setContentType("text/csv; charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");

        try (PrintWriter writer = response.getWriter()) {
            writer.println(String.join(",", headers));

            for (T item : data) {
                String[] fields = mapper.apply(item);
                String row = String.join(",", escapeCsvFields(fields));
                writer.println(row);
            }
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
