package at.technikum.percentageservice;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class CurrentPercentageRepository {

    private final DbConnection db;

    public CurrentPercentageRepository(DbConnection db) {
        this.db = db;
    }

    // Prüft, ob für diese Stunde schon eine Zeile existiert.
    public boolean exists(LocalDateTime hour) throws SQLException {
        String sql = "SELECT 1 FROM current_percentage WHERE hour = ?;";
        Connection c = db.get();
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(hour));
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    // Legt eine neue Zeile an.
    public void insert(CurrentPercentage row) throws SQLException {
        String sql = "INSERT INTO current_percentage "
                + "(hour, community_depleted, grid_portion) "
                + "VALUES (?, ?, ?);";
        Connection c = db.get();
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(row.getHour()));
            ps.setDouble(2, row.getCommunityDepleted());
            ps.setDouble(3, row.getGridPortion());
            ps.execute();
        }
    }

    // Aktualisiert eine bestehende Zeile.
    public void update(CurrentPercentage row) throws SQLException {
        String sql = "UPDATE current_percentage "
                + "SET community_depleted = ?, grid_portion = ? "
                + "WHERE hour = ?;";
        Connection c = db.get();
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setDouble(1, row.getCommunityDepleted());
            ps.setDouble(2, row.getGridPortion());
            ps.setTimestamp(3, Timestamp.valueOf(row.getHour()));
            ps.execute();
        }
    }

    // Legt an oder aktualisiert, je nachdem ob die Stunde schon existiert.
    public void save(CurrentPercentage row) throws SQLException {
        if (exists(row.getHour())) {
            update(row);
        } else {
            insert(row);
        }
    }
}