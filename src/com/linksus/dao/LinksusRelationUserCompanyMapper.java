package com.linksus.dao;

import java.util.List;

import com.linksus.entity.LinksusRelationUserCompany;

public interface LinksusRelationUserCompanyMapper{

	/** 列表查询 */
	public List<LinksusRelationUserCompany> getLinksusRelationUserCompanyList();

	/** 主键查询 */
	public LinksusRelationUserCompany getLinksusRelationUserCompanyById(Long pid);

	/** 新增 */
	public Integer insertLinksusRelationUserCompany(LinksusRelationUserCompany entity);

	/** 更新 */
	public Integer updateLinksusRelationUserCompany(LinksusRelationUserCompany entity);

	/** 主键删除 */
	public Integer deleteLinksusRelationUserCompanyById(Long pid);

	/**根据用户id删除职业信息*/
	public Integer deleteLinksusRelationUserCompanyByUserId(Long userId);

}
