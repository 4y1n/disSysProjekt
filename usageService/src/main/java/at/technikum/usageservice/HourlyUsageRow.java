package at.technikum.usageservice;
import java.time.LocalDateTime;

public class HourlyUsageRow {
    private LocalDateTime hour;
    private double communityProduced;
    private double communityUsed;
    private double gridUsed;

    // Neue, leere Stunde (alles 0)
    public HourlyUsageRow(LocalDateTime hour) {
        this(hour, 0.0, 0.0, 0.0);
}
    // Existierende Zeile aus der DB
    public HourlyUsageRow(LocalDateTime hour, double communityProduced,
                          double communityUsed, double gridUsed) {
        this.hour = hour;
        this.communityProduced = communityProduced;
        this.communityUsed = communityUsed;
        this.gridUsed = gridUsed;
    }

    public LocalDateTime getHour() {
        return hour;
    }

    public double getCommunityProduced()  {
        return communityProduced;
    }

    public double getCommunityUsed() {
        return communityUsed;
    }

    public double getGridUsed()           {
        return gridUsed;
    }

    public void setCommunityProduced(double v) {
        this.communityProduced = v;
    }

    public void setCommunityUsed(double v)     {
        this.communityUsed = v;
    }

    public void setGridUsed(double v)          {
        this.gridUsed = v;
    }

}
