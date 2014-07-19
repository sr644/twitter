package edu.njit.cs656.twitter.server.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AccessUtil {

   /*
    * ====================================================================
    * Constants
    * ====================================================================
    */
	private static final String DB_URL = "jdbc:mysql://localhost/twitter";
	private static final String DB_USER = "twitteruser";
	private static final String DB_PASSWORD = "twitteruserpw";
	     

   /*
    * ====================================================================
    * Member Variables
    * ====================================================================
    */
	private static final Logger LOGGER = Logger.getLogger(AccessUtil.class.getName());
	
    
   /*
    * ====================================================================
    * Constructors
    * ====================================================================
    */

	

   /*
    * ====================================================================
    * Public Methods
    * ====================================================================
    */
		
	static {        
        try {
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());            
            LOGGER.log(Level.INFO, "MySql Driver Registered===============");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
	
	public static Connection getMySqlConnection() throws SQLException {
		return DriverManager.getConnection("jdbc:mysql://localhost/twitter", "twitteruser", "twitteruserpw");
    }


   /*
    * ====================================================================
    * Protected Methods
    * ====================================================================
    */



   /*
    * ====================================================================
    * Private Methods
    * ====================================================================
    */
	
	
    
   /*
    * ====================================================================
    * Inner classes
    * ====================================================================
    */
}
