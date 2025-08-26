
package auxillary_functions;

import projects.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.JOptionPane;

public class MailCreator extends javax.swing.JFrame {
    
    private MailMenu prevFrame;
    private Mailview prevframe;
    private boolean forward;
    private String currentUser;
    
    private LocalDate date = LocalDate.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    
    private String recipient;
    private String message;
    private String topic;
    private String dateCreated=date.format(formatter); 
    

    public MailCreator(MailMenu mm, String currentUser) {
        initComponents();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        
        prevFrame=mm;
        this.currentUser = currentUser;
        
        forward=false;
    }
    
    public MailCreator(Mailview mv, String currentUser, String topic, String recipient) {
        
        System.out.println(recipient);
        
        initComponents();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        
        prevframe=mv;
        this.currentUser = currentUser;
        
        forward=true;
        
        enterRecipient.setText(recipient);
        enterTopic.setText(topic);
    }
    
    private boolean checkUserExists(String user){
        
        boolean exists = false;
        
        try{
            
            Connection con = DBConnection.getConnection();

            String query = "SELECT * FROM `users` WHERE `Username`= '" + user + "'";
            
            //System.out.println(query);

            PreparedStatement ps = con.prepareStatement(query);

            ResultSet rs = ps.executeQuery();
            
            while(rs.next()){
                exists=true;
            }                        

        }catch(Exception e){
            JOptionPane.showMessageDialog(null,e, "WARNING", JOptionPane.WARNING_MESSAGE);
            System.out.println(e);
        }
        
        //System.out.println(exists);
        return(exists);
    }
        

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel5 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        welcomeText = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        welcomeText1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        enterRecipient = new javax.swing.JTextField();
        CreateMail = new javax.swing.JButton();
        Cancel = new javax.swing.JButton();
        EnterRecipientLabel = new javax.swing.JLabel();
        enterTopic = new javax.swing.JTextField();
        EnterMessageLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        messageInput = new javax.swing.JTextPane();
        MessageInputLabel = new javax.swing.JLabel();

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

        jPanel2.setBackground(new java.awt.Color(0, 102, 255));
        jPanel2.setForeground(new java.awt.Color(240, 240, 240));

        welcomeText.setFont(new java.awt.Font("Nirmala UI", 1, 36)); // NOI18N
        welcomeText.setForeground(new java.awt.Color(255, 255, 255));
        welcomeText.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        welcomeText.setText("Inbox");

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

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(47, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

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
            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 545, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

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

        CreateMail.setText("Send");
        CreateMail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CreateMailActionPerformed(evt);
            }
        });

        Cancel.setText("Cancel");
        Cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CancelActionPerformed(evt);
            }
        });

        EnterRecipientLabel.setText("To:");

        enterTopic.setText("Enter Message Topic");
        enterTopic.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                enterTopicMouseClicked(evt);
            }
        });

        EnterMessageLabel.setText("Topic:");

        jScrollPane1.setViewportView(messageInput);

        MessageInputLabel.setText("Message:");

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
                            .addComponent(EnterRecipientLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(EnterMessageLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(enterTopic)
                            .addComponent(enterRecipient)))
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(Cancel, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(CreateMail, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(MessageInputLabel)
                        .addGap(0, 0, Short.MAX_VALUE)))
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
                    .addComponent(enterTopic, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(EnterMessageLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(MessageInputLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Cancel, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(CreateMail, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
       
        //System.out.println(forward);
        
        if(forward==true){
            prevframe.setVisible(true);
        }else{
            prevFrame.setVisible(true);
        }
        
    }//GEN-LAST:event_formWindowClosed

    private void CreateMailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CreateMailActionPerformed
       
        recipient = enterRecipient.getText();
        topic = enterTopic.getText();
        message = messageInput.getText();
        
        //System.out.println(recipient);
        
        if(checkUserExists(recipient)==false){
            
            JOptionPane.showMessageDialog(null,"This username does not exist", "error", JOptionPane.WARNING_MESSAGE);
            
        }else{
            
            Connection con = DBConnection.getConnection();

            String query = "INSERT INTO `mail`(`Recipient`, `Sender`, `Topic`, `Message`, `DateSent`, `ViewTimes`, `Pinned`) VALUES ('"+ recipient +"','" + currentUser + "','" + topic + "','" + message + "','" + dateCreated + "','3','0')";

            try{

                Statement statement = con.createStatement();
                statement.executeUpdate(query);
                JOptionPane.showMessageDialog(null,"Mail Sent", "SUCCESS", JOptionPane.WARNING_MESSAGE);
                
                this.dispose();

            }catch(Exception e){
                
                JOptionPane.showMessageDialog(null,"Error: \n" + e, "WARNING", JOptionPane.WARNING_MESSAGE);
                System.out.println(e);
            }
        }
    
    }//GEN-LAST:event_CreateMailActionPerformed

    private void enterRecipientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_enterRecipientActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_enterRecipientActionPerformed

    private void enterRecipientMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_enterRecipientMouseClicked
        //System.out.println("ENUFweknfowNFoWONEf");
        if(enterRecipient.getText().equals("Enter Recipient Username")){
            enterRecipient.setText("");
        }
        
    }//GEN-LAST:event_enterRecipientMouseClicked

    private void enterTopicMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_enterTopicMouseClicked
        if(enterTopic.getText().equals("Enter Message Topic")){
            enterTopic.setText("");
        }
    }//GEN-LAST:event_enterTopicMouseClicked

    private void CancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CancelActionPerformed
        this.dispose();
    }//GEN-LAST:event_CancelActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Cancel;
    private javax.swing.JButton CreateMail;
    private javax.swing.JLabel EnterMessageLabel;
    private javax.swing.JLabel EnterRecipientLabel;
    private javax.swing.JLabel MessageInputLabel;
    private javax.swing.JTextField enterRecipient;
    private javax.swing.JTextField enterTopic;
    private javax.swing.JButton jButton1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextPane messageInput;
    private javax.swing.JLabel welcomeText;
    private javax.swing.JLabel welcomeText1;
    // End of variables declaration//GEN-END:variables
}
