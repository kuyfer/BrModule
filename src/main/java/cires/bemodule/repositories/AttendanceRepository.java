package cires.bemodule.repositories;

import cires.bemodule.entities.Attendance;
import cires.bemodule.enums.AttendanceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.Set;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    @Override
    Optional<Attendance> findById(Long aLong);

    Set<Attendance> findByAttendanceStatus(AttendanceStatus attendanceStatus);

}
