package com.linksus.entity;

import java.io.Serializable;

public class LinksusWx extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** ����id */
	private Long id;
	/** �˻�id */
	private Long institutionId;
	/** �ʺ�id */
	private Long accountId;
	/** ����ʱ�� */
	private Integer createdTime;
	/** ����ʱ�� */
	private Integer sendTime;
	/** ����id ���� */
	private Long authorId;
	/** ���������� */
	private String authorName;
	/** ��ǰ��������΢�ŵ�״̬
	        0              �ݸ�
	        1	Ԥ����
	        2	����ʧ��
	        3	�ѷ���
	        4	����δͨ��
	        5	������
	        98	��ɾ��
	        99	��ͣ�� */
	private Integer status;
	/** ����״̬ ͬ΢�� */
	private String applyStatus;
	/** ����״̬ ͬ΢�� */
	private Integer publishStatus;
	/** �鵵״̬ ͬ΢�� */
	private Integer toFile;
	/** ��дid */
	private Long mid;
	/** ΢������0���ı�1��ͼ��2��ͼ�� */
	private Integer type;
	/** ΢��ʧ��ԭ�� */
	private String errmsg;
	/** ΢���˻�token */
	private String token;
	/** ��ͬ�������� */
	private String appEtime;
	/** ��Ȩ��������  */
	private String tokenEtime;

	public Long getId(){
		return id;
	}

	public void setId(Long id){
		this.id = id;
	}

	public Long getInstitutionId(){
		return institutionId;
	}

	public void setInstitutionId(Long institutionId){
		this.institutionId = institutionId;
	}

	public Long getAccountId(){
		return accountId;
	}

	public void setAccountId(Long accountId){
		this.accountId = accountId;
	}

	public Integer getCreatedTime(){
		return createdTime;
	}

	public void setCreatedTime(Integer createdTime){
		this.createdTime = createdTime;
	}

	public Integer getSendTime(){
		return sendTime;
	}

	public void setSendTime(Integer sendTime){
		this.sendTime = sendTime;
	}

	public Long getAuthorId(){
		return authorId;
	}

	public void setAuthorId(Long authorId){
		this.authorId = authorId;
	}

	public String getAuthorName(){
		return authorName;
	}

	public void setAuthorName(String authorName){
		this.authorName = authorName;
	}

	public Integer getStatus(){
		return status;
	}

	public void setStatus(Integer status){
		this.status = status;
	}

	public String getApplyStatus(){
		return applyStatus;
	}

	public void setApplyStatus(String applyStatus){
		this.applyStatus = applyStatus;
	}

	public Integer getPublishStatus(){
		return publishStatus;
	}

	public void setPublishStatus(Integer publishStatus){
		this.publishStatus = publishStatus;
	}

	public Integer getToFile(){
		return toFile;
	}

	public void setToFile(Integer toFile){
		this.toFile = toFile;
	}

	public Long getMid(){
		return mid;
	}

	public void setMid(Long mid){
		this.mid = mid;
	}

	public Integer getType(){
		return type;
	}

	public void setType(Integer type){
		this.type = type;
	}

	public String getErrmsg(){
		return errmsg;
	}

	public void setErrmsg(String errmsg){
		this.errmsg = errmsg;
	}

	public String getToken(){
		return token;
	}

	public void setToken(String token){
		this.token = token;
	}

	public String getAppEtime(){
		return appEtime;
	}

	public void setAppEtime(String appEtime){
		this.appEtime = appEtime;
	}

	public String getTokenEtime(){
		return tokenEtime;
	}

	public void setTokenEtime(String tokenEtime){
		this.tokenEtime = tokenEtime;
	}

}
