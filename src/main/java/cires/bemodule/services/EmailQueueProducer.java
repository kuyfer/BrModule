package cires.bemodule.services;

// code source:: https://medium.com/@AlexanderObregon/making-a-simple-email-queue-with-spring-boot-and-rabbitmq-566a188a9e67

import cires.bemodule.configs.RabbitMQConfig;
import cires.bemodule.entities.Notification;
import cires.bemodule.enums.NotificationStatus;
import cires.bemodule.enums.NotificationType;
import cires.bemodule.models.EmailPayload;
import cires.bemodule.repositories.NotificationRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EmailQueueProducer {
    private final RabbitTemplate rabbitTemplate;
    private final NotificationRepository notificationRepository;

    public EmailQueueProducer(RabbitTemplate rabbitTemplate, NotificationRepository notificationRepository) {
        this.rabbitTemplate = rabbitTemplate;
        this.notificationRepository = notificationRepository;
    }

    public void queueEmail(EmailPayload payload, NotificationType type) {
        Notification notification = new Notification();
        notification.setToEmail(payload.getTo());
        notification.setSubject(payload.getSubject());
        notification.setNotificationStatus(NotificationStatus.PENDING);
        notification.setNotificationType(type);
        notification = notificationRepository.save(notification);

        payload.setNotificationId(notification.getId());

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                RabbitMQConfig.ROUTING_KEY,
                payload
        );
    }
}
