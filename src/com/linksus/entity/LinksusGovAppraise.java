package com.linksus.entity;

import java.io.Serializable;

public class LinksusGovAppraise extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** ��ˮ�� */
	private Long runId;
	/** ��ˮ�� */
	private Integer isAppraise;
	/** ������Ч�� */
	private Integer appraiseTime;
	/** �������۵ȼ� */
	private Integer qualityLevel;
	/** ̬�����۵ȼ� */
	private Integer attitudeLevel;
	/** ̬�����۵ȼ� */
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
