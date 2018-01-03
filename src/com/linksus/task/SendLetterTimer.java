package com.linksus.task;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.linksus.common.util.LogUtil;
import com.linksus.entity.LinksusInteractMessage;

/**
 * ��ʱ˽�ŷ��� ��ʱ��
 * @author zhangew
 *
 */
public class SendLetterTimer extends TaskSendSinaLetter implements Job{

	public void execute(JobExecutionContext arg0) throws JobExecutionException{
		JobDataMap map = arg0.getJobDetail().getJobDataMap();// ȡ����
		Long pid = map.getLong("pid");// ˽��id����
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
				logger.error("��˽��״̬���Ǵ�����״̬,���ܷ���:currentstatus={}", message.getStatus());
			}
		}else{
			logger.error("��ʱ˽�ŷ���ʧ��,��˽�Ų�����:pid={}", pid);
		}
	}
}