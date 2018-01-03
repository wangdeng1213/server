package com.linksus.entity;

import java.io.Serializable;

public class LinksusInteractReplyCount extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** 主键 */
	private Long pid;
	/** 常用回复ID */
	private Long replyId;
	/** 机构ID */
	private Long institutionId;
	/** 使用次数 */
	private Integer useCount;
	/** 最近使用时间 */
	private Integer lastUseTime;

	public Long getPid(){
		return pid;
	}

	public void setPid(Long pid){
		this.pid = pid;
	}

	public Long getReplyId(){
		return replyId;
	}

	public void setReplyId(Long replyId){
		this.replyId = replyId;
	}

	public Long getInstitutionId(){
		return institutionId;
	}

	public void setInstitutionId(Long institutionId){
		this.institutionId = institutionId;
	}

	public Integer getUseCount(){
		return useCount;
	}

	public void setUseCount(Integer useCount){
		this.useCount = useCount;
	}

	public Integer getLastUseTime(){
		return lastUseTime;
	}

	public void setLastUseTime(Integer lastUseTime){
		this.lastUseTime = lastUseTime;
	}
}
