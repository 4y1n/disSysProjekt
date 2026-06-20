package at.technikum.restapi.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "hourly_usage")
public class HourlyUsage {

    @Id
    @Column(name = "hour")
    private LocalDateTime hour;

    @Column(name = "community_produced")
    private double communityProduced;

    @Column(name = "community_used")
    private double communityUsed;

    @Column(name = "grid_used")
    private double gridUsed;

    public HourlyUsage() {}

    public HourlyUsage(LocalDateTime hour, double communityProduced, double communityUsed, double gridUsed) {
        this.hour = hour;
        this.communityProduced = communityProduced;
        this.communityUsed = communityUsed;
        this.gridUsed = gridUsed;
    }

    public LocalDateTime getHour() { return hour; }
    public void setHour(LocalDateTime hour) { this.hour = hour; }

    public double getCommunityProduced() { return communityProduced; }
    public void setCommunityProduced(double v) { this.communityProduced = v; }

    public double getCommunityUsed() { return communityUsed; }
    public void setCommunityUsed(double v) { this.communityUsed = v; }

    public double getGridUsed() { return gridUsed; }
    public void setGridUsed(double v) { this.gridUsed = v; }
}