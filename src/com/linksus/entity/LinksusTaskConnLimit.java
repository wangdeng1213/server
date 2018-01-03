package com.linksus.entity;

import java.io.Serializable;
import java.util.Date;

public class LinksusTaskConnLimit extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** �������� */
	private Long pid;
	/** �˺�ID,����΢������Ѷ΢�����û���ʶ */
	private Long appid;
	/** �ʺ����� 1���� 2��Ѷ 3΢�� */
	private Integer type;
	/** ����ʱ�䣺����2013121801,20131218 */
	private String limitDate;
	/** 1:΢��ÿСʱ 2������ÿСʱ 3����עÿСʱ 4����עÿ�� */
	private String limitType;
	/** ����ʱ�� */
	private Date createTime;

	public Long getPid(){
		return pid;
	}

	public void setPid(Long pid){
		this.pid = pid;
	}

	public Long getAppid(){
		return appid;
	}

	public void setAppid(Long appid){
		this.appid = appid;
	}

	public Integer getType(){
		return type;
	}

	public void setType(Integer type){
		this.type = type;
	}

	public String getLimitDate(){
		return limitDate;
	}

	public void setLimitDate(String limitDate){
		this.limitDate = limitDate;
	}

	public String getLimitType(){
		return limitType;
	}

	public void setLimitType(String limitType){
		this.limitType = limitType;
	}

	public Date getCreateTime(){
		return createTime;
	}

	public void setCreateTime(Date createTime){
		this.createTime = createTime;
	}
}
