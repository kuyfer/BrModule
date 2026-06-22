package cires.bemodule.dtos.requests;

import cires.bemodule.enums.AttendanceSlot;
import cires.bemodule.enums.AttendanceStatus;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceFilterRequest {
    private Long       sessionId;
    private Long       participantId;
    private AttendanceStatus status;
    private AttendanceSlot   slot;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateFrom;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateTo;
}