package com.linksus.entity;

import java.io.Serializable;

public class LinksusInteractKeywordReplyAcct extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** ���� */
	private Long pid;
	/** �ؼ���ID */
	private Long keywordId;
	/** ƽ̨�˺�ID */
	private Long accountId;
	/** �ظ�����ID */
	private Long replyId;
	/** �ؼ������� */
	private String keywordName;

	public Long getPid(){
		return pid;
	}

	public void setPid(Long pid){
		this.pid = pid;
	}

	public Long getKeywordId(){
		return keywordId;
	}

	public void setKeywordId(Long keywordId){
		this.keywordId = keywordId;
	}

	public Long getAccountId(){
		return accountId;
	}

	public void setAccountId(Long accountId){
		this.accountId = accountId;
	}

	public Long getReplyId(){
		return replyId;
	}

	public void setReplyId(Long replyId){
		this.replyId = replyId;
	}

	public String getKeywordName(){
		return keywordName;
	}

	public void setKeywordName(String keywordName){
		this.keywordName = keywordName;
	}

}
