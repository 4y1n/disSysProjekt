package at.technikum.percentageservice;

import java.time.LocalDateTime;

public class PercentageService {

    private final HourlyUsageRepository usageRepository;
    private final CurrentPercentageRepository percentageRepository;
    private final PercentageCalculator calculator = new PercentageCalculator();

    public PercentageService(HourlyUsageRepository usageRepository,
                             CurrentPercentageRepository percentageRepository) {
        this.usageRepository = usageRepository;
        this.percentageRepository = percentageRepository;
    }

    public void process(UsageUpdateMessage message) throws Exception {

        LocalDateTime hour = LocalDateTime.parse(message.getHour());

        // 1. Aktuelle Stunden-Daten laden
        HourlyUsage usage = usageRepository.findByHour(hour);
        if (usage == null) {
            // Stunde existiert noch nicht in hourly_usage -> nichts zu berechnen
            return;
        }

        // 2. Prozentwerte berechnen
        double communityDepleted = calculator.calculateCommunityDepleted(
                usage.getCommunityProduced(), usage.getCommunityUsed());

        double gridPortion = calculator.calculateGridPortion(
                usage.getCommunityUsed(), usage.getGridUsed());

        // 3. Speichern (Insert bei neuer Stunde, sonst Update)
        CurrentPercentage row = new CurrentPercentage(hour, communityDepleted, gridPortion);
        percentageRepository.save(row);

        System.out.printf("Berechnet: Stunde %s -> community_depleted=%.2f%%, grid_portion=%.2f%%%n",
                hour, communityDepleted, gridPortion);
    }
}