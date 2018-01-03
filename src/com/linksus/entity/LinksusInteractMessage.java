package com.linksus.entity;

import java.io.Serializable;

public class LinksusInteractMessage extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** ���� ������ϢID */
	private Long pid;
	/** ������¼ID */
	private Long recordId;
	/** �û�ID:΢���û�ID */
	private Long userId;
	/** ƽ̨�˺�ID */
	private Long accountId;
	/** ΢������:1 ���� 2 ��Ѷ */
	private Integer accountType;
	/** ��ǰ˽��id:����,��Ѷƽ̨���� */
	private Long msgId;
	/** ��������:1 �û����� 2 ƽ̨�˻��ظ� */
	private Integer interactType;
	/** ����:�ı����� */
	private String content;
	/** ͼƬ����(json):�û�����/�˻��ظ���ͼƬurl */
	private String img;
	/** ͼƬԭʼ���� */
	private String imgName;
	/** ͼƬ��������(json) */
	private String imgThumb;
	/** ��������(json) */
	private String attatch;
	/** ����ԭʼ���� */
	private String attatchName;
	/** ��������(ƽ̨�˻��ظ�):0 ��ʱ���� 1Ϊ��ʱ���� */
	private Integer sendType;
	/** �ظ�˽��id(ƽ̨�˻��ظ�):����˽�Żظ���Ҫ�Է����͵�˽��ID */
	private Long replyMsgId;
	/** ����״̬(ƽ̨�˻��ظ�):1:������ 2:�ѷ��� 3:����ʧ�� */
	private Integer status;
	/** ����ʱ��(ƽ̨�˻��ظ�):��ʱ����,�����ɹ�����ʱ�� */
	private Integer sendTime;
	/** ��ά��ԱID:(ƽ̨�˻��ظ�) */
	private Long instPersonId;
	/** ����ʱ�� */
	private Integer interactTime;

	/** �ظ�˽�Ż���ʱ�� */
	private Integer lastInteractTime;
	/** ��Ϣ���� */
	private Integer msgType;
	/** �ظ����� */
	private Integer replyCount;

	/** �ظ�˽��ID */
	private Long replyId;
	private String userName;

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

	public Integer getAccountType(){
		return accountType;
	}

	public void setAccountType(Integer accountType){
		this.accountType = accountType;
	}

	public Long getMsgId(){
		return msgId;
	}

	public void setMsgId(Long msgId){
		this.msgId = msgId;
	}

	public Integer getInteractType(){
		return interactType;
	}

	public void setInteractType(Integer interactType){
		this.interactType = interactType;
	}

	public String getContent(){
		return content;
	}

	public void setContent(String content){
		this.content = content;
	}

	public String getImg(){
		return img;
	}

	public void setImg(String img){
		this.img = img;
	}

	public String getImgName(){
		return imgName;
	}

	public void setImgName(String imgName){
		this.imgName = imgName;
	}

	public String getImgThumb(){
		return imgThumb;
	}

	public void setImgThumb(String imgThumb){
		this.imgThumb = imgThumb;
	}

	public String getAttatch(){
		return attatch;
	}

	public void setAttatch(String attatch){
		this.attatch = attatch;
	}

	public String getAttatchName(){
		return attatchName;
	}

	public void setAttatchName(String attatchName){
		this.attatchName = attatchName;
	}

	public Integer getSendType(){
		return sendType;
	}

	public void setSendType(Integer sendType){
		this.sendType = sendType;
	}

	public Long getReplyMsgId(){
		return replyMsgId;
	}

	public void setReplyMsgId(Long replyMsgId){
		this.replyMsgId = replyMsgId;
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

	public Integer getLastInteractTime(){
		return lastInteractTime;
	}

	public void setLastInteractTime(Integer lastInteractTime){
		this.lastInteractTime = lastInteractTime;
	}

	public Long getReplyId(){
		return replyId;
	}

	public void setReplyId(Long replyId){
		this.replyId = replyId;
	}

	public String getUserName(){
		return userName;
	}

	public void setUserName(String userName){
		this.userName = userName;
	}

	public Integer getMsgType(){
		return msgType;
	}

	public void setMsgType(Integer msgType){
		this.msgType = msgType;
	}

	public Integer getReplyCount(){
		return replyCount;
	}

	public void setReplyCount(Integer replyCount){
		this.replyCount = replyCount;
	}
}
