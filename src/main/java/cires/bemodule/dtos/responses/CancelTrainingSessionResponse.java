package cires.bemodule.dtos.responses;

import cires.bemodule.dtos.views.TrainerDTO;
import cires.bemodule.enums.TrainingSessionMode;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Response containing cancellation details of a training session.
 */
@Getter @Builder @AllArgsConstructor @NoArgsConstructor
public class CancelTrainingSessionResponse {
    private Long id;
    private String title;
    private String description;
    private String location;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private TrainingSessionMode mode;
    private TrainerDTO trainer;
    private String reason;
}