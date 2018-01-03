package com.linksus.service;

import java.util.List;

import com.linksus.entity.LinksusGovRunning;

public interface LinksusGovRunningService{

	/** 查询列表 */
	public List<LinksusGovRunning> getLinksusGovRunningList();

	/** 主键查询 */
	public LinksusGovRunning getLinksusGovRunningById(Long pid);

	/** 新增 */
	public Integer insertLinksusGovRunning(LinksusGovRunning entity);

	/** 更新 */
	public Integer updateLinksusGovRunning(LinksusGovRunning entity);

	/** 主键删除 */
	public Integer deleteLinksusGovRunningById(Long pid);

	public void saveLinksusGovRunning(List<LinksusGovRunning> list,String operType);

}
