package com.linksus.entity;

import java.io.Serializable;
import java.util.Date;

public class LinksusTaskConnLimit extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** 主键自增 */
	private Long pid;
	/** 账号ID,新浪微博、腾讯微博的用户标识 */
	private Long appid;
	/** 帐号类型 1新浪 2腾讯 3微信 */
	private Integer type;
	/** 超限时间：比如2013121801,20131218 */
	private String limitDate;
	/** 1:微博每小时 2：评论每小时 3：关注每小时 4：关注每天 */
	private String limitType;
	/** 创建时间 */
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
