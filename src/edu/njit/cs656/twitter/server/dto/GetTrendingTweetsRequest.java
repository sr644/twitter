package edu.njit.cs656.twitter.server.dto;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.njit.cs656.twitter.server.util.AccessUtil;

public class GetTrendingTweetsRequest extends Request {
	
   /*
    * ====================================================================
    * Constants
    * ====================================================================
    */
	private static final Logger LOGGER = Logger.getLogger(GetTrendingTweetsRequest.class.getName());
	private static final String TAG = GetTrendingTweetsRequest.class.getSimpleName();

	
   /*
    * ====================================================================
    * Member Variables
    * ====================================================================
    */
	
    
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
	public GetTrendingTweetsResponse validate() {
		return getTrendingTweets();
	}	
	
	public String toString(){
		return new StringBuffer()
			.append("LoginRequest[")				
			.append("requestType: " + requestType + "; ")
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
	private GetTrendingTweetsResponse getTrendingTweets() {
		try {
			GetTrendingTweetsResponse getTrendingTweetsResponse = new GetTrendingTweetsResponse();
			getTrendingTweetsResponse.setSuccess(true);
			getTrendingTweetsResponse.setTweetList(getTrendingTweetsFromDb());
			return getTrendingTweetsResponse;
		} catch (SQLException e) {
			GetTrendingTweetsResponse getTrendingTweetsResponse = new GetTrendingTweetsResponse();
			getTrendingTweetsResponse.setSuccess(true);
			getTrendingTweetsResponse.setErrorMessage("Error getting tweets from database: " + e.getMessage());
			return getTrendingTweetsResponse;
		}
	}
		
	private static final String GET_TRENDING_TWEETS_SQL = 	" select " +
															"   u.username, t.date_added, t.data " + 
															" from " + 
															"   sec_user u, tweet t  " + 
															" where " + 
															"   t.sec_user_id = u.sec_user_id" +
															"   and t.trending_flag = 1 "; 
	private List<Tweet> getTrendingTweetsFromDb() throws SQLException {
		String input = new StringBuffer()
							.append(":Input[")
							.append("]:")
							.toString();
		String logHead = new StringBuffer()
							.append(TAG + ":getTrendingTweetsFromDb:")
							.append(input)
							.toString();
		LOGGER.log(Level.INFO, logHead);

		Statement stmt = null;
		Connection dbConnection = null;
		ResultSet result = null;
		try {
			dbConnection = AccessUtil.getMySqlConnection();
			LOGGER.log(Level.INFO, logHead + ": Query: " + GET_TRENDING_TWEETS_SQL);
			stmt = dbConnection.createStatement();
			result = stmt.executeQuery(GET_TRENDING_TWEETS_SQL);   
			List<Tweet> tweetList = new ArrayList<Tweet>();
			Tweet tweet = null;
			while(result.next()) {
				tweet = new Tweet(); tweetList.add(tweet);
				tweet.setUserName(result.getString("username"));
				tweet.setDateAdded(result.getTimestamp("date_added") == null ? null : new java.util.Date(result.getTimestamp("date_added").getTime()));
				tweet.setData(result.getString("data"));
				tweet.setTrendingFlag(true);
			}
			return tweetList;
		} finally {
			try {if (result != null) result.close();} catch (Exception e1) {}
			try {if (stmt != null) stmt.close(); } catch (SQLException e2) {}
			try {if (dbConnection != null) dbConnection.close(); } catch (SQLException e3) {}
		} 		
	}
}
