package com.linksus.entity;

import java.io.Serializable;

public class LinksusInteractWxMenu extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** �˵�ID */
	private Long menuId;
	/** �˵��� */
	private String menuName;
	/** ����ID */
	private Long institutionId;

	public Long getMenuId(){
		return menuId;
	}

	public void setMenuId(Long menuId){
		this.menuId = menuId;
	}

	public String getMenuName(){
		return menuName;
	}

	public void setMenuName(String menuName){
		this.menuName = menuName;
	}

	public Long getInstitutionId(){
		return institutionId;
	}

	public void setInstitutionId(Long institutionId){
		this.institutionId = institutionId;
	}
}
