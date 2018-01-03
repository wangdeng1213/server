package com.linksus.entity;

import java.io.Serializable;

public class LinksusTaskAttentionUser extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	/**主键id*/
	private Long pid;
	/** 账号ID */
	private Long accountId;
	/** 用户ID */
	private Long userId;
	/** 账号类型:1 新浪 2 腾讯 */
	private Integer accountType;
	/** 处理状态:1 未处理 2 已处理 */
	private Integer status;
	/** 创建时间 */
	private Integer createTime;
	/** 互动时间 */
	private Integer interactTime;

	public Long getPid(){
		return pid;
	}

	public void setPid(Long pid){
		this.pid = pid;
	}

	public Long getAccountId(){
		return accountId;
	}

	public void setAccountId(Long accountId){
		this.accountId = accountId;
	}

	public Long getUserId(){
		return userId;
	}

	public void setUserId(Long userId){
		this.userId = userId;
	}

	public Integer getAccountType(){
		return accountType;
	}

	public void setAccountType(Integer accountType){
		this.accountType = accountType;
	}

	public Integer getStatus(){
		return status;
	}

	public void setStatus(Integer status){
		this.status = status;
	}

	public Integer getCreateTime(){
		return createTime;
	}

	public void setCreateTime(Integer createTime){
		this.createTime = createTime;
	}

	public Integer getInteractTime(){
		return interactTime;
	}

	public void setInteractTime(Integer interactTime){
		this.interactTime = interactTime;
	}
}
