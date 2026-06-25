package cires.bemodule.dtos.internal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter @Builder @NoArgsConstructor @AllArgsConstructor
public class UnvalidatedDayRow {
    private Long      sessionId;
    private String    sessionTitle;
    private LocalDate date;
    private String    trainerName;
    private long      unmarkedCount;
}