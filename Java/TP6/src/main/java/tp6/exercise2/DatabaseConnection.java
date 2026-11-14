package tp6.exercise2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/** Shared MySQL settings for the DAO and database initializer. */
public final class DatabaseConnection {
    private DatabaseConnection() {
    }

    public static Connection connect() throws SQLException {
        String url = System.getenv("TP6_DB_URL");
        if (url == null || url.isBlank()) {
            url = "jdbc:mysql://localhost:3306/tp6";
        }
        String user = System.getenv("TP6_DB_USER");
        String password = System.getenv("TP6_DB_PASSWORD");
        if (user == null || user.isBlank() || password == null) {
            throw new SQLException("Set TP6_DB_USER and TP6_DB_PASSWORD before connecting to MySQL.");
        }
        return DriverManager.getConnection(url, user, password);
    }
}
