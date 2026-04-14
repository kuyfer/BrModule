package cires.bemodule.services;

// code source:: https://medium.com/@AlexanderObregon/making-a-simple-email-queue-with-spring-boot-and-rabbitmq-566a188a9e67

import cires.bemodule.configs.RabbitMQConfig;
import cires.bemodule.models.EmailPayload;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class EmailQueueProducer {
    private final RabbitTemplate rabbitTemplate;

    public EmailQueueProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void queueEmail(EmailPayload payload) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                RabbitMQConfig.ROUTING_KEY, payload
        );
    }
}
