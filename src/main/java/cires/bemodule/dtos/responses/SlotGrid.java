package cires.bemodule.dtos.responses;

import lombok.*;

import java.util.List;

/**
 * Grid of attendance entries for a single slot (AM or PM).
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SlotGrid {
    private long            presentCount;
    private long            lateCount;
    private long            absentCount;
    private long            justifiedCount;
    private long            unmarkedCount;
    private List<SlotEntry> entries;
}