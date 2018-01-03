package com.linksus.dao;

import java.util.List;

import com.linksus.entity.LinksusRelationOperateItem;

public interface LinksusRelationOperateItemMapper{

	/** 列表查询 */
	public List<LinksusRelationOperateItem> getLinksusRelationOperateItemList();

	/** 主键查询 */
	public LinksusRelationOperateItem getLinksusRelationOperateItemById(Long pid);

	/** 新增 */
	public Integer insertLinksusRelationOperateItem(LinksusRelationOperateItem entity);

	/** 更新 */
	public Integer updateLinksusRelationOperateItem(LinksusRelationOperateItem entity);

	/** 主键删除 */
	public Integer deleteLinksusRelationOperateItemById(Long pid);

}
