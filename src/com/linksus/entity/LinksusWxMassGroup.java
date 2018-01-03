package com.linksus.entity;

import java.io.Serializable;

public class LinksusWxMassGroup extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** 主键id */
	private Long pid;
	/** 消息id */
	private Long msgId;
	/** 微信主id */
	private Long wxId;
	/** 帐号id */
	private Long accountId;
	/** 群发到的分组的group_id */
	private Long groupId;
	/** 分组名字 */
	private String groupName;
	/** 分组内用户数量 */
	private Integer groupCount;
	/** 过滤（过滤是指特定地区、性别的过滤、用户设置拒收的过滤，用户接收已超4条的过滤）后，准备发送的粉丝数，原则上，FilterCount = SentCount + ErrorCount */
	private Integer filterCount;
	/** 发送成功的粉丝数 */
	private Integer sentCount;
	/** 发送失败的粉丝数 */
	private Integer errorCount;
	/** 群发时间 */
	private Integer createTime;
	/** 结束时间 */
	private Integer lastTime;
	/** 微信返回信息 */
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
