package com.linksus.entity;

import java.io.Serializable;

public class LinksusRelationUserCompany extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** ���� */
	private Long pid;
	/** ΢���û�ID */
	private Long userId;
	/** ΢���˺�ID */
	private String openid;
	/** ��ʼ�� */
	private Integer beginYear;
	/** ������ */
	private Integer endYear;
	/** ��˾���� */
	private String companyName;
	/** �������� */
	private String departmentName;
	/** ��˾ID */
	private String companyId;
	/** ���ʱ�� */
	private Integer addTime;

	public Long getPid(){
		return pid;
	}

	public void setPid(Long pid){
		this.pid = pid;
	}

	public Long getUserId(){
		return userId;
	}

	public void setUserId(Long userId){
		this.userId = userId;
	}

	public String getOpenid(){
		return openid;
	}

	public void setOpenid(String openid){
		this.openid = openid;
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

	public String getCompanyName(){
		return companyName;
	}

	public void setCompanyName(String companyName){
		this.companyName = companyName;
	}

	public String getDepartmentName(){
		return departmentName;
	}

	public void setDepartmentName(String departmentName){
		this.departmentName = departmentName;
	}

	public String getCompanyId(){
		return companyId;
	}

	public void setCompanyId(String companyId){
		this.companyId = companyId;
	}

	public Integer getAddTime(){
		return addTime;
	}

	public void setAddTime(Integer addTime){
		this.addTime = addTime;
	}
}
