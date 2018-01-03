package com.linksus.task;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;

import com.linksus.common.util.LogUtil;
import com.linksus.entity.LinksusWeibo;

/**
 * 定时微博发布 定时器
 * 
 * @author zhangew
 * 
 */
public class SendRecordTimer extends TaskSendWeibo implements Job{

	public void execute(JobExecutionContext arg0){
		try{
			JobDataMap map = arg0.getJobDetail().getJobDataMap();// 取参数
			Long pid = map.getLong("pid");// 微博id主键
			Integer reSendCount = map.getInt("reSendCount");// 重发次数
			LinksusWeibo record = weiboService.getLinksusWeiboById(pid);
			if(record != null){
				if(record.getStatus().intValue() == 1){
					if(reSendCount == null){
						reSendCount = 0;
					}
					record.setRegularFlag(true);// 重发标志
					record.setRepostCount(reSendCount);
					sendWeibo(record);
				}else{
					logger.error("该微博状态不是待发布状态,不能发布:currentstatus={}", record.getStatus());
				}
			}else{
				logger.error("定时微博发布失败,该微博不存在:pid={}", pid);
			}
		}catch (Exception e){
			LogUtil.saveException(logger, e);
		}
	}
}