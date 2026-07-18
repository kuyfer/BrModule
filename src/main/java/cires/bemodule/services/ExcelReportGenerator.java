package cires.bemodule.services;

import cires.bemodule.dtos.ReportData;
import cires.bemodule.exceptions.ReportGenerationException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;

@Component
public class ExcelReportGenerator {

    public byte[] generate(ReportData data) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Rapport");

            CellStyle titleStyle = workbook.createCellStyle();
            Font titleFont = workbook.createFont();
            titleFont.setBold(true);
            titleFont.setFontHeightInPoints((short) 14);
            titleStyle.setFont(titleFont);

            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.WHITE.getIndex());
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.GREEN.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            int rowIdx = 0;
            Row titleRow = sheet.createRow(rowIdx++);
            titleRow.createCell(0).setCellValue(data.getTitle());
            titleRow.getCell(0).setCellStyle(titleStyle);

            Row subtitleRow = sheet.createRow(rowIdx++);
            subtitleRow.createCell(0).setCellValue(data.getSubtitle());

            rowIdx++; // blank row

            if (!data.getSummary().isEmpty()) {
                for (var entry : data.getSummary().entrySet()) {
                    Row r = sheet.createRow(rowIdx++);
                    r.createCell(0).setCellValue(entry.getKey());
                    r.createCell(1).setCellValue(entry.getValue());
                }
                rowIdx++; // blank row
            }

            Row headerRow = sheet.createRow(rowIdx++);
            for (int i = 0; i < data.getColumns().size(); i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(data.getColumns().get(i));
                cell.setCellStyle(headerStyle);
            }

            for (var dataRow : data.getRows()) {
                Row row = sheet.createRow(rowIdx++);
                for (int i = 0; i < dataRow.size(); i++) {
                    row.createCell(i).setCellValue(dataRow.get(i));
                }
            }

            for (int i = 0; i < data.getColumns().size(); i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return out.toByteArray();
        } catch (Exception e) {
            throw new ReportGenerationException("Échec de la génération du rapport Excel", e);
        }
    }
}