package com.linksus.dao;

import java.util.List;
import java.util.Map;

import com.linksus.entity.LinksusPersonaccount;

public interface LinksusPersonaccountMapper{

	/** 列表查询 */
	public List<LinksusPersonaccount> getLinksusPersonaccountList();

	/** 主键查询 */
	public LinksusPersonaccount getLinksusPersonaccountById(Long pid);

	/** 新增 */
	public Integer insertLinksusPersonaccount(LinksusPersonaccount entity);

	/** 更新 */
	public Integer updateLinksusPersonaccount(LinksusPersonaccount entity);

	/** 主键删除 */
	public Integer deleteLinksusPersonaccountById(Long pid);

	/** 根据机构id获取管理员信息 */
	public Map getManagerByInstitutionId(Long institutionId);

	public List getPersonaccountByInstitutionId(Long institutionId);

}
