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

	/** 查询列表 */
	public List<LinksusInteractWxMenu> getLinksusInteractWxMenuList(){
		return linksusInteractWxMenuMapper.getLinksusInteractWxMenuList();
	}

	/** 主键查询 */
	public LinksusInteractWxMenu getLinksusInteractWxMenuById(Long pid){
		return linksusInteractWxMenuMapper.getLinksusInteractWxMenuById(pid);
	}

	/** 新增 */
	public Integer insertLinksusInteractWxMenu(LinksusInteractWxMenu entity){
		return linksusInteractWxMenuMapper.insertLinksusInteractWxMenu(entity);
	}

	/** 更新 */
	public Integer updateLinksusInteractWxMenu(LinksusInteractWxMenu entity){
		return linksusInteractWxMenuMapper.updateLinksusInteractWxMenu(entity);
	}

	/** 主键删除 */
	public Integer deleteLinksusInteractWxMenuById(Long pid){
		return linksusInteractWxMenuMapper.deleteLinksusInteractWxMenuById(pid);
	}
}