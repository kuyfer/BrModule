package cires.bemodule.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

// What comes back to the client after import
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImportResult {
    private int              totalRows;
    private int              successCount;
    private int              skippedCount;
    private int              errorCount;
    private List<ImportRowError> errors;
}