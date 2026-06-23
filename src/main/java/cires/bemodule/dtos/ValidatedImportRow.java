package cires.bemodule.dtos;

import lombok.*;

// Internal — row that passed all validation, ready for DB insert
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidatedImportRow {
    private String firstName;
    private String lastName;
    private String email;          // already normalized
    private String phone;
    private Long   sessionId;      // resolved from formationRaw
}