package com.linksus.entity;

import java.io.Serializable;

public class LinksusTaskAttentionUser extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	/**����id*/
	private Long pid;
	/** �˺�ID */
	private Long accountId;
	/** �û�ID */
	private Long userId;
	/** �˺�����:1 ���� 2 ��Ѷ */
	private Integer accountType;
	/** ����״̬:1 δ���� 2 �Ѵ��� */
	private Integer status;
	/** ����ʱ�� */
	private Integer createTime;
	/** ����ʱ�� */
	private Integer interactTime;

	public Long getPid(){
		return pid;
	}

	public void setPid(Long pid){
		this.pid = pid;
	}

	public Long getAccountId(){
		return accountId;
	}

	public void setAccountId(Long accountId){
		this.accountId = accountId;
	}

	public Long getUserId(){
		return userId;
	}

	public void setUserId(Long userId){
		this.userId = userId;
	}

	public Integer getAccountType(){
		return accountType;
	}

	public void setAccountType(Integer accountType){
		this.accountType = accountType;
	}

	public Integer getStatus(){
		return status;
	}

	public void setStatus(Integer status){
		this.status = status;
	}

	public Integer getCreateTime(){
		return createTime;
	}

	public void setCreateTime(Integer createTime){
		this.createTime = createTime;
	}

	public Integer getInteractTime(){
		return interactTime;
	}

	public void setInteractTime(Integer interactTime){
		this.interactTime = interactTime;
	}
}
