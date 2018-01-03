package com.linksus.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linksus.common.config.LoadConfig;
import com.linksus.common.module.TaskStatisticCommon;
import com.linksus.common.util.ContextUtil;
import com.linksus.common.util.LogUtil;
import com.linksus.common.util.MailUtil;
import com.linksus.entity.Email;
import com.linksus.entity.MailProperty;
import com.linksus.interfaces.BaseInterface;
import com.linksus.queue.BaseQueue;

/**
 * 发送任务队列统计情况的邮件
 * @author zhangew
 *
 */
public class TaskQueueStatistic{

	protected static final Logger logger = LoggerFactory.getLogger(TaskQueueStatistic.class);

	/**
	 * 定时统计队列刷新情况 并向管理人员邮箱发送邮件
	 */
	public void emailTaskStatistic(){
		try{
			MailProperty property = null;
			//向多个邮件用户发送任务统计邮件
			MailUtil mailUtils = (MailUtil) ContextUtil.getBean("mail");
			String emailStr = LoadConfig.getString("TaskManagerEmail");
			Email email = new Email();
			email.setAddress(emailStr);
			email.setHtmlFlag(true);//html格式
			email.setSubject("JAVA任务执行统计");
			email.setContent(TaskStatisticCommon.taskStatistic());
			mailUtils.sendEmail(email, property);
		}catch (Exception e){
			LogUtil.saveException(logger, e);
			e.printStackTrace();
		}
	}

	/**
	 * 向指定邮箱发送任务统计信息
	 * @param emailAddr
	 */
	public void emailTaskStatistic(String emailAddr){
		try{
			MailProperty property = null;
			//向多个邮件用户发送任务统计邮件
			MailUtil mailUtils = (MailUtil) ContextUtil.getBean("mail");
			Email email = new Email();
			email.setAddress(emailAddr);
			email.setHtmlFlag(true);//html格式
			email.setSubject("JAVA任务执行统计");
			email.setContent(TaskStatisticCommon.taskStatistic());
			mailUtils.sendEmail(email, property);
		}catch (Exception e){
			LogUtil.saveException(logger, e);
			e.printStackTrace();
		}
	}

	/**
	 * 每天日终清空当天的统计数据
	 */
	public void clearTaskStatistic(){
		try{
			BaseQueue.resetTaskStatisticMap();
			BaseTask.resetTaskStatisticMap();
			BaseInterface.resetTaskStatisticMap();
			CronJob.resetTaskStatisticMap();
			LoadConfig.reflushUrlEntity();
		}catch (Exception e){
			LogUtil.saveException(logger, e);
			e.printStackTrace();
		}
	}
}
