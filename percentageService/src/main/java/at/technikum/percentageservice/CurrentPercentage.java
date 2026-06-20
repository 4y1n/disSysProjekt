package at.technikum.percentageservice;

import java.time.LocalDateTime;

public class CurrentPercentage {
    private final LocalDateTime hour;
    private final double communityDepleted;
    private final double gridPortion;

    public CurrentPercentage(LocalDateTime hour, double communityDepleted, double gridPortion) {
        this.hour = hour;
        this.communityDepleted = communityDepleted;
        this.gridPortion = gridPortion;
    }

    public LocalDateTime getHour()        { return hour; }
    public double getCommunityDepleted()  { return communityDepleted; }
    public double getGridPortion()        { return gridPortion; }
}