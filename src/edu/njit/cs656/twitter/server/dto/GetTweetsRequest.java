package edu.njit.cs656.twitter.server.dto;

public class GetTweetsRequest extends Request {
	private int secUserId;

	public int getSecUserId() {return secUserId;}
	public void setSecUserId(int secUserId) {this.secUserId = secUserId;}	
}
