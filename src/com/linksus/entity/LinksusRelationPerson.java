package com.linksus.entity;

import java.io.Serializable;

public class LinksusRelationPerson extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** 人员ID */
	private Long personId;
	/** 头像URL */
	private String headimgurl;
	/** 人员姓名 */
	private String personName;
	/** 性别 m：男、f：女、n：未知 */
	private String gender;
	/** 生日 */
	private String birthDay;
	/** 所在地中文 */
	private String location;
	/** 所在国家ID */
	private String countryCode;
	/** 所在地区ID */
	private String stateCode;
	/** 所在城市ID */
	private String cityCode;
	/** 新浪账号:多个以|分隔 */
	private String sinaIds;
	/** 腾讯账号:多个以|分隔 */
	private String tencentIds;
	/** 微信用户号:多个以|分隔 */
	private String weixinIds;
	/** 创建时间 */
	private Integer addTime;
	/** 同步时间 */
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
