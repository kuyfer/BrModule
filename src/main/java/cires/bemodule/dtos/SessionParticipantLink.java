package cires.bemodule.dtos;

import lombok.*;

// Internal — join table link built after participants are inserted
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SessionParticipantLink {
    private Long   sessionId;
    private String email;          // used to look up participant id via subquery
}