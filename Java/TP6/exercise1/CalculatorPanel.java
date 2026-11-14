package Java.TP6.exercise1;

import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JOptionPane;

public class CalculatorPanel extends JPanel {

    private final JTextField firstNumberField = new JTextField(10);
    private final JTextField secondNumberField = new JTextField(10);
    private final JTextField resultField = new JTextField(10);

    public CalculatorPanel() {
        setLayout(new GridLayout(0, 2, 8, 8));

        resultField.setEditable(false);

        add(new JLabel("First number:"));
        add(firstNumberField);
        add(new JLabel("Second number:"));
        add(secondNumberField);
        add(new JLabel("Result:"));
        add(resultField);

        addOperationButton("+", (first, second) -> first + second);
        addOperationButton("-", (first, second) -> first - second);
        addOperationButton("×", (first, second) -> first * second);
        addOperationButton("÷", (first, second) -> first / second);

        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(event -> {
            firstNumberField.setText("");
            secondNumberField.setText("");
            resultField.setText("");
        });
        add(clearButton);
    }

    private void addOperationButton(String text, Calculation calculation) {
        JButton button = new JButton(text);
        button.addActionListener(event -> calculate(calculation));
        add(button);
    }

    private void calculate(Calculation calculation) {
        try {
            double first = Double.parseDouble(firstNumberField.getText().trim());
            double second = Double.parseDouble(secondNumberField.getText().trim());
            if (calculation == DIVISION && second == 0) {
                JOptionPane.showMessageDialog(this, "Cannot divide by zero.",
                        "Calculation error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            resultField.setText(Double.toString(calculation.apply(first, second)));
        } catch (NumberFormatException exception) {
            JOptionPane.showMessageDialog(this, "Enter valid numbers in both fields.",
                    "Invalid input", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static final Calculation DIVISION = (first, second) -> first / second;

    @FunctionalInterface
    private interface Calculation {
        double apply(double first, double second);
    }
}