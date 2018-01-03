package com.linksus.entity;

import java.io.Serializable;

public class LinksusWx extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** 主键id */
	private Long id;
	/** 账户id */
	private Long institutionId;
	/** 帐号id */
	private Long accountId;
	/** 创建时间 */
	private Integer createdTime;
	/** 发送时间 */
	private Integer sendTime;
	/** 作者id 数字 */
	private Long authorId;
	/** 创建者姓名 */
	private String authorName;
	/** 当前所发布的微信的状态
	        0              草稿
	        1	预发布
	        2	发布失败
	        3	已发布
	        4	审批未通过
	        5	审批中
	        98	已删除
	        99	暂停中 */
	private Integer status;
	/** 审批状态 同微博 */
	private String applyStatus;
	/** 发布状态 同微博 */
	private Integer publishStatus;
	/** 归档状态 同微博 */
	private Integer toFile;
	/** 回写id */
	private Long mid;
	/** 微信类型0纯文本1单图文2多图文 */
	private Integer type;
	/** 微信失败原因 */
	private String errmsg;
	/** 微信账户token */
	private String token;
	/** 合同结束日期 */
	private String appEtime;
	/** 授权结束日期  */
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
