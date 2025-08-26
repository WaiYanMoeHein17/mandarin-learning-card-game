package creator;

import AuxFunctions.search;
import Flashcards.FlashcardSelector;
import MainPage.MainPage;
import ia.projects.DBConnection;
import ia.projects.IAProjects;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

public class createSet extends javax.swing.JFrame {
    private LocalDate date = LocalDate.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private int setNo;
    private boolean update;
    private String creator;
    private String setName;
    private String access;
    private String topic = "All";
    private String password;
    private String dateCreated=date.format(formatter);
    private String dateUpdated=date.format(formatter); 
    private String[] columnNames={"term 1","term 2"}; 
    private String[] cNexpandable=new String[5];
    private int numColumns=2;
    private int numTerms=1;
    private String description;
    private DefaultTableModel model;
    private String[][] tableData;
    private IAProjects menuFrame;
    private FlashcardSelector setFrame;
    private String outputData;
    ArrayList<ArrayList<String>> terms2 = new ArrayList<>();
    //final private String[] accessOptions = { "Private", "Protected", "Public"};
    final private String[] topicOptions = {"Math","Chinese","Spanish","English","French","German","Biology","Physics","Chemisty","DT","CS","Food and Nutrient","Other"};
    private String setNotes;
    private boolean reset=false;
    private JTableHeader header;
    private MainPage prevframe;
    private search prevframesearch;
    private FlashcardSelector prevFrame;

    //Constructor for creating a new set
    public createSet(String creator, MainPage prevframe, search prevFrameSearch ) {
        
        //initialise the set Creator's UserInterface
        initComponents();    
        //center the new frame
        this.setLocationRelativeTo(null);   
        
        //set update set to false as we are creating a new set
        update=false;
        
        //store data needed to got to previous frame
        //(either prevframe or prevframesearch will be null, so the non-null frame will be opened when this frame is closed.
        this.creator=creator;
        this.prevframe=prevframe;
        prevframesearch=prevFrameSearch;
        
        //The ability to add passwords always off for new sets, therefore these components are invisible until specified by user
        PasswordEnter.setVisible(false);
        jLabel4.setVisible(false);
        
        //Create a new 2D array to store inputs. This 2d array is empty as this is the constructor for a new set
        tableData = new String[numTerms][numColumns];
        
        //As the set is empty/new, set the table's titles to the default 
        cNexpandable[0]="term 1";
        cNexpandable[1]="term 2";
        
        //Add the properties intitalised above to the table
        model = (DefaultTableModel)this.table.getModel();
        model.setDataVector(tableData, columnNames);
      
        //Code to update table titles
        //Basic functions understood from (https://stackoverflow.com/questions/13079777/editable-jtableheader/13089386) how has been modified for this project
        
        //get the header of our table and add a mouseListener to it
        header = table.getTableHeader();
        header.addMouseListener(new MouseAdapter(){

            //SettingPicker = new JComboBox(accessOptions);z
            //TopicPicker = new JComboBox(topicOptions);
        
        //tell the mouse listener to only run if double clicked    
        @Override
        public void mouseClicked(MouseEvent event)
        {
          if (event.getClickCount() == 2)
          {
            //call the edit table title method
            editColumnAt(event.getPoint());
            //System.out.println(event.getPoint().getX());
          }
        }
      });

    }
    
    //Constructor to update exsisiting set
    public createSet(int setNo, String creator,ArrayList<ArrayList<String>> terms2,String access, String setName, String topic, String password, String columnNames[], int numColumns,int numTerms, String setNotes,String dateCreated, FlashcardSelector prevFrame){
        
        //initialise the set Creator's UserInterface
        initComponents();    
        //centre the new frame
        this.setLocationRelativeTo(null);
        
        //Update the page name to an appropirate title
        createSetButton.setText("Update");
        
        //set update set to true, so sepific methods are only executed when the set is being updated
        update=true;
        
        //get the data for exisiting fields so we can populate the template with this data, allowing the user to edit it
        this.setNo = setNo;
        this.creator=creator;
        this.setName=setName;
        this.access=access;
        this.topic=topic;
        this.password=password; 
        this.numColumns=numColumns;
        this.setNotes=setNotes;
        Integer temp = numColumns;
        
        //set the column size adjuster to the size of current set, rather that the default 2
        numColSetter.setValue(temp);
        
        //
        this.columnNames = new String[numColumns];
        
        //cNexpandable[0]="term 1";
        //cNexpandable[1]="term 2";
        
        for(int i = 0;i<numColumns;i++){
                this.columnNames[i]=columnNames[i];
                cNexpandable[i]=columnNames[i];
        }
        
        this.terms2=terms2;
        this.numTerms=numTerms;
        //this.description=description;
        this.prevFrame=prevFrame;
        this.dateCreated=dateCreated;
                        
        String[][] tableData = new String[numTerms][numColumns];
        for (int i=0; i<numTerms; i++){
            for (int j=0;j<numColumns;j++){
                tableData[i][j]=terms2.get(j).get(i);
            }
        }     
        
        model = (DefaultTableModel)this.table.getModel();
        model.setDataVector(tableData, columnNames);
        Object [] row = new Object[5];
        
        if(access.equals("Protected")){
            PasswordEnter.setVisible(true);
            jLabel4.setVisible(true);
            PasswordEnter.setText(password);
        }else{
            PasswordEnter.setVisible(false);
            jLabel4.setVisible(false);
        }
        
        if(access.equals("Protected")){
           SettingPicker.setSelectedIndex(1);
        }
        
        if(access.equals("Public")){
           SettingPicker.setSelectedIndex(2);
        }
        
        setNameInput.setText(setName);
        SetNameDisplay.setText(setName);
        
        for(int i=0;i<topicOptions.length;i++){
            if(topic.equals(topicOptions[i])){
                TopicPicker.setSelectedIndex(i);
            }
        }
        
        SetDescriptionInput.setText(setNotes);
        description="Creator: " + creator + "\nNotes: "+ setNotes +"\nTopic: " + topic + "\nDateCreated: "+ dateCreated + "\nDateUpdated: "+dateUpdated;
        SetDescriptionDisplay.setText(description);
        
        header = table.getTableHeader();
        header.addMouseListener(new MouseAdapter(){
        @Override
        public void mouseClicked(MouseEvent event)
        {
          if (event.getClickCount() == 2)
          {
            editColumnAt(event.getPoint());
            //System.out.println(event.getPoint().getX());
          }
        }
      });
        
    }
    
    //Method from stack overflow (https://stackoverflow.com/questions/13079777/editable-jtableheader#) 
    
    private void editColumnAt(Point p)
  {
    TableColumn column;
    
    int columnIndex = header.columnAtPoint(p);
    //System.out.println(columnIndex);

    if (columnIndex != -1){
        column = header.getColumnModel().getColumn(columnIndex);
        //Rectangle columnRectangle = header.getHeaderRect(columnIndex);

        String s = (String)JOptionPane.showInputDialog (this,"Enter the name for the tite of this columns", "Enter column title:",JOptionPane.PLAIN_MESSAGE,null,null,null);

        if ((s != null) && (s.length() > 0)) {
                
            column.setHeaderValue(s);
            header.repaint();
            columnNames[columnIndex] = s;
            cNexpandable[columnIndex]= s;
            /*
            for(int i = 0;i<numColumns;i++){
                    columnNames=new String[numColumns];
                    columnNames[i]=cNexpandable[i];
                    System.out.println(columnNames[i]);
                } */
            
            
        }else{
            
        }
        
                
    }
  }    
    
    

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setNameInput = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        SetDescriptionDisplay = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        addRow = new javax.swing.JButton();
        numColSetter = new javax.swing.JSpinner();
        deleteRow = new javax.swing.JButton();
        TopicPicker = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        PasswordEnter = new javax.swing.JTextField();
        createSetButton = new javax.swing.JButton();
        Cancel = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        SettingPicker = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        SetDescription = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        SetDescriptionInput = new javax.swing.JTextArea();
        jPanel1 = new javax.swing.JPanel();
        SetNameDisplay = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        setNameInput.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        setNameInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setNameInputActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel1.setLabelFor(setNameInput);
        jLabel1.setText("Set Name:");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setLabelFor(SetDescriptionDisplay);
        jLabel2.setText("Set Description:");

        SetDescriptionDisplay.setEditable(false);
        SetDescriptionDisplay.setColumns(20);
        SetDescriptionDisplay.setRows(5);
        jScrollPane1.setViewportView(SetDescriptionDisplay);

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane2.setViewportView(table);

        addRow.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        addRow.setText("Add Row");
        addRow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addRowActionPerformed(evt);
            }
        });

        numColSetter.setFont(new java.awt.Font("Tahoma", 0, 64)); // NOI18N
        numColSetter.setModel(new javax.swing.SpinnerNumberModel(2, 2, 5, 1));
        numColSetter.setAlignmentX(1.0F);
        numColSetter.setAlignmentY(1.0F);
        numColSetter.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        numColSetter.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                numColSetterStateChanged(evt);
            }
        });

        deleteRow.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        deleteRow.setText("Delete Selected Row");
        deleteRow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteRowActionPerformed(evt);
            }
        });

        TopicPicker.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        TopicPicker.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Math", "Chinese", "Spanish", "English", "French", "German", "Biology", "Physics", "Chemistry", "DT", "CS", "Food and Nutrition", "Other" }));
        TopicPicker.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TopicPickerActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel3.setLabelFor(TopicPicker);
        jLabel3.setText("Topic:");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel4.setText("Set Password:");

        PasswordEnter.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        PasswordEnter.setText("Enter Password Here");
        PasswordEnter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PasswordEnterActionPerformed(evt);
            }
        });

        createSetButton.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        createSetButton.setText("Create Set");
        createSetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createSetButtonActionPerformed(evt);
            }
        });

        Cancel.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        Cancel.setText("Cancel");
        Cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CancelActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel5.setText("Access Settings:");

        SettingPicker.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        SettingPicker.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Private", "Protected", "Public" }));
        SettingPicker.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SettingPickerActionPerformed(evt);
            }
        });

        jLabel6.setText("(Press enter to update)");

        SetDescription.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        SetDescription.setLabelFor(setNameInput);
        SetDescription.setText("SetDescription");

        SetDescriptionInput.setColumns(20);
        SetDescriptionInput.setRows(5);
        SetDescriptionInput.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SetDescriptionInputKeyPressed(evt);
            }
        });
        jScrollPane4.setViewportView(SetDescriptionInput);

        jPanel1.setBackground(new java.awt.Color(0, 0, 240));

        SetNameDisplay.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        SetNameDisplay.setForeground(new java.awt.Color(255, 255, 255));
        SetNameDisplay.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        SetNameDisplay.setText("Set Name");

        jButton1.setBackground(new java.awt.Color(255, 255, 255));
        jButton1.setFont(new java.awt.Font("Nirmala UI", 1, 14)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ia/projects/help button.png"))); // NOI18N
        jButton1.setBorder(null);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(SetNameDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 915, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(59, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(SetNameDisplay)
                .addGap(25, 25, 25))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(Cancel, javax.swing.GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(createSetButton, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(63, 63, 63)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(TopicPicker, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(SettingPicker, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(PasswordEnter, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE))
                        .addGap(6, 6, 6))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                            .addComponent(setNameInput))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(SetDescription, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(17, 17, 17)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(7, 7, 7)))
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 507, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(deleteRow, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(addRow, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(numColSetter))
                .addContainerGap())
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(setNameInput, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(1, 1, 1)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(SetDescription, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(TopicPicker, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(SettingPicker, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(PasswordEnter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(createSetButton, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Cancel, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(numColSetter, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(addRow, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(deleteRow, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 505, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void setNameInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setNameInputActionPerformed
        SetNameDisplay.setText(setNameInput.getText());
    }//GEN-LAST:event_setNameInputActionPerformed

    private void PasswordEnterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PasswordEnterActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_PasswordEnterActionPerformed

    private void numColSetterStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_numColSetterStateChanged
      
        if((int) numColSetter.getValue()>numColumns){
            //for (int i=0; i<1; i++) {
                            
                //TableColumn add = new TableColumn();
                //String tempName = "term"+numColSetter.getValue();
                //add.setHeaderValue(tempName);
                //table.getColumnModel().addColumn(add);
                
                String tempName = "term"+numColSetter.getValue();
                model.addColumn(tempName); 
            //}

                numColumns = (int) numColSetter.getValue();
                //System.out.println(numColumns);
                cNexpandable[numColumns-1]="term"+numColSetter.getValue();
                columnNames=new String[numColumns];
                for(int i = 0; i<numColumns;i++){
                    columnNames[i]=cNexpandable[i];
                } 
            
        }
        if((int) numColSetter.getValue()<numColumns){
            model.setColumnCount((int)numColSetter.getValue());
            //table.removeColumn(table.getColumnModel().getColumn(numColumns-1));
            columnNames=new String[numColumns-1];
            for(int i = 0; i<numColumns-1;i++){
                    columnNames[i]=cNexpandable[i];
                } 
        }

        //System.out.print("columnNames[i]");
        for(int i=0;i<columnNames.length;i++){
            //System.out.print(columnNames[i]);
        }
        //System.out.println();

        //System.out.print("cNexpandable[i]");
        for(int i=0;i<cNexpandable.length;i++){
            //System.out.print(cNexpandable[i]);
        }
        //System.out.println();
        
        reset=true;
        numColumns = (int) numColSetter.getValue();
        //System.out.println(numColumns);
    }//GEN-LAST:event_numColSetterStateChanged

    private void addRowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addRowActionPerformed
        String tempArray[]=new String[numColumns];
        for(int i=0;i<tempArray.length;i++){
            tempArray[i]="";
        }
        model.addRow(tempArray);
        numTerms+=1;
    }//GEN-LAST:event_addRowActionPerformed

    private void deleteRowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteRowActionPerformed
        int deleterow = table.getSelectedRow();
        if (deleterow >= 0) {
            model.removeRow(deleterow);
            JOptionPane.showMessageDialog(null, "The row was deleted");
            numTerms-=1;
        } else {
            JOptionPane.showMessageDialog(null, "The row could not be deleted");
        }
    }//GEN-LAST:event_deleteRowActionPerformed

    private void TopicPickerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TopicPickerActionPerformed
        topic = (String)TopicPicker.getSelectedItem();
        description="Creator: " + creator + "\nNotes: "+ setNotes +"\nTopic: " + topic + "\nDateCreated: "+ dateCreated + "\nDateUpdated: "+dateUpdated;
        SetDescriptionDisplay.setText(description);
    }//GEN-LAST:event_TopicPickerActionPerformed

    private void SettingPickerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SettingPickerActionPerformed
        access = SettingPicker.getSelectedItem().toString();
        if(access.equals("Protected")){
            PasswordEnter.setVisible(true);
            jLabel4.setVisible(true);
        }else{
            PasswordEnter.setVisible(false);
            jLabel4.setVisible(false);
        }
    }//GEN-LAST:event_SettingPickerActionPerformed

    private void CancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CancelActionPerformed
        this.dispose();
        //setFrame.setVisible(true);
    }//GEN-LAST:event_CancelActionPerformed

    private void SetDescriptionInputKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SetDescriptionInputKeyPressed
        setNotes=(SetDescriptionInput.getText());
        description="Creator: " + creator + "\nNotes: "+ setNotes +"\nTopic: " + topic + "\nDateCreated: "+ dateCreated + "\nDateUpdated: "+dateUpdated;
        SetDescriptionDisplay.setText(description);
    }//GEN-LAST:event_SetDescriptionInputKeyPressed

    private void createSetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createSetButtonActionPerformed
        
        if(update==true){
            //System.out.println(numColumns + " column count");
            //for(int i = 0; i<numColumns;i++){
                //System.out.println(columnNames[i]);
            //}
            
            String [] tempColname = new String[numColumns];
            for(int i =0; i<numColumns;i++){
                tempColname[i]=(columnNames[i]);
                //System.out.println(columnNames[i]);
            }
            
            
            //System.out.println("c"+numColumns);
            //System.out.println("r"+numTerms);
            outputData="";
            outputData=outputData+numColumns;
            for(int i =0; i<numColumns;i++){
                for(int j=0;j<numTerms;j++){
                    //System.out.println(model.getValueAt(j,i));
                    if(model.getValueAt(j,i) == null){
                        outputData=outputData+" ,";
                    }else{
                     outputData=outputData+model.getValueAt(j,i)+",";
                    }
                }
            outputData=outputData+"/";
            }
            //System.out.println(outputData);
            /*for(int i =0; i<numColumns;i++){
                        System.out.print(tempColnum[i]);
            }*/
            prevFrame.EnterData(outputData, tempColname);
            prevFrame.printTerms();
            prevFrame.resetdisplayStarred();
            prevFrame.updateTable();
            prevFrame.setAccess(access);
            prevFrame.setsetName(setName);
            prevFrame.resetDisplay();
            prevFrame.setsetNotes(setNotes);
            prevFrame.setTopic(topic);
            
            if(access.equals("Protected")){
                prevFrame.setsetPassword(password);
            }else{
                prevFrame.setsetPassword("");
            }
            prevFrame.resetDisplay();
            prevFrame.resetDisplay();
            prevFrame.setsetNotes(setNotes);
            prevFrame.setDateCreated(dateCreated);
            prevFrame.setLastUpdated(dateUpdated);
            if(reset==true){
                prevFrame.decodeStarred("0");
            }
            
            
            //
            
            String tt="";
            
            for (int i =0; i<columnNames.length;i++){
                tt = tt + columnNames[i];
                tt = tt+ ",";
                //System.out.println(columnNames[i]);
            }
            String setName =setNameInput.getText();
                        
            String query = "UPDATE `setdata` SET `Verified`='0',`accessType`='"+access+"+',`Password`='"+password+"',`TableTitles`='"+tt+"',`SetData`='"+outputData+"',`Set Name`='"+ setName +"',`SetNotes`='"+setNotes+"',`SetTopic`='"+topic+"',`DateUpdated`='"+dateUpdated+"' WHERE `Set number`="+setNo+"";
            
            //System.out.println(query);
            
            try{
                Connection con = DBConnection.getConnection();
                Statement statement = con.createStatement();
                statement.executeUpdate(query);

                
            }catch(Exception e){
                JOptionPane.showMessageDialog(null,e, "WARNING", JOptionPane.WARNING_MESSAGE);
                System.out.println(e);
            }
            
            
            //
            
            if(prevFrame!=null){
                prevFrame.setVisible(true);
                this.dispose();
                
                prevFrame.changetitle(setNameInput.getText());
               
            }
            if(prevframesearch!=null){
                
                prevframesearch.setVisible(true);
                
                this.dispose();
            }
            
            
        }else{
            
                        
            String tt="";
            
            for (int i =0; i<columnNames.length;i++){
                tt = tt + columnNames[i];
                tt = tt+ ",";
                //System.out.println(columnNames[i]);
            }
            
            //System.out.println(tt);
            
            outputData="";
            outputData=outputData+numColumns;
            for(int i =0; i<numColumns;i++){
                for(int j=0;j<numTerms;j++){
                    //System.out.println(model.getValueAt(j,i));
                    if(model.getValueAt(j,i) == null){
                        outputData=outputData+" ,";
                    }else{
                     outputData=outputData+model.getValueAt(j,i)+",";
                    }
                }
            outputData=outputData+"/";
            }
            
            String setName =setNameInput.getText();
            
            String query = "INSERT INTO `setdata`(`Set Creator`, `Verified`, `accessType`, `Password`, `TableTitles`, `SetData`, `Set Name`, `SetNotes`, `SetTopic`, `DateCreated`, `DateUpdated`) VALUES ('"+creator+"','0','"+access+"','"+password+"','"+tt+"','"+outputData+"','"+setName+"','"+setNotes+"','"+topic+"','"+dateCreated+"','"+dateCreated+"')";
            System.out.println(query);
            try{
                Connection con = DBConnection.getConnection();
                Statement statement = con.createStatement();
                statement.executeUpdate(query);

                
            }catch(Exception e){
                JOptionPane.showMessageDialog(null,e, "WARNING", JOptionPane.WARNING_MESSAGE);
                System.out.println(e);
            }
            
            
            prevframe.getSets();
            
            //prevFrame.setVisible(true);

            //System.out.println("hi");
                                
            
                      
            
        }  
        String requestVerify = "";
        
        if(!topic.equals("All")){
            //System.out.println((String)TopicPicker.getSelectedItem()+"Admin");
            requestVerify =(String)TopicPicker.getSelectedItem()+"Admin";
        }
        
        String query = "SELECT `Set number` FROM `setdata` WHERE `Set Creator` = '" + creator +  "' AND `Set Name` = '" + setName +"'";
           
            //System.out.println(query);
            try{
                Connection con = DBConnection.getConnection();
                
                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();

                while(rs.next()){
                    int setNo = rs.getInt("Set number");
                }

                String query2 = "INSERT INTO `mail`(`Recipient`, `Sender`, `Topic`, `Message`, `DateSent`, `ViewTimes`, `Pinned`) VALUES ('" + requestVerify + "','" + creator + "','Set Verification Required: ActionRequired','ActionRequired: Verify Set\n" + creator +" has created setNumber  [" + setNo + "] \n\n Should the set be verified?','" + dateUpdated + "','1','0')";
           
                System.out.println("Query: "  +query2);
                System.out.println("Query: "  +creator);
                try{
                    Statement statement = con.createStatement();
                    statement.executeUpdate(query2);
               
                }catch(Exception e){
                    JOptionPane.showMessageDialog(null,e, "WARNING", JOptionPane.WARNING_MESSAGE);
                    System.out.println(e);
                }
                
            }catch(Exception e){
                JOptionPane.showMessageDialog(null,e, "WARNING", JOptionPane.WARNING_MESSAGE);
                System.out.println(e);
            }    
        
        
        
        this.dispose();
        
    }//GEN-LAST:event_createSetButtonActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
    }//GEN-LAST:event_formWindowClosing

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        if(update==true){
           prevFrame.setVisible(true);
        }else{
            
            if(prevframe!=null){
                prevframe.setVisible(true);
            }
            if(prevframesearch!=null){
                prevframesearch.setVisible(true);
            }
        }
        
        this.dispose();
         
    }//GEN-LAST:event_formWindowClosed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Cancel;
    private javax.swing.JTextField PasswordEnter;
    private javax.swing.JLabel SetDescription;
    private javax.swing.JTextArea SetDescriptionDisplay;
    private javax.swing.JTextArea SetDescriptionInput;
    private javax.swing.JLabel SetNameDisplay;
    private javax.swing.JComboBox<String> SettingPicker;
    private javax.swing.JComboBox<String> TopicPicker;
    private javax.swing.JButton addRow;
    private javax.swing.JButton createSetButton;
    private javax.swing.JButton deleteRow;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSpinner numColSetter;
    private javax.swing.JTextField setNameInput;
    private javax.swing.JTable table;
    // End of variables declaration//GEN-END:variables
}


