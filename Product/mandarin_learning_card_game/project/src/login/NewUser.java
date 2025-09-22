package login;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import projects.DBConnection;

/**
 * The NewUser class provides the account creation interface for the Mandarin Learning Card Game.
 * This dialog allows new users to register by providing:
 * - A unique username
 * - Their forename and surname
 * - A password (with confirmation)
 *
 * The class validates the input data and creates new user accounts in the database.
 * It performs checks for:
 * - Username uniqueness
 * - Password confirmation match
 * - Required field completion
 *
 * Upon successful registration, the user is added to the database with non-admin privileges
 * and returned to the login screen.
 */
public class NewUser extends javax.swing.JFrame {

    /** The username entered by the user (must be unique) */
    private String username;
    
    /** The user's first name */
    private String forename;
    
    /** The user's last name */
    private String surname;
    
    /** The password entered in the first password field */
    private String password;
    
    /** The password entered in the confirmation field (must match password) */
    private String passwordConfirm;
    
    /** Reference to the login screen that opened this dialog */
    private LoginScreen prevFrame;
          
    /**
     * Creates a new user registration dialog.
     * Initializes the UI components and centers the dialog on screen.
     * 
     * @param A The parent LoginScreen instance that created this dialog
     */
    public NewUser(LoginScreen A) {
        prevFrame = A;
        initComponents();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    /**
     * Adds a new user to the database with the provided information.
     * Creates a non-admin user account with the specified credentials.
     * 
     * @param u The username (must be unique)
     * @param f The user's forename
     * @param s The user's surname
     * @param p The user's password (stored as plain text - should be hashed in production)
     */
    public void addUser(String u, String f, String s, String p) {
        try {
            Connection con = DBConnection.getConnection();
            Statement statement = con.createStatement();
            String query = "INSERT INTO `users` (`Username`, `Forename`, `Surname`, `Password`, `Admin`) " +
                          "VALUES ('" + u + "', '" + f + "', '" + s + "', '" + p + "', '0')";
            statement.executeUpdate(query);
                //JOptionPane.showMessageDialog(null,"User Added", "Success", JOptionPane.WARNING_MESSAGE);
            
            //delte this frame and return to login page    
            prevFrame.setVisible(true);
            this.dispose();
        
        } catch (SQLException e) {
            //display error to user
            JOptionPane.showMessageDialog(null,"The user could not be added to the database. Try again, view help by clicking the help button or contact an admin if this issue persists. Please note down the error code in case an admin requires it. \n Error Code: " + e, "Error adding database", JOptionPane.WARNING_MESSAGE);
            //save error 
            System.out.println(e);
            e.printStackTrace();
           
                //log the error
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
        createAccount = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        passwordInput1 = new javax.swing.JPasswordField();
        jLabel3 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        usernameInput = new javax.swing.JTextField();
        surnameInput = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Nirmala UI", 1, 18)); // NOI18N
        jLabel2.setText("Username:");

        forenameInput.setText("Enter Forename Here");
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
        jLabel4.setText("Password:");

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

        createAccount.setFont(new java.awt.Font("Nirmala UI", 1, 14)); // NOI18N
        createAccount.setText("Create Account");
        createAccount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createAccountActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Nirmala UI", 1, 18)); // NOI18N
        jLabel5.setText("Reconfirm Password:");

        passwordInput1.setText("passwordInpu1");
        passwordInput1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                passwordInput1MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                passwordInput1MouseEntered(evt);
            }
        });
        passwordInput1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                passwordInput1ActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Nirmala UI", 1, 18)); // NOI18N
        jLabel3.setText("Surname:");

        jLabel6.setFont(new java.awt.Font("Nirmala UI", 1, 18)); // NOI18N
        jLabel6.setText("Forename:");

        usernameInput.setText("Enter Username Here");
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

        surnameInput.setText("Enter Surname Here");
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
        jLabel1.setText("Create Account");
        jLabel1.setOpaque(true);

        jButton3.setBackground(new java.awt.Color(255, 255, 255));
        jButton3.setFont(new java.awt.Font("Nirmala UI", 1, 14)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ia/projects/help button.png"))); // NOI18N
        jButton3.setBorder(null);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 316, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(49, 49, 49))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 11, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(Cancel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(createAccount, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(usernameInput, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(forenameInput, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 223, Short.MAX_VALUE)
                                    .addComponent(surnameInput, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 223, Short.MAX_VALUE)
                                    .addComponent(passwordInput1, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(passwordInput, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addGap(6, 6, 6)))))
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(usernameInput, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(13, 13, 13)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(forenameInput, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(surnameInput, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(passwordInput, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(passwordInput1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(Cancel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(createAccount, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void forenameInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_forenameInputActionPerformed
    }//GEN-LAST:event_forenameInputActionPerformed

    /**
     * Handles the Cancel button click event.
     * Returns to the login screen without creating an account.
     *
     * @param evt The action event from the cancel button
     */
    private void CancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CancelActionPerformed
        prevFrame.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_CancelActionPerformed

    /**
     * Handles the Create Account button click event.
     * Validates input and creates a new user account if all requirements are met:
     * - All fields must be filled out
     * - Passwords must match
     * - Username must be unique
     * After validation, shows a confirmation dialog before creating the account.
     *
     * @param evt The action event from the create account button
     */
    private void createAccountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createAccountActionPerformed
        username = usernameInput.getText();
        forename = forenameInput.getText();
        surname = surnameInput.getText();
        password = String.valueOf(passwordInput.getPassword());
        passwordConfirm = String.valueOf(passwordInput1.getPassword());

        if(password.equals(passwordConfirm)){
        //getDB values
        Connection con = DBConnection.getConnection();
        String query = "SELECT * FROM users WHERE Username=?";
            try{
                PreparedStatement ps = con.prepareStatement(query);
                ps.setString(1,username);
                ResultSet rs = ps.executeQuery();
                boolean unique = true;
                if(rs.next()){
                    unique=false;
                }else{            }

                if(unique==false){
                    JOptionPane.showMessageDialog(null,"This username is already in use. Pick a new one", "User Exists", JOptionPane.WARNING_MESSAGE);
                }else{

                    String s = "Are you sure you want to create a user called " + username + " with name " + forename + " " + surname;

                    int answer = JOptionPane.showConfirmDialog(this,s,"confirm",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);

                    if(answer==JOptionPane.YES_OPTION){

                        addUser(username,forename,surname,password);
                        JOptionPane.showMessageDialog(null,"Added new user " + username + " with name " + forename + " " + surname, "Success", JOptionPane.WARNING_MESSAGE);
                        this.dispose();
                    }
                }

            }catch(Exception e){
                JOptionPane.showMessageDialog(null,e, "WARNING", JOptionPane.WARNING_MESSAGE);
                System.out.println(e);
            }
        }else{
            JOptionPane.showMessageDialog(null,"Passwords do not match", "Input Error", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_createAccountActionPerformed

    private void usernameInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_usernameInputActionPerformed
    }//GEN-LAST:event_usernameInputActionPerformed

    private void surnameInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_surnameInputActionPerformed
    }//GEN-LAST:event_surnameInputActionPerformed

    /**
     * Handles the window close event (X button or system menu).
     * Returns to the login screen without creating an account.
     *
     * @param evt The window event from closing the form
     */
    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        prevFrame.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_formWindowClosed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    private void passwordInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_passwordInputActionPerformed
    }//GEN-LAST:event_passwordInputActionPerformed

    private void passwordInput1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_passwordInput1ActionPerformed
    }//GEN-LAST:event_passwordInput1ActionPerformed

    /**
     * Clears the default placeholder text from the username field on first click.
     *
     * @param evt The mouse event from clicking the username field
     */
    private void usernameInputMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_usernameInputMouseClicked
        if (usernameInput.getText().equals("Enter Username Here")) {
            usernameInput.setText("");
        }
    }//GEN-LAST:event_usernameInputMouseClicked

    /**
     * Clears the default placeholder text from the forename field on first click.
     *
     * @param evt The mouse event from clicking the forename field
     */
    private void forenameInputMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_forenameInputMouseClicked
        if (forenameInput.getText().equals("Enter Forename Here")) {
            forenameInput.setText("");
        }
    }//GEN-LAST:event_forenameInputMouseClicked

    /**
     * Clears the default placeholder text from the surname field on first click.
     *
     * @param evt The mouse event from clicking the surname field
     */
    private void surnameInputMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_surnameInputMouseClicked
        if (surnameInput.getText().equals("Enter Surname Here")) {
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

    private void passwordInput1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_passwordInput1MouseEntered
    }//GEN-LAST:event_passwordInput1MouseEntered

    private void passwordInput1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_passwordInput1MouseClicked
       if(String.valueOf(passwordInput1.getPassword()).equals("passwordInpu1")){
            passwordInput1.setText("");
        }
    }//GEN-LAST:event_passwordInput1MouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Cancel;
    private javax.swing.JButton createAccount;
    private javax.swing.JTextField forenameInput;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPasswordField passwordInput;
    private javax.swing.JPasswordField passwordInput1;
    private javax.swing.JTextField surnameInput;
    private javax.swing.JTextField usernameInput;
    // End of variables declaration//GEN-END:variables
}
