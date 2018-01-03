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

	/** 查询微博内容是否已在话题表中*/
	public LinksusWeiboTopic getIsLinksusWeiboTopic(LinksusWeiboTopic topic){
		return linksusWeiboTopicMapper.getIsLinksusWeiboTopic(topic);
	}

	/**更新已存在的账户及话题内容的话题数*/
	public void updateLinksusWeiboTopicByUsedNum(LinksusWeiboTopic topic){
		linksusWeiboTopicMapper.updateLinksusWeiboTopicByUsedNum(topic);
	}

	/**插入新内容*/
	public void insertintoLinksusWeiboTopic(LinksusWeiboTopic topic){
		linksusWeiboTopicMapper.insertintoLinksusWeiboTopic(topic);
	}
}