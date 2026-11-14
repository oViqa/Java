package tp6.exercise2;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;

/** Product form: validation and events stay here; SQL stays in ProductDAO. */
public class ProductPanel extends JPanel {
    private final ProductDAO dao;
    private final JTextField code = new JTextField(5);
    private final JTextField name = new JTextField(19);
    private final JTextField purchase = new JTextField(10);
    private final JTextField sale = new JTextField(10);
    private final JLabel status = new JLabel("Connexion à MySQL…");
    private final List<JButton> actionButtons = new ArrayList<>();
    private final JButton first = new JButton("<<");
    private final JButton previous = new JButton("<");
    private final JButton next = new JButton(">");
    private final JButton last = new JButton(">>");
    private List<Product> products = new ArrayList<>();
    private int index = -1;
    private boolean busy;

    public ProductPanel(ProductDAO dao, Runnable closeWindow) {
        this.dao = dao;
        setLayout(new BorderLayout(24, 45));
        setBackground(new Color(238, 238, 238));
        setBorder(BorderFactory.createEmptyBorder(35, 35, 22, 20));
        setPreferredSize(new Dimension(650, 465));

        JLabel title = new JLabel("Feuille Produit", SwingConstants.CENTER);
        title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 32));
        title.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(82, 70, 170)),
                BorderFactory.createEmptyBorder(12, 10, 12, 10)));
        JPanel heading = new JPanel(new BorderLayout());
        heading.setOpaque(false);
        heading.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 55));
        heading.add(title);
        add(heading, BorderLayout.NORTH);

        JPanel fields = new JPanel(new GridBagLayout());
        fields.setOpaque(false);
        addField(fields, "Code Produit :", code, 0);
        addField(fields, "Nom produit :", name, 1);
        addField(fields, "Prix Achat :", purchase, 2);
        addField(fields, "Prix Vente :", sale, 3);

        JPanel actions = new JPanel(new GridLayout(4, 1, 0, 10));
        actions.setOpaque(false);
        actions.setPreferredSize(new Dimension(175, 190));
        addAction(actions, "Ajouter", this::addProduct);
        addAction(actions, "Modifier", this::updateProduct);
        addAction(actions, "Supprimer", this::deleteProduct);
        addAction(actions, "Fermer", closeWindow);
        JPanel center = new JPanel(new BorderLayout(35, 0));
        center.setOpaque(false);
        center.add(fields, BorderLayout.CENTER);
        center.add(actions, BorderLayout.EAST);
        add(center, BorderLayout.CENTER);

        JPanel navigation = new JPanel(new GridLayout(1, 4, 10, 0));
        navigation.setOpaque(false);
        navigation.setPreferredSize(new Dimension(375, 36));
        for (JButton button : List.of(first, previous, next, last)) {
            styleButton(button);
            navigation.add(button);
        }
        first.setToolTipText("Premier produit");
        previous.setToolTipText("Produit précédent");
        next.setToolTipText("Produit suivant");
        last.setToolTipText("Dernier produit");
        first.addActionListener(event -> showProduct(0));
        previous.addActionListener(event -> showProduct(index - 1));
        next.addActionListener(event -> showProduct(index + 1));
        last.addActionListener(event -> showProduct(products.size() - 1));
        JPanel bottom = new JPanel(new BorderLayout(0, 12));
        bottom.setOpaque(false);
        JPanel navigationRow = new JPanel(new BorderLayout());
        navigationRow.setOpaque(false);
        navigationRow.add(navigation, BorderLayout.WEST);
        bottom.add(navigationRow, BorderLayout.NORTH);
        bottom.add(status, BorderLayout.SOUTH);
        add(bottom, BorderLayout.SOUTH);

        runDatabaseTask(() -> {
            dao.createTable();
            return dao.findAll();
        }, null, "Produits chargés.");
    }

    private void addField(JPanel panel, String text, JTextField field, int row) {
        JLabel label = new JLabel(text);
        label.setFont(new Font(Font.SERIF, Font.BOLD, 18));
        label.setBorder(BorderFactory.createLineBorder(new Color(232, 186, 36)));
        label.setPreferredSize(new Dimension(155, 30));
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 0, 5, 12);
        panel.add(label, constraints);
        field.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 180, 185)),
                BorderFactory.createEmptyBorder(3, 4, 3, 4)));
        field.setColumns(0);
        field.setPreferredSize(new Dimension(row == 0 ? 60 : row == 1 ? 210 : 115, 30));
        constraints.gridx = 1;
        constraints.weightx = 1;
        constraints.insets = new Insets(5, 0, 5, 0);
        panel.add(field, constraints);
    }

    private void styleButton(JButton button) {
        button.setFont(new Font(Font.SERIF, Font.BOLD, 20));
        button.setBackground(new Color(207, 225, 240));
    }

    private void addAction(JPanel panel, String text, Runnable action) {
        JButton button = new JButton(text);
        styleButton(button);
        button.addActionListener(event -> action.run());
        actionButtons.add(button);
        panel.add(button);
    }

    private int readCode() {
        try {
            int value = Integer.parseInt(code.getText().trim());
            if (value > 0) return value;
        } catch (NumberFormatException ignored) {
        }
        throw new IllegalArgumentException("Le code produit doit être un entier positif.");
    }

    private double readPrice(JTextField field, String label) {
        try {
            BigDecimal value = new BigDecimal(field.getText().trim().replace(',', '.'));
            if (value.signum() < 0 || value.compareTo(new BigDecimal("9999999999.99")) > 0
                    || value.stripTrailingZeros().scale() > 2) {
                throw new NumberFormatException();
            }
            return value.doubleValue();
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException(label + " : saisissez un prix positif ou nul, avec au maximum 2 décimales.");
        }
    }

    private Product readProduct() {
        int productCode = readCode();
        String productName = name.getText().trim();
        if (productName.isEmpty() || productName.codePointCount(0, productName.length()) > 255) {
            throw new IllegalArgumentException("Le nom produit doit contenir entre 1 et 255 caractères.");
        }
        return new Product(productCode, productName,
                readPrice(purchase, "Prix Achat"), readPrice(sale, "Prix Vente"));
    }

    private void addProduct() {
        try {
            Product product = readProduct();
            runDatabaseTask(() -> {
                dao.add(product);
                return dao.findAll();
            }, product.getCode(), "Produit ajouté.");
        } catch (IllegalArgumentException exception) {
            showError(exception.getMessage());
        }
    }

    private void updateProduct() {
        try {
            Product product = readProduct();
            runDatabaseTask(() -> {
                if (!dao.update(product)) throw new SQLException("Aucun produit avec ce code.", "02000");
                return dao.findAll();
            }, product.getCode(), "Produit modifié.");
        } catch (IllegalArgumentException exception) {
            showError(exception.getMessage());
        }
    }

    private void deleteProduct() {
        try {
            int productCode = readCode();
            if (JOptionPane.showConfirmDialog(this, "Supprimer le produit " + productCode + " ?",
                    "Confirmation", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
            runDatabaseTask(() -> {
                if (!dao.delete(productCode)) throw new SQLException("Aucun produit avec ce code.", "02000");
                return dao.findAll();
            }, null, "Produit supprimé.");
        } catch (IllegalArgumentException exception) {
            showError(exception.getMessage());
        }
    }

    @FunctionalInterface
    private interface DatabaseTask {
        List<Product> execute() throws SQLException;
    }

    // JDBC runs in the background so a slow connection does not freeze the window.
    private void runDatabaseTask(DatabaseTask task, Integer selectedCode, String message) {
        if (busy) return;
        busy = true;
        updateControls();
        status.setText("Opération en cours…");
        new SwingWorker<List<Product>, Void>() {
            @Override
            protected List<Product> doInBackground() throws SQLException {
                return task.execute();
            }

            @Override
            protected void done() {
                try {
                    products = get();
                    int selectedIndex = Math.min(Math.max(index, 0), products.size() - 1);
                    if (selectedCode != null) {
                        for (int i = 0; i < products.size(); i++) {
                            if (products.get(i).getCode() == selectedCode) selectedIndex = i;
                        }
                    }
                    showProduct(selectedIndex);
                    status.setText(message + " " + products.size() + " produit(s).");
                } catch (InterruptedException exception) {
                    Thread.currentThread().interrupt();
                    showError("Opération interrompue.");
                } catch (ExecutionException exception) {
                    Throwable cause = exception.getCause();
                    String detail = "Impossible d'effectuer l'opération. Vérifiez la connexion et les données saisies.";
                    if (cause instanceof SQLException sql) {
                        if (sql.getErrorCode() == 1062) {
                            detail = "Ce code produit existe déjà. Choisissez un autre code.";
                        } else if ("02000".equals(sql.getSQLState())) {
                            detail = "Aucun produit avec ce code.";
                        }
                    }
                    status.setText("Échec de l'opération. Vérifiez la connexion et réessayez.");
                    showError(detail);
                } finally {
                    busy = false;
                    updateControls();
                }
            }
        }.execute();
    }

    private void showProduct(int requestedIndex) {
        if (products.isEmpty()) {
            index = -1;
            for (JTextField field : List.of(code, name, purchase, sale)) field.setText("");
        } else {
            index = Math.max(0, Math.min(requestedIndex, products.size() - 1));
            Product product = products.get(index);
            code.setText(Integer.toString(product.getCode()));
            name.setText(product.getNom());
            purchase.setText(BigDecimal.valueOf(product.getPrixAchat()).setScale(2,
                    java.math.RoundingMode.HALF_UP).toPlainString());
            sale.setText(BigDecimal.valueOf(product.getPrixVente()).setScale(2,
                    java.math.RoundingMode.HALF_UP).toPlainString());
            status.setText("Produit " + (index + 1) + " / " + products.size());
        }
        updateControls();
    }

    private void updateControls() {
        for (JTextField field : List.of(code, name, purchase, sale)) field.setEnabled(!busy);
        for (JButton button : actionButtons) {
            button.setEnabled(!busy || "Fermer".equals(button.getText()));
        }
        first.setEnabled(!busy && index > 0);
        previous.setEnabled(!busy && index > 0);
        next.setEnabled(!busy && index >= 0 && index < products.size() - 1);
        last.setEnabled(next.isEnabled());
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Erreur", JOptionPane.ERROR_MESSAGE);
    }
}
