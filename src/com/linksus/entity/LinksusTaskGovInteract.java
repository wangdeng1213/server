package com.linksus.entity;

import java.io.Serializable;

public class LinksusTaskGovInteract extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** ƽ̨�˺�ID */
	private Long accountId;
	/** ��������:1 ���� 2 ת�� 3 @ 4 ���۲�@ 5 ��Ѷ���� 6 ��Ѷ˽�� */
	private Integer interactType;
	/** ���˵�since_id,��Ѷ��lastid */
	private Long maxId;
	/** ��Ѷ��ҳ��ʼʱ�� */
	private Long pagetime;

	public Long getAccountId(){
		return accountId;
	}

	public void setAccountId(Long accountId){
		this.accountId = accountId;
	}

	public Integer getInteractType(){
		return interactType;
	}

	public void setInteractType(Integer interactType){
		this.interactType = interactType;
	}

	public Long getMaxId(){
		return maxId;
	}

	public void setMaxId(Long maxId){
		this.maxId = maxId;
	}

	public Long getPagetime(){
		return pagetime;
	}

	public void setPagetime(Long pagetime){
		this.pagetime = pagetime;
	}
}
