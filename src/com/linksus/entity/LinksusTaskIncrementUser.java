package com.linksus.entity;

import java.io.Serializable;

public class LinksusTaskIncrementUser extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** �˺�ID */
	private Long accountId;
	/** �˺�����: 1 ���� 2 ��Ѷ 3 ΢�� */
	private Integer appType;
	/** ����ID */
	private Long institutionId;
	/** ��ʼ����˿�� */
	private Integer initialFansNum;
	/** �ϴθ��º��¼�� */
	private Integer lastFansNum;
	/** �ϴθ��·�˿ʱ�� */
	private Integer updtFansTime;
	/** ��ʼ����ע�� */
	private Integer initialFollowNum;
	/** �ϴθ��º��ע�� */
	private Integer lastFollowNum;
	/** �ϴθ��¹�ע�¼� */
	private Integer updtFollowTime;
	/** �Ƿ�ƽ̨�û� */
	private Integer isSystemUser;
	//��ȡ�˺ŵ�rps_id
	private String rpsId;
	//ƽ̨�˺�token
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
