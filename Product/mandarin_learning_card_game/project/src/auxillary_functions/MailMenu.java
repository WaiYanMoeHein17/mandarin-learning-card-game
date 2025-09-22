
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

/**
 * MailMenu is a JFrame-based GUI component that displays the user's message inbox.
 * It shows a table of messages with their topics, senders, and dates, and allows users to:
 * - View their received messages
 * - Create new messages
 * - Handle special messages like password reset requests
 * - Navigate back to the main page
 *
 * The class maintains a list of MailSelector objects representing each message
 * and handles double-click events to open appropriate message viewer windows.
 */
public class MailMenu extends javax.swing.JFrame {
    
    /** List of all messages in the user's inbox */
    private ArrayList<MailSelector> inbox;
    
    /** Number of messages retrieved from database */
    private int results;
    
    /** Username of the currently logged-in user */
    private String currentUser;
    
    /** Reference to the main page for navigation */
    private MainPage prevFrame;

    /**
     * Creates a new MailMenu window to display the user's inbox.
     * 
     * @param currentUser The username of the currently logged-in user
     * @param mp The MainPage instance to return to when closing
     */
    public MailMenu(String currentUser, MainPage mp) {
        initComponents();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        
        this.currentUser = currentUser;
        prevFrame = mp;
        
        getAndMakeTable();     
    }
    
    /**
     * Retrieves messages from the database and populates the inbox table.
     * This method:
     * 1. Fetches all messages for the current user
     * 2. Creates MailSelector objects for each message
     * 3. Displays messages in reverse chronological order
     */
    public void getAndMakeTable() {
        inbox = new ArrayList<MailSelector>();
                
        try {
            Connection con = DBConnection.getConnection();
            String query = "SELECT * FROM `mail` WHERE `Recipient` = ?";
            
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, currentUser);
            
            ResultSet rs = ps.executeQuery();
            results = 0;
            
            while (rs.next()) {
                results++;
                
                MailSelector ms = new MailSelector(
                    rs.getInt("MailIndex"),
                    currentUser,
                    rs.getString("Sender"),
                    rs.getString("Topic"),
                    rs.getString("Message"),
                    rs.getString("DateSent"),
                    rs.getInt("ViewTimes"),
                    rs.getBoolean("Pinned")
                );

                inbox.add(ms);
            }
            
            rs.close();
            ps.close();
                                           
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error loading messages: " + e.getMessage(), 
                "WARNING", JOptionPane.WARNING_MESSAGE);
        }
          
        // Display newest messages first
        Collections.reverse(inbox);
        
        // Prepare table data
        DefaultTableModel model = (DefaultTableModel)this.DisplayedResult1.getModel();
        String[][] tableData = new String[results][3];
        
        for (int i = 0; i < inbox.size(); i++) {
            tableData[i] = new String[] {
                inbox.get(i).getTopic(),
                inbox.get(i).getSender(),
                inbox.get(i).getDateSent()
            };
        }

        String[] colNames = {"TOPIC", "SENDER", "DATE SENT"};
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

    /**
     * Handles double-click events on the inbox table.
     * Opens the appropriate message viewer based on the message type.
     */
    private void DisplayedResult1MouseClicked(java.awt.event.MouseEvent evt) {
        if (evt.getClickCount() == 2) {
            evt.consume();
            
            int row = DisplayedResult1.getSelectedRow();
            MailSelector ms = inbox.get(row);
            
            // Handle special messages (password reset, set verification)
            if (ms.getTopic().equals("RESET PASSWORD REQUEST: ACTION REQUIRED") ||
                ms.getTopic().equals("Set Verification Required: ActionRequired")) {
                new MailPasswordResetConfirm(
                    currentUser, this, ms.getMailIndex(),
                    ms.getSender(), ms.getTopic(),
                    ms.getViewTimes(), ms.isPinned(),
                    ms.getDateSent(), ms.getMessage()
                );
            } else {
                // Open standard message viewer
                new Mailview(
                    currentUser, this, ms.getMailIndex(),
                    ms.getSender(), ms.getTopic(),
                    ms.getViewTimes(), ms.isPinned(),
                    ms.getDateSent(), ms.getMessage()
                );
            }
            
            this.setVisible(false);
        }
    }

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
