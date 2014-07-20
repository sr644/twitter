package edu.njit.cs656.twitter.server.dto;

import java.util.Date;

public class Tweet {
	private String userName;
	private Date dateAdded;
	private String data;
	private boolean trendingFlag;
	private String userDescription;
	
	public String getUserName() {return userName;}
	public void setUserName(String userName) {this.userName = userName;}
	public Date getDateAdded() {return dateAdded;}
	public void setDateAdded(Date dateAdded) {this.dateAdded = dateAdded;}
	public String getData() {return data;}
	public void setData(String data) {this.data = data;}
	public boolean isTrendingFlag() {return trendingFlag;}
	public void setTrendingFlag(boolean trendingFlag) {this.trendingFlag = trendingFlag;}
	public String getUserDescription() {return userDescription;}
	public void setUserDescription(String userDescription) {this.userDescription = userDescription;}	
}
