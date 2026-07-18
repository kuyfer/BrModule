package cires.bemodule.utilities;

import cires.bemodule.entities.TrainingSession;
import cires.bemodule.enums.TrainingSessionStatus;
import cires.bemodule.repositories.TrainingSessionRepository;
import cires.bemodule.services.NotificationService;
import cires.bemodule.services.TrainingSessionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Scheduled component responsible for automating the lifecycle of
 * {@link TrainingSession} instances.
 * <p>
 * Three recurring tasks are performed:
 * <ol>
 *   <li><b>Start sessions</b> – sessions whose {@code startDate} has passed
 *       are moved from {@code SCHEDULED} to {@code ONGOING}.</li>
 *   <li><b>Complete sessions</b> – sessions whose {@code endDate} has passed
 *       are moved from {@code ONGOING} to {@code COMPLETED}.</li>
 *   <li><b>Send reminders</b> – for sessions starting within the next 24
 *       hours, a reminder email is sent to the assigned trainer.</li>
 * </ol>
 * <p>
 * All tasks are wrapped in try‑catch blocks so that a failure on one session
 * does not affect the processing of other sessions.
 * </p>
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class SessionStatusScheduler {

    private final TrainingSessionRepository sessionRepository;
    private final TrainingSessionService sessionService;
    private final NotificationService notificationService;

    /**
     * Transitions all {@code SCHEDULED} sessions whose {@code startDate} is
     * earlier than or equal to the current moment into {@code ONGOING}.
     * <p>
     * Runs every 5 minutes. If a session's status change fails, the error is
     * logged and the scheduler continues with the next session.
     * </p>
     */
    @Scheduled(fixedRate = 5 * 60 * 1000)
    public void startScheduledSessions() {
        List<TrainingSession> due = sessionRepository
                .findByStatusAndStartDateLessThanEqual(
                        TrainingSessionStatus.SCHEDULED,
                        LocalDateTime.now()
                );

        for (TrainingSession session : due) {
            try {
                sessionService.changeStatus(session.getId(), TrainingSessionStatus.ONGOING);
                log.info("Session auto-started [id={}]", session.getId());
            } catch (Exception e) {
                log.error("Failed to auto-start session [id={}]: {}", session.getId(), e.getMessage());
            }
        }
    }

    /**
     * Completes all {@code ONGOING} sessions whose {@code endDate} is earlier
     * than or equal to the current moment by moving them to
     * {@code COMPLETED}.
     * <p>
     * Runs every 5 minutes. Failures are logged individually without
     * interrupting the processing of the remaining sessions.
     * </p>
     */
    @Scheduled(fixedRate = 5 * 60 * 1000)
    public void completeOngoingSessions() {
        List<TrainingSession> due = sessionRepository
                .findByStatusAndEndDateLessThanEqual(
                        TrainingSessionStatus.ONGOING,
                        LocalDateTime.now()
                );

        for (TrainingSession session : due) {
            try {
                sessionService.changeStatus(session.getId(), TrainingSessionStatus.COMPLETED);
                log.info("Session auto-completed [id={}]", session.getId());
            } catch (Exception e) {
                log.error("Failed to auto-complete session [id={}]: {}", session.getId(), e.getMessage());
            }
        }
    }

    /**
     * Sends reminder emails to the trainers of all {@code SCHEDULED} sessions
     * that are due to start within the next 24 hours.
     * <p>
     * Runs every hour on the hour ({@code 0 0 * * * *}). The method is
     * transactional; if sending an email fails, the error is logged and the
     * scheduler continues with the next session without rolling back the
     * entire task.
     * </p>
     */
    @Scheduled(cron = "0 0 * * * *")
    @Transactional
    public void sendSessionReminders() {
        log.debug("Scheduler running: checking for upcoming session reminders");

        LocalDateTime from = LocalDateTime.now();
        LocalDateTime to   = LocalDateTime.now().plusHours(24);

        List<TrainingSession> upcoming = sessionRepository
                .findByStatusAndStartDateBetween(
                        TrainingSessionStatus.SCHEDULED, from, to
                );

        for (TrainingSession session : upcoming) {
            try {
                notificationService.sendReminderEmail(session);
                log.info("Reminder sent for session [id={}]", session.getId());
            } catch (Exception e) {
                log.error("Failed to send reminder for session [id={}]: {}", session.getId(), e.getMessage());
            }
        }
    }
}