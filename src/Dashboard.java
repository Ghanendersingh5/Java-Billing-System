package src;

import javax.swing.*;
import java.awt.*;

public class Dashboard extends JFrame {

    public Dashboard() {
        setTitle("Dashboard");
        setSize(400, 350);
        setLayout(new GridLayout(4, 1, 10, 10)); // 4 rows, 1 column
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JButton salesBtn = new JButton("Sales Form");
        JButton purchaseBtn = new JButton("Goods Receive");
        JButton productBtn = new JButton("Product Master");
        JButton logoutBtn = new JButton("Logout");

        add(salesBtn);
        add(purchaseBtn);
        add(productBtn);
        add(logoutBtn);

        // Action Listeners
        salesBtn.addActionListener(e -> new SalesForm());
        purchaseBtn.addActionListener(e -> new PurchaseForm());
        productBtn.addActionListener(e -> new ProductMasterForm()); // ✅ NEW
        logoutBtn.addActionListener(e -> {
            dispose(); // Close current window
            new LoginForm(); // Go back to login
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Dashboard::new);
    }
}
