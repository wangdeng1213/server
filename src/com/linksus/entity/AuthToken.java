package com.linksus.entity;

public class AuthToken{

	private String token;
	private String appId;

	public String getToken(){
		return token;
	}

	public void setToken(String token){
		this.token = token;
	}

	public String getAppId(){
		return appId;
	}

	public void setAppId(String appId){
		this.appId = appId;
	}
}
