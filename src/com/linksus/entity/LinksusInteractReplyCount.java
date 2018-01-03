package com.linksus.entity;

import java.io.Serializable;

public class LinksusInteractReplyCount extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** ���� */
	private Long pid;
	/** ���ûظ�ID */
	private Long replyId;
	/** ����ID */
	private Long institutionId;
	/** ʹ�ô��� */
	private Integer useCount;
	/** ���ʹ��ʱ�� */
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
