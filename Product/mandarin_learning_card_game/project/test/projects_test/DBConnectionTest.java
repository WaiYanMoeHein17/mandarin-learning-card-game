package projects_test;

import static org.junit.Assert.*;

import org.junit.Test;

import projects.DBConnection;

/**
 * Tests for DBConnection that assert graceful failure when the MySQL driver/database is unavailable.
 */
public class DBConnectionTest {

    @Test
    public void testGetConnectionThrowsWhenDriverMissing() {
        RuntimeException exception = assertThrows(RuntimeException.class, DBConnection::getConnection);
        assertTrue("Exception message should mention JDBC driver", exception.getMessage().contains("Driver"));
    }

    @Test
    public void testRepeatedCallsRemainDeterministic() {
        RuntimeException first = null;
        RuntimeException second = null;

        try {
            DBConnection.getConnection();
        } catch (RuntimeException e) {
            first = e;
        }

        try {
            DBConnection.getConnection();
        } catch (RuntimeException e) {
            second = e;
        }

        assertNotNull("First call should throw", first);
        assertNotNull("Second call should throw", second);
        assertEquals("Exceptions should have the same message", first.getMessage(), second.getMessage());
    }
}
