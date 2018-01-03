package com.linksus.entity;

import java.io.Serializable;

public class LinksusTaskInteractGovMid extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** ƽ̨�˺�ID */
	private Long accountId;
	/** ��ǰ���۵�id */
	private Long commentId;
	/** ����ID */
	private Long mid;
	/** �����û���ʶID */
	private Long rpsId;
	/** ��ԱID:������Ա���� */
	private Long personId;
	/** �û�ID */
	private Long userId;
	/** �ظ�����id */
	private Long replyId;
	/** ����:ת��/@/�������� */
	private String content;
	/** ��������:1 ���� 2 ת�� 3 @ 4 ���۲�@ */
	private Integer interactType;
	/** ����ʱ�� */
	private Integer interactTime;

	private Long replyCommentId;

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

	public Long getMid(){
		return mid;
	}

	public void setMid(Long mid){
		this.mid = mid;
	}

	public Long getRpsId(){
		return rpsId;
	}

	public void setRpsId(Long rpsId){
		this.rpsId = rpsId;
	}

	public Long getPersonId(){
		return personId;
	}

	public void setPersonId(Long personId){
		this.personId = personId;
	}

	public Long getUserId(){
		return userId;
	}

	public void setUserId(Long userId){
		this.userId = userId;
	}

	public Long getReplyId(){
		return replyId;
	}

	public void setReplyId(Long replyId){
		this.replyId = replyId;
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

	public Long getReplyCommentId(){
		return replyCommentId;
	}

	public void setReplyCommentId(Long replyCommentId){
		this.replyCommentId = replyCommentId;
	}

}
