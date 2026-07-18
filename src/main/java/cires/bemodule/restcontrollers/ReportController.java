package cires.bemodule.restcontrollers;

import cires.bemodule.dtos.ReportRequest;
import cires.bemodule.services.ReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;
    private static final DateTimeFormatter FILE_TS = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    @PostMapping("/generate")
   // @PreAuthorize("hasAuthority('report:generate') ")
    public ResponseEntity<byte[]> generate(@Valid @RequestBody ReportRequest request) {
        byte[] content = reportService.generate(request);

        boolean isPdf = request.getFormat().name().equals("PDF");
        MediaType mediaType = isPdf
                ? MediaType.APPLICATION_PDF
                : MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        String extension = isPdf ? "pdf" : "xlsx";
        String filename = "rapport_" + request.getReportType().name().toLowerCase()
                + "_" + java.time.LocalDateTime.now().format(FILE_TS) + "." + extension;

        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment().filename(filename).build().toString())
                .body(content);
    }
}