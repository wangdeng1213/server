package com.linksus.dao;

import java.util.List;

import com.linksus.entity.LinksusGovMeger;

public interface LinksusGovMegerMapper{

	/** 列表查询 */
	public List<LinksusGovMeger> getLinksusGovMegerList();

	/** 主键查询 */
	public LinksusGovMeger getLinksusGovMegerById(Long pid);

	/** 新增 */
	public Integer insertLinksusGovMeger(LinksusGovMeger entity);

	/** 更新 */
	public Integer updateLinksusGovMeger(LinksusGovMeger entity);

	/** 主键删除 */
	public Integer deleteLinksusGovMegerById(Long pid);

	public List<LinksusGovMeger> getLinksusGovMegerListByFather(Long fatherId);

}
