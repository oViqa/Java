import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import tp6.exercise2.DatabaseConnection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class InitDb {
    public static void main(String[] args) throws Exception {
        // Run from TP6, or pass the TP6 folder as the first argument.
        Path project = Path.of(args.length == 0 ? "." : args[0])
                .toAbsolutePath().normalize();
        Path scripts = project.resolve("exercise2/database");

        if (!Files.isRegularFile(scripts.resolve("schema.sql"))) {
            throw new IOException("schema.sql not found. Run from TP6 or pass its path as an argument.");
        }

        try (Connection connection = DatabaseConnection.connect()) {
            // Initialize the table without inserting sample products.
            runSqlFile(connection, scripts.resolve("schema.sql"));

            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(
                         "SELECT code, nom, prix_achat, prix_vente FROM PRODUIT ORDER BY code")) {
                System.out.println("MySQL database initialized.");
                while (resultSet.next()) {
                    System.out.println(
                            "[" + resultSet.getString("code") + ", "
                                    + resultSet.getString("nom") + ", "
                                    + resultSet.getDouble("prix_achat") + ", "
                                    + resultSet.getDouble("prix_vente") + "]"
                    );
                }
            }
        }
    }

    private static void runSqlFile(Connection connection, Path sqlFile) throws IOException, SQLException {
        String sql = Files.readString(sqlFile);
        // The schema file contains one complete statement.
        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }
}
