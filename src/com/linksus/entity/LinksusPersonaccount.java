package com.linksus.entity;

import java.io.Serializable;

public class LinksusPersonaccount extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** 主键ID */
	private Long id;
	/** 用户姓名 */
	private String name;
	/** 用户邮箱 */
	private String mail;
	/** 用户密码 */
	private String pwd;
	/**  */
	private String tele;
	/** 1激活
	          2未激活
	        只针对授予帐号
	         */
	private Integer status;
	/** 帐号类型
	        0超级管理员
	        1授予管理员
	         */
	private Integer type;
	/** 手机是否验证 0为否 1为是 */
	private Integer teleStatus;
	/** 邮箱是否验证 0为否 1为是 */
	private Integer mailStatus;
	/** 全拼  用逗号分割 */
	private String pinyinName;
	/** 简拼  用逗号分割 */
	private String pinyinShortName;
	/** 注册ip */
	private String createdIp;
	/** 最后登录ip */
	private String lastLoginIp;
	/** 注册时间 通过此字段和type ,status 判断运维帐号是否超过了一周 没有激活 */
	private Integer createdTime;
	/** 激活时间 */
	private Integer activeTime;
	/** 最后登陆时间 */
	private Integer lastLoginTime;
	/** 最后修改时间 */
	private Integer lastUpdTime;

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

	public String getMail(){
		return mail;
	}

	public void setMail(String mail){
		this.mail = mail;
	}

	public String getPwd(){
		return pwd;
	}

	public void setPwd(String pwd){
		this.pwd = pwd;
	}

	public String getTele(){
		return tele;
	}

	public void setTele(String tele){
		this.tele = tele;
	}

	public Integer getStatus(){
		return status;
	}

	public void setStatus(Integer status){
		this.status = status;
	}

	public Integer getType(){
		return type;
	}

	public void setType(Integer type){
		this.type = type;
	}

	public Integer getTeleStatus(){
		return teleStatus;
	}

	public void setTeleStatus(Integer teleStatus){
		this.teleStatus = teleStatus;
	}

	public Integer getMailStatus(){
		return mailStatus;
	}

	public void setMailStatus(Integer mailStatus){
		this.mailStatus = mailStatus;
	}

	public String getPinyinName(){
		return pinyinName;
	}

	public void setPinyinName(String pinyinName){
		this.pinyinName = pinyinName;
	}

	public String getPinyinShortName(){
		return pinyinShortName;
	}

	public void setPinyinShortName(String pinyinShortName){
		this.pinyinShortName = pinyinShortName;
	}

	public String getCreatedIp(){
		return createdIp;
	}

	public void setCreatedIp(String createdIp){
		this.createdIp = createdIp;
	}

	public String getLastLoginIp(){
		return lastLoginIp;
	}

	public void setLastLoginIp(String lastLoginIp){
		this.lastLoginIp = lastLoginIp;
	}

	public Integer getCreatedTime(){
		return createdTime;
	}

	public void setCreatedTime(Integer createdTime){
		this.createdTime = createdTime;
	}

	public Integer getActiveTime(){
		return activeTime;
	}

	public void setActiveTime(Integer activeTime){
		this.activeTime = activeTime;
	}

	public Integer getLastLoginTime(){
		return lastLoginTime;
	}

	public void setLastLoginTime(Integer lastLoginTime){
		this.lastLoginTime = lastLoginTime;
	}

	public Integer getLastUpdTime(){
		return lastUpdTime;
	}

	public void setLastUpdTime(Integer lastUpdTime){
		this.lastUpdTime = lastUpdTime;
	}
}
