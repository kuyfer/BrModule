package cires.bemodule.listeners;

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

/**
 * RabbitMQ consumer that listens on the email queue and processes
 * {@link EmailPayload} messages.
 * <p>
 * For each incoming payload, a Thymeleaf template is rendered to HTML and
 * sent as an email via {@link JavaMailSender}.  The corresponding
 * {@code Notification} entity is updated to {@code SENT} on success or
 * {@code FAILED} on error, with the failure reason recorded.
 * </p>
 * <p>
 * Implementation inspired by the Spring Boot + RabbitMQ email queue example:
 * <a href="https://medium.com/@AlexanderObregon/making-a-simple-email-queue-with-spring-boot-and-rabbitmq-566a188a9e67">
 * Making a simple email queue with Spring Boot and RabbitMQ</a>.
 * </p>
 *
 * @see RabbitMQConfig
 * @see EmailPayload
 * @see NotificationRepository
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class EmailQueueConsumer {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;
    private final NotificationRepository notificationRepository;

    /**
     * Main listener method invoked when a message is pulled from the email queue.
     * <p>
     * The method performs the following steps:
     * <ol>
     *   <li>Creates a Thymeleaf {@link Context} with the template model.</li>
     *   <li>Renders the HTML content using the template name.</li>
     *   <li>Sends the HTML email via {@link #sendHtmlMessage(String, String, String)}.</li>
     *   <li>Updates the notification status to {@code SENT} on success.</li>
     *   <li>In case of any exception, marks the notification as {@code FAILED}
     *       and records the error message.</li>
     * </ol>
     *
     * @param payload the email payload containing recipient, subject, template
     *                name, template variables, and optional notification ID
     */
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

    /**
     * Updates the notification entity identified by {@code id} with the
     * appropriate status.
     *
     * @param id        the notification ID (may be {@code null} if no
     *                  notification is linked)
     * @param errorMsg  error message for a failure; {@code null} for success
     */
    private void updateNotificationStatus(Long id, String errorMsg) {
        notificationRepository.findById(id).ifPresent(notif -> {
            notif.setNotificationStatus(NotificationStatus.SENT);
            if (errorMsg != null) {
                notif.setFailureReason(errorMsg);
                notif.setNotificationStatus(NotificationStatus.FAILED);
            }
            notificationRepository.save(notif);
        });
    }

    /**
     * Builds and sends an HTML email.
     *
     * @param to       recipient email address
     * @param subject  email subject
     * @param htmlBody HTML content of the email
     * @throws MessagingException if the email cannot be sent
     */
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