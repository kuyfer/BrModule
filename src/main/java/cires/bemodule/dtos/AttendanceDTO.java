package cires.bemodule.dtos;

import cires.bemodule.entities.SessionParticipant;
import cires.bemodule.enums.AttendanceStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data @AllArgsConstructor @NoArgsConstructor
public class AttendanceDTO {

    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private AttendanceStatus attendanceStatus;
    private SessionParticipant sessionParticipant;

}
