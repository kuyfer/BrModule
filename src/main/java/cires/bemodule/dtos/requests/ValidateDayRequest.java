package cires.bemodule.dtos.requests;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

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
