
package login;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import main_page.MainPage;
import projects.DBConnection;
import java.awt.Color;
import javax.swing.JOptionPane;

/**
 * The LoginScreen class provides the main login interface for the Mandarin Learning Card Game.
 * It allows users to:
 * - Log in with existing username/password
 * - Create a new account
 * - Reset forgotten passwords
 * - Access help documentation
 * 
 * The class handles authentication against a database and manages navigation
 * to other screens like account creation, password reset, and the main application.
 * It uses a MySQL database through JDBC for user authentication.
 */
public class LoginScreen extends javax.swing.JFrame {

    /** The username of the currently logged in user */
    private String userID;
        
    /**
     * Creates a new LoginScreen window with initialized components.
     * Sets up the UI elements including:
     * - Username and password input fields
     * - Login and Create Account buttons
     * - Forgot Password link (styled as underlined blue text)
     * - Centers the window on screen
     */
    public LoginScreen() {
        initComponents();
        String forgetPassword = "<html><u>forgot password</u></html>";
        forgotPassword.setText(forgetPassword);
        forgotPassword.setForeground(new Color(26, 0, 255));
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    /**
     * Opens the main application window after successful login.
     * Creates a new MainPage instance with the current user's credentials
     * and disposes of the login window.
     */
    public void openMainPage() {
        new MainPage(this, userID); // Create and show main page
        this.dispose(); // Close login window
    }
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel3 = new javax.swing.JLabel();
        login = new javax.swing.JButton();
        passwordInput = new javax.swing.JPasswordField();
        jLabel2 = new javax.swing.JLabel();
        usernameInput = new javax.swing.JTextField();
        createAccount = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        forgotPassword = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();

        jLabel3.setText("jLabel3");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        login.setFont(new java.awt.Font("Nirmala UI", 1, 14)); // NOI18N
        login.setText("Login");
        login.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginActionPerformed(evt);
            }
        });

        passwordInput.setText("enterpassword12");
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

        jLabel2.setFont(new java.awt.Font("Nirmala UI", 1, 18)); // NOI18N
        jLabel2.setText("Username:");

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

        createAccount.setFont(new java.awt.Font("Nirmala UI", 1, 14)); // NOI18N
        createAccount.setText("Create Account");
        createAccount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createAccountActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Nirmala UI", 1, 18)); // NOI18N
        jLabel4.setText("Password:");

        forgotPassword.setText("forgot password");
        forgotPassword.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                forgotPasswordMouseClicked(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(0, 0, 240));
        jPanel1.setForeground(new java.awt.Color(0, 0, 240));

        jLabel1.setBackground(new java.awt.Color(0, 0, 240));
        jLabel1.setFont(new java.awt.Font("Nirmala UI", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Login");
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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(120, 120, 120)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 5, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(forgotPassword)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(passwordInput))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(usernameInput))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(createAccount, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(login, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(usernameInput, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(passwordInput, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2)
                .addComponent(forgotPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(createAccount, javax.swing.GroupLayout.DEFAULT_SIZE, 47, Short.MAX_VALUE)
                    .addComponent(login, javax.swing.GroupLayout.DEFAULT_SIZE, 47, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Handles the Login button click event. Validates the entered username/password
     * against the database and either:
     * - Opens the main application if credentials are valid
     * - Shows an error message if credentials are invalid
     * - Shows a database connection error if database is unreachable
     *
     * @param evt The action event from the login button
     */
    private void loginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginActionPerformed
        String username = usernameInput.getText();
        String password = String.valueOf(passwordInput.getPassword());
        
        Connection con = DBConnection.getConnection();
        String query = "SELECT * FROM users WHERE Username=? AND Password=?";
        
        try {
            
            PreparedStatement ps = con.prepareStatement(query);
            
            ps.setString(1,username);
            
            ps.setString(2,password);
            
            ResultSet rs = ps.executeQuery();
                                
            if(rs.next()){
                
                userID = rs.getString("Username");
                 
                //System.out.println(userID);
                
                this.setVisible(false);
                
                openMainPage();
                
                this.dispose();
                
            }else{
                
                JOptionPane.showMessageDialog(null,"Username or password is not correct", "ERROR", JOptionPane.WARNING_MESSAGE);
       
            }
            
            
        }catch(Exception e){
            
            //if(e.toString().equals("java.lang.IndexOutOfBoundsException: Index 0 out of bounds for length 0")){
                //System.out.println("ERROR OUT OF BOUNDS");
            //}else{
                JOptionPane.showMessageDialog(null,"Error Connecting to the database \n Error Code: " + e , "WARNING", JOptionPane.WARNING_MESSAGE);
                System.out.println(e);
            //}
        }
    
        
        
        
    }//GEN-LAST:event_loginActionPerformed

    /**
     * Handles the Create Account button click event.
     * Opens the new user registration window and hides the login screen.
     *
     * @param evt The action event from the create account button
     */
    private void createAccountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createAccountActionPerformed
        NewUser nu = new NewUser(this);
        nu.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_createAccountActionPerformed

    /**
     * Handles the username input field's action event.
     * Currently not used but preserved for future functionality.
     *
     * @param evt The action event from the username field
     */
    private void usernameInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_usernameInputActionPerformed
        // No action needed at this time
    }//GEN-LAST:event_usernameInputActionPerformed

    /**
     * Handles clicks on the "forgot password" link.
     * Opens the password reset window and changes the link color to indicate it was clicked.
     *
     * @param evt The mouse event from clicking the forgot password link
     */
    private void forgotPasswordMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_forgotPasswordMouseClicked
        new resetPassword(this); // Create and show password reset window
        this.setVisible(false);
        forgotPassword.setForeground(new Color(91, 16, 183));
    }//GEN-LAST:event_forgotPasswordMouseClicked

    /**
     * Handles clicks in the username input field.
     * Clears the default placeholder text when the field is first clicked.
     *
     * @param evt The mouse event from clicking the username field
     */
    private void usernameInputMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_usernameInputMouseClicked
        if (usernameInput.getText().equals("Enter Username here")) {
            usernameInput.setText("");
        }
    }//GEN-LAST:event_usernameInputMouseClicked

    /**
     * Handles clicks in the password input field.
     * Clears the default placeholder text when the field is first clicked.
     *
     * @param evt The mouse event from clicking the password field
     */
    private void passwordInputMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_passwordInputMouseClicked
        if (String.valueOf(passwordInput.getPassword()).equals("jPasswordField1")) {
            passwordInput.setText("");
        }
    }//GEN-LAST:event_passwordInputMouseClicked

    /**
     * Handles the password input field's action event.
     * Currently not used but preserved for future functionality.
     *
     * @param evt The action event from the password field
     */
    private void passwordInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_passwordInputActionPerformed
        // No action needed at this time
    }//GEN-LAST:event_passwordInputActionPerformed

    /**
     * Handles the Help button click event.
     * Displays a help message dialog with instructions for:
     * - Logging in
     * - Creating a new account
     * - Resetting a forgotten password 
     * - Getting additional support
     *
     * @param evt The action event from the help button
     */
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        JOptionPane.showMessageDialog(null,
            " Enter your username and password in the corrosponding boxes then click the 'Login' button. \n" +
            " If you need a new account press the 'Create Account' button. \n" +
            " To reset a password press 'Forgot Password' and follow ensuing instructions. \n" +
            " If the previous instructions do not help or there are other issues please contact the following \n" +
            " Help Email: ******************** \n" +
            " School Website: **************** ",
            "Help",
            JOptionPane.QUESTION_MESSAGE);
    }//GEN-LAST:event_jButton1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton createAccount;
    private javax.swing.JLabel forgotPassword;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton login;
    private javax.swing.JPasswordField passwordInput;
    private javax.swing.JTextField usernameInput;
    // End of variables declaration//GEN-END:variables
}
