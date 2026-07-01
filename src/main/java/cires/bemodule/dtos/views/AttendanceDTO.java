package cires.bemodule.dtos.views;

import cires.bemodule.enums.AttendanceSlot;
import cires.bemodule.enums.AttendanceStatus;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * View projection for attendance records.
 */
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class AttendanceDTO {
    private Long id;
    private Long sessionId;
    private String sessionTitle;
    private Long participantId;
    private String participantName;
    private LocalDate date;
    private AttendanceSlot slot;
    private AttendanceStatus status;
    private boolean validated;
    private LocalDateTime validatedAt;
    private String delayReason;
    private String comment;
    private String correctionReason;
}