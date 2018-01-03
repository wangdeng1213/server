package com.linksus.entity;

import java.io.File;
import java.io.Serializable;
import java.util.List;

public class Email implements Serializable{

	private static final long serialVersionUID = 1L;
	/** 收件人 多个以逗号分隔 **/
	private String address;
	/** 暗送 **/
	private String bcc;
	/** 抄送给 多个以逗号分隔**/
	private String cc;
	/** 邮件主题 **/
	private String subject;
	/** 邮件内容 **/
	private String content;
	/** 附件 **/
	private List<File> annexFile;
	/** HTML格式*/
	private boolean htmlFlag = false;

	public String getBcc(){
		return bcc;
	}

	public void setBcc(String bcc){
		this.bcc = bcc;
	}

	public String getAddress(){
		return address;
	}

	public void setAddress(String address){
		this.address = address;
	}

	public String getCc(){
		return cc;
	}

	public void setCc(String cc){
		this.cc = cc;
	}

	public String getSubject(){
		return subject;
	}

	public void setSubject(String subject){
		this.subject = subject;
	}

	public String getContent(){
		return content;
	}

	public void setContent(String content){
		this.content = content;
	}

	public List<File> getAnnexFile(){
		return annexFile;
	}

	public void setAnnexFile(List<File> annexFile){
		this.annexFile = annexFile;
	}

	public boolean isHtmlFlag(){
		return htmlFlag;
	}

	public void setHtmlFlag(boolean htmlFlag){
		this.htmlFlag = htmlFlag;
	}
}