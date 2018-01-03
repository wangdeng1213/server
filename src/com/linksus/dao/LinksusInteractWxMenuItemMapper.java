package com.linksus.dao;

import java.util.List;

import com.linksus.entity.LinksusInteractWxMenuItem;

public interface LinksusInteractWxMenuItemMapper{

	/** 列表查询 */
	public List<LinksusInteractWxMenuItem> getLinksusInteractWxMenuItemList();

	/** 主键查询 */
	public LinksusInteractWxMenuItem getLinksusInteractWxMenuItemById(Long pid);

	/** 新增 */
	public Integer insertLinksusInteractWxMenuItem(LinksusInteractWxMenuItem entity);

	/** 更新 */
	public Integer updateLinksusInteractWxMenuItem(LinksusInteractWxMenuItem entity);

	/** 主键删除 */
	public Integer deleteLinksusInteractWxMenuItemById(Long pid);

	/**根据菜单id读取菜单内容 */
	public List<LinksusInteractWxMenuItem> getLinksusInteractWxMenuItemByMenuId(Long menuId);

	/** 查询一级菜单是否有二级内容 */
	public List<LinksusInteractWxMenuItem> getLinksusInteractWxMenuItemByPrarentId(Long parentId);
}
