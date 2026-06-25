package cires.bemodule.dtos.internal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @Builder @NoArgsConstructor @AllArgsConstructor
public class MonthlyAttendanceStat {
    private String yearMonth;          // "2026-01"
    private double presenceRate;
    private long   totalSlots;
    private long   presentCount;
}