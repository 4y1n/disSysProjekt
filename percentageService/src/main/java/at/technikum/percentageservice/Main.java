package at.technikum.percentageservice;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Main {

    public static void main(String[] args) throws Exception {

        // Queue-Name aus RABBITMQ.md
        String updatesQueue = "usage.updates";

        // 1. RabbitMQ-Verbindung aufbauen
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("guest");
        factory.setPassword("guest");

        Connection rabbitConnection = factory.newConnection();
        Channel channel = rabbitConnection.createChannel();

        // 2. Queue deklarieren (durable=true, siehe RABBITMQ.md)
        channel.queueDeclare(updatesQueue, true, false, false, null);

        // 3. Datenbank-Verbindung und Repositories
        DbConnection db = new DbConnection();
        HourlyUsageRepository usageRepository = new HourlyUsageRepository(db);
        CurrentPercentageRepository percentageRepository = new CurrentPercentageRepository(db);

        // 4. Service und Consumer zusammenstecken
        PercentageService percentageService = new PercentageService(usageRepository, percentageRepository);
        PercentageConsumer consumer = new PercentageConsumer(channel, updatesQueue, percentageService);
        consumer.start();

        System.out.println("Percentage Service runs. Warte auf Nachrichten in '"
                + updatesQueue + "' ...");
    }
}