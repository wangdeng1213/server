package com.linksus.service;

import java.util.List;

import com.linksus.entity.LinksusRelationPersonOper;

public interface LinksusRelationPersonOperService{

	/** 查询列表 */
	public List<LinksusRelationPersonOper> getLinksusRelationPersonOperList();

	/** 主键查询 */
	public LinksusRelationPersonOper getLinksusRelationPersonOperById(Long pid);

	/** 新增 */
	public Integer insertLinksusRelationPersonOper(LinksusRelationPersonOper entity);

	/** 更新 */
	public Integer updateLinksusRelationPersonOper(LinksusRelationPersonOper entity);

	/** 主键删除 */
	public Integer deleteLinksusRelationPersonOperById(Long pid);

}
