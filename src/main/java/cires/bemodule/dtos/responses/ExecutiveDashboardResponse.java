package cires.bemodule.dtos.responses;

import cires.bemodule.dtos.internal.MonthlyAttendanceStat;
import cires.bemodule.dtos.internal.SessionPresenceStat;
import cires.bemodule.dtos.internal.SessionSummaryRow;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExecutiveDashboardResponse {
    // Top KPI cards
    private long totalSessions;
    private long totalParticipants;
    private long totalTrainers;
    private long totalActiveUsers;

    // Session breakdown by status
    private long scheduledSessions;
    private long ongoingSessions;
    private long completedSessions;
    private long cancelledSessions;
    private long postponedSessions;

    // Attendance
    private double globalPresenceRate;      // % across all completed sessions
    private double globalAbsenceRate;
    private double globalLateRate;

    // Trends — last 6 months, one entry per month
    private List<MonthlyAttendanceStat> attendanceTrend;

    // Top 5 sessions by presence rate
    private List<SessionPresenceStat> topSessions;

    // Recent sessions (last 5)
    private List<SessionSummaryRow> recentSessions;

}
