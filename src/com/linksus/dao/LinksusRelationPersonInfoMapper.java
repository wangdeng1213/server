package com.linksus.dao;

import java.util.List;

import com.linksus.entity.LinksusRelationPersonInfo;

public interface LinksusRelationPersonInfoMapper{

	/** 列表查询 */
	public List<LinksusRelationPersonInfo> getLinksusRelationPersonInfoList();

	/** 主键查询 */
	public LinksusRelationPersonInfo getLinksusRelationPersonInfoById(Long pid);

	/** 新增 */
	public Integer insertLinksusRelationPersonInfo(LinksusRelationPersonInfo entity);

	/** 更新 */
	public Integer updateLinksusRelationPersonInfo(LinksusRelationPersonInfo entity);

	/** 主键删除 */
	public Integer deleteLinksusRelationPersonInfoById(Long pid);

}
