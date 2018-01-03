package com.linksus.entity;

import java.io.Serializable;

public class LinksusInteractWxMenuItem extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** 主键 回复ID */
	private Long pid;
	/** 菜单ID */
	private Long menuId;
	/** 上一级ID:第一级为0 */
	private Long prarentId;
	/** 类型:1 click类型 2 view类型 */
	private Integer itemType;
	/** 菜单项名称 */
	private String itemName;
	/** 链接地址[view类型] */
	private String redirectUrl;
	/** 回复类型(click类型):1 文本消息 2 单图文信息 3 多图文信息 */
	private Integer replyType;
	/** 文本信息(click类型) */
	private String content;
	/** 素材ID(图文信息)(click类型) */
	private Long materialId;

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

	public Long getPrarentId(){
		return prarentId;
	}

	public void setPrarentId(Long prarentId){
		this.prarentId = prarentId;
	}

	public Integer getItemType(){
		return itemType;
	}

	public void setItemType(Integer itemType){
		this.itemType = itemType;
	}

	public String getItemName(){
		return itemName;
	}

	public void setItemName(String itemName){
		this.itemName = itemName;
	}

	public String getRedirectUrl(){
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl){
		this.redirectUrl = redirectUrl;
	}

	public Integer getReplyType(){
		return replyType;
	}

	public void setReplyType(Integer replyType){
		this.replyType = replyType;
	}

	public String getContent(){
		return content;
	}

	public void setContent(String content){
		this.content = content;
	}

	public Long getMaterialId(){
		return materialId;
	}

	public void setMaterialId(Long materialId){
		this.materialId = materialId;
	}
}
