package com.linksus.entity;

import java.io.Serializable;

public class LinksusTaskException extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** ���� */
	private Long pid;
	/** ƽ̨����:1 �ͻ��� 2 ����� */
	private Integer type;
	/** ������Ϣ */
	private String content;
	/** ��ԴIP */
	private String sourceIp;
	/** ����ʱ�� */
	private Integer addTime;

	public Long getPid(){
		return pid;
	}

	public void setPid(Long pid){
		this.pid = pid;
	}

	public Integer getType(){
		return type;
	}

	public void setType(Integer type){
		this.type = type;
	}

	public String getContent(){
		return content;
	}

	public void setContent(String content){
		this.content = content;
	}

	public String getSourceIp(){
		return sourceIp;
	}

	public void setSourceIp(String sourceIp){
		this.sourceIp = sourceIp;
	}

	public Integer getAddTime(){
		return addTime;
	}

	public void setAddTime(Integer addTime){
		this.addTime = addTime;
	}
}
