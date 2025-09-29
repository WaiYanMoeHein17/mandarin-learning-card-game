package flashcards;

/**
 * The FlashcardSelector class provides the interface for configuring and initializing
 * flashcard study sessions in the Mandarin learning game. It processes raw flashcard data
 * from the database and allows users to configure how each field should be treated
 * (as terms, definitions, or notes).
 *
 * Key features:
 * - Decodes and displays flashcard data in a table view
 * - Allows selection of which columns represent terms, definitions, and notes
 * - Supports flagging/starring specific terms for review
 * - Provides shuffling capability for randomized study
 * - Handles access control for private/shared flashcard sets
 * - Manages navigation between study and creation modes
 *
 * Data Structure:
 * - Rows represent individual flashcards
 * - Columns represent different fields (terms, translations, notes)
 * - Maximum 5 fields per card, configurable as terms/definitions/notes
 * - Maintains starred/flagged status per field
 *
 * UI Components:
 * - Table view showing all flashcard data
 * - Radio buttons for term/definition/note selection
 * - Flag selectors for marking important terms
 * - Controls for shuffling and starting study session
 * - Access control for editing private sets
 *
 * Note on variables:
 * - wordNumber: Number of rows/cards in the set
 * - numColumns: Number of fields per card (max 5)
 */
import auxillary_functions.search;
import main_page.MainPage;
import main_page.SetSelector;
import creator.createSet;
import projects.fileReplacer;
import java.awt.Color;
import java.awt.Component;
import java.util.*;
import javax.swing.table.DefaultTableModel;
import java.util.Random;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.UIManager;


public class FlashcardSelector extends javax.swing.JFrame {

    // Navigation and parent frame references
    /** Reference to the main page that opened this selector */
    private MainPage prevFrame;
    /** Reference to the search page that opened this selector */
    private search prevSearchFrame;
    /** Flag indicating whether this was opened from search (true) or main page (false) */
    private boolean search = false;
    
    // Set metadata
    /** Unique identifier for this flashcard set */
    private int setNo;
    /** Name/title of the flashcard set */
    private String setName;
    /** Username of the set creator */
    private String setCreator;
    /** Topic/category of the flashcard set */
    private String topic;
    /** Creation date of the set */
    private String dateCreated;
    /** Last modification date of the set */
    private String dateUpdated;
    /** Description or additional notes about the set */
    private String setNotes;
    /** Formatted notes text for display */
    private String notesText = "";
    
    // Access control
    /** Current logged-in username */
    private String currentUser;
    /** Access level for this set (public/private) */
    private String access;
    /** Password for protected sets */
    private String password;
    /** Whether user access has been validated */
    private boolean validated;

    // Card data structure
    /** Raw input data string from database */
    private String dataInput;
    /** Number of fields per card (1-5) */
    private int numColumns;
    /** Current column being processed */
    private int columnNumber = 0;
    /** Total number of cards in the set */
    private int wordNumber = 0;
    /** List of all card data, each inner list represents one card's fields */
    private ArrayList<ArrayList<String>> terms2 = new ArrayList<>();
    /** Column titles for the table view */
    private String[] tableTitles;
    
    // Field type markers
    /** Marks which fields are definitions ('t' for true) */
    private char definitions[] = {'f','f','f','f','f'};
    /** Marks which fields are terms ('t' for true) */
    private char terms[] = {'f','f','f','f','f'};
    /** Marks which fields are notes ('t' for true) */
    private char notes[] = {'f','f','f','f','f'};
    
    // Starring/flagging system
    /** Number of terms that can be starred */
    private int numStarred;
    /** Matrix of starred status for each field */
    private char[][] starred;
    /** Temporary storage for starred status */
    private char[] tempStarred;
    /** Names/labels for starred term categories */
    private String[] starredName;
    /** CSV string storing starred status */
    private String csvStarredStore = "0";
    /** Display markers for starred terms */
    private long[] displayStarred;
    /** Filtered view of starred terms */
    private char filteredStarred[] = {'f','f','f','f','f'};
    
    // Shuffling support
    /** Original card order before shuffling */
    private ArrayList<Integer> cardNo = new ArrayList<Integer>();
    /** Card data in shuffled order */
    private ArrayList<ArrayList<String>> terms2shuffled = new ArrayList<>();
    /** Card numbers in shuffled order */
    private ArrayList<Integer> cardNoShuffled = new ArrayList<Integer>();
    /** Whether cards are currently shuffled */
    private boolean shuffled = false;
    
    /** Number of terms currently selected */
    private int numberSelected = 0;
    
    /**
     * Creates a new FlashcardSelector window opened from the main page.
     * This constructor initializes the selector with a flashcard set's data
     * and configures access permissions based on the current user.
     *
     * @param prevFrame The MainPage window that opened this selector
     * @param currentUser Username of the currently logged in user
     * @param ss The SetSelector containing the flashcard set's metadata
     * @param starredNames Array of labels for starred term categories
     * @param starredInput String encoding which terms are starred
     * @param setNum Unique identifier for this flashcard set
     */
    public FlashcardSelector(MainPage prevFrame, String currentUser, SetSelector ss, String[] starredNames, String starredInput, int setNum) {
        // Set system look and feel for modern appearance
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("Error setting Look and Feel: " + e);
        }
        
        initComponents();
        this.setLocationRelativeTo(null);   
        this.prevFrame=prevFrame;
        
        // Add keyboard shortcuts
        addKeyboardShortcuts();
        
        search=false;
        
        //get variables required to check if user has access
        setCreator=ss.getSetCreator();
        validated=ss.getValidity();
        access=ss.getAccess();
        setCurrentUser(currentUser);
        password = ss.getPassword();
        
        //check if user has access
        if(check()==true){
            
            //if they do have access:
            //1st decode and apply the data essential to the programs functions
            tableTitles=ss.getTableTitles();
            EnterData(ss.getInputTerms(),tableTitles);
            //updateTable();
            
            //decode and update Starred system
            
            setStarredName(starredNames);
            decodeStarred(starredInput);
            resetdisplayStarred();
            
            //set auxillary data: e.g. dates/topics for display
            resetDisplay();
            setNo=setNum;
            setsetName(ss.getNameOfSet());
            setsetNotes(ss.getNameOfSet());
            setTopic(ss.getTopicOfSet());
            setDateCreated(ss.getDateC());
            setLastUpdated(ss.getDateU());
            setSetNumber(ss.getSetNumber());
            

            //System.out.println(numColumns);
            updateTable();
            
            prevFrame.setVisible(false);
            this.setVisible(true);
            
        //otherwiser Deny access   
        }else{
            JOptionPane.showMessageDialog(null,"Sorry you cannot access this set right now", "Access Denied", JOptionPane.ERROR_MESSAGE);
            this.dispose();
        }
    }
    
    /**
     * Creates a new FlashcardSelector window opened from the search page.
     * This constructor initializes the selector with found flashcard data
     * and configures access permissions based on the current user.
     *
     * @param prevFrame The search window that opened this selector
     * @param currentUser Username of the currently logged in user
     * @param ss The SetSelector containing the flashcard set's metadata
     * @param starredNames Array of labels for starred term categories
     * @param starredInput String encoding which terms are starred
     */
    public FlashcardSelector(search prevFrame, String currentUser, SetSelector ss, String[] starredNames, String starredInput) {
        // Set system look and feel for modern appearance
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("Error setting Look and Feel: " + e);
        }
        
        initComponents();
        this.setLocationRelativeTo(null);   
        this.prevSearchFrame=prevFrame;
        
        // Add keyboard shortcuts
        addKeyboardShortcuts();
        search=true;
        
        //get variables required to check if user has access
        setCreator=ss.getSetCreator();
        validated=ss.getValidity();
        access=ss.getAccess();
        setCurrentUser(currentUser);
        password = ss.getPassword();
        
        //check if user has access
        if(check()==true){
            
            //if they do have access:
            //1st decode and apply the data essential to the programs functions
            tableTitles=ss.getTableTitles();
            EnterData(ss.getInputTerms(),tableTitles);
            //updateTable();
            
            //decode and update Starred system
            
            setStarredName(starredNames);
            decodeStarred(starredInput);
            resetdisplayStarred();
            
            //set auxillary data: e.g. dates/topics for display
            resetDisplay();
            setsetName(ss.getNameOfSet());
            setsetNotes(ss.getNameOfSet());
            setTopic(ss.getTopicOfSet());
            setDateCreated(ss.getDateC());
            setLastUpdated(ss.getDateU());
            setSetNumber(ss.getSetNumber());

            //System.out.println(numColumns);
            updateTable();
            
            prevSearchFrame.setVisible(false);
            this.setVisible(true);
            
        //otherwiser Deny access   
        }else{
            JOptionPane.showMessageDialog(null,"Sorry this set has not been verified by an admin yet. \nIf you require urgent access please contact the subject admin or set creator.", "Access Denied", JOptionPane.ERROR_MESSAGE);
            this.dispose();
        }
    }
    
    /**
     * Processes raw flashcard data received from the database.
     * This method decodes a specially formatted string containing all flashcard data
     * and initializes the internal data structures. The input string format is:
     * - First character: number of fields per card (1-5)
     * - Remaining characters: concatenated card data fields
     * 
     * @param s The encoded flashcard data string from database
     * @param x Array of column titles for the table view
     */
    public void EnterData(String s, String[] x) {
        settableTitles(x);
        columnNumber=0;    
        int pointer=0;
        cardNo = new ArrayList<>();
        terms2 = new ArrayList<>();
        //decoding data by getting first digit(# of potential terms per row)
        dataInput=s;
        numColumns = Character.getNumericValue(dataInput.charAt(0));
            //System.out.println("di"+dataInput);
            //System.out.println("0th char "+ dataInput.charAt(0));
            //System.out.println(numColumns);
        dataInput=dataInput.substring(1);
         String word="";
            //terms = new ArrayList[numColumns][];
        
        //breaks up data into words     
        while(pointer<dataInput.length()){
            ArrayList<String> temp = new ArrayList<>();
            wordNumber=0;
            //while a new row is not needed...
            while((dataInput.charAt(pointer))!='/'){
                word = "";
                //while the word is not finished (i.e. not inidicated by a comma, add on to word one letter at a time)
                while((dataInput.charAt(pointer))!=','){
                    word = word+dataInput.charAt(pointer);
                        //System.out.println(pointer);
                    pointer = pointer +1;
                }
                    //terms[columnNumber][wordNumber].add(word);
                temp.add(word);
                wordNumber = wordNumber + 1;
                pointer=pointer+1;
                
                
            }
            //once set of words are in the array and a the row is complete, add to the 2d array and move to next row
            terms2.add(temp);
            columnNumber=columnNumber+1;
                //wordNumber=wordNumber+1;
                //System.out.println("wn="+wordNumber);
                //System.out.println("NC"+numColumns);
            pointer+=1;               
        }
        for(int i=0;i<wordNumber;i++){
           cardNo.add(i+1);
        }
        
        updateNoButton();
        
        terms2shuffled = new ArrayList<>();
        
        for(int i =0; i<terms2.size();i++){
               terms2shuffled.add(new ArrayList<>());
               for(int j=0; j<terms2.get(i).size();j++){
                   terms2shuffled.get(i).add(" ");
                   //System.out.println(terms2shuffled);
                   terms2shuffled.get(i).set(j,terms2.get(i).get(j));
               }
           }
        

        for(int i:cardNo){
                cardNoShuffled.add(cardNo.get(i-1));
        }
        for(int i =0; i<5;i++){
           definitions[i]='f';
           terms[i]='f';
           notes[i]='f';
           filteredStarred[i]='f';
        }
        
        
               //printTerms();
    }
    
    public void decodeStarred(String x){
        
        csvStarredStore=x;
        numStarred=Character.getNumericValue(x.charAt(0));
            //System.out.println(numStarred);
        if(numStarred!=0){
            char[][] temp = new char[((x.length()-1)/numStarred)][numStarred];
            for(int i = 0;i<((x.length()-1)/numStarred);i++){
                for(int j = 0; j<numStarred; j++){
                    temp[i][j]=x.charAt((i*numStarred)+j+1);
                }
            }

            starred=temp;


            for(int i =0; i<starred.length;i++){
                for(int j =0; j<starred[i].length;j++){
                    //System.out.print((starred[i][j]));
                //System.out.print("\n");
                }
            }
        }
        else{
            char[][] temp = new char[wordNumber][0];
                for(int i = 0;i<temp.length;i++){
                    for(int j = 0; j<temp[i].length; j++){
                    //temp[i][j]=;
                }
            }
                
            starred=temp;
        }
        
        updateButtons(numStarred);
    }
    
    //go through the 2d array accordingly
    public void printTerms(){
        System.out.println("Columns="+numColumns);
        System.out.println("rows="+wordNumber);
        for(int i = 0; i<numColumns;i++){
             System.out.println("");
            for(int j = 0; j<wordNumber;j++){
                    //System.out.println("j="+j);
                    //System.out.println(terms[i][j]);
                System.out.print(terms2.get(i).get(j)+",");
            }
        }            
    }

    //Setting auxillary details (the same of the set, creator and extra details)
    public void setsetName (String setName){
        this.setName=setName;
        SetTitle.setText(setName);
    }
    public void setsetCreator (String setCreator){
        this.setCreator=setCreator;
        notesText=notesText+"Creator: "+ setCreator+"\n";
    }
    public void setsetNotes (String setNotes){
        this.setNotes=setNotes;
        notesText=notesText+"Set No:" + setNo + "\n" + "Notes: "+ setNotes+"\n";
    }

    public void setTopic(String x){
        topic=x;
        notesText=notesText+"Topic: " + topic +"\n";  
    }

    public void setDateCreated(String x){
            //dateCreated = LocalDate.parse(x);
        dateCreated=x;
        notesText=notesText+"Date Created: " + dateCreated.toString() +"\n";  
    }

    public void setLastUpdated(String x){
            //dateUpdated = LocalDate.parse(x);
        dateUpdated=x;
        notesText=notesText+"Last Updated: " + dateUpdated.toString() +"\n";
        //System.out.println(notesText);
        SetDescription.setText(notesText);
    }

    public void setStarredName(String[] x){
        starredName=x;
    }
    
        
    public void settableTitles(String[] x){
        tableTitles = new String[x.length];
        for(int i=0;i<x.length;i++){
            tableTitles[i]=x[i];
            //System.out.println(tableTitles[i]);
        }
        for (int i=0; i<wordNumber; i++){
            //System.out.println(starredName[i]);
        }
    }
        
    
    public void setsetPassword(String x){
        password=x;
    }
    
    public void resetDisplay(){
        SetDescription.setText("");
        notesText="";
                
    }
    
    // REPLACED BY UPDATETABLE()
    public void displayTable(){
        //creates the table for display
        DefaultTableModel model = (DefaultTableModel)this.TableOfCards.getModel();   
        for(int i =0; i< wordNumber;i++){
        }
    }


    /**
     * Creates a DoublyLinkedList containing Card objects based on current settings.
     * This method:
     * 1. Validates that there are both terms and definitions selected
     * 2. Creates Card objects from each row of data
     * 3. Links cards together in a doubly-linked list
     * 4. Applies any shuffling if enabled
     * 
     * @return A new DoublyLinkedList containing all cards, or null if validation fails
     */
    public DoublyLinkedList createDLL() {
        // Check if we have both terms and definitions selected
        boolean containD = false;
        boolean containT = false;
        for(int i = 0; i<numColumns;i++){
            if(terms[i]=='t'){
                containT=true;
            }
            if(definitions[i]=='t'){
                containD=true;
            }
        }
        
        if(containT==false){
            JOptionPane.showMessageDialog(this,"Make sure at least one term is selected","Term not selected",JOptionPane.ERROR_MESSAGE);
        }else{
            if(containD==false){
                JOptionPane.showMessageDialog(this,"Make sure at least one defintion is selected","Defintion not selected",JOptionPane.ERROR_MESSAGE);
            }else{
        
            int cardsEntered=0;


                boolean filterApplied = false;
                int cardNoTemp;

                for(int i = 0; i<5;i++){
                    if(filteredStarred[i]=='t'){
                        filterApplied = true;
                        //System.out.println(i);
                    }
                }

                //this will create a new DLL
                DoublyLinkedList DLL = new DoublyLinkedList();

                if(filterApplied == false){


                    //for every column
                    for(int i = 0; i<wordNumber;i++){
                        //create a temp array for the words
                            String[] tempWords = new String[5];
                        for(int j = 0; j<numColumns;j++){
                            tempWords[j]=terms2shuffled.get(j).get(i); 
                                //System.out.println(tempWords[j]);          
                        }
                        
                    cardNoTemp=cardNoShuffled.get(i); 
                    //System.out.println("CNS" + cardNoShuffled.size());
                    //System.out.println("ST"+starred.length);
                    tempStarred=starred[cardNoShuffled.get(i)-1];

                    //System.out.println(starred[cardNoShuffled.get(i)-1]);
                    //Creates a new flashcard
                    Cards tempCard = new Cards(tempWords[0],tempWords[1],tempWords[2],tempWords[3],tempWords[4],tempStarred,definitions,terms,notes,cardNoTemp);
                    cardsEntered+=1;
                    //Add to DLL
                    DLL.additem(tempCard);
                    }
                                
                }else{
                    

                    //for every column
                    for(int i = 0; i<wordNumber;i++){
                        //create a temp array for the words
                        boolean valid = true;
                        //System.out.println("false");
                        for(int k = 0; k<5; k++){
                            if (filteredStarred[k]=='t'){
                                if(starred[i][k]=='f'){
                                    valid = false;
                                }
                            }
                        }
                        if(valid==true){
                                String[] tempWords = new String[5];
                            for(int j = 0; j<numColumns;j++){
                                tempWords[j]=terms2shuffled.get(j).get(i); 
                                //System.out.println(tempWords[j]);          
                            }

                            tempStarred=starred[i];
                            cardNoTemp=cardNoShuffled.get(i);

                            //Creates a new flashcard
                            Cards tempCard = new Cards(tempWords[0],tempWords[1],tempWords[2],tempWords[3],tempWords[4],tempStarred,definitions,terms,notes,cardNoTemp);
                            cardsEntered+=1;
                            //Add to DLL
                            DLL.additem(tempCard);
                        }
                    }
                }
                if(cardsEntered==0){
                    JOptionPane.showMessageDialog(this,"There are no valid cards to display as flashcards","Error",JOptionPane.ERROR_MESSAGE);
                    return(null);
                }else{
                return(DLL);
                }
            }
        }  
        return(null);
    }

    /**
     * Updates the table view with current flashcard data.
     * This method:
     * 1. Creates a data matrix from the current card set
     * 2. Sets up column headers with appropriate titles
     * 3. Configures column widths for optimal display
     * 4. Highlights starred terms with special formatting
     * 5. Refreshes the table view with updated data
     */
    public void updateTable() {
        // Get the table model for updates
        // Prepare data for table display
        String[][] tableData = new String[wordNumber][numColumns];
        for (int i = 0; i < wordNumber; i++) {
            for (int j = 0; j < numColumns; j++) {
                tableData[i][j] = terms2.get(j).get(i);
            }
        }
        
        // Update table model with data
        ((DefaultTableModel)this.TableOfCards.getModel()).setDataVector(tableData, tableTitles);
       
        for(int i =0;i<numColumns;i++){
            TableOfCards.getColumnModel().getColumn(i).setCellRenderer(new Renderer(displayStarred));
        }
        
        for(int i =0;i<wordNumber;i++){
            for(int j=0;j<numColumns;j++){
                //TableOfCards.getColumnModel().getColumn(i).setCellRenderer(new Renderer(displayStarred[i],j,false));
            }
        }
        
        
        for(int i = 0;i<wordNumber;i++){
               // TableOfCards.getColumnModel().getColumn(i).setCellRenderer(new Renderer(displayStarred[i],0));
            }
        
        /*for(int p=0; p<displayStarred.length; p++){
            System.out.println("p:" + p);
            System.out.println("x" + displayStarred[p]);
            for(int q = 0;q<wordNumber;q++){
                TableOfCards.getColumnModel().getColumn(q).setCellRenderer(new Renderer(displayStarred[p],3));
            }
        }
        */

        //loop for each record in the array
        //for(int j=0; j<numColumns;j++){
           // for(int i=0; i<wordNumber;i++){

                //System.out.println(tableData[j][i]);

                //row[j] = terms2.get(j).get(i);


                //add row to table
               // model.addRow(row);  
          //  }  
         //}  
        
        for (int c = 0; c < TableOfCards.getColumnCount(); c++)
            {
                Class<?> col_class = TableOfCards.getColumnClass(c);
                TableOfCards.setDefaultEditor(col_class, null); 
            }
        
        
        //model.setValueAt("s", 1, 1);
    }


    /**
     * Updates the visibility of field type selector buttons.
     * Shows or hides term/definition/note selectors based on the 
     * number of available fields per card (numColumns).
     */
    public void updateNoButton() {
        // Reset all selectors to visible by default

        SelectorD5.setVisible(true);
        SelectorN5.setVisible(true);
        SelectorT5.setVisible(true);
        SelectorD4.setVisible(true);
        SelectorN4.setVisible(true);
        SelectorT4.setVisible(true);       
        SelectorD3.setVisible(true);
        SelectorN3.setVisible(true);
        SelectorT3.setVisible(true);  

        if(numColumns < 5){
            SelectorD5.setVisible(false);
            SelectorN5.setVisible(false);
            SelectorT5.setVisible(false);
        }
        if(numColumns<4){
            SelectorD4.setVisible(false);
            SelectorN4.setVisible(false);
            SelectorT4.setVisible(false);       
        }
        if(numColumns<3){
            SelectorD3.setVisible(false);
            SelectorN3.setVisible(false);
            SelectorT3.setVisible(false);       
        }

    } 
    
    /**
     * Updates the visibility of flag/star selectors.
     * Shows or hides flag selectors and their labels based on how many
     * terms can be flagged in this flashcard set.
     * 
     * @param x Number of terms that can be flagged (1-5)
     */
    public void updateButtons(int x) {
        flagtermselector5.setVisible(true);
        flagtermselector4.setVisible(true);
        flagtermselector3.setVisible(true);
        flagtermselector2.setVisible(true);
        flagtermselector1.setVisible(true);
        lablel1.setVisible(true);
        lablel2.setVisible(true);
        lablel3.setVisible(true);
        lablel4.setVisible(true);
        lablel5.setVisible(true);
        lablel1.setText(starredName[0]);
        lablel2.setText(starredName[1]);
        lablel3.setText(starredName[2]);
        lablel4.setText(starredName[3]);
        lablel5.setText(starredName[4]);
        
        
        if(x<5){
            flagtermselector5.setVisible(false);
            lablel5.setVisible(false);
        }
        if(x<4){
            flagtermselector4.setVisible(false);
            lablel4.setVisible(false);
        }
        if(x<3){
            flagtermselector3.setVisible(false);
            lablel3.setVisible(false);
        }
        if(x<2){
            flagtermselector2.setVisible(false);
            lablel2.setVisible(false);
        }
        if(x<1){
            flagtermselector1.setVisible(false);
            lablel1.setVisible(false);
        }
    }
    
    
    public void changeStarred (int x){
        
        
        System.out.println(csvStarredStore);
        if(csvStarredStore.charAt(x)=='f'){
            csvStarredStore=csvStarredStore.substring(0,x)+'t'+csvStarredStore.substring(x+1);
        }else{
            csvStarredStore=csvStarredStore.substring(0,x)+'f'+csvStarredStore.substring(x+1);
        }
        System.out.println(csvStarredStore);
        new fileReplacer(setNo, 1, csvStarredStore); // Update starred terms in database
    }
    
    public void shuffle(){
        
        //System.out.println("before" + terms2shuffled);
        System.out.println("before" + cardNoShuffled);

        if(shuffled==false){
            
            Random random = new Random();
                                 
            ArrayList<ArrayList<String>> intermediateTerms = new ArrayList<>();
            ArrayList<Integer> intermediateCardNo = new ArrayList<Integer>();
            terms2shuffled = new ArrayList<>();
            cardNoShuffled = new ArrayList<>();
            
            //ArrayList<String> temp = new ArrayList();
            for(int i = 0;i<columnNumber;i++){
                terms2shuffled.add(new ArrayList<>());
            }
           
           
           for(int i =0; i<terms2.size();i++){
               intermediateTerms.add(new ArrayList<>());
               for(int j=0; j<terms2.get(i).size();j++){
                   intermediateTerms.get(i).add(" ");
                   //System.out.println(terms2shuffled);
                   intermediateTerms.get(i).set(j,terms2.get(i).get(j));
               }
           }
                                        
            for(int i:cardNo){
                intermediateCardNo.add(cardNo.get(i-1));
            }
            
           
            
            int length = terms2.get(0).size();
            //System.out.println(terms2.get(0).size());
            for(int i =0; i<length;i++){
                //System.out.println("length: "+length);
                int j = random.nextInt(length-i);
                for(int k = 0; k<numColumns;k++){
                    //System.out.println(intermediateTerms.get(k).get(j));
                    terms2shuffled.get(k).add(intermediateTerms.get(k).get(j));
                    intermediateTerms.get(k);
                    intermediateTerms.get(k).remove(j);    
                    
                }
                cardNoShuffled.add(intermediateCardNo.get(j));
                intermediateCardNo.remove(j);
                //System.out.println(intermediateTerms);
                
            }
            
            //System.out.println("after" + terms2shuffled);
            System.out.println("after" + cardNoShuffled);

                        
            shuffled=true;
            
            
        }else{
            
            //System.out.println("terms2" + terms2);
            //System.out.println(cardNo);
            
            cardNoShuffled = new ArrayList<>();

            for(int i =0; i<terms2.size();i++){
                for(int j=0; j<terms2.get(i).size();j++){
                    terms2shuffled.get(i).set(j,terms2.get(i).get(j));
                }
            }
            
            for(int i:cardNo){
                cardNoShuffled.add(cardNo.get(i-1));
            }
            
            shuffled=false;
            System.out.println("after" + cardNoShuffled);
        }

    //System.out.println(terms2shuffled);
        
    }

    public void setCurrentUser(String s){
        currentUser=s;
        if(currentUser.equals(setCreator)==true){
            edit.setVisible(true);
        }else{
         edit.setVisible(false);
        }
    }
    
    public void resetdisplayStarred(){
        displayStarred = new long[wordNumber];
        for(int i=0;i<displayStarred.length;i++){
            displayStarred[i]=0;
        }
    }
    
    // public JFrame getFCSClass(){
    //}
    
    public void setSetNumber(int s){
        setNo=s;
    }
   
    public int getSetNumber(){
        //System.out.println(setNo);
        return(setNo);      
    }
    
    public void changetitle(String t){
        SetTitle.setText(t);
    }
    
    
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        SetDescription = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        TableOfCards = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        Start = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        SelectorD1 = new javax.swing.JRadioButton();
        SelectorT1 = new javax.swing.JRadioButton();
        SelectorN1 = new javax.swing.JRadioButton();
        SelectorD2 = new javax.swing.JRadioButton();
        SelectorT2 = new javax.swing.JRadioButton();
        SelectorN2 = new javax.swing.JRadioButton();
        SelectorD3 = new javax.swing.JRadioButton();
        SelectorN3 = new javax.swing.JRadioButton();
        SelectorT3 = new javax.swing.JRadioButton();
        SelectorD4 = new javax.swing.JRadioButton();
        SelectorT4 = new javax.swing.JRadioButton();
        SelectorN4 = new javax.swing.JRadioButton();
        SelectorN5 = new javax.swing.JRadioButton();
        SelectorD5 = new javax.swing.JRadioButton();
        SelectorT5 = new javax.swing.JRadioButton();
        flagtermselector1 = new javax.swing.JRadioButton();
        flagtermselector2 = new javax.swing.JRadioButton();
        flagtermselector3 = new javax.swing.JRadioButton();
        flagtermselector5 = new javax.swing.JRadioButton();
        flagtermselector4 = new javax.swing.JRadioButton();
        lablel1 = new javax.swing.JLabel();
        lablel2 = new javax.swing.JLabel();
        lablel3 = new javax.swing.JLabel();
        lablel4 = new javax.swing.JLabel();
        lablel5 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        SetTitle = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        edit = new javax.swing.JButton();
        Cancel4 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        SetDescription.setEditable(false);
        SetDescription.setColumns(20);
        SetDescription.setRows(5);
        jScrollPane1.setViewportView(SetDescription);

        TableOfCards.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane2.setViewportView(TableOfCards);

        jButton1.setText("Flagging Setting");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Shuffle 🔀");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        Start.setText("Start");
        Start.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                StartActionPerformed(evt);
            }
        });

        jLabel1.setText("Definitions");

        jLabel2.setText("Terms");

        jLabel3.setText("Notes");

        SelectorD1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SelectorD1ActionPerformed(evt);
            }
        });

        SelectorT1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SelectorT1ActionPerformed(evt);
            }
        });

        SelectorN1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SelectorN1ActionPerformed(evt);
            }
        });

        SelectorD2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SelectorD2ActionPerformed(evt);
            }
        });

        SelectorT2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SelectorT2ActionPerformed(evt);
            }
        });

        SelectorN2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SelectorN2ActionPerformed(evt);
            }
        });

        SelectorD3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SelectorD3ActionPerformed(evt);
            }
        });

        SelectorN3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SelectorN3ActionPerformed(evt);
            }
        });

        SelectorT3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SelectorT3ActionPerformed(evt);
            }
        });

        SelectorD4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SelectorD4ActionPerformed(evt);
            }
        });

        SelectorT4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SelectorT4ActionPerformed(evt);
            }
        });

        SelectorN4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SelectorN4ActionPerformed(evt);
            }
        });

        SelectorN5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SelectorN5ActionPerformed(evt);
            }
        });

        SelectorD5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SelectorD5ActionPerformed(evt);
            }
        });

        SelectorT5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SelectorT5ActionPerformed(evt);
            }
        });

        flagtermselector1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                flagtermselector1ActionPerformed(evt);
            }
        });

        flagtermselector2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                flagtermselector2ActionPerformed(evt);
            }
        });

        flagtermselector3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                flagtermselector3ActionPerformed(evt);
            }
        });

        flagtermselector5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                flagtermselector5ActionPerformed(evt);
            }
        });

        flagtermselector4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                flagtermselector4ActionPerformed(evt);
            }
        });

        lablel1.setText("jLabel4");

        lablel2.setText("jLabel8");

        lablel3.setText("jLabel9");

        lablel4.setText("jLabel10");

        lablel5.setText("jLabel11");

        jPanel1.setBackground(new java.awt.Color(0, 0, 240));

        SetTitle.setFont(new java.awt.Font("Nirmala UI", 1, 36)); // NOI18N
        SetTitle.setForeground(new java.awt.Color(255, 255, 255));
        SetTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        SetTitle.setText("Set name");

        jButton3.setBackground(new java.awt.Color(255, 255, 255));
        jButton3.setFont(new java.awt.Font("Nirmala UI", 1, 14)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ia/projects/help button.png"))); // NOI18N
        jButton3.setBorder(null);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        edit.setText("Edit");
        edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(91, 91, 91)
                .addComponent(SetTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(9, 9, 9)
                .addComponent(edit, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(SetTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(edit, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        Cancel4.setText("Cancel");
        Cancel4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Cancel4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(38, 38, 38)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(SelectorT1)
                                    .addComponent(SelectorD1)
                                    .addComponent(SelectorN1))
                                .addGap(59, 59, 59)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(SelectorT2)
                                    .addComponent(SelectorD2)
                                    .addComponent(SelectorN2))
                                .addGap(56, 56, 56)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(SelectorT3)
                                    .addComponent(SelectorD3)
                                    .addComponent(SelectorN3))
                                .addGap(55, 55, 55)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(SelectorT4)
                                    .addComponent(SelectorD4)
                                    .addComponent(SelectorN4))
                                .addGap(63, 63, 63)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(SelectorT5)
                                    .addComponent(SelectorD5)
                                    .addComponent(SelectorN5))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 52, Short.MAX_VALUE)
                                .addComponent(Start, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 396, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(25, 25, 25)
                                        .addComponent(jButton1))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(24, 24, 24)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(flagtermselector2)
                                            .addComponent(flagtermselector3)
                                            .addComponent(flagtermselector4)
                                            .addComponent(flagtermselector5)
                                            .addComponent(flagtermselector1))
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addGap(5, 5, 5)
                                                .addComponent(lablel1, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addGap(4, 4, 4)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(lablel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(lablel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(lablel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(lablel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)))))))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(Cancel4, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(14, 14, 14))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lablel1)
                                    .addComponent(flagtermselector1))
                                .addGap(15, 15, 15)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(flagtermselector2)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(2, 2, 2)
                                        .addComponent(lablel2)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(flagtermselector3)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addGap(3, 3, 3)
                                        .addComponent(lablel3)))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(21, 21, 21)
                                        .addComponent(lablel4)
                                        .addGap(22, 22, 22))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(flagtermselector4)
                                        .addGap(18, 18, 18)))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(flagtermselector5)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(lablel5)
                                        .addGap(3, 3, 3))))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Start, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(SelectorT5, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(24, 24, 24)
                                .addComponent(jLabel1))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(19, 19, 19)
                                .addComponent(SelectorD5)))
                        .addGap(20, 20, 20)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(SelectorN5)
                            .addComponent(jLabel3)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(SelectorT1)
                                .addComponent(SelectorT2))
                            .addComponent(SelectorT3, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(SelectorT4, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(19, 19, 19)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(SelectorD1)
                                .addComponent(SelectorD2))
                            .addComponent(SelectorD3)
                            .addComponent(SelectorD4))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(SelectorN2)
                            .addComponent(SelectorN1)
                            .addComponent(SelectorN3)
                            .addComponent(SelectorN4))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(Cancel4, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        shuffle();
        if(shuffled==true){
            jButton2.setBackground(Color.decode("#CCDCEC"));
        }else{
            jButton2.setBackground(Color.decode("#DCECFC"));
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void StartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_StartActionPerformed
            
        //if(createDLL()!=null){
            decodeStarred(csvStarredStore);
            FlashCards flashcard = new FlashCards();
            flashcard.prevFrame(this);
            flashcard.setDLL(createDLL());
            flashcard.setsetName(setName);
            flashcard.setVisible(true);
            flashcard.updateButtons(numStarred);
            flashcard.setNumberStarred(numStarred);
            flashcard.setLabelName(starredName);
            flashcard.updateButtonSelected();
            setVisible(false);
        //}        
    }//GEN-LAST:event_StartActionPerformed

    
//Checks the definition radion button lines if they are selected or not and updates the array accordingly    

    private void SelectorD1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SelectorD1ActionPerformed
        if (SelectorD1.isSelected()){
            definitions[0]='t';
        }else{
            definitions[0]='f';
        }    
    }//GEN-LAST:event_SelectorD1ActionPerformed

    private void SelectorD2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SelectorD2ActionPerformed
        if (SelectorD2.isSelected()){
            definitions[1]='t';
        }else{
            definitions[1]='f';
        }    
    }//GEN-LAST:event_SelectorD2ActionPerformed

    private void SelectorD3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SelectorD3ActionPerformed
    if (SelectorD3.isSelected()){
                definitions[2]='t';
            }else{
                definitions[2]='f';
            }    
    }//GEN-LAST:event_SelectorD3ActionPerformed

    private void SelectorD4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SelectorD4ActionPerformed
        if (SelectorD4.isSelected()){
            definitions[3]='t';
        }else{
            definitions[3]='f';
        }    
    }//GEN-LAST:event_SelectorD4ActionPerformed

    private void SelectorD5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SelectorD5ActionPerformed
        if (SelectorD5.isSelected()){
            definitions[4]='t';
        }else{
            definitions[4]='f';
        }    
    }//GEN-LAST:event_SelectorD5ActionPerformed

    
//Checks the terms radio button lines if they are selected or not and updates the array accordingly        
    
    private void SelectorT1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SelectorT1ActionPerformed
        if (SelectorT1.isSelected()){
            terms[0]='t';
        }else{
            terms[0]='f';
        }    
    }//GEN-LAST:event_SelectorT1ActionPerformed

    private void SelectorT2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SelectorT2ActionPerformed
        if (SelectorT2.isSelected()){
            terms[1]='t';
        }else{
            terms[1]='f';
        }    
    }//GEN-LAST:event_SelectorT2ActionPerformed

    private void SelectorT3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SelectorT3ActionPerformed
        if (SelectorT3.isSelected()){
            terms[2]='t';
        }else{
            terms[2]='f';
        }    
    }//GEN-LAST:event_SelectorT3ActionPerformed

    private void SelectorT4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SelectorT4ActionPerformed
        if (SelectorT4.isSelected()){
            terms[3]='t';
        }else{
            terms[3]='f';
        }    
    }//GEN-LAST:event_SelectorT4ActionPerformed

    private void SelectorT5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SelectorT5ActionPerformed
        if (SelectorT5.isSelected()){
            terms[4]='t';
        }else{
            terms[4]='f';
        }    
    }//GEN-LAST:event_SelectorT5ActionPerformed

    
//Checks the notes radio button lines if they are selected or not and updates the array accordingly        

    private void SelectorN1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SelectorN1ActionPerformed
        if (SelectorN1.isSelected()){
            notes[0]='t';
        }else{
            notes[0]='f';
        }    
    }//GEN-LAST:event_SelectorN1ActionPerformed

    private void SelectorN2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SelectorN2ActionPerformed
        if (SelectorN2.isSelected()){
            notes[1]='t';
        }else{
            notes[1]='f';
        } 
    }//GEN-LAST:event_SelectorN2ActionPerformed

    private void SelectorN3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SelectorN3ActionPerformed
        if (SelectorN3.isSelected()){
            notes[2]='t';
        }else{
            notes[2]='f';
        } 
    }//GEN-LAST:event_SelectorN3ActionPerformed

    private void SelectorN4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SelectorN4ActionPerformed
        if (SelectorN4.isSelected()){
            notes[3]='t';
        }else{
            notes[3]='f';
        } 
    }//GEN-LAST:event_SelectorN4ActionPerformed

    private void SelectorN5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SelectorN5ActionPerformed
        if (SelectorN5.isSelected()){
            notes[4]='t';
        }else{
            notes[4]='f';
        } 
    }//GEN-LAST:event_SelectorN5ActionPerformed

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_formKeyPressed

    private void flagtermselector1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_flagtermselector1ActionPerformed
        //if label one clicked
        if (flagtermselector1.isSelected()){
            //the first flag of has been starred 
            filteredStarred[0]='t';
            // Store how many flags have been selected
            numberSelected+=1;
            
            //x indicates which flag is selected. In this case the first flag
           int x=0;
           // go through the string that stores and check every nth value as it corrosponds to flag 1. (n = number of flags initalised by user)
           for (int i=1;i<csvStarredStore.length();i=i+Character.getNumericValue(csvStarredStore.charAt(0))){
               // if the row's matches the star/flag
                if(csvStarredStore.charAt(i)=='t'){
                    // if this row matches with other selected flags (value is not 0)
                    if(displayStarred[x]!=0){
                        //multiply by 100
                       displayStarred[x]=(displayStarred[x]*10)*(1*10);
                    }else{
                        //if this card has not matched a flag set colour value to 1
                        displayStarred[x]=1;
                    }
                }  
            
            x+=1;
            }
            

        }else{
            filteredStarred[0]='f';
            numberSelected-=1;
            int x=0;
            for (int i=1;i<csvStarredStore.length();i=i+Character.getNumericValue(csvStarredStore.charAt(0))){
                if(csvStarredStore.charAt(i)=='t'){
                    if(displayStarred[x]>10){
                       displayStarred[x]=(displayStarred[x]/10)/10;
                    }else{
                        if(displayStarred[x]==1){
                            displayStarred[x]=0;
                        }else{
                            displayStarred[x]=displayStarred[x];
                        }
                }
            
            }
                x+=1;
            }
        } 
        
        for(int p=0; p<displayStarred.length; p++){
           System.out.print(displayStarred[p]+",");
        }
        System.out.println("");

        
        updateTable();
    }//GEN-LAST:event_flagtermselector1ActionPerformed

    private void flagtermselector2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_flagtermselector2ActionPerformed
        if (flagtermselector2.isSelected()){
           filteredStarred[1]='t';
           numberSelected+=1;

           int x=0;
           for (int i=2;i<csvStarredStore.length();i=i+Character.getNumericValue(csvStarredStore.charAt(0))){
                //System.out.println(csvStarredStore.charAt(i));
                if(csvStarredStore.charAt(i)=='t'){
                    if(displayStarred[x]!=0){
                       displayStarred[x]=(displayStarred[x]*10)*(2*10);
                    }else{
                        displayStarred[x]=2;
                    }
                }  
            x+=1;
            }
           
        }else{
           filteredStarred[1]='f';
           numberSelected-=1;
           
            int x=0;
            for (int i=2;i<csvStarredStore.length();i=i+Character.getNumericValue(csvStarredStore.charAt(0))){
                if(csvStarredStore.charAt(i)=='t'){
                if(displayStarred[x]>10){
                   displayStarred[x]=(displayStarred[x]/20)/10;
                }else{
                   if(displayStarred[x]==2){
                        displayStarred[x]=0;
                   }
                }
                }
            x+=1;
            }
        }
        
        for(int p=0; p<displayStarred.length; p++){
           System.out.print(displayStarred[p]+",");
        }
        System.out.println("");
        
        updateTable();
    }//GEN-LAST:event_flagtermselector2ActionPerformed

    private void flagtermselector3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_flagtermselector3ActionPerformed
        if (flagtermselector3.isSelected()){
            filteredStarred[2]='t';
            numberSelected+=1;
            
            int x=0;
            for (int i=3;i<csvStarredStore.length();i=i+Character.getNumericValue(csvStarredStore.charAt(0))){
                // System.out.println(csvStarredStore.charAt(i));
                 if(csvStarredStore.charAt(i)=='t'){
                     if(displayStarred[x]!=0){
                        displayStarred[x]=(displayStarred[x]*10)*(3*10);
                     }else{
                         displayStarred[x]=3;
                     }
                 }  
             x+=1;
             }
           
        }else{
            filteredStarred[2]='f';
            numberSelected-=1;
            
            int x=0;
            for (int i=3;i<csvStarredStore.length();i=i+Character.getNumericValue(csvStarredStore.charAt(0))){
               if(csvStarredStore.charAt(i)=='t'){
                if(displayStarred[x]>10){
                   displayStarred[x]=(displayStarred[x]/30)/10;
                }else{
                    if(displayStarred[x]==3){
                        displayStarred[x]=0;
                   }
                }
                }
            x+=1;
            }
        } 
        
        for(int p=0; p<displayStarred.length; p++){
           System.out.print(displayStarred[p]+",");
        }
        System.out.println("");
        
        updateTable();
    }//GEN-LAST:event_flagtermselector3ActionPerformed

    private void flagtermselector5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_flagtermselector5ActionPerformed
        if (flagtermselector5.isSelected()){
            filteredStarred[4]='t';
            numberSelected+=1;
            
            int x=0;
            for (int i=5;i<csvStarredStore.length();i=i+Character.getNumericValue(csvStarredStore.charAt(0))){
                 //System.out.println(csvStarredStore.charAt(i));
                 if(csvStarredStore.charAt(i)=='t'){
                     if(displayStarred[x]!=0){
                        displayStarred[x]=(displayStarred[x]*10)*(5*10);
                     }else{
                         displayStarred[x]=5;
                     }
                 }  
            x+=1;
            }
           
        }else{
            filteredStarred[4]='f';
            numberSelected-=1;
            
            int x=0;
            for (int i=5;i<csvStarredStore.length();i=i+Character.getNumericValue(csvStarredStore.charAt(0))){
                if(csvStarredStore.charAt(i)=='t'){
                if(displayStarred[x]>10){
                   displayStarred[x]=(displayStarred[x]/50)/10;
                }else{
                    if(displayStarred[x]==5){
                        displayStarred[x]=0;
                   }
                }
                }
            x+=1;
            }
        } 
        
        for(int p=0; p<displayStarred.length; p++){
           System.out.print(displayStarred[p]+",");
        }
        System.out.println("");
        
        updateTable();
    }//GEN-LAST:event_flagtermselector5ActionPerformed

    private void flagtermselector4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_flagtermselector4ActionPerformed
        if (flagtermselector4.isSelected()){
            filteredStarred[3]='t';
            numberSelected+=1;
            
            int x=0;
            for (int i=4;i<csvStarredStore.length();i=i+Character.getNumericValue(csvStarredStore.charAt(0))){
                 //System.out.println(csvStarredStore.charAt(i));
                 if(csvStarredStore.charAt(i)=='t'){
                     if(displayStarred[x]!=0){
                        displayStarred[x]=(displayStarred[x]*10)*(4*10);
                     }else{
                         displayStarred[x]=4;
                     }
                 }  
            x+=1;
            }
           
        }else{
            filteredStarred[3]='f';
            numberSelected-=1;
            
            int x=0;
            for (int i=4;i<csvStarredStore.length();i=i+Character.getNumericValue(csvStarredStore.charAt(0))){
                if(csvStarredStore.charAt(i)=='t'){
                if(displayStarred[x]>10){
                   displayStarred[x]=(displayStarred[x]/40)/10;
                }else{
                    if(displayStarred[x]==4){
                        displayStarred[x]=0;
                   }
                }
                }
            x+=1;
            }
        } 

        for(int p=0; p<displayStarred.length; p++){
           //System.out.print(displayStarred[p]+",");
        }
        //System.out.println("");
        
        updateTable();
    }//GEN-LAST:event_flagtermselector4ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
       StarredSettings ss = new StarredSettings(csvStarredStore,starredName,wordNumber);
       ss.setVisible(true);
       ss.prevFrame(this);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void editActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editActionPerformed
        createSet cs = new createSet(setNo, setCreator,terms2,access,setName,topic,password,tableTitles,numColumns,wordNumber,setNotes, dateCreated, this);
        this.setVisible(false);
        cs.setVisible(true);
    }//GEN-LAST:event_editActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
       
    }//GEN-LAST:event_formWindowClosing

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        //System.out.println(search);
        if(currentUser.equals("admin")){
            this.setVisible(false);
        }else if(search==false){
            prevFrame.setVisible(true);
            prevFrame.readCSV();
            prevFrame.getSets();
        }else{
            prevSearchFrame.setVisible(true);   
        }
        this.dispose(); 
    }//GEN-LAST:event_formWindowClosed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    private void Cancel4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Cancel4ActionPerformed
        //returns to previous frame
        this.dispose();
        prevFrame.setVisible(true);
    }//GEN-LAST:event_Cancel4ActionPerformed

    
    
    
    
    /**
     * Custom cell renderer that highlights flagged/starred terms with different colors.
     */
    class Renderer extends DefaultTableCellRenderer {
        /** Array storing flag status for each row */
        private final long[] colourNoArray;
        
        /**
         * Creates a new renderer with the given flag status array.
         * 
         * @param colourNo Array containing flag values for each row
         */
        public Renderer(long[] colourNo) {
            this.colourNoArray = colourNo;
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, 
                boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel label = (JLabel)super.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column);
            
            // Set background color based on flag value
            Color bgColor = switch((int)colourNoArray[row]) {
                case 1 -> Color.GREEN;
                case 2 -> Color.MAGENTA;
                case 3 -> Color.ORANGE;
                case 4 -> Color.CYAN;
                case 5 -> Color.YELLOW;
                default -> colourNoArray[row] > 5 ? Color.RED : Color.WHITE;
            };
            label.setBackground(bgColor);
            
            return label;
        }
    }
          
    public void setValidated(boolean x){
        validated=x;
    }
   
    public void setAccess(String x){
        access=x;
    }
    
    public void setPassword(String x){
        password = x;
    }
    
    public boolean check(){
        if(currentUser.equals(setCreator)||currentUser.equals("admin")){
            return(true);
        }else{
            if(access.equals("protected")){
                String s = (String) JOptionPane.showInputDialog("This set has restricted access \n Enter the Password");
                if(s==null){
                    return(false);
                }
                if(s.equals(password)){
                    return(true);
                }else{
                    JOptionPane.showMessageDialog(this,"Password Incorrect", "Access Denied", JOptionPane.ERROR_MESSAGE);
                    return(false);
                }
            }
            if(access.equals("public")){
                if(validated==true){
                    return(true);
                }else{
                    JOptionPane.showMessageDialog(this,"This set has not been reviewed by an admin yet", "Set Not Validated", JOptionPane.ERROR_MESSAGE);
                }
            }       
            if(access.equals("private")) {
                    JOptionPane.showMessageDialog(this,"This set is private", "Access Denied", JOptionPane.ERROR_MESSAGE);
            }
        }
        return(false);
    }
    
    
    
    
    
    
    /**
     * Adds keyboard shortcuts to improve accessibility and usability
     */
    private void addKeyboardShortcuts() {
        // Alt+S for Start button
        jButton1.setMnemonic(KeyEvent.VK_S);
        
        // Alt+H for Shuffle button
        jButton2.setMnemonic(KeyEvent.VK_H);
        
        // Alt+C for Cancel button
        jButton3.setMnemonic(KeyEvent.VK_C);
        
        // Alt+E for Edit button
        edit.setMnemonic(KeyEvent.VK_E);
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Cancel4;
    private javax.swing.JRadioButton SelectorD1;
    private javax.swing.JRadioButton SelectorD2;
    private javax.swing.JRadioButton SelectorD3;
    private javax.swing.JRadioButton SelectorD4;
    private javax.swing.JRadioButton SelectorD5;
    private javax.swing.JRadioButton SelectorN1;
    private javax.swing.JRadioButton SelectorN2;
    private javax.swing.JRadioButton SelectorN3;
    private javax.swing.JRadioButton SelectorN4;
    private javax.swing.JRadioButton SelectorN5;
    private javax.swing.JRadioButton SelectorT1;
    private javax.swing.JRadioButton SelectorT2;
    private javax.swing.JRadioButton SelectorT3;
    private javax.swing.JRadioButton SelectorT4;
    private javax.swing.JRadioButton SelectorT5;
    private javax.swing.JTextArea SetDescription;
    private javax.swing.JLabel SetTitle;
    private javax.swing.JButton Start;
    private javax.swing.JTable TableOfCards;
    private javax.swing.JButton edit;
    private javax.swing.JRadioButton flagtermselector1;
    private javax.swing.JRadioButton flagtermselector2;
    private javax.swing.JRadioButton flagtermselector3;
    private javax.swing.JRadioButton flagtermselector4;
    private javax.swing.JRadioButton flagtermselector5;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lablel1;
    private javax.swing.JLabel lablel2;
    private javax.swing.JLabel lablel3;
    private javax.swing.JLabel lablel4;
    private javax.swing.JLabel lablel5;
    // End of variables declaration//GEN-END:variables
}
