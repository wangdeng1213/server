package com.linksus.entity;

import java.io.Serializable;

public class LinksusSourceAppaccount extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** �˻�id */
	private Long institutionId;
	/** ��Դid */
	private Long sourceId;
	/** �ʺ�id */
	private Long accountId;
	/** token�ַ��� */
	private String token;
	/** token�������� */
	private Integer tokenEtime;
	/** ����ʱ�� */
	private Integer createdTime;
	/** ����ʱ�� */
	private Integer lastUpdTime;

	private String appid;
	private Integer account_type;

	public Long getInstitutionId(){
		return institutionId;
	}

	public void setInstitutionId(Long institutionId){
		this.institutionId = institutionId;
	}

	public Long getSourceId(){
		return sourceId;
	}

	public void setSourceId(Long sourceId){
		this.sourceId = sourceId;
	}

	public Long getAccountId(){
		return accountId;
	}

	public void setAccountId(Long accountId){
		this.accountId = accountId;
	}

	public String getToken(){
		return token;
	}

	public void setToken(String token){
		this.token = token;
	}

	public Integer getTokenEtime(){
		return tokenEtime;
	}

	public void setTokenEtime(Integer tokenEtime){
		this.tokenEtime = tokenEtime;
	}

	public Integer getCreatedTime(){
		return createdTime;
	}

	public void setCreatedTime(Integer createdTime){
		this.createdTime = createdTime;
	}

	public Integer getLastUpdTime(){
		return lastUpdTime;
	}

	public void setLastUpdTime(Integer lastUpdTime){
		this.lastUpdTime = lastUpdTime;
	}

	public String getAppid(){
		return appid;
	}

	public void setAppid(String appid){
		this.appid = appid;
	}

	public Integer getAccount_type(){
		return account_type;
	}

	public void setAccount_type(Integer accountType){
		account_type = accountType;
	}

}
