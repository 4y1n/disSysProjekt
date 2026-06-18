package at.technikum.percentageservice;


import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
    public static final String PROCESSED_EXCHANGE = "energy.processed";
    public static final String PERCENTAGE_QUEUE   = "energy.percentage";

    @Bean
    public FanoutExchange processedExchange() {
        return new FanoutExchange(PROCESSED_EXCHANGE);
    }

    @Bean
    public Queue percentageQueue() {
        return QueueBuilder.durable(PERCENTAGE_QUEUE).build();
    }

    @Bean
    public Binding percentageBinding(Queue percentageQueue, FanoutExchange processedExchange) {
        return BindingBuilder.bind(percentageQueue).to(processedExchange);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}
