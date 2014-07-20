package edu.njit.cs656.twitter.server.dto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.njit.cs656.twitter.server.util.AccessUtil;

public class GetTweetsByUserIdRequest extends Request {
	
   /*
    * ====================================================================
    * Constants
    * ====================================================================
    */
	private static final Logger LOGGER = Logger.getLogger(GetTweetsByUserIdRequest.class.getName());
	private static final String TAG = GetTweetsByUserIdRequest.class.getSimpleName();

	
   /*
    * ====================================================================
    * Member Variables
    * ====================================================================
    */
	private int userId;
	
    
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
	public GetTweetsByUserIdResponse validate() {
		return getTweetsByUserId();
	}	
	
	public String toString(){
		return new StringBuffer()
			.append("GetTweetsByUserIdRequest[")
			.append("requestType: " + requestType + "; ")
			.append("userId: " + userId + "; ")
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
	private GetTweetsByUserIdResponse getTweetsByUserId() {
		try {
			GetTweetsByUserIdResponse getTweetsByUserIdResponse = new GetTweetsByUserIdResponse();
			getTweetsByUserIdResponse.setSuccess(true);
			getTweetsByUserIdResponse.setTweetList(getTweetsByUserIdFromDb());
			return getTweetsByUserIdResponse;
		} catch (SQLException e) {
			GetTweetsByUserIdResponse getTweetsByUserIdResponse = new GetTweetsByUserIdResponse();
			getTweetsByUserIdResponse.setSuccess(true);
			getTweetsByUserIdResponse.setErrorMessage("Error getting tweets from database: " + e.getMessage());
			return getTweetsByUserIdResponse;
		}
	}
		
	private static final String GET_TWEETS_BY_USER_ID_SQL = 	" select " +
																"   u.username, t.date_added, t.data, t.trending_flag " + 
																" from " + 
																"   sec_user u, tweet t  " + 
																" where " + 
																"   t.sec_user_id = u.sec_user_id" +
																"   and u.sec_user_id = ? "; 
	private List<Tweet> getTweetsByUserIdFromDb() throws SQLException {
		String input = new StringBuffer()
							.append(":Input[")
							.append("]:")
							.toString();
		String logHead = new StringBuffer()
							.append(TAG + ":getTweetsByUserIdFromDb:")
							.append(input)
							.toString();
		LOGGER.log(Level.INFO, logHead);

		PreparedStatement stmt = null;
		Connection dbConnection = null;
		ResultSet result = null;
		try {
			dbConnection = AccessUtil.getMySqlConnection();
			LOGGER.log(Level.INFO, logHead + ": Query: " + GET_TWEETS_BY_USER_ID_SQL);
			stmt = dbConnection.prepareStatement(GET_TWEETS_BY_USER_ID_SQL);			
			stmt.clearParameters();
			stmt.setInt(1, userId);
			result = stmt.executeQuery();
			List<Tweet> tweetList = new ArrayList<Tweet>();
			Tweet tweet = null;
			while(result.next()) {
				tweet = new Tweet(); tweetList.add(tweet);
				tweet.setUserName(result.getString("username"));
				tweet.setDateAdded(result.getTimestamp("date_added") == null ? null : new java.util.Date(result.getTimestamp("date_added").getTime()));
				tweet.setData(result.getString("data"));
				tweet.setTrendingFlag(result.getBoolean("trending_flag"));
			}
			return tweetList;
		} finally {
			try {if (result != null) result.close();} catch (Exception e1) {}
			try {if (stmt != null) stmt.close(); } catch (SQLException e2) {}
			try {if (dbConnection != null) dbConnection.close(); } catch (SQLException e3) {}
		} 		
	}
}
