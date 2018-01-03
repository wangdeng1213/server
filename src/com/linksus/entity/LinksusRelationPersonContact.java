package com.linksus.entity;

import java.io.Serializable;

public class LinksusRelationPersonContact extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** ���� */
	private Long pid;
	/** ��ԱID */
	private Long personId;
	/** ����ID */
	private Long institutionId;
	/** ��ϵ��ʽ����:1 �ƶ��绰 2 �̶��绰 2 �칫�绰 4 ��ͥ�绰 5 ������� 6 �������� 10 �������� 11 ˽������ 12 �������� */
	private Integer contactType;
	/** ��ϵ��ʽ:�̶��绰��ֻ�����-�ָ� */
	private String contact;
	/** �Ƿ�Ĭ�Ϸ�ʽ:�����ʼ����� 0 �� 1 �� */
	private Integer defaultFlag;
	/** ����ʱ�� */
	private Integer lastUpdtTime;

	public Long getPid(){
		return pid;
	}

	public void setPid(Long pid){
		this.pid = pid;
	}

	public Long getPersonId(){
		return personId;
	}

	public void setPersonId(Long personId){
		this.personId = personId;
	}

	public Long getInstitutionId(){
		return institutionId;
	}

	public void setInstitutionId(Long institutionId){
		this.institutionId = institutionId;
	}

	public Integer getContactType(){
		return contactType;
	}

	public void setContactType(Integer contactType){
		this.contactType = contactType;
	}

	public String getContact(){
		return contact;
	}

	public void setContact(String contact){
		this.contact = contact;
	}

	public Integer getDefaultFlag(){
		return defaultFlag;
	}

	public void setDefaultFlag(Integer defaultFlag){
		this.defaultFlag = defaultFlag;
	}

	public Integer getLastUpdtTime(){
		return lastUpdtTime;
	}

	public void setLastUpdtTime(Integer lastUpdtTime){
		this.lastUpdtTime = lastUpdtTime;
	}
}
