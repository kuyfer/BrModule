package cires.bemodule.services;

import cires.bemodule.dtos.ReportData;
import cires.bemodule.dtos.ReportRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportDataService reportDataService;
    private final PdfReportGenerator pdfReportGenerator;
    private final ExcelReportGenerator excelReportGenerator;

    public byte[] generate(ReportRequest request) {
        log.info("Generating report for format: {}, request: {}", request.getFormat(), request);
        ReportData data = reportDataService.build(request);
        byte[] report = switch (request.getFormat()) {
            case PDF -> {
                log.debug("Generating PDF report");
                yield pdfReportGenerator.generate(data);
            }
            case EXCEL -> {
                log.debug("Generating Excel report");
                yield excelReportGenerator.generate(data);
            }
        };
        log.info("Report generated successfully, size: {} bytes", report.length);
        return report;
    }
}