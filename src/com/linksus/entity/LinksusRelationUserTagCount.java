package com.linksus.entity;

import java.io.Serializable;

public class LinksusRelationUserTagCount extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** 标签 */
	private String tag;
	/** 标签类型:1 微博用户 2 新浪 3 腾讯 4 人员 (暂时只有1) */
	private Integer tagType;
	/** 统计数值 */
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
