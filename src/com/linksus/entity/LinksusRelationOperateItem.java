package com.linksus.entity;

import java.io.Serializable;

public class LinksusRelationOperateItem extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** ���� */
	private Long pid;
	/** ��ϵ����ID */
	private Long operateId;
	/** ��˿�û�ID ����΢����id���� */
	private Long userId;
	/** 1 δִ�� 2 ִ��ʧ�� 3 ִ�гɹ� */
	private Long taskStatus;

	public Long getPid(){
		return pid;
	}

	public void setPid(Long pid){
		this.pid = pid;
	}

	public Long getOperateId(){
		return operateId;
	}

	public void setOperateId(Long operateId){
		this.operateId = operateId;
	}

	public Long getUserId(){
		return userId;
	}

	public void setUserId(Long userId){
		this.userId = userId;
	}

	public Long getTaskStatus(){
		return taskStatus;
	}

	public void setTaskStatus(Long taskStatus){
		this.taskStatus = taskStatus;
	}
}
