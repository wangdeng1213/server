package com.linksus.entity;

import java.io.Serializable;

public class LinksusTaskTotalUser extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** 账户ID */
	private Long accountId;
	/** 账号类型: 1 新浪 */
	private Integer accountType;
	/** 任务类型: 1 全量粉丝 2 全量关注 */
	private Integer taskType;
	/** 机构ID */
	private Long institutionId;
	/** 任务状态 0：未执行 1：成功 2：失败 */
	private Integer status;
	/** 下次max_time值:防止意外中断,支持恢复 */
	private Long nextCursor;
	/** 上次更新时间 */
	private Integer lastUpdtTime;

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

	public Integer getTaskType(){
		return taskType;
	}

	public void setTaskType(Integer taskType){
		this.taskType = taskType;
	}

	public Long getInstitutionId(){
		return institutionId;
	}

	public void setInstitutionId(Long institutionId){
		this.institutionId = institutionId;
	}

	public Integer getStatus(){
		return status;
	}

	public void setStatus(Integer status){
		this.status = status;
	}

	public Long getNextCursor(){
		return nextCursor;
	}

	public void setNextCursor(Long nextCursor){
		this.nextCursor = nextCursor;
	}

	public Integer getLastUpdtTime(){
		return lastUpdtTime;
	}

	public void setLastUpdtTime(Integer lastUpdtTime){
		this.lastUpdtTime = lastUpdtTime;
	}
}
