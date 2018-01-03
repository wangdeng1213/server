package com.linksus.entity;

import java.io.Serializable;

public class LinksusWeiXinUser implements Serializable{

	//当前用户是否关注该公众号
	private Integer subscribe;

	//用户昵称
	private String nickname;

	//用户关注时间
	private String subscribeTime;

	//用户性别
	private String sex;

	//用户所在城市
	private String city;

	//用户所在国家 
	private String country;

	//用户所在省份
	private String province;

	//用户的语言
	private String language;

	//用户头像
	private String headimgurl;

	//openid
	private String openid;

	//公众号
	private long appid;

	public Integer getSubscribe(){
		return subscribe;
	}

	public void setSubscribe(Integer subscribe){
		this.subscribe = subscribe;
	}

	public String getNickname(){
		return nickname;
	}

	public void setNickname(String nickname){
		this.nickname = nickname;
	}

	public String getSubscribeTime(){
		return subscribeTime;
	}

	public void setSubscribeTime(String subscribeTime){
		this.subscribeTime = subscribeTime;
	}

	public String getSex(){
		return sex;
	}

	public void setSex(String sex){
		this.sex = sex;
	}

	public String getCity(){
		return city;
	}

	public void setCity(String city){
		this.city = city;
	}

	public String getCountry(){
		return country;
	}

	public void setCountry(String country){
		this.country = country;
	}

	public String getProvince(){
		return province;
	}

	public void setProvince(String province){
		this.province = province;
	}

	public String getLanguage(){
		return language;
	}

	public void setLanguage(String language){
		this.language = language;
	}

	public String getHeadimgurl(){
		return headimgurl;
	}

	public void setHeadimgurl(String headimgurl){
		this.headimgurl = headimgurl;
	}

	public String getOpenid(){
		return openid;
	}

	public void setOpenid(String openid){
		this.openid = openid;
	}

	public long getAppid(){
		return appid;
	}

	public void setAppid(long appid){
		this.appid = appid;
	}

}
