package tp6.exercise2;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class MainFrame extends JFrame {
    public MainFrame() {
        super("Facture");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setContentPane(new ProductPanel(new ProductDAO(), this::dispose));
        pack();
        setMinimumSize(getSize());
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                for (UIManager.LookAndFeelInfo theme : UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(theme.getName())) {
                        UIManager.setLookAndFeel(theme.getClassName());
                        break;
                    }
                }
            } catch (Exception ignored) {
                // The default Swing appearance remains usable.
            }
            new MainFrame().setVisible(true);
        });
    }
}
