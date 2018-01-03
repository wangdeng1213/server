package com.linksus.entity;

import java.io.Serializable;

public class AppInfo implements Serializable{

	private String name;
	private String psssword;
	private String appKey;
	private String appSecret;
	private String type;

	public String getName(){
		return name;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getAppKey(){
		return appKey;
	}

	public void setAppKey(String appKey){
		this.appKey = appKey;
	}

	public String getAppSecret(){
		return appSecret;
	}

	public void setAppSecret(String appSecret){
		this.appSecret = appSecret;
	}

	public String getType(){
		return type;
	}

	public void setType(String type){
		this.type = type;
	}

	public String getPsssword(){
		return psssword;
	}

	public void setPsssword(String psssword){
		this.psssword = psssword;
	}
}
