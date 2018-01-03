package com.linksus.entity;

import java.io.Serializable;

public class LinksusRelationOperateItem extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** 主键 */
	private Long pid;
	/** 关系操作ID */
	private Long operateId;
	/** 粉丝用户ID 关联微博表id主键 */
	private Long userId;
	/** 1 未执行 2 执行失败 3 执行成功 */
	private Long taskStatus;

	public Long getPid(){
		return pid;
	}

	public void setPid(Long pid){
		this.pid = pid;
	}

	public Long getOperateId(){
		return operateId;
	}

	public void setOperateId(Long operateId){
		this.operateId = operateId;
	}

	public Long getUserId(){
		return userId;
	}

	public void setUserId(Long userId){
		this.userId = userId;
	}

	public Long getTaskStatus(){
		return taskStatus;
	}

	public void setTaskStatus(Long taskStatus){
		this.taskStatus = taskStatus;
	}
}
