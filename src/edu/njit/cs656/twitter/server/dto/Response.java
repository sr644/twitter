package edu.njit.cs656.twitter.server.dto;

public class Response {
	protected boolean success;
	protected String errorMessage;
	
	public static final Response RESPONSE_SUCCESS = new Response(true, "");
			
	public static Response getErrorResponse(String errorMessage) {return new Response(false, errorMessage);}

	public Response() {}
	public Response(boolean success, String errorMessage) {
		this.success = success;
		this.errorMessage = errorMessage;
	}

	public boolean isSuccess() {return success;}
	public void setSuccess(boolean success) {this.success = success;}
	public String getErrorMessage() {return errorMessage;}
	public void setErrorMessage(String errorMessage) {this.errorMessage = errorMessage;}	
}
