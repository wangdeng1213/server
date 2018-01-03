package com.linksus.common.config;

import java.util.List;
import java.util.Map;

public class InterfaceConfig{

	private String taskType;
	private String taskName;
	private String className;
	private String methodName;
	private String status;
	private List paramsList;
	private Map fieldsMap;

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

	public String getClassName(){
		return className;
	}

	public void setClassName(String className){
		this.className = className;
	}

	public String getMethodName(){
		return methodName;
	}

	public void setMethodName(String methodName){
		this.methodName = methodName;
	}

	public String getStatus(){
		return status;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public List getParamsList(){
		return paramsList;
	}

	public void setParamsList(List paramsList){
		this.paramsList = paramsList;
	}

	public Map getFieldsMap(){
		return fieldsMap;
	}

	public void setFieldsMap(Map fieldsMap){
		this.fieldsMap = fieldsMap;
	}

	@Override
	public int hashCode(){
		final int prime = 31;
		int result = 1;
		result = prime * result + ((className == null) ? 0 : className.hashCode());
		result = prime * result + ((fieldsMap == null) ? 0 : fieldsMap.hashCode());
		result = prime * result + ((methodName == null) ? 0 : methodName.hashCode());
		result = prime * result + ((paramsList == null) ? 0 : paramsList.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
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
		InterfaceConfig other = (InterfaceConfig) obj;
		if(className == null){
			if(other.className != null){
				return false;
			}
		}else if(!className.equals(other.className)){
			return false;
		}
		if(fieldsMap == null){
			if(other.fieldsMap != null){
				return false;
			}
		}else if(!fieldsMap.equals(other.fieldsMap)){
			return false;
		}
		if(methodName == null){
			if(other.methodName != null){
				return false;
			}
		}else if(!methodName.equals(other.methodName)){
			return false;
		}
		if(paramsList == null){
			if(other.paramsList != null){
				return false;
			}
		}else if(!paramsList.equals(other.paramsList)){
			return false;
		}
		if(status == null){
			if(other.status != null){
				return false;
			}
		}else if(!status.equals(other.status)){
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
