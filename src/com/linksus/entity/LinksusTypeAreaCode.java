package com.linksus.entity;

import java.io.Serializable;

public class LinksusTypeAreaCode extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** ϵͳ�������� */
	private Long areaCode;
	/** �ϼ����� */
	private Long parentCode;
	/** �������� */
	private String areaName;
	/** ���˵������� */
	private String sinaAreaCode;
	/** ��Ѷ�������� */
	private String tencentAreaCode;
	/** �����ֶ� */
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
