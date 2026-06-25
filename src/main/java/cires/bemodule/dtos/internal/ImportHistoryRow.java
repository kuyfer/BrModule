package cires.bemodule.dtos.internal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter @Builder @NoArgsConstructor @AllArgsConstructor
public class ImportHistoryRow {
    private Long          id;
    private String        importedBy;
    private int           totalRows;
    private int           successCount;
    private int           errorCount;
    private LocalDateTime importedAt;
}