package com.linksus.entity;

import java.io.Serializable;

public class LinksusInteractKeywordReplyAcct extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** 主键 */
	private Long pid;
	/** 关键字ID */
	private Long keywordId;
	/** 平台账号ID */
	private Long accountId;
	/** 回复定义ID */
	private Long replyId;
	/** 关键字内容 */
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
