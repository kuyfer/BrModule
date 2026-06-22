package cires.bemodule.dtos.responses;

import lombok.*;
// Per-participant summary for a session
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantAttendanceSummary {
    private Long   participantId;
    private String participantFullName;
    private Long   sessionId;
    private String sessionTitle;
    private long   totalSlots;
    private long   presentCount;
    private long   lateCount;
    private long   absentCount;
    private long   justifiedCount;
    private long   unmarkedCount;
    private double presenceRate;   // percentage: (present + late) / total * 100
}