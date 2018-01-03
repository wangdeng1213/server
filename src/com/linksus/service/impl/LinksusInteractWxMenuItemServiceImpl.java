package com.linksus.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.dao.LinksusInteractWxMenuItemMapper;
import com.linksus.entity.LinksusInteractWxMenuItem;
import com.linksus.service.LinksusInteractWxMenuItemService;

@Service("linksusInteractWxMenuItemService")
public class LinksusInteractWxMenuItemServiceImpl implements LinksusInteractWxMenuItemService{

	@Autowired
	private LinksusInteractWxMenuItemMapper linksusInteractWxMenuItemMapper;

	/** ��ѯ�б� */
	public List<LinksusInteractWxMenuItem> getLinksusInteractWxMenuItemList(){
		return linksusInteractWxMenuItemMapper.getLinksusInteractWxMenuItemList();
	}

	/** ������ѯ */
	public LinksusInteractWxMenuItem getLinksusInteractWxMenuItemById(Long pid){
		return linksusInteractWxMenuItemMapper.getLinksusInteractWxMenuItemById(pid);
	}

	/** ���� */
	public Integer insertLinksusInteractWxMenuItem(LinksusInteractWxMenuItem entity){
		return linksusInteractWxMenuItemMapper.insertLinksusInteractWxMenuItem(entity);
	}

	/** ���� */
	public Integer updateLinksusInteractWxMenuItem(LinksusInteractWxMenuItem entity){
		return linksusInteractWxMenuItemMapper.updateLinksusInteractWxMenuItem(entity);
	}

	/** ����ɾ�� */
	public Integer deleteLinksusInteractWxMenuItemById(Long pid){
		return linksusInteractWxMenuItemMapper.deleteLinksusInteractWxMenuItemById(pid);
	}

	/** ���ݲ˵�id��ȡһ���˵�����  */
	public List<LinksusInteractWxMenuItem> getLinksusInteractWxMenuItemByMenuId(Long menuId){
		return linksusInteractWxMenuItemMapper.getLinksusInteractWxMenuItemByMenuId(menuId);
	}

	/** ��ѯһ���˵��Ƿ��ж�������  */
	public List<LinksusInteractWxMenuItem> getLinksusInteractWxMenuItemByPrarentId(Long parentId){
		return linksusInteractWxMenuItemMapper.getLinksusInteractWxMenuItemByPrarentId(parentId);
	}
}