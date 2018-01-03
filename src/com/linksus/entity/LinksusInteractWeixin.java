package com.linksus.entity;

import java.io.Serializable;

public class LinksusInteractWeixin extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** 主键 互动消息ID */
	private Long pid;
	/** 互动记录ID */
	private Long recordId;
	/** 用户ID:微信用户ID */
	private Long userId;
	/** 平台账号ID */
	private Long accountId;
	/** 微信openid:账号回复最新一条互动,使用该记录的openid和对应账号token */
	private String openid;
	/** 内容:微信文本信息(语音,视频类型显示url) */
	private String content;
	/** 互动类型:1 用户发布 2 账户回复 */
	private Integer interactType;
	/** 信息类型:1 文本消息 2 图片消息 3 语音 4 视频 5(回复)单图文信息 6(回复)多图文信息 */
	private Integer msgType;
	/** 素材ID(图文信息) */
	private Long materialId;
	/** 发布类型(平台账户回复):0 即时发布 1为定时发布 */
	private Integer sendType;
	/** 发布状态(平台账户回复):1:待发布 2:已发布 3:发布失败 */
	private Integer status;
	/** 发布时间(平台账户回复):定时发布 */
	private Integer sendTime;
	/** 运维人员ID:(平台账户回复) */
	private Long instPersonId;
	/** 互动时间 */
	private Integer interactTime;

	public Long getPid(){
		return pid;
	}

	public void setPid(Long pid){
		this.pid = pid;
	}

	public Long getRecordId(){
		return recordId;
	}

	public void setRecordId(Long recordId){
		this.recordId = recordId;
	}

	public Long getUserId(){
		return userId;
	}

	public void setUserId(Long userId){
		this.userId = userId;
	}

	public Long getAccountId(){
		return accountId;
	}

	public void setAccountId(Long accountId){
		this.accountId = accountId;
	}

	public String getOpenid(){
		return openid;
	}

	public void setOpenid(String openid){
		this.openid = openid;
	}

	public String getContent(){
		return content;
	}

	public void setContent(String content){
		this.content = content;
	}

	public Integer getInteractType(){
		return interactType;
	}

	public void setInteractType(Integer interactType){
		this.interactType = interactType;
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

	public Integer getSendType(){
		return sendType;
	}

	public void setSendType(Integer sendType){
		this.sendType = sendType;
	}

	public Integer getStatus(){
		return status;
	}

	public void setStatus(Integer status){
		this.status = status;
	}

	public Integer getSendTime(){
		return sendTime;
	}

	public void setSendTime(Integer sendTime){
		this.sendTime = sendTime;
	}

	public Long getInstPersonId(){
		return instPersonId;
	}

	public void setInstPersonId(Long instPersonId){
		this.instPersonId = instPersonId;
	}

	public Integer getInteractTime(){
		return interactTime;
	}

	public void setInteractTime(Integer interactTime){
		this.interactTime = interactTime;
	}

}
