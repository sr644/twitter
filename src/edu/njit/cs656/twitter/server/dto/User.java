package edu.njit.cs656.twitter.server.dto;

import java.util.List;

public class User {
	private int userId;
	private String username;
	private String userDescription;	

	public int getUserId() {return userId;}
	public void setUserId(int userId) {this.userId = userId;}	
	public String getUsername() {return username;}
	public void setUsername(String username) {this.username = username;}
	public String getUserDescription() {return userDescription;}
	public void setUserDescription(String userDescription) {this.userDescription = userDescription;}
		
}
