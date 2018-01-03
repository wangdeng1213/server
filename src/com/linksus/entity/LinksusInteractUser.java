package com.linksus.entity;

import java.io.Serializable;

public class LinksusInteractUser extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** 主键 */
	private Long pid;
	/** 人员ID */
	private Long personId;
	/** 平台账号ID */
	private Long accountId;
	/** 互动ID:互动人员表主键 */
	private Long interactId;
	/** 用户ID:微博/微信用户ID */
	private Long userId;
	/** 未处理互动数 */
	private Integer count;
	/** 更新时间 */
	private Integer updateTime;

	public Long getPid(){
		return pid;
	}

	public void setPid(Long pid){
		this.pid = pid;
	}

	public Long getInteractId(){
		return interactId;
	}

	public void setInteractId(Long interactId){
		this.interactId = interactId;
	}

	public Long getUserId(){
		return userId;
	}

	public void setUserId(Long userId){
		this.userId = userId;
	}

	public Integer getCount(){
		return count;
	}

	public void setCount(Integer count){
		this.count = count;
	}

	public Integer getUpdateTime(){
		return updateTime;
	}

	public void setUpdateTime(Integer updateTime){
		this.updateTime = updateTime;
	}

	public Long getPersonId(){
		return personId;
	}

	public void setPersonId(Long personId){
		this.personId = personId;
	}

	public Long getAccountId(){
		return accountId;
	}

	public void setAccountId(Long accountId){
		this.accountId = accountId;
	}
}
