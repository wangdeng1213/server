package com.linksus.entity;

public class MailProperty{

	// �ʼ�����
	private String smtpHost;
	// �˿�
	private int port;
	// �������˺�
	private String user;
	// ����������
	private String pwd;
	// ��������
	private String from;
	// �ظ���
	private String replyTo;
	//SSL����
	private int sslFlag = 0;

	public String getSmtpHost(){
		return smtpHost;
	}

	public void setSmtpHost(String smtpHost){
		this.smtpHost = smtpHost;
	}

	public String getUser(){
		return user;
	}

	public void setUser(String user){
		this.user = user;
	}

	public String getPwd(){
		return pwd;
	}

	public void setPwd(String pwd){
		this.pwd = pwd;
	}

	public String getFrom(){
		return from;
	}

	public void setFrom(String from){
		this.from = from;
	}

	public String getReplyTo(){
		return replyTo;
	}

	public void setReplyTo(String replyTo){
		this.replyTo = replyTo;
	}

	public int getSslFlag(){
		return sslFlag;
	}

	public void setSslFlag(int sslFlag){
		this.sslFlag = sslFlag;
	}

	public int getPort(){
		return port;
	}

	public void setPort(int port){
		this.port = port;
	}
}
