package com.linksus.entity;

import java.io.Serializable;

public class LinksusGovMessage extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	private Long pid;
	/** ��ˮ�� */
	private Long runId = 0L;
	/** ƽ̨�˺�ID */
	private Long accountId = 0L;
	/** ��ǰ˽��id:����,��Ѷƽ̨���� */
	private Long msgId = 0L;
	/** ��������:1 �û����� 2 ƽ̨�˻��ظ� */
	private Integer interactType = 0;
	/** ����:�ı����� */
	private String content = "";
	/** ͼƬ����(json):�û�����/�˻��ظ���ͼƬurl */
	private String img = "";
	/** ͼƬԭʼ���� */
	private String imgName = "";
	/** ͼƬ��������(json) */
	private String imgThumb = "";
	/** ��������(json) */
	private String attatch = "";
	/** ����ԭʼ���� */
	private String attatchName = "";
	/** ��������(ƽ̨�˻��ظ�):0 ��ʱ���� 1Ϊ��ʱ���� */
	private Integer sendType = 0;
	/** �ظ�˽��id(ƽ̨�˻��ظ�):����˽�Żظ���Ҫ�Է����͵�˽��ID */
	private Long replyMsgId = 0L;
	/** ����״̬(ƽ̨�˻��ظ�):1:������ 2:�ѷ��� 3:����ʧ�� */
	private Integer status = 0;
	/** ����ʱ��(ƽ̨�˻��ظ�):��ʱ����,�����ɹ�����ʱ�� */
	private Integer sendTime = 0;
	/** ����ʱ�� */
	private Integer interactTime = 0;

	private Integer lastInteractTime;

	private Long replyId;
	/** �ظ����� */
	private Integer msgType;

	private Long userId;

	public Long getUserId(){
		return userId;
	}

	public void setUserId(Long userId){
		this.userId = userId;
	}

	public Integer getMsgType(){
		return msgType;
	}

	public void setMsgType(Integer msgType){
		this.msgType = msgType;
	}

	public Long getPid(){
		return pid;
	}

	public void setPid(Long pid){
		this.pid = pid;
	}

	public Long getRunId(){
		return runId;
	}

	public void setRunId(Long runId){
		this.runId = runId;
	}

	public Long getAccountId(){
		return accountId;
	}

	public void setAccountId(Long accountId){
		this.accountId = accountId;
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

}
