package com.linksus.service;

import java.util.List;

import com.linksus.entity.LinksusGovStructure;

public interface LinksusGovStructureService{

	/** 查询列表 */
	public List<LinksusGovStructure> getLinksusGovStructureList();

	/** 主键查询 */
	public LinksusGovStructure getLinksusGovStructureById(Long pid);

	/** 新增 */
	public Integer insertLinksusGovStructure(LinksusGovStructure entity);

	/** 更新 */
	public Integer updateLinksusGovStructure(LinksusGovStructure entity);

	/** 主键删除 */
	public Integer deleteLinksusGovStructureById(Long pid);

	/** 根据组织ID查询 */
	public List<LinksusGovStructure> getLinksusGovStructureByOrgId();

	/** 根据account_id查询对应最小的gid */
	public LinksusGovStructure getMinGidByAccountId(Long accountId);

}
