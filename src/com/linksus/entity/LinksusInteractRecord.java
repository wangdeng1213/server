package com.linksus.entity;

import java.io.Serializable;

public class LinksusInteractRecord extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** 互动记录ID:主键 */
	private Long recordId;
	/** 用户ID:微博/微信用户ID */
	private Long userId;
	/** 平台账号ID */
	private Long accountId;
	/** 帐号类型:1 新浪 2 腾讯 3 微信 */
	private Integer accountType;
	/** 互动类型:1 评论 2 转发 3 @ 4 评论并@ 5 私信 6 微信 */
	private Integer interactType;
	/** 微博ID:互动类型为1-4的有值 */
	private Long weiboId;
	/** 伪互动标志:1 是 0 否 */
	private Integer fakeFlag;
	/** 新消息标志:1 存在新消息 0 无新消息 */
	private Integer newMsgFlag;
	/** 互动信息ID:用户最新互动消息ID */
	private Long messageId;
	/** 更新时间 */
	private Integer updateTime;

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

	public Integer getInteractType(){
		return interactType;
	}

	public void setInteractType(Integer interactType){
		this.interactType = interactType;
	}

	public Long getWeiboId(){
		return weiboId;
	}

	public void setWeiboId(Long weiboId){
		this.weiboId = weiboId;
	}

	public Integer getFakeFlag(){
		return fakeFlag;
	}

	public void setFakeFlag(Integer fakeFlag){
		this.fakeFlag = fakeFlag;
	}

	public Long getMessageId(){
		return messageId;
	}

	public void setMessageId(Long messageId){
		this.messageId = messageId;
	}

	public Integer getUpdateTime(){
		return updateTime;
	}

	public void setUpdateTime(Integer updateTime){
		this.updateTime = updateTime;
	}

	public Integer getNewMsgFlag(){
		return newMsgFlag;
	}

	public void setNewMsgFlag(Integer newMsgFlag){
		this.newMsgFlag = newMsgFlag;
	}
}
