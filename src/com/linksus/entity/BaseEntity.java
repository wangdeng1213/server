package com.linksus.entity;

import java.io.Serializable;

public class BaseEntity implements Serializable{

	//��ҳ��Ϣ 
	private Integer startCount = 0;
	private Integer pageSize = 0;
	//��ѯ��¼��
	private Integer totalCount = -1;
	//�����ֶ� ����
	private String sort = "";
	private String sortType = "";

	public Integer getStartCount(){
		return startCount;
	}

	public void setStartCount(Integer startCount){
		this.startCount = startCount;
	}

	public Integer getPageSize(){
		return pageSize;
	}

	public void setPageSize(Integer pageSize){
		this.pageSize = pageSize;
	}

	public Integer getTotalCount(){
		return totalCount;
	}

	public void setTotalCount(Integer totalCount){
		this.totalCount = totalCount;
	}

	public String getSort(){
		return sort;
	}

	public void setSort(String sort){
		this.sort = sort;
	}

	public String getSortType(){
		return sortType;
	}

	public void setSortType(String sortType){
		this.sortType = sortType;
	}
}
