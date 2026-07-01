package cires.bemodule.dtos.imports;

import lombok.*;

/**
 * One error entry per bad row in the import file.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImportRowError {
    private int    row;
    private String email;
    private String reason;
}