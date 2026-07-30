package cires.bemodule.dtos.views;

import cires.bemodule.enums.ExportFormat;
import cires.bemodule.enums.ExportStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @Builder @AllArgsConstructor @NoArgsConstructor
public class ExportHistoryDTO {

    private Long id;
    private String exportedBy;
    private LocalDateTime exportedAt;
    private String entityType;
    private ExportFormat exportFormat;
    private ExportStatus exportStatus;
    private String fileName;
}