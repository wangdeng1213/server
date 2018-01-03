package com.linksus.entity;

import java.io.Serializable;

public class LinksusRelationUserCompany extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** 主键 */
	private Long pid;
	/** 微博用户ID */
	private Long userId;
	/** 微博账号ID */
	private String openid;
	/** 开始年 */
	private Integer beginYear;
	/** 结束年 */
	private Integer endYear;
	/** 公司名称 */
	private String companyName;
	/** 部门名称 */
	private String departmentName;
	/** 公司ID */
	private String companyId;
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
