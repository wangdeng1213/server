package com.linksus.service;

import java.util.List;
import java.util.Map;

import com.linksus.entity.LinksusInteractKeywordReply;

public interface LinksusInteractKeywordReplyService{

	/** 查询列表 */
	public List<LinksusInteractKeywordReply> getLinksusInteractKeywordReplyList();

	/** 主键查询 */
	public LinksusInteractKeywordReply getLinksusInteractKeywordReplyById(Long pid);

	/** 新增 */
	public Integer insertLinksusInteractKeywordReply(LinksusInteractKeywordReply entity);

	/** 更新 */
	public Integer updateLinksusInteractKeywordReply(LinksusInteractKeywordReply entity);

	/** 主键删除 */
	public Integer deleteLinksusInteractKeywordReplyById(Long pid);

	/** 通过关联查询，查询账户下所有关键字内容 */
	public LinksusInteractKeywordReply getAllKeywordsByAccountId(Map accountId);

}
