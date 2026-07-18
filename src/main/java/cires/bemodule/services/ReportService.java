package cires.bemodule.services;

import cires.bemodule.dtos.ReportData;
import cires.bemodule.dtos.ReportRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportDataService reportDataService;
    private final PdfReportGenerator pdfReportGenerator;
    private final ExcelReportGenerator excelReportGenerator;

    public byte[] generate(ReportRequest request) {
        ReportData data = reportDataService.build(request);
        return switch (request.getFormat()) {
            case PDF -> pdfReportGenerator.generate(data);
            case EXCEL -> excelReportGenerator.generate(data);
        };
    }
}