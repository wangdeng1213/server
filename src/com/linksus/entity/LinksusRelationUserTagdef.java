package com.linksus.entity;

import java.io.Serializable;

public class LinksusRelationUserTagdef extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** 主键 标签ID */
	private Long pid;
	/** 标签类型 1:新浪 2 腾讯 */
	private Integer tagType;
	/** 标签 */
	private String tag;
	/** 使用次数 */
	private Integer useCount;

	public Long getPid(){
		return pid;
	}

	public void setPid(Long pid){
		this.pid = pid;
	}

	public Integer getTagType(){
		return tagType;
	}

	public void setTagType(Integer tagType){
		this.tagType = tagType;
	}

	public String getTag(){
		return tag;
	}

	public void setTag(String tag){
		this.tag = tag;
	}

	public Integer getUseCount(){
		return useCount;
	}

	public void setUseCount(Integer useCount){
		this.useCount = useCount;
	}
}
