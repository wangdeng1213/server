package com.linksus.entity;

import java.io.Serializable;

public class LinksusInteractMessage extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** 主键 互动消息ID */
	private Long pid;
	/** 互动记录ID */
	private Long recordId;
	/** 用户ID:微博用户ID */
	private Long userId;
	/** 平台账号ID */
	private Long accountId;
	/** 微博类型:1 新浪 2 腾讯 */
	private Integer accountType;
	/** 当前私信id:新浪,腾讯平台返回 */
	private Long msgId;
	/** 互动类型:1 用户发送 2 平台账户回复 */
	private Integer interactType;
	/** 内容:文本类型 */
	private String content;
	/** 图片内容(json):用户发送/账护回复的图片url */
	private String img;
	/** 图片原始名称 */
	private String imgName;
	/** 图片缩略内容(json) */
	private String imgThumb;
	/** 附件内容(json) */
	private String attatch;
	/** 附件原始名称 */
	private String attatchName;
	/** 发布类型(平台账户回复):0 即时发布 1为定时发布 */
	private Integer sendType;
	/** 回复私信id(平台账户回复):新浪私信回复需要对方发送的私信ID */
	private Long replyMsgId;
	/** 发布状态(平台账户回复):1:待发布 2:已发布 3:发布失败 */
	private Integer status;
	/** 发布时间(平台账户回复):定时发布,发布成功更新时间 */
	private Integer sendTime;
	/** 运维人员ID:(平台账户回复) */
	private Long instPersonId;
	/** 互动时间 */
	private Integer interactTime;

	/** 回复私信互动时间 */
	private Integer lastInteractTime;
	/** 信息类型 */
	private Integer msgType;
	/** 回复次数 */
	private Integer replyCount;

	/** 回复私信ID */
	private Long replyId;
	private String userName;

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

	public Integer getAccountType(){
		return accountType;
	}

	public void setAccountType(Integer accountType){
		this.accountType = accountType;
	}

	public Long getMsgId(){
		return msgId;
	}

	public void setMsgId(Long msgId){
		this.msgId = msgId;
	}

	public Integer getInteractType(){
		return interactType;
	}

	public void setInteractType(Integer interactType){
		this.interactType = interactType;
	}

	public String getContent(){
		return content;
	}

	public void setContent(String content){
		this.content = content;
	}

	public String getImg(){
		return img;
	}

	public void setImg(String img){
		this.img = img;
	}

	public String getImgName(){
		return imgName;
	}

	public void setImgName(String imgName){
		this.imgName = imgName;
	}

	public String getImgThumb(){
		return imgThumb;
	}

	public void setImgThumb(String imgThumb){
		this.imgThumb = imgThumb;
	}

	public String getAttatch(){
		return attatch;
	}

	public void setAttatch(String attatch){
		this.attatch = attatch;
	}

	public String getAttatchName(){
		return attatchName;
	}

	public void setAttatchName(String attatchName){
		this.attatchName = attatchName;
	}

	public Integer getSendType(){
		return sendType;
	}

	public void setSendType(Integer sendType){
		this.sendType = sendType;
	}

	public Long getReplyMsgId(){
		return replyMsgId;
	}

	public void setReplyMsgId(Long replyMsgId){
		this.replyMsgId = replyMsgId;
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

	public Integer getLastInteractTime(){
		return lastInteractTime;
	}

	public void setLastInteractTime(Integer lastInteractTime){
		this.lastInteractTime = lastInteractTime;
	}

	public Long getReplyId(){
		return replyId;
	}

	public void setReplyId(Long replyId){
		this.replyId = replyId;
	}

	public String getUserName(){
		return userName;
	}

	public void setUserName(String userName){
		this.userName = userName;
	}

	public Integer getMsgType(){
		return msgType;
	}

	public void setMsgType(Integer msgType){
		this.msgType = msgType;
	}

	public Integer getReplyCount(){
		return replyCount;
	}

	public void setReplyCount(Integer replyCount){
		this.replyCount = replyCount;
	}
}
