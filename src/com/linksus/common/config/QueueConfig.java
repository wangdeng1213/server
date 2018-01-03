package com.linksus.common.config;

import java.util.Map;

public class QueueConfig{

	private String taskName;
	private String className;
	private String taskType;
	private String status;
	private String assignCount;
	private String pageSize;
	private String queueInterval;
	private String taskInterval;
	private String taskCountPerHour;
	private String taskCountPerDay;
	private Map paramsMap;

	public String getTaskName(){
		return taskName;
	}

	public void setTaskName(String taskName){
		this.taskName = taskName;
	}

	public String getTaskType(){
		return taskType;
	}

	public void setTaskType(String taskType){
		this.taskType = taskType;
	}

	public String getClassName(){
		return className;
	}

	public void setClassName(String className){
		this.className = className;
	}

	public Map getParamsMap(){
		return paramsMap;
	}

	public void setParamsMap(Map paramsMap){
		this.paramsMap = paramsMap;
	}

	public String getStatus(){
		return status;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getAssignCount(){
		return assignCount;
	}

	public void setAssignCount(String assignCount){
		this.assignCount = assignCount;
	}

	public String getPageSize(){
		return pageSize;
	}

	public void setPageSize(String pageSize){
		this.pageSize = pageSize;
	}

	public String getQueueInterval(){
		return queueInterval;
	}

	public void setQueueInterval(String queueInterval){
		this.queueInterval = queueInterval;
	}

	public String getTaskInterval(){
		return taskInterval;
	}

	public void setTaskInterval(String taskInterval){
		this.taskInterval = taskInterval;
	}

	public String getTaskCountPerHour(){
		return taskCountPerHour;
	}

	public void setTaskCountPerHour(String taskCountPerHour){
		this.taskCountPerHour = taskCountPerHour;
	}

	public String getTaskCountPerDay(){
		return taskCountPerDay;
	}

	public void setTaskCountPerDay(String taskCountPerDay){
		this.taskCountPerDay = taskCountPerDay;
	}

	@Override
	public int hashCode(){
		final int prime = 31;
		int result = 1;
		result = prime * result + ((assignCount == null) ? 0 : assignCount.hashCode());
		result = prime * result + ((className == null) ? 0 : className.hashCode());
		result = prime * result + ((pageSize == null) ? 0 : pageSize.hashCode());
		result = prime * result + ((paramsMap == null) ? 0 : paramsMap.hashCode());
		result = prime * result + ((queueInterval == null) ? 0 : queueInterval.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((taskCountPerDay == null) ? 0 : taskCountPerDay.hashCode());
		result = prime * result + ((taskCountPerHour == null) ? 0 : taskCountPerHour.hashCode());
		result = prime * result + ((taskInterval == null) ? 0 : taskInterval.hashCode());
		result = prime * result + ((taskName == null) ? 0 : taskName.hashCode());
		result = prime * result + ((taskType == null) ? 0 : taskType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj){
		if(this == obj){
			return true;
		}
		if(obj == null){
			return false;
		}
		if(getClass() != obj.getClass()){
			return false;
		}
		QueueConfig other = (QueueConfig) obj;
		if(assignCount == null){
			if(other.assignCount != null){
				return false;
			}
		}else if(!assignCount.equals(other.assignCount)){
			return false;
		}
		if(className == null){
			if(other.className != null){
				return false;
			}
		}else if(!className.equals(other.className)){
			return false;
		}
		if(pageSize == null){
			if(other.pageSize != null){
				return false;
			}
		}else if(!pageSize.equals(other.pageSize)){
			return false;
		}
		if(paramsMap == null){
			if(other.paramsMap != null){
				return false;
			}
		}else if(!paramsMap.equals(other.paramsMap)){
			return false;
		}
		if(queueInterval == null){
			if(other.queueInterval != null){
				return false;
			}
		}else if(!queueInterval.equals(other.queueInterval)){
			return false;
		}
		if(status == null){
			if(other.status != null){
				return false;
			}
		}else if(!status.equals(other.status)){
			return false;
		}
		if(taskCountPerDay == null){
			if(other.taskCountPerDay != null){
				return false;
			}
		}else if(!taskCountPerDay.equals(other.taskCountPerDay)){
			return false;
		}
		if(taskCountPerHour == null){
			if(other.taskCountPerHour != null){
				return false;
			}
		}else if(!taskCountPerHour.equals(other.taskCountPerHour)){
			return false;
		}
		if(taskInterval == null){
			if(other.taskInterval != null){
				return false;
			}
		}else if(!taskInterval.equals(other.taskInterval)){
			return false;
		}
		if(taskName == null){
			if(other.taskName != null){
				return false;
			}
		}else if(!taskName.equals(other.taskName)){
			return false;
		}
		if(taskType == null){
			if(other.taskType != null){
				return false;
			}
		}else if(!taskType.equals(other.taskType)){
			return false;
		}
		return true;
	}
}
