package com.linksus.entity;

import java.io.Serializable;

public class LinksusWeChatGroupInfo extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** ����id */
	private Long id;
	/** ����id */
	private String name;
	/** ����id */
	private Integer count;

	public Long getId(){
		return id;
	}

	public void setId(Long id){
		this.id = id;
	}

	public String getName(){
		return name;
	}

	public void setName(String name){
		this.name = name;
	}

	public Integer getCount(){
		return count;
	}

	public void setCount(Integer count){
		this.count = count;
	}

}
