package cires.bemodule.dtos.responses;

import cires.bemodule.entities.Trainer;
import cires.bemodule.enums.TrainingSessionMode;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Builder @AllArgsConstructor @NoArgsConstructor
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
