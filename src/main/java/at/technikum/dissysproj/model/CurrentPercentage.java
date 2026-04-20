package at.technikum.dissysproj.model;

public class CurrentPercentage {
    private String hour;
    private double communityDepleted;
    private double gridPortion;

    public CurrentPercentage(String hour, double communityDepleted, double gridPortion) {
        this.hour = hour;
        this.communityDepleted = communityDepleted;
        this.gridPortion = gridPortion;
        //test
    }

    public String getHour() {
        return hour;
    }

    public double getCommunityDepleted() {
        return communityDepleted;
    }

    public double getGridPortion() {
        return gridPortion;
    }
}