package com.linksus.service;

import java.util.List;

import com.linksus.entity.LinksusRelationUserCompany;

public interface LinksusRelationUserCompanyService{

	/** 查询列表 */
	public List<LinksusRelationUserCompany> getLinksusRelationUserCompanyList();

	/** 主键查询 */
	public LinksusRelationUserCompany getLinksusRelationUserCompanyById(Long pid);

	/** 新增 */
	public Integer insertLinksusRelationUserCompany(LinksusRelationUserCompany entity);

	/** 更新 */
	public Integer updateLinksusRelationUserCompany(LinksusRelationUserCompany entity);

	/** 主键删除 */
	public Integer deleteLinksusRelationUserCompanyById(Long pid);

	/**根据userId删除*/
	public Integer deleteLinksusRelationUserCompanyByUserId(Long userId);

}
