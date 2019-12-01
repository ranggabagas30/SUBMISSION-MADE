package com.rangga.submission.data.network.pojo;

import com.google.gson.annotations.SerializedName;

public class ErrorResponse{

	@SerializedName("status_message")
	private String statusMessage;

	@SerializedName("status_code")
	private int statusCode;

	@SerializedName("success")
	private boolean success;

	public void setStatusMessage(String statusMessage){
		this.statusMessage = statusMessage;
	}

	public String getStatusMessage(){
		return statusMessage;
	}

	public void setStatusCode(int statusCode){
		this.statusCode = statusCode;
	}

	public int getStatusCode(){
		return statusCode;
	}

	public void setSuccess(boolean success){
		this.success = success;
	}

	public boolean isSuccess(){
		return success;
	}

	@Override
 	public String toString(){
		return 
			"ErrorResponse{" + 
			"status_message = '" + statusMessage + '\'' + 
			",status_code = '" + statusCode + '\'' + 
			",success = '" + success + '\'' + 
			"}";
		}
}