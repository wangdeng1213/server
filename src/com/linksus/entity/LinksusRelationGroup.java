package com.linksus.entity;

import java.io.Serializable;

public class LinksusRelationGroup extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** 分组ID */
	private Long groupId;
	/** 机构ID */
	private Long institutionId;
	/** 分组名称 */
	private String groupName;
	/** 是否黑名单组:1 是,0 否 */
	private Integer groupType;
	/** 账号类型 */
	private Integer accountType;
	/** 账号ID 关联linksus_appaccount主键 */
	private Long accountId;
	/** 添加关注的日期:范围的以|分隔 */
	private String addDate;
	/** 粉丝数:范围的以|分隔 */
	private String fansNum;
	/** 腾讯身份:多个以|分隔 */
	private String accountIdQq;
	/** 新浪身份:多个以|分隔 */
	private String accountIdSina;
	/** 用户性别 多个以|分隔 */
	private String rpsGender;
	/** 用户质量 多个以|分隔 */
	private String fansQuality;
	/** 互动方式:评论等 多个以|分隔 */
	private String relationType;
	/** 关系来源:舆情推送 多个以|分隔 */
	private String relationSource;
	/** 地域代码:单选 */
	private Long areaCode;
	/** 执行状态:1 未执行 3 执行中 2  已执行(除自定义分组外,其他类型只有2类型) */
	private Integer status;
	/** 更新时间 */
	private Integer lastUpdateTime;

	public Long getGroupId(){
		return groupId;
	}

	public void setGroupId(Long groupId){
		this.groupId = groupId;
	}

	public Long getInstitutionId(){
		return institutionId;
	}

	public void setInstitutionId(Long institutionId){
		this.institutionId = institutionId;
	}

	public String getGroupName(){
		return groupName;
	}

	public void setGroupName(String groupName){
		this.groupName = groupName;
	}

	public Integer getAccountType(){
		return accountType;
	}

	public void setAccountType(Integer accountType){
		this.accountType = accountType;
	}

	public Long getAccountId(){
		return accountId;
	}

	public void setAccountId(Long accountId){
		this.accountId = accountId;
	}

	public String getAddDate(){
		return addDate;
	}

	public void setAddDate(String addDate){
		this.addDate = addDate;
	}

	public String getFansNum(){
		return fansNum;
	}

	public void setFansNum(String fansNum){
		this.fansNum = fansNum;
	}

	public String getRpsGender(){
		return rpsGender;
	}

	public void setRpsGender(String rpsGender){
		this.rpsGender = rpsGender;
	}

	public String getFansQuality(){
		return fansQuality;
	}

	public void setFansQuality(String fansQuality){
		this.fansQuality = fansQuality;
	}

	public String getRelationType(){
		return relationType;
	}

	public void setRelationType(String relationType){
		this.relationType = relationType;
	}

	public String getRelationSource(){
		return relationSource;
	}

	public void setRelationSource(String relationSource){
		this.relationSource = relationSource;
	}

	public Integer getStatus(){
		return status;
	}

	public void setStatus(Integer status){
		this.status = status;
	}

	public Integer getLastUpdateTime(){
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Integer lastUpdateTime){
		this.lastUpdateTime = lastUpdateTime;
	}

	public Integer getGroupType(){
		return groupType;
	}

	public void setGroupType(Integer groupType){
		this.groupType = groupType;
	}

	public String getAccountIdQq(){
		return accountIdQq;
	}

	public void setAccountIdQq(String accountIdQq){
		this.accountIdQq = accountIdQq;
	}

	public String getAccountIdSina(){
		return accountIdSina;
	}

	public void setAccountIdSina(String accountIdSina){
		this.accountIdSina = accountIdSina;
	}

	public Long getAreaCode(){
		return areaCode;
	}

	public void setAreaCode(Long areaCode){
		this.areaCode = areaCode;
	}

}
