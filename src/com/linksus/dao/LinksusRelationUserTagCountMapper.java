package com.linksus.dao;

import java.util.List;

import com.linksus.entity.LinksusRelationUserTagCount;

public interface LinksusRelationUserTagCountMapper{

	/** 列表查询 */
	public List<LinksusRelationUserTagCount> getLinksusRelationUserTagCountList();

	/** 主键查询 */
	public LinksusRelationUserTagCount getLinksusRelationUserTagCountById(Long pid);

	/** 新增 */
	public Integer insertLinksusRelationUserTagCount(LinksusRelationUserTagCount entity);

	/** 更新 */
	public Integer updateLinksusRelationUserTagCount(LinksusRelationUserTagCount entity);

	/** 主键删除 */
	public Integer deleteLinksusRelationUserTagCountById(Long pid);

	/** 全表删除 */
	public Integer deleteALLLinksusRelationUserTagCount();

	/** 全表新增 */
	public Integer insertALLLinksusRelationUserTagCount();

}
