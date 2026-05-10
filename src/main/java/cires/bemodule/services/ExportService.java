package cires.bemodule.services;

import cires.bemodule.entities.Notification;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.io.OutputStreamWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.rmi.server.ExportException;
import java.util.List;
import java.util.function.Function;


@Service
public class ExportService {

    private String[] notificationCsvHeaders() {
        return new String[]{"ID", "Status", "Type", "Failure reason", "Subject", "Recipient"};
    }


    private String[] notificationToCsvRow(Notification n) {
        return new String[]{
                String.valueOf(n.getId()), n.getNotificationStatus().toString(), n.getNotificationType().toString(),
                n.getFailureReason(), n.getSubject(), n.getToEmail()
        };
    }

    private <T> byte[] toCsv(List<T> rows, Function<T, String[]> rowMapper, String[] headers) throws ExportException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             PrintWriter writer = new PrintWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8));
             CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(headers))) {

            for (T row : rows) {
                printer.printRecord((Object[]) rowMapper.apply(row));
            }
            printer.flush();
            return out.toByteArray();

        } catch (IOException e) {
            throw new ExportException("Failed to generate CSV export: " + e.getMessage());
        }
    }
}
