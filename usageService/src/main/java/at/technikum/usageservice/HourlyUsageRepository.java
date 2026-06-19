package at.technikum.usageservice;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class HourlyUsageRepository {

    private final DbConnection db;

    public HourlyUsageRepository(DbConnection db) {
        this.db = db;
    }

    // R(ead) sucht Zeile für eine Stunde , null = Stunde gibt es noch nicht.
    public HourlyUsageRow findByHour(LocalDateTime hour) throws SQLException {
        String sql = "SELECT hour, community_produced, community_used, grid_used "
                + "FROM hourly_usage WHERE hour = ?;";
        Connection c = db.get();
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(hour));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new HourlyUsageRow(
                            rs.getTimestamp("hour").toLocalDateTime(),
                            rs.getDouble("community_produced"),
                            rs.getDouble("community_used"),
                            rs.getDouble("grid_used")
                    );
                }
                return null;
            }
        }
    }

    // C(reate): anlegen neuer Stunden-Zeilen
    public void insert(HourlyUsageRow row) throws SQLException {
        String sql = "INSERT INTO hourly_usage "
                + "(hour, community_produced, community_used, grid_used) "
                + "VALUES (?, ?, ?, ?);";
        Connection c = db.get();
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(row.getHour()));
            ps.setDouble(2, row.getCommunityProduced());
            ps.setDouble(3, row.getCommunityUsed());
            ps.setDouble(4, row.getGridUsed());
            ps.execute();
        }
    }

    // U(pdate): bestehende Stunden-Zeile aktualisieren
    public void update(HourlyUsageRow row) throws SQLException {
        String sql = "UPDATE hourly_usage "
                + "SET community_produced = ?, community_used = ?, grid_used = ? "
                + "WHERE hour = ?;";
        Connection c = db.get();
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setDouble(1, row.getCommunityProduced());
            ps.setDouble(2, row.getCommunityUsed());
            ps.setDouble(3, row.getGridUsed());
            ps.setTimestamp(4, Timestamp.valueOf(row.getHour()));
            ps.execute();
        }
    }
}