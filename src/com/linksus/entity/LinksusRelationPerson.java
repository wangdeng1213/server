package com.linksus.entity;

import java.io.Serializable;

public class LinksusRelationPerson extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** ��ԱID */
	private Long personId;
	/** ͷ��URL */
	private String headimgurl;
	/** ��Ա���� */
	private String personName;
	/** �Ա� m���С�f��Ů��n��δ֪ */
	private String gender;
	/** ���� */
	private String birthDay;
	/** ���ڵ����� */
	private String location;
	/** ���ڹ���ID */
	private String countryCode;
	/** ���ڵ���ID */
	private String stateCode;
	/** ���ڳ���ID */
	private String cityCode;
	/** �����˺�:�����|�ָ� */
	private String sinaIds;
	/** ��Ѷ�˺�:�����|�ָ� */
	private String tencentIds;
	/** ΢���û���:�����|�ָ� */
	private String weixinIds;
	/** ����ʱ�� */
	private Integer addTime;
	/** ͬ��ʱ�� */
	private Integer synctime;

	public Long getPersonId(){
		return personId;
	}

	public void setPersonId(Long personId){
		this.personId = personId;
	}

	public String getHeadimgurl(){
		return headimgurl;
	}

	public void setHeadimgurl(String headimgurl){
		this.headimgurl = headimgurl;
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

	public String getSinaIds(){
		return sinaIds;
	}

	public void setSinaIds(String sinaIds){
		this.sinaIds = sinaIds;
	}

	public String getTencentIds(){
		return tencentIds;
	}

	public void setTencentIds(String tencentIds){
		this.tencentIds = tencentIds;
	}

	public String getWeixinIds(){
		return weixinIds;
	}

	public void setWeixinIds(String weixinIds){
		this.weixinIds = weixinIds;
	}

	public Integer getAddTime(){
		return addTime;
	}

	public void setAddTime(Integer addTime){
		this.addTime = addTime;
	}

	public Integer getSynctime(){
		return synctime;
	}

	public void setSynctime(Integer synctime){
		this.synctime = synctime;
	}
}
