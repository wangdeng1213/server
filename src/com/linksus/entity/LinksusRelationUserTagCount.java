package com.linksus.entity;

import java.io.Serializable;

public class LinksusRelationUserTagCount extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** ��ǩ */
	private String tag;
	/** ��ǩ����:1 ΢���û� 2 ���� 3 ��Ѷ 4 ��Ա (��ʱֻ��1) */
	private Integer tagType;
	/** ͳ����ֵ */
	private Integer totalCount;

	public String getTag(){
		return tag;
	}

	public void setTag(String tag){
		this.tag = tag;
	}

	public Integer getTagType(){
		return tagType;
	}

	public void setTagType(Integer tagType){
		this.tagType = tagType;
	}

	public Integer getTotalCount(){
		return totalCount;
	}

	public void setTotalCount(Integer totalCount){
		this.totalCount = totalCount;
	}
}
