package com.linksus.entity;

import java.io.Serializable;

public class LinksusPersonaccount extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** ����ID */
	private Long id;
	/** �û����� */
	private String name;
	/** �û����� */
	private String mail;
	/** �û����� */
	private String pwd;
	/**  */
	private String tele;
	/** 1����
	          2δ����
	        ֻ��������ʺ�
	         */
	private Integer status;
	/** �ʺ�����
	        0��������Ա
	        1�������Ա
	         */
	private Integer type;
	/** �ֻ��Ƿ���֤ 0Ϊ�� 1Ϊ�� */
	private Integer teleStatus;
	/** �����Ƿ���֤ 0Ϊ�� 1Ϊ�� */
	private Integer mailStatus;
	/** ȫƴ  �ö��ŷָ� */
	private String pinyinName;
	/** ��ƴ  �ö��ŷָ� */
	private String pinyinShortName;
	/** ע��ip */
	private String createdIp;
	/** ����¼ip */
	private String lastLoginIp;
	/** ע��ʱ�� ͨ�����ֶκ�type ,status �ж���ά�ʺ��Ƿ񳬹���һ�� û�м��� */
	private Integer createdTime;
	/** ����ʱ�� */
	private Integer activeTime;
	/** ����½ʱ�� */
	private Integer lastLoginTime;
	/** ����޸�ʱ�� */
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
