package cires.bemodule.dtos.internal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Internal monthly attendance statistics.
 */
@Getter @Builder @NoArgsConstructor @AllArgsConstructor
public class MonthlyAttendanceStat {
    private String yearMonth;
    private double presenceRate;
    private long   totalSlots;
    private long   presentCount;
}