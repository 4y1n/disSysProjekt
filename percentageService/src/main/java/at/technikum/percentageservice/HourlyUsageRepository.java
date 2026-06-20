package at.technikum.percentageservice;

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

    // Liest die Stunden-Zeile, null falls die Stunde (noch) nicht existiert.
    public HourlyUsage findByHour(LocalDateTime hour) throws SQLException {
        String sql = "SELECT hour, community_produced, community_used, grid_used "
                + "FROM hourly_usage WHERE hour = ?;";
        Connection c = db.get();
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(hour));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new HourlyUsage(
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
}