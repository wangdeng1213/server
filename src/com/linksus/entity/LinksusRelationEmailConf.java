package com.linksus.entity;

import java.io.Serializable;

public class LinksusRelationEmailConf extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** ����ID */
	private Long institutionId;
	/** �û���Ϣ */
	private String userInfo;
	/** �����ַ */
	private String emailAddr;
	/** ��½�û��� */
	private String userName;
	/** ���� */
	private String password;
	/** ��������� */
	private String smtpHost;
	/** �˿� */
	private Integer port;
	/** �Ƿ�ʹ��SSL */
	private Integer isSslFlag;

	public Long getInstitutionId(){
		return institutionId;
	}

	public void setInstitutionId(Long institutionId){
		this.institutionId = institutionId;
	}

	public String getUserInfo(){
		return userInfo;
	}

	public void setUserInfo(String userInfo){
		this.userInfo = userInfo;
	}

	public String getEmailAddr(){
		return emailAddr;
	}

	public void setEmailAddr(String emailAddr){
		this.emailAddr = emailAddr;
	}

	public String getUserName(){
		return userName;
	}

	public void setUserName(String userName){
		this.userName = userName;
	}

	public String getPassword(){
		return password;
	}

	public void setPassword(String password){
		this.password = password;
	}

	public String getSmtpHost(){
		return smtpHost;
	}

	public void setSmtpHost(String smtpHost){
		this.smtpHost = smtpHost;
	}

	public Integer getPort(){
		return port;
	}

	public void setPort(Integer port){
		this.port = port;
	}

	public Integer getIsSslFlag(){
		return isSslFlag;
	}

	public void setIsSslFlag(Integer isSslFlag){
		this.isSslFlag = isSslFlag;
	}
}
