package com.linksus.entity;

import java.io.Serializable;

public class LinksusWxObjectSupply extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** 微信主id */
	private Long wxId;
	/** 标题 */
	private String title;
	/** 作者名称 */
	private String autherName;
	/** 图片原url */
	private String picOriginalUrl;
	/** 图片中图url */
	private String picMiddleUrl;
	/** 图片缩略图url */
	private String picThumbUrl;
	/** 简述 */
	private String summary;
	/** 内容 */
	private String content;
	/** 内容的url */
	private String conentUrl;
	/** 是否第一个
	        0为否
	        1为是 */
	private Integer isFirst;

	public Long getWxId(){
		return wxId;
	}

	public void setWxId(Long wxId){
		this.wxId = wxId;
	}

	public String getTitle(){
		return title;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public String getAutherName(){
		return autherName;
	}

	public void setAutherName(String autherName){
		this.autherName = autherName;
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

	public String getSummary(){
		return summary;
	}

	public void setSummary(String summary){
		this.summary = summary;
	}

	public String getContent(){
		return content;
	}

	public void setContent(String content){
		this.content = content;
	}

	public String getConentUrl(){
		return conentUrl;
	}

	public void setConentUrl(String conentUrl){
		this.conentUrl = conentUrl;
	}

	public Integer getIsFirst(){
		return isFirst;
	}

	public void setIsFirst(Integer isFirst){
		this.isFirst = isFirst;
	}
}
