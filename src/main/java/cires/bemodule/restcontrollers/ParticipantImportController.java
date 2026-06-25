package cires.bemodule.restcontrollers;

import cires.bemodule.dtos.ImportResult;
import cires.bemodule.services.ParticipantImportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/participants/import")
@RequiredArgsConstructor
public class ParticipantImportController {

    private final ParticipantImportService importService;

    @GetMapping("/template")
    @PreAuthorize("hasAuthority('import:execute')")
    public ResponseEntity<byte[]> downloadTemplate() {
        byte[] template = importService.generateExcelTemplate();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"participant_import_template.xlsx\"")
                .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .contentLength(template.length)
                .body(template);
    }

    @PostMapping(value = "/csv", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('import:execute')")
    public ResponseEntity<ImportResult> importCsv(
            @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(importService.importFromCsv(file));
    }

    @PostMapping(value = "/excel", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('import:execute')")
    public ResponseEntity<ImportResult> importExcel(
            @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(importService.importFromExcel(file));
    }
}