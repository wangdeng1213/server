package com.linksus.entity;

import java.io.Serializable;

public class LinksusRelationUserAccount extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** ���� */
	private Long pid = 0L;
	/** ƽ̨�˺�ID */
	private Long accountId = 0L;
	/** �ʺ�����:1 ���� 2 ��Ѷ 3 ΢�� */
	private Integer accountType = 0;
	/** ΢���û�ID ����΢���û���΢���û������� */
	private Long userId = 0L;
	/** ����ID */
	private Long institutionId = 0L;
	/** ��ԱID */
	private Long personId = 0L;
	/** 1���ҵķ�˿2���ҵĹ�ע3���໥��ע4��Ǳ�ڹ�ϵ5������ӵ�(���ֹ�ϵ���⣬ֻ��ѡ��һ�֣���ֱ�������Ϊ5) */
	private Integer flagRelation = 0;
	/** 0��δ����1�����ҵķ�˿����2�����ҵĹ�ע����3�����໥��ע����4����Ǳ�ڹ�ϵ����, */
	private Integer flagBlacklist = 0;
	/** 0��δ���1������� */
	private Integer flagAdd = 0;
	/** 0�����Ǿ�������1���Ǿ������� */
	private Integer flagPushMatrix = 0;
	/** 0��������������1������������ */
	private Integer flagPushPublico = 0;
	/** 0������1510cloud����1����1510cloud����, */
	private Integer flagPush1510 = 0;

	/** ����ʱ�� */
	private Integer uptime = 0;
	/** ��עʱ��*/
	private Integer attentionTime;
	/** �ӷ�˿ʱ��*/
	private Integer fansTime;
	/** ��󻥶�ʱ��*/
	private Integer interactTime;

	/** �����ϵʱ�� */
	private Integer newTouchTime = 0;
	/** �Ƽ�ԭ�� */
	private Integer recommendRelationReason = 0;
	/** �Ƿ�ʹ��ϵͳ��:��0,��1, */
	private Integer isRelationByWbcloud = 0;

	private Long maxId = 0L;
	private String rpsId;

	private Integer createdTime;
	/** ������Դ */
	private String interactType = "";
	/** �������� */
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
