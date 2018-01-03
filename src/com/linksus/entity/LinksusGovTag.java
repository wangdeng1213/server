package com.linksus.entity;

import java.io.Serializable;

public class LinksusGovTag extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** 标签id */
	private Integer tagId;
	/** 标签名称 */
	private String context;
	/** 账号id（谁创建的） */
	private Integer accountId;

	public Integer getTagId(){
		return tagId;
	}

	public void setTagId(Integer tagId){
		this.tagId = tagId;
	}

	public String getContext(){
		return context;
	}

	public void setContext(String context){
		this.context = context;
	}

	public Integer getAccountId(){
		return accountId;
	}

	public void setAccountId(Integer accountId){
		this.accountId = accountId;
	}
}
