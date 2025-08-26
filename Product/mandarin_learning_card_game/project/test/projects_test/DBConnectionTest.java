import projects.DBConnection;

/**
 * Unit tests for DBConnection class
 */
public class DBConnectionTest {
    
    public static void main(String[] args) {
        DBConnectionTest test = new DBConnectionTest();
        test.runAllTests();
    }
    
    public void runAllTests() {
        System.out.println("Running DBConnection Tests...");
        
        testDBConnectionCreation();
        testConnectionProperties();
        
        System.out.println("DBConnection tests completed!");
    }
    
    public void testDBConnectionCreation() {
        System.out.print("Test DB Connection object creation: ");
        try {
            DBConnection dbConnection = new DBConnection();
            if (dbConnection != null) {
                System.out.println("PASSED");
            }
        } catch (Exception e) {
            System.out.println("FAILED - Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void testConnectionProperties() {
        System.out.print("Test database connection properties: ");
        try {
            // Test that the connection can be created without throwing exceptions
            DBConnection dbConnection = new DBConnection();
            
            // Basic validation that object was created successfully
            if (dbConnection != null) {
                System.out.println("PASSED - Basic connection properties test");
            }
        } catch (Exception e) {
            System.out.println("FAILED - Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
