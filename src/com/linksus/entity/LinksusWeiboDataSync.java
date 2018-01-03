package com.linksus.entity;

import java.io.Serializable;

public class LinksusWeiboDataSync extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** 账户id */
	private Long institutionId;
	/** 帐号id */
	private Long accountId;
	/** 账号类型
	        1新浪
	        2腾讯 */
	private Integer accountType;
	/** 最大id */
	private Long maxId;

	public Long getInstitutionId(){
		return institutionId;
	}

	public void setInstitutionId(Long institutionId){
		this.institutionId = institutionId;
	}

	public Long getAccountId(){
		return accountId;
	}

	public void setAccountId(Long accountId){
		this.accountId = accountId;
	}

	public Integer getAccountType(){
		return accountType;
	}

	public void setAccountType(Integer accountType){
		this.accountType = accountType;
	}

	public Long getMaxId(){
		return maxId;
	}

	public void setMaxId(Long maxId){
		this.maxId = maxId;
	}
}
