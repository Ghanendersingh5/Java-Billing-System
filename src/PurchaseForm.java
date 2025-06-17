package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class PurchaseForm extends JFrame {
    private JTextField productField, supplierField, quantityField, unitField, rateField, totalField;

    public PurchaseForm() {
        setTitle("Good Receive Form");
        setSize(400, 400);
        setLayout(new GridLayout(7, 2, 10, 10));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        add(new JLabel("Product Name:"));
        productField = new JTextField();
        add(productField);

        add(new JLabel("Supplier Name:"));
        supplierField = new JTextField();
        add(supplierField);

        add(new JLabel("Quantity:"));
        quantityField = new JTextField();
        add(quantityField);

        add(new JLabel("Unit:"));
        unitField = new JTextField();
        add(unitField);

        add(new JLabel("Rate per Unit:"));
        rateField = new JTextField();
        add(rateField);

        add(new JLabel("Total:"));
        totalField = new JTextField();
        totalField.setEditable(false);
        add(totalField);

        JButton calcBtn = new JButton("Calculate");
        JButton saveBtn = new JButton("Save");
        add(calcBtn);
        add(saveBtn);

        calcBtn.addActionListener(e -> calculateTotal());
        saveBtn.addActionListener(e -> saveToDatabase());

        setVisible(true);
    }

    private void calculateTotal() {
        try {
            double qty = Double.parseDouble(quantityField.getText());
            double rate = Double.parseDouble(rateField.getText());
            double total = qty * rate;
            totalField.setText(String.valueOf(total));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Enter valid numbers.");
        }
    }

    private void saveToDatabase() {
        String product = productField.getText();
        String supplier = supplierField.getText();
        String quantity = quantityField.getText();
        String unit = unitField.getText();
        String rate = rateField.getText();
        String total = totalField.getText();

        try (Connection conn = DBHelper.connect()) {
            String sql = "CREATE TABLE IF NOT EXISTS purchases (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "product TEXT, supplier TEXT, quantity REAL, unit TEXT, rate REAL, total REAL)";
            conn.createStatement().execute(sql);

            PreparedStatement stmt = conn.prepareStatement("INSERT INTO purchases (product, supplier, quantity, unit, rate, total) VALUES (?, ?, ?, ?, ?, ?)");
            stmt.setString(1, product);
            stmt.setString(2, supplier);
            stmt.setDouble(3, Double.parseDouble(quantity));
            stmt.setString(4, unit);
            stmt.setDouble(5, Double.parseDouble(rate));
            stmt.setDouble(6, Double.parseDouble(total));

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Purchase saved successfully!");
        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
}
