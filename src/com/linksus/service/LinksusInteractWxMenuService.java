package com.linksus.service;

import java.util.List;

import com.linksus.entity.LinksusInteractWxMenu;

public interface LinksusInteractWxMenuService{

	/** 查询列表 */
	public List<LinksusInteractWxMenu> getLinksusInteractWxMenuList();

	/** 主键查询 */
	public LinksusInteractWxMenu getLinksusInteractWxMenuById(Long pid);

	/** 新增 */
	public Integer insertLinksusInteractWxMenu(LinksusInteractWxMenu entity);

	/** 更新 */
	public Integer updateLinksusInteractWxMenu(LinksusInteractWxMenu entity);

	/** 主键删除 */
	public Integer deleteLinksusInteractWxMenuById(Long pid);

}
