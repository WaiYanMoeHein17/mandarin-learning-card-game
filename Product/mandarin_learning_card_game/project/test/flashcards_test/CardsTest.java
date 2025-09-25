package flashcards_test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import flashcards.Cards;

/**
 * Unit tests for Cards class using JUnit framework.
 * Tests card creation, data access, field validation, and error handling.
 */
public class CardsTest {
    
    private Cards testCard;
    private char[] starred;
    private char[] def;
    private char[] term;
    private char[] notes;
    
    @Before
    public void setUp() {
        // Initialize test data
        starred = new char[]{'Y', 'N', 'Y', 'N', 'Y'};
        def = new char[]{'t', 't', 'f', 't', 'f'};
        term = new char[]{'t', 't', 't', 'f', 't'};
        notes = new char[]{'f', 't', 'f', 't', 'f'};
        
        testCard = new Cards("Hello", "Ni Hao", "Greeting", "Polite", "Common",
                           starred, def, term, notes, 1);
    }
    
    @Test
    public void testCardCreation() {
        assertNotNull("Card should be created successfully", testCard);
        assertEquals("Card number should be 1", 1, testCard.getCardNumber());
    }
    
    @Test
    public void testBasicDataAccess() {
        // Test the full output of each getter method
        // Since terms, definitions, and notes are defined by the type markers in the constructor,
        // we need to verify which fields were categorized correctly
        
        // Based on our type markers:
        // term[] = {'t', 't', 't', 'f', 't'} → fields 0, 1, 2, 4 are terms
        // def[] = {'t', 't', 'f', 't', 'f'} → fields 0, 1, 3 are definitions
        // notes[] = {'f', 't', 'f', 't', 'f'} → fields 1, 3 are notes
        
        // Verify terms
        String expectedTerms = "Hello\nNi Hao\nGreeting\nCommon\n";
        assertEquals("Terms should match", expectedTerms, testCard.getTerms());
        
        // Verify definitions
        String expectedDefinitions = "Hello\nNi Hao\nPolite\n";
        assertEquals("Definitions should match", expectedDefinitions, testCard.getDefinitions());
        
        // Verify notes
        String expectedNotes = "Ni Hao\nPolite\n";
        assertEquals("Notes should match", expectedNotes, testCard.getNotes());
    }
    
    @Test
    public void testStarredStatus() {
        char[] starredStatus = testCard.getStarred();
        assertArrayEquals("Starred status should match input", starred, starredStatus);
    }
    
    @Test
    public void testTermsAndDefinitions() {
        String terms = testCard.getTerms();
        String definitions = testCard.getDefinitions();
        
        assertNotNull("Terms string should not be null", terms);
        assertNotNull("Definitions string should not be null", definitions);
        assertTrue("Should have term content", terms.trim().length() > 0);
        assertTrue("Should have definition content", definitions.trim().length() > 0);
        assertTrue("Terms should contain Hello", terms.contains("Hello"));
    }
    
    @Test
    public void testCardNumbers() {
        assertEquals("Card number should be set correctly", 1, testCard.getCardNumber());
    }
    
    @Test
    public void testStarredFields() {
        char[] starredStatus = testCard.getStarred();
        assertNotNull("Starred status array should not be null", starredStatus);
        assertEquals("Starred array should have correct length", 5, starredStatus.length);
    }

    @Test
    public void testNotes() {
        String notes = testCard.getNotes();
        assertNotNull("Notes string should not be null", notes);
        assertTrue("Should have notes content", notes.trim().length() > 0);
    }
    
    @Test
    public void testPrintMethods() {
        // Testing that print methods don't throw exceptions
        testCard.printTerms();
        testCard.printDefinitions();
        testCard.printNotes();
    }
}
