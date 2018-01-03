package com.linksus.entity;

import java.io.Serializable;

public class LinksusRelationOperate extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** ���� */
	private Long pid;
	/** �˺�ID */
	private Long accountId;
	/** �˺�����:1 ���� 2 ��Ѷ 3΢�� */
	private Integer accountType;
	/** �������� 1 ��ӹ�ע 2 ȡ����ע 3 �Ƴ���˿ */
	private Long taskType;
	/** �����ɹ����� */
	private Integer operSuccessNum;
	/** ����ʧ������ */
	private Integer operFailNum;
	/** 1 δ���� 2 ������ͨ�� 3����ͨ�� */
	private Integer status;
	/** ��ǰ��˵�״̬ 00 δ���� 10 һ��������� 11 һ�� ������������� */
	private Integer applyStatus;

	public Long getPid(){
		return pid;
	}

	public void setPid(Long pid){
		this.pid = pid;
	}

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

	public Long getTaskType(){
		return taskType;
	}

	public void setTaskType(Long taskType){
		this.taskType = taskType;
	}

	public Integer getOperSuccessNum(){
		return operSuccessNum;
	}

	public void setOperSuccessNum(Integer operSuccessNum){
		this.operSuccessNum = operSuccessNum;
	}

	public Integer getOperFailNum(){
		return operFailNum;
	}

	public void setOperFailNum(Integer operFailNum){
		this.operFailNum = operFailNum;
	}

	public Integer getStatus(){
		return status;
	}

	public void setStatus(Integer status){
		this.status = status;
	}

	public Integer getApplyStatus(){
		return applyStatus;
	}

	public void setApplyStatus(Integer applyStatus){
		this.applyStatus = applyStatus;
	}
}
