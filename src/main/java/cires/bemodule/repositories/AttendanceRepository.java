package cires.bemodule.repositories;

import cires.bemodule.entities.Attendance;
import cires.bemodule.enums.AttendanceSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long>, JpaSpecificationExecutor<Attendance> {


    List<Attendance> findBySessionIdAndPeriod(Long sessionId, String period);

    Optional<Attendance> findBySessionIdAndParticipantIdAndDateAndSlot(
            Long sessionId, Long participantId, LocalDate startTime, AttendanceSlot slot);

    List<Attendance> findAllBySessionIdOrderByDateAscSlotAsc(Long sessionId);

    List<Attendance> findAllBySessionIdAndParticipantIdOrderByDateAscSlotAsc(
            Long sessionId, Long participantId);

    boolean existsByParticipantId(Long participantId);

    @Query("""
        SELECT DISTINCT a.participant.id
        FROM Attendance a
        WHERE a.session.id = :sessionId
        AND a.date = :date
    """)
    List<Long> findParticipantIdsMarkedForDay(
            @Param("sessionId") Long sessionId,
            @Param("date") LocalDate startTime);

    @Query("""
        SELECT COUNT(a) > 0
        FROM Attendance a
        WHERE a.session.id = :sessionId
        AND a.date = :date
        AND a.validated = true
    """)
    boolean isDayValidatedForSession(
            @Param("sessionId") Long sessionId,
            @Param("date") LocalDate startTime);

    @Modifying
    @Query("""
        UPDATE Attendance a
        SET a.validated    = true,
            a.validatedBy  = :trainerId,
            a.validatedAt  = :validatedAt
        WHERE a.session.id = :sessionId
        AND a.date         = :date
    """)
    int markDayAsValidated(
            @Param("sessionId")  Long sessionId,
            @Param("date")       LocalDate startDate,
            @Param("trainerId")  Long trainerId,
            @Param("validatedAt") LocalDateTime validatedAt);
}
