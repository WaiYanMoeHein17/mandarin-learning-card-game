
package auxillary_functions;

import projects.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

/**
 * Mailview provides a GUI for viewing and managing individual mail messages.
 * This class allows users to:
 * - View message content and metadata
 * - Forward messages to other users
 * - Pin/unpin important messages
 * - Delete messages
 * - Track message view counts
 * 
 * The class manages message state through a database connection and
 * automatically updates the parent inbox view when changes are made.
 */
public class Mailview extends javax.swing.JFrame {
    
    /** Username of the currently logged-in user */
    private String currentUser;
    
    /** Reference to the previous MailMenu frame */
    private MailMenu prevFrame;

    /** Unique identifier for the message in the database */
    private int mailIndex;
    
    /** Username of the message sender */
    private String sender;
    
    /** Message topic/subject */
    private String topic;
    
    /** Number of times this message has been viewed */
    private int viewTimes;
    
    /** Flag indicating if this message is pinned */
    private boolean pinned;
    
    /** Date when the message was sent */
    private String dateSent;
    
    /** Content of the message */
    private String message;

    /**
     * Creates a new message viewer window.
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
    public Mailview(String cu, MailMenu p, int mi, String s, String t, int vt, 
            boolean pinned, String dateSent, String message) {
        
        //create frame
        initComponents();
        
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        
        //store the parameters from the constructor
        currentUser=cu;
        prevFrame=p; 
        mailIndex=mi;
        sender=s;
        topic=t;
        viewTimes=vt;
        this.pinned=pinned;
        this.dateSent=dateSent;
        this.message=message;
        
        welcomeText1.setText(topic);
        enterRecipient.setText(sender);
        dateSentDisplay.setText(dateSent);
        messageInput.setText(message);
        
        //System.out.println(pinned);
        
        //update radio buttons accordingly
        if(pinned==true){
            isPinned.setSelected(true);
        }else{
            isPinned.setSelected(false);
        }
    }
    
    //method to delete this message
    private void delete(){
        
        //communication with database
        try{

            Connection con = DBConnection.getConnection();
            
            //create a query that deletes this message from database
            String query = "DELETE FROM `mail` WHERE `MailIndex`='" + mailIndex + "'";

            ///excute this query
            Statement statement = con.createStatement();
            statement.executeUpdate(query);
            //JOptionPane.showMessageDialog(null,"Mail Sent", "SUCCESS", JOptionPane.WARNING_MESSAGE);       

        //if not possible
        }catch(Exception e){
            
            //print errorcode
            JOptionPane.showMessageDialog(null,e, "WARNING", JOptionPane.WARNING_MESSAGE);
            System.out.println(e);
        }
        
        //update the inbox on previous page to account for this sets's changes
        prevFrame.getAndMakeTable();
    }
    
    //method to update the current message in the database
    
    private void updateInfo(){
        
        //communicate with database
        try{
            
            Connection con = DBConnection.getConnection();
           
            //find out if the mail has been pinned
            int pinnedInt;
            
            if(pinned){
                pinnedInt=1;
            }else{
                pinnedInt=0;
            }

            //create a query to update the saved viewTimes and pinned boolean in the databse
            String query = "UPDATE `mail` SET `ViewTimes`='" + viewTimes + "',`Pinned`='" + pinnedInt + "' WHERE `MailIndex`='" + mailIndex + "'";
            
            //execute query
            Statement statement = con.createStatement();
            statement.executeUpdate(query);
            //JOptionPane.showMessageDialog(null,"Mail Sent", "SUCCESS", JOptionPane.WARNING_MESSAGE);       

        //if error encountered 
        }catch(Exception e){
            
            //print error
            JOptionPane.showMessageDialog(null,e, "WARNING", JOptionPane.WARNING_MESSAGE);
            System.out.println(e);
        }
        
        //update the inbox on previous page to account for this sets's changes
        prevFrame.getAndMakeTable();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        enterRecipient = new javax.swing.JTextField();
        ForwardButton = new javax.swing.JButton();
        EnterRecipientLabel = new javax.swing.JLabel();
        dateSentDisplay = new javax.swing.JTextField();
        EnterMessageLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        messageInput = new javax.swing.JTextPane();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        welcomeText1 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        MessageInputLabel = new javax.swing.JLabel();
        Cancel1 = new javax.swing.JButton();
        isPinned = new javax.swing.JRadioButton();
        Delete = new javax.swing.JButton();

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

        ForwardButton.setText("Forward");
        ForwardButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ForwardButtonActionPerformed(evt);
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

        welcomeText1.setFont(new java.awt.Font("Nirmala UI", 1, 36)); // NOI18N
        welcomeText1.setForeground(new java.awt.Color(255, 255, 255));
        welcomeText1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        welcomeText1.setText("Compose");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(welcomeText1, javax.swing.GroupLayout.PREFERRED_SIZE, 534, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(310, 310, 310))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(welcomeText1)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        jButton3.setBackground(new java.awt.Color(255, 255, 255));
        jButton3.setFont(new java.awt.Font("Nirmala UI", 1, 14)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ia/projects/help button.png"))); // NOI18N
        jButton3.setBorder(null);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
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

        isPinned.setText("Pinned");
        isPinned.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                isPinnedActionPerformed(evt);
            }
        });

        Delete.setText("Delete");
        Delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DeleteActionPerformed(evt);
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
                        .addComponent(ForwardButton, javax.swing.GroupLayout.PREFERRED_SIZE, 369, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(MessageInputLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(isPinned)
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
                    .addComponent(isPinned)
                    .addComponent(Delete))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ForwardButton, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Cancel1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void enterRecipientMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_enterRecipientMouseClicked
    }//GEN-LAST:event_enterRecipientMouseClicked

    private void enterRecipientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_enterRecipientActionPerformed
    }//GEN-LAST:event_enterRecipientActionPerformed

    private void ForwardButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ForwardButtonActionPerformed
        
        //open the message creator with the the sender and the topic abeing passed as arguements into the to:,topic: parameters 
        //System.out.println(sender);
        MailCreator mc = new MailCreator(this,currentUser,topic,sender);

    }//GEN-LAST:event_ForwardButtonActionPerformed

    private void dateSentDisplayMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dateSentDisplayMouseClicked
    }//GEN-LAST:event_dateSentDisplayMouseClicked

    private void Cancel1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Cancel1ActionPerformed
        //returns to previous frame
        this.dispose();
        prevFrame.setVisible(true);
    }//GEN-LAST:event_Cancel1ActionPerformed

    private void isPinnedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_isPinnedActionPerformed
        
        //gets value of radiobutton whenever clicked
        if(isPinned.isSelected()){
            pinned=true;
        }else{
            pinned=false;
        }
        
        updateInfo();
    }//GEN-LAST:event_isPinnedActionPerformed

    private void DeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DeleteActionPerformed
        
        //Prompts the user to confirm deletion of the message
        Object[] finishedOptions = {"NO","YES"};
        
        JLabel warning = new JLabel();
        int x = JOptionPane.showOptionDialog(warning,"Are you sure you want to delete this message \n This action will be permenant","Confirm",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,finishedOptions, finishedOptions[0]);
        warning.setHorizontalAlignment(SwingConstants.CENTER);
        
        //yes is clicked
        if(x==JOptionPane.NO_OPTION){
            delete();
            viewTimes=-1;
            this.dispose();
        }
        
    }//GEN-LAST:event_DeleteActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        
        //only update the viewcount if the message is not pinned
        if(pinned==false){
            viewTimes=viewTimes-1;
        }
        
        //update this in the database incase change
        updateInfo();
        
        //System.out.println(viewTimes);
        
        //if the message has a viewTime of 0,it will be delete
        if((pinned==false)&&(viewTimes==0)){
            
            //Prompts the user to confirm deletion of the message or if they would like to pin the message instead to "save it"
            Object[] finishedOptions = {"NO","YES"};
        
            JLabel warning = new JLabel();
            int x = JOptionPane.showOptionDialog(warning,"Do you want to pin this message \n Elsewise this message will be permenantly delete","Confirm",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,finishedOptions, finishedOptions[0]);
            warning.setHorizontalAlignment(SwingConstants.CENTER);

            //if yes is clicked
            if(x==JOptionPane.NO_OPTION){
                
                //pin the message and increase the viewtimes to 1 (to ensure if unpinned in the future it will be able to meet the conditions in this outmost if loop
                viewTimes=viewTimes+1;
                pinned=true;
                
                //update this in the database
                updateInfo();
                
            }else{
                //if no is clicked delete set from database
                delete();
            }
        }
        
        prevFrame.setVisible(true);
        
    }//GEN-LAST:event_formWindowClosed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Cancel1;
    private javax.swing.JButton Delete;
    private javax.swing.JLabel EnterMessageLabel;
    private javax.swing.JLabel EnterRecipientLabel;
    private javax.swing.JButton ForwardButton;
    private javax.swing.JLabel MessageInputLabel;
    private javax.swing.JTextField dateSentDisplay;
    private javax.swing.JTextField enterRecipient;
    private javax.swing.JRadioButton isPinned;
    private javax.swing.JButton jButton3;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextPane messageInput;
    private javax.swing.JLabel welcomeText1;
    // End of variables declaration//GEN-END:variables
}
