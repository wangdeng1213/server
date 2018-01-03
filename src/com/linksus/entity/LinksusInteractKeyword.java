package com.linksus.entity;

import java.io.Serializable;

public class LinksusInteractKeyword extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** ���� */
	private Long pid;
	/** ����ID:�������� */
	private Long institutionId;
	/** ��һ��ID:��һ��Ϊ0 */
	private Long prarentId;
	/** �ؼ���(��һ��Ϊ�ؼ�������) */
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
