package com.linksus.entity;

import java.io.Serializable;

public class LinksusMassArticle implements Serializable{

	//ͼ����Ϣ����ͼ��media_id
	private String thumb_media_id;
	//ͼ����Ϣ������ 
	private String author;
	//ͼ����Ϣ�ı��� 
	private String title;
	//��ͼ����Ϣҳ�������Ķ�ԭ�ġ����ҳ��
	private String content_source_url;
	//ͼ����Ϣҳ������ݣ�֧��HTML��ǩ 
	private String content;
	//ͼ����Ϣ������ 
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
