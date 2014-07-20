package edu.njit.cs656.twitter.server.dto;

public class GetUserProfileResponse extends Response {
	private UserProfile userProfile;

	public UserProfile getUserProfile() {return userProfile;}
	public void setUserProfile(UserProfile userProfile) {this.userProfile = userProfile;}	
	
}
