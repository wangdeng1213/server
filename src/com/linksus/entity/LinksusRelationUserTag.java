package com.linksus.entity;

import java.io.Serializable;

public class LinksusRelationUserTag extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** 微博用户ID 关联微博用户表 */
	private Long userId;
	/** 标签 */
	private String tag;

	/** 来源**/
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
