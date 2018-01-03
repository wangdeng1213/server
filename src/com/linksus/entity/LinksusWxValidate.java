package com.linksus.entity;

import java.io.Serializable;

public class LinksusWxValidate extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** ����id */
	private Long id;
	/** ΢�ź� */
	private String wxNum;
	/** ��֤token */
	private String code;
	/** ��ǰ��֤״̬  0Ϊδ��֤ 1Ϊ����֤ */
	private Integer status;
	/** ����ʱ�� */
	private Integer createdTime;
	/** ������ʱ�� */
	private Integer lastUpdTime;
	/** ��֤ʱ�� */
	private Integer validTime;

	public Long getId(){
		return id;
	}

	public void setId(Long id){
		this.id = id;
	}

	public String getWxNum(){
		return wxNum;
	}

	public void setWxNum(String wxNum){
		this.wxNum = wxNum;
	}

	public String getCode(){
		return code;
	}

	public void setCode(String code){
		this.code = code;
	}

	public Integer getStatus(){
		return status;
	}

	public void setStatus(Integer status){
		this.status = status;
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

	public Integer getValidTime(){
		return validTime;
	}

	public void setValidTime(Integer validTime){
		this.validTime = validTime;
	}
}
