package com.linksus.entity;

import java.io.Serializable;

public class LinksusWxMassGroup extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** ����id */
	private Long pid;
	/** ��Ϣid */
	private Long msgId;
	/** ΢����id */
	private Long wxId;
	/** �ʺ�id */
	private Long accountId;
	/** Ⱥ�����ķ����group_id */
	private Long groupId;
	/** �������� */
	private String groupName;
	/** �������û����� */
	private Integer groupCount;
	/** ���ˣ�������ָ�ض��������Ա�Ĺ��ˡ��û����þ��յĹ��ˣ��û������ѳ�4���Ĺ��ˣ���׼�����͵ķ�˿����ԭ���ϣ�FilterCount = SentCount + ErrorCount */
	private Integer filterCount;
	/** ���ͳɹ��ķ�˿�� */
	private Integer sentCount;
	/** ����ʧ�ܵķ�˿�� */
	private Integer errorCount;
	/** Ⱥ��ʱ�� */
	private Integer createTime;
	/** ����ʱ�� */
	private Integer lastTime;
	/** ΢�ŷ�����Ϣ */
	private String statusMsg;

	public Long getPid(){
		return pid;
	}

	public void setPid(Long pid){
		this.pid = pid;
	}

	public Long getMsgId(){
		return msgId;
	}

	public void setMsgId(Long msgId){
		this.msgId = msgId;
	}

	public Long getWxId(){
		return wxId;
	}

	public void setWxId(Long wxId){
		this.wxId = wxId;
	}

	public Long getAccountId(){
		return accountId;
	}

	public void setAccountId(Long accountId){
		this.accountId = accountId;
	}

	public Long getGroupId(){
		return groupId;
	}

	public void setGroupId(Long groupId){
		this.groupId = groupId;
	}

	public String getGroupName(){
		return groupName;
	}

	public void setGroupName(String groupName){
		this.groupName = groupName;
	}

	public Integer getGroupCount(){
		return groupCount;
	}

	public void setGroupCount(Integer groupCount){
		this.groupCount = groupCount;
	}

	public Integer getFilterCount(){
		return filterCount;
	}

	public void setFilterCount(Integer filterCount){
		this.filterCount = filterCount;
	}

	public Integer getSentCount(){
		return sentCount;
	}

	public void setSentCount(Integer sentCount){
		this.sentCount = sentCount;
	}

	public Integer getErrorCount(){
		return errorCount;
	}

	public void setErrorCount(Integer errorCount){
		this.errorCount = errorCount;
	}

	public Integer getCreateTime(){
		return createTime;
	}

	public void setCreateTime(Integer createTime){
		this.createTime = createTime;
	}

	public Integer getLastTime(){
		return lastTime;
	}

	public void setLastTime(Integer lastTime){
		this.lastTime = lastTime;
	}

	public String getStatusMsg(){
		return statusMsg;
	}

	public void setStatusMsg(String statusMsg){
		this.statusMsg = statusMsg;
	}
}
