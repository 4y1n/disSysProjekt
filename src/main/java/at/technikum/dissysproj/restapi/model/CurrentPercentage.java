package at.technikum.dissysproj.restapi.model;

import java.time.LocalDateTime;

public class CurrentPercentage {
    private LocalDateTime hour;
    private double communityDepleted;
    private double gridPortion;

    public CurrentPercentage() {
    }

    public CurrentPercentage(LocalDateTime hour, double communityDepleted, double gridPortion) {
        this.hour = hour;
        this.communityDepleted = communityDepleted;
        this.gridPortion = gridPortion;
    }

    public LocalDateTime getHour() {
        return hour;
    }

    public double getCommunityDepleted() {
        return communityDepleted;
    }

    public double getGridPortion() {
        return gridPortion;
    }

    public void setHour(LocalDateTime hour) {
        this.hour = hour;
    }

    public void setCommunityDepleted(double communityDepleted) {
        this.communityDepleted = communityDepleted;
    }

    public void setGridPortion(double gridPortion) {
        this.gridPortion = gridPortion;
    }
}
