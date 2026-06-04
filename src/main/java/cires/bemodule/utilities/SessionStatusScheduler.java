package cires.bemodule.utilities;

import cires.bemodule.entities.TrainingSession;
import cires.bemodule.enums.TrainingSessionStatus;
import cires.bemodule.repositories.TrainingSessionRepository;
import cires.bemodule.services.NotificationService;
import cires.bemodule.services.TrainingSessionService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class SessionStatusScheduler {

    Logger log = LoggerFactory.getLogger(SessionStatusScheduler.class);

    private final TrainingSessionRepository sessionRepository;
    private final TrainingSessionService sessionService;
    private final NotificationService notificationService;

    public SessionStatusScheduler(TrainingSessionRepository sessionRepository, TrainingSessionService sessionService, NotificationService notificationService) {
        this.sessionRepository = sessionRepository;
        this.sessionService = sessionService;
        this.notificationService = notificationService;
    }

    @Scheduled(fixedRate = 5 * 60 * 1000) // runs every 5 minutes
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

    @Scheduled(cron = "0 0 * * * *") // every hour on the hour
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
