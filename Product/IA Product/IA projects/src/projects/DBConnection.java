//learnt from (https://www.javatpoint.com/steps-to-connect-to-the-database-in-java);

package projects;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;

/**
 *
 * @author Uneve
 */
public class DBConnection {
    
        public static Connection getConnection(){
            Connection con = null;
            try{
                con = DriverManager.getConnection("jdbc:mysql://localhost/database","root","");
                //System.out.println("Succesful");
                
            //output error message if database connection fales
            }catch(Exception e){
                JOptionPane.showMessageDialog(null," Error recieving data \n Check your internet connection or try again later"
                        + " \n If error persists contact the following and provide the 'Error Message' if required."
                        + " \n Help Email: ******************** \n School Website: **************** \n Error Message: " + e, "WARNING",
                        JOptionPane.WARNING_MESSAGE);
            }
            return con;
        }
}
