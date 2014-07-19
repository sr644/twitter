package edu.njit.cs656.twitter.server.dto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.njit.cs656.twitter.server.util.AccessUtil;
import edu.njit.cs656.twitter.server.util.StringUtil;

public class AddTweetRequest extends Request {

   /*
    * ====================================================================
    * Constants
    * ====================================================================
    */
	private static final Logger LOGGER = Logger.getLogger(AddTweetRequest.class.getName());
	private static final String TAG = AddTweetRequest.class.getSimpleName();

   /*
    * ====================================================================
    * Member Variables
    * ====================================================================
    */
	private int userId;
	private String tweetData;
	
    
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
	public int getUserId() {return userId;}
	public void setUserId(int userId) {this.userId = userId;}
	public String getTweetData() {return tweetData;}
	public void setTweetData(String tweetData) {this.tweetData = tweetData;}
	
	public Response validate() {		
		Response response = null;
		
		response = validatesecUserId();
		if(!response.isSuccess()){return response;} else {} // secUserId validated successfully
		
		response = validateTweetData();
		if(!response.isSuccess()){return response;} else {} // tweetData validated successfully		
		
		return addTweet();
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
	private Response validatesecUserId() {
		if(userId <= 0) {
			return Response.getErrorResponse("Invalid userId: " + userId);
		} else {
			return Response.RESPONSE_SUCCESS;
		}
	}

	private Response validateTweetData() {
		if(StringUtil.isEmptyString(tweetData)) {
			return Response.getErrorResponse("Invalid tweetData: " + null);
		} else {			
			try {
				// Check in db
				if(secUserIdExistInDb()) {
					return Response.RESPONSE_SUCCESS;
				} else {
					return Response.getErrorResponse("userId: " + userId + " does not exist in database.");
				}
			} catch(SQLException e) {
				return Response.getErrorResponse("Error checking userId in database: " + e.getMessage());
			}			
		}
	}

	public static final String SEC_USER_ID_EXISTS_IN_DB_SQL = 	" select " +
																"   username " + 
																" from " + 
																"   sec_user  " + 
																" where " + 
																"   sec_user_id = ? ";
	private boolean secUserIdExistInDb() throws SQLException {
		String input = new StringBuffer()
							.append(":Input[")
							.append("userId: " + userId + ";")
							.append("]:")
							.toString();
		String logHead = new StringBuffer()
							.append(TAG + ":secUserIdExistInDb:")
							.append(input)
							.toString();
		LOGGER.log(Level.INFO, logHead);

		PreparedStatement stmt = null;
		Connection dbConnection = null;
		ResultSet result = null;
		try {
			dbConnection = AccessUtil.getMySqlConnection();
			LOGGER.log(Level.INFO, logHead + ": Query: " + SEC_USER_ID_EXISTS_IN_DB_SQL);
			stmt = dbConnection.prepareStatement(SEC_USER_ID_EXISTS_IN_DB_SQL);
			stmt.clearParameters();
			stmt.setInt(1, userId);
			result = stmt.executeQuery();            
			if(result.next()){
				return true;
			} else {
				return false;
			}
		} finally {
			try {if (result != null) result.close();} catch (Exception e1) {}
			try {if (stmt != null) stmt.close(); } catch (SQLException e2) {}
			try {if (dbConnection != null) dbConnection.close(); } catch (SQLException e3) {}
		} 		
	}
	
	private Response addTweet() {
		try {
			// Add tweet to the database;
			addTweetInDb();
			return Response.RESPONSE_SUCCESS;
		} catch(SQLException e) {
			return Response.getErrorResponse("Error adding tweet to the database: " + e.getMessage());
		}
	}
	
	private final String ADD_TWEET_IN_DB_SQL = 	" insert " +
												" into tweet (sec_user_id, date_added, data, trending_flag) " +
												" values (?, ?, ?, ?) "; 
	private void addTweetInDb() throws SQLException{

		String input = new StringBuffer()
							.append(":Input[")
							.append("userId: " + userId + ";")
							.append("tweetData: " + tweetData + ";")
							.append("]:")
							.toString();
		String logHead = new StringBuffer()
							.append(TAG + ":secUserIdExistInDb:")
							.append(input)
							.toString();
		LOGGER.log(Level.INFO, logHead);

		PreparedStatement stmt = null;
		Connection dbConnection = null;
		ResultSet result = null;
		try {
			dbConnection = AccessUtil.getMySqlConnection();
			LOGGER.log(Level.INFO, logHead + ": Query: " + ADD_TWEET_IN_DB_SQL);
			stmt = dbConnection.prepareStatement(ADD_TWEET_IN_DB_SQL);
			stmt.clearParameters();
			stmt.setInt(1, userId);
			stmt.setTimestamp(2, new java.sql.Timestamp(new java.util.Date().getTime()));
			stmt.setString(3, tweetData);			
			stmt.setBoolean(4, false);
			stmt.execute();            
			LOGGER.log(Level.INFO, logHead + ": Tweet added successfully");
		} finally {
			try {if (result != null) result.close();} catch (Exception e1) {}
			try {if (stmt != null) stmt.close(); } catch (SQLException e2) {}
			try {if (dbConnection != null) dbConnection.close(); } catch (SQLException e3) {}
		}        
	}

    
   /*
    * ====================================================================
    * Inner classes
    * ====================================================================
    */
}



























