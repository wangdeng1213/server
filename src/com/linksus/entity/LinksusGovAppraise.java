package com.linksus.entity;

import java.io.Serializable;

public class LinksusGovAppraise extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** 流水号 */
	private Long runId;
	/** 流水号 */
	private Integer isAppraise;
	/** 评价有效期 */
	private Integer appraiseTime;
	/** 质量评价等级 */
	private Integer qualityLevel;
	/** 态度评价等级 */
	private Integer attitudeLevel;
	/** 态度评价等级 */
	private String appraiseUrl;

	public Long getRunId(){
		return runId;
	}

	public void setRunId(Long runId){
		this.runId = runId;
	}

	public Integer getIsAppraise(){
		return isAppraise;
	}

	public void setIsAppraise(Integer isAppraise){
		this.isAppraise = isAppraise;
	}

	public Integer getAppraiseTime(){
		return appraiseTime;
	}

	public void setAppraiseTime(Integer appraiseTime){
		this.appraiseTime = appraiseTime;
	}

	public Integer getQualityLevel(){
		return qualityLevel;
	}

	public void setQualityLevel(Integer qualityLevel){
		this.qualityLevel = qualityLevel;
	}

	public Integer getAttitudeLevel(){
		return attitudeLevel;
	}

	public void setAttitudeLevel(Integer attitudeLevel){
		this.attitudeLevel = attitudeLevel;
	}

	public String getAppraiseUrl(){
		return appraiseUrl;
	}

	public void setAppraiseUrl(String appraiseUrl){
		this.appraiseUrl = appraiseUrl;
	}
}
