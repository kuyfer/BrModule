package cires.bemodule.dtos;

import cires.bemodule.enums.ExportFormat;
import cires.bemodule.enums.ExportStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class ExportHistoryDTO {

    private Long id;
    private ExportStatus exportStatus;
    private ExportFormat exportFormat;

}
