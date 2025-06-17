package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class GoodsReceivingForm extends JFrame {
    private JTextField productNameField, supplierField, quantityField, rateField, taxField, totalRateField;
    private JComboBox<String> unitBox;

    public GoodsReceivingForm() {
        setTitle("Goods Receiving Form");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(8, 2, 10, 10));
        setLocationRelativeTo(null);

        // Input fields
        add(new JLabel("Product Name:"));
        productNameField = new JTextField();
        add(productNameField);

        add(new JLabel("Supplier Name:"));
        supplierField = new JTextField();
        add(supplierField);

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

        // Total Calculation Button
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

        // Save to DB
        saveBtn.addActionListener(e -> saveToDatabase());

        setVisible(true);
    }

    private void saveToDatabase() {
        String product = productNameField.getText().trim();
        String supplier = supplierField.getText().trim();
        String quantity = quantityField.getText().trim();
        String unit = (String) unitBox.getSelectedItem();
        String rate = rateField.getText().trim();
        String tax = taxField.getText().trim();
        String total = totalRateField.getText().trim();

        try (Connection conn = DBHelper.connect()) {
            String sql = "CREATE TABLE IF NOT EXISTS goods_receiving (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "product TEXT, supplier TEXT, quantity REAL, unit TEXT, rate REAL, total REAL, tax REAL)";
            conn.createStatement().execute(sql);

            String insert = "INSERT INTO goods_receiving (product, supplier, quantity, unit, rate, total, tax) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(insert);
            stmt.setString(1, product);
            stmt.setString(2, supplier);
            stmt.setDouble(3, Double.parseDouble(quantity));
            stmt.setString(4, unit);
            stmt.setDouble(5, Double.parseDouble(rate));
            stmt.setDouble(6, Double.parseDouble(total));
            stmt.setDouble(7, Double.parseDouble(tax));
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Saved successfully!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error saving: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GoodsReceivingForm::new);
    }
}
