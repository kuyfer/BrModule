package cires.bemodule.dtos2;

import cires.bemodule.enums.TrainingSessionMode;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data @NoArgsConstructor @AllArgsConstructor
public class CreateTrainingSessionRequest {

    @NotNull
    private Long trainerId;

    @NotNull
    @FutureOrPresent
    private LocalDateTime startDate;

    @NotNull
    @Future
    private LocalDateTime endDate;

    @NotBlank(message = "title is required")
    private String title;

    private String description;

    private String location;

    @Enumerated(EnumType.STRING)
    private TrainingSessionMode mode;

}
