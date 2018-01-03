package com.linksus.entity;

import java.io.Serializable;

public class LinksusRelationPersonContact extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** 主键 */
	private Long pid;
	/** 人员ID */
	private Long personId;
	/** 机构ID */
	private Long institutionId;
	/** 联系方式类型:1 移动电话 2 固定电话 2 办公电话 4 家庭电话 5 传真号码 6 其他号码 10 工作邮箱 11 私人邮箱 12 其他邮箱 */
	private Integer contactType;
	/** 联系方式:固定电话与分机号用-分隔 */
	private String contact;
	/** 是否默认方式:短信邮件发送 0 否 1 是 */
	private Integer defaultFlag;
	/** 更新时间 */
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
