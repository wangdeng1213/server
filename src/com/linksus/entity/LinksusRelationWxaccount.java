package com.linksus.entity;

import java.io.Serializable;

public class LinksusRelationWxaccount extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** 用户的标识，对当前公众号唯一 */
	private String openid;
	/** 公众号 */
	private Long appid;
	/** 微信用户ID:关联微信用户 */
	private Long userId;
	/** 用户昵称 */
	private String nickname;
	/** 性别 */
	private String sex;
	/** 用户所在城市 */
	private String city;
	/** 用户所在国家 */
	private String country;
	/** 用户所在省份 */
	private String province;
	/** 用户的语言 */
	private String language;
	/** 用户头像 */
	private String headimgurl;
	/** 用户信息MD5,用于判断账号合并 */
	private String msgMd5;
	/** 用户关注时间 */
	private Integer subscribeTime;
	/** 更新时间 */
	private Integer lastUpdtTime;

	public String getOpenid(){
		return openid;
	}

	public void setOpenid(String openid){
		this.openid = openid;
	}

	public Long getAppid(){
		return appid;
	}

	public void setAppid(Long appid){
		this.appid = appid;
	}

	public Long getUserId(){
		return userId;
	}

	public void setUserId(Long userId){
		this.userId = userId;
	}

	public String getNickname(){
		return nickname;
	}

	public void setNickname(String nickname){
		this.nickname = nickname;
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

	public String getMsgMd5(){
		return msgMd5;
	}

	public void setMsgMd5(String msgMd5){
		this.msgMd5 = msgMd5;
	}

	public Integer getSubscribeTime(){
		return subscribeTime;
	}

	public void setSubscribeTime(Integer subscribeTime){
		this.subscribeTime = subscribeTime;
	}

	public Integer getLastUpdtTime(){
		return lastUpdtTime;
	}

	public void setLastUpdtTime(Integer lastUpdtTime){
		this.lastUpdtTime = lastUpdtTime;
	}
}
