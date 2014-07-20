package edu.njit.cs656.twitter.server.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import edu.njit.cs656.twitter.server.dto.AddTweetRequest;
import edu.njit.cs656.twitter.server.dto.GetTrendingTweetsRequest;
import edu.njit.cs656.twitter.server.dto.GetTrendingTweetsResponse;
import edu.njit.cs656.twitter.server.dto.GetTweetsByUserIdRequest;
import edu.njit.cs656.twitter.server.dto.GetTweetsByUserIdResponse;
import edu.njit.cs656.twitter.server.dto.GetTweetsRequest;
import edu.njit.cs656.twitter.server.dto.GetTweetsResponse;
import edu.njit.cs656.twitter.server.dto.GetUserProfileRequest;
import edu.njit.cs656.twitter.server.dto.GetUserProfileResponse;
import edu.njit.cs656.twitter.server.dto.LoginRequest;
import edu.njit.cs656.twitter.server.dto.LoginResponse;
import edu.njit.cs656.twitter.server.dto.Request;
import edu.njit.cs656.twitter.server.dto.Response;
import edu.njit.cs656.twitter.server.dto.Tweet;
import edu.njit.cs656.twitter.server.util.StringUtil;

public class TwitterServlet extends HttpServlet {
	
   /*
    * ====================================================================
    * Constants
    * ====================================================================
    */
	
	     
   /*
    * ====================================================================
    * Member Variables
    * ====================================================================
    */
	private static final Logger LOGGER = Logger.getLogger(TwitterServlet.class.getName());
	
    
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
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LOGGER.log(Level.INFO, "doGet===============");
		PrintWriter out = response.getWriter();	
		out.println("Twitter GET Response");
		out.close();
		LOGGER.log(Level.INFO, "doGet done===============");
	}
	
	/*
	 * Processes HTTP Post requests.
	 * 
	 * JSON is the expected data. Example:
	 	  
	  -- login
	  	-- Request
	  		-- {"requestType":"login", "username":"sanjayrally", "password":"sanjayrallypw"}
	  	-- Response
	  		-- {"userId":1,"success":true}
	  
      -- addTweet
	  	-- Request
	  		-- {"requestType":"addTweet", "userId":"1", "tweetData":"This is test data 071814-1"}
	  	-- Response
	  		-- {"success":true,"errorMessage":""}
	  
	  -- getTrendingTweets
 	  	-- Request
	  		-- {"requestType":"getTrendingTweets"}
	  	-- Response
	  		-- {
				   	"tweetList":    [
				            {
				         "userName": "nfl",
				         "dateAdded": "Jul 19, 2014 10:45:32 PM",
				         "data": "Vikings suspend special teams coach Priefer",
				         "trendingFlag": true
				      },
				            {
				         "userName": "nfl",
				         "dateAdded": "Jul 19, 2014 10:45:34 PM",
				         "data": "Godell floats 49ers' pad as Raiders option",
				         "trendingFlag": true
				      },            
				   ],
				   "success": true
				}
	  
	  -- getTweetsByUserId
 	  	-- Request
	  		-- {"requestType":"getTweetsByUserId", "userId":"1"}
	  	-- Response
	  		-- {
				   	"tweetList":    [
				            {
				         "userName": "nfl",
				         "dateAdded": "Jul 19, 2014 10:45:32 PM",
				         "data": "Vikings suspend special teams coach Priefer",
				         "trendingFlag": true
				      },
				            {
				         "userName": "nfl",
				         "dateAdded": "Jul 19, 2014 10:45:34 PM",
				         "data": "Godell floats 49ers' pad as Raiders option",
				         "trendingFlag": true
				      },            
				   ],
				   "success": true
				}
				
	  -- getUserProfile
 	  	-- Request
	  		-- {"requestType":"getUserProfile", "userId":"1"}
	  	-- Response
	  		-- {
				   	"tweetList":    [
				            {
				         "userName": "nfl",
				         "dateAdded": "Jul 19, 2014 10:45:32 PM",
				         "data": "Vikings suspend special teams coach Priefer",
				         "trendingFlag": true
				      },
				            {
				         "userName": "nfl",
				         "dateAdded": "Jul 19, 2014 10:45:34 PM",
				         "data": "Godell floats 49ers' pad as Raiders option",
				         "trendingFlag": true
				      },            
				   ],
				   "success": true
				}
	 
	  -- Invalid Request
	  	-- Request
	  		-- {"requestType":"Test", "username":"sanjayrally", "password":"sanjayrallypw"}
	  	-- Response
	 		-- {"success":false,"errorMessage":"Request Type not supported: Test"}
	 		
	 		
	 *  
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
		LOGGER.log(Level.INFO, "doPost===============");
		
		String jsonStr = getJsonFromHttpServletRequest(httpServletRequest);
		Gson gson = new GsonBuilder().create();
		String jsonResponseStr = null;
		if(StringUtil.isEmptyString(jsonStr)) {
			jsonResponseStr = getErrorResponseJsonString("Unable to read input data", gson);
		} else {			
			Request request = gson.fromJson(jsonStr, Request.class);
			
			if(Request.REQUEST_TYPE_LOGIN.equals(request.getRequestType())) {
				LoginRequest loginRequest = gson.fromJson(jsonStr, LoginRequest.class);
				LoginResponse loginResponse = loginRequest.validate();
				jsonResponseStr = gson.toJson(loginResponse);
			} else if(Request.REQUEST_TYPE_ADD_TWEET.equals(request.getRequestType())) {		
				AddTweetRequest addTweetRequest = gson.fromJson(jsonStr, AddTweetRequest.class);
				Response response = addTweetRequest.validate();
				jsonResponseStr = gson.toJson(response);
			} else if(Request.REQUEST_TYPE_GET_TRENDING_TWEETS.equals(request.getRequestType())) {		
				GetTrendingTweetsRequest getTrendingTweetsRequest = gson.fromJson(jsonStr, GetTrendingTweetsRequest.class);
				GetTrendingTweetsResponse getTrendingTweetsResponse = getTrendingTweetsRequest.validate();
				jsonResponseStr = gson.toJson(getTrendingTweetsResponse);
			} else if(Request.REQUEST_TYPE_GET_TWEETS_BY_USER_ID.equals(request.getRequestType())) {		
				GetTweetsByUserIdRequest getTweetsByUserIdRequest = gson.fromJson(jsonStr, GetTweetsByUserIdRequest.class);
				GetTweetsByUserIdResponse getTweetsByUserIdResponse = getTweetsByUserIdRequest.validate();
				jsonResponseStr = gson.toJson(getTweetsByUserIdResponse);
			} else if(Request.REQUEST_TYPE_GET_USER_PROFILE.equals(request.getRequestType())) {		
				GetUserProfileRequest getUserProfileRequest = gson.fromJson(jsonStr, GetUserProfileRequest.class);
				GetUserProfileResponse getUserProfileResponse = getUserProfileRequest.validate();
				jsonResponseStr = gson.toJson(getUserProfileResponse);
			} else {
				jsonResponseStr = getErrorResponseJsonString("Request Type not supported: " + request.getRequestType(), gson);
			}
		}
		
		httpServletResponse.setContentType("application/json");
		PrintWriter printWriter = httpServletResponse.getWriter();
		printWriter.write(jsonResponseStr);
		printWriter.flush();
		
		LOGGER.log(Level.INFO, "doPost done===============");
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
	private String getJsonFromHttpServletRequest(HttpServletRequest httpServletRequest) {
		if(httpServletRequest != null) {			
			try {
				StringBuffer stringBuffer = new StringBuffer();
				String line = null;
				BufferedReader reader = httpServletRequest.getReader();
				while ((line = reader.readLine()) != null) {
					stringBuffer.append(line);
				}		
				return stringBuffer.toString();
			} catch (Exception e) {
				e.printStackTrace();
				LOGGER.warning("Error reading request data: " + e.getMessage());
				return "";
			}
		} else {
			return "";
		}
	}
	
	private String getErrorResponseJsonString(String errorMessage, Gson gson) {
		Response response = new Response();
		response.setSuccess(false);
		response.setErrorMessage("Unable to read input data");
		return gson.toJson(response);
	}
    
   /*
    * ====================================================================
    * Inner classes
    * ====================================================================
    */

}
