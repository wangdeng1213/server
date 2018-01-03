package com.linksus.entity;

import java.io.Serializable;

public class LinksusRelationUserEdu extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** ���� */
	private Long pid;
	/** �û�ID */
	private Long userId;
	/** ΢���˺�ID */
	private String openid;
	/** ԺϵID */
	private String departmentid;
	/** ������Ϣ��¼ID */
	private String id;
	/** ѧ������ */
	private String level;
	/** ѧУID */
	private Integer schoolid;
	/** ��ѧ�� */
	private Integer year;
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

	public String getDepartmentid(){
		return departmentid;
	}

	public void setDepartmentid(String departmentid){
		this.departmentid = departmentid;
	}

	public String getId(){
		return id;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getLevel(){
		return level;
	}

	public void setLevel(String level){
		this.level = level;
	}

	public Integer getSchoolid(){
		return schoolid;
	}

	public void setSchoolid(Integer schoolid){
		this.schoolid = schoolid;
	}

	public Integer getYear(){
		return year;
	}

	public void setYear(Integer year){
		this.year = year;
	}

	public Integer getAddTime(){
		return addTime;
	}

	public void setAddTime(Integer addTime){
		this.addTime = addTime;
	}
}
