package com.linksus.entity;

import java.io.File;
import java.io.Serializable;
import java.util.List;

public class Email implements Serializable{

	private static final long serialVersionUID = 1L;
	/** �ռ��� ����Զ��ŷָ� **/
	private String address;
	/** ���� **/
	private String bcc;
	/** ���͸� ����Զ��ŷָ�**/
	private String cc;
	/** �ʼ����� **/
	private String subject;
	/** �ʼ����� **/
	private String content;
	/** ���� **/
	private List<File> annexFile;
	/** HTML��ʽ*/
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