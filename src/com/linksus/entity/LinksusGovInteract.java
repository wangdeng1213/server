package com.linksus.entity;

import java.io.Serializable;

public class LinksusGovInteract extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	/** 互动ID 主键 */
	private Long interactId;
	/** 流水号 */
	private Long runId;
	/** 用户ID:微博用户ID */
	private Long userId;
	/** 平台账号ID */
	private Long accountId;
	/** 当前评论id */
	private Long commentId;
	/** 微博ID */
	private Long weiboId;
	/** 原微博id（转发） */
	private Long sourceWeiboId;
	/** 内容:转发/@/评论内容  */
	private String content;
	/** 互动类型:1 评论 2 转发 3 @ 4 评论并@ 7 平台账户回复 */
	private Integer interactType;
	/** 回复评论id(平台账户回复) */
	private Long replyCommentId;
	/** 发布类型(平台账户回复):0 及时发布 1为定时发布 */
	private Integer sendType;
	/** 发布状态(平台账户回复):1:待发布 2:已发布 3:发布失败 */
	private Integer status;
	/** 发布时间:(平台账户回复) 定时发布,发布成功需更新 */
	private Integer sendTime;
	/** 互动时间 */
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
