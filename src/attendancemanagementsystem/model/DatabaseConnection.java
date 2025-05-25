package attendancemanagementsystem.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/attendance_management";
    private static final String USER = "root";      // Replace with your MySQL username
    private static final String PASSWORD = "QWERTY@2005";     // Replace with your MySQL password

    // Private constructor to prevent instantiation
    private DatabaseConnection() {}

    /**
     * Establishes a connection to the MySQL database.
     * @return Connection object
     * @throws SQLException if connection fails
     */
    public static Connection getConnection() throws SQLException {
        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found!", e);
        }

        // Create and return the connection
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     * Test the database connection (optional - for debugging)
     */
    public static void testConnection() {
        try (Connection conn = getConnection()) {
            System.out.println("Database connection successful!");
        } catch (SQLException e) {
            System.err.println("Database connection failed:");
            e.printStackTrace();
        }
    }
}
 