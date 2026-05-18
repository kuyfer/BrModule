package cires.bemodule.configs;

// code source https://medium.com/@AlexanderObregon/making-a-simple-email-queue-with-spring-boot-and-rabbitmq-566a188a9e67

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableRabbit
@Configuration
public class RabbitMQConfig {

    public static final String EMAIL_QUEUE_NAME = "emailQueue";
    public static final String EMAIL_EXCHANGE_NAME = "emailExchange";
    public static final String EMAIL_ROUTING_KEY = "emailRoutingKey";

    public static final String EXPORT_QUEUE_NAME = "exportQueue";
    public static final String EXPORT_EXCHANGE_NAME = "exportExchange";
    public static final String EXPORT_ROUTING_KEY = "exportRoutingKey";


    @Bean
   public Queue emailQueue() {
       return new Queue(EMAIL_QUEUE_NAME, true);
   }


    @Bean
    public Queue exportQueue() {
        return new Queue(EXPORT_QUEUE_NAME, true);
    }


    @Bean
    public DirectExchange emailExchange() {
        return new DirectExchange(EMAIL_EXCHANGE_NAME, true, false);
    }

    @Bean
    public DirectExchange exportExchange() {
        return new DirectExchange(EXPORT_EXCHANGE_NAME, true, false);
    }

    @Bean
    public Binding emailBinding(Queue emailQueue, DirectExchange emailExchange) {
        return BindingBuilder
                .bind(emailQueue)
                .to(emailExchange)
                .with(EMAIL_ROUTING_KEY);
    }

    @Bean
    public Binding exportBinding(Queue exportQueue, DirectExchange exportExchange) {
        return BindingBuilder
                .bind(exportQueue)
                .to(exportExchange)
                .with(EMAIL_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonConverter() {
        return new JacksonJsonMessageConverter();
    }

}
