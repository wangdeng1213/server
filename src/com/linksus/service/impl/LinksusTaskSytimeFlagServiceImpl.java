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

	/** 查询最后一次微博发送时间 */
	public LinksusTaskSytimeFlag getWeiboTopicLastTime(int tasktype){
		return linksusTaskSytimeFlagMapper.getWeiboTopicLastTime(tasktype);
	}

	/**插入最后一条微博发送时间*/
	public void insertWeiboTopicLastTime(LinksusTaskSytimeFlag taskSytimeFlag){
		linksusTaskSytimeFlagMapper.insertWeiboTopicLastTime(taskSytimeFlag);
	}

	/**更新最后一次微博发送时间*/
	public void updateWeiboTopicLastTime(LinksusTaskSytimeFlag taskSytimeFlag){
		linksusTaskSytimeFlagMapper.updateWeiboTopicLastTime(taskSytimeFlag);
	}
}