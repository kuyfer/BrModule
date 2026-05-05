package cires.bemodule.dtos;

import cires.bemodule.entities.SessionParticipant;
import cires.bemodule.enums.AttendanceStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class AttendanceDTO {

    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private AttendanceStatus attendanceStatus;
    private SessionParticipant sessionParticipant;

}
