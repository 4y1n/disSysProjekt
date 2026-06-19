package at.technikum.usageservice;

import com.rabbitmq.client.Channel;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UsageService {

    private static final DateTimeFormatter ISO =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    private final HourlyUsageRepository repository;
    private final UsageCalculator calculator = new UsageCalculator();
    private final Channel channel;
    private final String updatesQueue;

    public UsageService(HourlyUsageRepository repository, Channel channel, String updatesQueue) {
        this.repository = repository;
        this.channel = channel;
        this.updatesQueue = updatesQueue;
    }

    public void process(EnergyMessage message) throws Exception {

        // 1. Stunde ableiten (14:34 -> 14:00)
        LocalDateTime hour = calculator.truncateToHour(message.getDatetime());

        // 2. Vorhandene Zeile laden oder leere neue anlegen
        HourlyUsageRow row = repository.findByHour(hour);
        boolean isNew = (row == null);
        if (isNew) {
            row = new HourlyUsageRow(hour);
        }

        // 3. PRODUCER oder USER
        calculator.apply(row, message);

        // 4. Speichern: INSERT bei neuer Stunde, sonst UPDATE
        if (isNew) {
            repository.insert(row);
        } else {
            repository.update(row);
        }

        // 5. Update-Nachricht an Percentage Service senden
        String updateJson = "{\"hour\":\"" + hour.format(ISO) + "\"}";
        channel.basicPublish("", updatesQueue, null, updateJson.getBytes(StandardCharsets.UTF_8));
    }
}