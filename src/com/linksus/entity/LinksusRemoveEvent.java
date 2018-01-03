package com.linksus.entity;

import java.io.Serializable;

public class LinksusRemoveEvent extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** ��id */
	private Long id;
	/** �˻�id */
	private Long institutionId;
	/** �ʺ�id */
	private Long accountId;
	/** ��Դid */
	private Long sourceId;
	/** Ŀ��id
	        ΢��id or
	        ΢��id �� */
	private Long destId;
	/** 1΢��2΢��3����������������� */
	private Integer sysType;
	/** ����
	        1����
	        2��Ѷ
	        3΢�� */
	private Integer type;
	/** ״̬λ
	        0δִ��
	        1ִ�г¹�
	        2ִ��ʧ�� */
	private Integer status;
	/** ����ʱ�� */
	private Integer createdTime;
	/** ����ʱ�� */
	private Integer operateTime;
	/** ִ������ */
	private String execCmd;

	private String token;
	private String appid;
	private Long mid;

	public Long getId(){
		return id;
	}

	public void setId(Long id){
		this.id = id;
	}

	public Long getInstitutionId(){
		return institutionId;
	}

	public void setInstitutionId(Long institutionId){
		this.institutionId = institutionId;
	}

	public Long getAccountId(){
		return accountId;
	}

	public void setAccountId(Long accountId){
		this.accountId = accountId;
	}

	public Long getSourceId(){
		return sourceId;
	}

	public void setSourceId(Long sourceId){
		this.sourceId = sourceId;
	}

	public Long getDestId(){
		return destId;
	}

	public void setDestId(Long destId){
		this.destId = destId;
	}

	public Integer getSysType(){
		return sysType;
	}

	public void setSysType(Integer sysType){
		this.sysType = sysType;
	}

	public Integer getType(){
		return type;
	}

	public void setType(Integer type){
		this.type = type;
	}

	public Integer getStatus(){
		return status;
	}

	public void setStatus(Integer status){
		this.status = status;
	}

	public Integer getCreatedTime(){
		return createdTime;
	}

	public void setCreatedTime(Integer createdTime){
		this.createdTime = createdTime;
	}

	public Integer getOperateTime(){
		return operateTime;
	}

	public void setOperateTime(Integer operateTime){
		this.operateTime = operateTime;
	}

	public String getExecCmd(){
		return execCmd;
	}

	public void setExecCmd(String execCmd){
		this.execCmd = execCmd;
	}

	public String getToken(){
		return token;
	}

	public void setToken(String token){
		this.token = token;
	}

	public String getAppid(){
		return appid;
	}

	public void setAppid(String appid){
		this.appid = appid;
	}

	public Long getMid(){
		return mid;
	}

	public void setMid(Long mid){
		this.mid = mid;
	}

}
