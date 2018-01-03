package com.linksus.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.dao.LinksusWeiboTopicMapper;
import com.linksus.entity.LinksusWeiboTopic;
import com.linksus.service.LinksusWeiboTopicService;

@Service("linksusWeiboTopicService")
public class LinksusWeiboTopicServiceImpl implements LinksusWeiboTopicService{

	@Autowired
	private LinksusWeiboTopicMapper linksusWeiboTopicMapper;

	/** ��ѯ΢�������Ƿ����ڻ������*/
	public LinksusWeiboTopic getIsLinksusWeiboTopic(LinksusWeiboTopic topic){
		return linksusWeiboTopicMapper.getIsLinksusWeiboTopic(topic);
	}

	/**�����Ѵ��ڵ��˻����������ݵĻ�����*/
	public void updateLinksusWeiboTopicByUsedNum(LinksusWeiboTopic topic){
		linksusWeiboTopicMapper.updateLinksusWeiboTopicByUsedNum(topic);
	}

	/**����������*/
	public void insertintoLinksusWeiboTopic(LinksusWeiboTopic topic){
		linksusWeiboTopicMapper.insertintoLinksusWeiboTopic(topic);
	}
}