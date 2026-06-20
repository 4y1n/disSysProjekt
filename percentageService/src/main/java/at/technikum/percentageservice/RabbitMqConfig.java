package at.technikum.percentageservice;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    public static final String USAGE_UPDATES_QUEUE = "usage.updates";

    @Bean
    public Queue usageUpdatesQueue() {
        return QueueBuilder
                .durable(USAGE_UPDATES_QUEUE)
                .build();
    }
}