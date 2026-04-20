package at.technikum.dissysproj.restapi.model;

import java.time.LocalDateTime;

public class CurrentPercentage {
    private LocalDateTime hour;
    private double communityPoolUsed;
    private double gridPortion;

    public CurrentPercentage(LocalDateTime hour, double communityPoolUsed, double gridPortion) {
        this.hour = hour;
        this.communityPoolUsed = communityPoolUsed;
        this.gridPortion = gridPortion;

    }

    public LocalDateTime getHour() {
        return hour;
    }

    public double getCommunity() {
        return communityPoolUsed;
    }

    public double getGridPortion() {
        return gridPortion;
    }

    public void setHour(LocalDateTime hour) {
        this.hour = hour;
    }

    public void setCommunityPoolUsed(double communityPoolUsed) {
        this.communityPoolUsed = communityPoolUsed;
    }

    public void setGridPortion(double gridPortion) {
        this.gridPortion = gridPortion;
    }
}