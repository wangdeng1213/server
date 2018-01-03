package com.linksus.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.dao.LinksusInteractWxMenuMapper;
import com.linksus.entity.LinksusInteractWxMenu;
import com.linksus.service.LinksusInteractWxMenuService;

@Service("linksusInteractWxMenuService")
public class LinksusInteractWxMenuServiceImpl implements LinksusInteractWxMenuService{

	@Autowired
	private LinksusInteractWxMenuMapper linksusInteractWxMenuMapper;

	/** ��ѯ�б� */
	public List<LinksusInteractWxMenu> getLinksusInteractWxMenuList(){
		return linksusInteractWxMenuMapper.getLinksusInteractWxMenuList();
	}

	/** ������ѯ */
	public LinksusInteractWxMenu getLinksusInteractWxMenuById(Long pid){
		return linksusInteractWxMenuMapper.getLinksusInteractWxMenuById(pid);
	}

	/** ���� */
	public Integer insertLinksusInteractWxMenu(LinksusInteractWxMenu entity){
		return linksusInteractWxMenuMapper.insertLinksusInteractWxMenu(entity);
	}

	/** ���� */
	public Integer updateLinksusInteractWxMenu(LinksusInteractWxMenu entity){
		return linksusInteractWxMenuMapper.updateLinksusInteractWxMenu(entity);
	}

	/** ����ɾ�� */
	public Integer deleteLinksusInteractWxMenuById(Long pid){
		return linksusInteractWxMenuMapper.deleteLinksusInteractWxMenuById(pid);
	}
}