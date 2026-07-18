package cires.bemodule.configs;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ configuration that declares the queues, exchanges, and bindings
 * used by the application for email and export processing.
 * <p>
 * Two direct exchanges are defined – one for email messages and one for
 * export jobs – each with a durable queue.  Messages are sent as JSON and
 * converted automatically by a {@link JacksonJsonMessageConverter}.
 * </p>
 * <p>
 * The email queue consumer is implemented in
 * {@link cires.bemodule.listeners.EmailQueueConsumer}.
 * </p>
 *
 * @see <a href="https://medium.com/@AlexanderObregon/making-a-simple-email-queue-with-spring-boot-and-rabbitmq-566a188a9e67">
 *          Making a simple email queue with Spring Boot and RabbitMQ</a>
 */
@EnableRabbit
@Configuration
public class RabbitMQConfig {

    /** Name of the queue used for outgoing emails. */
    public static final String EMAIL_QUEUE_NAME = "emailQueue";
    /** Name of the direct exchange for email messages. */
    public static final String EMAIL_EXCHANGE_NAME = "emailExchange";
    /** Routing key used when publishing email messages. */
    public static final String EMAIL_ROUTING_KEY = "emailRoutingKey";

    /** Name of the queue used for export jobs. */
    public static final String EXPORT_QUEUE_NAME = "exportQueue";
    /** Name of the direct exchange for export jobs. */
    public static final String EXPORT_EXCHANGE_NAME = "exportExchange";
    /** Routing key used when publishing export jobs. */
    public static final String EXPORT_ROUTING_KEY = "exportRoutingKey";

    /**
     * Declares a durable email queue.
     *
     * @return the email queue bean
     */
    @Bean
    public Queue emailQueue() {
        return new Queue(EMAIL_QUEUE_NAME, true);
    }

    /**
     * Declares a durable export queue.
     *
     * @return the export queue bean
     */
    @Bean
    public Queue exportQueue() {
        return new Queue(EXPORT_QUEUE_NAME, true);
    }

    /**
     * Declares a durable, non‑auto‑delete direct exchange for email messages.
     *
     * @return the email exchange bean
     */
    @Bean
    public DirectExchange emailExchange() {
        return new DirectExchange(EMAIL_EXCHANGE_NAME, true, false);
    }

    /**
     * Declares a durable, non‑auto‑delete direct exchange for export jobs.
     *
     * @return the export exchange bean
     */
    @Bean
    public DirectExchange exportExchange() {
        return new DirectExchange(EXPORT_EXCHANGE_NAME, true, false);
    }

    /**
     * Binds the email queue to the email exchange using the email routing key.
     *
     * @param emailQueue    the email queue bean (injected by Spring)
     * @param emailExchange the email exchange bean (injected by Spring)
     * @return the binding
     */
    @Bean
    public Binding emailBinding(Queue emailQueue, DirectExchange emailExchange) {
        return BindingBuilder
                .bind(emailQueue)
                .to(emailExchange)
                .with(EMAIL_ROUTING_KEY);
    }

    /**
     * Binds the export queue to the export exchange using the email routing key.
     * <p>
     * <b>Note:</b> This binding uses {@link #EMAIL_ROUTING_KEY} – verify that
     * the export producer uses the correct routing key.
     * </p>
     *
     * @param exportQueue    the export queue bean (injected by Spring)
     * @param exportExchange the export exchange bean (injected by Spring)
     * @return the binding
     */
    @Bean
    public Binding exportBinding(Queue exportQueue, DirectExchange exportExchange) {
        return BindingBuilder
                .bind(exportQueue)
                .to(exportExchange)
                .with(EMAIL_ROUTING_KEY);
    }

    /**
     * Provides a JSON message converter so that RabbitMQ messages are
     * serialized/deserialized as JSON.
     *
     * @return a {@link JacksonJsonMessageConverter}
     */
    @Bean
    public MessageConverter jsonConverter() {
        return new JacksonJsonMessageConverter();
    }

}