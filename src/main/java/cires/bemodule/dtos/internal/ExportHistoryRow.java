package cires.bemodule.dtos.internal;


import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Internal row representing an export history entry.
 */

@Getter @Builder @NoArgsConstructor @AllArgsConstructor
public class ExportHistoryRow {
    private Long          id;
    private String        requestedBy;
    private String        module;
    private String        format;
    private int           rowCount;
    private LocalDateTime requestedAt;
}