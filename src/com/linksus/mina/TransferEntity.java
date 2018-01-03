package com.linksus.mina;

import java.io.Serializable;
import java.util.Map;

public class TransferEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	private String taskType;
	private String requestType;
	private Map transferData;

	public String getTaskType(){
		return taskType;
	}

	public void setTaskType(String taskType){
		this.taskType = taskType;
	}

	public String getRequestType(){
		return requestType;
	}

	public void setRequestType(String requestType){
		this.requestType = requestType;
	}

	public Map getTransferData(){
		return transferData;
	}

	public void setTransferData(Map transferData){
		this.transferData = transferData;
	}
}
