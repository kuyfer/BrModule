package cires.bemodule.dtos.views;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Summary view for attendance statistics.
 */
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class AttendanceSummaryDTO {
    private Long sessionId;
    private String period;
    private int total;
    private long present;
    private long absent;
    private long late;
    private long justified;
    private boolean validated;
}