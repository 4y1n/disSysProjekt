package at.technikum.percentageservice;


import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class PercentageListener {
    @Autowired
    private HourlyUsageRepository usageRepository;

    @Autowired
    private CurrentPercentageRepository percentageRepository;

    @RabbitListener(queues = RabbitMqConfig.PERCENTAGE_QUEUE)
    public void handleProcessed(String hourString) {
        LocalDateTime hour = LocalDateTime.parse(hourString);

        usageRepository.findByHour(hour).ifPresent(usage -> {
            double produced = usage.getCommunityProduced();
            double used     = usage.getCommunityUsed();
            double grid     = usage.getGridUsed();

            double communityDepleted = (produced > 0) ? (used / produced) * 100.0 : 0.0;
            double gridPortion       = (used + grid > 0) ? (grid / (used + grid)) * 100.0 : 0.0;

            CurrentPercentage cp = percentageRepository.findById(hour)
                    .orElse(new CurrentPercentage());
            cp.setHour(hour);
            cp.setCommunityDepleted(communityDepleted);
            cp.setGridPortion(gridPortion);

            percentageRepository.save(cp);
        });
    }
}
