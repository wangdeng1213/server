package com.linksus.task;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.linksus.common.util.LogUtil;
import com.linksus.entity.LinksusInteractWeixin;

public class SendWeiXinTimer extends TaskReplyWeixins implements Job{

	public void execute(JobExecutionContext arg0) throws JobExecutionException{
		JobDataMap map = arg0.getJobDetail().getJobDataMap();// ȡ����
		Long pid = map.getLong("pid");// ΢��id����
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
				logger.error("��΢��״̬���Ǵ�����״̬,���ܷ���:currentstatus={}", weixin.getStatus());
			}
		}else{
			logger.error("��ʱ΢�ŷ���ʧ��,��˽�Ų�����:pid={}", pid);
		}
	}

}
