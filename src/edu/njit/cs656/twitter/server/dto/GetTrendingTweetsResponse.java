package edu.njit.cs656.twitter.server.dto;

import java.util.List;

public class GetTrendingTweetsResponse extends Response {
	private List<Tweet> tweetList;

	public List<Tweet> getTweetList() {return tweetList;}
	public void setTweetList(List<Tweet> tweetList) {this.tweetList = tweetList;}
		
}
