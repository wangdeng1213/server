package com.linksus.entity;

import java.io.Serializable;

public class LinksusInstitution extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** 机构ID */
	private Long id;
	/** 机构名称 */
	private String name;
	/** 机构简称 */
	private String shortName;
	/** 机构行业 */
	private Integer industry;
	/** 机构地址 */
	private String address;
	/** 机构邮政编码 */
	private String code;
	/** 机构电话 */
	private String phone;
	/** 机构传真 */
	private String fax;
	/** 是否试用
	        0为是
	        1为否
	         */
	private Integer trival;
	/** 试用状态
	        0未审批
	        1已通过
	        2未通过
	         */
	private Integer trivalStatus;
	/** 试用开始日期 */
	private Integer trivalStime;
	/** 试用结束日期 */
	private Integer trivalEtime;
	/** 创建时间 */
	private Integer createdTime;
	/** 最后更新时间 */
	private Integer lastUpdTime;
	/** 新浪可用数据量 */
	private Integer sinaBusinessNumber;
	/** 腾讯可用数据量 */
	private Integer qqBusinessNumber;
	/** 账户短信可用量 */
	private Integer smsNumber;
	/** 账户类型1大客户2普通客户 */
	private Integer type;
	/** 默认腾讯来源默认为2 */
	private Long qqSourceId;
	/** 默认新浪来源默认为1 */
	private Long sinaSourceId;

	public Long getId(){
		return id;
	}

	public void setId(Long id){
		this.id = id;
	}

	public String getName(){
		return name;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getShortName(){
		return shortName;
	}

	public void setShortName(String shortName){
		this.shortName = shortName;
	}

	public Integer getIndustry(){
		return industry;
	}

	public void setIndustry(Integer industry){
		this.industry = industry;
	}

	public String getAddress(){
		return address;
	}

	public void setAddress(String address){
		this.address = address;
	}

	public String getCode(){
		return code;
	}

	public void setCode(String code){
		this.code = code;
	}

	public String getPhone(){
		return phone;
	}

	public void setPhone(String phone){
		this.phone = phone;
	}

	public String getFax(){
		return fax;
	}

	public void setFax(String fax){
		this.fax = fax;
	}

	public Integer getTrival(){
		return trival;
	}

	public void setTrival(Integer trival){
		this.trival = trival;
	}

	public Integer getTrivalStatus(){
		return trivalStatus;
	}

	public void setTrivalStatus(Integer trivalStatus){
		this.trivalStatus = trivalStatus;
	}

	public Integer getTrivalStime(){
		return trivalStime;
	}

	public void setTrivalStime(Integer trivalStime){
		this.trivalStime = trivalStime;
	}

	public Integer getTrivalEtime(){
		return trivalEtime;
	}

	public void setTrivalEtime(Integer trivalEtime){
		this.trivalEtime = trivalEtime;
	}

	public Integer getCreatedTime(){
		return createdTime;
	}

	public void setCreatedTime(Integer createdTime){
		this.createdTime = createdTime;
	}

	public Integer getLastUpdTime(){
		return lastUpdTime;
	}

	public void setLastUpdTime(Integer lastUpdTime){
		this.lastUpdTime = lastUpdTime;
	}

	public Integer getSinaBusinessNumber(){
		return sinaBusinessNumber;
	}

	public void setSinaBusinessNumber(Integer sinaBusinessNumber){
		this.sinaBusinessNumber = sinaBusinessNumber;
	}

	public Integer getQqBusinessNumber(){
		return qqBusinessNumber;
	}

	public void setQqBusinessNumber(Integer qqBusinessNumber){
		this.qqBusinessNumber = qqBusinessNumber;
	}

	public Integer getSmsNumber(){
		return smsNumber;
	}

	public void setSmsNumber(Integer smsNumber){
		this.smsNumber = smsNumber;
	}

	public Integer getType(){
		return type;
	}

	public void setType(Integer type){
		this.type = type;
	}

	public Long getQqSourceId(){
		return qqSourceId;
	}

	public void setQqSourceId(Long qqSourceId){
		this.qqSourceId = qqSourceId;
	}

	public Long getSinaSourceId(){
		return sinaSourceId;
	}

	public void setSinaSourceId(Long sinaSourceId){
		this.sinaSourceId = sinaSourceId;
	}
}
