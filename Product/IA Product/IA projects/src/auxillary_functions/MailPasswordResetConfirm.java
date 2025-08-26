
package auxillary_functions;

//Imports
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



public class MailPasswordResetConfirm extends javax.swing.JFrame {
    
    //initiase variables
    //variables needed to navigate to other frames
    private String currentUser;
    private MailMenu prevFrame; 

    //set specific vairables
    private int mailIndex;
    private String sender;
    private String topic;
    private int viewTimes;
    private boolean pinned;
    private String dateSent;
    private String message;
    

    //constructor
    //take in the parameters from a single mail selector object
    public MailPasswordResetConfirm(String cu, MailMenu p, int mi,String s, String t, int vt, boolean pinned, String dateSent, String message) {
        
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
                
        if(topic.equals("Set Verification Required: ActionRequired")){
            AcceptPasswordChange.setText("Verify Set");
        }else{
            ViewSet.setVisible(false);
        }
        
        //System.out.println(pinned);
        

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
            
            //print error code
            JOptionPane.showMessageDialog(null,"The current message could not be deleted from the database.\n Try again view help by clicking the help button or contact an admin if this issue persists.\n\n Please note down the error code in case an admin requires it. \n Error Code: " + e, "WARNING", JOptionPane.WARNING_MESSAGE);
            System.out.println(e);
        }
        
        this.dispose();
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
        
        delete();
        
        //update the inbox on previous page to account for this sets's changes
        prevFrame.getAndMakeTable();
    }

    //method that resets password if boolean x is true, else reverting it to the old password
    private void updatePassword(boolean x){
        
            //initialse the current password and the new password as strings with no value
            String password = null;
            String newPassword = null;
        
            //create a connection to the database
            Connection con = DBConnection.getConnection();

            //create SQL query and save it to a string
            String query = "SELECT `Password` FROM users WHERE Username=?";

            //try do
            try{
                //create a statent which the SQL query is passed into
                PreparedStatement ps = con.prepareStatement(query);

                //pass the sender as tje argument for the first parameter for the SQL statement
                ps.setString(1,sender);

                //save returned rows to result set 
                ResultSet rs = ps.executeQuery();

                //as the sender is a unique user, therefore we need to ensure the password is present
                if(rs.next()){
                    
                    //if there are any result rows get the password feild and save it to a variable  
                    password = rs.getString("password");
                    
                }

            // if an error is encountered
            }catch(Exception e){
                
                //output error to users
                JOptionPane.showMessageDialog(null,"The current password could not be received from the database. Try again view help by clicking the help button or contact an admin if this issue persists. Please note down the error code in case an admin requires it. \n Error Code: " + e, "WARNING", JOptionPane.WARNING_MESSAGE);
                //print to output for testing/debugging purposes
                System.out.println(e);
            }
            
                //System.out.println("Password:" + password);
            
            //if password is found and reset parameter for this method is true
            if(x==true&&password!=null){
                
                    //System.out.println("reset=true");
                // set the new password to the string after the delimite "///"
                newPassword = password.substring(password.indexOf("///")+3);
                    //System.out.println("new password = " + newPassword);
                
            }else{
                //if a passowrd is fond and the reset parameter for this method is false
                if(password!=null){
                    
                        //System.out.println("reset=false");
                        //System.out.println(password.indexOf("///"));
                    //set new password to the string before the delimiter "///"
                    newPassword = password.substring(0,password.indexOf("///"));
                        //System.out.println("new password = " + newPassword);
                }
            }
        
        //Create a new SQL query for setting the password feild for the requesting user to the new password string
        String query2 = "UPDATE `users` SET `Password`='"+newPassword+"' WHERE `Username`= '" + sender +"'";

        //if the new password is not null
        if(newPassword!=null){
            
            //try to
            try{
                
                //create a new statement which will be executed to the database through connection con
                Statement statement = con.createStatement();
                //execute this query
                statement.executeUpdate(query2);
                //Display success message to the user
                JOptionPane.showMessageDialog(null,"Password has been successfully updated", "Success", JOptionPane.WARNING_MESSAGE);

                //set view times of this message to zero, so the message is deleted from the mail table in the database
                viewTimes=0;
                //return to the previous inbox frame 
                this.dispose();
                
            // if an error is encountered
            }catch(Exception e){
                //display error message to user
                JOptionPane.showMessageDialog(null,"The password could  be updated in the database. Try again, view help by clicking the help button or contact an admin if this issue persists. Please note down the error code in case an admin requires it. \n Error Code: " + e, "WARNING", JOptionPane.WARNING_MESSAGE);
                //print error message to output for debugging/testing
                System.out.println(e);
            }
            
        }else{
            JOptionPane.showMessageDialog(null,"The password could not be reset", "Error", JOptionPane.WARNING_MESSAGE);
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

    private void ViewSetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ViewSetActionPerformed
        
        Connection con = DBConnection.getConnection();
        
        String query = "SELECT `Set number`, `Set Creator`, `Verified`, `accessType`, `Password`, `TableTitles`, `SetData`, `Set Name`, `SetNotes`, `SetTopic`, `DateCreated`, `DateUpdated` FROM `setdata` WHERE `Set number`=?";

        try{

            PreparedStatement ps = con.prepareStatement(query);

            //System.out.println("i" + setNumbers.get(ci+i));

            ps.setInt(1,Integer.parseInt(message.substring(message.indexOf("[")+1,message.indexOf("]"))));


            ResultSet rs = ps.executeQuery();


            while(rs.next()){
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
                int setNo = rs.getInt("Set number");


                SetSelector ss = new SetSelector(setNumber,setCreator,Verified,accessType,password,tableTitles,setData,setName,setNotes,setTopics,dateCreated,dateUpdated);

                String starredNames[] = new String[] {"", "", "", "", "" };;

                String starred = "0";

                FlashcardSelector fcs = new FlashcardSelector(prevFrame.getPrevPage(),"admin",ss,starredNames,starred,setNo);

            }                                           

        }catch(Exception e){
            JOptionPane.showMessageDialog(null,e, "WARNING", JOptionPane.WARNING_MESSAGE);
            System.out.println(e);
        }

    }//GEN-LAST:event_ViewSetActionPerformed


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
