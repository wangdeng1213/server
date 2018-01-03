package com.linksus.entity;

import java.io.Serializable;

public class LinksusRelationPersonInfo extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** ��ԱID */
	private Long personId;
	/** ����ID */
	private Long institutionId;
	/** ��Ա���� */
	private String personName;
	/** �Ա� */
	private String gender;
	/** ���� */
	private String birthDay;
	/** ��֤��Ϣ */
	private String verifiedInfo;
	/** ���ڵ����� */
	private String location;
	/** ���ڹ���ID */
	private String countryCode;
	/** ���ڵ���ID */
	private String stateCode;
	/** ���ڳ���ID */
	private String cityCode;
	/** ����ʱ�� */
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
