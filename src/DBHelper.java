package src;

import java.sql.*;

public class DBHelper {
    private static final String DB_URL = "jdbc:sqlite:database.db";

    public static Connection connect() {
        try {
            return DriverManager.getConnection(DB_URL);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void initializeDatabase() {
        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {

            // Operators Table
            String createOperators = "CREATE TABLE IF NOT EXISTS operators (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "username TEXT NOT NULL," +
                    "password TEXT NOT NULL)";
            stmt.execute(createOperators);

            // Default 2 operators (agar pehle se na ho)
            String insertDefault = "INSERT INTO operators (username, password) " +
                    "SELECT 'admin', 'admin' WHERE NOT EXISTS " +
                    "(SELECT 1 FROM operators WHERE username = 'admin')";
            stmt.execute(insertDefault);

            String insertSecond = "INSERT INTO operators (username, password) " +
                    "SELECT 'operator', '1234' WHERE NOT EXISTS " +
                    "(SELECT 1 FROM operators WHERE username = 'operator')";
            stmt.execute(insertSecond);

            System.out.println("Database initialized.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
