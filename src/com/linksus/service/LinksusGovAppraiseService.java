package com.linksus.service;

import java.util.List;

import com.linksus.entity.LinksusGovAppraise;

public interface LinksusGovAppraiseService{

	/** 查询列表 */
	public List<LinksusGovAppraise> getLinksusGovAppraiseList();

	/** 主键查询 */
	public LinksusGovAppraise getLinksusGovAppraiseById(Long pid);

	/** 新增 */
	public Integer insertLinksusGovAppraise(LinksusGovAppraise entity);

	/** 更新 */
	public Integer updateLinksusGovAppraise(LinksusGovAppraise entity);

	/** 主键删除 */
	public Integer deleteLinksusGovAppraiseById(Long pid);

}
