package com.linksus.entity;

import java.io.Serializable;

public class LinksusTaskGovInteract extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** 平台账号ID */
	private Long accountId;
	/** 互动类型:1 评论 2 转发 3 @ 4 评论并@ 5 腾讯互动 6 腾讯私信 */
	private Integer interactType;
	/** 新浪的since_id,腾讯的lastid */
	private Long maxId;
	/** 腾讯本页起始时间 */
	private Long pagetime;

	public Long getAccountId(){
		return accountId;
	}

	public void setAccountId(Long accountId){
		this.accountId = accountId;
	}

	public Integer getInteractType(){
		return interactType;
	}

	public void setInteractType(Integer interactType){
		this.interactType = interactType;
	}

	public Long getMaxId(){
		return maxId;
	}

	public void setMaxId(Long maxId){
		this.maxId = maxId;
	}

	public Long getPagetime(){
		return pagetime;
	}

	public void setPagetime(Long pagetime){
		this.pagetime = pagetime;
	}
}
