package cires.bemodule.dtos.requests;

import cires.bemodule.enums.AttendanceSlot;
import cires.bemodule.enums.AttendanceStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

/**
 * Request to mark attendance for a single participant.
 */
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class MarkAttendanceRequest {
    @NotNull(message = "Session ID is required")
    private Long sessionId;

    @NotNull(message = "Participant ID is required")
    private Long participantId;

    @NotNull(message = "Date is required")
    private LocalDate date;

    @NotNull(message = "Slot is required (AM or PM)")
    private AttendanceSlot slot;

    @NotNull(message = "Status is required")
    private AttendanceStatus status;

    private String delayReason;  // required only when status = LATE
    private String comment;
}