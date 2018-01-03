package com.linksus.service;

import java.util.List;

import com.linksus.entity.LinksusRelationPersonTag;

public interface LinksusRelationPersonTagService{

	/** 查询列表 */
	public List<LinksusRelationPersonTag> getLinksusRelationPersonTagList();

	/** 主键查询 */
	public LinksusRelationPersonTag getLinksusRelationPersonTagById(Long pid);

	/** 新增 */
	public Integer insertLinksusRelationPersonTag(LinksusRelationPersonTag entity);

	/** 更新 */
	public Integer updateLinksusRelationPersonTag(LinksusRelationPersonTag entity);

	/** 主键删除 */
	public Integer deleteLinksusRelationPersonTagById(Long pid);

	/** 删除personId的数据 */
	public Integer deleteByPersonId(Long personId);

}
