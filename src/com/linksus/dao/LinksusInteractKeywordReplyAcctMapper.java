package com.linksus.dao;

import java.util.List;

import com.linksus.entity.LinksusInteractKeywordReplyAcct;

public interface LinksusInteractKeywordReplyAcctMapper{

	/** 列表查询 */
	public List<LinksusInteractKeywordReplyAcct> getLinksusInteractKeywordReplyAcctList();

	/** 主键查询 */
	public LinksusInteractKeywordReplyAcct getLinksusInteractKeywordReplyAcctById(Long pid);

	/** 新增 */
	public Integer insertLinksusInteractKeywordReplyAcct(LinksusInteractKeywordReplyAcct entity);

	/** 更新 */
	public Integer updateLinksusInteractKeywordReplyAcct(LinksusInteractKeywordReplyAcct entity);

	/** 主键删除 */
	public Integer deleteLinksusInteractKeywordReplyAcctById(Long pid);

}
