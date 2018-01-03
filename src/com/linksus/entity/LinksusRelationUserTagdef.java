package com.linksus.entity;

import java.io.Serializable;

public class LinksusRelationUserTagdef extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** ���� ��ǩID */
	private Long pid;
	/** ��ǩ���� 1:���� 2 ��Ѷ */
	private Integer tagType;
	/** ��ǩ */
	private String tag;
	/** ʹ�ô��� */
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
