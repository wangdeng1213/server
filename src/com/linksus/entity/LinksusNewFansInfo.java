package com.linksus.entity;

import java.io.Serializable;

public class LinksusNewFansInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//�˺�id
	private String institutionId;

	//��˾�˻�id
	private String compangAppId;

	//��˿id
	private String fanId;

	//�˻�����
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
