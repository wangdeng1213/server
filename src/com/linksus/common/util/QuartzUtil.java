package com.linksus.common.util;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleTrigger;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ��ʱ���������
 */
public class QuartzUtil{

	private static final Logger logger = LoggerFactory.getLogger(QuartzUtil.class);
	private static SchedulerFactory gSchedulerFactory = new StdSchedulerFactory();
	private static Scheduler scheduler;
	private static String JOB_GROUP_NAME = "JOBGROUP_NAME";
	private static String TRIGGER_GROUP_NAME = "TRIGGERGROUP_NAME";

	public static synchronized Scheduler getScheduler() throws SchedulerException{
		if(scheduler == null){
			scheduler = gSchedulerFactory.getScheduler();
		}
		return scheduler;
	}

	/**
	 * ���һ����ʱ����ʹ��Ĭ�ϵ�������������������������������
	 * 
	 * @param jobName
	 *            ������
	 * @param jobClass
	 *            ����
	 * @param time
	 *            ʱ�����ã��ο�quartz˵���ĵ�
	 * @throws SchedulerException
	 * @throws ParseException
	 */
	public static void addCronJob(String jobName,Class jobClass,Map dataMap,String expression){
		try{
			Scheduler sched = getScheduler();
			JobDetail jobDetail = new JobDetail(jobName, JOB_GROUP_NAME, jobClass);// �������������飬����ִ����
			JobDataMap jobData = jobDetail.getJobDataMap();
			for(Iterator<Entry> itor = dataMap.entrySet().iterator(); itor.hasNext();){
				Entry entry = itor.next();
				jobData.put(entry.getKey(), entry.getValue());
			}
			// ������
			CronTrigger trigger = new CronTrigger(jobName, TRIGGER_GROUP_NAME);// ��������,��������
			trigger.setCronExpression(expression);// ������ʱ���趨
			sched.scheduleJob(jobDetail, trigger);
			// ����
			if(!sched.isShutdown()){
				sched.start();
			}
		}catch (Exception e){
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * �������� �ж�������������ʱ�� �ô�����ֻ����ִ��һ��
	 * 
	 * @param jobDetail
	 * @param trigger
	 */
	public static void addSimpleJob(String jobName,String groupName,Map dataMap,Date execTime,Class jobClass){
		try{
			Scheduler sched = getScheduler();
			JobDetail job = sched.getJobDetail(jobName, groupName);
			if(job != null){// ����,����ִ��ʱ��
				SimpleTrigger trigger = (SimpleTrigger) sched.getTrigger(jobName, groupName);
				if(trigger == null){
					return;
				}
				Date oldTime = trigger.getStartTime();
				if(!oldTime.equals(execTime)){
					sched.pauseTrigger(jobName, groupName);// ֹͣ������
					sched.unscheduleJob(jobName, groupName);// �Ƴ�������
					sched.deleteJob(jobName, groupName);// ɾ������
					addJob(jobName, groupName, dataMap, execTime, jobClass);
				}
			}else{// ������,����
				addJob(jobName, groupName, dataMap, execTime, jobClass);
			}
		}catch (Exception e){
			LogUtil.saveException(logger, e);
		}
	}

	private static void addJob(String jobName,String groupName,Map dataMap,Date execTime,Class jobClass){
		try{
			Scheduler sched = getScheduler();
			JobDetail jobDetail = new JobDetail();
			jobDetail.setJobClass(jobClass);
			jobDetail.setName(jobName);
			jobDetail.setGroup(groupName);
			JobDataMap jobData = jobDetail.getJobDataMap();
			for(Iterator<Entry> itor = dataMap.entrySet().iterator(); itor.hasNext();){
				Entry entry = itor.next();
				jobData.put(entry.getKey(), entry.getValue());
			}
			SimpleTrigger trigger = new SimpleTrigger(jobName, groupName, execTime, null, 0, 0);
			sched.scheduleJob(jobDetail, trigger);
			// ����
			if(!sched.isShutdown()){
				sched.start();
			}
		}catch (Exception e){
			LogUtil.saveException(logger, e);
		}
	}

	/**
	 * ���һ����ʱ����
	 * 
	 * @param jobName
	 *            ������
	 * @param jobGroupName
	 *            ��������
	 * @param triggerName
	 *            ��������
	 * @param triggerGroupName
	 *            ����������
	 * @param jobClass
	 *            ����
	 * @param time
	 *            ʱ�����ã��ο�quartz˵���ĵ�
	 * @throws SchedulerException
	 * @throws ParseException
	 */
	public static void addCronJob(String jobName,String jobGroupName,String triggerName,String triggerGroupName,
			Job job,String expression){
		try{
			Scheduler sched = getScheduler();
			JobDetail jobDetail = new JobDetail(jobName, jobGroupName, job.getClass());// �������������飬����ִ����
			// ������
			CronTrigger trigger = new CronTrigger(triggerName, triggerGroupName);// ��������,��������
			trigger.setCronExpression(expression);// ������ʱ���趨
			sched.scheduleJob(jobDetail, trigger);
		}catch (Exception e){
			LogUtil.saveException(logger, e);
		}
	}

	/**
	 * �޸�һ������Ĵ���ʱ��(ʹ��Ĭ�ϵ�������������������������������)
	 * 
	 * @param jobName
	 * @param time
	 */
	public static void modifyCronJobTime(String jobName,String expression){
		try{
			Scheduler sched = getScheduler();
			CronTrigger trigger = (CronTrigger) sched.getTrigger(jobName, TRIGGER_GROUP_NAME);
			if(trigger == null){
				return;
			}
			String oldTime = trigger.getCronExpression();
			if(!oldTime.equalsIgnoreCase(expression)){
				// �޸�ʱ��
				trigger.setCronExpression(expression);
				// ����������
				sched.resumeTrigger(jobName, TRIGGER_GROUP_NAME);
			}
		}catch (Exception e){
			LogUtil.saveException(logger, e);
		}
	}

	/**
	 * �޸�һ������Ĵ���ʱ��
	 * 
	 * @param triggerName
	 * @param triggerGroupName
	 * @param time
	 */
	public static void modifyCronJobTime(String triggerName,String triggerGroupName,String expression){
		try{
			Scheduler sched = getScheduler();
			CronTrigger trigger = (CronTrigger) sched.getTrigger(triggerName, triggerGroupName);
			if(trigger == null){
				return;
			}
			String oldTime = trigger.getCronExpression();
			if(!oldTime.equalsIgnoreCase(expression)){
				CronTrigger ct = trigger;
				// �޸�ʱ��
				ct.setCronExpression(expression);
				// ����������
				sched.resumeTrigger(triggerName, triggerGroupName);
			}
		}catch (Exception e){
			LogUtil.saveException(logger, e);
		}
	}

	/**
	 * �Ƴ�һ������(ʹ��Ĭ�ϵ�������������������������������)
	 * 
	 * @param jobName
	 */
	public static void removeJob(String jobName){
		try{
			Scheduler sched = getScheduler();
			sched.pauseTrigger(jobName, TRIGGER_GROUP_NAME);// ֹͣ������
			sched.unscheduleJob(jobName, TRIGGER_GROUP_NAME);// �Ƴ�������
			sched.deleteJob(jobName, JOB_GROUP_NAME);// ɾ������
		}catch (Exception e){
			LogUtil.saveException(logger, e);
		}
	}

	/**
	 * �Ƴ�һ������
	 * 
	 * @param jobName
	 * @param jobGroupName
	 * @param triggerName
	 * @param triggerGroupName
	 */
	public static void removeJob(String jobName,String jobGroupName){
		try{
			Scheduler sched = getScheduler();
			sched.pauseTrigger(jobName, jobGroupName);// ֹͣ������
			sched.unscheduleJob(jobName, jobGroupName);// �Ƴ�������
			sched.deleteJob(jobName, jobGroupName);// ɾ������
		}catch (Exception e){
			LogUtil.saveException(logger, e);
		}
	}

	/**
	 * �������ж�ʱ����
	 */
	public static void startJobs(){
		try{
			Scheduler sched = getScheduler();
			sched.start();
		}catch (Exception e){
			LogUtil.saveException(logger, e);
		}
	}

	/**
	 * �ر����ж�ʱ����
	 */
	public static void shutdownJobs(){
		try{
			Scheduler sched = getScheduler();
			if(!sched.isShutdown()){
				sched.shutdown();
			}
		}catch (Exception e){
			LogUtil.saveException(logger, e);
		}
	}
}