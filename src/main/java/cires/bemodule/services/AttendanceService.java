package cires.bemodule.services;

import cires.bemodule.dtos.requests.AttendanceRequest;
import cires.bemodule.entities.Attendance;
import cires.bemodule.repositories.AttendanceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;

    public List<Attendance> getAttendanceForSession(Long sessionId, String period) {
        List<Attendance> records = attendanceRepository.findBySessionIdAndPeriod(sessionId, period);


        return null;
    }
    
    
}
