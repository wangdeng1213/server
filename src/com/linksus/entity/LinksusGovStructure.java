package com.linksus.entity;

import java.io.Serializable;

public class LinksusGovStructure extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** 组织id，标示处于哪个组织下 */
	private Integer orgId;
	/** 账号id */
	private Integer accountId;
	/** 唯一id，确定一条记录 */
	private Integer gid;
	/** 账号名称 */
	private String accountName;
	/** 层级id，标示处在组织中的哪个层级 */
	private Integer levelId;
	/** 上级id，标示上级是谁 */
	private Integer fatherId;
	/** 机构id和3.0linksus_institution.id 关联 */
	private Long institutionId;
	/** 第三方平台唯一id,和linksus_appaccount.appid关联 */
	private String appid;
	/** 组织名称 */
	private String orgName;
	/** 最后更新时间 */
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
