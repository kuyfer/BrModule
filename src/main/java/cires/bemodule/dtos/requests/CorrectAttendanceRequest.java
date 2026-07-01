package cires.bemodule.dtos.requests;

import cires.bemodule.enums.AttendanceStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * Request to correct an existing attendance record (admin override).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CorrectAttendanceRequest {

    @NotNull(message = "New status is required")
    private AttendanceStatus status;

    private String delayReason;
    private String comment;

    @NotBlank(message = "Correction reason is mandatory for admin overrides")
    private String correctionReason;
}