package tp6.exercise2;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/** Checks the MySQL connection without creating or changing any data. */
public class CheckConnection {
    public static void main(String[] args) throws Exception {
        try (Connection connection = DatabaseConnection.connect();
             Statement statement = connection.createStatement();
             ResultSet result = statement.executeQuery("SELECT DATABASE(), VERSION()")) {
            if (result.next()) {
                System.out.println("Connected to MySQL " + result.getString(2));
                System.out.println("Database: " + result.getString(1));
            }
        }
    }
}
