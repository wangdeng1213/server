package com.linksus.dao;

import java.util.List;

import com.linksus.entity.LinksusInteractAttentionReplyAcct;

public interface LinksusInteractAttentionReplyAcctMapper{

	/** 列表查询 */
	public List<LinksusInteractAttentionReplyAcct> getLinksusInteractAttentionReplyAcctList();

	/** 主键查询 */
	public LinksusInteractAttentionReplyAcct getLinksusInteractAttentionReplyAcctById(Long pid);

	/** 新增 */
	public Integer insertLinksusInteractAttentionReplyAcct(LinksusInteractAttentionReplyAcct entity);

	/** 更新 */
	public Integer updateLinksusInteractAttentionReplyAcct(LinksusInteractAttentionReplyAcct entity);

	/** 主键删除 */
	public Integer deleteLinksusInteractAttentionReplyAcctById(Long pid);

	/** accountId主键查询 */
	public LinksusInteractAttentionReplyAcct getLinksusInteractAttentionReplyAcctByAccountId(Long accountId);
}
