package cires.bemodule.dtos.responses;

import cires.bemodule.dtos.internal.AuditEventRow;
import cires.bemodule.dtos.internal.ExportHistoryRow;
import cires.bemodule.dtos.internal.UserActivityRow;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter @Builder @NoArgsConstructor @AllArgsConstructor
public class AuditDashboardResponse {
    // Security events (last 24h)
    private long failedLoginCount;
    private long forbiddenAccessCount;

    // Activity (last 7 days)
    private long totalAuditEventsThisWeek;
    private List<AuditEventRow> recentAuditEvents;   // last 20 events

    // Export history
    private long totalExportsThisMonth;
    private List<ExportHistoryRow> recentExports;

    // Most active users (by audit events)
    private List<UserActivityRow> topActiveUsers;
}
