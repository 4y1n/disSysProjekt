package at.technikum.usageservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class EnergyConsumer {

    private final Channel channel;
    private final String inputQueue;
    private final UsageService usageService;
    private final ObjectMapper mapper;

    public EnergyConsumer(Channel channel, String inputQueue, UsageService usageService) {
        this.channel = channel;
        this.inputQueue = inputQueue;
        this.usageService = usageService;

        // Jackson mit LocalDateTime-Unterstützung
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule());
    }

    public void start() throws IOException {

        // DeliverCallback wird für jede ankommende Nachricht aufgerufen
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String json = new String(delivery.getBody(), StandardCharsets.UTF_8);
            try {
                EnergyMessage message = mapper.readValue(json, EnergyMessage.class);
                usageService.process(message);
                System.out.println("Verarbeitet: " + message.getType()
                        + " " + message.getKwh() + " kWh @ " + message.getDatetime());
            } catch (Exception e) {
                // Kaputte Nachricht darf den Consumer nicht abstürzen lassen
                System.out.println("Fehler bei Nachricht: " + json + " -> " + e.getMessage());
            }
        };

        //  bestätigt die Nachricht automatisch
        channel.basicConsume(inputQueue, true, deliverCallback, consumerTag -> {});
    }
}