package com.linksus.entity;

public class InterfaceStatistic{

	//任务代码
	private String taskType;
	//任务名称
	private String taskName;
	//当前状态
	private String taskStatus;
	//调用总数
	private long taskSize = 0;
	//出现错误次数
	private long errorCodeCount = 0;
	//异常个数
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
