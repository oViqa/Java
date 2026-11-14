package Java.TP6.exercise1;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main{
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(() -> {
            JFrame window = new JFrame("Calculatrice");
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            window.setContentPane(new CalculatorPanel());
            window.pack();
            window.setLocationRelativeTo(null);
            window.setVisible(true);
        });
    }
}