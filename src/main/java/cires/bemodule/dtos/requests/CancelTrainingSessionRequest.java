package cires.bemodule.dtos.requests;

import lombok.*;

/**
 * Request to cancel a training session with a reason.
 */
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class CancelTrainingSessionRequest {
    private String reason;
}