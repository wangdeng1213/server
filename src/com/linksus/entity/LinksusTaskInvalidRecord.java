package com.linksus.entity;

import java.io.Serializable;

public class LinksusTaskInvalidRecord extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** 主键 */
	private Long pid;
	/** 错误代码 */
	private String errorCode;
	/** 错误信息 */
	private String errorMsg;
	/** 操作类型:1 微博发布 2 营销@ 3 营销评论 4 互动评论 5 互动私信 6 互动微信 */
	private Integer operType;
	/** 机构ID */
	private Long institutionId;
	/** 短信运维人员ID,多个逗号分隔 */
	private String smsInstPersons;
	/** 邮件运维人员ID,多个逗号分隔 */
	private String emailInstPersons;
	/** 记录主键 */
	private Long recordId;
	/** 来源IP */
	private String sourceIp;
	/** 发生时间 */
	private Integer addTime;

	/**短信发送标志*/
	private boolean smsFlag = false;
	/**邮件发送标志*/
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
