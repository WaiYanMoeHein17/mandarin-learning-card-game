package flashcards;

/**
 * The Cards class represents a single flashcard in the Mandarin learning card game.
 * Each card is created from a row of data in a flashcard set and can contain:
 * - Multiple terms in different languages/forms
 * - Definitions for learning and practice
 * - Additional notes for context or memory aids
 * - Starred status for marking important or difficult terms
 * 
 * This class is designed to work with the doubly-linked list structure in the 
 * flashcard system, where each card becomes a node in the list. The card's data
 * is displayed in the UI through the Flashcard class.
 * 
 * Key features:
 * - Supports up to 5 different terms/translations per card
 * - Flexible marking of which fields are terms vs definitions
 * - Tracking of starred/important terms
 * - Card numbering for navigation and progress tracking
 */
public class Cards {
    
    /** Raw data array containing all fields (terms, definitions, notes) for this card */
    private String[] data = new String[5];
    
    /** Number of definition fields in this card */
    private int numDefinitions;
    
    /** Number of term fields in this card */
    private int numTerms;
    
    /** Number of additional note fields in this card */
    private int numNotes;
    
    /** Array of terms extracted from the data array */
    private String[] terms;
    
    /** Array of definitions extracted from the data array */
    private String[] definitions;
    
    /** Array of additional notes extracted from the data array */
    private String[] additionalNotes;
    
    /** Array indicating which fields are starred/marked as important */
    private char[] starred;
    
    /** Position of this card in the flashcard set */
    private int cardNumber;
    
    /** SRS level (0=new, 1-5=mastery levels) */
    private int srsLevel = 0;
    
    /** Date of next review in format YYYY-MM-DD */
    private String nextReviewDate = null;
    
    /** Time interval in days until next review */
    private static final int[] SRS_INTERVALS = {1, 3, 7, 14, 30};
    
    /**
     * Returns the SRS intervals array for spaced repetition
     * 
     * @return int array of intervals in days for each SRS level
     */
    public static int[] getSrsIntervals() {
        return SRS_INTERVALS;
    }
    
    /**
     * Creates a new flashcard with the specified content and metadata.
     * 
     * @param a First field content (term/definition/note)
     * @param b Second field content (term/definition/note)
     * @param c Third field content (term/definition/note)
     * @param d Fourth field content (term/definition/note)
     * @param e Fifth field content (term/definition/note)
     * @param s Array marking which fields are starred ('t' for starred)
     * @param def Array marking which fields are definitions ('t' for definition)
     * @param term Array marking which fields are terms ('t' for term)
     * @param notes Array marking which fields are notes ('t' for note)
     * @param cardNo Position of this card in the set
     */
    public Cards(String a, String b, String c, String d, String e, 
                char[] s, char[] def, char[] term, char[] notes, int cardNo) {
        
        // Store the raw field data
        data[0] = a;
        data[1] = b;
        data[2] = c;
        data[3] = d;
        data[4] = e;
        
        // Count and collect definitions
        numDefinitions = countFieldType(def);
        definitions = new String[numDefinitions];
        populateFieldArray(definitions, data, def);
        
        // Count and collect terms
        numTerms = countFieldType(term);
        terms = new String[numTerms];
        populateFieldArray(terms, data, term);
        
        // Count and collect notes
        numNotes = countFieldType(notes);
        additionalNotes = new String[numNotes];
        populateFieldArray(additionalNotes, data, notes);
        // the starred array is copied
        starred = s;
        cardNumber=cardNo;
        //System.out.println("DONE");
        
  
    }
    
    /**
     * Prints all terms in this flashcard to the console.
     * Used for debugging and verification purposes.
     */
    public void printTerms() {
        for(int i = 0; i < terms.length; i++) {
            System.out.println("Term " + (i+1) + ": " + terms[i]);
        }
    }
    
    /**
     * Prints all definitions in this flashcard to the console.
     * Used for debugging and verification purposes.
     */
    public void printDefinitions() {
        for(int i = 0; i < definitions.length; i++) {
            System.out.println("Definition " + (i+1) + ": " + definitions[i]);
        }
    }
    
    /**
     * Prints all additional notes in this flashcard to the console.
     * Used for debugging and verification purposes.
     */
    public void printNotes() {
        for(int i = 0; i < additionalNotes.length; i++) {
            System.out.println("Note " + (i+1) + ": " + additionalNotes[i]);
        }
    }
    
    /**
     * Counts how many fields of a given type exist in the card.
     * 
     * @param typeArray Array marking field types with 't' for true
     * @return Number of fields marked as this type
     */
    private int countFieldType(char[] typeArray) {
        int count = 0;
        for (int i = 0; i < 5; i++) {
            if (typeArray[i] == 't') {
                count++;
            }
        }
        return count;
    }
    
    /**
     * Populates a target array with fields of a specific type from the source data.
     * 
     * @param targetArray Array to populate with matching fields
     * @param sourceData Source data array containing all fields
     * @param typeArray Array marking which fields to include with 't'
     */
    private void populateFieldArray(String[] targetArray, String[] sourceData, char[] typeArray) {
        int targetIndex = 0;
        for (int i = 0; i < 5; i++) {
            if (typeArray[i] == 't') {
                targetArray[targetIndex++] = sourceData[i];
            }
        }
    }
    
    public String getTerms(){
        String termStr = "";
        for(int i=0; i<terms.length; i++){
            termStr = termStr + terms[i]+ "\n";
        }
        //System.out.println(termStr);
        return(termStr);
    }
    
    public String getDefinitions(){
        String defStr = "";
        for(int i=0; i<definitions.length; i++){
            defStr = defStr + definitions[i]+ "\n";
        }
        return(defStr);
    }
    
    public String getNotes(){
        String defNotes = "";
        for(int i=0; i<additionalNotes.length; i++){
            defNotes = defNotes + additionalNotes[i]+ "\n";
        }
        return(defNotes);
    }
    
    public char[] getStarred(){
        return(starred);
    }

    public int getCardNumber(){
        return cardNumber;
    }
    
    /**
     * Gets the current SRS level of this card.
     * 
     * @return The SRS level (0=new, 1-5=mastery levels)
     */
    public int getSrsLevel() {
        return srsLevel;
    }
    
    /**
     * Sets a new SRS level for this card.
     * 
     * @param level The new SRS level (0-5)
     */
    public void setSrsLevel(int level) {
        // Keep level within bounds (0-5)
        this.srsLevel = Math.max(0, Math.min(5, level));
    }
    
    /**
     * Gets the date when this card should be reviewed next.
     * 
     * @return The next review date in format YYYY-MM-DD, or null if not scheduled
     */
    public String getNextReviewDate() {
        return nextReviewDate;
    }
    
    /**
     * Sets the next review date for this card.
     * 
     * @param date The next review date in format YYYY-MM-DD
     */
    public void setNextReviewDate(String date) {
        this.nextReviewDate = date;
    }
    
    /**
     * Updates the card's SRS level and next review date based on performance.
     * 
     * @param performance Performance rating (1-5, where 1=poor, 5=excellent)
     * @return The next review date
     */
    public String updateSrsLevel(int performance) {
        // Adjust the SRS level based on performance
        if (performance >= 4) {
            // Good performance = level up (max 5)
            srsLevel = Math.min(5, srsLevel + 1);
        } else if (performance <= 2) {
            // Poor performance = level down (min 0)
            srsLevel = Math.max(0, srsLevel - 1);
        }
        // else performance = 3, maintain current level
        
        // Calculate the next review date
        return calculateNextReviewDate();
    }
    
    /**
     * Calculates the next review date based on the current SRS level.
     * 
     * @return The next review date in format YYYY-MM-DD
     */
    private String calculateNextReviewDate() {
        // If new card (level 0) or reset card, review tomorrow
        int interval = (srsLevel > 0 && srsLevel <= 5) ? SRS_INTERVALS[srsLevel - 1] : 1;
        
        // Calculate date interval days from now
        java.time.LocalDate today = java.time.LocalDate.now();
        java.time.LocalDate nextDate = today.plusDays(interval);
        
        // Format as YYYY-MM-DD
        String formattedDate = nextDate.toString();
        nextReviewDate = formattedDate;
        
        return formattedDate;
    }

    /*
    public int getNumberOfRows(){
        int x = terms.length;
        System.out.println(x);
        return(x);
    }
    */
}
  