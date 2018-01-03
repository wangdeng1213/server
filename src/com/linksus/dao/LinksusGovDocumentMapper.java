package com.linksus.dao;

import java.util.List;

import com.linksus.entity.LinksusGovDocument;

public interface LinksusGovDocumentMapper{

	/** 列表查询 */
	public List<LinksusGovDocument> getLinksusGovDocumentList();

	/** 主键查询 */
	public LinksusGovDocument getLinksusGovDocumentById(Long pid);

	/** 新增 */
	public Integer insertLinksusGovDocument(LinksusGovDocument entity);

	/** 更新 */
	public Integer updateLinksusGovDocument(LinksusGovDocument entity);

	/** 主键删除 */
	public Integer deleteLinksusGovDocumentById(Long pid);

	public List<LinksusGovDocument> getLinksusGovDocumentListByAccount(Long accountId);

}
