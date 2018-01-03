package com.linksus.entity;

import java.io.Serializable;

public class LinksusInteractWxArticle extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** ����:��ͼ�İ��������� */
	private Long pid;
	/** �ز�ID */
	private Long materialId;
	/** ���� */
	private String title;
	/** ���� */
	private String autherName;
	/** ժҪ */
	private String summary;
	/** ԭͼurl */
	private String picOriginalUrl;
	/** ��ͼurl */
	private String picMiddleUrl;
	/** ��΢ͼurl */
	private String picThumbUrl;
	/** ���� */
	private String content;
	/** ���ݵ�url */
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
