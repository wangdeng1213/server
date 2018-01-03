package com.linksus.task;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linksus.common.config.LoadConfig;
import com.linksus.common.config.TaskConfig;
import com.linksus.common.util.LogUtil;
import com.linksus.common.util.QuartzUtil;
import com.linksus.data.InteractCountSave;
import com.linksus.data.QueueDataSave;

/**
 * ����������Զ��������
 */
public class ServerTaskControl implements ServletContextListener{

	protected static final Logger logger = LoggerFactory.getLogger(ServerTaskControl.class);
	private static final String CORN_TASK_PREFIX = "cornTask_";
	private static Map serverTasksMap = new HashMap();
	private static Map cornTasksMap = new HashMap();
	private static Map taskThreadsMap = new HashMap();

	public void contextDestroyed(ServletContextEvent arg0){
	}

	/**
	 * ʵ�������������������
	 * @param config
	 */
	public static void initServerTask(TaskConfig config){
		try{
			if("1".equals(config.getStatus())){
				String taskType = config.getTaskType();
				String cronExpression = config.getCronExpression();
				if(!StringUtils.isBlank(cronExpression)){//��ʱ���� 
					Map map = new HashMap();
					map.put("taskType", taskType);
					QuartzUtil.addCronJob(CORN_TASK_PREFIX + taskType, CronJob.class, map, cronExpression);
					logger.info("=================={}��ʱ��������================", config.getTaskName());
					cornTasksMap.put(taskType, CORN_TASK_PREFIX + taskType);
					CronJob.setCornTaskStatistic(taskType);
				}else{
					String pkg = BaseTask.class.getPackage().getName();
					String claName = pkg + "." + config.getClassName();
					Class taskClass = Class.forName(claName);
					// ��ʼ�����캯��
					Constructor cc = taskClass.getDeclaredConstructor();
					BaseTask task = (BaseTask) cc.newInstance();
					task.setTaskType(taskType);
					// ��ʼ������
					flushTaskField(task, config);
					logger.info("=================={}��������================", config.getTaskName());
					serverTasksMap.put(taskType, task);
					Thread thread = new Thread(task);
					thread.start();
					taskThreadsMap.put(taskType, thread);
				}
			}
		}catch (Exception e){
			logger.error("=================={}��������ʧ��,��鿴================", config.getTaskName());
			LogUtil.saveException(logger, e);
			e.printStackTrace();
		}
	}

	/**
	 * ����������Ը�ֵ
	 * @param task
	 * @param config
	 * @throws Exception
	 */
	private static void flushTaskField(BaseTask task,TaskConfig config) throws Exception{
		if(!StringUtils.isBlank(config.getPageSize())){
			task.setPageSize(Integer.parseInt(config.getPageSize()));
		}
		if(!StringUtils.isBlank(config.getTaskInterval())){
			task.setTaskInterval(Long.parseLong(config.getTaskInterval()));
		}
		Map paramsMap = config.getParamsMap();
		for(Iterator params = paramsMap.entrySet().iterator(); params.hasNext();){
			Entry param = (Entry) params.next();
			String fieldName = (String) param.getKey();
			String fieldValue = (String) param.getValue();
			// ���丳ֵ
			String setMethodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
			Method setFeildMethod = task.getClass().getMethod(setMethodName, String.class);
			setFeildMethod.invoke(task, fieldValue);
		}
	}

	/**
	 * ˢ�·����������:�����ļ�����ʱʹ��
	 * @param config
	 * @throws Exception 
	 */
	public static void flushServerTask(TaskConfig config) throws Exception{
		String taskType = config.getTaskType();
		if(serverTasksMap.containsKey(taskType) || cornTasksMap.containsKey(taskType)){
			if(!"1".equals(config.getStatus())){//��Ч����Ч
				removeServerTask(config);
			}else{//��������
				if(serverTasksMap.containsKey(taskType)){
					flushTaskField((BaseTask) serverTasksMap.get(taskType), config);
				}else{
					Map map = new HashMap();
					map.put("taskType", taskType);
					QuartzUtil.removeJob(CORN_TASK_PREFIX + taskType);
					QuartzUtil.addCronJob(CORN_TASK_PREFIX + taskType, CronJob.class, map, config.getCronExpression());
					logger.info("=================={}��ʱ������������================", config.getTaskName());
				}
			}
		}else{
			if("1".equals(config.getStatus())){//��Ч����Ч
				initServerTask(config);
			}
		}
	}

	/**
	 * �Ƴ�һ���Ѿ����õķ��������
	 * @param config
	 */
	public static void removeServerTask(TaskConfig config){
		String taskType = config.getTaskType();
		if(cornTasksMap.containsKey(taskType)){//��ʱ����
			QuartzUtil.removeJob(CORN_TASK_PREFIX + taskType);//ɾ������
			cornTasksMap.remove(taskType);
		}else{
			logger.info("==================��ʼ��ֹ�����߳�{}================", taskType);
			BaseTask task = (BaseTask) serverTasksMap.get(taskType);
			task.setExitFlag(true);
			Thread thread = (Thread) taskThreadsMap.get(taskType);
			if(thread != null){
				thread.interrupt();
			}
		}
	}

	/**
	 * ������������� 
	 * �������
	 */
	public void contextInitialized(ServletContextEvent arg0){
		logger.info("==================����������������================");
		Thread thread = new Thread(new QueueDataSave());
		thread.start();
		if("1".equals(LoadConfig.getString("taskServer"))){
			logger.info("==================������ϵ����������������================");
			Thread thread1 = new Thread(new InteractCountSave());
			thread1.start();
		}
		logger.info("==================�������������================");
		Map tasksMap = LoadConfig.getTasksMap();
		for(Iterator itor = tasksMap.entrySet().iterator(); itor.hasNext();){
			Entry entry = (Entry) itor.next();
			TaskConfig config = (TaskConfig) entry.getValue();
			initServerTask(config);
		}
	}

	/**
	 * �����߳̽���,������
	 * @param taskType
	 */
	public static void removeTaskThread(String taskType){
		Thread thread = (Thread) taskThreadsMap.get(taskType);
		if(thread != null){
			taskThreadsMap.remove(taskType);
			serverTasksMap.remove(taskType);
			logger.info("==================�����߳�{}��ֹ================", taskType);
		}
	}
}
