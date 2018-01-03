package com.linksus.entity;

import java.io.Serializable;

public class LinksusRelationStatistics extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** 主键 */
	private Long pid;
	/** 机构ID */
	private Long institutionId;
	/** 平台账号ID */
	private Long accountId;
	/** 帐号类型:1 新浪 2 腾讯 3 微信 */
	private Integer accountType;
	/** 1：我的粉丝2：我的关注3：相互关注4：潜在关系 (四种关系互斥，只能选择一种) */
	private Integer flagRelation;
	/** 统计口径 ：1.身份 2.质量 3.性别 4.地域 5.注册时长 6.组别分布 7.粉丝的粉丝数量 8.粉丝发布微博数量分布 9.粉丝相互关注数量 */
	private String statisticsBore;
	/** 统计类型 : 在口径确定的情况下 比如口径为性别3的话1.男 2.女；口径为地域4的话 1.北京 2.广州 此处全部为代码 */
	private String statisticsType;
	/** 统计数量 */
	private Integer statisticsCount;
	/** 统计比例 */
	private Integer statisticsScale;
	/** 统计起始时间 */
	private Integer startTime;
	/** 统计截止时间 */
	private Integer endTime;
	/** 更新时间 */
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
