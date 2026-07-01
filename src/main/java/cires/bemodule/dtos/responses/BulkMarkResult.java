package cires.bemodule.dtos.responses;

import lombok.*;

import java.util.List;

/**
 * Result of a bulk attendance marking operation.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BulkMarkResult {
    private int              totalEntries;
    private int              successCount;
    private int              errorCount;
    private List<String> errors;
    private List<AttendanceResponse> results;
}