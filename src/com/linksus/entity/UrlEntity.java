package com.linksus.entity;

import java.io.Serializable;

import com.linksus.common.util.DateUtil;

public class UrlEntity implements Serializable{

	/** 类型:1新浪 2腾讯 3微信 4 其他*/
	private Integer urlType;
	/** 名称 */
	private String name;
	/** 描述 */
	private String desc;
	/** 完整url */
	private String url;
	/** urlKey */
	private String urlKey;
	/** 调用限制 0无限制,1读限制 2写限制*/
	private int limitType;
	/** 当前访问小时 */
	private String currHour;
	/** 访问次数*/
	private long linkTime;
	/** 访问次数*/
	private long errorTime;

	/**
	 * 当前小时访问次数
	 */
	public synchronized void addCurrHourLinkTime(){
		String dateStr = DateUtil.getCurrDate("yyyyMMddHH");
		if(dateStr.equals(currHour)){//同一小时
			linkTime = linkTime + 1;
		}else{
			currHour = dateStr;
			linkTime = 1;
		}
	}

	/**
	 * 当前小时访问返回错误次数
	 */
	public synchronized void addCurrHourErrorTime(){
		String dateStr = DateUtil.getCurrDate("yyyyMMddHH");
		if(dateStr.equals(currHour)){//同一小时
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
