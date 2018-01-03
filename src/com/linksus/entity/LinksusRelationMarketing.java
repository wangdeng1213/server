package com.linksus.entity;

import java.io.Serializable;

public class LinksusRelationMarketing extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** ���� */
	private Long pid;
	/** ƽ̨�˺�ID */
	private Long accountId;
	/** �ʺ�����:1 ���� 2 ��Ѷ 3 ΢�� */
	private Integer accountType;
	/** ����ID */
	private Long institutionId;
	/** Ӫ������ 0�ҵķ�˿,1Ǳ�ڷ�˿,2�ҵĹ�ע,3�Ƽ���ע */
	private Integer marketingObject;
	/** Ӫ������(1@,2����,3����,4�ʼ�) */
	private Integer marketingType;
	/** Ӫ��΢��ID[1@��΢��ID] */
	private Long weiboId;
	/** Ӫ������[4�ʼ��ı���] */
	private String marketingTitle;
	/** Ӫ������ */
	private String marketingContent;
	/** Ӫ���ɹ����� */
	private Integer marketingSuccessNum;
	/** Ӫ��ʧ������ */
	private Integer marketingFailNum;
	/** 1 δ����  2 ����δͨ��  3  ����ͨ�� */
	private Integer status;
	/** ��ǰ��˵�״̬ 00 δ���� 10 һ��������� 11 һ�� ������������� ���������linksus_apply_operation */
	private String applyStatus;
	/** ����ʱ�� */
	private Long createTime;
	/** ����ʱ�� */
	private Integer updateTime;

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

	public Integer getAccountType(){
		return accountType;
	}

	public void setAccountType(Integer accountType){
		this.accountType = accountType;
	}

	public Long getInstitutionId(){
		return institutionId;
	}

	public void setInstitutionId(Long institutionId){
		this.institutionId = institutionId;
	}

	public Integer getMarketingObject(){
		return marketingObject;
	}

	public void setMarketingObject(Integer marketingObject){
		this.marketingObject = marketingObject;
	}

	public Integer getMarketingType(){
		return marketingType;
	}

	public void setMarketingType(Integer marketingType){
		this.marketingType = marketingType;
	}

	public Long getWeiboId(){
		return weiboId;
	}

	public void setWeiboId(Long weiboId){
		this.weiboId = weiboId;
	}

	public String getMarketingTitle(){
		return marketingTitle;
	}

	public void setMarketingTitle(String marketingTitle){
		this.marketingTitle = marketingTitle;
	}

	public String getMarketingContent(){
		return marketingContent;
	}

	public void setMarketingContent(String marketingContent){
		this.marketingContent = marketingContent;
	}

	public Integer getMarketingSuccessNum(){
		return marketingSuccessNum;
	}

	public void setMarketingSuccessNum(Integer marketingSuccessNum){
		this.marketingSuccessNum = marketingSuccessNum;
	}

	public Integer getMarketingFailNum(){
		return marketingFailNum;
	}

	public void setMarketingFailNum(Integer marketingFailNum){
		this.marketingFailNum = marketingFailNum;
	}

	public Integer getStatus(){
		return status;
	}

	public void setStatus(Integer status){
		this.status = status;
	}

	public String getApplyStatus(){
		return applyStatus;
	}

	public void setApplyStatus(String applyStatus){
		this.applyStatus = applyStatus;
	}

	public Long getCreateTime(){
		return createTime;
	}

	public void setCreateTime(Long createTime){
		this.createTime = createTime;
	}

	public Integer getUpdateTime(){
		return updateTime;
	}

	public void setUpdateTime(Integer updateTime){
		this.updateTime = updateTime;
	}
}
