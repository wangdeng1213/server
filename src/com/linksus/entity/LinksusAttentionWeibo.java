package com.linksus.entity;

import java.io.Serializable;

public class LinksusAttentionWeibo extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** ����id */
	private Long id;
	/** �˻�id */
	private Long institutionId;
	/** �ʺ�id */
	private Long accountId;
	/** �ʺ�����
	        1����
	        2��Ѷ */
	private Integer accountType;
	/** ΢��id */
	private Long mid;
	/** ���� */
	private String content;
	/** ԭͼ */
	private String picOriginalUrl;
	/** ��ͼ */
	private String picMiddleUrl;
	/** Сͼ */
	private String picThumbUrl;
	/** ��Ƶ��ַ */
	private String musicUrl;
	/** ��Ƶ��ַ */
	private String videoUrl;
	/** ������ */
	private Integer commentCount;
	/** ת���� */
	private Integer repostCount;
	/** ԭid */
	private Long srcMid;
	/** ��ַ��Ϣ */
	private String geo;
	private String user;
	/** �û�id */
	private Long uid;
	/** �û����� */
	private String uname;
	/** �û�ͷ�� */
	private String uprofileUrl;
	/** ��������
	        ��������
	        00000
	        ��һλ ͼƬ
	        �ڶ�λ ��Ƶ
	        ����λ ����
	        ����λ ����
	        ����λ ���� */
	private String contentType;
	/** ��������
	        0ֱ��
	        1ת�� */
	private Integer publishType;
	/** ��Դ */
	private String source;
	/** ����ʱ�� */
	private Integer createdTime;
	/** ΢����URL */
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
