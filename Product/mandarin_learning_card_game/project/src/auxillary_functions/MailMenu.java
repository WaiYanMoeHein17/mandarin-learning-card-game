
package auxillary_functions;

import main_page.MainPage;
import main_page.SetSelector;
import projects.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;


public class MailMenu extends javax.swing.JFrame {
    
    private ArrayList<MailSelector> inbox;
    private int results;
    private String currentUser;
    private MainPage prevFrame;

    public MailMenu(String currentUser, MainPage mp) {
        
        initComponents();
        
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        
        this.currentUser=currentUser;
        prevFrame = mp;
        
        getAndMakeTable();     
    }
    
    public void getAndMakeTable(){
        
        inbox = new ArrayList<MailSelector>();
                
        try{
            
            Connection con = DBConnection.getConnection();

            String query = "SELECT * FROM `mail` WHERE `Recipient`= '" + currentUser+ "'";

            PreparedStatement ps = con.prepareStatement(query);

            ResultSet rs = ps.executeQuery();

            results = 0;
            
            while(rs.next()){
                results = results + 1;
                
                int mi = rs.getInt("MailIndex");
                String sender = rs.getString("Sender");
                boolean p = rs.getBoolean("Pinned");
                int vt = rs.getInt("ViewTimes");
                String ds = rs.getString("DateSent");
                String m = rs.getString("Message");
                String t = rs.getString("Topic");

                MailSelector ms = new MailSelector(mi,currentUser, sender, t, m, ds, vt, p);

                inbox.add(ms);
            }                                           

        }catch(Exception e){
            JOptionPane.showMessageDialog(null,e, "WARNING", JOptionPane.WARNING_MESSAGE);
            System.out.println(e);
        }
          
        Collections.reverse(inbox);
        
        DefaultTableModel model = (DefaultTableModel)this.DisplayedResult1.getModel();
        
        String[][] tableData = new String[results][3];
        
        for (int i=0; i<inbox.size(); i++){
            //for (int j=0;j<6;j++){
                String[] neededData= new String[8];
                neededData[0]=inbox.get(i).gettopic();
                neededData[1]=inbox.get(i).getsender();
                neededData[2]=inbox.get(i).getdateSent();
                tableData[i]=neededData;
            //}
        }

        String[] colNames = {"TOPIC","SENDER","DATE SENT"};

        model.setDataVector(tableData, colNames); 
    }
    
    public MainPage getPrevPage(){
        return(prevFrame);
    }
        
        @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        DisplayedResult = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        welcomeText = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        DisplayedResult1 = new javax.swing.JTable();
        NewMail = new javax.swing.JButton();
        Cancel = new javax.swing.JButton();

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

    setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    addWindowListener(new java.awt.event.WindowAdapter() {
        public void windowClosed(java.awt.event.WindowEvent evt) {
            formWindowClosed(evt);
        }
    });

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

    javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
    jPanel4.setLayout(jPanel4Layout);
    jPanel4Layout.setHorizontalGroup(
        jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addGroup(jPanel4Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    jPanel4Layout.setVerticalGroup(
        jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(0, 0, 0))
    );

    DisplayedResult1.setModel(new javax.swing.table.DefaultTableModel(
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
    DisplayedResult1.setToolTipText("");
    DisplayedResult1.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            DisplayedResult1MouseClicked(evt);
        }
    });
    jScrollPane2.setViewportView(DisplayedResult1);

    NewMail.setText("Create Mail");
    NewMail.addActionListener(new java.awt.event.ActionListener() {
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

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 759, Short.MAX_VALUE)
        .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addGroup(layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(Cancel, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE))
                .addComponent(jScrollPane2))
            .addContainerGap())
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(263, Short.MAX_VALUE)
                .addComponent(NewMail, javax.swing.GroupLayout.PREFERRED_SIZE, 486, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap()))
    );
    layout.setVerticalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(0, 0, 0)
            .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(18, 18, 18)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 376, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(Cancel, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)
            .addContainerGap())
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(556, 556, 556)
                .addComponent(NewMail, javax.swing.GroupLayout.DEFAULT_SIZE, 62, Short.MAX_VALUE)
                .addContainerGap()))
    );

    pack();
    }// </editor-fold>//GEN-END:initComponents

    private void DisplayedResultMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_DisplayedResultMouseClicked
        if (evt.getClickCount() == 2) {
            System.out.println("DisplayedResultMouseClicked");
            evt.consume();
            int column = 1;
            int row = DisplayedResult.getSelectedRow();
            String setNo = DisplayedResult.getModel().getValueAt(row, column).toString();
            //System.out.println(row);

            //openSet(Integer.parseInt(setNo),row);
        }
    }//GEN-LAST:event_DisplayedResultMouseClicked

    private void DisplayedResult1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_DisplayedResult1MouseClicked
        //if double clicked
        if (evt.getClickCount() == 2) {
            //System.out.println("DisplayedResult1MouseClicked");
            //stop listening for new events 
            evt.consume();
            //get the row which was clicked
            int column = 1;
            int row = DisplayedResult1.getSelectedRow();
            //String mailNoStr = DisplayedResult1.getModel().getValueAt(row, column).toString();
            //int mailNo = Integer.parseInt(mailNoStr);
            
            //System.out.println(row);
           
            //get the mail which corrosponds to the clicked row
            MailSelector ms = inbox.get(row);
            //Food and NutrientAdmin
            
            if(ms.gettopic().equals("RESET PASSWORD REQUEST: ACTION REQUIRED")){
                System.out.println("SAME");
            }
            
            //if this row's message has a specialised topic
            if((ms.gettopic().equals("RESET PASSWORD REQUEST: ACTION REQUIRED"))||(ms.gettopic().equals("Set Verification Required: ActionRequired"))){
                //open special message viewer
                MailPasswordResetConfirm mprc = new MailPasswordResetConfirm(currentUser, this, ms.getmailIndex(),ms.getsender(),ms.gettopic(),ms.getviewTimes(),ms.getpinned(),ms.getdateSent(),ms.getmessage());  
                
            //otherwise   
            }else{
                //open default message viewer
                Mailview mv = new Mailview(currentUser, this, ms.getmailIndex(),ms.getsender(),ms.gettopic(),ms.getviewTimes(),ms.getpinned(),ms.getdateSent(),ms.getmessage());
            }
            
            //hide this set while the message viewer is visible
            this.setVisible(false);
            
        }
    }//GEN-LAST:event_DisplayedResult1MouseClicked

    private void CreateMailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CreateMailActionPerformed
        MailCreator mc = new MailCreator(this,currentUser);
        this.setVisible(false);
    }//GEN-LAST:event_CreateMailActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        prevFrame.setVisible(true);
    }//GEN-LAST:event_formWindowClosed

    private void CancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CancelActionPerformed
        this.dispose();
        prevFrame.setVisible(true);
    }//GEN-LAST:event_CancelActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton4ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Cancel;
    private javax.swing.JTable DisplayedResult;
    private javax.swing.JTable DisplayedResult1;
    private javax.swing.JButton NewMail;
    private javax.swing.JButton jButton4;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel welcomeText;
    // End of variables declaration//GEN-END:variables
}
