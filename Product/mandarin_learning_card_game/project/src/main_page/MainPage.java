
package main_page;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import javax.swing.JOptionPane;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.UIManager;

import auxillary_functions.MailMenu;
import auxillary_functions.search;
import creator.createSet;
import flashcards.FlashcardSelector;
import login.LoginScreen;
import projects.DBConnection;

/**
 * MainPage provides the central hub for the Mandarin Learning Card Game.
 * This class manages:
 * 
 * 1. Flashcard Set Management:
 *    - Displaying user's flashcard sets
 *    - Creating new sets
 *    - Organizing sets into folders
 *    - Pagination for large numbers of sets
 * 
 * 2. Navigation:
 *    - Access to flashcard study interface
 *    - Access to mail system
 *    - Access to search functionality
 *    - Folder organization tools
 * 
 * 3. User Interface:
 *    - Welcome message with user's name
 *    - Progress tracking
 *    - Set and folder visibility controls
 *    - Intuitive navigation controls
 */
public class MainPage extends javax.swing.JFrame {
    /** Current user's username for database queries and personalization */
    private String username;

    /** Total number of flashcard sets owned by the current user */
    private int totalSets;
    
    /** Current page number being displayed (for pagination) */
    private int pageDisplayed = 1;
    
    /** Starting index for the current page's sets */
    private int currentIndex = 0;
    
    /** List of currently displayed flashcard set UI components */
    private ArrayList<SetSelector> displayedSets = new ArrayList<>();
    
    /** User's first name for personalized welcome messages */
    private String forename;
    
    /** Set identifiers for database queries and organization */
    private ArrayList<Integer> setNumbers = new ArrayList<>();
    
    /** CSV data for all flashcard sets in current view */
    private ArrayList<String[]> allCSVData = new ArrayList<>();
    
    /** List of folder UI components for set organization */
    private ArrayList<folderSelector> folders = new ArrayList<>();
    
    /** ID of the currently opened folder (-1 if no folder is open) */
    private int folderOpened;

    /**
     * Creates a new MainPage instance and initializes the flashcard management interface.
     * 
     * @param ls The LoginScreen that created this MainPage (for back navigation)
     * @param username The current user's username for database operations
     */
    public MainPage(LoginScreen ls, String username) {
        // Set system look and feel for modern appearance
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("Error setting Look and Feel: " + e);
        }
        
        // Initialize the Swing components and layout
        initComponents();
        this.setLocationRelativeTo(null);
        
        // Setup responsive UI features
        setupPlaceholders();
        setupEnterKeyListeners();
        addKeyboardShortcuts();
        
        this.setVisible(true);
        
        // Store username for database operations and personalization
        this.username = username;
        
        //Print Hello user
        Connection con = DBConnection.getConnection();
        
        String query = "SELECT `Forename` FROM `users` WHERE `Username`=?";
        
                 
        try{
            
            PreparedStatement ps = con.prepareStatement(query);
            
            ps.setString(1,username);
            
            ResultSet rs = ps.executeQuery();
            
            if(rs.next()){
                forename = rs.getString("Forename");
            }                                           
           
        }catch(Exception e){
            JOptionPane.showMessageDialog(null,e, "WARNING", JOptionPane.WARNING_MESSAGE);
            System.out.println(e);
        }
    
        
        welcomeText.setText("HELLO "+ forename.toUpperCase());
        
        //prevPage.setVisible(false);
        prevPageVisible(false);
        
        //get the number of set needed to be displayed
        
        getSets();
                       
        
    }
    
    /**
     * Retrieves all flashcard sets created by the current user from the database.
     * Resets and repopulates the displayedSets list with the user's sets.
     * This method is called when initializing the main page or refreshing the set list.
     */
    public void getSets() {
        // Get database connection for querying user's sets
        Connection con = DBConnection.getConnection();

        String query2 = "SELECT `Set number` FROM `setdata` WHERE`Set Creator`=?";
        
        //displayedSets = new ArrayList<>();
        
        while(!displayedSets.isEmpty()){
            displayedSets.remove(0);
        }
        
        setNumbers = new ArrayList<>();
        //while(!setNumbers.isEmpty()){
            //setNumbers.remove(0);
        //}
        
        //setNumbers.clear();
        
        //setNumbers.removeAll(setNumbers);
        
        System.out.println("SET NUMBER SIZE: " + setNumbers.size());
        totalSets=0;
        currentIndex=0;
        
        try{
            
            PreparedStatement ps = con.prepareStatement(query2);
            
            ps.setString(1,username);
            
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()){
                int tempSetNumber = rs.getInt("Set number");
                //System.out.println(tempSetNumber);
                setNumbers.add(tempSetNumber);
                totalSets+=1;
            }                                           
           
        }catch(Exception e){
            JOptionPane.showMessageDialog(null,e, "WARNING", JOptionPane.WARNING_MESSAGE);
            System.out.println(e);
        }
        
        //how many sets to display on first page        
        
        
        //System.out.println(totalSets);
        //for(int i =0; i<setNumbers.size();i++){
            
          //  System.out.println(setNumbers.get(i));
        //}
        
        
        
        System.out.println("total sets" + totalSets);
        //System.out.println("GettingSetDetails");
        
        readCSV();
        
        //System.out.println("GettingFolderDetails");
        
        getFolderDetails();
        
        closeFolderEditor();
        
        getSetData(currentIndex);
         
        //displayBoxes(currentIndex);
        
        
        
    }
  
    
    /*private void setButtons(int sIndex, int eIndex){
        for(int i = 0; i<(eIndex-sIndex);i++){
            //get each value from database and then create set 
            int setNumber = 1;
            String setCreator = "Jimmy";
            boolean validity = true;
            String access = "protected";
            String password="hi";
            String[] tableTitles={"name","f or v","cost","rating","rater"};
            String inputTerms="5apple,banana,carrot,pineapple,potato,/f,f,v,f,v,/2,3,4,5,6,/nah,yum,alirght,nah,yum,/taran,lt,colin,flembo,me,/";
            String nameOfSet="food n stuff";
            String notesOfSet="This is a small set explaining what food the CS likes";
            String topicOfSet="Food n nutr";
            String dateC="12/12/2004";
            String dateU="14/23/2012";
            
            //set to setSelector obj
            //parameters = creator, validitiy(approved by teacher), accessLevel, CurrentUser, password (="" if NA), tableTitles(array), inputdata, set name, notes, topic, date created, date updated, StarredTitles (array), starred input
                    
            SetSelector ss = new SetSelector(setNumber,setCreator,validity,access,password,tableTitles,inputTerms,nameOfSet,notesOfSet,topicOfSet,dateC,dateU);
            eachSetsData.add(ss);
        }
    }*/
    //tpxx
    /**
     * Retrieves metadata for a specific flashcard set from the database.
     * 
     * @param ci Current index in the displayedSets list where the set data should be loaded
     */
    private void getSetData(int ci) {
                
        int x;
                
        if(totalSets==0){
            x=0;
        }else{        
            if((totalSets-ci)>=6){
                x=6;
            }else{
                x=((totalSets-ci)%6);
            }
        }
        
           //System.out.println("pages" + x);
            
           //System.out.println(setNumbers.size());
           //System.out.println(setNumbers.get(0));
           
           
        if(x!=0){
            
            
            
            Connection con = DBConnection.getConnection();


            for(int i = 0; i<x; i++){
                //System.out.println(i);
                
                //create query in SQL
                String query = "SELECT `Set number`, `Set Creator`, `Verified`, `accessType`, `Password`, `TableTitles`, `SetData`, `Set Name`, `SetNotes`, `SetTopic`, `DateCreated`, `DateUpdated` FROM `setdata` WHERE `Set number`=?";

                try{
                    
                    //create a new statement to execute in the database through connection con
                    PreparedStatement ps = con.prepareStatement(query);

                        //System.out.println("i" + setNumbers.get(ci+i));
                    
                    //pass the set number in the first parameter in the preparedStatment 
                    ps.setInt(1,setNumbers.get(ci+i));
                    
                        //System.out.println(i + "DOEN");
                        
                    //execute query and save returned values in resultSet   
                    ResultSet rs = ps.executeQuery();

                    //iterate through results in result set
                    while(rs.next()){
                        //store each value per column of the result in a corrosponding variable
                        int setNumber = rs.getInt("Set number");
                            //System.out.println(setNumber);

                        String setCreator = rs.getString("Set Creator");
                        boolean Verified = rs.getBoolean("Verified");
                        String accessType = rs.getString("accessType");
                        String password = rs.getString("Password");
                        String tableTitlesString = rs.getString("TableTitles");
                        
                        //get the number of columns by counting the number of delimiter (,) in the string
                        int numColumns=0;
                        for(int j = 0; j<tableTitlesString.length();j++){
                            if(tableTitlesString.charAt(j)==','){
                                numColumns=numColumns+1;
                            }
                        }
                            //System.out.println(numColumns);   

                        //create new array for the tableTitles
                        String tableTitles[] = new String[numColumns];
                        //create an empty string to store each table Title
                        String tempword ="";
                        //initialise a count 
                        int wordsInserted=0;
                        //for every letter in tableTitlesString compare if it is a comma,
                        for(int j = 0; j<tableTitlesString.length();j++){                    
                            if(tableTitlesString.charAt(j)!=','){
                               //if it is not a comma, at this character to the tempword string (ie addng another character to the word seperate by commas
                               tempword=tempword+tableTitlesString.charAt(j);
                            }else{
                               //else add this tempword to the tabletitles array, as the comma indicates that this is one string that we require
                               tableTitles[wordsInserted]=tempword;
                               //increase the count of words, and then reset the tempword string to an empty string
                               wordsInserted+=1;
                               tempword ="";
                            }
                        }

                        //for(int k =0; k<tableTitles.length;k++){
                            //System.out.println(tableTitles[k]);
                        //}

                        String setData = rs.getString("setData");
                        String setName = rs.getString("Set Name");
                        String setNotes = rs.getString("setNotes");
                        String setTopics = rs.getString("SetTopic");
                        String dateCreated = rs.getString("DateCreated");
                        String dateUpdated = rs.getString("DateUpdated");


                        SetSelector ss = new SetSelector(setNumber,setCreator,Verified,accessType,password,tableTitles,setData,setName,setNotes,setTopics,dateCreated,dateUpdated);

                        //System.out.println(ss.getDateU());
                        
                        displayedSets.add(ss);
                        
                    }                                           

                }catch(Exception e){
                    JOptionPane.showMessageDialog(null,e, "WARNING", JOptionPane.WARNING_MESSAGE);
                    System.out.println(e);
                }
            }
            
            displayBoxes(ci);
            
            
        
        }else{
            
            FC6box.setOpaque(false);
            FC6box.setContentAreaFilled(false);
            FC6box.setBorderPainted(false);
            FC6box.setText("");
            FC5box.setOpaque(false);
            FC5box.setContentAreaFilled(false);
            FC5box.setBorderPainted(false);
            FC5box.setText("");
            FC4box.setOpaque(false);
            FC4box.setContentAreaFilled(false);
            FC4box.setBorderPainted(false);
            FC4box.setText("");
            FC3box.setOpaque(false);
            FC3box.setContentAreaFilled(false);
            FC3box.setBorderPainted(false);
            FC3box.setText("");
            FC2box.setOpaque(false);
            FC2box.setContentAreaFilled(false);
            FC2box.setBorderPainted(false);
            FC2box.setText("");
            FC1box.setOpaque(false);
            FC1box.setContentAreaFilled(false);
            FC1box.setBorderPainted(false);
            FC1box.setText("");
          
            
            JOptionPane.showMessageDialog(null,"There are no sets to display", "Notice", JOptionPane.WARNING_MESSAGE);
        }
                
        //System.out.println("Done");
        
    }
    
    /**
     * Retrieves folder information from the database and updates the UI.
     * This method loads all folders created by the current user and their contents.
     */
    private void getFolderDetails() {
        
        Connection con = DBConnection.getConnection();

        String query = "SELECT * FROM `folders` WHERE `User`='" + username + "'";
        
        //System.out.println(query);
                
        folders = new ArrayList<>();

        try{
            
            PreparedStatement ps = con.prepareStatement(query);
            
            ResultSet rs = ps.executeQuery();
            
            
            while(rs.next()){
                int folderNumber = rs.getInt("FolderIndex");
                String FolderName = rs.getString("FolderName");
                String setNos = rs.getString("SetNumbers");
                folderSelector fs = new folderSelector(setNos,folderNumber,FolderName);
                folders.add(fs);
                //System.out.println("added");
            }                                           
           
        }catch(Exception e){
            JOptionPane.showMessageDialog(null,e, "WARNING", JOptionPane.WARNING_MESSAGE);
            System.out.println(e);
        }
        
        //System.out.println("foldersNo: " + folders.size());
        
        displayFolders();
        
    }
    
    /**
     * Updates the UI to display folder icons and names.
     * Each folder is shown with its title and contains links to its flashcard sets.
     */
    private void displayFolders() {
        
        int noFolders = folders.size();
        Folder1.setVisible(true);
        Folder2.setVisible(true);
        Folder3.setVisible(true);
        Folder4.setVisible(true);
        Folder5.setVisible(true);
        Folder6.setVisible(true);
                        
        
        if(noFolders<6){
            Folder6.setVisible(false);
        }else{
            Folder6.setText(folders.get(5).getfolderName());
        }
        
        if(noFolders<5){
            Folder5.setVisible(false);
        }else{
            Folder5.setText(folders.get(4).getfolderName());
        }
        
        if(noFolders<4){
            Folder4.setVisible(false);
        }else{
            Folder4.setText(folders.get(3).getfolderName());
        }
        
        if(noFolders<3){
            Folder3.setVisible(false);
        }else{
            Folder3.setText(folders.get(2).getfolderName());
        }
        
        if(noFolders<2){
            Folder2.setVisible(false);
        }else{
            Folder2.setText(folders.get(1).getfolderName());
        }
        
        if(noFolders<1){
            Folder1.setVisible(false);
        }else{
            //System.out.println(folders.get(0).getfolderName());
            Folder1.setText(folders.get(0).getfolderName());
        }
    }
    
    
    /**
     * Updates the UI to display flashcard set boxes with titles and metadata.
     * Controls visibility of navigation buttons based on the current page.
     * 
     * @param ci Current index in the displayedSets list to start displaying from
     */
    private void displayBoxes(int ci) {
                         
        if(ci+6>totalSets){
            //nextPage.setVisible(false);
            nextPageVisible(false);
        }

        //FC1box.setVisible(true);
        FC1box.setOpaque(true);
        FC1box.setContentAreaFilled(true);
        FC1box.setBorderPainted(true);
        //FC2box.setVisible(true);
        FC2box.setOpaque(true);
        FC2box.setContentAreaFilled(true);
        FC2box.setBorderPainted(true);
        //FC3box.setVisible(true);
        FC3box.setOpaque(true);
        FC3box.setContentAreaFilled(true);
        FC3box.setBorderPainted(true);
        //FC4box.setVisible(true);
        FC4box.setOpaque(true);
        FC4box.setContentAreaFilled(true);
        FC4box.setBorderPainted(true);
        //FC5box.setVisible(true);
        FC5box.setOpaque(true);
        FC5box.setContentAreaFilled(true);
        FC5box.setBorderPainted(true);
        //FC6box.setVisible(true);
        FC6box.setOpaque(true);
        FC6box.setContentAreaFilled(true);
        FC6box.setBorderPainted(true);


        int x=totalSets-ci;
        
        if(x<6){
            //FC6box.setVisible(false);
            FC6box.setOpaque(false);
            FC6box.setContentAreaFilled(false);
            FC6box.setBorderPainted(false);
            FC6box.setText("");
        }
        if(x<5){
            //FC5box.setVisible(false); 
            FC5box.setOpaque(false);
            FC5box.setContentAreaFilled(false);
            FC5box.setBorderPainted(false);
            FC5box.setText("");
        }
        if(x<4){
            //FC4box.setVisible(false);  
            FC4box.setOpaque(false);
            FC4box.setContentAreaFilled(false);
            FC4box.setBorderPainted(false);
            FC4box.setText("");
        }
        if(x<3){
            //FC3box.setVisible(false);
            FC3box.setOpaque(false);
            FC3box.setContentAreaFilled(false);
            FC3box.setBorderPainted(false);
            FC3box.setText("");
        }
        if(x<2){
            //FC2box.setVisible(false); 
            FC2box.setOpaque(false);
            FC2box.setContentAreaFilled(false);
            FC2box.setBorderPainted(false);
            FC2box.setText("");
        }
        if(x<1){
           FC1box.setVisible(false); 
        }


        String text = "";

        text=(displayedSets.get(0)).getNameOfSet()+"\\n By:"+(displayedSets.get(0)).getSetCreator();

        FC1box.setText("<html>" + text.replaceAll("\\n","<br>") + "</html>");

        if(x>0){
            text=(displayedSets.get(0)).getNameOfSet()+" By:"+(displayedSets.get(0)).getSetCreator();
            FC1box.setText("<html>" + text.replaceAll("\\n","<br>") + "</html>");
        }
        if(x>1){
            text=(displayedSets.get(1)).getNameOfSet()+" By:"+(displayedSets.get(1)).getSetCreator();
            FC2box.setText("<html>" + text.replaceAll("\\n","<br>") + "</html>");
        }
        if(x>2){
            text=(displayedSets.get(2)).getNameOfSet()+" By:"+(displayedSets.get(2)).getSetCreator();
            FC3box.setText("<html>" + text.replaceAll("\\n","<br>") + "</html>");
        }
        if(x>3){
            text=(displayedSets.get(3)).getNameOfSet()+" By:"+(displayedSets.get(3)).getSetCreator();
            FC4box.setText("<html>" + text.replaceAll("\\n","<br>") + "</html>");
        }
        if(x>4){
            text=(displayedSets.get(4)).getNameOfSet()+" By:"+(displayedSets.get(4)).getSetCreator();
            FC5box.setText("<html>" + text.replaceAll("\\n","<br>") + "</html>");
        }
        if(x>5){
            text=(displayedSets.get(5)).getNameOfSet()+" By:"+(displayedSets.get(5)).getSetCreator();
            FC6box.setText("<html>" + text.replaceAll("\\n","<br>") + "</html>");
        }

        updateProgressBar();
        
    }
    
    /**
     * Updates the progress bar indicating the user's completion rate of flashcards.
     * Reads CSV files to calculate the percentage of cards marked as "learned".
     */
    public void updateProgressBar() {
        float precentage=(float) (((pageDisplayed)/Math.ceil((float)totalSets/6))*100);
        ProgressBar.setValue((int) precentage);
        //System.out.println(precentage);
    }
    
    /**
     * Reads and parses CSV files containing flashcard data.
     * Each CSV file contains a set of Mandarin words, their translations, and study progress.
     * The data is stored in the allCSVData list for use in progress tracking.
     */
    public void readCSV() {
        //System.out.println("hi");
        String fileName = "flaggedTerms.txt";
        int ticker = 0;
        String[] csvData = new String[3];
        ArrayList<String[]> allCSVData = new ArrayList<>();
        try (Scanner s = new Scanner(new FileReader(fileName))) {
            while(s.hasNext()) {
                if(ticker == 0) {
                    csvData = new String[3];
                }
                String line = s.nextLine();
                csvData[ticker] = line;
                ticker += 1;
                
                if(ticker == 3) {
                    ticker = 0;
                    allCSVData.add(csvData.clone()); // Clone to avoid reference issues
                }
            }
        } catch(FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, e, "WARNING", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    /**
     * Shows the folder editor interface where users can create, rename, or delete folders.
     * Also allows users to organize flashcard sets into folders.
     */
    public void openFolderEditor() {
        
        folderEditorLabel.setVisible(true);
        backspace.setVisible(true);
        enterSetNumberFeild.setVisible(true);
        editFolderButton.setVisible(true);
        addfolder.setVisible(true);
        removeFolder.setVisible(true);
        addfolder.setSelected(true);
        removeFolder.setSelected(false);
               
    }
    
    /**
     * Closes the folder editor and updates the main interface to reflect any changes.
     * Refreshes the folder display and set organization.
     */
    public void closeFolderEditor() {
        folderEditorLabel.setVisible(false);
        backspace.setVisible(false);
        enterSetNumberFeild.setVisible(false);
        editFolderButton.setVisible(false);
        addfolder.setVisible(false);
        removeFolder.setVisible(false); 
    }
     
    /**
     * Checks if a flashcard set exists and is accessible.
     * 
     * @param setNo The ID number of the set to validate
     * @return true if the set exists and is accessible, false otherwise
     */
    public boolean validateSetExsistance(int setNo) {
        boolean validate=false;
        
        Connection con = DBConnection.getConnection();
        
        String query = "SELECT `Set number` FROM `setdata` WHERE `Set number`=" + setNo ;
        
        try{
            
            PreparedStatement ps = con.prepareStatement(query);
                        
            ResultSet rs = ps.executeQuery();
            
            if(rs.next()){
                validate=true;
            }                                           
           
        }catch(Exception e){
            JOptionPane.showMessageDialog(null,e, "WARNING", JOptionPane.WARNING_MESSAGE);
            System.out.println(e);
        }
        
        return(validate);
    }
    
    /**
     * Removes a flashcard set from a folder.
     * This only affects the organization, not the set data itself.
     * 
     * @param setNo The ID of the set to remove
     * @param folderNo The ID of the folder to remove the set from
     */
    public void removeSet(int setNo, int folderNo) {
        
        String remove = setNo+",";
        int setIndexInArray=-1;
        
        for(int i = 0; i<folders.size();i++){
            if(folders.get(i).getfoldernumber()==folderNo){
                setIndexInArray=i;
            }
        }
        
        String updatedSetNos = (folders.get(setIndexInArray).getsetNumbers()).replace(remove, "");
        folders.get(setIndexInArray).setsetNumbers(updatedSetNos);
        
        Connection con = DBConnection.getConnection();

        String query = "UPDATE `folders` SET `SetNumbers`='" + updatedSetNos + "' WHERE `FolderIndex`='"+folderNo+"'";

        try{

            Statement statement = con.createStatement();
            
            statement.executeUpdate(query);
            
            JOptionPane.showMessageDialog(null,"Set removed from folder", "Success", JOptionPane.WARNING_MESSAGE);
            System.out.println("Success");

        }catch(Exception e){
            
            JOptionPane.showMessageDialog(null,e, "WARNING", JOptionPane.WARNING_MESSAGE);
            System.out.println(e);
        }
       
    }
    
    public static boolean isNumeric(String str) { 
        try {  
          Double.parseDouble(str);  
          return(true);
        } catch(NumberFormatException e){  
          return(false);  
        }  
    }
    
    /**
     * Controls the visibility of the "Previous Page" navigation button.
     * 
     * @param x true to show the button, false to hide it
     */
    public void prevPageVisible(boolean x) {
        prevPage.setOpaque(x);
        prevPage.setContentAreaFilled(x);
        prevPage.setBorderPainted(x);
        if(x==true){
            prevPage.setText("<");
        }else{
            prevPage.setText("");
        }
    }
    
    /**
     * Controls the visibility of the "Next Page" navigation button.
     * 
     * @param x true to show the button, false to hide it
     */
    public void nextPageVisible(boolean x) {
        nextPage.setOpaque(x);
        nextPage.setContentAreaFilled(x);
        nextPage.setBorderPainted(x);
        if(x==true){
            nextPage.setText(">");
        }else{
            nextPage.setText("");
        }
    }
    

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {


        jPanel1 = new javax.swing.JPanel();
        FC1box = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        welcomeText = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        FolderMP = new javax.swing.JButton();
        Folder1 = new javax.swing.JButton();
        Folder2 = new javax.swing.JButton();
        Folder3 = new javax.swing.JButton();
        Folder4 = new javax.swing.JButton();
        Folder5 = new javax.swing.JButton();
        Folder6 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        Cancel5 = new javax.swing.JButton();
        FC2box = new javax.swing.JButton();
        FC3box = new javax.swing.JButton();
        FC4box = new javax.swing.JButton();
        FC5box = new javax.swing.JButton();
        FC6box = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        ProgressBar = new javax.swing.JProgressBar();
        jPanel4 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        searchbutton = new javax.swing.JButton();
        searchfield = new javax.swing.JTextField();
        MailButton = new javax.swing.JButton();
        progressDashboardButton = new javax.swing.JButton();
        addfolder = new javax.swing.JRadioButton();
        removeFolder = new javax.swing.JRadioButton();
        folderEditorLabel = new javax.swing.JLabel();
        enterSetNumberFeild = new javax.swing.JTextField();
        editFolderButton = new javax.swing.JButton();
        backspace = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        prevPage = new javax.swing.JButton();
        CreateSet = new javax.swing.JButton();
        nextPage = new javax.swing.JButton();

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        FC1box.setText("jButton1");
        FC1box.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FC1boxActionPerformed(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(0, 102, 255));
        jPanel2.setForeground(new java.awt.Color(240, 240, 240));

        welcomeText.setFont(new java.awt.Font("Nirmala UI", 1, 36)); // NOI18N
        welcomeText.setForeground(new java.awt.Color(255, 255, 255));
        welcomeText.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        welcomeText.setText("HELLO");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(welcomeText, javax.swing.GroupLayout.PREFERRED_SIZE, 729, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(80, 80, 80))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(welcomeText, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel2.setFont(new java.awt.Font("Nirmala UI", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(51, 51, 51));
        jLabel2.setText("Folders:");

        FolderMP.setText("Main Page");
        FolderMP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FolderMPActionPerformed(evt);
            }
        });

        Folder1.setText("Folder1");
        Folder1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Folder1ActionPerformed(evt);
            }
        });

        Folder2.setText("Folder2");
        Folder2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Folder2ActionPerformed(evt);
            }
        });

        Folder3.setText("Folder3");
        Folder3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Folder3ActionPerformed(evt);
            }
        });

        Folder4.setText("Folder4");
        Folder4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Folder4ActionPerformed(evt);
            }
        });

        Folder5.setText("Folder5");
        Folder5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Folder5ActionPerformed(evt);
            }
        });

        Folder6.setText("Folder6");
        Folder6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Folder6ActionPerformed(evt);
            }
        });

        jButton1.setText("Add Folder");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        Cancel5.setText("Log Out");
        Cancel5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Cancel5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(Folder2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(Folder1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(Folder4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(Folder3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(Folder6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(Folder5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(FolderMP, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addComponent(Cancel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(FolderMP, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(Folder1)
                .addGap(10, 10, 10)
                .addComponent(Folder2)
                .addGap(10, 10, 10)
                .addComponent(Folder3)
                .addGap(10, 10, 10)
                .addComponent(Folder4)
                .addGap(10, 10, 10)
                .addComponent(Folder5)
                .addGap(10, 10, 10)
                .addComponent(Folder6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Cancel5, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3))
        );

        jPanel3Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {Folder1, Folder2, Folder3, Folder4, Folder5, Folder6, FolderMP});

        FC2box.setText("jButton1");
        FC2box.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FC2boxActionPerformed(evt);
            }
        });

        FC3box.setText("jButton1");
        FC3box.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FC3boxActionPerformed(evt);
            }
        });

        FC4box.setText("jButton1");
        FC4box.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FC4boxActionPerformed(evt);
            }
        });

        FC5box.setText("jButton1");
        FC5box.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FC5boxActionPerformed(evt);
            }
        });

        FC6box.setText("jButton1");
        FC6box.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FC6boxActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Nirmala UI", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(51, 51, 51));
        jLabel1.setText("Sets:");

        jPanel4.setBackground(new java.awt.Color(0, 0, 255));

        jButton2.setBackground(new java.awt.Color(255, 255, 255));
        jButton2.setFont(new java.awt.Font("Nirmala UI", 1, 14)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        try {
            java.net.URL imageUrl = getClass().getResource("/projects/help button.png");
            if (imageUrl != null) {
                jButton2.setIcon(new javax.swing.ImageIcon(imageUrl));
            } else {
                // Fallback if image not found
                jButton2.setText("?");
                jButton2.setFont(new java.awt.Font("Nirmala UI", 1, 14));
                System.err.println("Could not find help button image at: /projects/help button.png");
            }
        } catch (Exception e) {
            // Fallback if error loading image
            jButton2.setText("?");
            jButton2.setFont(new java.awt.Font("Nirmala UI", 1, 14));
            System.err.println("Error loading help button image: " + e.getMessage());
        }
        jButton2.setBorder(null);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel5.setBackground(new java.awt.Color(0, 204, 255));

        searchbutton.setText("Search");
        searchbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchbuttonActionPerformed(evt);
            }
        });

        searchfield.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchfieldActionPerformed(evt);
            }
        });

        MailButton.setBackground(new java.awt.Color(0, 102, 255));
        MailButton.setForeground(new java.awt.Color(255, 255, 255));
        MailButton.setText("Mail");
        MailButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MailButtonActionPerformed(evt);
            }
        });
        
        progressDashboardButton.setBackground(new java.awt.Color(46, 204, 113));
        progressDashboardButton.setForeground(new java.awt.Color(255, 255, 255));
        progressDashboardButton.setText("Progress Dashboard");
        progressDashboardButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                progressDashboardButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(MailButton, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(progressDashboardButton, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(searchbutton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(searchfield, javax.swing.GroupLayout.PREFERRED_SIZE, 619, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(211, 211, 211))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(searchbutton, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(searchfield, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(MailButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(progressDashboardButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        addfolder.setText("Add");
        addfolder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addfolderActionPerformed(evt);
            }
        });

        removeFolder.setText("Remove");
        removeFolder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeFolderActionPerformed(evt);
            }
        });

        folderEditorLabel.setText("Enter Set Number:");

        enterSetNumberFeild.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                enterSetNumberFeildActionPerformed(evt);
            }
        });
        enterSetNumberFeild.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                enterSetNumberFeildKeyPressed(evt);
            }
        });

        editFolderButton.setText("Confirm");
        editFolderButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editFolderButtonActionPerformed(evt);
            }
        });

        backspace.setFont(new java.awt.Font("Tahoma", 0, 6)); // NOI18N
        backspace.setText("[X]");
        backspace.setPreferredSize(new java.awt.Dimension(43, 20));
        backspace.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backspaceActionPerformed(evt);
            }
        });

        prevPage.setFont(new java.awt.Font("Nirmala UI", 1, 24)); // NOI18N
        prevPage.setText("<");
        prevPage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prevPageActionPerformed(evt);
            }
        });

        CreateSet.setFont(new java.awt.Font("Nirmala UI", 1, 18)); // NOI18N
        CreateSet.setText("Create Set");
        CreateSet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CreateSetActionPerformed(evt);
            }
        });

        nextPage.setFont(new java.awt.Font("Nirmala UI", 1, 24)); // NOI18N
        nextPage.setText(">");
        nextPage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextPageActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(prevPage, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(CreateSet, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nextPage, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(nextPage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(CreateSet, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(prevPage))
                .addGap(0, 0, 0))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 22, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(FC3box, javax.swing.GroupLayout.PREFERRED_SIZE, 362, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(38, 38, 38)
                                .addComponent(FC4box, javax.swing.GroupLayout.PREFERRED_SIZE, 362, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(FC5box, javax.swing.GroupLayout.PREFERRED_SIZE, 362, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(38, 38, 38)
                                .addComponent(FC6box, javax.swing.GroupLayout.PREFERRED_SIZE, 362, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(FC1box, javax.swing.GroupLayout.PREFERRED_SIZE, 362, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(folderEditorLabel)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(enterSetNumberFeild, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(backspace, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(editFolderButton))
                                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(addfolder)
                                .addGap(18, 18, 18)
                                .addComponent(removeFolder)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(38, 38, 38)
                                .addComponent(FC2box, javax.swing.GroupLayout.PREFERRED_SIZE, 362, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(11, 11, 11)
                                        .addComponent(ProgressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 362, javax.swing.GroupLayout.PREFERRED_SIZE))))))))
            .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 935, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(folderEditorLabel)
                                    .addComponent(enterSetNumberFeild, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(editFolderButton)
                                    .addComponent(backspace, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(4, 4, 4)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(addfolder)
                                    .addComponent(removeFolder))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel1))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(ProgressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(14, 14, 14)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(FC2box, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(FC1box, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(FC3box, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(FC4box, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(FC5box, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(FC6box, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    
 
    /**
     * Event handler for clicking the first flashcard set button.
     * Opens the selected set in the flashcard study interface.
     * 
     * @param evt The action event from clicking the button
     */
    private void FC1boxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FC1boxActionPerformed
        
        //Variables need to open flashcard selector (from csv files)
        String[] starredNames = new String[5];
        String starred = "0";
        String starredNamesRaw = "";
        
        //Will store which index of array the needed csv data is stored
        int indexInAllCSVData=-1;
        boolean flag = false;
        //iterate through csv data array
        for(int i=0; i<allCSVData.size();i++){
            //System.out.println(displayedSets.get(0).getSetNumber());
            //System.out.println(allCSVData.get(i)[0]);
            //if setnumber in csv corresponds to database setnumber get data 
            
            //System.out.println(Integer.parseInt(allCSVData.get(i)[0])==(displayedSets.get(0).getSetNumber()));
            
            if(Integer.parseInt(allCSVData.get(i)[0])==(displayedSets.get(0).getSetNumber())){
                flag=true;
                //System.out.println("TRUE");
                indexInAllCSVData=i;
                //save this data from csv array
                starred=allCSVData.get(indexInAllCSVData)[1];
                starredNamesRaw = allCSVData.get(indexInAllCSVData)[2];

                //split string to create an array on titles
                String word = "";
                ArrayList<String> arraylistWord = new ArrayList<>();
                //System.out.println(starredNamesRaw);
                //System.out.println(starredNamesRaw.length());
                for(int j = 0; j<starredNamesRaw.length();j++){
                    if(starredNamesRaw.charAt(j)==(',')){
                        arraylistWord.add(word);
                        //System.out.println("added: " + word);
                        word="";
                    }else{
                        word=word+starredNamesRaw.charAt(j);
                        //System.out.println(word);
                    }
                }
                
                //transfere files from arraylist to array
                starredNames=new String[arraylistWord.size()];
                for(int j = 0; j<arraylistWord.size(); j++){
                    //System.out.println(arraylistWord.get(j));
                    //System.out.println(arraylistWord.get(j));
                    starredNames[j] = arraylistWord.get(j);
                }
            }
        }
        
        //for(int l = 0; l<5;l++){
           // System.out.println(starredNames[l]);
        //}
        //System.out.println(starredNames.length);
                
        if(flag==false){
            try{
                
                FileWriter fr = new FileWriter("flaggedTerms.txt",true);
                BufferedWriter br = new BufferedWriter(fr);
                br.write(""+displayedSets.get(0).getSetNumber()+"\n0\n,,,,,");
                br.close();
                fr.close();
                System.out.println("sucess");
            }catch(IOException e){
                System.out.println(e);
                JOptionPane.showMessageDialog(null,e, "WARNING", JOptionPane.WARNING_MESSAGE);
            }
        }
            
        
        //System.out.println(starred);
        
        for(int i = 0; i<5;i++){
          //System.out.println(displayedSets.get(0).getTableTitles()[i]);
          //System.out.println(starredNames[i]);
        }
        // Create and display flashcard selector window
        new FlashcardSelector(this, username, displayedSets.get(0), starredNames, starred, displayedSets.get(0).getSetNumber()).setVisible(true);
        
        //dispose 
        
    }//GEN-LAST:event_FC1boxActionPerformed

    
    private void FC2boxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FC2boxActionPerformed
           
        //Variables need to open flashcard selector (from csv files)
        String[] starredNames = new String[5];
        String starred = "0";
        String starredNamesRaw = "";
        
        //Will store which index of array the needed csv data is stored
        int indexInAllCSVData=-1;
        boolean flag = false;
        //iterate through csv data array
        for(int i=0; i<allCSVData.size();i++){
            //System.out.println(displayedSets.get(1).getSetNumber());
            //System.out.println(allCSVData.get(i)[1]);
            //if setnumber in csv corresponds to database setnumber get data 
            
            //System.out.println(Integer.parseInt(allCSVData.get(i)[1])==(displayedSets.get(1).getSetNumber()));
            
            if(Integer.parseInt(allCSVData.get(i)[0])==(displayedSets.get(1).getSetNumber())){
                flag=true;
                //System.out.println("TRUE");
                indexInAllCSVData=i;
                //save this data from csv array
                starred=allCSVData.get(indexInAllCSVData)[1];
                starredNamesRaw = allCSVData.get(indexInAllCSVData)[2];

                //split string to create an array on titles
                String word = "";
                ArrayList<String> arraylistWord = new ArrayList<>();
                //System.out.println(starredNamesRaw);
                //System.out.println(starredNamesRaw.length());
                for(int j = 0; j<starredNamesRaw.length();j++){
                    if(starredNamesRaw.charAt(j)==(',')){
                        arraylistWord.add(word);
                        //System.out.println("added: " + word);
                        word="";
                    }else{
                        word=word+starredNamesRaw.charAt(j);
                        //System.out.println(word);
                    }
                }
                
                //transfere files from arraylist to array
                starredNames=new String[arraylistWord.size()];
                for(int j = 0; j<arraylistWord.size(); j++){
                    //System.out.println(arraylistWord.get(j));
                    //System.out.println(arraylistWord.get(j));
                    starredNames[j] = arraylistWord.get(j);
                }
            }
        }
        
        //for(int l = 0; l<5;l++){
           // System.out.println(starredNames[l]);
        //}
        //System.out.println(starredNames.length);
                
        if(flag==false){
            try{
                
                FileWriter fr = new FileWriter("flaggedTerms.txt",true);
                BufferedWriter br = new BufferedWriter(fr);
                br.write(""+displayedSets.get(1).getSetNumber()+"\n0\n,,,,,");
                br.close();
                fr.close();
                System.out.println("sucess");
            }catch(IOException e){
                System.out.println(e);
                JOptionPane.showMessageDialog(null,e, "WARNING", JOptionPane.WARNING_MESSAGE);
            }
        }
            
                
        for(int i = 0; i<5;i++){
          //System.out.println(displayedSets.get(1).getTableTitles()[i]);
          //System.out.println(starredNames[i]);
        }
        new FlashcardSelector(this, username, displayedSets.get(1), starredNames, starred, displayedSets.get(1).getSetNumber()).setVisible(true);
    
    }//GEN-LAST:event_FC2boxActionPerformed

    private void FC3boxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FC3boxActionPerformed
       
        //Variables need to open flashcard selector (from csv files)
        String[] starredNames = new String[5];
        String starred = "0";
        String starredNamesRaw = "";
        
        //Will store which index of array the needed csv data is stored
        int indexInAllCSVData=-1;
        boolean flag = false;
        //iterate through csv data array
        for(int i=0; i<allCSVData.size();i++){
            //System.out.println(displayedSets.get(2).getSetNumber());
            //System.out.println(allCSVData.get(i)[2]);
            //if setnumber in csv corresponds to database setnumber get data 
            
            //System.out.println(Integer.parseInt(allCSVData.get(i)[2])==(displayedSets.get(2).getSetNumber()));
            
            if(Integer.parseInt(allCSVData.get(i)[0])==(displayedSets.get(2).getSetNumber())){
                flag=true;
                //System.out.println("TRUE");
                indexInAllCSVData=i;
                //save this data from csv array
                starred=allCSVData.get(indexInAllCSVData)[1];
                starredNamesRaw = allCSVData.get(indexInAllCSVData)[2];
                //System.out.println("SNR" + starredNamesRaw);
                //System.out.println("S"+starred);

                //split string to create an array on titles
                String word = "";
                ArrayList<String> arraylistWord = new ArrayList<>();
                //System.out.println(starredNamesRaw);
                //System.out.println(starredNamesRaw.length());
                for(int j = 0; j<starredNamesRaw.length();j++){
                    if(starredNamesRaw.charAt(j)==(',')){
                        arraylistWord.add(word);
                        //System.out.println("added: " + word);
                        word="";
                    }else{
                        word=word+starredNamesRaw.charAt(j);
                        //System.out.println(word);
                    }
                }
                
                //transfere files from arraylist to array
                starredNames=new String[arraylistWord.size()];
                for(int j = 0; j<arraylistWord.size(); j++){
                    //System.out.println(arraylistWord.get(j));
                    //System.out.println(arraylistWord.get(j));
                    starredNames[j] = arraylistWord.get(j);
                }
            }
        }
        
        //for(int l = 0; l<5;l++){
           // System.out.println(starredNames[l]);
        //}
        //System.out.println(starredNames.length);
                
        if(flag==false){
            try{
                
                FileWriter fr = new FileWriter("flaggedTerms.txt",true);
                BufferedWriter br = new BufferedWriter(fr);
                br.write(""+displayedSets.get(2).getSetNumber()+"\n0\n,,,,,");
                br.close();
                fr.close();
                System.out.println("sucess");
            }catch(IOException e){
                System.out.println(e);
                JOptionPane.showMessageDialog(null,e, "WARNING", JOptionPane.WARNING_MESSAGE);
            }
        }
            
        
        //System.out.println(starred);
        
        for(int i = 0; i<5;i++){
          //System.out.println(displayedSets.get(2).getTableTitles()[i]);
          //System.out.println(starredNames[i]);
        }
        new FlashcardSelector(this, username, displayedSets.get(2), starredNames, starred, displayedSets.get(2).getSetNumber()).setVisible(true);
        }//GEN-LAST:event_FC3boxActionPerformed

    private void FC4boxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FC4boxActionPerformed
              
        //Variables need to open flashcard selector (from csv files)
        String[] starredNames = new String[5];
        String starred = "0";
        String starredNamesRaw = "";
        
        //Will store which index of array the needed csv data is stored
        int indexInAllCSVData=-1;
        boolean flag = false;
        //iterate through csv data array
        for(int i=0; i<allCSVData.size();i++){
            //System.out.println(displayedSets.get(3).getSetNumber());
            //System.out.println(allCSVData.get(i)[3]);
            //if setnumber in csv corresponds to database setnumber get data 
            
            //System.out.println(Integer.parseInt(allCSVData.get(i)[3])==(displayedSets.get(3).getSetNumber()));
            
            if(Integer.parseInt(allCSVData.get(i)[0])==(displayedSets.get(3).getSetNumber())){
                flag=true;
                //System.out.println("TRUE");
                indexInAllCSVData=i;
                //save this data from csv array
                starred=allCSVData.get(indexInAllCSVData)[1];
                starredNamesRaw = allCSVData.get(indexInAllCSVData)[2];
                System.out.println(starredNamesRaw);
                //split string to create an array on titles
                String word = "";
                ArrayList<String> arraylistWord = new ArrayList<>();
                //System.out.println(starredNamesRaw);
                //System.out.println(starredNamesRaw.length());
                for(int j = 0; j<starredNamesRaw.length();j++){
                    if(starredNamesRaw.charAt(j)==(',')){
                        arraylistWord.add(word);
                        //System.out.println("added: " + word);
                        word="";
                    }else{
                        word=word+starredNamesRaw.charAt(j);
                        //System.out.println(word);
                    }
                }
                
                //transfere files from arraylist to array
                starredNames=new String[arraylistWord.size()];
                for(int j = 0; j<arraylistWord.size(); j++){
                    //System.out.println(arraylistWord.get(j));
                    //System.out.println(arraylistWord.get(j));
                    starredNames[j] = arraylistWord.get(j);
                }
            }
        }
        
        //for(int l = 0; l<5;l++){
           // System.out.println(starredNames[l]);
        //}
        //System.out.println(starredNames.length);
                
        if(flag==false){
            try{
                
                FileWriter fr = new FileWriter("flaggedTerms.txt",true);
                BufferedWriter br = new BufferedWriter(fr);
                br.write(""+displayedSets.get(3).getSetNumber()+"\n0\n,,,,,");
                br.close();
                fr.close();
                System.out.println("sucess");
            }catch(IOException e){
                System.out.println(e);
                JOptionPane.showMessageDialog(null,e, "WARNING", JOptionPane.WARNING_MESSAGE);
            }
        }
            
                
        for(int i = 0; i<5;i++){
          //System.out.println(displayedSets.get(3).getTableTitles()[i]);
          //System.out.println(starredNames[i]);
        }
        new FlashcardSelector(this, username, displayedSets.get(3), starredNames, starred, displayedSets.get(3).getSetNumber()).setVisible(true);
    
    }//GEN-LAST:event_FC4boxActionPerformed

    private void FC5boxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FC5boxActionPerformed
                  
        //Variables need to open flashcard selector (from csv files)
        String[] starredNames = new String[5];
        String starred = "0";
        String starredNamesRaw = "";
        
        //Will store which index of array the needed csv data is stored
        int indexInAllCSVData=-1;
        boolean flag = false;
        //iterate through csv data array
        for(int i=0; i<allCSVData.size();i++){
            //System.out.println(displayedSets.get(4).getSetNumber());
            //System.out.println(allCSVData.get(i)[]);
            //if setnumber in csv corresponds to database setnumber get data 
            
            //System.out.println(Integer.parseInt(allCSVData.get(i)[4])==(displayedSets.get(4).getSetNumber()));
            
            if(Integer.parseInt(allCSVData.get(i)[0])==(displayedSets.get(4).getSetNumber())){
                flag=true;
                //System.out.println("TRUE");
                indexInAllCSVData=i;
                //save this data from csv array
                starred=allCSVData.get(indexInAllCSVData)[1];
                starredNamesRaw = allCSVData.get(indexInAllCSVData)[2];
                System.out.println(starredNamesRaw);
                //split string to create an array on titles
                String word = "";
                ArrayList<String> arraylistWord = new ArrayList<>();
                //System.out.println(starredNamesRaw);
                //System.out.println(starredNamesRaw.length());
                for(int j = 0; j<starredNamesRaw.length();j++){
                    if(starredNamesRaw.charAt(j)==(',')){
                        arraylistWord.add(word);
                        //System.out.println("added: " + word);
                        word="";
                    }else{
                        word=word+starredNamesRaw.charAt(j);
                        //System.out.println(word);
                    }
                }
                
                //transfere files from arraylist to array
                starredNames=new String[arraylistWord.size()];
                for(int j = 0; j<arraylistWord.size(); j++){
                    //System.out.println(arraylistWord.get(j));
                    //System.out.println(arraylistWord.get(j));
                    starredNames[j] = arraylistWord.get(j);
                }
            }
        }
        
        //for(int l = 0; l<5;l++){
           // System.out.println(starredNames[l]);
        //}
        //System.out.println(starredNames.length);
                
        if(flag==false){
            try{
                
                FileWriter fr = new FileWriter("flaggedTerms.txt",true);
                BufferedWriter br = new BufferedWriter(fr);
                br.write(""+displayedSets.get(4).getSetNumber()+"\n0\n,,,,,");
                br.close();
                fr.close();
                System.out.println("sucess");
            }catch(IOException e){
                System.out.println(e);
                JOptionPane.showMessageDialog(null,e, "WARNING", JOptionPane.WARNING_MESSAGE);
            }
        }
            
                
        for(int i = 0; i<5;i++){
          //System.out.println(displayedSets.get(4).getTableTitles()[i]);
          //System.out.println(starredNames[i]);
        }
        new FlashcardSelector(this, username, displayedSets.get(4), starredNames, starred, displayedSets.get(4).getSetNumber()).setVisible(true);
    
    }//GEN-LAST:event_FC5boxActionPerformed

    private void FC6boxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FC6boxActionPerformed
                 
        //Variables need to open flashcard selector (from csv files)
        String[] starredNames = new String[5];
        String starred = "0";
        String starredNamesRaw = "";
        
        //Will store which index of array the needed csv data is stored
        int indexInAllCSVData=-1;
        boolean flag = false;
        //iterate through csv data array
        for(int i=0; i<allCSVData.size();i++){
            //System.out.println(displayedSets.get(5).getSetNumber());
            //System.out.println(allCSVData.get(i)[5]);
            //if setnumber in csv corresponds to database setnumber get data 
            
            //System.out.println(Integer.parseInt(allCSVData.get(i)[5])==(displayedSets.get(5).getSetNumber()));
            
            if(Integer.parseInt(allCSVData.get(i)[0])==(displayedSets.get(5).getSetNumber())){
                flag=true;
                //System.out.println("TRUE");
                indexInAllCSVData=i;
                //save this data from csv array
                starred=allCSVData.get(indexInAllCSVData)[1];
                starredNamesRaw = allCSVData.get(indexInAllCSVData)[2];
                System.out.println(starredNamesRaw);
                //split string to create an array on titles
                String word = "";
                ArrayList<String> arraylistWord = new ArrayList<>();
                //System.out.println(starredNamesRaw);
                //System.out.println(starredNamesRaw.length());
                for(int j = 0; j<starredNamesRaw.length();j++){
                    if(starredNamesRaw.charAt(j)==(',')){
                        arraylistWord.add(word);
                        //System.out.println("added: " + word);
                        word="";
                    }else{
                        word=word+starredNamesRaw.charAt(j);
                        //System.out.println(word);
                    }
                }
                
                //transfere files from arraylist to array
                starredNames=new String[arraylistWord.size()];
                for(int j = 0; j<arraylistWord.size(); j++){
                    //System.out.println(arraylistWord.get(j));
                    //System.out.println(arraylistWord.get(j));
                    starredNames[j] = arraylistWord.get(j);
                }
            }
        }
        
        //for(int l = 0; l<5;l++){
           // System.out.println(starredNames[l]);
        //}
        //System.out.println(starredNames.length);
                
        if(flag==false){
            try{
                
                FileWriter fr = new FileWriter("flaggedTerms.txt",true);
                BufferedWriter br = new BufferedWriter(fr);
                br.write(""+displayedSets.get(5).getSetNumber()+"\n0\n,,,,,");
                br.close();
                fr.close();
                System.out.println("sucess");
            }catch(IOException e){
                System.out.println(e);
                JOptionPane.showMessageDialog(null,e, "WARNING", JOptionPane.WARNING_MESSAGE);
            }
        }
            
                
        for(int i = 0; i<5;i++){
          //System.out.println(displayedSets.get(5).getTableTitles()[i]);
          //System.out.println(starredNames[i]);
        }
        new FlashcardSelector(this, username, displayedSets.get(5), starredNames, starred, displayedSets.get(5).getSetNumber()).setVisible(true);
    
    }//GEN-LAST:event_FC6boxActionPerformed

    private void CreateSetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CreateSetActionPerformed
        createSet cs = new createSet(username,this,null); 
        cs.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_CreateSetActionPerformed

    private void nextPageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextPageActionPerformed
        
        while(displayedSets.isEmpty()==false){
            displayedSets.remove(0);
        }
        
        currentIndex=currentIndex+6;
        //prevPage.setVisible(true);
        prevPageVisible(true);
     
        pageDisplayed+=1;
        getSetData(currentIndex);
        displayBoxes(currentIndex);
        
        if(currentIndex+6>=totalSets){
            //nextPage.setVisible(false);
            //nextPage.setOpaque(false);
            //nextPage.setContentAreaFilled(false);
            //nextPage.setBorderPainted(false);
            //nextPage.setText("");
            nextPageVisible(false);
        }else{
            nextPage.setOpaque(true);
            nextPage.setContentAreaFilled(true);
            nextPage.setBorderPainted(true);
            nextPage.setText(">");
            nextPageVisible(true);
        }
        
    }//GEN-LAST:event_nextPageActionPerformed

    private void prevPageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prevPageActionPerformed
        
        
        while(displayedSets.isEmpty()==false){
            displayedSets.remove(0);
        }
        
        currentIndex=currentIndex-6;
        //nextPage.setVisible(true);
        //nextPage.setOpaque(true);
        //nextPage.setContentAreaFilled(true);
        //nextPage.setBorderPainted(true);
        //nextgPage.setText(">");
        nextPageVisible(true);
        
        pageDisplayed-=1;
        getSetData(currentIndex);
        displayBoxes(currentIndex);
        
        if(currentIndex-6<=0){
            //prevPage.setVisible(false);
            //prevPage.setOpaque(false);
            //prevPage.setContentAreaFilled(false);
            //prevPage.setBorderPainted(false);
            //prevPage.setText("");
            prevPageVisible(false);
        }else{
            //prevPage.setOpaque(true);
            //prevPage.setContentAreaFilled(true);
            //prevPage.setBorderPainted(true);
            //prevPage.setText("<");
            prevPageVisible(true);
        }
        
    }//GEN-LAST:event_prevPageActionPerformed

    private void searchbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchbuttonActionPerformed
        this.setVisible(false);
        String search = searchfield.getText();
        new search(username, this, search).setVisible(true);
    }//GEN-LAST:event_searchbuttonActionPerformed

    private void searchfieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchfieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchfieldActionPerformed

    /**
     * Event handler for the main folder management button.
     * Opens the folder editor interface for organizing sets.
     * 
     * @param evt The action event from clicking the button
     */
    private void FolderMPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FolderMPActionPerformed
        
        pageDisplayed=1;
        currentIndex=0;
        totalSets=0;
        setNumbers = new ArrayList<>();
        
        Connection con = DBConnection.getConnection();
       
        String query2 = "SELECT `Set number` FROM `setdata` WHERE`Set Creator`=?";

        try{

            PreparedStatement ps = con.prepareStatement(query2);

            ps.setString(1,username);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                int tempSetNumber = rs.getInt("Set number");
                //System.out.println(tempSetNumber);
                setNumbers.add(tempSetNumber);
                totalSets+=1;
            }                                           

        }catch(Exception e){
            JOptionPane.showMessageDialog(null,e, "WARNING", JOptionPane.WARNING_MESSAGE);
            System.out.println(e);
        }
        
        //System.out.println(setNumbers.size());

        getSetData(currentIndex);
        
        closeFolderEditor();
        //prevPage.setVisible(false);
        prevPageVisible(false);
        
        
        if(setNumbers.size()>6){
            //nextPage.setVisible(true);
            //System.out.println("here");
            nextPageVisible(true);
        }else{
            //nextPage.setVisible(false);
            nextPageVisible(false);
        }
         
    }//GEN-LAST:event_FolderMPActionPerformed

    public void loadFolder1(){  
        
        getFolderDetails();
        
        enterSetNumberFeild.setText("");
        
        String setNumberRaw = folders.get(0).getsetNumbers();
        ArrayList<Integer> setsInFolder = new ArrayList<>();
        String number = "";
        for(int i = 0; i<setNumberRaw.length();i++){
            if(setNumberRaw.charAt(i)!=','){
                number=number+setNumberRaw.charAt(i);
            }else{
                
                if(number!=""){
                    setsInFolder.add(Integer.parseInt(number));
                    number="";
                }
            }
        }
        
        pageDisplayed=1;
        currentIndex=0;
        
        setNumbers = new ArrayList<>();
        
        ArrayList<Integer> setsToDelete = new ArrayList<>();
         
        int deletedSets = 0;
        for(int i =0; i<setsInFolder.size();i++){
            if(validateSetExsistance(setsInFolder.get(i))){
                setNumbers.add(setsInFolder.get(i));
            }else{
                setsToDelete.add(setsInFolder.get(i));
                deletedSets=deletedSets+1;
                //i=i-1;
            }
        }
        
        for(int i =0; i<setsToDelete.size();i++){
            removeSet(setsToDelete.get(i),folders.get(0).getfoldernumber());
        }
        
        
        if(deletedSets!=0){
            JOptionPane.showMessageDialog(null,deletedSets + " sets have been deleted since folder last opened", "Denied", JOptionPane.WARNING_MESSAGE);
        }
        
        //System.out.println("size"+ setNumbers.size());
        
        totalSets=setNumbers.size();
        
        openFolderEditor();
        
        getSetData(currentIndex);
        
        //prevPage.setVisible(false);
        prevPageVisible(false);
        if(setNumbers.size()>6){
            //nextPage.setVisible(true);
            nextPageVisible(true);
        }else{
            //nextPage.setVisible(false);
            nextPageVisible(false);
        }
        
        folderOpened=1;
    }
    
    
    /**
     * Event handler for clicking the first folder button.
     * Opens the selected folder to display its flashcard sets.
     * 
     * @param evt The action event from clicking the button
     */
    private void Folder1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Folder1ActionPerformed
        
        loadFolder1();
        
    }//GEN-LAST:event_Folder1ActionPerformed

    public void loadFolder2(){
        
        String setNumberRaw = folders.get(1).getsetNumbers();
        ArrayList<Integer> setsInFolder = new ArrayList<>();
        String number = "";
        for(int i = 0; i<setNumberRaw.length();i++){
            if(setNumberRaw.charAt(i)!=','){
                number=number+setNumberRaw.charAt(i);
            }else{
                
                if(number!=""){
                    setsInFolder.add(Integer.parseInt(number));
                    number="";
                }
            }
        }
        
        pageDisplayed=1;
        currentIndex=0;
        
        setNumbers = new ArrayList<>();
        
        ArrayList<Integer> setsToDelete = new ArrayList<>();
         
        int deletedSets = 0;
        for(int i =0; i<setsInFolder.size();i++){
            if(validateSetExsistance(setsInFolder.get(i))){
                setNumbers.add(setsInFolder.get(i));
            }else{
                setsToDelete.add(setsInFolder.get(i));
                deletedSets=deletedSets+1;
                //i=i-1;
            }
        }
        
        for(int i =0; i<setsToDelete.size();i++){
            removeSet(setsToDelete.get(i),folders.get(1).getfoldernumber());
        }
        
        
        if(deletedSets!=0){
            JOptionPane.showMessageDialog(null,deletedSets + " sets have been deleted since folder last opened", "Denied", JOptionPane.WARNING_MESSAGE);
        }
        
        //System.out.println("size"+ setNumbers.size());
        
        totalSets=setNumbers.size();
        
        getSetData(currentIndex);
        
        openFolderEditor();
        
        //prevPage.setVisible(false);
        prevPageVisible(false);
        if(setNumbers.size()>6){
            //nextPage.setVisible(true);
            nextPageVisible(true);
        }else{
             //nextPage.setVisible(false);
             nextPageVisible(false);
        }
        
        folderOpened=2;
    }
    
    private void Folder2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Folder2ActionPerformed
        
        loadFolder2();
        
    }//GEN-LAST:event_Folder2ActionPerformed

    public void loadFolder3(){
        String setNumberRaw = folders.get(2).getsetNumbers();
        ArrayList<Integer> setsInFolder = new ArrayList<>();
        String number = "";
        for(int i = 0; i<setNumberRaw.length();i++){
            if(setNumberRaw.charAt(i)!=','){
                number=number+setNumberRaw.charAt(i);
            }else{
                
                if(number!=""){
                    setsInFolder.add(Integer.parseInt(number));
                    number="";
                }
            }
        }
        
        pageDisplayed=1;
        currentIndex=0;
        
        setNumbers = new ArrayList<>();
        
        ArrayList<Integer> setsToDelete = new ArrayList<>();
         
        int deletedSets = 0;
        for(int i =0; i<setsInFolder.size();i++){
            if(validateSetExsistance(setsInFolder.get(i))){
                setNumbers.add(setsInFolder.get(i));
            }else{
                setsToDelete.add(setsInFolder.get(i));
                deletedSets=deletedSets+1;
                //i=i-1;
            }
        }
        
        for(int i =0; i<setsToDelete.size();i++){
            removeSet(setsToDelete.get(i),folders.get(2).getfoldernumber());
        }
        
        
        if(deletedSets!=0){
            JOptionPane.showMessageDialog(null,deletedSets + " sets have been deleted since folder last opened", "Denied", JOptionPane.WARNING_MESSAGE);
        }
        
        //System.out.println("size"+ setNumbers.size());
        
        totalSets=setNumbers.size();
        
        getSetData(currentIndex);
        
        openFolderEditor();
        
        //prevPage.setVisible(false);
        prevPageVisible(false);
        if(setNumbers.size()>6){
            //nextPage.setVisible(true);
            nextPageVisible(true);
        }else{
           //  nextPage.setVisible(false);
             nextPageVisible(false);
        }
        
        folderOpened=3;
    }
    
    private void Folder3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Folder3ActionPerformed
        
        loadFolder3();
        
    }//GEN-LAST:event_Folder3ActionPerformed

    public void loadFolder4(){
        String setNumberRaw = folders.get(3).getsetNumbers();
        ArrayList<Integer> setsInFolder = new ArrayList<>();
        String number = "";
        for(int i = 0; i<setNumberRaw.length();i++){
            if(setNumberRaw.charAt(i)!=','){
                number=number+setNumberRaw.charAt(i);
            }else{
                
                if(number!=""){
                    setsInFolder.add(Integer.parseInt(number));
                    number="";
                }
            }
        }
        
        pageDisplayed=1;
        currentIndex=0;
        
        setNumbers = new ArrayList<>();
        
        ArrayList<Integer> setsToDelete = new ArrayList<>();
         
        int deletedSets = 0;
        for(int i =0; i<setsInFolder.size();i++){
            if(validateSetExsistance(setsInFolder.get(i))){
                setNumbers.add(setsInFolder.get(i));
            }else{
                setsToDelete.add(setsInFolder.get(i));
                deletedSets=deletedSets+1;
                //i=i-1;
            }
        }
        
        for(int i =0; i<setsToDelete.size();i++){
            removeSet(setsToDelete.get(i),folders.get(3).getfoldernumber());
        }
        
        
        if(deletedSets!=0){
            JOptionPane.showMessageDialog(null,deletedSets + " sets have been deleted since folder last opened", "Denied", JOptionPane.WARNING_MESSAGE);
        }
        
        //System.out.println("size"+ setNumbers.size());
        
        totalSets=setNumbers.size();
        
        getSetData(currentIndex);
        
        openFolderEditor();
        
        //prevPage.setVisible(false);
        prevPageVisible(false);
        if(setNumbers.size()>6){
            //nextPage.setVisible(true);
            nextPageVisible(true);
        }else{
             //nextPage.setVisible(false);
             nextPageVisible(false);
        }
        
        folderOpened=4;
    }
    
    
    private void Folder4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Folder4ActionPerformed
        
        loadFolder4();
        
    }//GEN-LAST:event_Folder4ActionPerformed

    public void loadFolder5(){
        
        String setNumberRaw = folders.get(4).getsetNumbers();
        ArrayList<Integer> setsInFolder = new ArrayList<>();
        String number = "";
        for(int i = 0; i<setNumberRaw.length();i++){
            if(setNumberRaw.charAt(i)!=','){
                number=number+setNumberRaw.charAt(i);
            }else{
                
                if(number!=""){
                    setsInFolder.add(Integer.parseInt(number));
                    number="";
                }
            }
        }
        
        pageDisplayed=1;
        currentIndex=0;
        
        setNumbers = new ArrayList<>();
        
        ArrayList<Integer> setsToDelete = new ArrayList<>();
         
        int deletedSets = 0;
        for(int i =0; i<setsInFolder.size();i++){
            if(validateSetExsistance(setsInFolder.get(i))){
                setNumbers.add(setsInFolder.get(i));
            }else{
                setsToDelete.add(setsInFolder.get(i));
                deletedSets=deletedSets+1;
                //i=i-1;
            }
        }
        
        for(int i =0; i<setsToDelete.size();i++){
            removeSet(setsToDelete.get(i),folders.get(4).getfoldernumber());
        }
        
        
        if(deletedSets!=0){
            JOptionPane.showMessageDialog(null,deletedSets + " sets have been deleted since folder last opened", "Denied", JOptionPane.WARNING_MESSAGE);
        }
        
        //System.out.println("size"+ setNumbers.size());
        
        totalSets=setNumbers.size();
        
        getSetData(currentIndex);
        
        openFolderEditor();
        
        //prevPage.setVisible(false);
        prevPageVisible(false);
        if(setNumbers.size()>6){
            //nextPage.setVisible(true);
            nextPageVisible(true);
        }else{
             //nextPage.setVisible(false);
             nextPageVisible(false);
        }
        
        folderOpened=5;
    }
    
    private void Folder5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Folder5ActionPerformed
        
        loadFolder5();
    
    }//GEN-LAST:event_Folder5ActionPerformed

    public void loadFolder6(){
        String setNumberRaw = folders.get(5).getsetNumbers();
        ArrayList<Integer> setsInFolder = new ArrayList<>();
        String number = "";
        for(int i = 0; i<setNumberRaw.length();i++){
            if(setNumberRaw.charAt(i)!=','){
                number=number+setNumberRaw.charAt(i);
            }else{
                
                if(number!=""){
                    setsInFolder.add(Integer.parseInt(number));
                    number="";
                }
            }
        }
        
        pageDisplayed=1;
        currentIndex=0;
        
        setNumbers = new ArrayList<>();
        
        ArrayList<Integer> setsToDelete = new ArrayList<>();
         
        int deletedSets = 0;
        for(int i =0; i<setsInFolder.size();i++){
            if(validateSetExsistance(setsInFolder.get(i))){
                setNumbers.add(setsInFolder.get(i));
            }else{
                setsToDelete.add(setsInFolder.get(i));
                deletedSets=deletedSets+1;
                //i=i-1;
            }
        }
        
        for(int i =0; i<setsToDelete.size();i++){
            removeSet(setsToDelete.get(i),folders.get(5).getfoldernumber());
        }
        
        
        if(deletedSets!=0){
            JOptionPane.showMessageDialog(null,deletedSets + " sets have been deleted since folder last opened", "Denied", JOptionPane.WARNING_MESSAGE);
        }
        
        //System.out.println("size"+ setNumbers.size());
        
        totalSets=setNumbers.size();
        
        getSetData(currentIndex);
        
        openFolderEditor();
        
        //prevPage.setVisible(false);
        prevPageVisible(false);
        if(setNumbers.size()>6){
            //nextPage.setVisible(true);
            nextPageVisible(true);
        }else{
             //nextPage.setVisible(false);
             nextPageVisible(false);
        }
        
        folderOpened=6;
        
    }
    
    private void Folder6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Folder6ActionPerformed
        
        loadFolder6();
        
    }//GEN-LAST:event_Folder6ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        
        if(folders.size()==6){
            JOptionPane.showMessageDialog(null,"You have reached you limit for folders created", "Denied", JOptionPane.WARNING_MESSAGE);
        }else{
            String newfoldername = JOptionPane.showInputDialog(null, "Please enter folder name");
            
            if(newfoldername!=null){
                //System.out.println(newfoldername);
                boolean flag = false;
                for(int i =0;i<folders.size();i++){
                    if(folders.get(i).getfolderName().toUpperCase().equals(newfoldername.toUpperCase())){
                        JOptionPane.showMessageDialog(null,"This folder name already Exists. Please create a new unique folder name", "Denied", JOptionPane.WARNING_MESSAGE);
                        flag=true;
                    }   
                }

                if(flag!=true){
                    Connection con = DBConnection.getConnection();

                    String query = "INSERT INTO `folders`(`FolderName`, `User`, `SetNumbers`) VALUES ('"+newfoldername+"','"+username+"','"+","+"')";

                    try{

                        Statement statement = con.createStatement();
                        statement.executeUpdate(query);

                        System.out.println("Success");

                        getFolderDetails();

                        //openFolderEditor();

                    }catch(Exception e){
                        
                        JOptionPane.showMessageDialog(null,e, "WARNING", JOptionPane.WARNING_MESSAGE);
                        System.out.println(e);
                    }
                }
            }
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void addfolderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addfolderActionPerformed
        if(addfolder.isSelected()){
            removeFolder.setSelected(false);
        }else{
            removeFolder.setSelected(true);
        }
    }//GEN-LAST:event_addfolderActionPerformed

    private void enterSetNumberFeildActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_enterSetNumberFeildActionPerformed
        
        
    }//GEN-LAST:event_enterSetNumberFeildActionPerformed

    private void editFolderButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editFolderButtonActionPerformed

        int tempSetNumber  = Integer.parseInt(enterSetNumberFeild.getText());
        
        if(editFolderButton.getText()!=""){
            
            if(addfolder.isSelected()){

                if(validateSetExsistance(tempSetNumber)){

                    if(setNumbers.contains(tempSetNumber)){
                        JOptionPane.showMessageDialog(null,"This set is already in this folder", "Denied", JOptionPane.WARNING_MESSAGE);      
                    }else{

                        String tempString = folders.get(folderOpened-1).getsetNumbers();
                        tempString = tempString + tempSetNumber +",";
                        folders.get(folderOpened-1).setsetNumbers(tempString);

                        Connection con = DBConnection.getConnection();

                        String query = "UPDATE `folders` SET `SetNumbers`='" + tempString + "' WHERE `FolderIndex`='"+folders.get(folderOpened-1).getfoldernumber()+"'";

                        try{

                            Statement statement = con.createStatement();

                            statement.executeUpdate(query);
                            
                            JOptionPane.showMessageDialog(null,"Set added To folder", "Sucess", JOptionPane.WARNING_MESSAGE);
                            System.out.println("Success");

                        }catch(Exception e){

                            JOptionPane.showMessageDialog(null,e, "WARNING", JOptionPane.WARNING_MESSAGE);
                            System.out.println(e);
                        }
                    }
                }else{
                    JOptionPane.showMessageDialog(null,"This set does not exist", "Denied", JOptionPane.WARNING_MESSAGE);      
                }
            }else{
               removeSet(tempSetNumber,folders.get(folderOpened-1).getfoldernumber());
               
            }
            
            if(folderOpened==1){
                loadFolder1();
            }
            if(folderOpened==2){
                loadFolder2();
            }
            if(folderOpened==3){
                loadFolder3();
            }
            if(folderOpened==4){
                loadFolder4();
            }
            if(folderOpened==5){
                loadFolder5();
            }
            if(folderOpened==6){
                loadFolder6();
            }
            
            
            
        }
    }//GEN-LAST:event_editFolderButtonActionPerformed

    private void removeFolderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeFolderActionPerformed
        if(removeFolder.isSelected()){
            addfolder.setSelected(false);
        }else{
            addfolder.setSelected(true);
        }
    }//GEN-LAST:event_removeFolderActionPerformed

    private void enterSetNumberFeildKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_enterSetNumberFeildKeyPressed
        
        char c = evt.getKeyChar();
        if(isNumeric(""+c)){
            enterSetNumberFeild.setEditable(true);            
        }else{
            enterSetNumberFeild.setEditable(false);   
        }
    }//GEN-LAST:event_enterSetNumberFeildKeyPressed

    private void backspaceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backspaceActionPerformed
        String text = enterSetNumberFeild.getText();
        text = text.substring(0,text.length()-1);
        enterSetNumberFeild.setText(text);
    }//GEN-LAST:event_backspaceActionPerformed

    private void MailButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MailButtonActionPerformed
        new MailMenu(username, this).setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_MailButtonActionPerformed
    
    private void progressDashboardButtonActionPerformed(java.awt.event.ActionEvent evt) {
        // Show the progress dashboard
        showProgressDashboard();
    }

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        //prevFrame.setVisible(true);
        
        LoginScreen ls2 = new LoginScreen();
        ls2.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_formWindowClosed

    private void Cancel5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Cancel5ActionPerformed
        //returns to previous frame
        this.dispose();

    }//GEN-LAST:event_Cancel5ActionPerformed

    /**
     * Sets up placeholder text for text fields to provide user guidance
     */
    private void setupPlaceholders() {
        // Placeholder text is now set via form properties
        // Additional placeholders can be set here if needed
        enterSetNumberFeild.putClientProperty("JTextField.placeholderText", "Enter set number...");
    }
    
    /**
     * Sets up Enter key listeners for text fields to improve form navigation
     */
    private void setupEnterKeyListeners() {
        // Add enter key listener to search field
        searchfield.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    searchbutton.doClick();
                }
            }
        });
        
        // Add enter key listener to set number field
        enterSetNumberFeild.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    jButton1.doClick(); // Add to folder button
                }
            }
        });
    }
    
    /**
     * Adds keyboard shortcuts to improve accessibility and usability
     */
    private void addKeyboardShortcuts() {
        // Alt+C for Create Set
        CreateSet.setMnemonic(KeyEvent.VK_C);
        
        // Alt+S for Search
        searchbutton.setMnemonic(KeyEvent.VK_S);
        
        // Alt+M for Mail
        MailButton.setMnemonic(KeyEvent.VK_M);
        progressDashboardButton.setMnemonic(KeyEvent.VK_P);
        
        // Alt+E for Edit Folder
        editFolderButton.setMnemonic(KeyEvent.VK_E);
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Cancel5;
    private javax.swing.JButton CreateSet;
    private javax.swing.JButton FC1box;
    private javax.swing.JButton FC2box;
    private javax.swing.JButton FC3box;
    private javax.swing.JButton FC4box;
    private javax.swing.JButton FC5box;
    private javax.swing.JButton FC6box;
    private javax.swing.JButton Folder1;
    private javax.swing.JButton Folder2;
    private javax.swing.JButton Folder3;
    private javax.swing.JButton Folder4;
    private javax.swing.JButton Folder5;
    private javax.swing.JButton Folder6;
    private javax.swing.JButton FolderMP;
    private javax.swing.JButton MailButton;
    private javax.swing.JButton progressDashboardButton;
    private javax.swing.JProgressBar ProgressBar;
    private javax.swing.JRadioButton addfolder;
    private javax.swing.JButton backspace;
    private javax.swing.JButton editFolderButton;
    private javax.swing.JTextField enterSetNumberFeild;
    private javax.swing.JLabel folderEditorLabel;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;

    private javax.swing.JButton nextPage;
    private javax.swing.JButton prevPage;
    private javax.swing.JRadioButton removeFolder;
    private javax.swing.JButton searchbutton;
    private javax.swing.JTextField searchfield;
    private javax.swing.JLabel welcomeText;
    // End of variables declaration//GEN-END:variables
    
    // Progress dashboard components
    private javax.swing.JPanel dashboardPanel;
    private ProgressDashboardUtils.DashboardComponents dashboardComponents;
    private boolean dashboardVisible = false;
    
    /**
     * Returns the username for the current user
     * @return the username
     */
    public String getUsername() {
        return this.username;
    }
    
    /**
     * Opens the flashcard selector to study cards
     */
    public void openFlashcards() {
        FlashcardSelector fs = new FlashcardSelector(this, username, null, null, null, 0);
        fs.setVisible(true);
        this.setVisible(false);
    }
    
    /**
     * Shows the main content panels and hides the dashboard
     */
    public void showMainContent() {
        // Show the main content panels
        jPanel2.setVisible(true);
        jPanel3.setVisible(true);
        jPanel5.setVisible(true);
        jPanel6.setVisible(true);
        
        // Hide dashboard panel
        if (dashboardPanel != null) {
            dashboardPanel.setVisible(false);
        }
        
        dashboardVisible = false;
    }
    
    /**
     * Shows the progress dashboard with updated data
     */
    public void showProgressDashboard() {
        if (dashboardComponents == null) {
            // Initialize dashboard on first use
            dashboardComponents = ProgressDashboardUtils.createDashboardPanel(this);
            dashboardPanel = dashboardComponents.dashboardPanel;
            getContentPane().add(dashboardPanel);
            validate();
        }
        
        // Hide the main content panels
        jPanel2.setVisible(false);
        jPanel3.setVisible(false);
        jPanel5.setVisible(false);
        jPanel6.setVisible(false);
        
        // Show dashboard panel
        dashboardPanel.setVisible(true);
        
        // Update the dashboard data
        updateDashboardData();
        
        dashboardVisible = true;
    }
    
    /**
     * Updates all the data displayed on the dashboard
     */
    public void updateDashboardData() {
        if (dashboardComponents == null) {
            return; // Nothing to update if dashboard hasn't been initialized
        }
        
        // Get data from the SRSProgressTracker
        Map<Integer, Integer> levelCounts = projects.SRSProgressTracker.getSrsLevelCounts(username);
        int dueCardCount = projects.SRSProgressTracker.getDueCardCount(username);
        int totalCardCount = projects.SRSProgressTracker.getTotalCardCount(username);
        Map<String, Integer> weeklyStats = projects.SRSProgressTracker.getWeeklyStats(username);
        double masteryPercentage = projects.SRSProgressTracker.calculateMasteryPercentage(username);
        
        // Update statistics panel
        if (dashboardComponents.totalCardsValue != null) {
            dashboardComponents.totalCardsValue.setText(String.valueOf(totalCardCount));
        }
        
        if (dashboardComponents.reviewedCardsValue != null) {
            dashboardComponents.reviewedCardsValue.setText(String.valueOf(weeklyStats.getOrDefault("reviewed", 0)));
        }
        
        if (dashboardComponents.masteredCardsValue != null) {
            dashboardComponents.masteredCardsValue.setText(String.valueOf(weeklyStats.getOrDefault("mastered", 0)));
        }
        
        // Update due today panel
        if (dashboardComponents.dueCardCount != null) {
            dashboardComponents.dueCardCount.setText(String.valueOf(dueCardCount));
        }
        
        int masteryPercent = (int) Math.round(masteryPercentage);
        if (dashboardComponents.masteryProgressBar != null) {
            dashboardComponents.masteryProgressBar.setValue(masteryPercent);
        }
        
        if (dashboardComponents.masteryPercent != null) {
            dashboardComponents.masteryPercent.setText(masteryPercent + "%");
        }
        
        // Enable/disable study button based on due cards
        if (dashboardComponents.studyDueCardsButton != null) {
            dashboardComponents.studyDueCardsButton.setEnabled(dueCardCount > 0);
        }
        
        // Update the level bars
        if (dashboardComponents.levelBars != null) {
            updateLevelProgressBars(levelCounts, totalCardCount);
        }
    }
    
    /**
     * Updates the progress bars for each SRS level
     */
    private void updateLevelProgressBars(Map<Integer, Integer> levelCounts, int totalCardCount) {
        if (totalCardCount > 0 && dashboardComponents != null && dashboardComponents.levelBars != null) {
            // Calculate percentages for each level
            int level0Count = levelCounts.getOrDefault(0, 0);
            int level1Count = levelCounts.getOrDefault(1, 0);
            int level2Count = levelCounts.getOrDefault(2, 0);
            int level3Count = levelCounts.getOrDefault(3, 0);
            int level4Count = levelCounts.getOrDefault(4, 0);
            int level5Count = levelCounts.getOrDefault(5, 0);
            
            // Update progress bars
            dashboardComponents.levelBars[0].setValue((int) ((level0Count / (double) totalCardCount) * 100));
            dashboardComponents.levelBars[1].setValue((int) ((level1Count / (double) totalCardCount) * 100));
            dashboardComponents.levelBars[2].setValue((int) ((level2Count / (double) totalCardCount) * 100));
            dashboardComponents.levelBars[3].setValue((int) ((level3Count / (double) totalCardCount) * 100));
            dashboardComponents.levelBars[4].setValue((int) ((level4Count / (double) totalCardCount) * 100));
            dashboardComponents.levelBars[5].setValue((int) ((level5Count / (double) totalCardCount) * 100));
            
            // Update tooltips with actual count
            dashboardComponents.levelBars[0].setToolTipText(level0Count + " cards");
            dashboardComponents.levelBars[1].setToolTipText(level1Count + " cards");
            dashboardComponents.levelBars[2].setToolTipText(level2Count + " cards");
            dashboardComponents.levelBars[3].setToolTipText(level3Count + " cards");
            dashboardComponents.levelBars[4].setToolTipText(level4Count + " cards");
            dashboardComponents.levelBars[5].setToolTipText(level5Count + " cards");
        } else if (dashboardComponents != null && dashboardComponents.levelBars != null) {
            // Reset progress bars if there are no cards
            for (int i = 0; i < dashboardComponents.levelBars.length; i++) {
                dashboardComponents.levelBars[i].setValue(0);
                dashboardComponents.levelBars[i].setToolTipText("0 cards");
            }
        }
    }
}
