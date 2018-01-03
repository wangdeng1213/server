package com.linksus.entity;

import java.io.Serializable;

public class LinksusInteractWxMenuAcct extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** 主键 */
	private Long pid;
	/** 菜单ID */
	private Long menuId;
	/** 平台账号ID */
	private Long accountId;

	public Long getPid(){
		return pid;
	}

	public void setPid(Long pid){
		this.pid = pid;
	}

	public Long getMenuId(){
		return menuId;
	}

	public void setMenuId(Long menuId){
		this.menuId = menuId;
	}

	public Long getAccountId(){
		return accountId;
	}

	public void setAccountId(Long accountId){
		this.accountId = accountId;
	}
}
