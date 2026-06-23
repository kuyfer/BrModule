package cires.bemodule.dtos;

import lombok.*;

// Internal — raw parsed row before any validation
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
    private String formationRaw;   // "42 - Java Bootcamp" or just "42"
}