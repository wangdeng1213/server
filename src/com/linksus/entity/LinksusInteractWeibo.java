package com.linksus.entity;

import java.io.Serializable;

public class LinksusInteractWeibo extends BaseEntity implements Serializable{

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
	/** 当前评论的id */
	private Long commentId;
	/** 微博ID */
	private Long weiboId;
	/** 源微博ID(转发/@时源微博 */
	private Long sourceWeiboId;
	/** 内容:转发/@/评论内容 */
	private String content;
	/** 互动类型:1 评论 2 转发 3 @ 4 评论并@ 7 平台账户回复 */
	private Integer interactType;
	/** 回复评论id(平台账户回复) */
	private Long replyMsgId;
	/** 发布类型(平台账户回复):0 及时发布 1为定时发布 */
	private Integer sendType;
	/** 发布状态(平台账户回复):1:待发布 2:已发布 3:发布失败 */
	private Integer status;
	/** 发布时间:(平台账户回复) 定时发布,发布成功需更新 */
	private Integer sendTime;
	/** 运维人员ID:(平台账户回复) */
	private Long instPersonId;
	/** 互动时间 */
	private Integer interactTime;
	/**回复评论id*/
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
