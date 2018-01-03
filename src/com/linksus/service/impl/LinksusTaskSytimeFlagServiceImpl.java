package com.linksus.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.dao.LinksusTaskSytimeFlagMapper;
import com.linksus.entity.LinksusTaskSytimeFlag;
import com.linksus.service.LinksusTaskSytimeFlagService;

@Service("linksusTaskSytimeFlagService")
public class LinksusTaskSytimeFlagServiceImpl implements LinksusTaskSytimeFlagService{

	@Autowired
	private LinksusTaskSytimeFlagMapper linksusTaskSytimeFlagMapper;

	/** ��ѯ���һ��΢������ʱ�� */
	public LinksusTaskSytimeFlag getWeiboTopicLastTime(int tasktype){
		return linksusTaskSytimeFlagMapper.getWeiboTopicLastTime(tasktype);
	}

	/**�������һ��΢������ʱ��*/
	public void insertWeiboTopicLastTime(LinksusTaskSytimeFlag taskSytimeFlag){
		linksusTaskSytimeFlagMapper.insertWeiboTopicLastTime(taskSytimeFlag);
	}

	/**�������һ��΢������ʱ��*/
	public void updateWeiboTopicLastTime(LinksusTaskSytimeFlag taskSytimeFlag){
		linksusTaskSytimeFlagMapper.updateWeiboTopicLastTime(taskSytimeFlag);
	}
}