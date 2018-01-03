package com.linksus.entity;

import java.io.Serializable;

public class LinksusRelationPersonEdu extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** ���� */
	private Long pid;
	/** ��ԱID */
	private Long personId;
	/** ����ID */
	private Long institutionId;
	/** ��������:1 ��ѧ 2 ��ѧ 3 Сѧ 4 ѧǰ */
	private Integer eduType;
	/** ѧУ���� */
	private String schoolName;
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

	public Integer getEduType(){
		return eduType;
	}

	public void setEduType(Integer eduType){
		this.eduType = eduType;
	}

	public String getSchoolName(){
		return schoolName;
	}

	public void setSchoolName(String schoolName){
		this.schoolName = schoolName;
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
