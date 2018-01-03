package com.linksus.entity;

import java.io.Serializable;

public class LinksusInteractKeywordReply extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** 主键 */
	private Long pid;
	/** 关键字ID */
	private Long keywordId;
	/** 信息类型:1 文本消息 2 单图文消息 3 多图文信息 */
	private Integer msgType;
	/** 文本信息(文本消息) */
	private String content;
	/** 素材ID(图文信息) */
	private Long materialId;

	public Long getPid(){
		return pid;
	}

	public void setPid(Long pid){
		this.pid = pid;
	}

	public Long getKeywordId(){
		return keywordId;
	}

	public void setKeywordId(Long keywordId){
		this.keywordId = keywordId;
	}

	public Integer getMsgType(){
		return msgType;
	}

	public void setMsgType(Integer msgType){
		this.msgType = msgType;
	}

	public String getContent(){
		return content;
	}

	public void setContent(String content){
		this.content = content;
	}

	public Long getMaterialId(){
		return materialId;
	}

	public void setMaterialId(Long materialId){
		this.materialId = materialId;
	}
}
