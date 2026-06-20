package at.technikum.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rabbitmq.client.Channel;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Random;

public class EnergyUser {

    private static final String QUEUE_NAME = "energy.messages";

    // Basis-Verbrauch pro Minute, fuer einen typischen Haushalt
    private static final double BASE_KWH = 0.010;     // 0.010 kWh/min  = 600 W Grundlast
    private static final double PEAK_KWH = 0.030;     // 0.030 kWh/min  = 1800 W bei Peak-Hours

    private final Channel channel;
    private final ObjectMapper mapper;
    private final Random random = new Random();

    public EnergyUser(Channel channel) {
        this.channel = channel;
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule());
    }

    /**
     * Berechnet kWh nach aktueller Stunde
     * Morgenpeak (6-9 Uhr) und Abendpeak (17-21 Uhr) führen zu höherem Verbrauch
     */
    private double calculateKwh(LocalDateTime now) {
        int hour = now.getHour();
        boolean isPeak = (hour >= 6 && hour < 9) || (hour >= 17 && hour < 21);

        double base = isPeak ? PEAK_KWH : BASE_KWH;

        // Zufallsschwankung
        double noise = (random.nextDouble() * 0.4) - 0.2;
        double kwh = base * (1.0 + noise);

        return Math.max(0.0, kwh);
    }

    public void sendOneMessage() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        double kwh = calculateKwh(now);

        EnergyMessage message = new EnergyMessage(
                "USER",
                "COMMUNITY",
                kwh,
                now
        );

        String json = mapper.writeValueAsString(message);
        channel.basicPublish("", QUEUE_NAME, null,
                json.getBytes(StandardCharsets.UTF_8));

        System.out.println("User -> " + json + "   (peak: "
                + ((now.getHour() >= 6 && now.getHour() < 9)
                || (now.getHour() >= 17 && now.getHour() < 21)) + ")");
    }
}