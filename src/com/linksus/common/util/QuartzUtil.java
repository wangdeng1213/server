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
 * 定时任务管理类
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
	 * 添加一个定时任务，使用默认的任务组名，触发器名，触发器组名
	 * 
	 * @param jobName
	 *            任务名
	 * @param jobClass
	 *            任务
	 * @param time
	 *            时间设置，参考quartz说明文档
	 * @throws SchedulerException
	 * @throws ParseException
	 */
	public static void addCronJob(String jobName,Class jobClass,Map dataMap,String expression){
		try{
			Scheduler sched = getScheduler();
			JobDetail jobDetail = new JobDetail(jobName, JOB_GROUP_NAME, jobClass);// 任务名，任务组，任务执行类
			JobDataMap jobData = jobDetail.getJobDataMap();
			for(Iterator<Entry> itor = dataMap.entrySet().iterator(); itor.hasNext();){
				Entry entry = itor.next();
				jobData.put(entry.getKey(), entry.getValue());
			}
			// 触发器
			CronTrigger trigger = new CronTrigger(jobName, TRIGGER_GROUP_NAME);// 触发器名,触发器组
			trigger.setCronExpression(expression);// 触发器时间设定
			sched.scheduleJob(jobDetail, trigger);
			// 启动
			if(!sched.isShutdown()){
				sched.start();
			}
		}catch (Exception e){
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * 加入任务 判断任务存在则更新时间 该触发器只设置执行一次
	 * 
	 * @param jobDetail
	 * @param trigger
	 */
	public static void addSimpleJob(String jobName,String groupName,Map dataMap,Date execTime,Class jobClass){
		try{
			Scheduler sched = getScheduler();
			JobDetail job = sched.getJobDetail(jobName, groupName);
			if(job != null){// 存在,更新执行时间
				SimpleTrigger trigger = (SimpleTrigger) sched.getTrigger(jobName, groupName);
				if(trigger == null){
					return;
				}
				Date oldTime = trigger.getStartTime();
				if(!oldTime.equals(execTime)){
					sched.pauseTrigger(jobName, groupName);// 停止触发器
					sched.unscheduleJob(jobName, groupName);// 移除触发器
					sched.deleteJob(jobName, groupName);// 删除任务
					addJob(jobName, groupName, dataMap, execTime, jobClass);
				}
			}else{// 不存在,新增
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
			// 启动
			if(!sched.isShutdown()){
				sched.start();
			}
		}catch (Exception e){
			LogUtil.saveException(logger, e);
		}
	}

	/**
	 * 添加一个定时任务
	 * 
	 * @param jobName
	 *            任务名
	 * @param jobGroupName
	 *            任务组名
	 * @param triggerName
	 *            触发器名
	 * @param triggerGroupName
	 *            触发器组名
	 * @param jobClass
	 *            任务
	 * @param time
	 *            时间设置，参考quartz说明文档
	 * @throws SchedulerException
	 * @throws ParseException
	 */
	public static void addCronJob(String jobName,String jobGroupName,String triggerName,String triggerGroupName,
			Job job,String expression){
		try{
			Scheduler sched = getScheduler();
			JobDetail jobDetail = new JobDetail(jobName, jobGroupName, job.getClass());// 任务名，任务组，任务执行类
			// 触发器
			CronTrigger trigger = new CronTrigger(triggerName, triggerGroupName);// 触发器名,触发器组
			trigger.setCronExpression(expression);// 触发器时间设定
			sched.scheduleJob(jobDetail, trigger);
		}catch (Exception e){
			LogUtil.saveException(logger, e);
		}
	}

	/**
	 * 修改一个任务的触发时间(使用默认的任务组名，触发器名，触发器组名)
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
				// 修改时间
				trigger.setCronExpression(expression);
				// 重启触发器
				sched.resumeTrigger(jobName, TRIGGER_GROUP_NAME);
			}
		}catch (Exception e){
			LogUtil.saveException(logger, e);
		}
	}

	/**
	 * 修改一个任务的触发时间
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
				// 修改时间
				ct.setCronExpression(expression);
				// 重启触发器
				sched.resumeTrigger(triggerName, triggerGroupName);
			}
		}catch (Exception e){
			LogUtil.saveException(logger, e);
		}
	}

	/**
	 * 移除一个任务(使用默认的任务组名，触发器名，触发器组名)
	 * 
	 * @param jobName
	 */
	public static void removeJob(String jobName){
		try{
			Scheduler sched = getScheduler();
			sched.pauseTrigger(jobName, TRIGGER_GROUP_NAME);// 停止触发器
			sched.unscheduleJob(jobName, TRIGGER_GROUP_NAME);// 移除触发器
			sched.deleteJob(jobName, JOB_GROUP_NAME);// 删除任务
		}catch (Exception e){
			LogUtil.saveException(logger, e);
		}
	}

	/**
	 * 移除一个任务
	 * 
	 * @param jobName
	 * @param jobGroupName
	 * @param triggerName
	 * @param triggerGroupName
	 */
	public static void removeJob(String jobName,String jobGroupName){
		try{
			Scheduler sched = getScheduler();
			sched.pauseTrigger(jobName, jobGroupName);// 停止触发器
			sched.unscheduleJob(jobName, jobGroupName);// 移除触发器
			sched.deleteJob(jobName, jobGroupName);// 删除任务
		}catch (Exception e){
			LogUtil.saveException(logger, e);
		}
	}

	/**
	 * 启动所有定时任务
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
	 * 关闭所有定时任务
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