package com.linksus.entity;

import java.io.Serializable;

public class LinksusSourceAppaccount extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** 账户id */
	private Long institutionId;
	/** 来源id */
	private Long sourceId;
	/** 帐号id */
	private Long accountId;
	/** token字符串 */
	private String token;
	/** token结束日期 */
	private Integer tokenEtime;
	/** 创建时间 */
	private Integer createdTime;
	/** 更新时间 */
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
