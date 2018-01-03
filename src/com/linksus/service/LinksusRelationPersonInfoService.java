package com.linksus.service;

import java.util.List;

import com.linksus.entity.LinksusRelationPersonInfo;

public interface LinksusRelationPersonInfoService{

	/** 查询列表 */
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
