package auxillary_functions;

import flashcards.FlashcardSelector;
import main_page.MainPage;
import main_page.SetSelector;
import creator.createSet;
import projects.DBConnection;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 * The search class provides a graphical user interface for searching and filtering flashcard sets.
 * It allows users to search sets by name or creator, filter by topic, and sort results by creation
 * or update date. The class implements a heap sort algorithm for date-based sorting.
 * 
 * Key features:
 * - Search by set name or creator username
 * - Filter sets by academic subject/topic
 * - Sort results by creation date or last update date
 * - Double-click to open selected flashcard sets
 * - Create new sets directly from search interface
 */
public class search extends javax.swing.JFrame {

    /** The username of the currently logged-in user */
    private String currentUser;
    
    /** Reference to the previous MainPage frame to return to when closing */
    private MainPage prevFrame;   
    
    /** List of flashcard sets matching the current search criteria */
    private ArrayList<SetSelector> searchResults;
    
    /** Date formatter for handling creation and update dates */
    private SimpleDateFormat StD;
        
    /**
     * Creates a new search window with initial search parameters.
     * 
     * @param c The username of the current user
     * @param p Reference to the previous MainPage frame
     * @param originalSearch Initial search term to execute
     */
    public search(String c, MainPage p, String originalSearch) {
        
        initComponents();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        

        
        currentUser = c;
        prevFrame = p;
              
        sortSetname.setSelected(true);
            
        
        searching(originalSearch);
        
        AscendingSort.setSelected(true);
        AscendingSort.setVisible(false);
        DecendingSort.setVisible(false);
        dateSortedLabel.setVisible(false);

        StD=new SimpleDateFormat("dd-MM-yyyy"); 
    }
    
    /**
     * Performs a search for flashcard sets based on the given search term and current filter settings.
     * This method queries the database for sets matching the search criteria and updates the results table.
     * The search can be filtered by:
     * - Set name or creator name (based on radio button selection)
     * - Topic/subject area
     * Results can be sorted by:
     * - Creation date
     * - Last update date
     * in either ascending or descending order using heap sort.
     *
     * @param searchTerm The text to search for in set names or creator names
     */
    public void searching(String searchTerm) {
        searchResults = new ArrayList<SetSelector>();
        
        String sortTopic;
        int results = 0;
        
        try {
            sortTopic = (String)TopicPicker2.getSelectedItem();
        } catch(Exception e) {
            sortTopic = "All";
        }
        
        int sortby;
        searchfield.setText(searchTerm);
        
        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = null;
            String query;
            
            if(sortSetname.isSelected()) {
                // Search by set name
                if(sortTopic.equals("All")) {
                    query = "SELECT * FROM `setdata` WHERE `Set Name` LIKE ?";
                    ps = con.prepareStatement(query);
                    ps.setString(1, "%" + searchTerm + "%");
                } else {
                    query = "SELECT * FROM `setdata` WHERE `Set Name` LIKE ? AND `SetTopic`=?";
                    ps = con.prepareStatement(query);
                    ps.setString(1, "%" + searchTerm + "%");
                    ps.setString(2, sortTopic);
                }
            } else {
                // Search by creator name
                if(sortTopic.equals("All")) {
                    query = "SELECT * FROM `setdata` WHERE `Set Creator` LIKE ?";
                    ps = con.prepareStatement(query);
                    ps.setString(1, "%" + searchTerm + "%");
                } else {
                    query = "SELECT * FROM `setdata` WHERE `Set Creator` LIKE ? AND `SetTopic`=?";
                    ps = con.prepareStatement(query);
                    ps.setString(1, "%" + searchTerm + "%");
                    ps.setString(2, sortTopic);
                }
            }
            
            ResultSet rs = ps.executeQuery();

                while(rs.next()){
                    results = results + 1;
                    int setNumber = rs.getInt("Set number");
                    
                    String setCreator = rs.getString("Set Creator");
                    boolean Verified = rs.getBoolean("Verified");
                    String accessType = rs.getString("accessType");
                    String password = rs.getString("Password");
                    String tableTitlesString = rs.getString("TableTitles");
                    
                    int numColumns=0;
                    for(int j = 0; j<tableTitlesString.length();j++){
                        if(tableTitlesString.charAt(j)==','){
                            numColumns=numColumns+1;
                        }
                    }
                    
                    String tableTitles[] = new String[numColumns];
                    String tempword ="";
                    int wordsInserted=0;
                    for(int j = 0; j<tableTitlesString.length();j++){                    
                        if(tableTitlesString.charAt(j)!=','){
                           tempword=tempword+tableTitlesString.charAt(j);
                        }else{
                           tableTitles[wordsInserted]=tempword;
                           wordsInserted+=1;
                           tempword ="";
                        }
                    }
                    
                    String setData = rs.getString("setData");
                    String setName = rs.getString("Set Name");
                    String setNotes = rs.getString("setNotes");
                    String setTopics = rs.getString("SetTopic");
                    String dateCreated = rs.getString("DateCreated");
                    String dateUpdated = rs.getString("DateUpdated");
                    
                    
                    SetSelector ss = new SetSelector(setNumber,setCreator,Verified,accessType,password,tableTitles,setData,setName,setNotes,setTopics,dateCreated,dateUpdated);
                    
                    //System.out.println(ss.getDateU());
                    searchResults.add(ss);
                    
                    //System.out.println("DONE" + searchTerm);
                }                                           

            }catch(Exception e){
                JOptionPane.showMessageDialog(null,e, "WARNING", JOptionPane.WARNING_MESSAGE);
                System.out.println(e);
            }
        
        if(dsc.isSelected()||dsu.isSelected()){

            if(dsc.isSelected()){
                sortby=6;
                
            }else{
                //sort dataUpdated
                sortby=7;
            }
            
            if(AscendingSort.isSelected()||DecendingSort.isSelected()){
            
                if(AscendingSort.isSelected()){

                    //int input [] =  {1,6,3,7,3,0,1,7,0,8,5,3,7,8,4,2};
                    
                    int n = searchResults.size();
                    
                    for(int i=(n/2)-1; i >= 0; i--){
                        heapify(searchResults, n, i,true,sortby);
                    }
                    
                    for (int i=n-1;i>-1;i--) {
                        //Date swap = StD.parse(searchResults.get(0).getDateC());
                        if(sortby==6){
                            String swap = searchResults.get(0).getDateC();
                            searchResults.get(0).setDateC(searchResults.get(i).getDateC());
                            searchResults.get(i).setDateC(swap);
                            heapify(searchResults, i, 0, true,sortby);
                        }else{
                            String swap = searchResults.get(0).getDateU();
                            searchResults.get(0).setDateU(searchResults.get(i).getDateU());
                            searchResults.get(i).setDateU(swap);
                            heapify(searchResults, i, 0, true,sortby);
                        }
                    }
                              
                //for(int i =0; i<searchResults.size(); i++){
                  //  System.out.println(searchResults.get(i).getDateC());
                //}    

                }else{
                //sort Decending
                    int n = searchResults.size();
                    
                    if(sortby==6){
                        for(int i=(n/2)-1; i >= 0; i--){
                            heapify(searchResults, n, i,false,sortby);
                        }

                        for (int i=n-1;i>-1;i--) {
                            //Date swap = StD.parse(searchResults.get(0).getDateC());
                            String swap = searchResults.get(0).getDateC();
                            searchResults.get(0).setDateC(searchResults.get(i).getDateC());
                            searchResults.get(i).setDateC(swap);
                            heapify(searchResults, i, 0, false,sortby);
                        }
                    }else{
                        for(int i=(n/2)-1; i >= 0; i--){
                            heapify(searchResults, n, i,false,sortby);
                        }

                        for (int i=n-1;i>-1;i--) {
                            //Date swap = StD.parse(searchResults.get(0).getDateC());
                            String swap = searchResults.get(0).getDateU();
                            searchResults.get(0).setDateU(searchResults.get(i).getDateU());
                            searchResults.get(i).setDateU(swap);
                            heapify(searchResults, i, 0, false,sortby);
                        }
                    }

                }

            }
             
        }
        
        SearchTermDisplay.setText("Showing " + results + " results for " + searchTerm + ":" );
        
        //System.out.println("Tabling now");
        DefaultTableModel model = (DefaultTableModel)this.DisplayedResult.getModel();
        //model.setRowCount(0);
        String[][] tableData = new String[results][8];
        for (int i=0; i<searchResults.size(); i++){
            for (int j=0;j<8;j++){
                String[] neededData= new String[8];
                neededData[0]=searchResults.get(i).getNameOfSet();
                neededData[1]=""+searchResults.get(i).getSetNumber();
                neededData[2]=searchResults.get(i).getNotesOfSet();
                neededData[4]=searchResults.get(i).getAccess();
                neededData[5]=searchResults.get(i).getSetCreator();
                neededData[3]=searchResults.get(i).getTopicOfSet();
                neededData[6]=searchResults.get(i).getDateC();
                neededData[7]=searchResults.get(i).getDateU();
                tableData[i]=neededData;
            }
        }
          /*  ArrayList<String> row = terms2.get(i);
            tableData[i] = row.toArray(new String[row.size()]);
        }
            */
        String[] colNames = {"Set Name","Set Number","Notes"," Topic","Access Type","Creator","Date Created","Date Last Updated"};

        model.setDataVector(tableData, colNames);        
    }
    
    /**
     * Maintains the heap property for the search results array during heap sort.
     * This method is used to sort search results by date (either creation or update date)
     * in either ascending or descending order.
     * 
     * The heap property ensures that for each parent node i:
     * - In ascending order: parent is greater than both children
     * - In descending order: parent is less than both children
     *
     * @param searchResults The list of SetSelector objects to heapify
     * @param n The size of the heap
     * @param i The index of the root node of the subtree to heapify
     * @param ascending True to sort in ascending order, false for descending
     * @param sortby 6 for creation date, 7 for update date
     */
    public void heapify(ArrayList<SetSelector> searchResults, int n, int i, boolean ascending, int sortby) {
        
        try {         
            if(sortby==6){
                if(ascending){
                    int largest = i;
                    int left = (2*i)+1;
                    int right = (2*i)+2;
                    if((left<n) && (StD.parse(searchResults.get(left).getDateC()).after(StD.parse(searchResults.get(largest).getDateC())))){
                        largest = left;
                    }
                    if((right<n) && (StD.parse(searchResults.get(right).getDateC()).after(StD.parse(searchResults.get(largest).getDateC())))){
                        largest = right;
                    }

                    if (largest != i) {
                        /*
                            String replace = searchResults.get(i).getDateC();
                            searchResults.get(i).setDateC(searchResults.get(largest).getDateC());
                            searchResults.get(largest).setDateC(replace);
                            heapify(searchResults, n, largest);
                        */

                            SetSelector replace = searchResults.get(i);
                            searchResults.set(i,(searchResults.get(largest)));
                            searchResults.set(largest,replace);
                            heapify(searchResults, n, largest,true,sortby);

                        }
                }else{

                    int smallest = i;
                    int left = (2*i)+1;
                    int right = (2*i)+2;

                    if((left<n) && (StD.parse(searchResults.get(left).getDateC()).before(StD.parse(searchResults.get(smallest).getDateC())))){
                        smallest = left;
                    }
                    if((right<n) && (StD.parse(searchResults.get(right).getDateC()).before(StD.parse(searchResults.get(smallest).getDateC())))){
                        smallest = right;
                    }

                    if (smallest != i) {
                    /*
                        String replace = searchResults.get(i).getDateC();
                        searchResults.get(i).setDateC(searchResults.get(largest).getDateC());
                        searchResults.get(largest).setDateC(replace);
                        heapify(searchResults, n, largest);
                    */

                        SetSelector replace = searchResults.get(i);
                        searchResults.set(i,(searchResults.get(smallest)));
                        searchResults.set(smallest,replace);
                        heapify(searchResults, n, smallest,false,sortby);

                    }
                }
            }else{
                if(ascending){
                    int largest = i;
                    int left = (2*i)+1;
                    int right = (2*i)+2;
                    if((left<n) && (StD.parse(searchResults.get(left).getDateU()).after(StD.parse(searchResults.get(largest).getDateU())))){
                        largest = left;
                    }
                    if((right<n) && (StD.parse(searchResults.get(right).getDateU()).after(StD.parse(searchResults.get(largest).getDateU())))){
                        largest = right;
                    }

                    if (largest != i) {

                            SetSelector replace = searchResults.get(i);
                            searchResults.set(i,(searchResults.get(largest)));
                            searchResults.set(largest,replace);
                            heapify(searchResults, n, largest,true,sortby);

                        }
                }else{

                    int smallest = i;
                    int left = (2*i)+1;
                    int right = (2*i)+2;

                    if((left<n) && (StD.parse(searchResults.get(left).getDateU()).before(StD.parse(searchResults.get(smallest).getDateU())))){
                        smallest = left;
                    }
                    if((right<n) && (StD.parse(searchResults.get(right).getDateU()).before(StD.parse(searchResults.get(smallest).getDateU())))){
                        smallest = right;
                    }

                    if (smallest != i) {

                        SetSelector replace = searchResults.get(i);
                        searchResults.set(i,(searchResults.get(smallest)));
                        searchResults.set(smallest,replace);
                        heapify(searchResults, n, smallest,false,sortby);

                    }
                }
            }
        } catch (ParseException ex) {
            Logger.getLogger(search.class.getName()).log(Level.SEVERE, null, ex);
        }
 
    }
    
    /**
     * Opens a flashcard set for viewing/editing when double-clicked in the search results.
     * Loads any starred/flagged terms associated with the set from the flaggedTerms.txt file.
     * If the set has no starred terms record, creates one with default values.
     *
     * @param setNumber The unique identifier of the set to open
     * @param rowNo The row index in the search results table
     */
    public void openSet(int setNumber, int rowNo) {
        String[] starredNames = new String[5];
        String starred = "0";
        String starredNamesRaw = "";
        int setNo = -1;
        
        String fileName = "flaggedTerms.txt";
        try (Scanner s = new Scanner(new FileReader(fileName))) {
            while(s.hasNext()) {
                String line = s.nextLine();
                if(line.equals(""+setNumber)) {
                    setNo = Integer.parseInt(line);
                    line = s.nextLine();
                    starred = line;
                    line = s.nextLine();
                    starredNamesRaw = line;
                }
            }
        } catch(FileNotFoundException e) {
            JOptionPane.showMessageDialog(null,e, "WARNING", JOptionPane.WARNING_MESSAGE);
        }            
        //if setnumber in csv corresponds to database setnumber get data 
        if(setNo!=-1){
            String word = "";
            ArrayList<String> arraylistWord = new ArrayList<>();
            for(int j = 0; j < starredNamesRaw.length(); j++) {
                if(starredNamesRaw.charAt(j) == ',') {
                    arraylistWord.add(word);
                    word = "";
                } else {
                    word = word + starredNamesRaw.charAt(j);
                }
            }

            starredNames = new String[arraylistWord.size()];
            for(int j = 0; j < arraylistWord.size(); j++) {
                starredNames[j] = arraylistWord.get(j);
            }
        }else{
            try{
               FileWriter fr = new FileWriter("flaggedTerms.txt",true);
               BufferedWriter br = new BufferedWriter(fr);
               System.out.println(setNumber);
               br.write(""+setNumber+"\n0\n,,,,,");
               br.close();
               fr.close();
               System.out.println("success");
            }catch(IOException e){
                System.out.println(e);
                JOptionPane.showMessageDialog(null,e, "WARNING", JOptionPane.WARNING_MESSAGE);
            }
        }
        
        //System.out.println((searchResults.get(rowNo).getNameOfSet()));
        new FlashcardSelector(this, currentUser, searchResults.get(rowNo), starredNames, starred);
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        dsc = new javax.swing.JRadioButton();
        dsu = new javax.swing.JRadioButton();
        TopicPicker2 = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        AscendingSort = new javax.swing.JRadioButton();
        DecendingSort = new javax.swing.JRadioButton();
        dateSortedLabel = new javax.swing.JLabel();
        sortUsername = new javax.swing.JRadioButton();
        sortSetname = new javax.swing.JRadioButton();
        Cancel4 = new javax.swing.JButton();
        CreateSet = new javax.swing.JButton();
        searchfield = new javax.swing.JTextField();
        searchbutton = new javax.swing.JButton();
        SearchTermDisplay = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        DisplayedResult = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        welcomeText = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        jPanel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel1.setText("Sort by Date:");

        dsc.setText("Date Set Created");
        dsc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dscActionPerformed(evt);
            }
        });

        dsu.setText("Date Set Updated");
        dsu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dsuActionPerformed(evt);
            }
        });

        TopicPicker2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        TopicPicker2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All", "Math", "Chinese", "Spanish", "English", "French", "German", "Biology", "Physics", "Chemistry", "DT", "CS", "Food and Nutrient", "Other" }));
        TopicPicker2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TopicPicker2ActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel2.setText("Filter Topic:");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel3.setText("Searching Field:");

        AscendingSort.setText("Oldest First");
        AscendingSort.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AscendingSortActionPerformed(evt);
            }
        });

        DecendingSort.setText("Newest First");
        DecendingSort.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DecendingSortActionPerformed(evt);
            }
        });

        dateSortedLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        dateSortedLabel.setText("Date sorted:");

        sortUsername.setText("Username");
        sortUsername.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sortUsernameActionPerformed(evt);
            }
        });

        sortSetname.setText("Setname");
        sortSetname.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sortSetnameActionPerformed(evt);
            }
        });

        Cancel4.setText("Cancel");
        Cancel4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Cancel4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(TopicPicker2, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(dateSortedLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 149, Short.MAX_VALUE)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(sortSetname)
                                    .addComponent(sortUsername)
                                    .addComponent(DecendingSort)
                                    .addComponent(AscendingSort)
                                    .addComponent(dsu)
                                    .addComponent(dsc))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(Cancel4, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(TopicPicker2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(sortSetname)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(sortUsername)
                .addGap(18, 18, 18)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(dsc)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(dsu)
                .addGap(18, 18, 18)
                .addComponent(dateSortedLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(AscendingSort)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(DecendingSort)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(Cancel4, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        CreateSet.setFont(new java.awt.Font("Nirmala UI", 1, 18)); // NOI18N
        CreateSet.setText("Create Set");
        CreateSet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CreateSetActionPerformed(evt);
            }
        });

        searchfield.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchfieldActionPerformed(evt);
            }
        });

        searchbutton.setText("Search");
        searchbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchbuttonActionPerformed(evt);
            }
        });

        SearchTermDisplay.setText("Showing Results for:");

        DisplayedResult.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }

        )
        {
            public boolean isCellEditable(int row, int column){
                return(false);
            }
        }

    );
    DisplayedResult.setToolTipText("");
    DisplayedResult.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            DisplayedResultMouseClicked(evt);
        }
    });
    jScrollPane1.setViewportView(DisplayedResult);

    jPanel2.setBackground(new java.awt.Color(0, 102, 255));
    jPanel2.setForeground(new java.awt.Color(240, 240, 240));

    welcomeText.setFont(new java.awt.Font("Nirmala UI", 1, 36)); // NOI18N
    welcomeText.setForeground(new java.awt.Color(255, 255, 255));
    welcomeText.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    welcomeText.setText("Search");

    javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
    jPanel2.setLayout(jPanel2Layout);
    jPanel2Layout.setHorizontalGroup(
        jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(welcomeText, javax.swing.GroupLayout.PREFERRED_SIZE, 729, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(115, 115, 115))
    );
    jPanel2Layout.setVerticalGroup(
        jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel2Layout.createSequentialGroup()
            .addGap(20, 20, 20)
            .addComponent(welcomeText)
            .addContainerGap(25, Short.MAX_VALUE))
    );

    jPanel5.setBackground(new java.awt.Color(0, 204, 255));

    javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
    jPanel5.setLayout(jPanel5Layout);
    jPanel5Layout.setHorizontalGroup(
        jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGap(0, 0, Short.MAX_VALUE)
    );
    jPanel5Layout.setVerticalGroup(
        jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGap(0, 13, Short.MAX_VALUE)
    );

    jPanel4.setBackground(new java.awt.Color(0, 0, 255));

    jButton1.setBackground(new java.awt.Color(255, 255, 255));
    jButton1.setFont(new java.awt.Font("Nirmala UI", 1, 14)); // NOI18N
    jButton1.setForeground(new java.awt.Color(255, 255, 255));
    jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ia/projects/help button.png"))); // NOI18N
    jButton1.setBorder(null);

    javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
    jPanel4.setLayout(jPanel4Layout);
    jPanel4Layout.setHorizontalGroup(
        jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel4Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    jPanel4Layout.setVerticalGroup(
        jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel4Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap(12, Short.MAX_VALUE))
    );

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(18, 18, 18)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(searchbutton, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                    .addGap(0, 0, 0)
                    .addComponent(searchfield, javax.swing.GroupLayout.PREFERRED_SIZE, 537, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(17, 17, 17)
                    .addComponent(CreateSet, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createSequentialGroup()
                    .addComponent(SearchTermDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE))
                .addComponent(jScrollPane1))
            .addContainerGap())
        .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );
    layout.setVerticalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(0, 0, 0)
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(0, 0, 0)
            .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(0, 0, 0)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createSequentialGroup()
                    .addGap(12, 12, 12)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(CreateSet, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(searchbutton, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(searchfield, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(SearchTermDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 576, Short.MAX_VALUE)
                    .addContainerGap())))
    );

    pack();
    }// </editor-fold>//GEN-END:initComponents

    private void CreateSetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CreateSetActionPerformed
        createSet cs = new createSet(currentUser,null,this);
        cs.setVisible(true);
    }//GEN-LAST:event_CreateSetActionPerformed

    private void searchbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchbuttonActionPerformed
        String searchTerm = searchfield.getText();
        searching(searchTerm);
        
    }//GEN-LAST:event_searchbuttonActionPerformed

    private void searchfieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchfieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchfieldActionPerformed

    private void dscActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dscActionPerformed
        dateSortedLabel.setVisible(true);
        DecendingSort.setVisible(true);
        AscendingSort.setVisible(true);
        if (dsc.isSelected()&& dsu.isSelected()){
            dsu.setSelected(false);
        }if (!dsc.isSelected() && !dsu.isSelected()){
            AscendingSort.setVisible(false); 
            dateSortedLabel.setVisible(false);
            DecendingSort.setVisible(false);
        }
    }//GEN-LAST:event_dscActionPerformed

    private void AscendingSortActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AscendingSortActionPerformed
        if (AscendingSort.isSelected()&& DecendingSort.isSelected()){
            DecendingSort.setSelected(false);
        }
    }//GEN-LAST:event_AscendingSortActionPerformed

    private void sortUsernameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sortUsernameActionPerformed
        if (sortUsername.isSelected()){
            sortSetname.setSelected(false);
        }else{
            sortSetname.setSelected(true);
        }
    }//GEN-LAST:event_sortUsernameActionPerformed

    private void dsuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dsuActionPerformed
        AscendingSort.setVisible(true); 
        dateSortedLabel.setVisible(true);
        DecendingSort.setVisible(true);
        if (dsc.isSelected()&& dsu.isSelected()){
            dsc.setSelected(false);
        }if (!dsc.isSelected()&& !dsu.isSelected()){
            AscendingSort.setVisible(false);  
            dateSortedLabel.setVisible(false);
            DecendingSort.setVisible(false);
        }
    }//GEN-LAST:event_dsuActionPerformed

    private void sortSetnameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sortSetnameActionPerformed
        if (sortSetname.isSelected()){
            sortUsername.setSelected(false);
        }else{
            sortUsername.setSelected(true);
        }
    }//GEN-LAST:event_sortSetnameActionPerformed

    private void DecendingSortActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DecendingSortActionPerformed
        if (AscendingSort.isSelected()&& DecendingSort.isSelected()){
            AscendingSort.setSelected(false);
        }
    }//GEN-LAST:event_DecendingSortActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        prevFrame.setVisible(true);
        prevFrame.readCSV();
        prevFrame.getSets();
        this.dispose();
    }//GEN-LAST:event_formWindowClosed

    private void TopicPicker2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TopicPicker2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TopicPicker2ActionPerformed

    private void DisplayedResultMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_DisplayedResultMouseClicked
        if (evt.getClickCount() == 2) {
            evt.consume();
            int column = 1;
            int row = DisplayedResult.getSelectedRow();
            String setNo = DisplayedResult.getModel().getValueAt(row, column).toString();
               System.out.println(row);

            openSet(Integer.parseInt(setNo),row);
        }
    }//GEN-LAST:event_DisplayedResultMouseClicked

    private void Cancel4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Cancel4ActionPerformed
        //returns to previous frame
        this.dispose();
        prevFrame.setVisible(true);
    }//GEN-LAST:event_Cancel4ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton AscendingSort;
    private javax.swing.JButton Cancel4;
    private javax.swing.JButton CreateSet;
    private javax.swing.JRadioButton DecendingSort;
    private javax.swing.JTable DisplayedResult;
    private javax.swing.JLabel SearchTermDisplay;
    private javax.swing.JComboBox<String> TopicPicker2;
    private javax.swing.JLabel dateSortedLabel;
    private javax.swing.JRadioButton dsc;
    private javax.swing.JRadioButton dsu;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton searchbutton;
    private javax.swing.JTextField searchfield;
    private javax.swing.JRadioButton sortSetname;
    private javax.swing.JRadioButton sortUsername;
    private javax.swing.JLabel welcomeText;
    // End of variables declaration//GEN-END:variables
}
