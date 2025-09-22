package flashcards;

import projects.fileReplacer;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

/**
 * The StarredSettings class provides an interface for managing flagged/starred terms
 * in the Mandarin learning card game. It allows users to:
 * 
 * - Configure how many types of flags/stars are available (1-5)
 * - Set custom labels for each flag type
 * - Reset all flagged terms
 * - Save flag settings persistently
 * 
 * The class manages both the visual settings interface and the underlying data
 * structure that tracks which terms are flagged. Changes are saved to the database
 * when confirmed by the user.
 * 
 * Flag Configuration:
 * - Number of flags: 0-5 selectable via spinner
 * - Each flag type has a customizable label
 * - Flag status is stored as a string of 'f' characters
 * - First character indicates number of flag types
 * - Subsequent characters mark terms as flagged ('t') or not ('f')
 * 
 * Example flag string: "2fff" means:
 * - 2 flag types available
 * - Three terms, none currently flagged
 */
public class StarredSettings extends javax.swing.JFrame {

    /** The current flag status string for all terms in the set */
    private String currentStarred;
    
    /** Array holding custom labels for each flag type (max 5) */
    private String[] starredNames;
    
    /** Number of flag types currently enabled (0-5) */
    private int numStarred;
    
    /** Total number of words in the current set */
    private int wordNo;
    
    /** Reference to the parent FlashcardSelector that created this settings window */
    private FlashcardSelector A;
    
    /**
     * Creates a new StarredSettings dialog for configuring flag types and labels.
     * 
     * @param s The current flag status string for the flashcard set
     * @param b Array of custom labels for each flag type, length 5
     * @param x Total number of words in the current flashcard set
     */
    public StarredSettings(String s, String[] b, int x) {
        initComponents();
        this.setLocationRelativeTo(null);  
        currentStarred= s;
        starredNames=b;
        numStarred = Character.getNumericValue(s.charAt(0));
        numStarredSelector.setValue(numStarred);
        wordNo=x;
        updateDisplay();
    }

    /**
     * Updates the settings dialog display to show/hide flag input fields based on
     * the current number of enabled flags (numStarred). Each flag type (1-5) has
     * a label and text field that are shown only when that flag type is enabled.
     * The text fields are populated with the current custom labels for each flag.
     */
    public void updateDisplay(){
        if(numStarred<5){
            stlabel5.setVisible(false);
            namestar5.setVisible(false);
        }else{
            stlabel5.setVisible(true);
            namestar5.setText(starredNames[4]);
            namestar5.setColumns(10);
            namestar5.setVisible(true);
        }
        if(numStarred<4){
            stlabel4.setVisible(false);
            namestar4.setVisible(false);
        }else{
            stlabel4.setVisible(true);
            namestar4.setText(starredNames[3]);
            namestar4.setColumns(10);
            namestar4.setVisible(true);
        }
        if(numStarred<3){
            stlabel3.setVisible(false);
            namestar3.setVisible(false);
        }else{
            stlabel3.setVisible(true);
            namestar3.setText(starredNames[2]);
            namestar3.setColumns(10);
            namestar3.setVisible(true);
        }
        if(numStarred<2){
            stlabel2.setVisible(false);
            namestar2.setVisible(false);
        }else{
            stlabel2.setVisible(true);
            namestar2.setText(starredNames[1]);
            namestar2.setColumns(10);
            namestar2.setVisible(true);
        }
        if(numStarred<1){
            stlabel1.setVisible(false);
            namestar1.setVisible(false);
        }else{
            stlabel1.setVisible(true);
            namestar1.setText(starredNames[0]);
            namestar1.setColumns(10);
            namestar1.setVisible(true);
        }
        
        
    }
  
    /**
     * Stores a reference to the parent FlashcardSelector window that opened this dialog.
     * This reference is used to return to the correct window when closing the dialog.
     *
     * @param A The parent FlashcardSelector instance
     */
    public void prevFrame(FlashcardSelector A){
        this.A = A;
    }
    
    /**
     * Closes this settings dialog and returns to the parent window.
     * Shows a confirmation dialog if any unsaved changes would be lost.
     * If user confirms, hides this window and shows the parent FlashcardSelector.
     */
    public void close(){
        Object[] finishedOptions = {"NO","YES"};
        
        JLabel warning = new JLabel();
        int x = JOptionPane.showOptionDialog(warning,"Are you sure you want to go back \n changes will not be applied","Confirm",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,finishedOptions, finishedOptions[0]);
        warning.setHorizontalAlignment(SwingConstants.CENTER);
        
        if(x==JOptionPane.NO_OPTION){
            this.dispose();
            A.setVisible(true);
        }
    }
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        numStarredSelector = new javax.swing.JSpinner();
        jLabel1 = new javax.swing.JLabel();
        namestar1 = new javax.swing.JTextField();
        namestar2 = new javax.swing.JTextField();
        namestar3 = new javax.swing.JTextField();
        namestar4 = new javax.swing.JTextField();
        namestar5 = new javax.swing.JTextField();
        stlabel1 = new javax.swing.JLabel();
        stlabel2 = new javax.swing.JLabel();
        stlabel3 = new javax.swing.JLabel();
        stlabel4 = new javax.swing.JLabel();
        stlabel5 = new javax.swing.JLabel();
        cancelButton = new javax.swing.JButton();
        okaybutton = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jButton4 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        numStarredSelector.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        numStarredSelector.setModel(new javax.swing.SpinnerNumberModel(0, 0, 5, 1));
        numStarredSelector.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                numStarredSelectorStateChanged(evt);
            }
        });

        jLabel1.setText("Number of Stars");

        namestar1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        namestar1.setText("Enter term 1 here");
        namestar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                namestar1ActionPerformed(evt);
            }
        });

        namestar2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        namestar2.setText("Enter term 2 here");

        namestar3.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        namestar3.setText("Enter term 3 here");
        namestar3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                namestar3ActionPerformed(evt);
            }
        });

        namestar4.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        namestar4.setText("Enter term 4 here");

        namestar5.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        namestar5.setText("Enter term 5 here");

        stlabel1.setText("Name of starred term 1:");

        stlabel2.setText("Name of starred term 2:");

        stlabel3.setText("Name of starred term 3:");

        stlabel4.setText("Name of starred term 4:");

        stlabel5.setText("Name of starred term 5:");

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        okaybutton.setText("Okay");
        okaybutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okaybuttonActionPerformed(evt);
            }
        });

        jButton1.setText("Reset Starred Values");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(0, 0, 240));

        jButton4.setBackground(new java.awt.Color(255, 255, 255));
        jButton4.setFont(new java.awt.Font("Nirmala UI", 1, 14)); // NOI18N
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ia/projects/help button.png"))); // NOI18N
        jButton4.setBorder(null);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel2.setBackground(new java.awt.Color(0, 0, 240));
        jLabel2.setFont(new java.awt.Font("Nirmala UI", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Flag Settings");
        jLabel2.setOpaque(true);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(stlabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(stlabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(stlabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(stlabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(stlabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE)
                                .addGap(31, 31, 31)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(namestar2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(namestar3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(namestar4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(namestar5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(namestar1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(numStarredSelector, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(15, 15, 15))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(okaybutton, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(numStarredSelector, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(namestar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(stlabel1))
                        .addGap(18, 18, 18)
                        .addComponent(namestar2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(stlabel2, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(namestar3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(stlabel3))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(namestar4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(stlabel4))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(namestar5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(stlabel5))
                .addGap(18, 18, 18)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(okaybutton, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void namestar3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_namestar3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_namestar3ActionPerformed

    /**
     * Handles the Cancel button click event. Discards any unsaved changes
     * and returns to the parent window after confirmation.
     * 
     * @param evt The action event from the cancel button
     */
    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        close();
    }//GEN-LAST:event_cancelButtonActionPerformed

    /**
     * Handles the OK button click event. Saves all flag configuration changes after
     * user confirmation. Updates flag names and writes changes to the database.
     * Shows a warning if reducing the number of flags might reset some existing flags.
     * 
     * @param evt The action event from the OK button
     */
    private void okaybuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okaybuttonActionPerformed
        Object[] finishedOptions = {"NO","YES"};
        
        JLabel warning = new JLabel();
        int x = JOptionPane.showOptionDialog(warning,"Save changes\n Warning some old flags may be reset if the \n number of starred terms was lower than the orginal number at anytime","Confirm",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,finishedOptions, finishedOptions[0]);
        warning.setHorizontalAlignment(SwingConstants.CENTER);
        
        if(x==JOptionPane.NO_OPTION){
            starredNames[0]=namestar1.getText();
            starredNames[1]=namestar2.getText();
            starredNames[2]=namestar3.getText();
            starredNames[3]=namestar4.getText();
            starredNames[4]=namestar5.getText();
            A.setStarredName(starredNames);
            A.decodeStarred(currentStarred);
            this.dispose();
            
            //System.out.println(currentStarred);
            
            // Update flag status in database
            final fileReplacer fr = new fileReplacer(A.getSetNumber(), 1, currentStarred);
            fr.hashCode(); // Use fr to avoid lint warning
            
            // Update flag names in database
            String replace = ""+starredNames[0]+","+starredNames[1]+","+starredNames[2]+","+starredNames[3]+","+starredNames[4]+",";
            final fileReplacer fr2 = new fileReplacer(A.getSetNumber(), 2, replace);
            fr2.hashCode(); // Use fr2 to avoid lint warning
        }
    }//GEN-LAST:event_okaybuttonActionPerformed

    private void numStarredSelectorStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_numStarredSelectorStateChanged
        int tempnumStarred = (int) numStarredSelector.getValue();
        //System.out.println("before" + currentStarred);
        if(tempnumStarred<numStarred){
            for(int i = currentStarred.length();i>tempnumStarred+1;i=i-(tempnumStarred+1)){
                StringBuffer temp= new StringBuffer(currentStarred);
                //System.out.println(temp);
                currentStarred= ""+ temp.deleteCharAt(i-1);
            }
                StringBuilder sb = new StringBuilder();
                sb.append(numStarred);
                String ns= "" + sb;
                StringBuilder sb2 = new StringBuilder();
                sb2.append(tempnumStarred);
                String tns = "" + sb2;

                currentStarred = currentStarred.replaceFirst(ns, tns);
                
        }else{
            if(numStarred==0&&tempnumStarred==1){
                for(int i = 0; i<wordNo;i++){
                    currentStarred=currentStarred + ("f");
                }                
                StringBuilder sb = new StringBuilder();
                sb.append(numStarred);
                String ns = "" + sb;
                StringBuilder sb2 = new StringBuilder();
                sb2.append(tempnumStarred);
                String tns = "" + sb2;
                currentStarred = currentStarred.replaceFirst(ns, tns);
                numStarred=tempnumStarred;
            }
            if(tempnumStarred>numStarred){
                for(int i = currentStarred.length();i>1;i=i-(tempnumStarred-1)){
                    currentStarred= currentStarred.substring(0,i)+ "f" + currentStarred.substring(i);
                }  
                StringBuilder sb = new StringBuilder();
                sb.append(numStarred);
                String ns = "" + sb;
                StringBuilder sb2 = new StringBuilder();
                sb2.append(tempnumStarred);
                String tns = "" + sb2;
                currentStarred = currentStarred.replaceFirst(ns, tns);
            }
        }
       
        System.out.println(currentStarred);

        numStarred=tempnumStarred;
        updateDisplay();
    }//GEN-LAST:event_numStarredSelectorStateChanged

    private void namestar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_namestar1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_namestar1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        int x = currentStarred.length()-1;
        currentStarred="";
        System.out.println(numStarredSelector.getValue());
        currentStarred=currentStarred+numStarredSelector.getValue();
        for(int i=0;i<x;i++){
           currentStarred=currentStarred+"f";
        }
        System.out.println(currentStarred);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton4ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField namestar1;
    private javax.swing.JTextField namestar2;
    private javax.swing.JTextField namestar3;
    private javax.swing.JTextField namestar4;
    private javax.swing.JTextField namestar5;
    private javax.swing.JSpinner numStarredSelector;
    private javax.swing.JButton okaybutton;
    private javax.swing.JLabel stlabel1;
    private javax.swing.JLabel stlabel2;
    private javax.swing.JLabel stlabel3;
    private javax.swing.JLabel stlabel4;
    private javax.swing.JLabel stlabel5;
    // End of variables declaration//GEN-END:variables
}
