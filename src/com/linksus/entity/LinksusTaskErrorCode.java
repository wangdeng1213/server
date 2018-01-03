package com.linksus.entity;

import java.io.Serializable;

public class LinksusTaskErrorCode extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** 错误代码 */
	private Long errorCode;
	/** 类型: 0 系统 1 新浪 2 腾讯 3微信*/
	private Integer errorType;
	/** 错误描述 */
	private String errorMsg;
	/** 源错误代码 */
	private String srcCode;
	/** 源错误代码 */
	private String srcMsg;
	/** 前端显示方式: 0 正常显示 1 显示系统错误 */
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
