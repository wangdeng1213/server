package com.linksus.entity;

import java.io.Serializable;

public class LinksusRelationPersonTag extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** 标签ID */
	private Long tagId;
	/** 人员ID */
	private Long personId;
	/** 创建时间 */
	private Integer createTime;

	public Long getTagId(){
		return tagId;
	}

	public void setTagId(Long tagId){
		this.tagId = tagId;
	}

	public Long getPersonId(){
		return personId;
	}

	public void setPersonId(Long personId){
		this.personId = personId;
	}

	public Integer getCreateTime(){
		return createTime;
	}

	public void setCreateTime(Integer createTime){
		this.createTime = createTime;
	}
}
