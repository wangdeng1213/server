package com.linksus.entity;

import java.io.Serializable;

public class LinksusRelationPersonTagdef extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** 主键 */
	private Long pid;
	/** 机构ID */
	private Long institutionId;
	/** 标签名称 */
	private String tagName;
	/** 使用次数 */
	private Integer useCount;
	/** 运维人员ID */
	private Long instPersonId;
	/** 创建时间 */
	private Integer createTime;

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

	public String getTagName(){
		return tagName;
	}

	public void setTagName(String tagName){
		this.tagName = tagName;
	}

	public Integer getUseCount(){
		return useCount;
	}

	public void setUseCount(Integer useCount){
		this.useCount = useCount;
	}

	public Long getInstPersonId(){
		return instPersonId;
	}

	public void setInstPersonId(Long instPersonId){
		this.instPersonId = instPersonId;
	}

	public Integer getCreateTime(){
		return createTime;
	}

	public void setCreateTime(Integer createTime){
		this.createTime = createTime;
	}
}
