package com.linksus.service;

import java.util.List;

import com.linksus.entity.LinksusGovTag;

public interface LinksusGovTagService{

	/** 查询列表 */
	public List<LinksusGovTag> getLinksusGovTagList();

	/** 主键查询 */
	public LinksusGovTag getLinksusGovTagById(Long pid);

	/** 新增 */
	public Integer insertLinksusGovTag(LinksusGovTag entity);

	/** 更新 */
	public Integer updateLinksusGovTag(LinksusGovTag entity);

	/** 主键删除 */
	public Integer deleteLinksusGovTagById(Long pid);

}
