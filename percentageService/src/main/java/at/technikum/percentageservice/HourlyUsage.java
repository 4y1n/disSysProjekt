package at.technikum.percentageservice;

import java.time.LocalDateTime;

public class HourlyUsage {
    private final LocalDateTime hour;
    private final double communityProduced;
    private final double communityUsed;
    private final double gridUsed;

    public HourlyUsage(LocalDateTime hour, double communityProduced,
                          double communityUsed, double gridUsed) {
        this.hour = hour;
        this.communityProduced = communityProduced;
        this.communityUsed = communityUsed;
        this.gridUsed = gridUsed;
    }

    public LocalDateTime getHour()           { return hour; }
    public double getCommunityProduced()     { return communityProduced; }
    public double getCommunityUsed()         { return communityUsed; }
    public double getGridUsed()              { return gridUsed; }
}