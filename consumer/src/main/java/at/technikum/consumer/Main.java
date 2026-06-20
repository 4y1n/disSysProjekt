package at.technikum.consumer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Main {

    private static final String QUEUE_NAME = "energy.messages";

    // Sende-Intervall
    private static final int SEND_INTERVAL_MS = 3000;

    public static void main(String[] args) throws Exception {

        // 1. RabbitMQ-Verbindung aufbauen
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("guest");
        factory.setPassword("guest");

        Connection rabbitConnection = factory.newConnection();
        Channel channel = rabbitConnection.createChannel();

        // 2. Queue
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);

        // 3. User
        EnergyUser energyUser = new EnergyUser(channel);

        System.out.println("User started. Sende alle " + SEND_INTERVAL_MS + " ms an '"
                + QUEUE_NAME + "' ...");

        // 4. Endlosschleife
        while (true) {
            try {
                energyUser.sendOneMessage();
            } catch (Exception e) {
                System.out.println("Fehler beim Senden: " + e.getMessage());
            }
            Thread.sleep(SEND_INTERVAL_MS);
        }
    }
}