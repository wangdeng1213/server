package com.linksus.entity;

import java.io.Serializable;

import com.linksus.common.util.DateUtil;

public class UrlEntity implements Serializable{

	/** ����:1���� 2��Ѷ 3΢�� 4 ����*/
	private Integer urlType;
	/** ���� */
	private String name;
	/** ���� */
	private String desc;
	/** ����url */
	private String url;
	/** urlKey */
	private String urlKey;
	/** �������� 0������,1������ 2д����*/
	private int limitType;
	/** ��ǰ����Сʱ */
	private String currHour;
	/** ���ʴ���*/
	private long linkTime;
	/** ���ʴ���*/
	private long errorTime;

	/**
	 * ��ǰСʱ���ʴ���
	 */
	public synchronized void addCurrHourLinkTime(){
		String dateStr = DateUtil.getCurrDate("yyyyMMddHH");
		if(dateStr.equals(currHour)){//ͬһСʱ
			linkTime = linkTime + 1;
		}else{
			currHour = dateStr;
			linkTime = 1;
		}
	}

	/**
	 * ��ǰСʱ���ʷ��ش������
	 */
	public synchronized void addCurrHourErrorTime(){
		String dateStr = DateUtil.getCurrDate("yyyyMMddHH");
		if(dateStr.equals(currHour)){//ͬһСʱ
			errorTime = errorTime + 1;
		}else{
			currHour = dateStr;
			errorTime = 1;
		}
	}

	public Integer getUrlType(){
		return urlType;
	}

	public void setUrlType(Integer urlType){
		this.urlType = urlType;
	}

	public String getName(){
		return name;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getDesc(){
		return desc;
	}

	public void setDesc(String desc){
		this.desc = desc;
	}

	public String getUrl(){
		return url;
	}

	public void setUrl(String url){
		this.url = url;
	}

	public String getUrlKey(){
		return urlKey;
	}

	public void setUrlKey(String urlKey){
		this.urlKey = urlKey;
	}

	public int getLimitType(){
		return limitType;
	}

	public void setLimitType(int limitType){
		this.limitType = limitType;
	}

	public String getCurrHour(){
		return currHour;
	}

	public void setCurrHour(String currHour){
		this.currHour = currHour;
	}

	public long getLinkTime(){
		return linkTime;
	}

	public void setLinkTime(long linkTime){
		this.linkTime = linkTime;
	}

	public long getErrorTime(){
		return errorTime;
	}

	public void setErrorTime(long errorTime){
		this.errorTime = errorTime;
	}
}
