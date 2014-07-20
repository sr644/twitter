package edu.njit.cs656.twitter.server.dto;

import java.util.List;

public class UserProfile {
	private User user;
	List<Tweet> tweetList;
	List<User> followingUserList;
	List<User> followedUserList;
	
	public User getUser() {return user;}
	public void setUser(User user) {this.user = user;}
	public List<Tweet> getTweetList() {return tweetList;}
	public void setTweetList(List<Tweet> tweetList) {this.tweetList = tweetList;}
	public List<User> getFollowingUserList() {return followingUserList;}
	public void setFollowingUserList(List<User> followingUserList) {this.followingUserList = followingUserList;}
	public List<User> getFollowedUserList() {return followedUserList;}
	public void setFollowedUserList(List<User> followedUserList) {this.followedUserList = followedUserList;}	
}
