package cires.bemodule.dtos.requests;


import cires.bemodule.enums.AttendanceStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BulkAttendanceEntry {

    @NotNull(message = "Participant ID is required")
    private Long participantId;

    @NotNull(message = "Status is required")
    private AttendanceStatus status;

    private String delayReason;
    private String comment;
}