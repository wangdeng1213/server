package com.linksus.entity;

import java.io.Serializable;

public class LinksusWeiXinUser implements Serializable{

	//��ǰ�û��Ƿ��ע�ù��ں�
	private Integer subscribe;

	//�û��ǳ�
	private String nickname;

	//�û���עʱ��
	private String subscribeTime;

	//�û��Ա�
	private String sex;

	//�û����ڳ���
	private String city;

	//�û����ڹ��� 
	private String country;

	//�û�����ʡ��
	private String province;

	//�û�������
	private String language;

	//�û�ͷ��
	private String headimgurl;

	//openid
	private String openid;

	//���ں�
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
