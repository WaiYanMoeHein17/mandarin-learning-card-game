
package main_page;

/**
 * SetSelector represents a single flashcard set in the Mandarin learning application.
 * This class encapsulates all metadata and properties of a flashcard set, including:
 * - Identification (set number and creator)
 * - Access control (validity, access level, password)
 * - Content information (titles, terms, name, notes, topic)
 * - Timestamps (creation and update dates)
 */
public class SetSelector {
    /** Unique identifier for the flashcard set */
    private int setNumber;
    
    /** Username of the set's creator */
    private String setCreator;
    
    /** Whether the set is valid and usable */
    private boolean validity;
    
    /** Access level of the set (e.g., "public", "private", "password-protected") */
    private String access;
    
    /** Password for protected sets (null if not password-protected) */
    private String password;
    
    /** Column headers for the flashcard table */
    private String[] tableTitles;
    
    /** Input language or terms type (e.g., "Mandarin", "Pinyin") */
    private String inputTerms;
    
    /** Display name of the flashcard set */
    private String nameOfSet;
    
    /** Additional notes or description for the set */
    private String notesOfSet;
    
    /** Main topic or category of the set */
    private String topicOfSet;
    
    /** Creation date of the set */
    private String dateC;
    
    /** Last update date of the set */
    private String dateU;

    /**
     * Creates a new flashcard set with the specified properties.
     *
     * @param setNumber Unique identifier for this set
     * @param setCreator Username of the set's creator
     * @param validity Whether the set is valid and usable
     * @param access Access level ("public", "private", or "password")
     * @param password Password for protected sets (null if not protected)
     * @param tableTitles Column headers for the flashcard table
     * @param inputTerms Input language or terms type
     * @param nameOfSet Display name of the set
     * @param notesOfSet Additional notes or description
     * @param topicOfSet Main topic or category
     * @param dateC Creation date
     * @param dateU Last update date
     */
    public SetSelector(int setNumber, String setCreator, boolean validity, String access,
                      String password, String[] tableTitles, String inputTerms, String nameOfSet, 
                      String notesOfSet, String topicOfSet, String dateC, String dateU) {
        this.setNumber = setNumber;
        this.setCreator = setCreator;
        this.validity = validity;
        this.access = access;
        this.password = password;
        this.tableTitles = tableTitles;
        this.inputTerms = inputTerms;
        this.nameOfSet = nameOfSet;
        this.notesOfSet = notesOfSet;
        this.topicOfSet = topicOfSet;
        this.dateC = dateC;
        this.dateU = dateU;
    }
            
    /**
     * Gets the unique identifier for this flashcard set.
     * @return The set number
     */
    public int getSetNumber() {
        return setNumber;
    }
    
    /**
     * Gets whether this set is valid and usable.
     * @return true if the set is valid, false otherwise
     */
    public boolean getValidity() {
        return validity;
    }
    
    /**
     * Gets the column headers for the flashcard table.
     * @return Array of table column titles
     */
    public String[] getTableTitles() {
        return tableTitles;
    }
    
    /**
     * Gets the username of the set's creator.
     * @return Creator's username
     */
    public String getSetCreator() {
        return setCreator;
    }
    
    /**
     * Gets the access level of this set.
     * @return Access level ("public", "private", or "password")
     */
    public String getAccess() {
        return access;
    }
    
    /**
     * Gets the password for protected sets.
     * @return Password string, or null if not password-protected
     */
    public String getPassword() {
        return password;
    }
    
    /**
     * Gets the input language or terms type.
     * @return Input terms type (e.g., "Mandarin", "Pinyin")
     */
    public String getInputTerms() {
        return inputTerms;
    }
    
    /**
     * Gets the display name of this set.
     * @return Set name
     */
    public String getNameOfSet() {
        return nameOfSet;
    }
    
    /**
     * Gets any additional notes or description.
     * @return Notes about the set
     */
    public String getNotesOfSet() {
        return notesOfSet;
    }
    
    /**
     * Gets the main topic or category.
     * @return Set topic
     */
    public String getTopicOfSet() {
        return topicOfSet;
    }
    
    /**
     * Updates the creation date.
     * @param date New creation date
     */
    public void setDateC(String date) {
        dateC = date;
    }
    
    /**
     * Gets the creation date.
     * @return Creation date
     */
    public String getDateC() {
        return dateC;
    }
    
    /**
     * Updates the last modification date.
     * @param date New update date
     */
    public void setDateU(String date) {
        dateU = date;
    }
    
    /**
     * Gets the last modification date.
     * @return Last update date
     */
    public String getDateU() {
        return dateU;
    }
}
