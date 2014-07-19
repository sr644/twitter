package edu.njit.cs656.twitter.server.dto;

import java.util.Date;

public class Tweet {
	private Date tweetDate;
	private String tweet;
	
	public Date getTweetDate() {return tweetDate;}
	public void setTweetDate(Date tweetDate) {this.tweetDate = tweetDate;}
	public String getTweet() {return tweet;}
	public void setTweet(String tweet) {this.tweet = tweet;}
		
}
