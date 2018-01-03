package com.linksus.entity;

import java.io.Serializable;

public class LinksusGovDocument extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** �İ�����id */
	private Integer documentId;
	/** �İ����� */
	private String documentContent;
	/** �İ����������ʺŹ��� */
	private Integer accountId;

	public Integer getDocumentId(){
		return documentId;
	}

	public void setDocumentId(Integer documentId){
		this.documentId = documentId;
	}

	public String getDocumentContent(){
		return documentContent;
	}

	public void setDocumentContent(String documentContent){
		this.documentContent = documentContent;
	}

	public Integer getAccountId(){
		return accountId;
	}

	public void setAccountId(Integer accountId){
		this.accountId = accountId;
	}
}
