package cires.bemodule.dtos.responses;

import cires.bemodule.entities.Trainer;
import cires.bemodule.enums.TrainingSessionMode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CancelTrainingSessionResponse {

    private Long id;
    private String title;
    private String description;
    private String location;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private TrainingSessionMode mode;
    private Trainer trainer;
    private String reason;
}
