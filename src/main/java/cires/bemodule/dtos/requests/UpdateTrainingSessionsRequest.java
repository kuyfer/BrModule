package cires.bemodule.dtos.requests;

import cires.bemodule.enums.TrainingSessionStatus;
import lombok.*;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class UpdateTrainingSessionsRequest {

    private TrainingSessionStatus status;

    private String reason;

}
