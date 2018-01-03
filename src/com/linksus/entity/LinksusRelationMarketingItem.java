package com.linksus.entity;

import java.io.Serializable;

public class LinksusRelationMarketingItem extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** ���� */
	private Long pid;
	/** Ӫ������ID */
	private Long marketingId;
	/** Ӫ������ID */
	private Long userId;
	/** 0��δִ�� 1���ɹ� 2��ʧ�� */
	private Integer status;

	public Long getPid(){
		return pid;
	}

	public void setPid(Long pid){
		this.pid = pid;
	}

	public Long getMarketingId(){
		return marketingId;
	}

	public void setMarketingId(Long marketingId){
		this.marketingId = marketingId;
	}

	public Long getUserId(){
		return userId;
	}

	public void setUserId(Long userId){
		this.userId = userId;
	}

	public Integer getStatus(){
		return status;
	}

	public void setStatus(Integer status){
		this.status = status;
	}
}
