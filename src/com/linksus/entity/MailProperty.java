package com.linksus.entity;

public class MailProperty{

	// 邮件主机
	private String smtpHost;
	// 端口
	private int port;
	// 发送人账号
	private String user;
	// 发送人密码
	private String pwd;
	// 发送邮箱
	private String from;
	// 回复到
	private String replyTo;
	//SSL发送
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
