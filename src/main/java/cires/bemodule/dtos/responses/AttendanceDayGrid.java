package cires.bemodule.dtos.responses;

import lombok.*;
import java.time.LocalDate;

/**
 * One row (one day) in the attendance grid.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceDayGrid {
    private LocalDate date;
    private boolean   dayValidated;
    private int       totalEnrolled;
    private SlotGrid  amSlot;
    private SlotGrid  pmSlot;
}