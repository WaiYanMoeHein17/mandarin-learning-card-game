package flashcards;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * The FlashCards class provides the main user interface for displaying and
 * interacting with flashcards in the Mandarin learning game. It allows users
 * to view, flip, and navigate through their flashcard sets.
 * 
 * Key features:
 * - Display terms and definitions on an interactive card interface
 * - Flip cards to switch between terms and definitions
 * - Navigate forward and backward through the flashcard set
 * - View additional notes for each card
 * - Mark/flag specific terms for review
 * - Track progress through the current set
 * - Support keyboard shortcuts for common actions
 * 
 * Navigation controls:
 * - Space: Flip card
 * - Left/Right arrows: Previous/Next card
 * - Notes button: Toggle additional notes
 * - Progress bar: Shows completion percentage
 * 
 * The UI integrates with the DoublyLinkedList structure to manage card
 * data and navigation, and with FlashcardSelector for set management.
 */
public class FlashCards extends javax.swing.JFrame implements KeyListener {

    
    public FlashCards() {
        initComponents();
        this.setLocationRelativeTo(null);  
        PrevCard.setVisible(false);
    }
    
    /** The doubly-linked list containing all flashcards in the current set */
    private DoublyLinkedList DLL;
    
    /** Reference to the parent FlashcardSelector window */
    private FlashcardSelector A;
    
    /** Flag indicating whether terms (true) or definitions (false) are currently shown */
    private boolean termsVisible = true;
    
    /** The currently displayed card node in the linked list */
    private Node currentNode;
    
    /** Flag indicating whether additional notes are being displayed */
    private boolean notesShowing = false;
    
    /** Current position (1-based) in the flashcard set */
    private int currentIndex;
    
    /** Total number of terms/cards in the set */
    private int numberTerms;
    
    /** Number of star/flag markers per card */
    private int numberStarred;
    
    /** Current logged-in username for storing SRS data */
    private String currentUsername;
    
    /** Flag indicating whether the SRS panel is visible */
    private boolean srsVisible = false;
    
    /**
     * Sets up a reference to the parent FlashcardSelector window.
     * 
     * @param A The FlashcardSelector instance to link to
     */
    public void prevFrame(FlashcardSelector A) {
        this.A = A;
    }
    
    /**
     * Initializes the flashcard display with a new set of cards.
     * Sets up the first card and initializes counters for navigation.
     * 
     * @param DLL The doubly-linked list containing the flashcard set
     */
    public void setDLL(DoublyLinkedList DLL) {
        this.DLL = DLL;
        currentNode = DLL.getHead();
        currentNode.getData().getTerms();  
        flashcardDisplay.setText(currentNode.getData().getTerms());
        currentIndex = 1;
        numberTerms = DLL.count() - 1;
        updateProgress();
        
        // Hide SRS panel initially - only show after first flip
        srsPanel.setVisible(false);
        srsVisible = false;
    }
    
    /**
     * Sets the title displayed for the current flashcard set.
     * 
     * @param x The title string to display
     */
    public void setsetName(String x) {
        SetTitle.setText(x);
    }
    
    /**
     * Flips the current flashcard between showing terms and definitions.
     * Updates the display and termsVisible flag accordingly.
     */
    public void flipcard() {
        if (termsVisible) {
            flashcardDisplay.setText(currentNode.getData().getDefinitions());
            termsVisible = false;
            
            // Show SRS panel when flipping to definition (for rating)
            srsPanel.setVisible(true);
            srsVisible = true;
        } else {
            flashcardDisplay.setText(currentNode.getData().getTerms());
            termsVisible = true;
            
            // Hide SRS panel when flipping back to term
            srsPanel.setVisible(false);
            srsVisible = false;
        }
    }

    /**
     * Advances to the next card in the set. If at the end, prompts user
     * to either restart or return to selector screen. Updates display and
     * progress indicators.
     */
    /**
     * Sets the current username for SRS data storage.
     * 
     * @param username The logged-in username
     */
    public void setUsername(String username) {
        this.currentUsername = username;
        loadSrsData(); // Load SRS data for this user when username is set
    }
    
    /**
     * 
     * @param username The current user's username
     */
    public void setCurrentUsername(String username) {
        this.currentUsername = username;
    }
    
    /**
     * Processes a rating for the current card using the SRS algorithm.
     * Updates the card's SRS level and schedules next review date.
     * 
     * @param rating Performance rating (1-5) where 1 is worst and 5 is best
     */
    public void processCardRating(int rating) {
        // Only process ratings when SRS panel is visible
        if (srsVisible) {
            // Update the card's SRS level and get next review date
            Cards currentCard = currentNode.getData();
            String nextReviewDate = currentCard.updateSrsLevel(rating);
            
            // Store SRS data in the database
            storeSrsDataInDatabase(currentCard.getCardNumber(), rating, nextReviewDate);
            
            // Auto-advance to next card after rating
            nextCard();
        }
    }
    
    /**
     * Stores the SRS data in the database for persistence.
     * 
     * @param cardId The ID of the rated card
     * @param rating The user's performance rating (1-5)
     * @param nextReviewDate The calculated next review date
     */
    private void storeSrsDataInDatabase(int cardId, int rating, String nextReviewDate) {
        try {
            // Get database connection
            Connection con = projects.DBConnection.getConnection();
            
            // Check if SRS data already exists for this user and card
            String checkQuery = "SELECT * FROM srs_data WHERE username = ? AND card_id = ?";
            PreparedStatement checkStmt = con.prepareStatement(checkQuery);
            checkStmt.setString(1, currentUsername);
            checkStmt.setInt(2, cardId);
            ResultSet rs = checkStmt.executeQuery();
            
            if (rs.next()) {
                // Update existing SRS data
                String updateQuery = "UPDATE srs_data SET srs_level = ?, last_rating = ?, next_review_date = ? WHERE username = ? AND card_id = ?";
                PreparedStatement updateStmt = con.prepareStatement(updateQuery);
                updateStmt.setInt(1, currentNode.getData().getSrsLevel());
                updateStmt.setInt(2, rating);
                updateStmt.setString(3, nextReviewDate);
                updateStmt.setString(4, currentUsername);
                updateStmt.setInt(5, cardId);
                updateStmt.executeUpdate();
                updateStmt.close();
            } else {
                // Insert new SRS data
                String insertQuery = "INSERT INTO srs_data (username, card_id, srs_level, last_rating, next_review_date) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement insertStmt = con.prepareStatement(insertQuery);
                insertStmt.setString(1, currentUsername);
                insertStmt.setInt(2, cardId);
                insertStmt.setInt(3, currentNode.getData().getSrsLevel());
                insertStmt.setInt(4, rating);
                insertStmt.setString(5, nextReviewDate);
                insertStmt.executeUpdate();
                insertStmt.close();
            }
            
            rs.close();
            checkStmt.close();
            con.close();
        } catch (Exception e) {
            System.out.println("Error storing SRS data: " + e.getMessage());
            // Continue without error - SRS data is not critical to functionality
        }
    }
    
    public void nextCard() {
        // Hide SRS panel when moving to next card
        srsPanel.setVisible(false);
        srsVisible = false;
        
        if(currentNode.getNext()!=null){
        currentNode=currentNode.getNext();
        flashcardDisplay.setText(currentNode.getData().getTerms());
        termsVisible=true;
        currentIndex+=1;
        updateProgress();
        updateButtonSelected();
        }else{
            
            flagtermselector1.setVisible(false);
            flagtermselector2.setVisible(false);
            flagtermselector3.setVisible(false);
            flagtermselector4.setVisible(false);
            flagtermselector5.setVisible(false);
            lablel1.setVisible(false);
            lablel2.setVisible(false);
            lablel3.setVisible(false);
            lablel4.setVisible(false);
            lablel5.setVisible(false);
            
            currentIndex+=1;
            updateProgress();
            flashcardDisplay.setText("");
            
            Object[] finishedOptions = {"Restart set","Go back to selector screen"};
            int x = JOptionPane.showOptionDialog(this,"100% complete\nWhat do you want to do now?","Next Steps?",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,finishedOptions, finishedOptions[0]); 
            if(x==JOptionPane.YES_OPTION){
                currentNode=DLL.getHead();
                flashcardDisplay.setText(currentNode.getData().getTerms());
                currentIndex=1;
                termsVisible=true;
                updateProgress();
                updateButtonSelected();
            }else{
                A.setVisible(true);     
                this.dispose();
            }
        }
        
        if(currentIndex!=1){
            PrevCard.setVisible(true);
        }else{
            PrevCard.setVisible(false);
        }
        
    }
    
    /**
     * Moves to the previous card in the set. Updates the display,
     * progress indicators, and navigation button visibility.
     */
    public void prevCard() {
        currentNode = currentNode.getPrev();
        flashcardDisplay.setText(currentNode.getData().getTerms());
        termsVisible = true;
        currentIndex -= 1;
        updateProgress();
        updateButtonSelected();
        if (currentIndex != 1) {
            PrevCard.setVisible(true);
        } else {
            PrevCard.setVisible(false);
        }
    }
     
    /**
     * Updates the progress display and progress bar based on current position.
     * Shows current card number out of total cards and calculates percentage.
     */
    public void updateProgress() {
        String display = "";
        if (currentIndex <= numberTerms) {
            display = display + currentIndex + "\n  /\n     " + numberTerms;
        }
        Progress.setText(display);
        float percentage = (((float)(currentIndex - 1) / numberTerms) * 100);
        ProgressBar.setValue((int) percentage);
    }
    
    /**
     * Updates the state of starred/flagged term selectors based on
     * the current card's data. Each term can be individually flagged
     * for review.
     */
    public void updateButtonSelected() {
        
        char[] tempStarred = currentNode.getData().getStarred();     
        
        if(tempStarred.length!=0){
        
            if(tempStarred[0]=='t'){
                flagtermselector1.setSelected(true);
            }else{
                flagtermselector1.setSelected(false);
            }

            if(tempStarred[1]=='t'){
                flagtermselector2.setSelected(true);
            }else{
                flagtermselector2.setSelected(false);
            }

            if(tempStarred[2]=='t'){
                flagtermselector3.setSelected(true);
            }else{
                flagtermselector3.setSelected(false);
            }

            if(tempStarred[3]=='t'){
                flagtermselector4.setSelected(true);
            }else{
                flagtermselector4.setSelected(false);
            }

            if(tempStarred[4]=='t'){
                flagtermselector5.setSelected(true);
            }else{
                flagtermselector5.setSelected(false);
            }
        }else{
            
        }
    }
    
    /**
     * Updates the visibility of flag term selectors and their labels based on
     * the number of available terms. Shows only the relevant number of selectors.
     * 
     * @param x The number of terms to show selectors for (1-5)
     */
    public void updateButtons(int x) {
        // First make all buttons visible
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
        
        // Then hide buttons based on number of terms
        if (x < 5) {
            flagtermselector5.setVisible(false);
            lablel5.setVisible(false);
        }
        if (x < 4) {
            flagtermselector4.setVisible(false);
            lablel4.setVisible(false);
        }
        if (x < 3) {
            flagtermselector3.setVisible(false);
            lablel3.setVisible(false);
        }
        if (x < 2) {
            flagtermselector2.setVisible(false);
            lablel2.setVisible(false);
        }
        if (x < 1) {
            flagtermselector1.setVisible(false);
            lablel1.setVisible(false);
        }
    }
    
    /**
     * Sets the labels for each term selector button.
     * 
     * @param x Array of strings containing labels for each term selector (length 5)
     */
    public void setLabelName(String[] x) {
        lablel1.setText(x[0]);
        lablel2.setText(x[1]);
        lablel3.setText(x[2]);
        lablel4.setText(x[3]);
        lablel5.setText(x[4]);
    }
    
    /**
     * Sets the number of terms that can be starred/flagged per card.
     * 
     * @param x Number of terms that can be flagged
     */
    public void setNumberStarred(int x) {
        numberStarred = x;
    }
    
    /**
     * Note: This method is superseded by flashcardDisplayKeyPressed.
     * Keyboard events are handled in the text pane's key listener instead.
     */
    @Override
    public void keyPressed(KeyEvent e) {
        // Handled by flashcardDisplayKeyPressed
    }   

    /**
     * Not used in this implementation.
     * Keyboard events are handled in keyPressed.
     */
    @Override
    public void keyTyped(KeyEvent e) {
        // Not implemented
    }

    /**
     * Not used in this implementation.
     * Keyboard events are handled in keyPressed.
     */
    @Override
    public void keyReleased(KeyEvent e) {
        // Not implemented
    }
    
    
//Do not delete :
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        PrevCard = new javax.swing.JButton();
        FlipCard = new javax.swing.JButton();
        NextCard = new javax.swing.JButton();
        flagtermselector2 = new javax.swing.JRadioButton();
        flagtermselector4 = new javax.swing.JRadioButton();
        flagtermselector5 = new javax.swing.JRadioButton();
        flagtermselector3 = new javax.swing.JRadioButton();
        flagtermselector1 = new javax.swing.JRadioButton();
        jButton3 = new javax.swing.JButton();
        SetTitle = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        flashcardDisplay = new javax.swing.JTextPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        Progress = new javax.swing.JTextPane();
        lablel1 = new javax.swing.JLabel();
        lablel2 = new javax.swing.JLabel();
        lablel3 = new javax.swing.JLabel();
        lablel4 = new javax.swing.JLabel();
        lablel5 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        ProgressBar = new javax.swing.JProgressBar();
        jButton2 = new javax.swing.JButton();
        Cancel4 = new javax.swing.JButton();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        PrevCard.setFont(new java.awt.Font("Segoe UI Black", 1, 36)); // NOI18N
        PrevCard.setText("<");
        PrevCard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PrevCardActionPerformed(evt);
            }
        });

        FlipCard.setText("-");
        FlipCard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FlipCardActionPerformed(evt);
            }
        });

        NextCard.setFont(new java.awt.Font("Teko SemiBold", 1, 36)); // NOI18N
        NextCard.setText(">");
        NextCard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NextCardActionPerformed(evt);
            }
        });

        flagtermselector2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                flagtermselector2ActionPerformed(evt);
            }
        });

        flagtermselector4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                flagtermselector4ActionPerformed(evt);
            }
        });

        flagtermselector5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                flagtermselector5ActionPerformed(evt);
            }
        });

        flagtermselector3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                flagtermselector3ActionPerformed(evt);
            }
        });

        flagtermselector1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                flagtermselector1ActionPerformed(evt);
            }
        });

        jButton3.setText("Notes");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        SetTitle.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        SetTitle.setText("Set Name");

        flashcardDisplay.setEditable(false);
        flashcardDisplay.setFont(new java.awt.Font("Tahoma", 0, 48)); // NOI18N
        flashcardDisplay.setAutoscrolls(false);
        flashcardDisplay.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                flashcardDisplayKeyPressed(evt);
            }
        });
        jScrollPane2.setViewportView(flashcardDisplay);

        Progress.setEditable(false);
        Progress.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jScrollPane1.setViewportView(Progress);

        lablel1.setText("jLabel4");

        lablel2.setText("jLabel8");

        lablel3.setText("jLabel9");

        lablel4.setText("jLabel10");

        lablel5.setText("jLabel11");

        jLabel1.setText(" ");

        jPanel1.setBackground(new java.awt.Color(0, 0, 240));

        ProgressBar.setStringPainted(true);

        jButton2.setBackground(new java.awt.Color(255, 255, 255));
        jButton2.setFont(new java.awt.Font("Nirmala UI", 1, 14)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ia/projects/help button.png"))); // NOI18N
        jButton2.setBorder(null);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(ProgressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 660, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(113, 113, 113))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ProgressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10))
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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(SetTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane1)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Cancel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 662, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(7, 7, 7)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(flagtermselector2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(flagtermselector1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(flagtermselector3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(flagtermselector4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(flagtermselector5))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(7, 7, 7)
                                        .addComponent(lablel1, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(lablel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(lablel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(lablel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(lablel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addContainerGap(7, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(PrevCard, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(FlipCard, javax.swing.GroupLayout.PREFERRED_SIZE, 309, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(NextCard, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(174, 174, 174))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(SetTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(flagtermselector1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(flagtermselector2, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(flagtermselector3, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(flagtermselector4, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(flagtermselector5, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(8, 8, 8)
                                .addComponent(lablel1)
                                .addGap(18, 18, 18)
                                .addComponent(lablel2)
                                .addGap(18, 18, 18)
                                .addComponent(lablel3)
                                .addGap(16, 16, 16)
                                .addComponent(lablel4)
                                .addGap(15, 15, 15)
                                .addComponent(lablel5)))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel1)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(PrevCard, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(NextCard, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(FlipCard, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(Cancel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Handles clicks on the flip card button.
     * Toggles between showing terms and definitions.
     */
    private void FlipCardActionPerformed(java.awt.event.ActionEvent evt) {
        flipcard();
    }

    /**
     * Handles changes to the fifth term's star/flag status.
     * Updates the flashcard selector when this term is starred/unstarred.
     */
    private void flagtermselector5ActionPerformed(java.awt.event.ActionEvent evt) {
        int termIndex = (currentNode.getData().getCardNumber() - 1) * numberStarred + 5;
        A.changeStarred(termIndex);
    }

    /**
     * Handles changes to the third term's star/flag status.
     * Updates the flashcard selector when this term is starred/unstarred.
     */
    private void flagtermselector3ActionPerformed(java.awt.event.ActionEvent evt) {
        int termIndex = (currentNode.getData().getCardNumber() - 1) * numberStarred + 3;
        A.changeStarred(termIndex);
    }

    /**
     * Handles clicks on the notes button.
     * Toggles between showing the current card's notes and its terms/definitions.
     */
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {
        if (!notesShowing) {
            flashcardDisplay.setText(currentNode.getData().getNotes());
            notesShowing = true;
        } else {
            if (termsVisible) {
                flashcardDisplay.setText(currentNode.getData().getTerms());
            } else {
                flashcardDisplay.setText(currentNode.getData().getDefinitions());
            }
            notesShowing = false;
        }
    }

    /**
     * Handles window closing events.
     * Returns to the flashcard selector screen.
     */
    private void formWindowClosing(java.awt.event.WindowEvent evt) {
        A.setVisible(true);     
    }

    /**
     * Not used - keyboard events are handled in flashcardDisplayKeyPressed.
     */
    private void formKeyPressed(java.awt.event.KeyEvent evt) {
        // Handled by flashcardDisplayKeyPressed
    }

    /**
     * Handles keyboard navigation in the flashcard display:
     * - Space: Flip card
     * - Left Arrow: Previous card
     * - Right Arrow: Next card
     */
    private void flashcardDisplayKeyPressed(java.awt.event.KeyEvent evt) {
        if (evt.getKeyCode() == KeyEvent.VK_SPACE) {
            flipcard();
        }
        if (evt.getKeyCode() == KeyEvent.VK_LEFT) {
            prevCard();
        }
        if (evt.getKeyCode() == KeyEvent.VK_RIGHT) {
            nextCard();
        }
        
        // Add number key (1-5) handling for SRS ratings
        if (srsVisible) {
            if (evt.getKeyCode() == KeyEvent.VK_1) {
                processCardRating(1);
            } else if (evt.getKeyCode() == KeyEvent.VK_2) {
                processCardRating(2);
            } else if (evt.getKeyCode() == KeyEvent.VK_3) {
                processCardRating(3);
            } else if (evt.getKeyCode() == KeyEvent.VK_4) {
                processCardRating(4);
            } else if (evt.getKeyCode() == KeyEvent.VK_5) {
                processCardRating(5);
            }
        }
    }

    private void NextCardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NextCardActionPerformed
        nextCard();
    }//GEN-LAST:event_NextCardActionPerformed

    private void PrevCardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PrevCardActionPerformed
        prevCard();
    }//GEN-LAST:event_PrevCardActionPerformed

    private void flagtermselector1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_flagtermselector1ActionPerformed
        int x;
        x=(currentNode.getData().getCardNumber()-1)*numberStarred+1;
        if (flagtermselector1.isSelected()){
            A.changeStarred(x);
        }else{
            A.changeStarred(x);
        }
    }//GEN-LAST:event_flagtermselector1ActionPerformed

    private void flagtermselector2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_flagtermselector2ActionPerformed
        int x;
        x=(currentNode.getData().getCardNumber()-1)*numberStarred+2;
        if (flagtermselector2.isSelected()){
            A.changeStarred(x);
        }else{
            A.changeStarred(x);
        }
    }//GEN-LAST:event_flagtermselector2ActionPerformed

    private void flagtermselector4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_flagtermselector4ActionPerformed
        int x;
        x=(currentNode.getData().getCardNumber()-1)*numberStarred+4;
        if (flagtermselector4.isSelected()){
            A.changeStarred(x);
        }else{
            A.changeStarred(x);
        }
    }//GEN-LAST:event_flagtermselector4ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void Cancel4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Cancel4ActionPerformed
        //returns to previous frame
        this.dispose();
        A.setVisible(true);
    }//GEN-LAST:event_Cancel4ActionPerformed

    private void srsButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_srsButton1ActionPerformed
        updateSrsLevel(1);
        showNextCard();
    }//GEN-LAST:event_srsButton1ActionPerformed

    private void srsButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_srsButton2ActionPerformed
        updateSrsLevel(2);
        showNextCard();
    }//GEN-LAST:event_srsButton2ActionPerformed

    private void srsButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_srsButton3ActionPerformed
        updateSrsLevel(3);
        showNextCard();
    }//GEN-LAST:event_srsButton3ActionPerformed

    private void srsButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_srsButton4ActionPerformed
        updateSrsLevel(4);
        showNextCard();
    }//GEN-LAST:event_srsButton4ActionPerformed

    private void srsButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_srsButton5ActionPerformed
        updateSrsLevel(5);
        showNextCard();
    }//GEN-LAST:event_srsButton5ActionPerformed
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Cancel4;
    private javax.swing.JButton FlipCard;
    private javax.swing.JButton NextCard;
    private javax.swing.JButton PrevCard;
    private javax.swing.JTextPane Progress;
    private javax.swing.JProgressBar ProgressBar;
    private javax.swing.JLabel SetTitle;
    private javax.swing.JRadioButton flagtermselector1;
    private javax.swing.JRadioButton flagtermselector2;
    private javax.swing.JRadioButton flagtermselector3;
    private javax.swing.JRadioButton flagtermselector4;
    private javax.swing.JRadioButton flagtermselector5;
    private javax.swing.JTextPane flashcardDisplay;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lablel1;
    private javax.swing.JLabel lablel2;
    private javax.swing.JLabel lablel3;
    private javax.swing.JLabel lablel4;
    private javax.swing.JLabel lablel5;
    private javax.swing.JPanel srsPanel;
    private javax.swing.JButton srsButton1;
    private javax.swing.JButton srsButton2;
    private javax.swing.JButton srsButton3;
    private javax.swing.JButton srsButton4;
    private javax.swing.JButton srsButton5;
    // End of variables declaration//GEN-END:variables

    /**
     * Updates the SRS level for the current card based on the user rating.
     * 
     * @param rating User rating from 1-5 (1=forgot, 5=perfect recall)
     */
    private void updateSrsLevel(int rating) {
        Cards currentCard = currentNode.getData();
        int cardId = currentCard.getCardNumber();
        int currentSrsLevel = currentCard.getSrsLevel();
        int newSrsLevel;
        
        // Calculate new SRS level based on rating
        if (rating <= 2) {
            // If rating is 1-2 (poor recall), reset to level 1
            newSrsLevel = 1;
        } else {
            // For ratings 3-5, increase SRS level (max 5)
            newSrsLevel = Math.min(currentSrsLevel + 1, 5);
        }
        
        // Calculate next review date based on SRS level
        java.util.Date today = new java.util.Date();
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTime(today);
        
        // Get the interval in days based on SRS level
        int intervalDays = 1; // Default for new cards
        if (newSrsLevel > 0 && newSrsLevel <= 5) {
            // Use the static intervals from Cards class
            intervalDays = Cards.getSrsIntervals()[newSrsLevel - 1];
        }
        
        calendar.add(java.util.Calendar.DATE, intervalDays);
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
        String nextReviewDate = sdf.format(calendar.getTime());
        
        // Update the card object
        currentCard.setSrsLevel(newSrsLevel);
        currentCard.setNextReviewDate(nextReviewDate);
        
        // Save to database
        saveSrsData(cardId, newSrsLevel, rating, nextReviewDate);
    }
    
    /**
     * Saves the SRS data for the current card to the database.
     * 
     * @param cardId The card's unique ID
     * @param srsLevel The new SRS level (1-5)
     * @param rating The user's rating (1-5)
     * @param nextReviewDate The next review date (YYYY-MM-DD)
     */
    private void saveSrsData(int cardId, int srsLevel, int rating, String nextReviewDate) {
        if (currentUsername == null || currentUsername.isEmpty()) {
            System.out.println("Error: Username not set, cannot save SRS data");
            return;
        }
        
        try {
            // Get database connection from DBConnection class in projects package
            Connection conn = projects.DBConnection.getConnection();
            
            // SQL to insert or update SRS data
            String sql = "INSERT INTO srs_data (username, card_id, srs_level, last_rating, next_review_date) "
                       + "VALUES (?, ?, ?, ?, ?) "
                       + "ON DUPLICATE KEY UPDATE "
                       + "srs_level = ?, last_rating = ?, next_review_date = ?, "
                       + "last_reviewed_date = CURRENT_TIMESTAMP";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, currentUsername);
            pstmt.setInt(2, cardId);
            pstmt.setInt(3, srsLevel);
            pstmt.setInt(4, rating);
            pstmt.setString(5, nextReviewDate);
            pstmt.setInt(6, srsLevel);
            pstmt.setInt(7, rating);
            pstmt.setString(8, nextReviewDate);
            
            pstmt.executeUpdate();
            pstmt.close();
            conn.close();
            
        } catch (Exception e) {
            System.out.println("Error saving SRS data: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Loads the SRS data for all cards in the current set for the logged-in user.
     */
    private void loadSrsData() {
        if (currentUsername == null || currentUsername.isEmpty() || DLL == null) {
            return;
        }
        
        try {
            Connection conn = projects.DBConnection.getConnection();
            String sql = "SELECT card_id, srs_level, next_review_date FROM srs_data "
                       + "WHERE username = ?";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, currentUsername);
            
            ResultSet rs = pstmt.executeQuery();
            
            // Create a map of card IDs to SRS data for efficient lookup
            java.util.Map<Integer, int[]> srsLevels = new java.util.HashMap<>();
            java.util.Map<Integer, String> nextReviewDates = new java.util.HashMap<>();
            
            while (rs.next()) {
                int cardId = rs.getInt("card_id");
                int srsLevel = rs.getInt("srs_level");
                String nextReviewDate = rs.getString("next_review_date");
                
                srsLevels.put(cardId, new int[]{srsLevel});
                nextReviewDates.put(cardId, nextReviewDate);
            }
            
            rs.close();
            pstmt.close();
            conn.close();
            
            // Update the cards in the doubly linked list
            Node current = DLL.getHead();
            while (current != null) {
                Cards card = current.getData();
                int cardId = card.getCardNumber();
                
                if (srsLevels.containsKey(cardId)) {
                    card.setSrsLevel(srsLevels.get(cardId)[0]);
                    card.setNextReviewDate(nextReviewDates.get(cardId));
                }
                
                current = current.getNext();
            }
            
        } catch (Exception e) {
            System.out.println("Error loading SRS data: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Shows the next card and resets term/definition visibility.
     */
    /**
     * Updates the flag/star radio buttons based on the current card's starred status.
     */
    private void updateFlagRadioButtons() {
        char[] starredArray = currentNode.getData().getStarred();
        
        // Set each flag radio button based on corresponding starred array value
        flagtermselector1.setSelected(starredArray[0] == 't');
        flagtermselector2.setSelected(starredArray[1] == 't');
        flagtermselector3.setSelected(starredArray[2] == 't');
        flagtermselector4.setSelected(starredArray[3] == 't');
        flagtermselector5.setSelected(starredArray[4] == 't');
    }
    
    private void showNextCard() {
        if (currentNode.getNext() != null) {
            currentNode = currentNode.getNext();
            currentIndex++;
            flashcardDisplay.setText(currentNode.getData().getTerms());
            termsVisible = true;
            srsPanel.setVisible(false);
            srsVisible = false;
            updateProgress();
            
            // Update flag status
            updateFlagRadioButtons();
        } else {
            // At the end of the set
            int option = JOptionPane.showConfirmDialog(this, 
                "You've reached the end of this set. Would you like to restart?",
                "Set Complete", JOptionPane.YES_NO_OPTION);
                
            if (option == JOptionPane.YES_OPTION) {
                // Restart the set
                currentNode = DLL.getHead();
                currentIndex = 1;
                flashcardDisplay.setText(currentNode.getData().getTerms());
                termsVisible = true;
                srsPanel.setVisible(false);
                srsVisible = false;
                updateProgress();
                updateFlagRadioButtons();
            } else {
                // Return to flashcard selector
                this.dispose();
                A.setVisible(true);
            }
        }
    }


}
