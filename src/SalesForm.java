package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class SalesForm extends JFrame {
    private JTextField productNameField, customerField, quantityField, rateField, taxField, totalRateField;
    private JComboBox<String> unitBox;

    public SalesForm() {
        setTitle("Sales Form");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(8, 2, 10, 10));
        setLocationRelativeTo(null);

        add(new JLabel("Product Name:"));
        productNameField = new JTextField();
        add(productNameField);

        add(new JLabel("Customer Name:"));
        customerField = new JTextField();
        add(customerField);

        add(new JLabel("Quantity:"));
        quantityField = new JTextField();
        add(quantityField);

        add(new JLabel("Unit of Measurement:"));
        unitBox = new JComboBox<>(new String[]{"Kg", "Litre", "Piece"});
        add(unitBox);

        add(new JLabel("Rate per Unit:"));
        rateField = new JTextField();
        add(rateField);

        add(new JLabel("Tax %:"));
        taxField = new JTextField();
        add(taxField);

        add(new JLabel("Total Rate:"));
        totalRateField = new JTextField();
        totalRateField.setEditable(false);
        add(totalRateField);

        JButton calcBtn = new JButton("Calculate Total");
        JButton saveBtn = new JButton("Save to DB");
        add(calcBtn);
        add(saveBtn);

        calcBtn.addActionListener(e -> {
            try {
                double qty = Double.parseDouble(quantityField.getText());
                double rate = Double.parseDouble(rateField.getText());
                double total = qty * rate;
                totalRateField.setText(String.valueOf(total));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Enter valid numbers");
            }
        });

        saveBtn.addActionListener(e -> saveToDatabase());

        setVisible(true);
    }

    private void saveToDatabase() {
        String product = productNameField.getText().trim();
        String customer = customerField.getText().trim();
        String quantity = quantityField.getText().trim();
        String unit = (String) unitBox.getSelectedItem();
        String rate = rateField.getText().trim();
        String tax = taxField.getText().trim();
        String total = totalRateField.getText().trim();

        try (Connection conn = DBHelper.connect();
             Statement stmt = conn.createStatement()) {

            String sql = "CREATE TABLE IF NOT EXISTS sales (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "product TEXT, customer TEXT, quantity REAL, unit TEXT, rate REAL, total REAL, tax REAL)";
            stmt.execute(sql);

            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO sales (product, customer, quantity, unit, rate, total, tax) VALUES (?, ?, ?, ?, ?, ?, ?)"
            );
            ps.setString(1, product);
            ps.setString(2, customer);
            ps.setDouble(3, Double.parseDouble(quantity));
            ps.setString(4, unit);
            ps.setDouble(5, Double.parseDouble(rate));
            ps.setDouble(6, Double.parseDouble(total));
            ps.setDouble(7, Double.parseDouble(tax));
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Data saved successfully!");

        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error saving data: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        DBHelper.initializeDatabase();
        SwingUtilities.invokeLater(SalesForm::new);
    }
}
