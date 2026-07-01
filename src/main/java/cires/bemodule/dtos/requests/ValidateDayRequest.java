package cires.bemodule.dtos.requests;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

/**
 * Request to validate a specific day of attendance for a session.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidateDayRequest {

    @NotNull
    private Long sessionId;

    @NotNull
    private LocalDate date;
}