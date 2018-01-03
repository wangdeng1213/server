package com.linksus.entity;

import java.io.Serializable;

public class LinksusWxValidate extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** 主键id */
	private Long id;
	/** 微信号 */
	private String wxNum;
	/** 验证token */
	private String code;
	/** 当前验证状态  0为未验证 1为已验证 */
	private Integer status;
	/** 创建时间 */
	private Integer createdTime;
	/** 最后更新时间 */
	private Integer lastUpdTime;
	/** 验证时间 */
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
