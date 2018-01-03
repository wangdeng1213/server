package com.linksus.entity;

import java.io.Serializable;

public class LinksusMassArticle implements Serializable{

	//图文消息缩略图的media_id
	private String thumb_media_id;
	//图文消息的作者 
	private String author;
	//图文消息的标题 
	private String title;
	//在图文消息页面点击“阅读原文”后的页面
	private String content_source_url;
	//图文消息页面的内容，支持HTML标签 
	private String content;
	//图文消息的描述 
	private String digest;

	public String getThumb_media_id(){
		return thumb_media_id;
	}

	public void setThumb_media_id(String thumbMediaId){
		thumb_media_id = thumbMediaId;
	}

	public String getAuthor(){
		return author;
	}

	public void setAuthor(String author){
		this.author = author;
	}

	public String getTitle(){
		return title;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public String getContent_source_url(){
		return content_source_url;
	}

	public void setContent_source_url(String contentSourceUrl){
		content_source_url = contentSourceUrl;
	}

	public String getContent(){
		return content;
	}

	public void setContent(String content){
		this.content = content;
	}

	public String getDigest(){
		return digest;
	}

	public void setDigest(String digest){
		this.digest = digest;
	}

}
