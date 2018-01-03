package com.linksus.entity;

import java.io.Serializable;

public class LinksusRelationPersonOper extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** ���� */
	private Long pid;
	/** ����ID */
	private Long institutionId;
	/** ��������:1 �ϲ� 2 ��� */
	private Integer operType;
	/** ��ԱID:�ϲ��Ķ��ID��|�ָ� */
	private String personId;
	/** ���˵�� */
	private String description;
	/** ״̬:1δ���� 2�Ѵ��� */
	private Integer operStatus;
	/** ����ʱ�� */
	private Integer lastUpdtTime;

	public Long getPid(){
		return pid;
	}

	public void setPid(Long pid){
		this.pid = pid;
	}

	public Long getInstitutionId(){
		return institutionId;
	}

	public void setInstitutionId(Long institutionId){
		this.institutionId = institutionId;
	}

	public Integer getOperType(){
		return operType;
	}

	public void setOperType(Integer operType){
		this.operType = operType;
	}

	public String getPersonId(){
		return personId;
	}

	public void setPersonId(String personId){
		this.personId = personId;
	}

	public String getDescription(){
		return description;
	}

	public void setDescription(String description){
		this.description = description;
	}

	public Integer getOperStatus(){
		return operStatus;
	}

	public void setOperStatus(Integer operStatus){
		this.operStatus = operStatus;
	}

	public Integer getLastUpdtTime(){
		return lastUpdtTime;
	}

	public void setLastUpdtTime(Integer lastUpdtTime){
		this.lastUpdtTime = lastUpdtTime;
	}
}
