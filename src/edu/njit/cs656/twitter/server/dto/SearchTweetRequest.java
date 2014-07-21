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
import edu.njit.cs656.twitter.server.util.StringUtil;

public class SearchTweetRequest extends Request {
	
   /*
    * ====================================================================
    * Constants
    * ====================================================================
    */
	private static final Logger LOGGER = Logger.getLogger(SearchTweetRequest.class.getName());
	private static final String TAG = SearchTweetRequest.class.getSimpleName();

	
   /*
    * ====================================================================
    * Member Variables
    * ====================================================================
    */
	private String searchString;
	
    
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
	public SearchTweetResponse validate() {
		SearchTweetResponse searchTweetResponse = validateSearchString();
		if(!searchTweetResponse.isSuccess()){return searchTweetResponse;} else {} // userId validated successfully
		
		return getTweetsBySearchString();
	}	
	
	public String toString(){
		return new StringBuffer()
			.append("GetTweetsByUserIdRequest[")
			.append("requestType: " + requestType + "; ")
			.append("searchString: " + searchString + "; ")
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
	private SearchTweetResponse validateSearchString() {
		if(StringUtil.isEmptyString(searchString)) {
			SearchTweetResponse searchTweetResponse = new SearchTweetResponse();
			searchTweetResponse.setSuccess(false);
			searchTweetResponse.setErrorMessage("Invalid searchString: " + searchString);
			return searchTweetResponse;
		} else {
			SearchTweetResponse searchTweetResponse = new SearchTweetResponse();
			searchTweetResponse.setSuccess(true);
			return searchTweetResponse; 
		}
	}
	
	private SearchTweetResponse getTweetsBySearchString() {
		try {
			SearchTweetResponse searchTweetResponse = new SearchTweetResponse();
			searchTweetResponse.setSuccess(true);
			searchTweetResponse.setTweetList(getTweetsBySearchStringFromDb());
			return searchTweetResponse;
		} catch (SQLException e) {
			SearchTweetResponse searchTweetResponse = new SearchTweetResponse();
			searchTweetResponse.setSuccess(false);
			searchTweetResponse.setErrorMessage("Error getting tweets from database: " + e.getMessage());
			return searchTweetResponse;
		}
	}
		
	private static final String GET_TWEETS_BY_SEARCH_STRING_SQL = 	" select " +
																	"   u.username, u.user_description, t.date_added, t.data, t.trending_flag " + 
																	" from " + 
																	"   sec_user u, tweet t  " + 
																	" where " + 
																	"   t.sec_user_id = u.sec_user_id" +
																	"   and t.data like ? "; 
	private List<Tweet> getTweetsBySearchStringFromDb() throws SQLException {
		String input = new StringBuffer()
							.append(":Input[")
							.append("]:")
							.toString();
		String logHead = new StringBuffer()
							.append(TAG + ":getTweetsBySearchStringFromDb:")
							.append(input)
							.toString();
		LOGGER.log(Level.INFO, logHead);

		PreparedStatement stmt = null;
		Connection dbConnection = null;
		ResultSet result = null;
		try {
			dbConnection = AccessUtil.getMySqlConnection();
			LOGGER.log(Level.INFO, logHead + ": Query: " + GET_TWEETS_BY_SEARCH_STRING_SQL);
			stmt = dbConnection.prepareStatement(GET_TWEETS_BY_SEARCH_STRING_SQL);			
			stmt.clearParameters();
			stmt.setString(1, "%" + searchString + "%");
			result = stmt.executeQuery();
			List<Tweet> tweetList = new ArrayList<Tweet>();
			Tweet tweet = null;
			while(result.next()) {
				tweet = new Tweet(); tweetList.add(tweet);
				tweet.setUserName(result.getString("username"));
				tweet.setDateAdded(result.getTimestamp("date_added") == null ? null : new java.util.Date(result.getTimestamp("date_added").getTime()));
				tweet.setData(result.getString("data"));
				tweet.setTrendingFlag(result.getBoolean("trending_flag"));
				tweet.setUserDescription(result.getString("user_description"));
			}
			return tweetList;
		} finally {
			try {if (result != null) result.close();} catch (Exception e1) {}
			try {if (stmt != null) stmt.close(); } catch (SQLException e2) {}
			try {if (dbConnection != null) dbConnection.close(); } catch (SQLException e3) {}
		} 		
	}
}
