package com.linksus.common.config;

public class DataConfig{

	private String queueName;
	private String operType;
	private String className;
	private String commitNum;
	private String commitInterval;
	private boolean redisFlag = false;
	private String hashKey;
	private String fieldKey;
	private String fieldSeparator;
	private String redisValue;
	private String valueSeparator;

	public String getQueueName(){
		return queueName;
	}

	public void setQueueName(String queueName){
		this.queueName = queueName;
	}

	public String getOperType(){
		return operType;
	}

	public void setOperType(String operType){
		this.operType = operType;
	}

	public String getClassName(){
		return className;
	}

	public void setClassName(String className){
		this.className = className;
	}

	public String getCommitNum(){
		return commitNum;
	}

	public void setCommitNum(String commitNum){
		this.commitNum = commitNum;
	}

	public String getCommitInterval(){
		return commitInterval;
	}

	public void setCommitInterval(String commitInterval){
		this.commitInterval = commitInterval;
	}

	public String getHashKey(){
		return hashKey;
	}

	public void setHashKey(String hashKey){
		this.hashKey = hashKey;
	}

	public String getFieldKey(){
		return fieldKey;
	}

	public void setFieldKey(String fieldKey){
		this.fieldKey = fieldKey;
	}

	public String getRedisValue(){
		return redisValue;
	}

	public void setRedisValue(String redisValue){
		this.redisValue = redisValue;
	}

	public boolean isRedisFlag(){
		return redisFlag;
	}

	public void setRedisFlag(boolean redisFlag){
		this.redisFlag = redisFlag;
	}

	public String getFieldSeparator(){
		return fieldSeparator;
	}

	public void setFieldSeparator(String fieldSeparator){
		this.fieldSeparator = fieldSeparator;
	}

	public String getValueSeparator(){
		return valueSeparator;
	}

	public void setValueSeparator(String valueSeparator){
		this.valueSeparator = valueSeparator;
	}
}
