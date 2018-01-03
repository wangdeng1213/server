package com.linksus.entity;

import java.io.Serializable;

public class LinksusInteractAttentionReply extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** ���� */
	private Long pid;
	/** ����ID:�������� */
	private Long institutionId;
	/** ƽ̨����:1 ΢�� 2 ΢�� */
	private Integer replyType;
	/** �ı���Ϣ */
	private String content;
	/** ��Ϣ����:1 �ı���Ϣ 2 (΢��)��ͼ����Ϣ 3 (΢��)��ͼ����Ϣ */
	private Integer msgType;
	/** �ز�ID(ͼ����Ϣ) */
	private Long materialId;

	public Long getPid(){
		return pid;
	}

	public void setPid(Long pid){
		this.pid = pid;
	}

	public Long getInstitutionId(){
		return institutionId;
	}

	public void setInstitutionId(Long institutionId){
		this.institutionId = institutionId;
	}

	public Integer getReplyType(){
		return replyType;
	}

	public void setReplyType(Integer replyType){
		this.replyType = replyType;
	}

	public String getContent(){
		return content;
	}

	public void setContent(String content){
		this.content = content;
	}

	public Integer getMsgType(){
		return msgType;
	}

	public void setMsgType(Integer msgType){
		this.msgType = msgType;
	}

	public Long getMaterialId(){
		return materialId;
	}

	public void setMaterialId(Long materialId){
		this.materialId = materialId;
	}
}
