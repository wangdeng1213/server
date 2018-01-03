package com.linksus.dao;

import java.util.List;

import com.linksus.entity.LinksusGovStructure;

public interface LinksusGovStructureMapper{

	/** 列表查询 */
	public List<LinksusGovStructure> getLinksusGovStructureList();

	/** 主键查询 */
	public LinksusGovStructure getLinksusGovStructureById(Long pid);

	/** 新增 */
	public Integer insertLinksusGovStructure(LinksusGovStructure entity);

	/** 更新 */
	public Integer updateLinksusGovStructure(LinksusGovStructure entity);

	/** 主键删除 */
	public Integer deleteLinksusGovStructureById(Long pid);

	/**
	 * 根据OrgId获取组织数据
	 * @param orgId
	 * @return
	 */
	public List<LinksusGovStructure> getLinksusGovStructureByOrgId();

	/** 根据account_id查询对应最小的gid */
	public LinksusGovStructure getMinGidByAccountId(Long accountId);
}
