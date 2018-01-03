package com.linksus.service;

import java.util.List;
import java.util.Map;

import com.linksus.entity.LinksusPersonaccount;

public interface LinksusPersonaccountService{

	/** 查询列表 */
	public List<LinksusPersonaccount> getLinksusPersonaccountList();

	/** 主键查询 */
	public LinksusPersonaccount getLinksusPersonaccountById(Long pid);

	/** 新增 */
	public Integer insertLinksusPersonaccount(LinksusPersonaccount entity);

	/** 更新 */
	public Integer updateLinksusPersonaccount(LinksusPersonaccount entity);

	/** 主键删除 */
	public Integer deleteLinksusPersonaccountById(Long pid);

	public Map getManagerByInstitutionId(Long institutionId);

	public List getPersonaccountByInstitutionId(Long institutionId);

}
