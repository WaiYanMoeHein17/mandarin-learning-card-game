package projects_test;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import projects.DBConnection;

/**
 * Unit tests for DBConnection class.
 * Tests database connectivity, connection properties, and error handling.
 */
public class DBConnectionTest {
    
    private Connection connection;
    
    @Before
    public void setUp() {
        connection = null;
    }
    
    @After
    public void tearDown() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                fail("Failed to close database connection: " + e.getMessage());
            }
        }
    }
    
    @Test
    public void testGetConnection_ShouldReturnValidConnection() {
        connection = DBConnection.getConnection();
        assertNotNull("Database connection should not be null", connection);
        
        try {
            assertTrue("Connection should be valid", connection.isValid(5));
            assertFalse("Connection should not be closed", connection.isClosed());
        } catch (SQLException e) {
            fail("Failed to validate connection: " + e.getMessage());
        }
    }
    
    @Test
    public void testConnectionProperties_ShouldHaveCorrectSettings() {
        connection = DBConnection.getConnection();
        
        try {
            assertEquals("Database name should be 'database'", "database", connection.getCatalog());
            assertFalse("Connection should not be read-only", connection.isReadOnly());
            assertTrue("Auto-commit should be enabled", connection.getAutoCommit());
            assertEquals("Transaction isolation should be READ_COMMITTED", 
                Connection.TRANSACTION_READ_COMMITTED, 
                connection.getTransactionIsolation());
        } catch (SQLException e) {
            fail("Failed to check connection properties: " + e.getMessage());
        }
    }
    
    @Test
    public void testConnectionMetadata_ShouldHaveValidMetadata() {
        connection = DBConnection.getConnection();
        
        try {
            assertNotNull("Database metadata should not be null", connection.getMetaData());
            assertTrue("Should be using MySQL", 
                connection.getMetaData().getDatabaseProductName().toLowerCase().contains("mysql"));
        } catch (SQLException e) {
            fail("Failed to check database metadata: " + e.getMessage());
        }
    }
    
    @Test
    public void testMultipleConnections_ShouldCreateSeparateInstances() {
        Connection conn1 = DBConnection.getConnection();
        Connection conn2 = DBConnection.getConnection();
        
        assertNotNull("First connection should not be null", conn1);
        assertNotNull("Second connection should not be null", conn2);
        assertNotSame("Connections should be separate instances", conn1, conn2);
        
        try {
            conn1.close();
            conn2.close();
        } catch (SQLException e) {
            fail("Failed to close test connections: " + e.getMessage());
        }
    }
    
    @Test
    public void testConnectionClose_ShouldCleanupProperly() {
        connection = DBConnection.getConnection();
        
        try {
            assertFalse("Connection should start open", connection.isClosed());
            connection.close();
            assertTrue("Connection should be closed after close()", connection.isClosed());
        } catch (SQLException e) {
            fail("Failed during connection cleanup test: " + e.getMessage());
        }
    }
    
    @Test(expected = SQLException.class)
    public void testInvalidTimeout_ShouldThrowException() throws SQLException {
        connection = DBConnection.getConnection();
        connection.isValid(-1); // Should throw SQLException for negative timeout
    }
    
    @Test
    public void testInvalidCredentials_ShouldFail() {
        try {
            DriverManager.getConnection("jdbc:mysql://localhost/database", "invalid_user", "wrong_pass");
            fail("Should not connect with invalid credentials");
        } catch (SQLException e) {
            assertTrue(e.getMessage().contains("Access denied"));
        }
    }
    
    @Test
    public void testInvalidConnectionString_ShouldFail() {
        try {
            // Try to connect with invalid database name
            DriverManager.getConnection("jdbc:mysql://localhost/nonexistent_db", "root", "");
            fail("Should not connect to non-existent database");
        } catch (SQLException e) {
            assertTrue(e.getMessage().contains("Unknown database"));
        }
    }
    
    @Test
    public void testQueryTimeout_ShouldRespectLimit() throws SQLException {
        connection = DBConnection.getConnection();
        Statement stmt = connection.createStatement();
        stmt.setQueryTimeout(1);
        
        try {
            stmt.execute("SELECT SLEEP(2)");
            fail("Query should timeout");
        } catch (SQLException e) {
            assertTrue(e.getMessage().contains("timeout"));
        }
    }
    
    @Test
    public void testConnectionPool_ShouldNotExceedMaxConnections() {
        List<Connection> connections = new ArrayList<>();
        int maxConnections = 10;
        
        try {
            for (int i = 0; i < maxConnections + 1; i++) {
                connections.add(DBConnection.getConnection());
            }
            fail("Should not allow more than max connections");
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("max_connections"));
        } finally {
            connections.forEach(this::closeQuietly);
        }
    }
    
    private void closeQuietly(Connection conn) {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            // Ignore
        }
    }
}
