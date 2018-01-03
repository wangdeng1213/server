package com.linksus.task;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.linksus.entity.LinksusInteractWeibo;

public class SendReplyTimer extends TaskReplyComments implements Job{

	public void execute(JobExecutionContext arg0) throws JobExecutionException{
		JobDataMap map = arg0.getJobDetail().getJobDataMap();// ȡ����
		Long pid = map.getLong("pid");// ����
		LinksusInteractWeibo record = linksusInteractWeiboService.getReplyWeiboById(pid);
		if(record != null){
			if(record.getStatus().intValue() == 1){
				sendWeiboReply(record);
			}else{
				logger.error("������״̬���Ǵ�����״̬,���ܷ���:currentstatus={}", record.getStatus());
			}
		}else{
			logger.error("��ʱ���۷���ʧ��,��΢��������:pid={}", pid);
		}
	}
}
