package cires.bemodule.services;

import cires.bemodule.dtos.internal.SessionSummaryRow;
import cires.bemodule.dtos.internal.UnvalidatedDayRow;
import cires.bemodule.dtos.responses.ExecutiveDashboardResponse;
import cires.bemodule.dtos.responses.OperationalDashboardResponse;
import cires.bemodule.dtos.responses.TrainerDashboardResponse;
import cires.bemodule.entities.Trainer;
import cires.bemodule.enums.TrainingSessionStatus;
import cires.bemodule.exceptions.controllerexceptions.TrainerNotFoundException;
import cires.bemodule.repositories.DashboardRepository;
import cires.bemodule.repositories.TrainerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardService {

    private final DashboardRepository dashboardRepository;
    private final TrainerRepository trainerRepository;

    // ─── EXECUTIVE ────────────────────────────────────────────────────────────

    public ExecutiveDashboardResponse getExecutiveDashboard() {
        long[] attendance = dashboardRepository.globalAttendanceCounts();
        long present   = attendance[0];
        long late      = attendance[1];
        long absent    = attendance[2];
        long total     = attendance[4];

        double presenceRate = total > 0 ? round((double)(present + late) / total * 100) : 0;
        double absenceRate  = total > 0 ? round((double) absent           / total * 100) : 0;
        double lateRate     = total > 0 ? round((double) late             / total * 100) : 0;

        return ExecutiveDashboardResponse.builder()
                .totalSessions(dashboardRepository.countSessions())
                .totalParticipants(dashboardRepository.countParticipants())
                .totalTrainers(dashboardRepository.countActiveTrainers())
                .totalActiveUsers(dashboardRepository.countActiveUsers())
                .scheduledSessions(dashboardRepository.countSessionsByStatus(TrainingSessionStatus.SCHEDULED))
                .ongoingSessions(dashboardRepository.countSessionsByStatus(TrainingSessionStatus.ONGOING))
                .completedSessions(dashboardRepository.countSessionsByStatus(TrainingSessionStatus.COMPLETED))
                .cancelledSessions(dashboardRepository.countSessionsByStatus(TrainingSessionStatus.CANCELLED))
                .postponedSessions(dashboardRepository.countSessionsByStatus(TrainingSessionStatus.POSTPONED))
                .globalPresenceRate(presenceRate)
                .globalAbsenceRate(absenceRate)
                .globalLateRate(lateRate)
                .attendanceTrend(dashboardRepository.attendanceTrendLast6Months())
                .topSessions(dashboardRepository.topSessionsByPresenceRate(5))
                .recentSessions(dashboardRepository.upcomingSessionsThisWeek())
                .build();
    }

    // ─── OPERATIONAL ──────────────────────────────────────────────────────────

    public OperationalDashboardResponse getOperationalDashboard() {
        List<UnvalidatedDayRow> pending  = dashboardRepository.pendingValidations();
        List<SessionSummaryRow> ongoing  = dashboardRepository.ongoingSessions();
        List<SessionSummaryRow> upcoming = dashboardRepository.upcomingSessionsThisWeek();

        long participantsToday = ongoing.stream()
                .mapToLong(SessionSummaryRow::getParticipantCount)
                .sum();

        return OperationalDashboardResponse.builder()
                .ongoingSessionsCount(ongoing.size())
                .pendingValidationCount(pending.size())
                .participantsInTrainingToday(participantsToday)
                .ongoingSessions(ongoing)
                .upcomingThisWeek(upcoming)
                .pendingValidations(pending)
                .build();
    }

    // ─── TRAINER ──────────────────────────────────────────────────────────────

    public TrainerDashboardResponse getTrainerDashboard(Long userId) {
        Trainer trainer = trainerRepository.findByUserId(userId)
                .orElseThrow(() -> new TrainerNotFoundException(userId));
        Long trainerId = trainer.getId();

        return TrainerDashboardResponse.builder()
                .totalSessionsAssigned(
                        dashboardRepository.countSessionsByTrainer(trainerId, TrainingSessionStatus.SCHEDULED) +
                                dashboardRepository.countSessionsByTrainer(trainerId, TrainingSessionStatus.ONGOING)   +
                                dashboardRepository.countSessionsByTrainer(trainerId, TrainingSessionStatus.COMPLETED))
                .completedSessions(
                        dashboardRepository.countSessionsByTrainer(trainerId, TrainingSessionStatus.COMPLETED))
                .ongoingSessions(
                        dashboardRepository.countSessionsByTrainer(trainerId, TrainingSessionStatus.ONGOING))
                .upcomingSessions(
                        dashboardRepository.countSessionsByTrainer(trainerId, TrainingSessionStatus.SCHEDULED))
                .todayAttendance(
                        dashboardRepository.todayAttendanceForTrainer(trainerId))
                .mySessions(
                        dashboardRepository.recentSessionsForTrainer(trainerId))
                .mySessionsAveragePresenceRate(
                        dashboardRepository.averagePresenceRateForTrainer(trainerId))
                .build();
    }

    // ─── AUDIT ────────────────────────────────────────────────────────────────

//    public AuditDashboardResponse getAuditDashboard() {
//        return AuditDashboardResponse.builder()
//                .totalAuditEventsThisWeek(dashboardRepository.countAuditEventsThisWeek())
//                .recentAuditEvents(dashboardRepository.recentAuditEvents(20))
//                .topActiveUsers(dashboardRepository.topActiveUsers(5))
//                .build();
//    }

    // ─── HELPER ───────────────────────────────────────────────────────────────

    private double round(double value) {
        return Math.round(value * 10.0) / 10.0;
    }
}