package com.linksus.task;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.linksus.common.util.LogUtil;
import com.linksus.entity.LinksusInteractMessage;

/**
 * 定时私信发布 定时器
 * @author zhangew
 *
 */
public class SendLetterTimer extends TaskSendSinaLetter implements Job{

	public void execute(JobExecutionContext arg0) throws JobExecutionException{
		JobDataMap map = arg0.getJobDetail().getJobDataMap();// 取参数
		Long pid = map.getLong("pid");// 私信id主键
		LinksusInteractMessage message = messageService.getSendMessageById(pid);
		if(message != null){
			if(message.getStatus().intValue() == 1){
				try{
					sendMessage(message);
				}catch (Exception e){
					LogUtil.saveException(logger, e);
					e.printStackTrace();
				}
			}else{
				logger.error("该私信状态不是待发布状态,不能发布:currentstatus={}", message.getStatus());
			}
		}else{
			logger.error("定时私信发布失败,该私信不存在:pid={}", pid);
		}
	}
}