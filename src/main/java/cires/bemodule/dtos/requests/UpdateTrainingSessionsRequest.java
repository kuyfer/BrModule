package cires.bemodule.dtos.requests;

import cires.bemodule.enums.TrainingSessionStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter@AllArgsConstructor @NoArgsConstructor
public class UpdateTrainingSessionsRequest {

    private TrainingSessionStatus status;

    private String reason;

}
