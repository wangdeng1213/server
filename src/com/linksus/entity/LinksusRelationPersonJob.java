package com.linksus.entity;

import java.io.Serializable;

public class LinksusRelationPersonJob extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** ���� */
	private Long pid;
	/** ��ԱID */
	private Long personId;
	/** ����ID */
	private Long institutionId;
	/** �������� */
	private String companyName;
	/** ��ʼ��� */
	private Integer beginYear;
	/** ������� */
	private Integer endYear;
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

	public String getCompanyName(){
		return companyName;
	}

	public void setCompanyName(String companyName){
		this.companyName = companyName;
	}

	public Integer getBeginYear(){
		return beginYear;
	}

	public void setBeginYear(Integer beginYear){
		this.beginYear = beginYear;
	}

	public Integer getEndYear(){
		return endYear;
	}

	public void setEndYear(Integer endYear){
		this.endYear = endYear;
	}

	public Integer getLastUpdtTime(){
		return lastUpdtTime;
	}

	public void setLastUpdtTime(Integer lastUpdtTime){
		this.lastUpdtTime = lastUpdtTime;
	}
}
