package cires.bemodule.dtos.imports;

import lombok.*;

/**
 * Internal link between a session and a participant (used for bulk linking).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidatedImportRow {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private Long   sessionId;
}