package com.linksus.service;

import java.util.List;

import com.linksus.entity.LinksusGovMeger;

public interface LinksusGovMegerService{

	/** 查询列表 */
	public List<LinksusGovMeger> getLinksusGovMegerList();

	/** 主键查询 */
	public LinksusGovMeger getLinksusGovMegerById(Long pid);

	/** 新增 */
	public Integer insertLinksusGovMeger(LinksusGovMeger entity);

	/** 更新 */
	public Integer updateLinksusGovMeger(LinksusGovMeger entity);

	/** 主键删除 */
	public Integer deleteLinksusGovMegerById(Long pid);

	/**
	 * 根据父节点查询
	 * @return
	 */
	public List<LinksusGovMeger> getLinksusGovMegerListByFather(Long fatherId);

}
