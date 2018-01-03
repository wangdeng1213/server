package com.linksus.entity;

import java.io.Serializable;

public class LinksusRelationMarketingItem extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** 主键 */
	private Long pid;
	/** 营销主表ID */
	private Long marketingId;
	/** 营销对象ID */
	private Long userId;
	/** 0：未执行 1：成功 2：失败 */
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
