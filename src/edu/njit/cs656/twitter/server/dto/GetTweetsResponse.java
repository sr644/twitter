package edu.njit.cs656.twitter.server.dto;

import java.util.List;

public class GetTweetsResponse extends Response {
	private String testData;	
	private List<Tweet> tweets;
	
	public String getTestData() {return testData;}
	public void setTestData(String testData) {this.testData = testData;}
	public List<Tweet> getTweets() {return tweets;}
	public void setTweets(List<Tweet> tweets) {this.tweets = tweets;}
	
}
