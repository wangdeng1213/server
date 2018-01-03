package com.linksus.entity;

import java.io.Serializable;

public class LinksusRelationUserAccount extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** 主键 */
	private Long pid = 0L;
	/** 平台账号ID */
	private Long accountId = 0L;
	/** 帐号类型:1 新浪 2 腾讯 3 微信 */
	private Integer accountType = 0;
	/** 微博用户ID 管理微博用户表微信用户表主键 */
	private Long userId = 0L;
	/** 机构ID */
	private Long institutionId = 0L;
	/** 人员ID */
	private Long personId = 0L;
	/** 1：我的粉丝2：我的关注3：相互关注4：潜在关系5：我添加的(五种关系互斥，只能选择一种，若直接添加则为5) */
	private Integer flagRelation = 0;
	/** 0：未拉黑1：由我的粉丝拉黑2：由我的关注拉黑3：由相互关注拉黑4：由潜在关系拉黑, */
	private Integer flagBlacklist = 0;
	/** 0：未添加1：已添加 */
	private Integer flagAdd = 0;
	/** 0：不是矩阵推送1：是矩阵推送 */
	private Integer flagPushMatrix = 0;
	/** 0：不是舆情推送1：是舆情推送 */
	private Integer flagPushPublico = 0;
	/** 0：不是1510cloud推送1：是1510cloud推送, */
	private Integer flagPush1510 = 0;

	/** 更新时间 */
	private Integer uptime = 0;
	/** 关注时间*/
	private Integer attentionTime;
	/** 加粉丝时间*/
	private Integer fansTime;
	/** 最后互动时间*/
	private Integer interactTime;

	/** 最近联系时间 */
	private Integer newTouchTime = 0;
	/** 推荐原因 */
	private Integer recommendRelationReason = 0;
	/** 是否使用系统后:否0,是1, */
	private Integer isRelationByWbcloud = 0;

	private Long maxId = 0L;
	private String rpsId;

	private Integer createdTime;
	/** 互动来源 */
	private String interactType = "";
	/** 互动次数 */
	private Long interactNum = 0L;

	public String getRpsId(){
		return rpsId;
	}

	public void setRpsId(String rpsId){
		this.rpsId = rpsId;
	}

	public Integer getCreatedTime(){
		return createdTime;
	}

	public void setCreatedTime(Integer createdTime){
		this.createdTime = createdTime;
	}

	public Long getMaxId(){
		return maxId;
	}

	public void setMaxId(Long maxId){
		this.maxId = maxId;
	}

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

	public Long getUserId(){
		return userId;
	}

	public void setUserId(Long userId){
		this.userId = userId;
	}

	public Long getInstitutionId(){
		return institutionId;
	}

	public void setInstitutionId(Long institutionId){
		this.institutionId = institutionId;
	}

	public Long getPersonId(){
		return personId;
	}

	public void setPersonId(Long personId){
		this.personId = personId;
	}

	public Integer getFlagRelation(){
		return flagRelation;
	}

	public void setFlagRelation(Integer flagRelation){
		this.flagRelation = flagRelation;
	}

	public Integer getFlagBlacklist(){
		return flagBlacklist;
	}

	public void setFlagBlacklist(Integer flagBlacklist){
		this.flagBlacklist = flagBlacklist;
	}

	public Integer getFlagAdd(){
		return flagAdd;
	}

	public void setFlagAdd(Integer flagAdd){
		this.flagAdd = flagAdd;
	}

	public Integer getFlagPushMatrix(){
		return flagPushMatrix;
	}

	public void setFlagPushMatrix(Integer flagPushMatrix){
		this.flagPushMatrix = flagPushMatrix;
	}

	public Integer getFlagPushPublico(){
		return flagPushPublico;
	}

	public void setFlagPushPublico(Integer flagPushPublico){
		this.flagPushPublico = flagPushPublico;
	}

	public Integer getFlagPush1510(){
		return flagPush1510;
	}

	public void setFlagPush1510(Integer flagPush1510){
		this.flagPush1510 = flagPush1510;
	}

	public Integer getUptime(){
		return uptime;
	}

	public void setUptime(Integer uptime){
		this.uptime = uptime;
	}

	public Integer getAttentionTime(){
		return attentionTime;
	}

	public void setAttentionTime(Integer attentionTime){
		this.attentionTime = attentionTime;
	}

	public Integer getFansTime(){
		return fansTime;
	}

	public void setFansTime(Integer fansTime){
		this.fansTime = fansTime;
	}

	public Integer getInteractTime(){
		return interactTime;
	}

	public void setInteractTime(Integer interactTime){
		this.interactTime = interactTime;
	}

	public Integer getNewTouchTime(){
		return newTouchTime;
	}

	public void setNewTouchTime(Integer newTouchTime){
		this.newTouchTime = newTouchTime;
	}

	public Integer getRecommendRelationReason(){
		return recommendRelationReason;
	}

	public void setRecommendRelationReason(Integer recommendRelationReason){
		this.recommendRelationReason = recommendRelationReason;
	}

	public Integer getIsRelationByWbcloud(){
		return isRelationByWbcloud;
	}

	public void setIsRelationByWbcloud(Integer isRelationByWbcloud){
		this.isRelationByWbcloud = isRelationByWbcloud;
	}

	public String getInteractType(){
		return interactType;
	}

	public void setInteractType(String interactType){
		this.interactType = interactType;
	}

	public Long getInteractNum(){
		return interactNum;
	}

	public void setInteractNum(Long interactNum){
		this.interactNum = interactNum;
	}

}
