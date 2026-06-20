package at.technikum.usageservice;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    // Eingehende Queue: hier landen PRODUCER/USER Nachrichten
    public static final String ENERGY_MESSAGES_QUEUE = "energy.messages";

    // Ausgehende Exchange: hier wird "neue Daten verfügbar" gemeldet
    public static final String PROCESSED_EXCHANGE = "energy.processed";

    @Bean
    public Queue energyMessagesQueue() {
        return QueueBuilder.durable(ENERGY_MESSAGES_QUEUE).build();
    }

    @Bean
    public FanoutExchange processedExchange() {
        return new FanoutExchange(PROCESSED_EXCHANGE);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
