package cires.bemodule.dtos.responses;

import cires.bemodule.enums.AttendanceStatus;
import cires.bemodule.enums.AttendanceSlot;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceResponse {
    private Long             id;
    private Long             sessionId;
    private String           sessionTitle;
    private Long             participantId;
    private LocalDate date;
    private AttendanceSlot   slot;
    private AttendanceStatus status;
    private String           delayReason;
    private String           comment;
    private boolean          validated;
    private LocalDateTime validatedAt;
    private String           correctionReason;
    private LocalDateTime    createdAt;
    private LocalDateTime    updatedAt;
}