package com.linksus.entity;

import java.io.Serializable;

public class LinksusGovInteract extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	/** ����ID ���� */
	private Long interactId;
	/** ��ˮ�� */
	private Long runId;
	/** �û�ID:΢���û�ID */
	private Long userId;
	/** ƽ̨�˺�ID */
	private Long accountId;
	/** ��ǰ����id */
	private Long commentId;
	/** ΢��ID */
	private Long weiboId;
	/** ԭ΢��id��ת���� */
	private Long sourceWeiboId;
	/** ����:ת��/@/��������  */
	private String content;
	/** ��������:1 ���� 2 ת�� 3 @ 4 ���۲�@ 7 ƽ̨�˻��ظ� */
	private Integer interactType;
	/** �ظ�����id(ƽ̨�˻��ظ�) */
	private Long replyCommentId;
	/** ��������(ƽ̨�˻��ظ�):0 ��ʱ���� 1Ϊ��ʱ���� */
	private Integer sendType;
	/** ����״̬(ƽ̨�˻��ظ�):1:������ 2:�ѷ��� 3:����ʧ�� */
	private Integer status;
	/** ����ʱ��:(ƽ̨�˻��ظ�) ��ʱ����,�����ɹ������ */
	private Integer sendTime;
	/** ����ʱ�� */
	private Integer interactTime;

	private Long replyId;

	public Long getInteractId(){
		return interactId;
	}

	public void setInteractId(Long interactId){
		this.interactId = interactId;
	}

	public Long getRunId(){
		return runId;
	}

	public void setRunId(Long runId){
		this.runId = runId;
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

	public Long getCommentId(){
		return commentId;
	}

	public void setCommentId(Long commentId){
		this.commentId = commentId;
	}

	public Long getWeiboId(){
		return weiboId;
	}

	public void setWeiboId(Long weiboId){
		this.weiboId = weiboId;
	}

	public Long getSourceWeiboId(){
		return sourceWeiboId;
	}

	public void setSourceWeiboId(Long sourceWeiboId){
		this.sourceWeiboId = sourceWeiboId;
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

	public Long getReplyCommentId(){
		return replyCommentId;
	}

	public void setReplyCommentId(Long replyCommentId){
		this.replyCommentId = replyCommentId;
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

	public Integer getInteractTime(){
		return interactTime;
	}

	public void setInteractTime(Integer interactTime){
		this.interactTime = interactTime;
	}

	public Long getReplyId(){
		return replyId;
	}

	public void setReplyId(Long replyId){
		this.replyId = replyId;
	}

}
