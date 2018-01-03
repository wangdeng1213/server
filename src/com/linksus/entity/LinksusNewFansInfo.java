package com.linksus.entity;

import java.io.Serializable;

public class LinksusNewFansInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//账号id
	private String institutionId;

	//公司账户id
	private String compangAppId;

	//粉丝id
	private String fanId;

	//账户类型
	private int accountType;

	public String getInstitutionId(){
		return institutionId;
	}

	public void setInstitutionId(String institutionId){
		this.institutionId = institutionId;
	}

	public String getCompangAppId(){
		return compangAppId;
	}

	public void setCompangAppId(String compangAppId){
		this.compangAppId = compangAppId;
	}

	public String getFanId(){
		return fanId;
	}

	public void setFanId(String fanId){
		this.fanId = fanId;
	}

	public int getAccountType(){
		return accountType;
	}

	public void setAccountType(int accountType){
		this.accountType = accountType;
	}

}
