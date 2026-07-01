package cires.bemodule.dtos.requests;

import cires.bemodule.enums.TrainingSessionMode;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Request to create a new training session.
 */
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class CreateTrainingSessionRequest {

    @NotNull
    private Long trainerId;

    /**
     * Start date and time (must be present or future).
     */
    @NotNull
    @FutureOrPresent
    private LocalDateTime startDate;

    /**
     * End date and time (must be strictly after startDate).
     */
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