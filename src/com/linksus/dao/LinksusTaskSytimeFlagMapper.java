package com.linksus.dao;

import com.linksus.entity.LinksusTaskSytimeFlag;

public interface LinksusTaskSytimeFlagMapper{

	/** ��ѯ���һ��΢������ʱ�� */
	public LinksusTaskSytimeFlag getWeiboTopicLastTime(int tasktype);

	/**�������һ��΢������ʱ��*/
	void insertWeiboTopicLastTime(LinksusTaskSytimeFlag taskSytimeFlag);

	/**�������һ��΢������ʱ��*/
	void updateWeiboTopicLastTime(LinksusTaskSytimeFlag taskSytimeFlag);

}
