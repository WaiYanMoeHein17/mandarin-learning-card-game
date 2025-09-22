package projects;

import java.sql.Connection;
import java.sql.DriverManager;
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
        Connection con = null;
        try {
            // Attempt to establish database connection
            con = DriverManager.getConnection(
                "jdbc:mysql://localhost/database",  // Database URL
                "root",                            // Username
                ""                                 // Password (empty for development)
            );
        } catch (Exception e) {
            // Show user-friendly error message with support information
            JOptionPane.showMessageDialog(
                null,
                "Error receiving data\n" +
                "Check your internet connection or try again later\n" +
                "If error persists contact the following and provide the 'Error Message' if required.\n" +
                "Help Email: ********************\n" +
                "School Website: ****************\n" +
                "Error Message: " + e,
                "WARNING",
                JOptionPane.WARNING_MESSAGE
            );
        }
        return con;
    }
}
