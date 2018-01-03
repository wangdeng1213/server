package com.linksus.dao;

import java.util.List;
import java.util.Map;

import com.linksus.entity.LinksusInteractAttentionReply;

public interface LinksusInteractAttentionReplyMapper{

	/** 列表查询 */
	public List<LinksusInteractAttentionReply> getLinksusInteractAttentionReplyList();

	/** 主键查询 */
	public LinksusInteractAttentionReply getLinksusInteractAttentionReplyById(Long pid);

	/** 新增 */
	public Integer insertLinksusInteractAttentionReply(LinksusInteractAttentionReply entity);

	/** 更新 */
	public Integer updateLinksusInteractAttentionReply(LinksusInteractAttentionReply entity);

	/** 主键删除 */
	public Integer deleteLinksusInteractAttentionReplyById(Long pid);

	/** 通过主键及平台类型查询 */
	public LinksusInteractAttentionReply getLinksusInteractAttentionReplyByIdAndType(Map map);
}
