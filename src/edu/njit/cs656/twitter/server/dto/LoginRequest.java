package edu.njit.cs656.twitter.server.dto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.njit.cs656.twitter.server.util.AccessUtil;
import edu.njit.cs656.twitter.server.util.StringUtil;

public class LoginRequest extends Request {
	
   /*
    * ====================================================================
    * Constants
    * ====================================================================
    */
	private static final Logger LOGGER = Logger.getLogger(LoginRequest.class.getName());
	private static final String TAG = LoginRequest.class.getSimpleName();

	
   /*
    * ====================================================================
    * Member Variables
    * ====================================================================
    */
	private String username;
	private String password;
	
    
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
	public String getUsername() {return username;}
	public void setUsername(String username) {this.username = username;}
	public String getPassword() {return password;}
	public void setPassword(String password) {this.password = password;}

	
	public LoginResponse validate() {
		LoginResponse loginResponse = null;
		
		loginResponse = validateUsername();
		if(!loginResponse.isSuccess()){return loginResponse;} else {} // username validated successfully

		loginResponse = validatePassword();
		if(!loginResponse.isSuccess()){return loginResponse;} else {} // password validated successfully

		return getUserInfo();
	}	
	
	public String toString(){
		return new StringBuffer()
			.append("LoginRequest[")				
			.append("requestType: " + requestType + "; ")
			.append("username: " + username + "; ")
			.append("password: " + password + "; ")
			.append("]")
			.toString(); 
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
	private LoginResponse getUserInfo() {
		try {
			int userId = getSecUserId();
			LoginResponse loginResponse = new LoginResponse();
			loginResponse.setSuccess(true);
			loginResponse.setUserId(userId);
			return loginResponse;
		} catch (SQLException e) {
			return getErrorLoginResponse("Error checking user info in database: " + e.getMessage());
		}
	}
	
	private LoginResponse validateUsername() {
		if(StringUtil.isEmptyString(username)) {
			return getErrorLoginResponse("Invalid username: " + null);
		} else {			
			return 	LoginResponse.RESPONSE_SUCCESS;	
		}
	}

	private LoginResponse validatePassword() {
		if(StringUtil.isEmptyString(password)) {
			return getErrorLoginResponse("Invalid password: " + null);
		} else {			
			return 	LoginResponse.RESPONSE_SUCCESS;	
		}
	}

	private LoginResponse getErrorLoginResponse(String errorMessage) {
		LoginResponse loginResponse = new LoginResponse();
		loginResponse.setSuccess(false);
		loginResponse.setErrorMessage(errorMessage);
		return loginResponse;
	}
	
	private static final String GET_SEC_USER_ID_SQL = 	" select " +
														"   sec_user_id " + 
														" from " + 
														"   sec_user  " + 
														" where " + 
														"   username = ? and password = ? "; 
	private int getSecUserId() throws SQLException {
		String input = new StringBuffer()
							.append(":Input[")
							.append("username: " + username + ";")
							.append("password: " + password + ";")
							.append("]:")
							.toString();
		String logHead = new StringBuffer()
							.append(TAG + ":getSecUserId:")
							.append(input)
							.toString();
		LOGGER.log(Level.INFO, logHead);

		PreparedStatement stmt = null;
		Connection dbConnection = null;
		ResultSet result = null;
		try {
			dbConnection = AccessUtil.getMySqlConnection();
			LOGGER.log(Level.INFO, logHead + ": Query: " + GET_SEC_USER_ID_SQL);
			stmt = dbConnection.prepareStatement(GET_SEC_USER_ID_SQL);
			stmt.clearParameters();
			stmt.setString(1, username);
			stmt.setString(2, password);
			result = stmt.executeQuery();            
			if(result.next()){
				return result.getInt(1);
			} else {
				return 0;
			}
		} finally {
			try {if (result != null) result.close();} catch (Exception e1) {}
			try {if (stmt != null) stmt.close(); } catch (SQLException e2) {}
			try {if (dbConnection != null) dbConnection.close(); } catch (SQLException e3) {}
		} 		
	}
}
