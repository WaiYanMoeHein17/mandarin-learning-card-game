import flashcards.Cards;

/**
 * Unit tests for Cards class using simple assertions
 */
public class CardsTest {
    
    public static void main(String[] args) {
        CardsTest test = new CardsTest();
        test.runAllTests();
    }
    
    public void runAllTests() {
        System.out.println("Running Cards Tests...");
        
        testCardsCreation();
        testCardsDataAccess();
        
        System.out.println("All tests completed!");
    }
    
    public void testCardsCreation() {
        System.out.print("Test Cards Creation: ");
        try {
            // Create sample data for Cards constructor based on actual constructor
            char[] starred = {'Y', 'N', 'Y', 'N', 'Y'};
            char[] def = {'t', 't', 'f', 't', 'f'};      // 't' for true definition
            char[] term = {'t', 't', 't', 'f', 't'};     // 't' for true term
            char[] notes = {'f', 't', 'f', 't', 'f'};    // 'f' for false note
            
            Cards card = new Cards("Hello", "Ni Hao", "Greeting", "Polite", "Common", 
                                 starred, def, term, notes, 1);
            
            if (card != null) {
                System.out.println("PASSED");
            }
        } catch (Exception e) {
            System.out.println("FAILED - Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void testCardsDataAccess() {
        System.out.print("Test Cards Data Access: ");
        try {
            char[] starred = {'Y', 'N', 'Y', 'N', 'Y'};
            char[] def = {'t', 't', 'f', 't', 'f'};
            char[] term = {'t', 't', 't', 'f', 't'};
            char[] notesArray = {'f', 't', 'f', 't', 'f'};
            
            Cards card = new Cards("Hello", "Ni Hao", "Greeting", "Polite", "Common", 
                                 starred, def, term, notesArray, 1);
            
            // Test accessible methods
            int cardNum = card.getCardNumber();
            String terms = card.getTerms();
            String definitions = card.getDefinitions();
            String notes = card.getNotes();
            
            if (cardNum == 1 && terms != null && definitions != null && notes != null) {
                System.out.println("PASSED");
            } else {
                System.out.println("FAILED - Data access issues");
                System.out.println("  Card Number: " + cardNum);
                System.out.println("  Terms: " + (terms != null ? "OK" : "NULL"));
                System.out.println("  Definitions: " + (definitions != null ? "OK" : "NULL"));
                System.out.println("  Notes: " + (notes != null ? "OK" : "NULL"));
            }
        } catch (Exception e) {
            System.out.println("FAILED - Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
