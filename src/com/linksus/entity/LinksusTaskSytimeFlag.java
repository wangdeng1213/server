package com.linksus.entity;

import java.io.Serializable;

public class LinksusTaskSytimeFlag extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** 任务类型:1:话题同步 */
	private Integer taskType;
	/** 最大发布时间 */
	private Integer lastSendTime;

	public Integer getTaskType(){
		return taskType;
	}

	public void setTaskType(Integer taskType){
		this.taskType = taskType;
	}

	public Integer getLastSendTime(){
		return lastSendTime;
	}

	public void setLastSendTime(Integer lastSendTime){
		this.lastSendTime = lastSendTime;
	}
}
