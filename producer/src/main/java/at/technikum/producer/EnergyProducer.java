package at.technikum.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rabbitmq.client.Channel;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Random;

public class EnergyProducer {

    private static final String QUEUE_NAME = "energy.messages";

    // Bei voller Sonne (1000 W/m²) erzeugt die Solaranlage ca. 0.05 kWh pro Minute.
    private static final double KWH_AT_FULL_SUN = 0.05;

    private final Channel channel;
    private final WeatherClient weatherClient;
    private final ObjectMapper mapper;
    private final Random random = new Random();

    public EnergyProducer(Channel channel, WeatherClient weatherClient) {
        this.channel = channel;
        this.weatherClient = weatherClient;

        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule());
    }

    // kWh Wert berechnen
    private double calculateKwh(double radiation) {
        double base = (radiation / 1000.0) * KWH_AT_FULL_SUN;

        // für den zufallsfaktor
        double noise = (random.nextDouble() * 0.2) - 0.1;
        double kwh = base * (1.0 + noise);

        // nicht negativ
        return Math.max(0.0, kwh);
    }

    // Nachricht als Json an RabbitMQ
    public void sendOneMessage() throws Exception {
        double radiation = weatherClient.getCurrentRadiation();
        double kwh = calculateKwh(radiation);

        EnergyMessage message = new EnergyMessage(
                "PRODUCER",
                "COMMUNITY",
                kwh,
                LocalDateTime.now()
        );

        String json = mapper.writeValueAsString(message);
        channel.basicPublish("", QUEUE_NAME, null,
                json.getBytes(StandardCharsets.UTF_8));

        System.out.println("Producer -> " + json + "   (radiation: " + radiation + " W/m^2)");
    }
}