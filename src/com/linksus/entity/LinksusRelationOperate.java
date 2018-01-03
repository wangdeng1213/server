package com.linksus.entity;

import java.io.Serializable;

public class LinksusRelationOperate extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** 主键 */
	private Long pid;
	/** 账号ID */
	private Long accountId;
	/** 账号类型:1 新浪 2 腾讯 3微信 */
	private Integer accountType;
	/** 任务类型 1 添加关注 2 取消关注 3 移除粉丝 */
	private Long taskType;
	/** 操作成功人数 */
	private Integer operSuccessNum;
	/** 操作失败人数 */
	private Integer operFailNum;
	/** 1 未审批 2 审批不通过 3审批通过 */
	private Integer status;
	/** 当前审核的状态 00 未审批 10 一级审批完成 11 一级 二级审批均完成 */
	private Integer applyStatus;

	public Long getPid(){
		return pid;
	}

	public void setPid(Long pid){
		this.pid = pid;
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

	public Long getTaskType(){
		return taskType;
	}

	public void setTaskType(Long taskType){
		this.taskType = taskType;
	}

	public Integer getOperSuccessNum(){
		return operSuccessNum;
	}

	public void setOperSuccessNum(Integer operSuccessNum){
		this.operSuccessNum = operSuccessNum;
	}

	public Integer getOperFailNum(){
		return operFailNum;
	}

	public void setOperFailNum(Integer operFailNum){
		this.operFailNum = operFailNum;
	}

	public Integer getStatus(){
		return status;
	}

	public void setStatus(Integer status){
		this.status = status;
	}

	public Integer getApplyStatus(){
		return applyStatus;
	}

	public void setApplyStatus(Integer applyStatus){
		this.applyStatus = applyStatus;
	}
}
