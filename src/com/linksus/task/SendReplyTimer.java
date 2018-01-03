package com.linksus.task;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.linksus.entity.LinksusInteractWeibo;

public class SendReplyTimer extends TaskReplyComments implements Job{

	public void execute(JobExecutionContext arg0) throws JobExecutionException{
		JobDataMap map = arg0.getJobDetail().getJobDataMap();// 取参数
		Long pid = map.getLong("pid");// 评论
		LinksusInteractWeibo record = linksusInteractWeiboService.getReplyWeiboById(pid);
		if(record != null){
			if(record.getStatus().intValue() == 1){
				sendWeiboReply(record);
			}else{
				logger.error("该评论状态不是待发布状态,不能发布:currentstatus={}", record.getStatus());
			}
		}else{
			logger.error("定时评论发布失败,该微博不存在:pid={}", pid);
		}
	}
}
