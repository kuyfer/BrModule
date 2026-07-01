package cires.bemodule.dtos.internal;

import cires.bemodule.enums.TrainingSessionStatus;
import lombok.Getter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

/**
 * Internal summary row for a training session.
 */
@Getter @Builder @NoArgsConstructor @AllArgsConstructor
public class SessionSummaryRow {
    private Long                  sessionId;
    private String                title;
    private String                trainerName;
    private LocalDate startDate;
    private LocalDate             endDate;
    private TrainingSessionStatus status;
    private long                  participantCount;
}