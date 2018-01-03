package com.linksus.service;

import java.util.List;

import com.linksus.entity.LinksusInteractWxMenu;

public interface LinksusInteractWxMenuService{

	/** ��ѯ�б� */
	public List<LinksusInteractWxMenu> getLinksusInteractWxMenuList();

	/** ������ѯ */
	public LinksusInteractWxMenu getLinksusInteractWxMenuById(Long pid);

	/** ���� */
	public Integer insertLinksusInteractWxMenu(LinksusInteractWxMenu entity);

	/** ���� */
	public Integer updateLinksusInteractWxMenu(LinksusInteractWxMenu entity);

	/** ����ɾ�� */
	public Integer deleteLinksusInteractWxMenuById(Long pid);

}
