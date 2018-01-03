package com.linksus.service;

import java.util.List;
import java.util.Map;

import com.linksus.entity.LinksusRelationMarketing;

public interface LinksusRelationMarketingService{

	/**查询审批通过的需要发表评论的营销列表*/
	public List<LinksusRelationMarketing> getSendCommentsList(Map paraMap);

	/**根据营销主表主键更新失败成功总数*/
	public void updateLinksysUserMarketing(LinksusRelationMarketing linksusRelationMarketing);

	/**查询审批通过的新浪或腾讯账号类型的营销列表*/
	public List<LinksusRelationMarketing> getSendCommentsByUserList(LinksusRelationMarketing linksusRelationMarketing);

	public void updateMentionsStatus(LinksusRelationMarketing marketing);

}
