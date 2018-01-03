package com.linksus.entity;

import java.io.Serializable;

public class LinksusTaskIncrementUser extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** 账号ID */
	private Long accountId;
	/** 账号类型: 1 新浪 2 腾讯 3 微信 */
	private Integer appType;
	/** 机构ID */
	private Long institutionId;
	/** 初始化粉丝数 */
	private Integer initialFansNum;
	/** 上次更新后记录数 */
	private Integer lastFansNum;
	/** 上次更新粉丝时间 */
	private Integer updtFansTime;
	/** 初始化关注数 */
	private Integer initialFollowNum;
	/** 上次更新后关注数 */
	private Integer lastFollowNum;
	/** 上次更新关注事件 */
	private Integer updtFollowTime;
	/** 是否平台用户 */
	private Integer isSystemUser;
	//获取账号的rps_id
	private String rpsId;
	//平台账号token
	private String token;

	public String getToken(){
		return token;
	}

	public void setToken(String token){
		this.token = token;
	}

	public String getRpsId(){
		return rpsId;
	}

	public void setRpsId(String rpsId){
		this.rpsId = rpsId;
	}

	public Long getAccountId(){
		return accountId;
	}

	public void setAccountId(Long accountId){
		this.accountId = accountId;
	}

	public Integer getAppType(){
		return appType;
	}

	public void setAppType(Integer appType){
		this.appType = appType;
	}

	public Long getInstitutionId(){
		return institutionId;
	}

	public void setInstitutionId(Long institutionId){
		this.institutionId = institutionId;
	}

	public Integer getInitialFansNum(){
		return initialFansNum;
	}

	public void setInitialFansNum(Integer initialFansNum){
		this.initialFansNum = initialFansNum;
	}

	public Integer getLastFansNum(){
		return lastFansNum;
	}

	public void setLastFansNum(Integer lastFansNum){
		this.lastFansNum = lastFansNum;
	}

	public Integer getUpdtFansTime(){
		return updtFansTime;
	}

	public void setUpdtFansTime(Integer updtFansTime){
		this.updtFansTime = updtFansTime;
	}

	public Integer getInitialFollowNum(){
		return initialFollowNum;
	}

	public void setInitialFollowNum(Integer initialFollowNum){
		this.initialFollowNum = initialFollowNum;
	}

	public Integer getLastFollowNum(){
		return lastFollowNum;
	}

	public void setLastFollowNum(Integer lastFollowNum){
		this.lastFollowNum = lastFollowNum;
	}

	public Integer getUpdtFollowTime(){
		return updtFollowTime;
	}

	public void setUpdtFollowTime(Integer updtFollowTime){
		this.updtFollowTime = updtFollowTime;
	}

	public Integer getIsSystemUser(){
		return isSystemUser;
	}

	public void setIsSystemUser(Integer isSystemUser){
		this.isSystemUser = isSystemUser;
	}
}
