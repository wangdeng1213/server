package com.linksus.entity;

import java.io.Serializable;

public class LinksusRelationPersonGroup extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** �û�ID */
	private Long personId;
	/** ����ID */
	private Long groupId;
	/** ������Դ: 1 �˹���� 2:�Զ�ɸѡ */
	private Integer groupSource;
	/** ����ʱ�� */
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
