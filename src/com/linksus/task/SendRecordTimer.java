package com.linksus.task;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;

import com.linksus.common.util.LogUtil;
import com.linksus.entity.LinksusWeibo;

/**
 * ��ʱ΢������ ��ʱ��
 * 
 * @author zhangew
 * 
 */
public class SendRecordTimer extends TaskSendWeibo implements Job{

	public void execute(JobExecutionContext arg0){
		try{
			JobDataMap map = arg0.getJobDetail().getJobDataMap();// ȡ����
			Long pid = map.getLong("pid");// ΢��id����
			Integer reSendCount = map.getInt("reSendCount");// �ط�����
			LinksusWeibo record = weiboService.getLinksusWeiboById(pid);
			if(record != null){
				if(record.getStatus().intValue() == 1){
					if(reSendCount == null){
						reSendCount = 0;
					}
					record.setRegularFlag(true);// �ط���־
					record.setRepostCount(reSendCount);
					sendWeibo(record);
				}else{
					logger.error("��΢��״̬���Ǵ�����״̬,���ܷ���:currentstatus={}", record.getStatus());
				}
			}else{
				logger.error("��ʱ΢������ʧ��,��΢��������:pid={}", pid);
			}
		}catch (Exception e){
			LogUtil.saveException(logger, e);
		}
	}
}