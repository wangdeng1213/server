package com.linksus.entity;

import java.io.Serializable;

public class LinksusGovRunning extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** 流水号 */
	private Long runId = 0L;
	/** 唯一定位 */
	private Integer gid = 0;
	/** 是否完结 */
	private Integer isFinish = 0;
	/** 创建时间 */
	private Integer createTime = 0;
	/** 指派时间 */
	private Integer disTime = 0;
	/** 延期时间 */
	private Integer delayTime = 0;
	/** 处理时间 */
	private Integer auditTime = 0;
	/** 组id关联gov_tag.tag_id  */
	private Integer tagId = 0;
	/** 合并条目数 */
	private Integer mergeCot = 0;
	/** 默认无状态=0，申请延期批准=1，申请延期驳回=2，确认办结=3，发回重办=4,上报政务=5,是否拆分=6,自办=7,退回政务=8,是否合并=9,待办政务=10,无效诉求=11 */
	private Integer opType = 0;
	/** 类型内容 */
	private String typeContext = "";
	/** 确定来自那个组织 */
	private Integer orgId = 0;
	/** 默认是0 多问为1 */
	private Integer isMultiterm = 0;
	/** 拆分父流水 */
	private Integer isMulsplitId = 0;
	/** 0=无状态，1.政务接待 2，政务跟进3政务办结 */
	private String gopStatus = "";
	/** 最后更新时间 */
	private Integer updateTime = 0;
	/** 0=无状态，1=市民反馈，2=多问政务，3=上报政务，4=退回政务，5=延期申请，6=申请办结 */
	private Integer govStatusTwo = 0;
	/** 用户id */
	private Long userId = 0L;
	/** 互动类型:1 评论 2 转发 3 @ 4 评论并@  5评论并转发 6 私信 7 平台账户回复 */
	private Integer interactType = 0;
	/** 互动方式： 1 微博 2 私信 */
	private Integer interactMode = 0;

	public Long getRunId(){
		return runId;
	}

	public void setRunId(Long runId){
		this.runId = runId;
	}

	public Integer getGid(){
		return gid;
	}

	public void setGid(Integer gid){
		this.gid = gid;
	}

	public Integer getIsFinish(){
		return isFinish;
	}

	public void setIsFinish(Integer isFinish){
		this.isFinish = isFinish;
	}

	public Integer getCreateTime(){
		return createTime;
	}

	public void setCreateTime(Integer createTime){
		this.createTime = createTime;
	}

	public Integer getDisTime(){
		return disTime;
	}

	public void setDisTime(Integer disTime){
		this.disTime = disTime;
	}

	public Integer getDelayTime(){
		return delayTime;
	}

	public void setDelayTime(Integer delayTime){
		this.delayTime = delayTime;
	}

	public Integer getAuditTime(){
		return auditTime;
	}

	public void setAuditTime(Integer auditTime){
		this.auditTime = auditTime;
	}

	public Integer getTagId(){
		return tagId;
	}

	public void setTagId(Integer tagId){
		this.tagId = tagId;
	}

	public Integer getMergeCot(){
		return mergeCot;
	}

	public void setMergeCot(Integer mergeCot){
		this.mergeCot = mergeCot;
	}

	public Integer getOpType(){
		return opType;
	}

	public void setOpType(Integer opType){
		this.opType = opType;
	}

	public String getTypeContext(){
		return typeContext;
	}

	public void setTypeContext(String typeContext){
		this.typeContext = typeContext;
	}

	public Integer getOrgId(){
		return orgId;
	}

	public void setOrgId(Integer orgId){
		this.orgId = orgId;
	}

	public Integer getIsMultiterm(){
		return isMultiterm;
	}

	public void setIsMultiterm(Integer isMultiterm){
		this.isMultiterm = isMultiterm;
	}

	public Integer getIsMulsplitId(){
		return isMulsplitId;
	}

	public void setIsMulsplitId(Integer isMulsplitId){
		this.isMulsplitId = isMulsplitId;
	}

	public String getGopStatus(){
		return gopStatus;
	}

	public void setGopStatus(String gopStatus){
		this.gopStatus = gopStatus;
	}

	public Integer getUpdateTime(){
		return updateTime;
	}

	public void setUpdateTime(Integer updateTime){
		this.updateTime = updateTime;
	}

	public Integer getGovStatusTwo(){
		return govStatusTwo;
	}

	public void setGovStatusTwo(Integer govStatusTwo){
		this.govStatusTwo = govStatusTwo;
	}

	public Long getUserId(){
		return userId;
	}

	public void setUserId(Long userId){
		this.userId = userId;
	}

	public Integer getInteractType(){
		return interactType;
	}

	public void setInteractType(Integer interactType){
		this.interactType = interactType;
	}

	public Integer getInteractMode(){
		return interactMode;
	}

	public void setInteractMode(Integer interactMode){
		this.interactMode = interactMode;
	}
}
