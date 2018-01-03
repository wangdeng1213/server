package com.linksus.entity;

import java.io.Serializable;

public class LinksusRelationPersonInfo extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** 人员ID */
	private Long personId;
	/** 机构ID */
	private Long institutionId;
	/** 人员姓名 */
	private String personName;
	/** 性别 */
	private String gender;
	/** 生日 */
	private String birthDay;
	/** 认证信息 */
	private String verifiedInfo;
	/** 所在地中文 */
	private String location;
	/** 所在国家ID */
	private String countryCode;
	/** 所在地区ID */
	private String stateCode;
	/** 所在城市ID */
	private String cityCode;
	/** 更新时间 */
	private Integer lastUpdtTime;

	public Long getPersonId(){
		return personId;
	}

	public void setPersonId(Long personId){
		this.personId = personId;
	}

	public Long getInstitutionId(){
		return institutionId;
	}

	public void setInstitutionId(Long institutionId){
		this.institutionId = institutionId;
	}

	public String getPersonName(){
		return personName;
	}

	public void setPersonName(String personName){
		this.personName = personName;
	}

	public String getGender(){
		return gender;
	}

	public void setGender(String gender){
		this.gender = gender;
	}

	public String getBirthDay(){
		return birthDay;
	}

	public void setBirthDay(String birthDay){
		this.birthDay = birthDay;
	}

	public String getVerifiedInfo(){
		return verifiedInfo;
	}

	public void setVerifiedInfo(String verifiedInfo){
		this.verifiedInfo = verifiedInfo;
	}

	public String getLocation(){
		return location;
	}

	public void setLocation(String location){
		this.location = location;
	}

	public String getCountryCode(){
		return countryCode;
	}

	public void setCountryCode(String countryCode){
		this.countryCode = countryCode;
	}

	public String getStateCode(){
		return stateCode;
	}

	public void setStateCode(String stateCode){
		this.stateCode = stateCode;
	}

	public String getCityCode(){
		return cityCode;
	}

	public void setCityCode(String cityCode){
		this.cityCode = cityCode;
	}

	public Integer getLastUpdtTime(){
		return lastUpdtTime;
	}

	public void setLastUpdtTime(Integer lastUpdtTime){
		this.lastUpdtTime = lastUpdtTime;
	}
}
