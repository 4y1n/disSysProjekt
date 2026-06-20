package at.technikum.percentageservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class PercentageConsumer {

    private final Channel channel;
    private final String updatesQueue;
    private final PercentageService percentageService;
    private final ObjectMapper mapper;

    public PercentageConsumer(Channel channel, String updatesQueue, PercentageService percentageService) {
        this.channel = channel;
        this.updatesQueue = updatesQueue;
        this.percentageService = percentageService;

        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule());
    }

    public void start() throws IOException {

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String json = new String(delivery.getBody(), StandardCharsets.UTF_8);
            try {
                UsageUpdateMessage message = mapper.readValue(json, UsageUpdateMessage.class);
                percentageService.process(message);
            } catch (Exception e) {
                // Kaputte Nachricht darf den Consumer nicht abstürzen lassen
                System.out.println("Fehler bei Nachricht: " + json + " -> " + e.getMessage());
            }
        };

        channel.basicConsume(updatesQueue, true, deliverCallback, consumerTag -> {});
    }
}