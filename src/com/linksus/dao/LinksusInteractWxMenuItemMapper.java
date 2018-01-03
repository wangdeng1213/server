package com.linksus.dao;

import java.util.List;

import com.linksus.entity.LinksusInteractWxMenuItem;

public interface LinksusInteractWxMenuItemMapper{

	/** �б��ѯ */
	public List<LinksusInteractWxMenuItem> getLinksusInteractWxMenuItemList();

	/** ������ѯ */
	public LinksusInteractWxMenuItem getLinksusInteractWxMenuItemById(Long pid);

	/** ���� */
	public Integer insertLinksusInteractWxMenuItem(LinksusInteractWxMenuItem entity);

	/** ���� */
	public Integer updateLinksusInteractWxMenuItem(LinksusInteractWxMenuItem entity);

	/** ����ɾ�� */
	public Integer deleteLinksusInteractWxMenuItemById(Long pid);

	/**���ݲ˵�id��ȡ�˵����� */
	public List<LinksusInteractWxMenuItem> getLinksusInteractWxMenuItemByMenuId(Long menuId);

	/** ��ѯһ���˵��Ƿ��ж������� */
	public List<LinksusInteractWxMenuItem> getLinksusInteractWxMenuItemByPrarentId(Long parentId);
}
