package cires.bemodule.dtos.responses;

import cires.bemodule.dtos.internal.SessionSummaryRow;
import cires.bemodule.dtos.internal.TodayAttendanceRow;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter @Builder @NoArgsConstructor @AllArgsConstructor
public class TrainerDashboardResponse {
    // My stats
    private long totalSessionsAssigned;
    private long completedSessions;
    private long ongoingSessions;
    private long upcomingSessions;

    // Today's work
    private List<TodayAttendanceRow> todayAttendance;  // sessions today + slots to fill

    // My sessions list
    private List<SessionSummaryRow> mySessions;

    // My overall presence rate across all my sessions
    private double mySessionsAveragePresenceRate;
}