package at.technikum.producer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.Random;

public class Main {

    private static final String QUEUE_NAME = "energy.messages";

    // Sende-Intervall: zufällig zwischen 1 und 5 Sekunden
    private static final int MIN_INTERVAL_MS = 1000;
    private static final int MAX_INTERVAL_MS = 5000;


    public static void main(String[] args) throws Exception {
        Random random = new Random();

        // 1. RabbitMQ-Verbindung
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("guest");
        factory.setPassword("guest");

        Connection rabbitConnection = factory.newConnection();
        Channel channel = rabbitConnection.createChannel();

        // 2. Queue
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);

        // 3. Producer
        WeatherClient weatherClient   = new WeatherClient();
        EnergyProducer energyProducer = new EnergyProducer(channel, weatherClient);

        System.out.println("Producer started. Sende alle " + MIN_INTERVAL_MS + "-"
                + MAX_INTERVAL_MS + " ms an '" + QUEUE_NAME + "' ...");

        // 4. Endlosschleife
        while (true) {
            try {
                energyProducer.sendOneMessage();
            } catch (Exception e) {
                System.out.println("Fehler beim Senden: " + e.getMessage());
            }
            int delay = MIN_INTERVAL_MS + random.nextInt(MAX_INTERVAL_MS - MIN_INTERVAL_MS + 1);
            Thread.sleep(delay);

        }
    }
}