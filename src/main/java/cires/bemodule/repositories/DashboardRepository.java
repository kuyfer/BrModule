package cires.bemodule.repositories;

import cires.bemodule.dtos.internal.MonthlyAttendanceStat;
import cires.bemodule.dtos.internal.SessionPresenceStat;
import cires.bemodule.dtos.internal.SessionSummaryRow;
import cires.bemodule.dtos.internal.UnvalidatedDayRow;
import cires.bemodule.enums.AccountStatus;
import cires.bemodule.enums.AttendanceStatus;
import cires.bemodule.enums.TrainingSessionStatus;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import cires.bemodule.dtos.internal.TodayAttendanceRow;
import cires.bemodule.enums.AttendanceSlot;
import java.time.LocalDate;

import java.util.List;


@Repository
@RequiredArgsConstructor
public class DashboardRepository {

    private final EntityManager em;

    // ─── GLOBAL COUNTS ────────────────────────────────────────────────────────

    public long countSessions() {
        return em.createQuery("SELECT COUNT(s) FROM TrainingSession s", Long.class)
                .getSingleResult();
    }

    public long countSessionsByStatus(TrainingSessionStatus status) {
        return em.createQuery(
                        "SELECT COUNT(s) FROM TrainingSession s WHERE s.status = :status", Long.class)
                .setParameter("status", status)
                .getSingleResult();
    }

    public long countParticipants() {
        return em.createQuery(
                        "SELECT COUNT(p) FROM Participant P", Long.class)
                .getSingleResult();
    }

    public long countActiveTrainers() {
        return em.createQuery(
                        "SELECT COUNT(t) FROM Trainer t", Long.class)
                .getSingleResult();
    }

    public long countActiveUsers() {
        return em.createQuery(
                        "SELECT COUNT(u) FROM User u WHERE u.accountStatus = :status", Long.class)
                .setParameter("status", AccountStatus.ACTIVE)
                .getSingleResult();
    }

    // ─── GLOBAL ATTENDANCE RATES ──────────────────────────────────────────────

    /**
     * Returns [presentCount, lateCount, absentCount, totalCount]
     * across all attendance records.
     */
    public long[] globalAttendanceCounts() {
        List<Object[]> rows = em.createQuery(
                        """
                        SELECT a.status, COUNT(a)
                        FROM Attendance a
                        GROUP BY a.status
                        """, Object[].class)
                .getResultList();

        long present = 0, late = 0, absent = 0, justified = 0;
        for (Object[] row : rows) {
            AttendanceStatus status = (AttendanceStatus) row[0];
            long count = (Long) row[1];
            switch (status) {
                case PRESENT            -> present   = count;
                case LATE               -> late      = count;
                case ABSENT             -> absent    = count;
                case JUSTIFIED_ABSENCE  -> justified = count;
            }
        }
        long total = present + late + absent + justified;
        return new long[]{present, late, absent, justified, total};
    }

    // ─── MONTHLY TREND ────────────────────────────────────────────────────────

    public List<MonthlyAttendanceStat> attendanceTrendLast6Months() {
        // Native query — JPQL doesn't support date_trunc
        List<Object[]> rows = (List<Object[]>) em.createNativeQuery(
                        """
                        SELECT
                            TO_CHAR(a.date, 'YYYY-MM')               AS year_month,
                            COUNT(*)                                  AS total_slots,
                            COUNT(*) FILTER (WHERE a.status IN ('PRESENT','LATE')) AS present_count
                        FROM attendance a
                        WHERE a.date >= CURRENT_DATE - INTERVAL '6 months'
                        GROUP BY TO_CHAR(a.date, 'YYYY-MM')
                        ORDER BY year_month
                        """)
                .getResultList();

        return rows.stream()
                .map(r -> {
                    long total   = ((Number) r[1]).longValue();
                    long present = ((Number) r[2]).longValue();
                    return MonthlyAttendanceStat.builder()
                            .yearMonth((String) r[0])
                            .totalSlots(total)
                            .presentCount(present)
                            .presenceRate(total > 0
                                    ? Math.round((double) present / total * 1000.0) / 10.0
                                    : 0.0)
                            .build();
                })
                .toList();
    }

    // ─── TOP SESSIONS BY PRESENCE RATE ────────────────────────────────────────

    public List<SessionPresenceStat> topSessionsByPresenceRate(int limit) {
        List<Object[]> rows = em.createQuery(
                        """
                        SELECT
                            s.id,
                            s.title,
                            CONCAT(u.firstName, ' ', u.lastName),
                            COUNT(a),
                            SUM(CASE WHEN a.status IN ('PRESENT','LATE') THEN 1 ELSE 0 END),
                            COUNT(DISTINCT sp.participant.id)
                        FROM TrainingSession s
                        JOIN s.trainer t
                        JOIN t.user u
                        JOIN SessionParticipant sp ON sp.trainingSession = s
                        LEFT JOIN Attendance a ON a.session = s
                        WHERE s.status = 'COMPLETED'
                        GROUP BY s.id, s.title, u.firstName, u.lastName
                        HAVING COUNT(a) > 0
                        ORDER BY (SUM(CASE WHEN a.status IN ('PRESENT','LATE') THEN 1 ELSE 0 END) * 1.0 / COUNT(a)) DESC
                        """, Object[].class)
                .setMaxResults(limit)
                .getResultList();

        return rows.stream()
                .map(r -> {
                    long total   = ((Number) r[3]).longValue();
                    long present = ((Number) r[4]).longValue();
                    return SessionPresenceStat.builder()
                            .sessionId((Long) r[0])
                            .sessionTitle((String) r[1])
                            .trainerName((String) r[2])
                            .totalParticipants(((Number) r[5]).longValue())
                            .presenceRate(total > 0
                                    ? Math.round((double) present / total * 1000.0) / 10.0
                                    : 0.0)
                            .build();
                })
                .toList();
    }

    // ─── UPCOMING / ONGOING SESSIONS ─────────────────────────────────────────

    public List<SessionSummaryRow> upcomingSessionsThisWeek() {
        LocalDate today   = LocalDate.now();
        LocalDate endWeek = today.plusDays(7);

        return em.createQuery(
                        """
                        SELECT s.id, s.title,
                               CONCAT(u.firstName, ' ', u.lastName),
                               s.startDate, s.endDate, s.status,
                               COUNT(sp)
                        FROM TrainingSession s
                        JOIN s.trainer t JOIN t.user u
                        LEFT JOIN SessionParticipant sp ON sp.trainingSession = s
                        WHERE s.status = 'SCHEDULED'
                        AND s.startDate BETWEEN :today AND :endWeek
                        GROUP BY s.id, s.title, u.firstName, u.lastName, s.startDate, s.endDate, s.status
                        ORDER BY s.startDate
                        """, Object[].class)
                .setParameter("today",   today)
                .setParameter("endWeek", endWeek)
                .getResultList()
                .stream()
                .map(this::toSessionSummaryRow)
                .toList();
    }

    public List<SessionSummaryRow> ongoingSessions() {
        return em.createQuery(
                        """
                        SELECT s.id, s.title,
                               CONCAT(u.firstName, ' ', u.lastName),
                               s.startDate, s.endDate, s.status,
                               COUNT(sp)
                        FROM TrainingSession s
                        JOIN s.trainer t JOIN t.user u
                        LEFT JOIN SessionParticipant sp ON sp.trainingSession = s
                        WHERE s.status = 'ONGOING'
                        GROUP BY s.id, s.title, u.firstName, u.lastName, s.startDate, s.endDate, s.status
                        ORDER BY s.startDate
                        """, Object[].class)
                .getResultList()
                .stream()
                .map(this::toSessionSummaryRow)
                .toList();
    }

    // ─── UNVALIDATED DAYS ─────────────────────────────────────────────────────

    public List<UnvalidatedDayRow> pendingValidations() {
        return (List<UnvalidatedDayRow>) em.createNativeQuery(
                        """
                        SELECT
                            s.id                                    AS session_id,
                            s.title                                 AS session_title,
                            a.date                                  AS date,
                            CONCAT(u.first_name, ' ', u.last_name)  AS trainer_name,
                            COUNT(DISTINCT sp.participant_id) -
                            COUNT(DISTINCT a.participant_id)         AS unmarked_count
                        FROM training_sessions s
                        JOIN trainers t     ON t.id = s.trainer_id
                        JOIN users u        ON u.id = t.user_id
                        JOIN session_participants sp ON sp.session_id = s.id
                        LEFT JOIN attendance a ON a.session_id = s.id
                            AND a.validated = false
                        WHERE s.status = 'ONGOING'
                        GROUP BY s.id, s.title, a.date, u.first_name, u.last_name
                        HAVING COUNT(DISTINCT sp.participant_id) >
                               COUNT(DISTINCT a.participant_id)
                        ORDER BY a.date
                        """)
                .getResultList()
                .stream()
                .map(r -> {
                    Object[] row = (Object[]) r;
                    return UnvalidatedDayRow.builder()
                            .sessionId(((Number) row[0]).longValue())
                            .sessionTitle((String) row[1])
                            .date(((java.sql.Date) row[2]).toLocalDate())
                            .trainerName((String) row[3])
                            .unmarkedCount(((Number) row[4]).longValue())
                            .build();
                })
                .toList();
    }

    // ─── TRAINER DASHBOARD ────────────────────────────────────────────────────

    public long countSessionsByTrainer(Long trainerId, TrainingSessionStatus status) {
        return em.createQuery(
                        """
                        SELECT COUNT(s) FROM TrainingSession s
                        WHERE s.trainer.id = :trainerId
                        AND s.status = :status
                        """, Long.class)
                .setParameter("trainerId", trainerId)
                .setParameter("status", status)
                .getSingleResult();
    }

    public List<TodayAttendanceRow> todayAttendanceForTrainer(Long trainerId) {
        LocalDate today = LocalDate.now();

        return em.createQuery(
                        """
                        SELECT s.id, s.title, COUNT(DISTINCT sp.participant.id)
                        FROM TrainingSession s
                        JOIN SessionParticipant sp ON sp.trainingSession = s
                        WHERE s.trainer.id = :trainerId
                        AND s.status = 'ONGOING'
                        AND s.startDate <= :today AND s.endDate >= :today
                        GROUP BY s.id, s.title
                        """, Object[].class)
                .setParameter("trainerId", trainerId)
                .setParameter("today", today)
                .getResultList()
                .stream()
                .map(r -> {
                    Long   sid        = (Long)   r[0];
                    String title      = (String) r[1];
                    long   total      = ((Number) r[2]).longValue();

                    long amMarked = countMarkedForSlot(sid, today, AttendanceSlot.AM);
                    long pmMarked = countMarkedForSlot(sid, today, AttendanceSlot.PM);
                    boolean amValidated = isSlotValidated(sid, today, AttendanceSlot.AM);
                    boolean pmValidated = isSlotValidated(sid, today, AttendanceSlot.PM);

                    return TodayAttendanceRow.builder()
                            .sessionId(sid)
                            .sessionTitle(title)
                            .totalParticipants(total)
                            .amMarkedCount(amMarked)
                            .pmMarkedCount(pmMarked)
                            .amValidated(amValidated)
                            .pmValidated(pmValidated)
                            .build();
                })
                .toList();
    }

    public double averagePresenceRateForTrainer(Long trainerId) {
        Long[] counts = em.createQuery(
                        """
                        SELECT
                            SUM(CASE WHEN a.status IN ('PRESENT','LATE') THEN 1 ELSE 0 END),
                            COUNT(a)
                        FROM Attendance a
                        WHERE a.session.trainer.id = :trainerId
                        """, Object[].class)
                .setParameter("trainerId", trainerId)
                .getResultList()
                .stream()
                .map(r -> new Long[]{((Number) r[0]).longValue(), ((Number) r[1]).longValue()})
                .findFirst()
                .orElse(new Long[]{0L, 0L});

        return counts[1] > 0
                ? Math.round((double) counts[0] / counts[1] * 1000.0) / 10.0
                : 0.0;
    }

    public List<SessionSummaryRow> recentSessionsForTrainer(Long trainerId) {
        return em.createQuery(
                        """
                        SELECT s.id, s.title,
                               CONCAT(u.firstName, ' ', u.lastName),
                               s.startDate, s.endDate, s.status,
                               COUNT(sp)
                        FROM TrainingSession s
                        JOIN s.trainer t JOIN t.user u
                        LEFT JOIN SessionParticipant sp ON sp.trainingSession = s
                        WHERE s.trainer.id = :trainerId
                        GROUP BY s.id, s.title, u.firstName, u.lastName,
                                 s.startDate, s.endDate, s.status
                        ORDER BY s.startDate DESC
                        """, Object[].class)
                .setParameter("trainerId", trainerId)
                .setMaxResults(10)
                .getResultList()
                .stream()
                .map(this::toSessionSummaryRow)
                .toList();
    }

    // ─── AUDIT DASHBOARD ──────────────────────────────────────────────────────

//    public long countAuditEventsThisWeek() {
//        return em.createQuery(
//                        """
//                        SELECT COUNT(a) FROM AuditLog a
//                        WHERE a.createdAt >= :since
//                        """, Long.class)
//                .setParameter("since", LocalDateTime.now().minusDays(7))
//                .getSingleResult();
//    }

//    public List<AuditEventRow> recentAuditEvents(int limit) {
//        return em.createQuery(
//                        """
//                        SELECT a.userEmail, a.action, a.module, a.entityType, a.createdAt
//                        FROM AuditLog a
//                        ORDER BY a.createdAt DESC
//                        """, Object[].class)
//                .setMaxResults(limit)
//                .getResultList()
//                .stream()
//                .map(r -> AuditEventRow.builder()
//                        .userEmail((String) r[0])
//                        .action((String) r[1])
//                        .module((String) r[2])
//                        .entityType((String) r[3])
//                        .createdAt((LocalDateTime) r[4])
//                        .build())
//                .toList();
//    }

//    public List<UserActivityRow> topActiveUsers(int limit) {
//        return em.createQuery(
//                        """
//                        SELECT a.userId, a.userEmail, COUNT(a)
//                        FROM AuditLog a
//                        WHERE a.createdAt >= :since
//                        GROUP BY a.userId, a.userEmail
//                        ORDER BY COUNT(a) DESC
//                        """, Object[].class)
//                .setParameter("since", LocalDateTime.now().minusDays(30))
//                .setMaxResults(limit)
//                .getResultList()
//                .stream()
//                .map(r -> UserActivityRow.builder()
//                        .userId((Long) r[0])
//                        .email((String) r[1])
//                        .eventCount((Long) r[2])
//                        .build())
//                .toList();
//    }

    // ─── PRIVATE HELPERS ──────────────────────────────────────────────────────

    private long countMarkedForSlot(Long sessionId, LocalDate date, AttendanceSlot slot) {
        return em.createQuery(
                        """
                        SELECT COUNT(a) FROM Attendance a
                        WHERE a.session.id = :sid AND a.date = :date AND a.slot = :slot
                        """, Long.class)
                .setParameter("sid",  sessionId)
                .setParameter("date", date)
                .setParameter("slot", slot)
                .getSingleResult();
    }

    private boolean isSlotValidated(Long sessionId, LocalDate date, AttendanceSlot slot) {
        Long count = em.createQuery(
                        """
                        SELECT COUNT(a) FROM Attendance a
                        WHERE a.session.id = :sid AND a.date = :date
                        AND a.slot = :slot AND a.validated = true
                        """, Long.class)
                .setParameter("sid",  sessionId)
                .setParameter("date", date)
                .setParameter("slot", slot)
                .getSingleResult();
        return count > 0;
    }

    private SessionSummaryRow toSessionSummaryRow(Object[] r) {
        return SessionSummaryRow.builder()
                .sessionId((Long) r[0])
                .title((String) r[1])
                .trainerName((String) r[2])
                .startDate(r[3] instanceof java.sql.Date d ? d.toLocalDate() : (LocalDate) r[3])
                .endDate(r[4] instanceof java.sql.Date d ? d.toLocalDate() : (LocalDate) r[4])
                .status((TrainingSessionStatus) r[5])
                .participantCount(((Number) r[6]).longValue())
                .build();
    }
}