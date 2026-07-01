package cires.bemodule.dtos.views;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Summary view for a session participant.
 */
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class SessionParticipantSummaryDTO {
    private Long id;
    private Long participantId;
    private String participantName;
    private String participantEmail;
}