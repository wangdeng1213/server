package com.linksus.entity;

import java.io.Serializable;

public class LinksusWeiboDataSync extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** �˻�id */
	private Long institutionId;
	/** �ʺ�id */
	private Long accountId;
	/** �˺�����
	        1����
	        2��Ѷ */
	private Integer accountType;
	/** ���id */
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
