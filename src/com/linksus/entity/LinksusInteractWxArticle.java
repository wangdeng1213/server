package com.linksus.entity;

import java.io.Serializable;

public class LinksusInteractWxArticle extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** 主键:多图文按主键升序 */
	private Long pid;
	/** 素材ID */
	private Long materialId;
	/** 标题 */
	private String title;
	/** 作者 */
	private String autherName;
	/** 摘要 */
	private String summary;
	/** 原图url */
	private String picOriginalUrl;
	/** 中图url */
	private String picMiddleUrl;
	/** 略微图url */
	private String picThumbUrl;
	/** 内容 */
	private String content;
	/** 内容的url */
	private String conentUrl;

	public Long getPid(){
		return pid;
	}

	public void setPid(Long pid){
		this.pid = pid;
	}

	public Long getMaterialId(){
		return materialId;
	}

	public void setMaterialId(Long materialId){
		this.materialId = materialId;
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

	public String getSummary(){
		return summary;
	}

	public void setSummary(String summary){
		this.summary = summary;
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
}
