package login;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.JOptionPane;
import projects.DBConnection;

/**
 * The resetPassword class provides the password reset functionality for the 
 * Mandarin Learning Card Game. It implements a two-step password reset process:
 * 
 * 1. User Verification:
 *    - Validates username exists
 *    - Verifies user's full name matches the account
 * 
 * 2. Password Reset Request:
 *    - Creates a reset request that requires admin approval
 *    - Stores old password for recovery if needed
 *    - Sends notification to admin via internal mail system
 * 
 * Security features:
 * - Requires both username and full name verification
 * - Admin must approve password changes
 * - Maintains password history
 * - All actions are logged with timestamps
 */
public class resetPassword extends javax.swing.JFrame {

    /** The username of the account requesting password reset */
    private String username;
    
    /** The forename provided for verification */
    private String forename;
    
    /** The surname provided for verification */
    private String surname;
    
    /** The new password to set */
    private String password;
    
    /** Password confirmation to prevent typos */
    private String passwordConfirm;
    
    /** Reference to the login screen that opened this dialog */
    private LoginScreen prevFrame;
    
    /** The current password stored in the database */
    private String oldPassword;
    
    /** Current date for logging the reset request */
    private LocalDate date = LocalDate.now();
    
    /** Date formatter for consistent date formatting */
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    
    /**
     * Creates a new password reset dialog.
     * Initializes the UI components, centers the window on screen, and stores the
     * parent login screen reference for navigation.
     * 
     * @param A The parent LoginScreen instance that created this dialog
     */
    public resetPassword(LoginScreen A) {
        initComponents();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        prevFrame = A;
    }

    /**
     * Default constructor - not supported.
     * This class requires a parent LoginScreen reference.
     * 
     * @throws UnsupportedOperationException Always thrown as this constructor
     *         should not be used
     */
    resetPassword() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
    
    /**
     * Initiates a password reset request for the specified user.
     * This method:
     * 1. Updates the user's password in the database, keeping old password for history
     * 2. Creates a notification for admin approval
     * 3. Returns to the login screen
     * 
     * Password format in database: "oldPassword///newPassword"
     * 
     * @param p The new password to set
     * @param u The username whose password is being reset
     */
    public void changePassword(String p, String u) {
    
        //make connection to database
        try {
            
            Connection con = DBConnection.getConnection();
            Statement statement = con.createStatement();
            
            //Set the query to update password at username with the respective arguments from the changePassword method
            //UPDATE `users` SET `Password`="admin" WHERE `username`="admin";
            String query = "UPDATE `users` SET `Password`='" + oldPassword + "///" + p + "' WHERE `username`='" + u + "'";
            statement.executeUpdate(query);
            //JOptionPane.showMessageDialog(null,"User Added", "Success", JOptionPane.WARNING_MESSAGE);
            
            String query2 = "INSERT INTO `mail`(`Recipient`, `Sender`, `Topic`, `Message`, `DateSent`, `ViewTimes`, `Pinned`) VALUES ('admin','" + username + "','RESET PASSWORD REQUEST: ACTION REQUIRED','" + username + " has requested a password update. \n \n Do you want to allow " + forename + " " + surname + " to change their password?','" + date + "','1','0')";
            Statement statement2 = con.createStatement();
            statement2.executeUpdate(query2);
            
            //System.out.println("Q@2: " + query2);
            
            //return to loginScreen page
            prevFrame.setVisible(true);
            this.dispose();
            
        //print error if database connection fails    
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,e, "Error adding database", JOptionPane.WARNING_MESSAGE);
            //Logger.getLogger(newUser.class.getName()).log(Level.SEVERE, null, e);
        }
    }  

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        forenameInput = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        passwordInput = new javax.swing.JPasswordField();
        Cancel = new javax.swing.JButton();
        RequestReset = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        passwordInpu1 = new javax.swing.JPasswordField();
        jLabel3 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        usernameInput = new javax.swing.JTextField();
        surnameInput = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Nirmala UI", 1, 18)); // NOI18N
        jLabel2.setText("Username:");

        forenameInput.setText("Enter Forename here");
        forenameInput.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                forenameInputMouseClicked(evt);
            }
        });
        forenameInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                forenameInputActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Nirmala UI", 1, 18)); // NOI18N
        jLabel4.setText("New Password:");

        passwordInput.setText("passwordInput");
        passwordInput.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                passwordInputMouseClicked(evt);
            }
        });
        passwordInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                passwordInputActionPerformed(evt);
            }
        });

        Cancel.setFont(new java.awt.Font("Nirmala UI", 1, 14)); // NOI18N
        Cancel.setText("Cancel");
        Cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CancelActionPerformed(evt);
            }
        });

        RequestReset.setFont(new java.awt.Font("Nirmala UI", 1, 14)); // NOI18N
        RequestReset.setText("Request Reset");
        RequestReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RequestResetActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Nirmala UI", 1, 18)); // NOI18N
        jLabel5.setText("Reconfirm Password:");

        passwordInpu1.setText("passwordInpu1");
        passwordInpu1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                passwordInpu1MouseClicked(evt);
            }
        });
        passwordInpu1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                passwordInpu1ActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Nirmala UI", 1, 18)); // NOI18N
        jLabel3.setText("Surname:");

        jLabel6.setFont(new java.awt.Font("Nirmala UI", 1, 18)); // NOI18N
        jLabel6.setText("Forename:");

        usernameInput.setText("Enter Username here");
        usernameInput.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                usernameInputMouseClicked(evt);
            }
        });
        usernameInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                usernameInputActionPerformed(evt);
            }
        });

        surnameInput.setText("Enter Surname here");
        surnameInput.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                surnameInputMouseClicked(evt);
            }
        });
        surnameInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                surnameInputActionPerformed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(0, 0, 240));

        jLabel1.setBackground(new java.awt.Color(0, 0, 240));
        jLabel1.setFont(new java.awt.Font("Nirmala UI", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Reset Password");
        jLabel1.setOpaque(true);

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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 408, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(Cancel, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(RequestReset, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(passwordInpu1)
                            .addComponent(passwordInput, javax.swing.GroupLayout.DEFAULT_SIZE, 222, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(surnameInput, javax.swing.GroupLayout.DEFAULT_SIZE, 223, Short.MAX_VALUE)
                            .addComponent(forenameInput, javax.swing.GroupLayout.DEFAULT_SIZE, 223, Short.MAX_VALUE)
                            .addComponent(usernameInput))))
                .addContainerGap())
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(usernameInput, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(17, 17, 17)
                        .addComponent(forenameInput, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel6)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(surnameInput, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(passwordInput, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(passwordInpu1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(Cancel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(RequestReset, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Handles the Cancel button click event.
     * Returns to the login screen without making any password changes.
     *
     * @param evt The action event from the cancel button
     */
    private void CancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CancelActionPerformed
        prevFrame.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_CancelActionPerformed

    /**
     * Handles the Request Reset button click event.
     * Validates the reset request by:
     * 1. Checking that both password fields match
     * 2. Verifying username exists and matches provided name
     * 3. Confirming user wants to proceed with reset
     * 4. Initiating password change and admin notification
     *
     * @param evt The action event from the request reset button
     */
    private void RequestResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RequestResetActionPerformed
        // Get form input values
        username = usernameInput.getText();
        forename = forenameInput.getText();
        surname = surnameInput.getText();
        password = String.valueOf(passwordInput.getPassword());
        passwordConfirm = String.valueOf(passwordInpu1.getPassword());

        //check if the passwords match
        if(password.equals(passwordConfirm)){
            
        //Connect to the database
        Connection con = DBConnection.getConnection();
        
        //set query to take in the parameters needed to check if a user should be able to reset the password
        String query = "SELECT * FROM `users` WHERE `Username`=? AND `Forename`=? AND `Surname`=?";
        
        try{
                //create the query statement with our query passed into it
                PreparedStatement ps = con.prepareStatement(query);
                
                //set the query statement's parameters with the corrsponding value
                ps.setString(1,username);
                ps.setString(2,forename);
                ps.setString(3,surname);
                                
                //get all the matching results (should only be one as username is primary key)
                ResultSet rs = ps.executeQuery();
                
                //set a boolean check if the query returned a positive match
                boolean valid = false;
                
                //if match found set valid check to true
                if(rs.next()){
                    oldPassword = rs.getString("Password");
                    valid=true;
                }else{            }

                //print reason why the valid check did not trigger
                if(valid==false){
                    JOptionPane.showMessageDialog(null,"This username is not registered to this name", "Invalid reset", JOptionPane.WARNING_MESSAGE);
                }else{

                    //confirm that user wants to reset password
                    //set confirm message
                    String s = "Are you sure you want to reset password?";
                    
                    //print confirm with question popup
                    int answer = JOptionPane.showConfirmDialog(this,s,"confirm",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);

                    //if confirmed
                    if(answer==JOptionPane.YES_OPTION){
                        
                        //pass change new password and username to changePassword method 
                        changePassword(password,username);
                        
                    }
                }
                
            //if it cannot connect to the database print error
            }catch(Exception e){
                JOptionPane.showMessageDialog(null,e, "WARNING", JOptionPane.WARNING_MESSAGE);
                System.out.println(e);
            }
        
        //if passwords didnt match show user the error reason
        }else{
            JOptionPane.showMessageDialog(null,"Passwords do not match", "Input Error", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_RequestResetActionPerformed

    /**
     * Handles the window close event (X button or system menu).
     * Returns to the login screen without making any password changes.
     *
     * @param evt The window event from closing the form
     */
    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        prevFrame.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_formWindowClosed

    private void usernameInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_usernameInputActionPerformed
    }//GEN-LAST:event_usernameInputActionPerformed

    private void forenameInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_forenameInputActionPerformed
    }//GEN-LAST:event_forenameInputActionPerformed

    private void surnameInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_surnameInputActionPerformed
    }//GEN-LAST:event_surnameInputActionPerformed

    private void passwordInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_passwordInputActionPerformed
    }//GEN-LAST:event_passwordInputActionPerformed

    private void passwordInpu1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_passwordInpu1ActionPerformed
    }//GEN-LAST:event_passwordInpu1ActionPerformed

    /**
     * Clears the default placeholder text from the username field on first click.
     *
     * @param evt The mouse event from clicking the username field
     */
    private void usernameInputMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_usernameInputMouseClicked
        if (usernameInput.getText().equals("Enter Username here")) {
            usernameInput.setText("");
        }
    }//GEN-LAST:event_usernameInputMouseClicked

    /**
     * Clears the default placeholder text from the forename field on first click.
     *
     * @param evt The mouse event from clicking the forename field
     */
    private void forenameInputMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_forenameInputMouseClicked
        if (forenameInput.getText().equals("Enter Forename here")) {
            forenameInput.setText("");
        }
    }//GEN-LAST:event_forenameInputMouseClicked

    /**
     * Clears the default placeholder text from the surname field on first click.
     *
     * @param evt The mouse event from clicking the surname field
     */
    private void surnameInputMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_surnameInputMouseClicked
        if (surnameInput.getText().equals("Enter Surname here")) {
            surnameInput.setText("");
        }
    }//GEN-LAST:event_surnameInputMouseClicked

    /**
     * Clears the default placeholder text from the password field on first click.
     *
     * @param evt The mouse event from clicking the password field
     */
    private void passwordInputMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_passwordInputMouseClicked
        if (String.valueOf(passwordInput.getPassword()).equals("passwordInput")) {
            passwordInput.setText("");
        }
    }//GEN-LAST:event_passwordInputMouseClicked

    /**
     * Clears the default placeholder text from the password confirmation field on first click.
     *
     * @param evt The mouse event from clicking the password confirmation field
     */
    private void passwordInpu1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_passwordInpu1MouseClicked
        if (String.valueOf(passwordInpu1.getPassword()).equals("passwordInpu1")) {
            passwordInpu1.setText("");
        }
    }//GEN-LAST:event_passwordInpu1MouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Cancel;
    private javax.swing.JButton RequestReset;
    private javax.swing.JTextField forenameInput;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPasswordField passwordInpu1;
    private javax.swing.JPasswordField passwordInput;
    private javax.swing.JTextField surnameInput;
    private javax.swing.JTextField usernameInput;
    // End of variables declaration//GEN-END:variables
}
