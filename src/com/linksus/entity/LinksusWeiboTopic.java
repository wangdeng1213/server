package com.linksus.entity;

import java.io.Serializable;

public class LinksusWeiboTopic extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** ����id */
	private Long id;
	/** �˻�id */
	private Long institutionId;
	/** �������� */
	private String topicName;
	/** ʹ�ô��� */
	private Integer usedNum;
	/** ���ʹ��ʱ�� */
	private Integer lastUsedTime;
	/** ����ʱ�� */
	private Integer createdTime;

	public Long getId(){
		return id;
	}

	public void setId(Long id){
		this.id = id;
	}

	public Long getInstitutionId(){
		return institutionId;
	}

	public void setInstitutionId(Long institutionId){
		this.institutionId = institutionId;
	}

	public String getTopicName(){
		return topicName;
	}

	public void setTopicName(String topicName){
		this.topicName = topicName;
	}

	public Integer getUsedNum(){
		return usedNum;
	}

	public void setUsedNum(Integer usedNum){
		this.usedNum = usedNum;
	}

	public Integer getLastUsedTime(){
		return lastUsedTime;
	}

	public void setLastUsedTime(Integer lastUsedTime){
		this.lastUsedTime = lastUsedTime;
	}

	public Integer getCreatedTime(){
		return createdTime;
	}

	public void setCreatedTime(Integer createdTime){
		this.createdTime = createdTime;
	}
}
