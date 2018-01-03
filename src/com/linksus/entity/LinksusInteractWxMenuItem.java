package com.linksus.entity;

import java.io.Serializable;

public class LinksusInteractWxMenuItem extends BaseEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/** ���� �ظ�ID */
	private Long pid;
	/** �˵�ID */
	private Long menuId;
	/** ��һ��ID:��һ��Ϊ0 */
	private Long prarentId;
	/** ����:1 click���� 2 view���� */
	private Integer itemType;
	/** �˵������� */
	private String itemName;
	/** ���ӵ�ַ[view����] */
	private String redirectUrl;
	/** �ظ�����(click����):1 �ı���Ϣ 2 ��ͼ����Ϣ 3 ��ͼ����Ϣ */
	private Integer replyType;
	/** �ı���Ϣ(click����) */
	private String content;
	/** �ز�ID(ͼ����Ϣ)(click����) */
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
