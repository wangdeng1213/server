package com.linksus.entity;

import java.io.Serializable;

public class LinksusInteractRecord extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** ������¼ID:���� */
	private Long recordId;
	/** �û�ID:΢��/΢���û�ID */
	private Long userId;
	/** ƽ̨�˺�ID */
	private Long accountId;
	/** �ʺ�����:1 ���� 2 ��Ѷ 3 ΢�� */
	private Integer accountType;
	/** ��������:1 ���� 2 ת�� 3 @ 4 ���۲�@ 5 ˽�� 6 ΢�� */
	private Integer interactType;
	/** ΢��ID:��������Ϊ1-4����ֵ */
	private Long weiboId;
	/** α������־:1 �� 0 �� */
	private Integer fakeFlag;
	/** ����Ϣ��־:1 ��������Ϣ 0 ������Ϣ */
	private Integer newMsgFlag;
	/** ������ϢID:�û����»�����ϢID */
	private Long messageId;
	/** ����ʱ�� */
	private Integer updateTime;

	public Long getRecordId(){
		return recordId;
	}

	public void setRecordId(Long recordId){
		this.recordId = recordId;
	}

	public Long getUserId(){
		return userId;
	}

	public void setUserId(Long userId){
		this.userId = userId;
	}

	public Long getAccountId(){
		return accountId;
	}

	public void setAccountId(Long accountId){
		this.accountId = accountId;
	}

	public Integer getAccountType(){
		return accountType;
	}

	public void setAccountType(Integer accountType){
		this.accountType = accountType;
	}

	public Integer getInteractType(){
		return interactType;
	}

	public void setInteractType(Integer interactType){
		this.interactType = interactType;
	}

	public Long getWeiboId(){
		return weiboId;
	}

	public void setWeiboId(Long weiboId){
		this.weiboId = weiboId;
	}

	public Integer getFakeFlag(){
		return fakeFlag;
	}

	public void setFakeFlag(Integer fakeFlag){
		this.fakeFlag = fakeFlag;
	}

	public Long getMessageId(){
		return messageId;
	}

	public void setMessageId(Long messageId){
		this.messageId = messageId;
	}

	public Integer getUpdateTime(){
		return updateTime;
	}

	public void setUpdateTime(Integer updateTime){
		this.updateTime = updateTime;
	}

	public Integer getNewMsgFlag(){
		return newMsgFlag;
	}

	public void setNewMsgFlag(Integer newMsgFlag){
		this.newMsgFlag = newMsgFlag;
	}
}
