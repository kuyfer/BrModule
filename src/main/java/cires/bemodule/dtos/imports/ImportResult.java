package cires.bemodule.dtos.imports;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Result object returned to the client after a bulk import operation.
 */
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