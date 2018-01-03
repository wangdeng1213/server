package com.linksus.entity;

import java.util.List;

public class InteractListResultObj{

	private static final long serialVersionUID = 1L;

	/** ����ID ���ŷָ� */
	private String interactIds;
	/** ��ԱID */
	private Long personId;
	/** ƽ̨�˺�ID */
	private Long accountId;
	/** �������ƽ̨����:1 ���� 2 ��Ѷ 3 ΢�� */
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
	/** �����������:1 ���� 2 ת�� 3 @ 4 ���۲�@ 5 ˽�� 6 ΢�� */
	private Integer interactType;
	/** ΢��ID:��������Ϊ1-4����ֵ */
	private Long weiboId;
	/** ΢��ID:��������Ϊ1-4����ֵ �����ַ�����ʽ*/
	private String weiboMid;
	/** ������ϢID:�û����»�����ϢID */
	private Long messageId;
	/** ����:�ı����� */
	private String content;
	/** �û�ͷ�� */
	private List headImages;
	/** ��Ա����*/
	private String personName;
	/** ��ά��ԱID*/
	private String instPersonIds;
	/** ��ά��Ա���� */
	private String instPersonNames;
	/** ƽ̨�˺����� */
	private String accountName;
	/** ���»�����Ա����  */
	private String interactUserName;
	/** ���»�����Աͷ��  */
	private String interactUserHeadImage;
	/** �¼�����  */
	private String eventType;
	/** ˽��ͼƬ·��  */
	private String messageImage;

	public List getHeadImages(){
		return headImages;
	}

	public String getContent(){
		return content;
	}

	public String getInstPersonIds(){
		return instPersonIds;
	}

	public void setInstPersonIds(String instPersonIds){
		this.instPersonIds = instPersonIds;
	}

	public String getInstPersonNames(){
		return instPersonNames;
	}

	public void setInstPersonNames(String instPersonNames){
		this.instPersonNames = instPersonNames;
	}

	public String getPersonName(){
		return personName;
	}

	public void setPersonName(String personName){
		this.personName = personName;
	}

	public void setContent(String content){
		this.content = content;
	}

	public void setHeadImages(List headImages){
		this.headImages = headImages;
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

	public String getInteractIds(){
		return interactIds;
	}

	public void setInteractIds(String interactIds){
		this.interactIds = interactIds;
	}

	public String getAccountName(){
		return accountName;
	}

	public void setAccountName(String accountName){
		this.accountName = accountName;
	}

	public String getInteractUserName(){
		return interactUserName;
	}

	public void setInteractUserName(String interactUserName){
		this.interactUserName = interactUserName;
	}

	public String getInteractUserHeadImage(){
		return interactUserHeadImage;
	}

	public void setInteractUserHeadImage(String interactUserHeadImage){
		this.interactUserHeadImage = interactUserHeadImage;
	}

	public String getEventType(){
		return eventType;
	}

	public void setEventType(String eventType){
		this.eventType = eventType;
	}

	public String getMessageImage(){
		return messageImage;
	}

	public void setMessageImage(String messageImage){
		this.messageImage = messageImage;
	}

	public String getWeiboMid(){
		return weiboMid;
	}

	public void setWeiboMid(String weiboMid){
		this.weiboMid = weiboMid;
	}

}
