package cires.bemodule.dtos.responses;

import cires.bemodule.dtos.internal.ImportHistoryRow;
import cires.bemodule.dtos.internal.SessionSummaryRow;
import cires.bemodule.dtos.internal.UnvalidatedDayRow;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter @Builder @AllArgsConstructor @NoArgsConstructor
public class OperationalDashboardResponse {
    // Right now
    private long ongoingSessionsCount;
    private long pendingValidationCount;   // days not yet validated by trainer
    private long participantsInTrainingToday;

    // This week
    private List<SessionSummaryRow> upcomingThisWeek;
    private List<SessionSummaryRow> ongoingSessions;

    // Unvalidated days — needs action
    private List<UnvalidatedDayRow> pendingValidations;

    // Import activity (last 5 imports)
    private List<ImportHistoryRow> recentImports;
}
