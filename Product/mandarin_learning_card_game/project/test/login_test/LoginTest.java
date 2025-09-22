package login_test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import login.LoginScreen;
import projects.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.lang.reflect.Field;

/**
 * Test suite for LoginScreen class.
 * Tests authentication, input validation, and security features.
 */
public class LoginTest {
    
    private LoginScreen loginScreen;
    private Connection testConnection;
    
    @Before
    public void setUp() {
        loginScreen = new LoginScreen();
        testConnection = DBConnection.getConnection();
        
        // Mock UI components that need resources
        try {
            // Use reflection to bypass image loading
            Field buttonField = LoginScreen.class.getDeclaredField("jButton1");
            buttonField.setAccessible(true);
            JButton button = new JButton();
            button.setIcon(null); // Skip image loading in tests
            buttonField.set(loginScreen, button);
        } catch (Exception e) {
            fail("Test setup failed: " + e.getMessage());
        }
    }
    
    @Test
    public void testValidLogin() {
        // Test valid credentials
        try {
            String username = "testUser";
            String password = "validPass123";
            
            // First ensure test user exists
            createTestUser(username, password);
            
            // Set input fields through reflection
            setPrivateField("usernameInput", username);
            setPrivateField("passwordInput", password);
            
            // Attempt login
            boolean loginSuccess = attemptLogin(username, password);
            assertTrue("Login should succeed with valid credentials", loginSuccess);
            
            // Clean up
            removeTestUser(username);
        } catch (Exception e) {
            fail("Valid login test failed: " + e.getMessage());
        }
    }
    
    @Test
    public void testInvalidLogin() {
        try {
            String username = "nonexistentUser";
            String password = "wrongPass";
            
            // Set input fields
            setPrivateField("usernameInput", username);
            setPrivateField("passwordInput", password);
            
            // Attempt login
            boolean loginSuccess = attemptLogin(username, password);
            assertFalse("Login should fail with invalid credentials", loginSuccess);
        } catch (Exception e) {
            fail("Invalid login test failed: " + e.getMessage());
        }
    }
    
    @Test
    public void testSQLInjectionAttempt() {
        try {
            String maliciousUsername = "' OR '1'='1";
            String maliciousPassword = "' OR '1'='1";
            
            // Set input fields
            setPrivateField("usernameInput", maliciousUsername);
            setPrivateField("passwordInput", maliciousPassword);
            
            // Attempt login
            boolean loginSuccess = attemptLogin(maliciousUsername, maliciousPassword);
            assertFalse("Login should fail for SQL injection attempt", loginSuccess);
        } catch (Exception e) {
            fail("SQL injection test failed: " + e.getMessage());
        }
    }
    
    @Test
    public void testEmptyCredentials() {
        try {
            // Test empty username
            setPrivateField("usernameInput", "");
            setPrivateField("passwordInput", "somepass");
            assertFalse("Login should fail with empty username", 
                attemptLogin("", "somepass"));
            
            // Test empty password
            setPrivateField("usernameInput", "someuser");
            setPrivateField("passwordInput", "");
            assertFalse("Login should fail with empty password", 
                attemptLogin("someuser", ""));
            
            // Test both empty
            setPrivateField("usernameInput", "");
            setPrivateField("passwordInput", "");
            assertFalse("Login should fail with empty credentials", 
                attemptLogin("", ""));
        } catch (Exception e) {
            fail("Empty credentials test failed: " + e.getMessage());
        }
    }
    
    @Test
    public void testSpecialCharactersInCredentials() {
        try {
            String[] specialChars = {
                "<script>alert('xss')</script>",
                "'; DROP TABLE users--",
                "%00",  // Null byte
                "' UNION SELECT '",
                "admin'--"
            };
            
            for (String maliciousInput : specialChars) {
                setPrivateField("usernameInput", maliciousInput);
                setPrivateField("passwordInput", "password");
                assertFalse("Login should fail with malicious input: " + maliciousInput,
                    attemptLogin(maliciousInput, "password"));
            }
        } catch (Exception e) {
            fail("Special characters test failed: " + e.getMessage());
        }
    }
    
    // Helper methods
    
    private void createTestUser(String username, String password) throws SQLException {
        try (PreparedStatement stmt = testConnection.prepareStatement(
                "INSERT INTO users (username, password, forename, surname, admin) VALUES (?, ?, 'Test', 'User', 'N')")) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.executeUpdate();
        }
    }
    
    private void removeTestUser(String username) throws SQLException {
        try (PreparedStatement stmt = testConnection.prepareStatement(
                "DELETE FROM users WHERE username = ?")) {
            stmt.setString(1, username);
            stmt.executeUpdate();
        }
    }
    
    private boolean attemptLogin(String username, String password) {
        try (PreparedStatement stmt = testConnection.prepareStatement(
                "SELECT * FROM users WHERE username = ? AND password = ?")) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            return false;
        }
    }
    
    private void setPrivateField(String fieldName, String value) throws Exception {
        Field field = LoginScreen.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        JTextField textField = (JTextField) field.get(loginScreen);
        textField.setText(value);
    }
}
