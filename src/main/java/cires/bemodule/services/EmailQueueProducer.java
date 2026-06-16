package cires.bemodule.services;

// code source:: https://medium.com/@AlexanderObregon/making-a-simple-email-queue-with-spring-boot-and-rabbitmq-566a188a9e67

import cires.bemodule.configs.RabbitMQConfig;
import cires.bemodule.entities.Notification;
import cires.bemodule.enums.NotificationStatus;
import cires.bemodule.enums.NotificationType;
import cires.bemodule.models.EmailPayload;
import cires.bemodule.repositories.NotificationRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class EmailQueueProducer {
    private static final Logger logger = LoggerFactory.getLogger(EmailQueueProducer.class);

    private final RabbitTemplate rabbitTemplate;
    private final NotificationRepository notificationRepository;

    public void queueEmail(EmailPayload payload, NotificationType type) {
        Notification notification = new Notification();
        notification.setToEmail(payload.getTo());
        notification.setSubject(payload.getSubject());
        notification.setNotificationStatus(NotificationStatus.PENDING);
        notification.setNotificationType(type);
        notification = notificationRepository.save(notification);

        payload.setNotificationId(notification.getId());

        logger.info("Queuing email with notification ID: {}, to: {}", notification.getId(), payload.getTo());

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EMAIL_EXCHANGE_NAME,
                RabbitMQConfig.EMAIL_ROUTING_KEY,
                payload
        );
    }
}