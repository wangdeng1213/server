package com.linksus.entity;

public class InterfaceStatistic{

	//�������
	private String taskType;
	//��������
	private String taskName;
	//��ǰ״̬
	private String taskStatus;
	//��������
	private long taskSize = 0;
	//���ִ������
	private long errorCodeCount = 0;
	//�쳣����
	private long errorCount = 0;

	public String getTaskType(){
		return taskType;
	}

	public void setTaskType(String taskType){
		this.taskType = taskType;
	}

	public String getTaskName(){
		return taskName;
	}

	public void setTaskName(String taskName){
		this.taskName = taskName;
	}

	public String getTaskStatus(){
		return taskStatus;
	}

	public void setTaskStatus(String taskStatus){
		this.taskStatus = taskStatus;
	}

	public long getTaskSize(){
		return taskSize;
	}

	public void setTaskSize(long taskSize){
		this.taskSize = taskSize;
	}

	public long getErrorCodeCount(){
		return errorCodeCount;
	}

	public void setErrorCodeCount(long errorCodeCount){
		this.errorCodeCount = errorCodeCount;
	}

	public long getErrorCount(){
		return errorCount;
	}

	public void setErrorCount(long errorCount){
		this.errorCount = errorCount;
	}
}
