package com.linksus.entity;

public class TaskStatistic{

	//�������
	private String taskType;
	//��������
	private String taskName;
	//��ǰ״̬
	private String taskStatus;
	//����ѭ������
	private long taskCount = 0;
	//����ѭ�����
	private String taskInterval;
	//��������
	private long taskSize = 0;
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

	public long getErrorCount(){
		return errorCount;
	}

	public void setErrorCount(long errorCount){
		this.errorCount = errorCount;
	}

	public long getTaskCount(){
		return taskCount;
	}

	public void setTaskCount(long taskCount){
		this.taskCount = taskCount;
	}

	public String getTaskInterval(){
		return taskInterval;
	}

	public void setTaskInterval(String taskInterval){
		this.taskInterval = taskInterval;
	}

}
