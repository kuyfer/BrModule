package cires.bemodule.listeners;

// code source:: https://medium.com/@AlexanderObregon/making-a-simple-email-queue-with-spring-boot-and-rabbitmq-566a188a9e67

import cires.bemodule.configs.RabbitMQConfig;
import cires.bemodule.models.EmailPayload;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailQueueConsumer {
    private final JavaMailSender mailSender;

    public EmailQueueConsumer(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void handleEmailMessage(EmailPayload payload) {
        SimpleMailMessage message =  new SimpleMailMessage();
        message.setTo(payload.getTo());
        message.setFrom("mmhimer0@gmail.com");
        message.setSubject(payload.getSubject());
        message.setText(payload.getBody());

        mailSender.send(message);
    }
}
