package cires.bemodule.repositories;

import cires.bemodule.entities.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {


    List<Attendance> findBySessionIdAndPeriod(Long sessionId, String period);

}
