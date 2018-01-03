package com.linksus.entity;

import java.io.Serializable;

public class LinksusGovRunning extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** ��ˮ�� */
	private Long runId = 0L;
	/** Ψһ��λ */
	private Integer gid = 0;
	/** �Ƿ���� */
	private Integer isFinish = 0;
	/** ����ʱ�� */
	private Integer createTime = 0;
	/** ָ��ʱ�� */
	private Integer disTime = 0;
	/** ����ʱ�� */
	private Integer delayTime = 0;
	/** ����ʱ�� */
	private Integer auditTime = 0;
	/** ��id����gov_tag.tag_id  */
	private Integer tagId = 0;
	/** �ϲ���Ŀ�� */
	private Integer mergeCot = 0;
	/** Ĭ����״̬=0������������׼=1���������ڲ���=2��ȷ�ϰ��=3�������ذ�=4,�ϱ�����=5,�Ƿ���=6,�԰�=7,�˻�����=8,�Ƿ�ϲ�=9,��������=10,��Ч����=11 */
	private Integer opType = 0;
	/** �������� */
	private String typeContext = "";
	/** ȷ�������Ǹ���֯ */
	private Integer orgId = 0;
	/** Ĭ����0 ����Ϊ1 */
	private Integer isMultiterm = 0;
	/** ��ָ���ˮ */
	private Integer isMulsplitId = 0;
	/** 0=��״̬��1.����Ӵ� 2���������3������ */
	private String gopStatus = "";
	/** ������ʱ�� */
	private Integer updateTime = 0;
	/** 0=��״̬��1=��������2=��������3=�ϱ�����4=�˻�����5=�������룬6=������ */
	private Integer govStatusTwo = 0;
	/** �û�id */
	private Long userId = 0L;
	/** ��������:1 ���� 2 ת�� 3 @ 4 ���۲�@  5���۲�ת�� 6 ˽�� 7 ƽ̨�˻��ظ� */
	private Integer interactType = 0;
	/** ������ʽ�� 1 ΢�� 2 ˽�� */
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
