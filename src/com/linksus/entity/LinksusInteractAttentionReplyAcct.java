package com.linksus.entity;

import java.io.Serializable;

public class LinksusInteractAttentionReplyAcct extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** ���� */
	private Long pid;
	/** �ظ�����ID */
	private Long replyId;
	/** ƽ̨�˺�ID */
	private Long accountId;

	public Long getPid(){
		return pid;
	}

	public void setPid(Long pid){
		this.pid = pid;
	}

	public Long getReplyId(){
		return replyId;
	}

	public void setReplyId(Long replyId){
		this.replyId = replyId;
	}

	public Long getAccountId(){
		return accountId;
	}

	public void setAccountId(Long accountId){
		this.accountId = accountId;
	}
}
