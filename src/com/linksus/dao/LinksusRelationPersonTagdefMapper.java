package com.linksus.dao;

import java.util.List;

import com.linksus.entity.LinksusRelationPersonTagdef;

public interface LinksusRelationPersonTagdefMapper{

	/** 列表查询 */
	public List<LinksusRelationPersonTagdef> getLinksusRelationPersonTagdefList();

	/** 主键查询 */
	public LinksusRelationPersonTagdef getLinksusRelationPersonTagdefById(Long pid);

	/** 新增 */
	public Integer insertLinksusRelationPersonTagdef(LinksusRelationPersonTagdef entity);

	/** 更新 */
	public Integer updateLinksusRelationPersonTagdef(LinksusRelationPersonTagdef entity);

	/** 主键删除 */
	public Integer deleteLinksusRelationPersonTagdefById(Long pid);

	/** tag_name查询 */
	public LinksusRelationPersonTagdef getLinksusRelationPersonTagdefByTagName(String tag_name);

	/** 标签使用次数统计更新 */
	public Integer updateLinksusRelationUserTagdefUseCount(LinksusRelationPersonTagdef entity);
}
