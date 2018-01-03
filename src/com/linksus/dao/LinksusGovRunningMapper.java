package com.linksus.dao;

import java.util.List;

import com.linksus.entity.LinksusGovRunning;

public interface LinksusGovRunningMapper{

	/** 列表查询 */
	public List<LinksusGovRunning> getLinksusGovRunningList();

	/** 主键查询 */
	public LinksusGovRunning getLinksusGovRunningById(Long pid);

	/** 新增 */
	public Integer insertLinksusGovRunning(LinksusGovRunning entity);

	/** 更新 */
	public Integer updateLinksusGovRunning(LinksusGovRunning entity);

	/** 主键删除 */
	public Integer deleteLinksusGovRunningById(Long pid);

}
