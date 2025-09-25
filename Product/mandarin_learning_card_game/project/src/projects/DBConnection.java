package projects;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JOptionPane;

/**
 * Database connection utility class for the Mandarin Learning Card Game.
 * This class provides a centralized way to establish connections to the MySQL database
 * that stores user data, flashcard sets, and learning progress.
 * 
 * The connection is configured to use:
 * - Database server: localhost
 * - Database name: database
 * - Username: root
 * - No password (for development environment)
 * 
 */
public class DBConnection {
    private static final String DB_URL = "jdbc:mysql://localhost/mandarin";
    private static final String USER = "root";
    private static final String PASS = "KevinDeBruyne17@"; // No password for development
    /**
     * Establishes and returns a connection to the MySQL database.
     * 
     * This method attempts to connect to a local MySQL server using predefined
     * credentials. If the connection fails, it displays a user-friendly error
     * message with support contact information.
     * 
     * @return Connection object if successful, null if connection fails
     */
    public static Connection getConnection() {
        //Connection con = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Load MySQL JDBC Driver
            // Attempt to establish database connection
            return DriverManager.getConnection(
                DB_URL,  // Database URL
                USER,    // Username
                PASS     // Password (empty for development)
            );
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL JDBC Driver not found. Include it in your library path.", e);
        } catch (SQLException e) {
            String errorMsg = String.format(
                "Database connection failed:%n" +
                "URL: %s%n" +
                "Error: %s%n" +
                "Check:%n" +
                "1. Is MySQL running?%n" +
                "2. Does database 'database' exist?%n" +
                "3. Are credentials correct?",
                DB_URL, e.getMessage()
            );
            throw new RuntimeException(errorMsg, e);
        }
    }
}
