package com.linksus.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linksus.common.util.ContextUtil;
import com.linksus.common.util.LogUtil;
import com.linksus.service.LinksusRelationUserTagService;
import com.linksus.service.LinksusRelationUserTagdefService;

/**
 * 标签使用记录数统计任务
 */
public class TaskCountUserTags extends BaseTask{

	protected static final Logger logger = LoggerFactory.getLogger(TaskCountUserTags.class);

	LinksusRelationUserTagService linksusRelationUserTagService = (LinksusRelationUserTagService) ContextUtil
			.getBean("linksusRelationUserTagService");

	LinksusRelationUserTagdefService linksusRelationUserTagdefService = (LinksusRelationUserTagdefService) ContextUtil
			.getBean("linksusRelationUserTagdefService");

	@Override
	public void cal(){
		updateLinksusRelationUserTagCount();
	}

	/*
	 * 标签使用次数统计更新
	 */
	public void updateLinksusRelationUserTagCount(){
		try{
			//将标签使用记录数插入中间表
			linksusRelationUserTagService.insertTagCountIntoMiddleTable();
			//将中间表中的数据更新至标签表中
			linksusRelationUserTagdefService.updateLinksusRelationUserTagdefUseCount();
			//清空中间表
			linksusRelationUserTagdefService.clearUserTagMiddleTable();
		}catch (Exception e){
			LogUtil.saveException(logger, e);
			e.printStackTrace();
		}

	}

}
