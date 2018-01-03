package com.linksus.entity;

import java.io.Serializable;

public class LinksusTaskInteractWeiboMid extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** 微博用户标识ID */
	private Long rpsId;
	/** 用户ID */
	private Long userId;
	/** 人员Id*/
	private Long personId;
	/** 平台账号ID */
	private Long accountId;
	/** 当前评论的id */
	private Long commentId;
	/** 回复评论id */
	private Long replyId;
	/** 微博ID */
	private Long mid;
	/** 内容:转发/@/评论内容 */
	private String content;
	/** 互动类型:1 评论 2 转发 3 @ 4 评论并@ */
	private Integer interactType;
	/** 互动时间 */
	private Integer interactTime;

	public Long getRpsId(){
		return rpsId;
	}

	public void setRpsId(Long rpsId){
		this.rpsId = rpsId;
	}

	public Long getAccountId(){
		return accountId;
	}

	public void setAccountId(Long accountId){
		this.accountId = accountId;
	}

	public Long getCommentId(){
		return commentId;
	}

	public void setCommentId(Long commentId){
		this.commentId = commentId;
	}

	public Long getReplyId(){
		return replyId;
	}

	public void setReplyId(Long replyId){
		this.replyId = replyId;
	}

	public Long getMid(){
		return mid;
	}

	public void setMid(Long mid){
		this.mid = mid;
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

	public Integer getInteractTime(){
		return interactTime;
	}

	public void setInteractTime(Integer interactTime){
		this.interactTime = interactTime;
	}

	public Long getUserId(){
		return userId;
	}

	public void setUserId(Long userId){
		this.userId = userId;
	}

	public Long getPersonId(){
		return personId;
	}

	public void setPersonId(Long personId){
		this.personId = personId;
	}
}
