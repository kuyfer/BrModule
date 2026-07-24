package cires.bemodule.dtos.responses;

import cires.bemodule.enums.AttendanceStatus;
import lombok.*;

/**
 * Entry for a single participant inside a slot grid.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SlotEntry {
    private Long             participantId;
    private String           participantFullName;
    private AttendanceStatus status;
    private String           delayReason;
    private String           comment;
    private boolean          validated;
    private Long             attendanceId;
}