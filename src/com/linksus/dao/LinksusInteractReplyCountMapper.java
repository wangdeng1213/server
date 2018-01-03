package com.linksus.dao;

import java.util.List;
import java.util.Map;

import com.linksus.entity.LinksusInteractReplyCount;

public interface LinksusInteractReplyCountMapper{

	/** 列表查询 */
	public List<LinksusInteractReplyCount> getLinksusInteractReplyCountList();

	/** 主键查询 */
	public LinksusInteractReplyCount getLinksusInteractReplyCountById(Long pid);

	/** 新增 */
	public Integer insertLinksusInteractReplyCount(LinksusInteractReplyCount entity);

	/** 更新 */
	public Integer updateLinksusInteractReplyCount(LinksusInteractReplyCount entity);

	/** 主键删除 */
	public Integer deleteLinksusInteractReplyCountById(Long pid);

	/** 业务组合主键查询 */
	public LinksusInteractReplyCount getLinksusInteractReplyCountByMap(Map map);
}
