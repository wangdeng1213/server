package com.linksus.entity;

import java.io.Serializable;

public class LinksusInteractWeibo extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** ���� ������ϢID */
	private Long pid;
	/** ������¼ID */
	private Long recordId;
	/** �û�ID:΢���û�ID */
	private Long userId;
	/** ƽ̨�˺�ID */
	private Long accountId;
	/** ΢������:1 ���� 2 ��Ѷ */
	private Integer accountType;
	/** ��ǰ���۵�id */
	private Long commentId;
	/** ΢��ID */
	private Long weiboId;
	/** Դ΢��ID(ת��/@ʱԴ΢�� */
	private Long sourceWeiboId;
	/** ����:ת��/@/�������� */
	private String content;
	/** ��������:1 ���� 2 ת�� 3 @ 4 ���۲�@ 7 ƽ̨�˻��ظ� */
	private Integer interactType;
	/** �ظ�����id(ƽ̨�˻��ظ�) */
	private Long replyMsgId;
	/** ��������(ƽ̨�˻��ظ�):0 ��ʱ���� 1Ϊ��ʱ���� */
	private Integer sendType;
	/** ����״̬(ƽ̨�˻��ظ�):1:������ 2:�ѷ��� 3:����ʧ�� */
	private Integer status;
	/** ����ʱ��:(ƽ̨�˻��ظ�) ��ʱ����,�����ɹ������ */
	private Integer sendTime;
	/** ��ά��ԱID:(ƽ̨�˻��ظ�) */
	private Long instPersonId;
	/** ����ʱ�� */
	private Integer interactTime;
	/**�ظ�����id*/
	private Long replyId;

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

	public Long getReplyMsgId(){
		return replyMsgId;
	}

	public void setReplyMsgId(Long replyMsgId){
		this.replyMsgId = replyMsgId;
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

	public Long getReplyId(){
		return replyId;
	}

	public void setReplyId(Long replyId){
		this.replyId = replyId;
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
