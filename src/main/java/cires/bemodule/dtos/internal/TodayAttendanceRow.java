package cires.bemodule.dtos.internal;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Internal row for today's attendance status per session.
 */
@Getter @Builder @NoArgsConstructor @AllArgsConstructor
public class TodayAttendanceRow {
    private Long      sessionId;
    private String    sessionTitle;
    private boolean   amValidated;
    private boolean   pmValidated;
    private long      totalParticipants;
    private long      amMarkedCount;
    private long      pmMarkedCount;
}