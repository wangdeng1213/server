package com.linksus.service;

import java.util.List;

import com.linksus.entity.LinksusGovYixin;

public interface LinksusGovYixinService{

	/** 查询列表 */
	public List<LinksusGovYixin> getLinksusGovYixinList();

	/** 主键查询 */
	public LinksusGovYixin getLinksusGovYixinById(Long pid);

	/** 新增 */
	public Integer insertLinksusGovYixin(LinksusGovYixin entity);

	/** 更新 */
	public Integer updateLinksusGovYixin(LinksusGovYixin entity);

	/** 主键删除 */
	public Integer deleteLinksusGovYixinById(Long pid);

}
