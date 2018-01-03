package com.linksus.entity;

import java.io.Serializable;
import java.util.List;

public class LinksusInteractPerson extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** ����ID ���� */
	private Long interactId;
	/** ��ԱID */
	private Long personId;
	/** ƽ̨�˺�ID */
	private Long accountId;
	/** �ʺ�����:1 ���� 2 ��Ѷ 3 ΢�� */
	private Integer accountType;
	/** ����״̬:0 δ���� 1:�ѷ��� 2:���� */
	private Integer status;
	/** δ�������� */
	private Integer count;
	/** ��ά��ԱID:�ѷ���ʱ��ֵ */
	private Long instPersonId;
	/** �ö�ʱ��:�ö�ʱ��ֵ */
	private Integer topTime;
	/** �������ʱ�� */
	private Integer lastInteractTime;
	/** �������ID */
	private Long lastRecordId;
	/** ����ʱ�� */
	private Integer updateTime;

	/** ��������:1 ���� 2 ת�� 3 @ 4 ���۲�@ 5 ˽�� 6 ΢�� */
	private Integer interactType;
	/** ΢��ID:��������Ϊ1-4����ֵ */
	private Long weiboId;
	/** ������ϢID:�û����»�����ϢID */
	private Long messageId;
	/** ����:�ı����� */
	private String content;

	//�����б�
	/** �û�ͷ�� */
	private List headImages;

	public List getHeadImages(){
		return headImages;
	}

	public String getContent(){
		return content;
	}

	public void setContent(String content){
		this.content = content;
	}

	public void setHeadImages(List headImages){
		this.headImages = headImages;
	}

	public Long getInteractId(){
		return interactId;
	}

	public void setInteractId(Long interactId){
		this.interactId = interactId;
	}

	public Long getPersonId(){
		return personId;
	}

	public void setPersonId(Long personId){
		this.personId = personId;
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

	public Integer getStatus(){
		return status;
	}

	public void setStatus(Integer status){
		this.status = status;
	}

	public Integer getCount(){
		return count;
	}

	public void setCount(Integer count){
		this.count = count;
	}

	public Long getInstPersonId(){
		return instPersonId;
	}

	public void setInstPersonId(Long instPersonId){
		this.instPersonId = instPersonId;
	}

	public Integer getTopTime(){
		return topTime;
	}

	public void setTopTime(Integer topTime){
		this.topTime = topTime;
	}

	public Integer getLastInteractTime(){
		return lastInteractTime;
	}

	public void setLastInteractTime(Integer lastInteractTime){
		this.lastInteractTime = lastInteractTime;
	}

	public Long getLastRecordId(){
		return lastRecordId;
	}

	public void setLastRecordId(Long lastRecordId){
		this.lastRecordId = lastRecordId;
	}

	public Integer getUpdateTime(){
		return updateTime;
	}

	public void setUpdateTime(Integer updateTime){
		this.updateTime = updateTime;
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

	public Long getMessageId(){
		return messageId;
	}

	public void setMessageId(Long messageId){
		this.messageId = messageId;
	}
}
