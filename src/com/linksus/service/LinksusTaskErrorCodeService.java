package com.linksus.service;

import java.util.List;

import com.linksus.entity.LinksusTaskErrorCode;

public interface LinksusTaskErrorCodeService{

	/** 查询列表 */
	public List<LinksusTaskErrorCode> getLinksusTaskErrorCodeList();

	/** 主键查询 */
	public LinksusTaskErrorCode getLinksusTaskErrorCodeById(Long pid);

	/** 新增 */
	public Integer insertLinksusTaskErrorCode(LinksusTaskErrorCode entity);

	/** 更新 */
	public Integer updateLinksusTaskErrorCode(LinksusTaskErrorCode entity);

	/** 主键删除 */
	public Integer deleteLinksusTaskErrorCodeById(Long pid);

}
