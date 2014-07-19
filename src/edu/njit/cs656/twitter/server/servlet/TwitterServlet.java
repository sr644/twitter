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
import edu.njit.cs656.twitter.server.dto.GetTweetsRequest;
import edu.njit.cs656.twitter.server.dto.GetTweetsResponse;
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
	 * 
	 * -- login
	 * 	-- Request
	 * 		-- {"requestType":"login", "username":"sanjayrally", "password":"sanjayrallypw"}
	 * 	-- Response
	 * 		-- {"userId":1,"success":true}
	 * 
	 * -- getTweets
	 * 	-- Request
	 * 		-- {"requestType":"getTweets", "secUserId":"1"}
	 * 	-- Response
	 * 		-- {"sessionId":99,"success":true}
     *
     * -- addTweet
	 * 	-- Request
	 * 		-- {"requestType":"addTweet", "userId":"1", "tweetData":"This is test data 071814-1"}
	 * 	-- Response
	 * 		-- {"success":true,"errorMessage":""}
     *
	 * -- Invalid Request
	 * 	-- Request
	 * 		-- {"requestType":"Test", "username":"sanjayrally", "password":"sanjayrallypw"}
	 * 	-- Response
	 * 		-- {"success":false,"errorMessage":"Request Type not supported: Test"}
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
			} else if(Request.REQUEST_TYPE_GET_TWEETS.equals(request.getRequestType())) {
				GetTweetsResponse getTweetsResponse = processGetTweets(gson.fromJson(jsonStr, GetTweetsRequest.class));
				jsonResponseStr = gson.toJson(getTweetsResponse);
			} else if(Request.REQUEST_TYPE_ADD_TWEET.equals(request.getRequestType())) {		
				AddTweetRequest addTweetRequest = gson.fromJson(jsonStr, AddTweetRequest.class);
				Response response = addTweetRequest.validate();
				jsonResponseStr = gson.toJson(response);
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
	
	private GetTweetsResponse processGetTweets(GetTweetsRequest getTweetsRequest) {
		try {
			GetTweetsResponse getTweetsResponse = new GetTweetsResponse();
			getTweetsResponse.setTestData("Test Data");
			Tweet tweet;
			List<Tweet> tweetList = new ArrayList<Tweet>();
			tweet = new Tweet(); tweet.setTweet("tweet1"); tweetList.add(tweet);
			tweet = new Tweet(); tweet.setTweet("tweet2"); tweetList.add(tweet);
			getTweetsResponse.setTweets(tweetList);
			
			return getTweetsResponse;			
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.warning("Errro looking up tweets in database: " + e.getMessage());
			GetTweetsResponse getTweetsResponse = new GetTweetsResponse();
			getTweetsResponse.setSuccess(false);
			getTweetsResponse.setErrorMessage("Error looking up tweets in database: " + e.getMessage());
			return getTweetsResponse;
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