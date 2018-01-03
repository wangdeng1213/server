package com.linksus.entity;

import java.io.Serializable;

public class LinksusInteractWeixin extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** ���� ������ϢID */
	private Long pid;
	/** ������¼ID */
	private Long recordId;
	/** �û�ID:΢���û�ID */
	private Long userId;
	/** ƽ̨�˺�ID */
	private Long accountId;
	/** ΢��openid:�˺Żظ�����һ������,ʹ�øü�¼��openid�Ͷ�Ӧ�˺�token */
	private String openid;
	/** ����:΢���ı���Ϣ(����,��Ƶ������ʾurl) */
	private String content;
	/** ��������:1 �û����� 2 �˻��ظ� */
	private Integer interactType;
	/** ��Ϣ����:1 �ı���Ϣ 2 ͼƬ��Ϣ 3 ���� 4 ��Ƶ 5(�ظ�)��ͼ����Ϣ 6(�ظ�)��ͼ����Ϣ */
	private Integer msgType;
	/** �ز�ID(ͼ����Ϣ) */
	private Long materialId;
	/** ��������(ƽ̨�˻��ظ�):0 ��ʱ���� 1Ϊ��ʱ���� */
	private Integer sendType;
	/** ����״̬(ƽ̨�˻��ظ�):1:������ 2:�ѷ��� 3:����ʧ�� */
	private Integer status;
	/** ����ʱ��(ƽ̨�˻��ظ�):��ʱ���� */
	private Integer sendTime;
	/** ��ά��ԱID:(ƽ̨�˻��ظ�) */
	private Long instPersonId;
	/** ����ʱ�� */
	private Integer interactTime;

	public Long getPid(){
		return pid;
	}

	public void setPid(Long pid){
		this.pid = pid;
	}

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

	public String getOpenid(){
		return openid;
	}

	public void setOpenid(String openid){
		this.openid = openid;
	}

	public String getContent(){
		return content;
	}

	public void setContent(String content){
		this.content = content;
	}

	public Integer getInteractType(){
		return interactType;
	}

	public void setInteractType(Integer interactType){
		this.interactType = interactType;
	}

	public Integer getMsgType(){
		return msgType;
	}

	public void setMsgType(Integer msgType){
		this.msgType = msgType;
	}

	public Long getMaterialId(){
		return materialId;
	}

	public void setMaterialId(Long materialId){
		this.materialId = materialId;
	}

	public Integer getSendType(){
		return sendType;
	}

	public void setSendType(Integer sendType){
		this.sendType = sendType;
	}

	public Integer getStatus(){
		return status;
	}

	public void setStatus(Integer status){
		this.status = status;
	}

	public Integer getSendTime(){
		return sendTime;
	}

	public void setSendTime(Integer sendTime){
		this.sendTime = sendTime;
	}

	public Long getInstPersonId(){
		return instPersonId;
	}

	public void setInstPersonId(Long instPersonId){
		this.instPersonId = instPersonId;
	}

	public Integer getInteractTime(){
		return interactTime;
	}

	public void setInteractTime(Integer interactTime){
		this.interactTime = interactTime;
	}

}