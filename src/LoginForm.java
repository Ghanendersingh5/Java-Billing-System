package src;


import javax.swing.*; // For JFrame, JButton, JTextField, JOptionPane, etc.
import java.awt.*;     // For layout managers, colors
import java.awt.event.*; // For ActionListener
import java.sql.*;     // For Connection, ResultSet, etc.
import src.DBHelper;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import src.DBHelper;

public class LoginForm extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginForm() {
        setTitle("Operator Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 250);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 2, 10, 10));

        add(new JLabel("Username:"));
        usernameField = new JTextField();
        add(usernameField);

        add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        add(passwordField);

        JButton loginBtn = new JButton("Login");
        add(loginBtn);

        JButton exitBtn = new JButton("Exit");
        add(exitBtn);

        loginBtn.addActionListener(e -> login());
        exitBtn.addActionListener(e -> System.exit(0));

        setVisible(true);
    }

    private void login() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        try (Connection conn = DBHelper.connect();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM operators WHERE username = ? AND password = ?")) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

          if (rs.next()) {
    JOptionPane.showMessageDialog(this, "Login successful!");
    new Dashboard();
    dispose();
 } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        DBHelper.initializeDatabase();  // DB bana dega agar pehli baar hai
        SwingUtilities.invokeLater(LoginForm::new);
    }
}
