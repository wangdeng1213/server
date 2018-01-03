package com.linksus.service;

import java.util.List;

import com.linksus.entity.LinksusInteractKeyword;

public interface LinksusInteractKeywordService{

	/** 查询列表 */
	public List<LinksusInteractKeyword> getLinksusInteractKeywordList();

	/** 主键查询 */
	public LinksusInteractKeyword getLinksusInteractKeywordById(Long pid);

	/** 新增 */
	public Integer insertLinksusInteractKeyword(LinksusInteractKeyword entity);

	/** 更新 */
	public Integer updateLinksusInteractKeyword(LinksusInteractKeyword entity);

	/** 主键删除 */
	public Integer deleteLinksusInteractKeywordById(Long pid);

}
