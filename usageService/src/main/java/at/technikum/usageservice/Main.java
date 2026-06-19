package at.technikum.usageservice;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Main {

    public static void main(String[] args) throws Exception {

        // Queue-Namen aus RABBITMQ.md
        String inputQueue   = "energy.messages";
        String updatesQueue = "usage.updates";

        // 1. RabbitMQ-Verbindung aufbauen
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("guest");
        factory.setPassword("guest");

        Connection rabbitConnection = factory.newConnection();
        Channel channel = rabbitConnection.createChannel();

        // 2. Beide Queues deklarieren
        //    queueDeclare(name, durable, exclusive, autoDelete, arguments)
        channel.queueDeclare(inputQueue,   true, false, false, null);
        channel.queueDeclare(updatesQueue, true, false, false, null);

        // 3. Datenbank-Verbindung
        DbConnection db = new DbConnection();
        HourlyUsageRepository repository = new HourlyUsageRepository(db);

        // 4. Service und Consumer zusammenstecken
        UsageService usageService = new UsageService(repository, channel, updatesQueue);
        EnergyConsumer consumer   = new EnergyConsumer(channel, inputQueue, usageService);
        consumer.start();

        System.out.println("Usage Service runs. Warte auf Nachrichten in '"
                + inputQueue + "' ...");
    }
}