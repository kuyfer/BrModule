package cires.bemodule.services;

import cires.bemodule.dtos.NotificationDTO;
import cires.bemodule.dtos.requests.RegisterRequest;
import cires.bemodule.entities.Notification;
import cires.bemodule.entities.Trainer;
import cires.bemodule.entities.TrainingSession;
import cires.bemodule.enums.NotificationStatus;
import cires.bemodule.enums.NotificationType;
import cires.bemodule.exceptions.controllerexceptions.NotificationNotFoundException;
import cires.bemodule.mappers.NotificationMapper;
import cires.bemodule.models.EmailPayload;
import cires.bemodule.repositories.NotificationRepository;
import cires.bemodule.specifications.NotificationSpecifications;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final EmailQueueProducer emailQueueProducer;

    public NotificationService(NotificationRepository notificationRepository, NotificationMapper notificationMapper, EmailQueueProducer emailQueueProducer) {
        this.notificationRepository = notificationRepository;
        this.notificationMapper = notificationMapper;
        this.emailQueueProducer = emailQueueProducer;
    }

    public NotificationDTO findById(Long id) {
        logger.info("Finding notification by id: {}", id);
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Notification not found with id: {}", id);
                    return new NotificationNotFoundException(id);
                });
        NotificationDTO dto = notificationMapper.toNotificationDto(notification);
        logger.info("Found notification with id: {}", id);
        return dto;
    }

    public List<NotificationDTO> findAll(NotificationType type, NotificationStatus status, String email) {
        logger.info("Finding all notifications with filters - type: {}, status: {}, email: {}", type, status, email);
        Specification<Notification> spec = Specification
                .where(NotificationSpecifications.hasType(type))
                .and(NotificationSpecifications.hasStatus(status))
                .and(NotificationSpecifications.toEmailEquals(email));

        List<Notification> notifications = notificationRepository.findAll(spec);
        List<NotificationDTO> dtos = notifications.stream()
                .map(notificationMapper::toNotificationDto)
                .toList();
        logger.info("Found {} notifications matching filters", dtos.size());
        return dtos;
    }

    public void sendTrainerAssignmentEmail(TrainingSession session, Trainer trainer) {
        logger.debug("Sending trainer assignment email to trainer: {} for session: {}", trainer.getUser().getEmail(), session.getTitle());
        Map<String, Object> model = new HashMap<>();
        model.put("trainerName", trainer.getUser().getFirstName());
        model.put("sessionTitle", session.getTitle());
        model.put("sessionDescription", session.getDescription());
        model.put("startDate", session.getStartDate().toString());
        model.put("endDate", session.getEndDate().toString());
        model.put("location", session.getLocation());
        model.put("mode", session.getMode());
        //model.put("subsidiary", session.getSubsidiary());

        EmailPayload payload = new EmailPayload(
                trainer.getUser().getEmail(),
                "Trainer Assignment",
                "trainer-assignement",
                model
        );
        emailQueueProducer.queueEmail(payload, NotificationType.TRAINER_ASSIGNMENT);
        logger.debug("Trainer assignment email queued for: {}", trainer.getUser().getEmail());

    }
    public void sendSessionCancelledEmail(TrainingSession session, String reason) {
        logger.debug("Sending session cancellation email to trainer: {} for session: {}", session.getTrainer().getUser().getEmail(), session.getTitle());
        Map<String, Object> model = new HashMap<>();
        model.put("sessionTitle", session.getTitle());
        model.put("sessionDescription", session.getDescription());
        model.put("startDate", session.getStartDate().toString());
        model.put("endDate", session.getEndDate().toString());
        model.put("location", session.getLocation());
        model.put("mode", session.getMode());
        model.put("cancellationReason", reason);

        EmailPayload payload = new EmailPayload(
                session.getTrainer().getUser().getEmail(),
                "Session Cancelled",
                "session-cancellation",
                model
        );
        emailQueueProducer.queueEmail(payload, NotificationType.SESSION_CANCELLATION);
        logger.debug("Session cancellation email queued for: {}", session.getTrainer().getUser().getEmail());
    }


    public void sendRegistrationEmail(RegisterRequest request) {
        logger.debug("Sending registration email to: {}", request.getEmail());
        Map<String, Object> model = new HashMap<>();
        model.put("recipientName", request.getFirstName());
        model.put("username", request.getUsername());
        model.put("body", "Hope you are doing well.");

        EmailPayload payload = new EmailPayload(
                request.getEmail(),
                "Welcome " + request.getUsername(),
                "welcome",
                model
        );

        emailQueueProducer.queueEmail(payload, NotificationType.ACCOUNT_CREATION);
        logger.debug("Registration email queued for: {}", request.getEmail());
    }

    public void sendResetEmail(String email, String token) {
        logger.debug("Sending password reset email to: {}", email);
        Map<String, Object> model = new HashMap<>();
        model.put("token", token);
        model.put("email", email);

        EmailPayload payload = new EmailPayload(
                email,
                "Password Reset Request",
                "password-reset",
                model
        );

        emailQueueProducer.queueEmail(payload, NotificationType.PASSWORD_RESET);
        logger.info("Password reset email queued for: {}", email);
    }

    public void sendReminderEmail(TrainingSession session) {
        logger.debug("Sending session reminder email to trainer: {} for session: {}", session.getTrainer().getUser().getEmail(), session.getTitle());
        Map<String, Object> model = new HashMap<>();
        model.put("sessionTitle", session.getTitle());
        model.put("sessionDescription", session.getDescription());
        model.put("startDate", session.getStartDate().toString());
        model.put("endDate", session.getEndDate().toString());
        model.put("location", session.getLocation());
        model.put("mode", session.getMode());

        EmailPayload payload = new EmailPayload(
                session.getTrainer().getUser().getEmail(),
                "Session reminder",
                "session-reminder",
                model
        );
        emailQueueProducer.queueEmail(payload, NotificationType.SESSION_REMINDER);
        logger.debug("Session reminder email queued for: {}", session.getTrainer().getUser().getEmail());
    }



    }