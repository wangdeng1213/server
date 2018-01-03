package com.linksus.entity;

import java.io.Serializable;

public class LinksusTaskTotalUser extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** �˻�ID */
	private Long accountId;
	/** �˺�����: 1 ���� */
	private Integer accountType;
	/** ��������: 1 ȫ����˿ 2 ȫ����ע */
	private Integer taskType;
	/** ����ID */
	private Long institutionId;
	/** ����״̬ 0��δִ�� 1���ɹ� 2��ʧ�� */
	private Integer status;
	/** �´�max_timeֵ:��ֹ�����ж�,֧�ָֻ� */
	private Long nextCursor;
	/** �ϴθ���ʱ�� */
	private Integer lastUpdtTime;

	public Long getAccountId(){
		return accountId;
	}

	public void setAccountId(Long accountId){
		this.accountId = accountId;
	}

	public Integer getAccountType(){
		return accountType;
	}

	public void setAccountType(Integer accountType){
		this.accountType = accountType;
	}

	public Integer getTaskType(){
		return taskType;
	}

	public void setTaskType(Integer taskType){
		this.taskType = taskType;
	}

	public Long getInstitutionId(){
		return institutionId;
	}

	public void setInstitutionId(Long institutionId){
		this.institutionId = institutionId;
	}

	public Integer getStatus(){
		return status;
	}

	public void setStatus(Integer status){
		this.status = status;
	}

	public Long getNextCursor(){
		return nextCursor;
	}

	public void setNextCursor(Long nextCursor){
		this.nextCursor = nextCursor;
	}

	public Integer getLastUpdtTime(){
		return lastUpdtTime;
	}

	public void setLastUpdtTime(Integer lastUpdtTime){
		this.lastUpdtTime = lastUpdtTime;
	}
}
