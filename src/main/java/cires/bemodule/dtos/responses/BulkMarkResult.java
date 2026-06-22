package cires.bemodule.dtos.responses;

import lombok.*;

import java.util.List;

// Returned after bulk mark
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