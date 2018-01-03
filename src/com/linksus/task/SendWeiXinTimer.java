package com.linksus.task;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.linksus.common.util.LogUtil;
import com.linksus.entity.LinksusInteractWeixin;

public class SendWeiXinTimer extends TaskReplyWeixins implements Job{

	public void execute(JobExecutionContext arg0) throws JobExecutionException{
		JobDataMap map = arg0.getJobDetail().getJobDataMap();// 取参数
		Long pid = map.getLong("pid");// 微信id主键
		LinksusInteractWeixin weixin = linksusInteractWeixinService.getLinksusInteractWeixinById(pid);
		if(weixin != null){
			if(weixin.getStatus().intValue() == 1){
				try{
					sendWeiXinTask(weixin);
				}catch (Exception e){
					LogUtil.saveException(logger, e);
					e.printStackTrace();
				}
			}else{
				logger.error("该微信状态不是待发布状态,不能发布:currentstatus={}", weixin.getStatus());
			}
		}else{
			logger.error("定时微信发布失败,该私信不存在:pid={}", pid);
		}
	}

}
