
package auxillary_functions;

import flashcards.FlashcardSelector;
import main_page.SetSelector;
import projects.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

/**
 * MailPasswordResetConfirm is a specialized message viewer for handling password reset
 * and set verification requests. This class provides a GUI interface for:
 * 
 * 1. Password Reset Requests:
 *    - Viewing password reset request details
 *    - Accepting or declining password changes
 *    - Managing temporary password storage
 * 
 * 2. Set Verification Requests:
 *    - Reviewing sets pending verification
 *    - Approving or rejecting set verification
 *    - Viewing set contents before verification
 * 
 * The class includes security measures for password management and maintains
 * message state in the database.
 */
public class MailPasswordResetConfirm extends javax.swing.JFrame {
    
    /** Username of the currently logged-in user */
    private String currentUser;
    
    /** Reference to the previous MailMenu frame for navigation */
    private MailMenu prevFrame;

    /** Unique identifier for this message in the database */
    private int mailIndex;
    
    /** Username of the message sender */
    private String sender;
    
    /** Message topic/subject - used to determine message type */
    private String topic;
    
    /** Number of times the message has been viewed */
    private int viewTimes;
    
    /** Flag indicating if the message is pinned */
    private boolean pinned;
    
    /** Date when the message was sent */
    private String dateSent;
    
    /** Content of the message */
    private String message;

    /**
     * Creates a new message viewer for password reset or set verification requests.
     * Initializes the GUI and populates it with the message details.
     *
     * @param cu Current user's username
     * @param p Reference to previous MailMenu frame
     * @param mi Message index in database
     * @param s Sender's username
     * @param t Message topic
     * @param vt View count
     * @param pinned Whether message is pinned
     * @param dateSent Date message was sent
     * @param message Message content
     */
    public MailPasswordResetConfirm(String cu, MailMenu p, int mi, String s, String t, 
            int vt, boolean pinned, String dateSent, String message) {
        
        initComponents();
        this.setLocationRelativeTo(null);
        
        // Initialize message properties
        this.currentUser = cu;
        this.prevFrame = p;
        this.mailIndex = mi;
        this.sender = s;
        this.topic = t;
        this.viewTimes = vt;
        this.pinned = pinned;
        this.dateSent = dateSent;
        this.message = message;
        
        // Set up UI components
        welcomeText1.setText(topic);
        enterRecipient.setText(sender);
        dateSentDisplay.setText(dateSent);
        messageInput.setText(message);
        
        // Configure UI based on message type
        if (topic.equals("Set Verification Required: ActionRequired")) {
            AcceptPasswordChange.setText("Verify Set");
        } else {
            ViewSet.setVisible(false);
        }
        
        this.setVisible(true);
    }
    
    //method to delete this message
    /**
     * Deletes the current message from the database.
     * This is called when a message is no longer needed or has been processed.
     */
    private void delete() {
        try {
            Connection con = DBConnection.getConnection();
            String query = "DELETE FROM `mail` WHERE `MailIndex` = ?";
            
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, mailIndex);
            ps.executeUpdate();
            ps.close();
            
        } catch (Exception e) {
            String errorMsg = "The current message could not be deleted from the database.\n" +
                            "Try again, view help, or contact an admin if this issue persists.\n\n" +
                            "Error Code: " + e.getMessage();
            JOptionPane.showMessageDialog(null, errorMsg, "WARNING", JOptionPane.WARNING_MESSAGE);
        }
        
        this.dispose();
        prevFrame.getAndMakeTable();
    }
    
    /**
     * Updates the message's view count and pinned status in the database.
     * Called when message properties have changed.
     */
    private void updateInfo() {
        try {
            Connection con = DBConnection.getConnection();
            String query = "UPDATE `mail` SET `ViewTimes` = ?, `Pinned` = ? WHERE `MailIndex` = ?";
            
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, viewTimes);
            ps.setInt(2, pinned ? 1 : 0);
            ps.setInt(3, mailIndex);
            
            ps.executeUpdate();
            ps.close();
            
        } catch (Exception e) {
            String errorMsg = "Failed to update message properties.\n" +
                            "Error: " + e.getMessage();
            JOptionPane.showMessageDialog(null, errorMsg, "WARNING", JOptionPane.WARNING_MESSAGE);
        }
        
        delete();
        prevFrame.getAndMakeTable();
    }

    //method that resets password if boolean x is true, else reverting it to the old password
    /**
     * Updates the user's password based on a reset request.
     * The password field in the database contains both the old and new passwords,
     * separated by "///". This method either applies the new password or reverts
     * to the old one.
     *
     * @param applyNewPassword true to apply the new password, false to revert to old password
     */
    private void updatePassword(boolean applyNewPassword) {
        String password = null;
        String newPassword = null;
        
        try {
            // First, retrieve the current password field which contains both passwords
            Connection con = DBConnection.getConnection();
            String query = "SELECT `Password` FROM users WHERE Username = ?";
            
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, sender);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                password = rs.getString("password");
            }
            
            rs.close();
            ps.close();
            
            if (password != null) {
                // Extract either the new or old password based on the parameter
                int delimiterIndex = password.indexOf("///");
                if (delimiterIndex != -1) {
                    if (applyNewPassword) {
                        newPassword = password.substring(delimiterIndex + 3);
                    } else {
                        newPassword = password.substring(0, delimiterIndex);
                    }
                }
                
                // Update the password if we successfully extracted it
                if (newPassword != null) {
                    String updateQuery = "UPDATE `users` SET `Password` = ? WHERE `Username` = ?";
                    PreparedStatement updatePs = con.prepareStatement(updateQuery);
                    updatePs.setString(1, newPassword);
                    updatePs.setString(2, sender);
                    updatePs.executeUpdate();
                    updatePs.close();
                    
                    JOptionPane.showMessageDialog(null, 
                        "Password has been successfully updated", 
                        "Success", 
                        JOptionPane.INFORMATION_MESSAGE);
                    
                    viewTimes = 0;
                    this.dispose();
                }
            }
            
        } catch (Exception e) {
            String errorMsg = "Failed to update password.\n" +
                            "Please try again or contact an administrator.\n" +
                            "Error: " + e.getMessage();
            JOptionPane.showMessageDialog(null, errorMsg, 
                "Warning", JOptionPane.WARNING_MESSAGE);
        }
        
        delete();
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        enterRecipient = new javax.swing.JTextField();
        Decline = new javax.swing.JButton();
        EnterRecipientLabel = new javax.swing.JLabel();
        dateSentDisplay = new javax.swing.JTextField();
        EnterMessageLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        messageInput = new javax.swing.JTextPane();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        welcomeText1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        MessageInputLabel = new javax.swing.JLabel();
        Cancel1 = new javax.swing.JButton();
        Delete = new javax.swing.JButton();
        AcceptPasswordChange = new javax.swing.JButton();
        ViewSet = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        enterRecipient.setEditable(false);
        enterRecipient.setText("Enter Recipient Username");
        enterRecipient.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                enterRecipientMouseClicked(evt);
            }
        });
        enterRecipient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                enterRecipientActionPerformed(evt);
            }
        });

        Decline.setText("Decline Request");
        Decline.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DeclineActionPerformed(evt);
            }
        });

        EnterRecipientLabel.setText("From:");

        dateSentDisplay.setEditable(false);
        dateSentDisplay.setText("Enter Message Topic");
        dateSentDisplay.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dateSentDisplayMouseClicked(evt);
            }
        });

        EnterMessageLabel.setText("Date Sent:");

        messageInput.setEditable(false);
        jScrollPane1.setViewportView(messageInput);

        jPanel6.setBackground(new java.awt.Color(0, 204, 255));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 13, Short.MAX_VALUE)
        );

        jPanel7.setBackground(new java.awt.Color(0, 0, 255));

        jPanel3.setBackground(new java.awt.Color(0, 102, 255));
        jPanel3.setForeground(new java.awt.Color(240, 240, 240));

        welcomeText1.setFont(new java.awt.Font("Nirmala UI", 1, 24)); // NOI18N
        welcomeText1.setForeground(new java.awt.Color(255, 255, 255));
        welcomeText1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        welcomeText1.setText("Compose");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(welcomeText1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(welcomeText1)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        jButton1.setBackground(new java.awt.Color(255, 255, 255));
        jButton1.setFont(new java.awt.Font("Nirmala UI", 1, 14)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ia/projects/help button.png"))); // NOI18N
        jButton1.setBorder(null);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        MessageInputLabel.setText("Message:");

        Cancel1.setText("Cancel");
        Cancel1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Cancel1ActionPerformed(evt);
            }
        });

        Delete.setText("Delete");
        Delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DeleteActionPerformed(evt);
            }
        });

        AcceptPasswordChange.setText("Update Password");
        AcceptPasswordChange.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AcceptPasswordChangeActionPerformed(evt);
            }
        });

        ViewSet.setText("View Set");
        ViewSet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ViewSetActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(EnterRecipientLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(EnterMessageLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(enterRecipient, javax.swing.GroupLayout.DEFAULT_SIZE, 467, Short.MAX_VALUE)
                            .addComponent(dateSentDisplay)))
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(Cancel1, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(Decline, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(AcceptPasswordChange, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(MessageInputLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(ViewSet, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Delete, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(enterRecipient, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(EnterRecipientLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dateSentDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(EnterMessageLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(MessageInputLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Delete)
                    .addComponent(ViewSet))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Decline, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Cancel1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(AcceptPasswordChange, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void enterRecipientMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_enterRecipientMouseClicked
    }//GEN-LAST:event_enterRecipientMouseClicked

    private void enterRecipientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_enterRecipientActionPerformed
    }//GEN-LAST:event_enterRecipientActionPerformed

    private void DeclineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DeclineActionPerformed
        if(topic.equals("Set Verification Required: ActionRequired")){
            int setNo = Integer.parseInt(message.substring(message.indexOf("[")+1,message.indexOf("]")));
            String query = "UPDATE `setdata` SET `Verified`='0'WHERE `Set number` = '"+ setNo + "'";
                        
            try{
                Connection con = DBConnection.getConnection();
                Statement statement = con.createStatement();
                statement.executeUpdate(query);

                
            }catch(Exception e){
                JOptionPane.showMessageDialog(null,e, "WARNING", JOptionPane.WARNING_MESSAGE);
                System.out.println(e);
            }
            
            delete();
            
        }else{        
            updatePassword(false);
        }
        
        
    }//GEN-LAST:event_DeclineActionPerformed

    private void dateSentDisplayMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dateSentDisplayMouseClicked
    }//GEN-LAST:event_dateSentDisplayMouseClicked

    private void Cancel1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Cancel1ActionPerformed
        //returns to previous frame
        this.dispose();
        prevFrame.setVisible(true);
    }//GEN-LAST:event_Cancel1ActionPerformed

    private void DeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DeleteActionPerformed
        
        //Prompts the user to confirm deletion of the message
        //Button options
        Object[] finishedOptions = {"NO","YES"};
        
        //Create pop-up 
        JLabel warning = new JLabel();
        int x = JOptionPane.showOptionDialog(warning,"Are you sure you want to delete this message \n This action will be permenant","Confirm",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,finishedOptions, finishedOptions[0]);
        //set the location to center of screen
        warning.setHorizontalAlignment(SwingConstants.CENTER);
        
        //if yes is clicked
        if(x==JOptionPane.NO_OPTION){
            //delete this mail and close frame
            delete();
            viewTimes=-1;
            this.dispose();
        }
        
    }//GEN-LAST:event_DeleteActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
 
        if((viewTimes==0)){
            delete();         
        }
        
        updateInfo(); 
        
        prevFrame.setVisible(true);

    }//GEN-LAST:event_formWindowClosed

    private void AcceptPasswordChangeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AcceptPasswordChangeActionPerformed
        
        //if the topic
        if(topic.equals("Set Verification Required: ActionRequired")){
            
            int setNo = Integer.parseInt(message.substring(message.indexOf("[")+1,message.indexOf("]")));
            String query = "UPDATE `setdata` SET `Verified`='1'WHERE `Set number` = '"+ setNo + "'";
                        
            try{
                Connection con = DBConnection.getConnection();
                Statement statement = con.createStatement();
                statement.executeUpdate(query);

                
            }catch(Exception e){
                JOptionPane.showMessageDialog(null,e, "WARNING", JOptionPane.WARNING_MESSAGE);
                System.out.println(e);
            }
            
            this.dispose();
            
        }else{        
            updatePassword(true);
        }
    }//GEN-LAST:event_AcceptPasswordChangeActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * Opens the flashcard set viewer for set verification requests.
     * Extracts the set number from the message content and loads the set details.
     */
    private void ViewSetActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            Connection con = DBConnection.getConnection();
            String query = "SELECT * FROM `setdata` WHERE `Set number` = ?";
            
            // Extract set number from message content (format: [setNumber])
            int setNumber = Integer.parseInt(
                message.substring(message.indexOf("[") + 1, message.indexOf("]"))
            );
            
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, setNumber);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                // Parse table titles from comma-separated string
                String tableTitlesString = rs.getString("TableTitles");
                String[] tableTitles = parseTableTitles(tableTitlesString);
                
                // Create set selector with database results
                SetSelector ss = new SetSelector(
                    rs.getInt("Set number"),
                    rs.getString("Set Creator"),
                    rs.getBoolean("Verified"),
                    rs.getString("accessType"),
                    rs.getString("Password"),
                    tableTitles,
                    rs.getString("setData"),
                    rs.getString("Set Name"),
                    rs.getString("setNotes"),
                    rs.getString("SetTopic"),
                    rs.getString("DateCreated"),
                    rs.getString("DateUpdated")
                );
                
                // Open flashcard viewer with empty starred items
                String[] starredNames = new String[] {"", "", "", "", ""};
                new FlashcardSelector(
                    prevFrame.getPrevPage(),
                    "admin",
                    ss,
                    starredNames,
                    "0",
                    setNumber
                );
            }
            
            rs.close();
            ps.close();
            
        } catch (Exception e) {
            String errorMsg = "Failed to load set details.\n" +
                            "Error: " + e.getMessage();
            JOptionPane.showMessageDialog(null, errorMsg, 
                "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    /**
     * Helper method to parse comma-separated table titles into an array.
     */
    private String[] parseTableTitles(String titlesString) {
        int numColumns = 0;
        for (char c : titlesString.toCharArray()) {
            if (c == ',') numColumns++;
        }
        
        String[] titles = new String[numColumns];
        StringBuilder tempWord = new StringBuilder();
        int wordsInserted = 0;
        
        for (char c : titlesString.toCharArray()) {
            if (c != ',') {
                tempWord.append(c);
            } else {
                titles[wordsInserted++] = tempWord.toString();
                tempWord.setLength(0);
            }
        }
        
        return titles;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AcceptPasswordChange;
    private javax.swing.JButton Cancel1;
    private javax.swing.JButton Decline;
    private javax.swing.JButton Delete;
    private javax.swing.JLabel EnterMessageLabel;
    private javax.swing.JLabel EnterRecipientLabel;
    private javax.swing.JLabel MessageInputLabel;
    private javax.swing.JButton ViewSet;
    private javax.swing.JTextField dateSentDisplay;
    private javax.swing.JTextField enterRecipient;
    private javax.swing.JButton jButton1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextPane messageInput;
    private javax.swing.JLabel welcomeText1;
    // End of variables declaration//GEN-END:variables
}
