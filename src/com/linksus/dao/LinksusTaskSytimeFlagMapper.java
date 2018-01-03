package com.linksus.dao;

import com.linksus.entity.LinksusTaskSytimeFlag;

public interface LinksusTaskSytimeFlagMapper{

	/** 查询最后一次微博发送时间 */
	public LinksusTaskSytimeFlag getWeiboTopicLastTime(int tasktype);

	/**插入最后一条微博发送时间*/
	void insertWeiboTopicLastTime(LinksusTaskSytimeFlag taskSytimeFlag);

	/**更新最后一次微博发送时间*/
	void updateWeiboTopicLastTime(LinksusTaskSytimeFlag taskSytimeFlag);

}
