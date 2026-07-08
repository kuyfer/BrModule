package cires.bemodule.dtos.requests;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class PostponeTrainingSessionRequest {

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

    private String reason;
}
