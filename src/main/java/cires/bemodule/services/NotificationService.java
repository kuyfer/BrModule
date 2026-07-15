package cires.bemodule.services;

import cires.bemodule.dtos.views.NotificationDTO;
import cires.bemodule.dtos.requests.RegisterRequest;
import cires.bemodule.entities.Notification;
import cires.bemodule.entities.Trainer;
import cires.bemodule.entities.TrainingSession;
import cires.bemodule.entities.User;
import cires.bemodule.enums.NotificationStatus;
import cires.bemodule.enums.NotificationType;
import cires.bemodule.exceptions.notfound.NotificationNotFoundException;
import cires.bemodule.mappers.NotificationMapper;
import cires.bemodule.models.EmailPayload;
import cires.bemodule.repositories.NotificationRepository;
import cires.bemodule.specifications.NotificationSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final EmailQueueProducer emailQueueProducer;

    public NotificationDTO findById(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new NotificationNotFoundException(id));
        return notificationMapper.toNotificationDto(notification);
    }

    public Page<NotificationDTO> findAll(NotificationType type, NotificationStatus status, String email, Pageable pageable) {
        Specification<Notification> spec = Specification
                .where(NotificationSpecifications.hasType(type))
                .and(NotificationSpecifications.hasStatus(status))
                .and(NotificationSpecifications.toEmailEquals(email));

        Page<Notification> notificationPage = notificationRepository.findAll(spec, pageable);
        return notificationPage.map(notificationMapper::toNotificationDto);
    }

    public List<NotificationDTO> findAll(NotificationType type, NotificationStatus status, String email) {
        Page<NotificationDTO> page = findAll(type, status, email, Pageable.unpaged());
        return page.getContent();
    }

    public void sendTrainerAssignmentEmail(TrainingSession session, Trainer trainer) {
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
    }

    public void sendPasswordSetupEmail(User user, String setupLink) {
        Map<String, Object> model = new HashMap<>();
        model.put("recipientName", user.getFirstName());
        model.put("username", user.getUsername());
        model.put("setupLink", setupLink);
        model.put("expiryHours", 24);

        EmailPayload payload = new EmailPayload(
                user.getEmail(),
                "Set Up Your Password",
                "password-setup",
                model
        );

        emailQueueProducer.queueEmail(payload, NotificationType.PASSWORD_SETUP);
    }

    public void sendSessionCancelledEmail(TrainingSession session, String reason) {
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
    }

    public void sendRegistrationEmail(RegisterRequest request) {
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
    }

    public void sendResetEmail(String email, String token) {
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
    }

    public void sendReminderEmail(TrainingSession session) {
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
    }

    public void sendSessionPostponedEmail(TrainingSession session, String reason, LocalDateTime newStartDate, LocalDateTime newEndDate) {
        Map<String, Object> model = new HashMap<>();
        model.put("sessionTitle", session.getTitle());
        model.put("sessionDescription", session.getDescription());
        model.put("oldStartDate", session.getStartDate().toString());
        model.put("oldEndDate", session.getEndDate().toString());
        model.put("newStartDate", newStartDate.toString());
        model.put("newEndDate", newEndDate.toString());
        model.put("location", session.getLocation());
        model.put("mode", session.getMode());
        model.put("postponementReason", reason);

        EmailPayload payload = new EmailPayload(
                session.getTrainer().getUser().getEmail(),
                "Session Postponed",
                "session-postponed",
                model
        );
        emailQueueProducer.queueEmail(payload, NotificationType.SESSION_POSTPONED);
    }
}