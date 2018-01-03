package com.linksus.task;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linksus.common.Constants;
import com.linksus.common.config.LoadConfig;
import com.linksus.common.config.TaskConfig;
import com.linksus.common.util.LogUtil;
import com.linksus.entity.TaskStatistic;

/**
 * 定时任务入口
 * @author zhangew
 *
 */
public class CronJob implements Job{

	protected static final Logger logger = LoggerFactory.getLogger(CronJob.class);
	/** 任务执行统计MAP */
	private static Map taskStatisticMap = new HashMap();

	public void execute(JobExecutionContext arg0) throws JobExecutionException{
		JobDataMap map = arg0.getJobDetail().getJobDataMap();// 取参数
		String taskType = map.getString("taskType");
		TaskStatistic taskStatistic = (TaskStatistic) taskStatisticMap.get(taskType);
		taskStatistic.setTaskCount(taskStatistic.getTaskCount() + 1);//执行次数
		taskStatistic.setTaskStatus(Constants.TASK_STATUS_EXEC);//执行状态
		try{
			Map tasksMap = LoadConfig.getTasksMap();
			TaskConfig config = (TaskConfig) tasksMap.get(taskType);
			logger.info("=================={}定时任务开始执行================", config.getTaskName());
			String pkg = BaseTask.class.getPackage().getName();
			String claName = pkg + "." + config.getClassName();
			Class taskClass = Class.forName(claName);
			// 初始化构造函数
			Constructor cc = taskClass.getDeclaredConstructor();
			Object task = cc.newInstance();
			// 初始化参数
			Map paramsMap = config.getParamsMap();
			for(Iterator params = paramsMap.entrySet().iterator(); params.hasNext();){
				Entry param = (Entry) params.next();
				String fieldName = (String) param.getKey();
				String fieldValue = (String) param.getValue();
				// 反射赋值
				String setMethodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
				Method setFeildMethod = taskClass.getMethod(setMethodName, String.class);
				setFeildMethod.invoke(task, fieldValue);
			}
			String runMethodName = config.getMethodName();
			Method runMethod = taskClass.getMethod(runMethodName);
			runMethod.invoke(task);
			logger.info("=================={}定时任务执行完成================", config.getTaskName());
			taskStatistic.setTaskStatus(Constants.TASK_STATUS_FINISH);//执行状态
		}catch (Exception e){
			LogUtil.saveException(logger, e);
			e.printStackTrace();
		}
	}

	public static void setCornTaskStatistic(String taskType){
		TaskStatistic taskStatistic = (TaskStatistic) taskStatisticMap.get(taskType);
		if(taskStatistic == null){
			taskStatistic = new TaskStatistic();
			taskStatisticMap.put(taskType, taskStatistic);
		}
		taskStatistic.setTaskStatus(Constants.TASK_STATUS_USE);//执行状态
	}

	public static Map getTaskStatisticMap(){
		return taskStatisticMap;
	}

	public static void resetTaskStatisticMap(){
		taskStatisticMap.clear();
	}
}