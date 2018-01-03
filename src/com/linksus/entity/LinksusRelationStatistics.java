package com.linksus.entity;

import java.io.Serializable;

public class LinksusRelationStatistics extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** ���� */
	private Long pid;
	/** ����ID */
	private Long institutionId;
	/** ƽ̨�˺�ID */
	private Long accountId;
	/** �ʺ�����:1 ���� 2 ��Ѷ 3 ΢�� */
	private Integer accountType;
	/** 1���ҵķ�˿2���ҵĹ�ע3���໥��ע4��Ǳ�ڹ�ϵ (���ֹ�ϵ���⣬ֻ��ѡ��һ��) */
	private Integer flagRelation;
	/** ͳ�ƿھ� ��1.��� 2.���� 3.�Ա� 4.���� 5.ע��ʱ�� 6.���ֲ� 7.��˿�ķ�˿���� 8.��˿����΢�������ֲ� 9.��˿�໥��ע���� */
	private String statisticsBore;
	/** ͳ������ : �ڿھ�ȷ��������� ����ھ�Ϊ�Ա�3�Ļ�1.�� 2.Ů���ھ�Ϊ����4�Ļ� 1.���� 2.���� �˴�ȫ��Ϊ���� */
	private String statisticsType;
	/** ͳ������ */
	private Integer statisticsCount;
	/** ͳ�Ʊ��� */
	private Integer statisticsScale;
	/** ͳ����ʼʱ�� */
	private Integer startTime;
	/** ͳ�ƽ�ֹʱ�� */
	private Integer endTime;
	/** ����ʱ�� */
	private Integer lastUpdtTime;

	private String batchsql;

	public String getBatchsql(){
		return batchsql;
	}

	public void setBatchsql(String batchsql){
		this.batchsql = batchsql;
	}

	public Long getPid(){
		return pid;
	}

	public void setPid(Long pid){
		this.pid = pid;
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

	public Integer getAccountType(){
		return accountType;
	}

	public void setAccountType(Integer accountType){
		this.accountType = accountType;
	}

	public Integer getFlagRelation(){
		return flagRelation;
	}

	public void setFlagRelation(Integer flagRelation){
		this.flagRelation = flagRelation;
	}

	public String getStatisticsBore(){
		return statisticsBore;
	}

	public void setStatisticsBore(String statisticsBore){
		this.statisticsBore = statisticsBore;
	}

	public String getStatisticsType(){
		return statisticsType;
	}

	public void setStatisticsType(String statisticsType){
		this.statisticsType = statisticsType;
	}

	public Integer getStatisticsCount(){
		return statisticsCount;
	}

	public void setStatisticsCount(Integer statisticsCount){
		this.statisticsCount = statisticsCount;
	}

	public Integer getStatisticsScale(){
		return statisticsScale;
	}

	public void setStatisticsScale(Integer statisticsScale){
		this.statisticsScale = statisticsScale;
	}

	public Integer getStartTime(){
		return startTime;
	}

	public void setStartTime(Integer startTime){
		this.startTime = startTime;
	}

	public Integer getEndTime(){
		return endTime;
	}

	public void setEndTime(Integer endTime){
		this.endTime = endTime;
	}

	public Integer getLastUpdtTime(){
		return lastUpdtTime;
	}

	public void setLastUpdtTime(Integer lastUpdtTime){
		this.lastUpdtTime = lastUpdtTime;
	}
}
