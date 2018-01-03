package com.linksus.entity;

import java.io.Serializable;

public class LinksusTaskInvalidRecord extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** ���� */
	private Long pid;
	/** ������� */
	private String errorCode;
	/** ������Ϣ */
	private String errorMsg;
	/** ��������:1 ΢������ 2 Ӫ��@ 3 Ӫ������ 4 �������� 5 ����˽�� 6 ����΢�� */
	private Integer operType;
	/** ����ID */
	private Long institutionId;
	/** ������ά��ԱID,������ŷָ� */
	private String smsInstPersons;
	/** �ʼ���ά��ԱID,������ŷָ� */
	private String emailInstPersons;
	/** ��¼���� */
	private Long recordId;
	/** ��ԴIP */
	private String sourceIp;
	/** ����ʱ�� */
	private Integer addTime;

	/**���ŷ��ͱ�־*/
	private boolean smsFlag = false;
	/**�ʼ����ͱ�־*/
	private boolean emailFlag = false;

	public Long getPid(){
		return pid;
	}

	public void setPid(Long pid){
		this.pid = pid;
	}

	public String getErrorCode(){
		return errorCode;
	}

	public void setErrorCode(String errorCode){
		this.errorCode = errorCode;
	}

	public String getErrorMsg(){
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg){
		this.errorMsg = errorMsg;
	}

	public Integer getOperType(){
		return operType;
	}

	public void setOperType(Integer operType){
		this.operType = operType;
	}

	public Long getInstitutionId(){
		return institutionId;
	}

	public void setInstitutionId(Long institutionId){
		this.institutionId = institutionId;
	}

	public String getSmsInstPersons(){
		return smsInstPersons;
	}

	public void setSmsInstPersons(String smsInstPersons){
		this.smsInstPersons = smsInstPersons;
	}

	public String getEmailInstPersons(){
		return emailInstPersons;
	}

	public void setEmailInstPersons(String emailInstPersons){
		this.emailInstPersons = emailInstPersons;
	}

	public Long getRecordId(){
		return recordId;
	}

	public void setRecordId(Long recordId){
		this.recordId = recordId;
	}

	public String getSourceIp(){
		return sourceIp;
	}

	public void setSourceIp(String sourceIp){
		this.sourceIp = sourceIp;
	}

	public Integer getAddTime(){
		return addTime;
	}

	public void setAddTime(Integer addTime){
		this.addTime = addTime;
	}

	public boolean isSmsFlag(){
		return smsFlag;
	}

	public void setSmsFlag(boolean smsFlag){
		this.smsFlag = smsFlag;
	}

	public boolean isEmailFlag(){
		return emailFlag;
	}

	public void setEmailFlag(boolean emailFlag){
		this.emailFlag = emailFlag;
	}
}
