package com.linksus.entity;

import java.io.Serializable;

public class LinksusInteractKeywordReply extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** ���� */
	private Long pid;
	/** �ؼ���ID */
	private Long keywordId;
	/** ��Ϣ����:1 �ı���Ϣ 2 ��ͼ����Ϣ 3 ��ͼ����Ϣ */
	private Integer msgType;
	/** �ı���Ϣ(�ı���Ϣ) */
	private String content;
	/** �ز�ID(ͼ����Ϣ) */
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
