package com.linksus.entity;

import java.io.Serializable;

public class LinksusRelationUserEdu extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** 主键 */
	private Long pid;
	/** 用户ID */
	private Long userId;
	/** 微博账号ID */
	private String openid;
	/** 院系ID */
	private String departmentid;
	/** 教育信息记录ID */
	private String id;
	/** 学历级别 */
	private String level;
	/** 学校ID */
	private Integer schoolid;
	/** 入学年 */
	private Integer year;
	/** 添加时间 */
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
