package com.linksus.dao;

import com.linksus.entity.LinksusWeiboTopic;

public interface LinksusWeiboTopicMapper{

	/** ��ѯ΢�����˻��Ƿ���ڻ������ */
	public LinksusWeiboTopic getIsLinksusWeiboTopic(LinksusWeiboTopic topic);

	/**�����Ѵ��ڵ��˻����������ݵĻ�����*/
	void updateLinksusWeiboTopicByUsedNum(LinksusWeiboTopic topic);

	/**����������*/
	void insertintoLinksusWeiboTopic(LinksusWeiboTopic topic);

}
