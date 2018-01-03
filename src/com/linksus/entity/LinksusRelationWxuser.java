package com.linksus.entity;

import java.io.Serializable;

public class LinksusRelationWxuser extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** ΢���û�ID */
	private Long userId;
	/** ��ԱID����������Ա���� */
	private Long personId;
	/** �û��ǳ� */
	private String nickname;
	/** �Ա� */
	private String sex;
	/** �û����ڳ��� */
	private String city;
	/** �û����ڳ��д��� */
	private String cityCode;
	/** �û����ڹ��� */
	private String country;
	/** �û����ڹ��Ҵ��� */
	private String countryCode;
	/** �û�����ʡ�� */
	private String province;
	/** �û�����ʡ�ݴ��� */
	private String provinceCode;
	/** �û������� */
	private String language;
	/** �û�ͷ�� */
	private String headimgurl;
	/** ����ʱ�� */
	private Integer lastUpdtTime;

	public Long getUserId(){
		return userId;
	}

	public void setUserId(Long userId){
		this.userId = userId;
	}

	public Long getPersonId(){
		return personId;
	}

	public void setPersonId(Long personId){
		this.personId = personId;
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

	public Integer getLastUpdtTime(){
		return lastUpdtTime;
	}

	public void setLastUpdtTime(Integer lastUpdtTime){
		this.lastUpdtTime = lastUpdtTime;
	}

	public String getCityCode(){
		return cityCode;
	}

	public void setCityCode(String cityCode){
		this.cityCode = cityCode;
	}

	public String getCountryCode(){
		return countryCode;
	}

	public void setCountryCode(String countryCode){
		this.countryCode = countryCode;
	}

	public String getProvinceCode(){
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode){
		this.provinceCode = provinceCode;
	}

}
