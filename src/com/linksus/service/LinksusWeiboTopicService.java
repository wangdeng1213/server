package com.linksus.service;

import com.linksus.entity.LinksusWeiboTopic;

public interface LinksusWeiboTopicService{

	/** ��ѯ΢�������Ƿ����ڻ������ */
	public LinksusWeiboTopic getIsLinksusWeiboTopic(LinksusWeiboTopic topic);

	/**�����Ѵ��ڵ��˻����������ݵĻ�����*/
	public void updateLinksusWeiboTopicByUsedNum(LinksusWeiboTopic topic);

	/**����������*/
	public void insertintoLinksusWeiboTopic(LinksusWeiboTopic topic);

}
