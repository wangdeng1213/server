package com.linksus.entity;

import java.io.Serializable;

public class LinksusRelationPersonOper extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** 主键 */
	private Long pid;
	/** 机构ID */
	private Long institutionId;
	/** 操作类型:1 合并 2 拆分 */
	private Integer operType;
	/** 人员ID:合并的多个ID用|分隔 */
	private String personId;
	/** 拆分说明 */
	private String description;
	/** 状态:1未处理 2已处理 */
	private Integer operStatus;
	/** 更新时间 */
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
