package cires.bemodule.dtos;

import cires.bemodule.entities.Subsidiary;
import cires.bemodule.enums.TrainingSessionMode;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTrainingSessionRequest {

    @NotBlank(message = "Trainer id is required")
    private Long trainerId;

    @NotBlank(message = "Start date is required")
    private LocalDateTime startDate;

    @NotBlank(message = "End date is required")
    private LocalDateTime endDate;

    @NotBlank(message = "title is required")
    private String title;

    private String description;

    private String location;

    @Enumerated(EnumType.STRING)
    private TrainingSessionMode mode;

}
