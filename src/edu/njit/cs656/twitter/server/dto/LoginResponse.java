package edu.njit.cs656.twitter.server.dto;

public class LoginResponse extends Response {
	private int userId;
	
	public static final LoginResponse RESPONSE_SUCCESS = new LoginResponse(true, "");
	
	public LoginResponse() {
		super();
	}
	
	public LoginResponse(boolean success, String errorMessage) {
		super(success, errorMessage);
	}
	
	
	public int getUserId() {return userId;}
	public void setUserId(int userId) {this.userId = userId;}
}
