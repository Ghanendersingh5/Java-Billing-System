package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ProductMasterForm extends JFrame {
    private JTextField nameField, rateField;
    private JComboBox<String> unitBox;

    public ProductMasterForm() {
        setTitle("Product Master");
        setSize(350, 250);
        setLayout(new GridLayout(4, 2, 10, 10));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        add(new JLabel("Product Name:"));
        nameField = new JTextField();
        add(nameField);

        add(new JLabel("Unit:"));
        unitBox = new JComboBox<>(new String[]{"Kg", "Litre", "Piece"});
        add(unitBox);

        add(new JLabel("Default Rate:"));
        rateField = new JTextField();
        add(rateField);

        JButton saveBtn = new JButton("Save");
        add(new JLabel());  // empty label for spacing
        add(saveBtn);

        saveBtn.addActionListener(e -> saveProduct());

        setVisible(true);
    }

    private void saveProduct() {
        String name = nameField.getText().trim();
        String unit = (String) unitBox.getSelectedItem();
        String rateText = rateField.getText().trim();

        if (name.isEmpty() || rateText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!");
            return;
        }

        try {
            double rate = Double.parseDouble(rateText);

            try (Connection conn = DBHelper.connect()) {
                String createTableSQL = "CREATE TABLE IF NOT EXISTS products (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "name TEXT, unit TEXT, rate REAL)";
                conn.createStatement().execute(createTableSQL);

                PreparedStatement stmt = conn.prepareStatement(
                        "INSERT INTO products (name, unit, rate) VALUES (?, ?, ?)");
                stmt.setString(1, name);
                stmt.setString(2, unit);
                stmt.setDouble(3, rate);
                stmt.executeUpdate();

                JOptionPane.showMessageDialog(this, "Product saved successfully!");
                nameField.setText("");
                rateField.setText("");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Rate must be a number.");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage());
        }
    }
}
