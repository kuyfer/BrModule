package cires.bemodule.listeners;

/**
 * code source::
 * https://medium.com/@AlexanderObregon/making-a-simple-email-queue-with-spring-boot-and-rabbitmq-566a188a9e67
 */

import cires.bemodule.configs.RabbitMQConfig;
import cires.bemodule.enums.NotificationStatus;
import cires.bemodule.models.EmailPayload;
import cires.bemodule.repositories.NotificationRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Slf4j
@RequiredArgsConstructor
@Component
public class EmailQueueConsumer {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;
    private final NotificationRepository notificationRepository;

    @RabbitListener(queues = RabbitMQConfig.EMAIL_QUEUE_NAME)
    public void handleEmailMessage(EmailPayload payload) {
        try {
            Context context = new Context();
            context.setVariables(payload.getTemplateModel());

            String htmlBody = templateEngine.process(
                    payload.getTemplateName(), context
            );

            sendHtmlMessage(payload.getTo(), payload.getSubject(), htmlBody);

            updateNotificationStatus(payload.getNotificationId(), null);

        } catch (Exception e) {
            updateNotificationStatus(payload.getNotificationId(), e.getMessage());
            log.error("Failed to process email for {}: {}", payload.getTo(), e.getMessage(), e);
        }
    }

    private void updateNotificationStatus(Long id, String errorMsg) {
        notificationRepository.findById(id).ifPresent(notif -> {
            notif.setNotificationStatus(NotificationStatus.SENT);
            if (errorMsg != null){
                notif.setFailureReason(errorMsg);
                notif.setNotificationStatus(NotificationStatus.FAILED);
            }
            notificationRepository.save(notif);
        });
    }

    private void sendHtmlMessage(String to, String subject, String htmlBody)
            throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);
        helper.setFrom("mmhimer0@gmail.com");
        mailSender.send(message);
    }
}
