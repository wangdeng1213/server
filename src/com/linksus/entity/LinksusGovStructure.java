package com.linksus.entity;

import java.io.Serializable;

public class LinksusGovStructure extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** ��֯id����ʾ�����ĸ���֯�� */
	private Integer orgId;
	/** �˺�id */
	private Integer accountId;
	/** Ψһid��ȷ��һ����¼ */
	private Integer gid;
	/** �˺����� */
	private String accountName;
	/** �㼶id����ʾ������֯�е��ĸ��㼶 */
	private Integer levelId;
	/** �ϼ�id����ʾ�ϼ���˭ */
	private Integer fatherId;
	/** ����id��3.0linksus_institution.id ���� */
	private Long institutionId;
	/** ������ƽ̨Ψһid,��linksus_appaccount.appid���� */
	private String appid;
	/** ��֯���� */
	private String orgName;
	/** ������ʱ�� */
	private Integer updateTime;

	public Integer getOrgId(){
		return orgId;
	}

	public void setOrgId(Integer orgId){
		this.orgId = orgId;
	}

	public Integer getAccountId(){
		return accountId;
	}

	public void setAccountId(Integer accountId){
		this.accountId = accountId;
	}

	public Integer getGid(){
		return gid;
	}

	public void setGid(Integer gid){
		this.gid = gid;
	}

	public String getAccountName(){
		return accountName;
	}

	public void setAccountName(String accountName){
		this.accountName = accountName;
	}

	public Integer getLevelId(){
		return levelId;
	}

	public void setLevelId(Integer levelId){
		this.levelId = levelId;
	}

	public Integer getFatherId(){
		return fatherId;
	}

	public void setFatherId(Integer fatherId){
		this.fatherId = fatherId;
	}

	public Long getInstitutionId(){
		return institutionId;
	}

	public void setInstitutionId(Long institutionId){
		this.institutionId = institutionId;
	}

	public String getAppid(){
		return appid;
	}

	public void setAppid(String appid){
		this.appid = appid;
	}

	public String getOrgName(){
		return orgName;
	}

	public void setOrgName(String orgName){
		this.orgName = orgName;
	}

	public Integer getUpdateTime(){
		return updateTime;
	}

	public void setUpdateTime(Integer updateTime){
		this.updateTime = updateTime;
	}
}
