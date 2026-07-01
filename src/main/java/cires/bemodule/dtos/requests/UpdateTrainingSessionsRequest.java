package cires.bemodule.dtos.requests;

import cires.bemodule.enums.TrainingSessionStatus;
import lombok.*;

/**
 * Request to update the status of a training session.
 */
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class UpdateTrainingSessionsRequest {

    private TrainingSessionStatus status;

    private String reason;

}