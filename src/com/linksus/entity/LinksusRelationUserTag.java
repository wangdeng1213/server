package com.linksus.entity;

import java.io.Serializable;

public class LinksusRelationUserTag extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** ΢���û�ID ����΢���û��� */
	private Long userId;
	/** ��ǩ */
	private String tag;

	/** ��Դ**/
	private Long tagId;

	public Long getUserId(){
		return userId;
	}

	public void setUserId(Long userId){
		this.userId = userId;
	}

	public String getTag(){
		return tag;
	}

	public void setTag(String tag){
		this.tag = tag;
	}

	public Long getTagId(){
		return tagId;
	}

	public void setTagId(Long tagId){
		this.tagId = tagId;
	}

}
