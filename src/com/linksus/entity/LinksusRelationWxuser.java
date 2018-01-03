package com.linksus.entity;

import java.io.Serializable;

public class LinksusRelationWxuser extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** 微信用户ID */
	private Long userId;
	/** 人员ID——关联人员主表 */
	private Long personId;
	/** 用户昵称 */
	private String nickname;
	/** 性别 */
	private String sex;
	/** 用户所在城市 */
	private String city;
	/** 用户所在城市代码 */
	private String cityCode;
	/** 用户所在国家 */
	private String country;
	/** 用户所在国家代码 */
	private String countryCode;
	/** 用户所在省份 */
	private String province;
	/** 用户所在省份代码 */
	private String provinceCode;
	/** 用户的语言 */
	private String language;
	/** 用户头像 */
	private String headimgurl;
	/** 更新时间 */
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
