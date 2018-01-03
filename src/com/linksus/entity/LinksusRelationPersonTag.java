package com.linksus.entity;

import java.io.Serializable;

public class LinksusRelationPersonTag extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** ��ǩID */
	private Long tagId;
	/** ��ԱID */
	private Long personId;
	/** ����ʱ�� */
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
