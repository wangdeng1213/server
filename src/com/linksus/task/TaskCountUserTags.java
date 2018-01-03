package com.linksus.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linksus.common.util.ContextUtil;
import com.linksus.common.util.LogUtil;
import com.linksus.service.LinksusRelationUserTagService;
import com.linksus.service.LinksusRelationUserTagdefService;

/**
 * ��ǩʹ�ü�¼��ͳ������
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
	 * ��ǩʹ�ô���ͳ�Ƹ���
	 */
	public void updateLinksusRelationUserTagCount(){
		try{
			//����ǩʹ�ü�¼�������м��
			linksusRelationUserTagService.insertTagCountIntoMiddleTable();
			//���м���е����ݸ�������ǩ����
			linksusRelationUserTagdefService.updateLinksusRelationUserTagdefUseCount();
			//����м��
			linksusRelationUserTagdefService.clearUserTagMiddleTable();
		}catch (Exception e){
			LogUtil.saveException(logger, e);
			e.printStackTrace();
		}

	}

}
