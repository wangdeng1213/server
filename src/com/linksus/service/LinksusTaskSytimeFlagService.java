package com.linksus.service;

import com.linksus.entity.LinksusTaskSytimeFlag;

public interface LinksusTaskSytimeFlagService{

	/** ��ѯ���һ��΢������ʱ�� */
	public LinksusTaskSytimeFlag getWeiboTopicLastTime(int tasktype);

	/**�������һ��΢������ʱ��*/
	public void insertWeiboTopicLastTime(LinksusTaskSytimeFlag taskSytimeFlag);

	/**�������һ��΢������ʱ��*/
	public void updateWeiboTopicLastTime(LinksusTaskSytimeFlag taskSytimeFlag);

}
