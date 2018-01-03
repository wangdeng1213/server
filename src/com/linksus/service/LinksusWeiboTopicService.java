package com.linksus.service;

import com.linksus.entity.LinksusWeiboTopic;

public interface LinksusWeiboTopicService{

	/** 查询微博内容是否已在话题表中 */
	public LinksusWeiboTopic getIsLinksusWeiboTopic(LinksusWeiboTopic topic);

	/**更新已存在的账户及话题内容的话题数*/
	public void updateLinksusWeiboTopicByUsedNum(LinksusWeiboTopic topic);

	/**插入新内容*/
	public void insertintoLinksusWeiboTopic(LinksusWeiboTopic topic);

}
