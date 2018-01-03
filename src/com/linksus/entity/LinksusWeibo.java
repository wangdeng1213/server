package com.linksus.entity;

import java.io.Serializable;

public class LinksusWeibo extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/**  */
	private Long id;
	/** 账户id */
	private Long institutionId;
	/** 帐号id */
	private Long accountId;
	/** 创建时间 */
	private Integer createdTime;
	/** 发送时间 */
	private Integer sendTime;
	/** 帐号名称 */
	private String accountName;
	/** 帐号类型
	        1新浪
	        2腾讯 */
	private Integer accountType;
	/** 微博内容 */
	private String content;
	/** 原图的url */
	private String picOriginalUrl;
	/** 中图的url */
	private String picMiddleUrl;
	/** 缩略图的url */
	private String picThumbUrl;
	/** 音频地址 */
	private String musicUrl;
	/** 视频地址 */
	private String videoUrl;
	/** 创建人 数字 */
	private Long authorId;
	/** 创建人 字符串 */
	private String authorName;
	/** 当前所发布的微博的状态
	        0              草稿
	        1	预发布
	        2	发布失败
	        3	已发布
	        4	审批未通过
	        5	审批中
	        98	已删除
	        99	暂停中 */
	private Integer status;
	/** 当前审核的状态
	        00 未审批
	        10 一级审批完成
	        11 一级 二级审批均完成 */
	private String applyStatus;
	/** 当前发布的状态
	        0为 及时发布
	        1为定时发布 */
	private Integer publishStauts;
	/** 发布类型 
	        0为直发
	        1为转发 */
	private Integer publishType;
	/** 内容类型
	        00000
	        第一位 图片
	        第二位 视频
	        第三位 音乐
	        第四位 锻炼
	        第五位 话题 */
	private String contentType;
	/** 当前的信息是否已经归档
	        0为 未归档
	        1为 已归档 */
	private Integer toFile;
	/** 原微博id */
	private Long srcid;
	/** 原微博的url */
	private String srcurl;
	/** 微博id */
	private Long mid;
	/** 评论数  */
	private Integer repostCount;
	/** 评论数 */
	private Integer commentCount;
	/** 来源id */
	private Long sourceId;
	/** 来源名称 */
	private String sourceName;
	/** 微博的URL */
	private String currentUrl;
	/** 发布来源:0发布,1采购 */
	private Integer publishSource;
	/** 发布为0,采购为taskid */
	private Long referId;

	/** 定时任务标志 */
	private boolean regularFlag = false;
	/**定时任务重发次数 */
	private int reSendCount;
	/** 微博失败原因 */
	private String errmsg;

	public Long getId(){
		return id;
	}

	public void setId(Long id){
		this.id = id;
	}

	public Long getInstitutionId(){
		return institutionId;
	}

	public void setInstitutionId(Long institutionId){
		this.institutionId = institutionId;
	}

	public Long getAccountId(){
		return accountId;
	}

	public void setAccountId(Long accountId){
		this.accountId = accountId;
	}

	public Integer getCreatedTime(){
		return createdTime;
	}

	public void setCreatedTime(Integer createdTime){
		this.createdTime = createdTime;
	}

	public Integer getSendTime(){
		return sendTime;
	}

	public void setSendTime(Integer sendTime){
		this.sendTime = sendTime;
	}

	public String getAccountName(){
		return accountName;
	}

	public void setAccountName(String accountName){
		this.accountName = accountName;
	}

	public Integer getAccountType(){
		return accountType;
	}

	public void setAccountType(Integer accountType){
		this.accountType = accountType;
	}

	public String getContent(){
		return content;
	}

	public void setContent(String content){
		this.content = content;
	}

	public String getPicOriginalUrl(){
		return picOriginalUrl;
	}

	public void setPicOriginalUrl(String picOriginalUrl){
		this.picOriginalUrl = picOriginalUrl;
	}

	public String getPicMiddleUrl(){
		return picMiddleUrl;
	}

	public void setPicMiddleUrl(String picMiddleUrl){
		this.picMiddleUrl = picMiddleUrl;
	}

	public String getPicThumbUrl(){
		return picThumbUrl;
	}

	public void setPicThumbUrl(String picThumbUrl){
		this.picThumbUrl = picThumbUrl;
	}

	public String getMusicUrl(){
		return musicUrl;
	}

	public void setMusicUrl(String musicUrl){
		this.musicUrl = musicUrl;
	}

	public String getVideoUrl(){
		return videoUrl;
	}

	public void setVideoUrl(String videoUrl){
		this.videoUrl = videoUrl;
	}

	public Long getAuthorId(){
		return authorId;
	}

	public void setAuthorId(Long authorId){
		this.authorId = authorId;
	}

	public String getAuthorName(){
		return authorName;
	}

	public void setAuthorName(String authorName){
		this.authorName = authorName;
	}

	public Integer getStatus(){
		return status;
	}

	public void setStatus(Integer status){
		this.status = status;
	}

	public String getApplyStatus(){
		return applyStatus;
	}

	public void setApplyStatus(String applyStatus){
		this.applyStatus = applyStatus;
	}

	public Integer getPublishStauts(){
		return publishStauts;
	}

	public void setPublishStauts(Integer publishStauts){
		this.publishStauts = publishStauts;
	}

	public Integer getPublishType(){
		return publishType;
	}

	public void setPublishType(Integer publishType){
		this.publishType = publishType;
	}

	public String getContentType(){
		return contentType;
	}

	public void setContentType(String contentType){
		this.contentType = contentType;
	}

	public Integer getToFile(){
		return toFile;
	}

	public void setToFile(Integer toFile){
		this.toFile = toFile;
	}

	public Long getSrcid(){
		return srcid;
	}

	public void setSrcid(Long srcid){
		this.srcid = srcid;
	}

	public String getSrcurl(){
		return srcurl;
	}

	public void setSrcurl(String srcurl){
		this.srcurl = srcurl;
	}

	public Long getMid(){
		return mid;
	}

	public void setMid(Long mid){
		this.mid = mid;
	}

	public Integer getRepostCount(){
		return repostCount;
	}

	public void setRepostCount(Integer repostCount){
		this.repostCount = repostCount;
	}

	public Integer getCommentCount(){
		return commentCount;
	}

	public void setCommentCount(Integer commentCount){
		this.commentCount = commentCount;
	}

	public Long getSourceId(){
		return sourceId;
	}

	public void setSourceId(Long sourceId){
		this.sourceId = sourceId;
	}

	public String getSourceName(){
		return sourceName;
	}

	public void setSourceName(String sourceName){
		this.sourceName = sourceName;
	}

	public String getCurrentUrl(){
		return currentUrl;
	}

	public void setCurrentUrl(String currentUrl){
		this.currentUrl = currentUrl;
	}

	public Integer getPublishSource(){
		return publishSource;
	}

	public void setPublishSource(Integer publishSource){
		this.publishSource = publishSource;
	}

	public Long getReferId(){
		return referId;
	}

	public void setReferId(Long referId){
		this.referId = referId;
	}

	public int getReSendCount(){
		return reSendCount;
	}

	public void setReSendCount(int reSendCount){
		this.reSendCount = reSendCount;
	}

	public boolean isRegularFlag(){
		return regularFlag;
	}

	public void setRegularFlag(boolean regularFlag){
		this.regularFlag = regularFlag;
	}

	public String getErrmsg(){
		return errmsg;
	}

	public void setErrmsg(String errmsg){
		this.errmsg = errmsg;
	}
}
