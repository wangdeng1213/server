package com.linksus.entity;

import java.io.Serializable;

public class LinksusTypeAreaCode extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** 系统地区代码 */
	private Long areaCode;
	/** 上级代码 */
	private Long parentCode;
	/** 地区名称 */
	private String areaName;
	/** 新浪地区代码 */
	private String sinaAreaCode;
	/** 腾讯地区代码 */
	private String tencentAreaCode;
	/** 排序字段 */
	private Integer ordervalue;

	public Long getAreaCode(){
		return areaCode;
	}

	public void setAreaCode(Long areaCode){
		this.areaCode = areaCode;
	}

	public Long getParentCode(){
		return parentCode;
	}

	public void setParentCode(Long parentCode){
		this.parentCode = parentCode;
	}

	public String getAreaName(){
		return areaName;
	}

	public void setAreaName(String areaName){
		this.areaName = areaName;
	}

	public String getSinaAreaCode(){
		return sinaAreaCode;
	}

	public void setSinaAreaCode(String sinaAreaCode){
		this.sinaAreaCode = sinaAreaCode;
	}

	public String getTencentAreaCode(){
		return tencentAreaCode;
	}

	public void setTencentAreaCode(String tencentAreaCode){
		this.tencentAreaCode = tencentAreaCode;
	}

	public Integer getOrdervalue(){
		return ordervalue;
	}

	public void setOrdervalue(Integer ordervalue){
		this.ordervalue = ordervalue;
	}
}
