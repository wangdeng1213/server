package com.linksus.dao;

import com.linksus.entity.LinksusWeiboTopic;

public interface LinksusWeiboTopicMapper{

	/** 查询微博及账户是否存在话题表中 */
	public LinksusWeiboTopic getIsLinksusWeiboTopic(LinksusWeiboTopic topic);

	/**更新已存在的账户及话题内容的话题数*/
	void updateLinksusWeiboTopicByUsedNum(LinksusWeiboTopic topic);

	/**插入新内容*/
	void insertintoLinksusWeiboTopic(LinksusWeiboTopic topic);

}
