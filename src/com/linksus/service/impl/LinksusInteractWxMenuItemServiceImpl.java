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

	/** 查询列表 */
	public List<LinksusInteractWxMenuItem> getLinksusInteractWxMenuItemList(){
		return linksusInteractWxMenuItemMapper.getLinksusInteractWxMenuItemList();
	}

	/** 主键查询 */
	public LinksusInteractWxMenuItem getLinksusInteractWxMenuItemById(Long pid){
		return linksusInteractWxMenuItemMapper.getLinksusInteractWxMenuItemById(pid);
	}

	/** 新增 */
	public Integer insertLinksusInteractWxMenuItem(LinksusInteractWxMenuItem entity){
		return linksusInteractWxMenuItemMapper.insertLinksusInteractWxMenuItem(entity);
	}

	/** 更新 */
	public Integer updateLinksusInteractWxMenuItem(LinksusInteractWxMenuItem entity){
		return linksusInteractWxMenuItemMapper.updateLinksusInteractWxMenuItem(entity);
	}

	/** 主键删除 */
	public Integer deleteLinksusInteractWxMenuItemById(Long pid){
		return linksusInteractWxMenuItemMapper.deleteLinksusInteractWxMenuItemById(pid);
	}

	/** 根据菜单id读取一级菜单内容  */
	public List<LinksusInteractWxMenuItem> getLinksusInteractWxMenuItemByMenuId(Long menuId){
		return linksusInteractWxMenuItemMapper.getLinksusInteractWxMenuItemByMenuId(menuId);
	}

	/** 查询一级菜单是否有二级内容  */
	public List<LinksusInteractWxMenuItem> getLinksusInteractWxMenuItemByPrarentId(Long parentId){
		return linksusInteractWxMenuItemMapper.getLinksusInteractWxMenuItemByPrarentId(parentId);
	}
}