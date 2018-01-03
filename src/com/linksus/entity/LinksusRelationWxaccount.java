package com.linksus.entity;

import java.io.Serializable;

public class LinksusRelationWxaccount extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** �û��ı�ʶ���Ե�ǰ���ں�Ψһ */
	private String openid;
	/** ���ں� */
	private Long appid;
	/** ΢���û�ID:����΢���û� */
	private Long userId;
	/** �û��ǳ� */
	private String nickname;
	/** �Ա� */
	private String sex;
	/** �û����ڳ��� */
	private String city;
	/** �û����ڹ��� */
	private String country;
	/** �û�����ʡ�� */
	private String province;
	/** �û������� */
	private String language;
	/** �û�ͷ�� */
	private String headimgurl;
	/** �û���ϢMD5,�����ж��˺źϲ� */
	private String msgMd5;
	/** �û���עʱ�� */
	private Integer subscribeTime;
	/** ����ʱ�� */
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
