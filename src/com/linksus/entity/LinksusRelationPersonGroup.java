package com.linksus.entity;

import java.io.Serializable;

public class LinksusRelationPersonGroup extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** 用户ID */
	private Long personId;
	/** 分组ID */
	private Long groupId;
	/** 分组来源: 1 人工添加 2:自动筛选 */
	private Integer groupSource;
	/** 更新时间 */
	private Integer lastUpdtTime;

	public Long getPersonId(){
		return personId;
	}

	public void setPersonId(Long personId){
		this.personId = personId;
	}

	public Long getGroupId(){
		return groupId;
	}

	public void setGroupId(Long groupId){
		this.groupId = groupId;
	}

	public Integer getGroupSource(){
		return groupSource;
	}

	public void setGroupSource(Integer groupSource){
		this.groupSource = groupSource;
	}

	public Integer getLastUpdtTime(){
		return lastUpdtTime;
	}

	public void setLastUpdtTime(Integer lastUpdtTime){
		this.lastUpdtTime = lastUpdtTime;
	}
}
