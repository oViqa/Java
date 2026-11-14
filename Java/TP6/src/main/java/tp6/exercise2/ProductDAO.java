package tp6.exercise2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {
    private Connection connect() throws SQLException {
        return DatabaseConnection.connect();
    }

    public void createTable() throws SQLException {
        String sql = """
                CREATE TABLE IF NOT EXISTS PRODUIT (
                    code INT PRIMARY KEY CHECK (code > 0),
                    nom VARCHAR(255) NOT NULL CHECK (CHAR_LENGTH(TRIM(nom)) > 0),
                    prix_achat DECIMAL(12, 2) NOT NULL CHECK (prix_achat >= 0),
                    prix_vente DECIMAL(12, 2) NOT NULL CHECK (prix_vente >= 0)
                ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
                """;

        try (Connection connection = connect();
             Statement statement = connection.createStatement()) {

            statement.executeUpdate(sql);
        }
    }

    public void add(Product product) throws SQLException {
        String sql = """
                INSERT INTO PRODUIT (code, nom, prix_achat, prix_vente)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection connection = connect();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, product.getCode());
            statement.setString(2, product.getNom());
            statement.setDouble(3, product.getPrixAchat());
            statement.setDouble(4, product.getPrixVente());

            statement.executeUpdate();
        }
    }

    public boolean update(Product product) throws SQLException {
        String sql = """
                UPDATE PRODUIT
                SET nom = ?, prix_achat = ?, prix_vente = ?
                WHERE code = ?
                """;

        try (Connection connection = connect();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, product.getNom());
            statement.setDouble(2, product.getPrixAchat());
            statement.setDouble(3, product.getPrixVente());
            statement.setInt(4, product.getCode());

            return statement.executeUpdate() > 0;
        }
    }

    public boolean delete(int code) throws SQLException {
        String sql = "DELETE FROM PRODUIT WHERE code = ?";

        try (Connection connection = connect();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, code);

            return statement.executeUpdate() > 0;
        }
    }

    public List<Product> findAll() throws SQLException {
        List<Product> products = new ArrayList<>();

        String sql = """
                SELECT code, nom, prix_achat, prix_vente
                FROM PRODUIT
                ORDER BY code
                """;

        try (Connection connection = connect();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                Product product = new Product(
                        result.getInt("code"),
                        result.getString("nom"),
                        result.getDouble("prix_achat"),
                        result.getDouble("prix_vente")
                );

                products.add(product);
            }
        }

        return products;
    }
}
