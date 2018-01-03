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
 * �����������ͳ��������ʼ�
 * @author zhangew
 *
 */
public class TaskQueueStatistic{

	protected static final Logger logger = LoggerFactory.getLogger(TaskQueueStatistic.class);

	/**
	 * ��ʱͳ�ƶ���ˢ����� ���������Ա���䷢���ʼ�
	 */
	public void emailTaskStatistic(){
		try{
			MailProperty property = null;
			//�����ʼ��û���������ͳ���ʼ�
			MailUtil mailUtils = (MailUtil) ContextUtil.getBean("mail");
			String emailStr = LoadConfig.getString("TaskManagerEmail");
			Email email = new Email();
			email.setAddress(emailStr);
			email.setHtmlFlag(true);//html��ʽ
			email.setSubject("JAVA����ִ��ͳ��");
			email.setContent(TaskStatisticCommon.taskStatistic());
			mailUtils.sendEmail(email, property);
		}catch (Exception e){
			LogUtil.saveException(logger, e);
			e.printStackTrace();
		}
	}

	/**
	 * ��ָ�����䷢������ͳ����Ϣ
	 * @param emailAddr
	 */
	public void emailTaskStatistic(String emailAddr){
		try{
			MailProperty property = null;
			//�����ʼ��û���������ͳ���ʼ�
			MailUtil mailUtils = (MailUtil) ContextUtil.getBean("mail");
			Email email = new Email();
			email.setAddress(emailAddr);
			email.setHtmlFlag(true);//html��ʽ
			email.setSubject("JAVA����ִ��ͳ��");
			email.setContent(TaskStatisticCommon.taskStatistic());
			mailUtils.sendEmail(email, property);
		}catch (Exception e){
			LogUtil.saveException(logger, e);
			e.printStackTrace();
		}
	}

	/**
	 * ÿ��������յ����ͳ������
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
