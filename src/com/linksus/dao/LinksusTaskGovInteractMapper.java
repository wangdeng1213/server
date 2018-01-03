package com.linksus.dao;

import java.util.List;

import com.linksus.entity.LinksusTaskGovInteract;

public interface LinksusTaskGovInteractMapper{

	/** 列表查询 */
	public List<LinksusTaskGovInteract> getLinksusTaskGovInteractList();

	/** 主键查询 */
	public LinksusTaskGovInteract getLinksusTaskGovInteractById(Long pid);

	/** 新增 */
	public Integer insertLinksusTaskGovInteract(LinksusTaskGovInteract entity);

	/** 更新 */
	public Integer updateLinksusTaskGovInteract(LinksusTaskGovInteract entity);

	/** 主键删除 */
	public Integer deleteLinksusTaskGovInteractById(Long pid);

	public LinksusTaskGovInteract getMaxIdByAccountId(LinksusTaskGovInteract linksusTaskGovInteract);

}
