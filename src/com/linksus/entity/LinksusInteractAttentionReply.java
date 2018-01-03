package com.linksus.entity;

import java.io.Serializable;

public class LinksusInteractAttentionReply extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** 主键 */
	private Long pid;
	/** 机构ID:创建机构 */
	private Long institutionId;
	/** 平台类型:1 微博 2 微信 */
	private Integer replyType;
	/** 文本信息 */
	private String content;
	/** 信息类型:1 文本消息 2 (微信)单图文消息 3 (微信)多图文信息 */
	private Integer msgType;
	/** 素材ID(图文信息) */
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
