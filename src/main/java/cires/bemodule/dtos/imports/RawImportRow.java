package cires.bemodule.dtos.imports;

import lombok.*;

/**
 * Raw parsed row from the import file, before any validation.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RawImportRow {
    private int    rowNumber;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String formationRaw;
}