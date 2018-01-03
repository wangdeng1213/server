package com.linksus.entity;

import java.io.Serializable;

public class LinksusInteractKeyword extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** 主键 */
	private Long pid;
	/** 机构ID:创建机构 */
	private Long institutionId;
	/** 上一级ID:第一级为0 */
	private Long prarentId;
	/** 关键字(第一级为关键字类型) */
	private String keywordName;

	public Long getPid(){
		return pid;
	}

	public void setPid(Long pid){
		this.pid = pid;
	}

	public Long getInstitutionId(){
		return institutionId;
	}

	public void setInstitutionId(Long institutionId){
		this.institutionId = institutionId;
	}

	public Long getPrarentId(){
		return prarentId;
	}

	public void setPrarentId(Long prarentId){
		this.prarentId = prarentId;
	}

	public String getKeywordName(){
		return keywordName;
	}

	public void setKeywordName(String keywordName){
		this.keywordName = keywordName;
	}
}
