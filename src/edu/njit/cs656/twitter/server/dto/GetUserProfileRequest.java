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

public class GetUserProfileRequest extends Request {
   /*
    * ====================================================================
    * Constants
    * ====================================================================
    */
	private static final Logger LOGGER = Logger.getLogger(GetUserProfileRequest.class.getName());
	private static final String TAG = GetUserProfileRequest.class.getSimpleName();

	
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
	public GetUserProfileResponse validate() {
		GetUserProfileResponse getUserProfileResponse = validateUserId();
		if(!getUserProfileResponse.isSuccess()){return getUserProfileResponse;} else {} // userId validated successfully
		
		return getUserProfileFromDb();
	}	
	
	public String toString(){
		return new StringBuffer()
			.append("GetUserProfileRequest[")
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
	private GetUserProfileResponse validateUserId() {
		if(userId <= 0) {
			GetUserProfileResponse getUserProfileResponse = new GetUserProfileResponse();
			getUserProfileResponse.setSuccess(false);
			getUserProfileResponse.setErrorMessage("Invalid userId: " + userId);
			return getUserProfileResponse;
		} else {
			GetUserProfileResponse getUserProfileResponse = new GetUserProfileResponse();
			getUserProfileResponse.setSuccess(true);
			return getUserProfileResponse; 
		}
	}
	
	private GetUserProfileResponse getUserProfileFromDb() {		 
		try {
			GetUserProfileResponse getUserProfileResponse = new GetUserProfileResponse();
			getUserProfileResponse.setSuccess(true);
			
			UserProfile userProfile = new UserProfile(); getUserProfileResponse.setUserProfile(userProfile);
			userProfile.setUser(getUser());
			userProfile.setTweetList(getUserTweetListFromDb());
			userProfile.setFollowingUserList(getFollowingUserListFromDb());
			userProfile.setFollowedUserList(getFollowedUserListFromDb());
			
			return getUserProfileResponse;
		} catch (SQLException e) {
			GetUserProfileResponse getUserProfileResponse = new GetUserProfileResponse();
			getUserProfileResponse.setSuccess(false);
			getUserProfileResponse.setErrorMessage("Error getting user info from database: " + e.getMessage());
			return getUserProfileResponse;
		}
	}
	
	private static final String GET_USER_SQL = 	" select " +
												"   username, user_description " + 
												" from " + 
												"   sec_user  " + 
												" where " + 
												"   sec_user_id = ? "; 
	private User getUser() throws SQLException {
		String input = new StringBuffer()
							.append(":Input[")
							.append("userId: " + userId + ";")
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
			LOGGER.log(Level.INFO, logHead + ": Query: " + GET_USER_SQL);
			stmt = dbConnection.prepareStatement(GET_USER_SQL);
			stmt.clearParameters();
			stmt.setInt(1, userId);
			result = stmt.executeQuery();            
			if(result.next()){
				User user = new User();
				user.setUserId(userId);
				user.setUsername(result.getString("username"));
				user.setUserDescription(result.getString("user_description"));
				return user;
			} else {
				return null;
			}
		} finally {
			try {if (result != null) result.close();} catch (Exception e1) {}
			try {if (stmt != null) stmt.close(); } catch (SQLException e2) {}
			try {if (dbConnection != null) dbConnection.close(); } catch (SQLException e3) {}
		} 		
	}
		
	private static final String GET_USER_TWEETLIST_SQL = 	" select " +
															"   u.username, u.user_description, t.date_added, t.data, t.trending_flag " + 
															" from " + 
															"   sec_user u, tweet t  " + 
															" where " + 
															"   t.sec_user_id = u.sec_user_id" +
															"   and u.sec_user_id = ? "; 
	private List<Tweet> getUserTweetListFromDb() throws SQLException {
		String input = new StringBuffer()
							.append(":Input[")
							.append("]:")
							.toString();
		String logHead = new StringBuffer()
							.append(TAG + ":getUserTweetListFromDb:")
							.append(input)
							.toString();
		LOGGER.log(Level.INFO, logHead);

		PreparedStatement stmt = null;
		Connection dbConnection = null;
		ResultSet result = null;
		try {
			dbConnection = AccessUtil.getMySqlConnection();
			LOGGER.log(Level.INFO, logHead + ": Query: " + GET_USER_TWEETLIST_SQL);
			stmt = dbConnection.prepareStatement(GET_USER_TWEETLIST_SQL);			
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
				tweet.setUserDescription(result.getString("user_description"));
			}
			return tweetList;
		} finally {
			try {if (result != null) result.close();} catch (Exception e1) {}
			try {if (stmt != null) stmt.close(); } catch (SQLException e2) {}
			try {if (dbConnection != null) dbConnection.close(); } catch (SQLException e3) {}
		} 		
	}
	
	private static final String GET_FOLLOWING_USER_LIST_SQL = 	" select " +
																"   su.sec_user_id, su.username, su.user_description" + 
																" from " + 
																"   user_followed uf, sec_user su  " + 
																" where " + 
																"   uf.sec_user_id = ? and su.sec_user_id = uf.sec_user_followed_id "; 
	private List<User> getFollowingUserListFromDb() throws SQLException {
		String input = new StringBuffer()
							.append(":Input[")
							.append("]:")
							.toString();
		String logHead = new StringBuffer()
							.append(TAG + ":getFollowingUserListFromDb:")
							.append(input)
							.toString();
		LOGGER.log(Level.INFO, logHead);

		PreparedStatement stmt = null;
		Connection dbConnection = null;
		ResultSet result = null;
		try {
			dbConnection = AccessUtil.getMySqlConnection();
			LOGGER.log(Level.INFO, logHead + ": Query: " + GET_FOLLOWING_USER_LIST_SQL);
			stmt = dbConnection.prepareStatement(GET_FOLLOWING_USER_LIST_SQL);			
			stmt.clearParameters();
			stmt.setInt(1, userId);
			result = stmt.executeQuery();
			List<User> userList = new ArrayList<User>();
			User user = null;
			while(result.next()) {
				user = new User(); userList.add(user);
				user.setUserId(result.getInt("sec_user_id"));
				user.setUsername(result.getString("username"));
				user.setUserDescription(result.getString("user_description"));
			}
			return userList;
		} finally {
			try {if (result != null) result.close();} catch (Exception e1) {}
			try {if (stmt != null) stmt.close(); } catch (SQLException e2) {}
			try {if (dbConnection != null) dbConnection.close(); } catch (SQLException e3) {}
		} 		
	}
	
	private static final String GET_FOLLOWED_USER_LIST_SQL = 	" select " +
																"   su.sec_user_id, su.username, su.user_description" + 
																" from " + 
																"   user_followed uf, sec_user su  " + 
																" where " + 
																"   uf.sec_user_followed_id = ? and su.sec_user_id = uf.sec_user_id "; 
	private List<User> getFollowedUserListFromDb() throws SQLException {
		String input = new StringBuffer()
							.append(":Input[")
							.append("]:")
							.toString();
		String logHead = new StringBuffer()
							.append(TAG + ":getFollowedUserListFromDb:")
							.append(input)
							.toString();
		LOGGER.log(Level.INFO, logHead);

		PreparedStatement stmt = null;
		Connection dbConnection = null;
		ResultSet result = null;
		try {
			dbConnection = AccessUtil.getMySqlConnection();
			LOGGER.log(Level.INFO, logHead + ": Query: " + GET_FOLLOWED_USER_LIST_SQL);
			stmt = dbConnection.prepareStatement(GET_FOLLOWED_USER_LIST_SQL);			
			stmt.clearParameters();
			stmt.setInt(1, userId);
			result = stmt.executeQuery();
			List<User> userList = new ArrayList<User>();
			User user = null;
			while(result.next()) {
				user = new User(); userList.add(user);
				user.setUserId(result.getInt("sec_user_id"));
				user.setUsername(result.getString("username"));
				user.setUserDescription(result.getString("user_description"));
			}
			return userList;
		} finally {
			try {if (result != null) result.close();} catch (Exception e1) {}
			try {if (stmt != null) stmt.close(); } catch (SQLException e2) {}
			try {if (dbConnection != null) dbConnection.close(); } catch (SQLException e3) {}
		} 		
	}
}
