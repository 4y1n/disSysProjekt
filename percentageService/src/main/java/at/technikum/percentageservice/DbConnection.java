package at.technikum.percentageservice;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {
    private static final String URL =
            "jdbc:postgresql://localhost:5432/energy_community?user=disysuser&password=disyspw";

    private final Connection connection;

    public DbConnection() throws SQLException {
        this.connection = DriverManager.getConnection(URL);
    }

    public Connection get() {
        return connection;
    }
}