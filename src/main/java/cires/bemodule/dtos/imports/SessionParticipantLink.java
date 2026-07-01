package cires.bemodule.dtos.imports;

import lombok.*;

/**
 * Row that passed all validation and is ready for database insertion.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SessionParticipantLink {
    private Long   sessionId;
    private String email;
}