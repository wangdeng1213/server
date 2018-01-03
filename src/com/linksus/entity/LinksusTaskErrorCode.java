package com.linksus.entity;

import java.io.Serializable;

public class LinksusTaskErrorCode extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** ������� */
	private Long errorCode;
	/** ����: 0 ϵͳ 1 ���� 2 ��Ѷ 3΢��*/
	private Integer errorType;
	/** �������� */
	private String errorMsg;
	/** Դ������� */
	private String srcCode;
	/** Դ������� */
	private String srcMsg;
	/** ǰ����ʾ��ʽ: 0 ������ʾ 1 ��ʾϵͳ���� */
	private Integer displayType;

	public Long getErrorCode(){
		return errorCode;
	}

	public void setErrorCode(Long errorCode){
		this.errorCode = errorCode;
	}

	public Integer getErrorType(){
		return errorType;
	}

	public void setErrorType(Integer errorType){
		this.errorType = errorType;
	}

	public String getErrorMsg(){
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg){
		this.errorMsg = errorMsg;
	}

	public String getSrcCode(){
		return srcCode;
	}

	public void setSrcCode(String srcCode){
		this.srcCode = srcCode;
	}

	public String getSrcMsg(){
		return srcMsg;
	}

	public void setSrcMsg(String srcMsg){
		this.srcMsg = srcMsg;
	}

	public Integer getDisplayType(){
		return displayType;
	}

	public void setDisplayType(Integer displayType){
		this.displayType = displayType;
	}
}
