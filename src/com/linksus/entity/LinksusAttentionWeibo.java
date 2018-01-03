package com.linksus.entity;

import java.io.Serializable;

public class LinksusAttentionWeibo extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** 主键id */
	private Long id;
	/** 账户id */
	private Long institutionId;
	/** 帐号id */
	private Long accountId;
	/** 帐号类型
	        1新浪
	        2腾讯 */
	private Integer accountType;
	/** 微博id */
	private Long mid;
	/** 内容 */
	private String content;
	/** 原图 */
	private String picOriginalUrl;
	/** 中图 */
	private String picMiddleUrl;
	/** 小图 */
	private String picThumbUrl;
	/** 音频地址 */
	private String musicUrl;
	/** 视频地址 */
	private String videoUrl;
	/** 评论数 */
	private Integer commentCount;
	/** 转发数 */
	private Integer repostCount;
	/** 原id */
	private Long srcMid;
	/** 地址信息 */
	private String geo;
	private String user;
	/** 用户id */
	private Long uid;
	/** 用户名称 */
	private String uname;
	/** 用户头像 */
	private String uprofileUrl;
	/** 内容类型
	        内容类型
	        00000
	        第一位 图片
	        第二位 视频
	        第三位 音乐
	        第四位 锻炼
	        第五位 话题 */
	private String contentType;
	/** 发布类型
	        0直发
	        1转发 */
	private Integer publishType;
	/** 来源 */
	private String source;
	/** 创建时间 */
	private Integer createdTime;
	/** 微博的URL */
	private String currentUrl;

	public String getUser(){
		return user;
	}

	public void setUser(String user){
		this.user = user;
	}

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

	public Integer getAccountType(){
		return accountType;
	}

	public void setAccountType(Integer accountType){
		this.accountType = accountType;
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

	public Integer getCommentCount(){
		return commentCount;
	}

	public void setCommentCount(Integer commentCount){
		this.commentCount = commentCount;
	}

	public Integer getRepostCount(){
		return repostCount;
	}

	public void setRepostCount(Integer repostCount){
		this.repostCount = repostCount;
	}

	public Long getSrcMid(){
		return srcMid;
	}

	public void setSrcMid(Long srcMid){
		this.srcMid = srcMid;
	}

	public String getGeo(){
		return geo;
	}

	public void setGeo(String geo){
		this.geo = geo;
	}

	public Long getUid(){
		return uid;
	}

	public void setUid(Long uid){
		this.uid = uid;
	}

	public String getUname(){
		return uname;
	}

	public void setUname(String uname){
		this.uname = uname;
	}

	public String getUprofileUrl(){
		return uprofileUrl;
	}

	public void setUprofileUrl(String uprofileUrl){
		this.uprofileUrl = uprofileUrl;
	}

	public String getContentType(){
		return contentType;
	}

	public void setContentType(String contentType){
		this.contentType = contentType;
	}

	public Integer getPublishType(){
		return publishType;
	}

	public void setPublishType(Integer publishType){
		this.publishType = publishType;
	}

	public String getSource(){
		return source;
	}

	public void setSource(String source){
		this.source = source;
	}

	public Integer getCreatedTime(){
		return createdTime;
	}

	public void setCreatedTime(Integer createdTime){
		this.createdTime = createdTime;
	}

	public String getCurrentUrl(){
		return currentUrl;
	}

	public void setCurrentUrl(String currentUrl){
		this.currentUrl = currentUrl;
	}
}
