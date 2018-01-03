package com.linksus.entity;

import java.io.Serializable;

public class LinksusGovMeger extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** ��ˮ�� */
	private Long runId;
	/** ����ˮ�� */
	private Long fatherRunId;
	/** Ψһgid */
	private Integer gid;
	/** �Ƿ���� */
	private Integer isFinish;
	/** ����ʱ�� */
	private Integer createTime;
	/** ����ʱ�� */
	private Integer auditTime;
	/** ��id����gov_tag.tag_id */
	private Integer tagId;

	public Long getRunId(){
		return runId;
	}

	public void setRunId(Long runId){
		this.runId = runId;
	}

	public Long getFatherRunId(){
		return fatherRunId;
	}

	public void setFatherRunId(Long fatherRunId){
		this.fatherRunId = fatherRunId;
	}

	public Integer getGid(){
		return gid;
	}

	public void setGid(Integer gid){
		this.gid = gid;
	}

	public Integer getIsFinish(){
		return isFinish;
	}

	public void setIsFinish(Integer isFinish){
		this.isFinish = isFinish;
	}

	public Integer getCreateTime(){
		return createTime;
	}

	public void setCreateTime(Integer createTime){
		this.createTime = createTime;
	}

	public Integer getAuditTime(){
		return auditTime;
	}

	public void setAuditTime(Integer auditTime){
		this.auditTime = auditTime;
	}

	public Integer getTagId(){
		return tagId;
	}

	public void setTagId(Integer tagId){
		this.tagId = tagId;
	}
}
