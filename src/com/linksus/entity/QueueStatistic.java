package com.linksus.entity;

public class QueueStatistic{

	//任务代码
	private String taskType;
	//任务名称
	private String taskName;
	//当前状态
	private String taskStatus;
	//任务循环次数
	private long taskCount = 0;
	//任务循环间隔
	private long taskInterval = 0;
	//任务刷新次数
	private long flushCount = 0;
	//任务总数
	private long taskSize = 0;
	//有任务刷新次数
	private long hasTaskCount = 0;
	//异常个数
	private long errorCount = 0;
	//未刷新服务端任务
	private int noFlushFlag = 0;

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

	public long getFlushCount(){
		return flushCount;
	}

	public void setFlushCount(long flushCount){
		this.flushCount = flushCount;
	}

	public long getTaskSize(){
		return taskSize;
	}

	public void setTaskSize(long taskSize){
		this.taskSize = taskSize;
	}

	public long getHasTaskCount(){
		return hasTaskCount;
	}

	public void setHasTaskCount(long hasTaskCount){
		this.hasTaskCount = hasTaskCount;
	}

	public long getErrorCount(){
		return errorCount;
	}

	public void setErrorCount(long errorCount){
		this.errorCount = errorCount;
	}

	public int getNoFlushFlag(){
		return noFlushFlag;
	}

	public void setNoFlushFlag(int noFlushFlag){
		this.noFlushFlag = noFlushFlag;
	}

	public long getTaskCount(){
		return taskCount;
	}

	public void setTaskCount(long taskCount){
		this.taskCount = taskCount;
	}

	public long getTaskInterval(){
		return taskInterval;
	}

	public void setTaskInterval(long taskInterval){
		this.taskInterval = taskInterval;
	}
}
