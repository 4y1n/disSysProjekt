package at.technikum.usageservice;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class UsageCalculator {

    // Verarbeitet  Nachricht und aktualisiert die Stunden
    public HourlyUsageRow apply(HourlyUsageRow row, EnergyMessage message) {

        if ("PRODUCER".equals(message.getType())) {
            row.setCommunityProduced(row.getCommunityProduced() + message.getKwh());
        }
        else if ("USER".equals(message.getType())) {
            applyUserMessage(row, message.getKwh());
        }
        return row;
    }

    // erst Community-Pool, dann Netz
    private void applyUserMessage(HourlyUsageRow row, double kwh) {

        double available = row.getCommunityProduced() - row.getCommunityUsed();
        if (available < 0) available = 0;

        double fromCommunity = Math.min(kwh, available);
        double fromGrid = kwh - fromCommunity;

        row.setCommunityUsed(row.getCommunityUsed() + fromCommunity);
        row.setGridUsed(row.getGridUsed() + fromGrid);
    }

    // Rundet auf die volle Stunde ab: 14:33 wird zb zu 14:00
    public LocalDateTime truncateToHour(LocalDateTime datetime) {
        return datetime.truncatedTo(ChronoUnit.HOURS);
    }
}
