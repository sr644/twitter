package edu.njit.cs656.twitter.server.dto;

public class Request {
	public static final String REQUEST_TYPE_LOGIN = "login";
	public static final String REQUEST_TYPE_GET_TWEETS = "getTweets";
	public static final String REQUEST_TYPE_ADD_TWEET = "addTweet";
	
	protected String requestType; 

	public String getRequestType() {return requestType;}
	public void setRequestType(String requestType) {this.requestType = requestType;} 	
	
}