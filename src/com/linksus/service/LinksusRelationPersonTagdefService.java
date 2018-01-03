package com.linksus.service;

import java.util.List;

import com.linksus.entity.LinksusRelationPersonTagdef;

public interface LinksusRelationPersonTagdefService{

	/** 查询列表 */
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

}
