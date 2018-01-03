package com.linksus.common.module;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linksus.common.Constants;
import com.linksus.common.config.InterfaceConfig;
import com.linksus.common.config.LoadConfig;
import com.linksus.common.config.QueueConfig;
import com.linksus.common.config.TaskConfig;
import com.linksus.common.util.CacheUtil;
import com.linksus.common.util.DateUtil;
import com.linksus.entity.InterfaceStatistic;
import com.linksus.entity.QueueStatistic;
import com.linksus.entity.TaskStatistic;
import com.linksus.entity.UrlEntity;
import com.linksus.interfaces.BaseInterface;
import com.linksus.queue.BaseQueue;
import com.linksus.task.BaseTask;
import com.linksus.task.CronJob;

public class TaskStatisticCommon{

	protected static final Logger logger = LoggerFactory.getLogger(TaskStatisticCommon.class);
	private static CacheUtil cache = CacheUtil.getInstance();
	private static Map urlEntityMap = new LinkedHashMap();
	private static Map currClientCount = new HashMap();
	private static Map clientLimitTime = new HashMap();

	public static String taskStatistic() throws Exception{
		VelocityContext context = new VelocityContext();
		//队列刷新统计
		Map queuesMap = LoadConfig.getQueuesMap();
		Map queueMap = BaseQueue.getTaskStatisticMap();
		List queueList = new ArrayList();
		for(Iterator itor = queueMap.entrySet().iterator(); itor.hasNext();){
			Entry entry = (Entry) itor.next();
			String taskType = (String) entry.getKey();
			QueueConfig config = (QueueConfig) queuesMap.get(taskType);
			QueueStatistic queueStatistic = (QueueStatistic) entry.getValue();
			queueStatistic.setTaskName(config.getTaskName());
			queueStatistic.setErrorCount(0l);
			queueStatistic.setNoFlushFlag(0);
			if(config.getTaskInterval() != null){
				Long taskInterval = Long.parseLong(config.getTaskInterval());
				queueStatistic.setTaskInterval(taskInterval / 1000);
			}
			if("1".equals(config.getStatus())){
				queueStatistic.setTaskStatus(Constants.TASK_STATUS_USE);
			}else if("0".equals(config.getStatus())){
				queueStatistic.setTaskStatus(Constants.TASK_STATUS_STOP);
			}else{
				queueStatistic.setTaskStatus(Constants.TASK_STATUS_INVALID);
			}
			queueList.add(queueStatistic);
		}
		for(Iterator itor = queuesMap.entrySet().iterator(); itor.hasNext();){
			Entry entry = (Entry) itor.next();
			String taskType = (String) entry.getKey();
			QueueConfig config = (QueueConfig) entry.getValue();
			if(!queueMap.containsKey(taskType) && "1".equals(config.getStatus())){
				QueueStatistic queueStatistic = new QueueStatistic();
				queueStatistic.setTaskName(config.getTaskName());
				queueStatistic.setTaskType(config.getTaskType());
				//queueStatistic.setErrorCount(0l);
				if(config.getTaskInterval() != null){
					Long taskInterval = Long.parseLong(config.getTaskInterval());
					queueStatistic.setTaskInterval(taskInterval / 1000);
				}
				queueStatistic.setTaskStatus(Constants.TASK_STATUS_USE);
				queueStatistic.setNoFlushFlag(1);
				queueList.add(queueStatistic);
			}
		}
		context.put("queueList", queueList);//队列刷新统计
		//服务端任务执行统计
		List taskList = new ArrayList();
		Map taskCofigMap = LoadConfig.getTasksMap();
		Map taskMap = BaseTask.getTaskStatisticMap();
		for(Iterator itor = taskMap.entrySet().iterator(); itor.hasNext();){
			Entry entry = (Entry) itor.next();
			String taskType = (String) entry.getKey();
			TaskConfig config = (TaskConfig) taskCofigMap.get(taskType);
			TaskStatistic taskStatistic = (TaskStatistic) entry.getValue();
			taskStatistic.setErrorCount(0l);
			taskStatistic.setTaskType(taskType);
			taskStatistic.setTaskName(config.getTaskName());
			long interval = Long.parseLong(config.getTaskInterval());
			taskStatistic.setTaskInterval((interval / 1000) + "");
			if("0".equals(config.getStatus())){
				taskStatistic.setTaskStatus(Constants.TASK_STATUS_STOP);
			}
			taskList.add(taskStatistic);
		}
		//定时任务统计
		Map cronMap = CronJob.getTaskStatisticMap();
		for(Iterator itor = cronMap.entrySet().iterator(); itor.hasNext();){
			Entry entry = (Entry) itor.next();
			String taskType = (String) entry.getKey();
			TaskConfig config = (TaskConfig) taskCofigMap.get(taskType);
			TaskStatistic taskStatistic = (TaskStatistic) entry.getValue();
			taskStatistic.setErrorCount(0l);
			taskStatistic.setTaskType(taskType);
			taskStatistic.setTaskName(config.getTaskName());
			taskStatistic.setTaskInterval(config.getCronExpression());
			if("0".equals(config.getStatus())){
				taskStatistic.setTaskStatus(Constants.TASK_STATUS_STOP);
			}
			taskList.add(taskStatistic);
		}
		context.put("taskList", taskList);//服务端任务执行统计
		//接口调用统计
		List interfaceList = new ArrayList();
		Map interCofigMap = LoadConfig.getInterfaceMap();
		Map interfaceMap = BaseInterface.getTaskStatisticMap();
		for(Iterator itor = interfaceMap.entrySet().iterator(); itor.hasNext();){
			Entry entry = (Entry) itor.next();
			String taskType = (String) entry.getKey();
			InterfaceConfig config = (InterfaceConfig) interCofigMap.get(taskType);
			InterfaceStatistic interfaceStatistic = (InterfaceStatistic) entry.getValue();
			interfaceStatistic.setTaskName(config.getTaskName());
			if("1".equals(config.getStatus())){
				interfaceStatistic.setTaskStatus(Constants.TASK_STATUS_USE);
			}else if("0".equals(config.getStatus())){
				interfaceStatistic.setTaskStatus(Constants.TASK_STATUS_STOP);
			}else{
				interfaceStatistic.setTaskStatus(Constants.TASK_STATUS_INVALID);
			}
			interfaceList.add(interfaceStatistic);
		}
		for(Iterator itor = interCofigMap.entrySet().iterator(); itor.hasNext();){
			Entry entry = (Entry) itor.next();
			String taskType = (String) entry.getKey();
			InterfaceConfig config = (InterfaceConfig) entry.getValue();
			if(!interfaceMap.containsKey(taskType) && "1".equals(config.getStatus())){
				InterfaceStatistic interfaceStatistic = new InterfaceStatistic();
				interfaceStatistic.setTaskType(taskType);
				interfaceStatistic.setTaskName(config.getTaskName());
				interfaceStatistic.setTaskSize(0);
				interfaceStatistic.setTaskStatus(Constants.TASK_STATUS_USE);
				interfaceStatistic.setErrorCount(0);
				interfaceStatistic.setErrorCodeCount(0);
				interfaceList.add(interfaceStatistic);
			}
		}
		context.put("interfaceList", interfaceList);//接口调用统计
		//API调用统计
		List titleList = new ArrayList();
		titleList.add(Constants.LOCAL_IP);
		if(urlEntityMap.size() > 0){
			Set set = urlEntityMap.keySet();
			titleList.addAll(set);
		}
		List urlsList = new ArrayList();
		Map urlsMap = LoadConfig.getUrlsMap();
		for(Iterator itor = urlsMap.entrySet().iterator(); itor.hasNext();){
			List tempList = new ArrayList();
			Entry entry = (Entry) itor.next();
			String urlName = (String) entry.getKey();
			UrlEntity urlEntity = (UrlEntity) entry.getValue();
			tempList.add(urlEntity);
			for(Iterator ir = urlEntityMap.entrySet().iterator(); ir.hasNext();){
				Entry e = (Entry) ir.next();
				Map map = (Map) e.getValue();
				UrlEntity entity = (UrlEntity) map.get(urlName);
				tempList.add(entity);
			}
			urlsList.add(tempList);
		}
		context.put("urlsList", urlsList);//API使用统计
		context.put("titleList", titleList);//API使用统计表头
		//24小时API受限时间 (未受限)调用总次数
		List hours = new ArrayList();
		String currDay = DateUtil.getCurrDate("yyyyMMdd");
		for(int i = 0; i < 24; i++){
			String hour = (i < 10) ? "0" + i : i + "";
			hours.add(hour);
		}
		List clientsList = new ArrayList();
		for(Iterator itor = clientLimitTime.entrySet().iterator(); itor.hasNext();){
			List tempList = new ArrayList();
			Entry entry = (Entry) itor.next();
			Map hourMap = (Map) entry.getValue();
			tempList.add(entry.getKey());
			for(int i = 0; i < hours.size(); i++){
				String hour = (String) hours.get(i);
				if(hourMap.containsKey(currDay + hour)){
					tempList.add(hourMap.get(currDay + hour));
				}else{
					tempList.add("");
				}
			}
			clientsList.add(tempList);
		}
		context.put("hours", hours);//表头
		context.put("clientsList", clientsList);//客户端
		//生成
		String statisticModel = (String) cache.getCache(Constants.CACHE_TASK_STATISTIC_MODEL);
		VelocityEngine ve = new VelocityEngine();
		ve.init();
		StringWriter writer = new StringWriter();
		context.put("formIP", cache.getCache(Constants.CACHE_LOCAL_IP_ADDR));//来自IP
		ve.evaluate(context, writer, "", statisticModel);
		return writer.toString();
	}

	public static void saveClientStatisticData(Map dataMap,String ipAddr){

		//String sinaLimit=JsonUtil.getNodeByName(data, Constants.LIMIT_SINA_TOTAL_PER_HOUR);
		//String tencentLimit=JsonUtil.getNodeByName(data, Constants.LIMIT_TENCENT_READ_PER_HOUR);
		//String urls=JsonUtil.getNodeByName(data, "urlsList");
		List list = (List) dataMap.get("urlsList");
		Map map = new HashMap();
		for(int i = 0; i < list.size(); i++){
			UrlEntity entity = (UrlEntity) list.get(i);
			map.put(entity.getName(), entity);
		}
		urlEntityMap.put("[" + ipAddr + "]", map);
	}

	public static void saveClientLimitTime(Map dataMap,String ipAddr){
		Map map = (Map) clientLimitTime.get(ipAddr);
		if(map == null){
			map = new HashMap();
			clientLimitTime.put(ipAddr, map);
		}
		map.put(dataMap.get("currHour"), dataMap.get("currTime"));
	}

	public static void main(String[] args){
		Map queuesMap = LoadConfig.getQueuesMap();
		Map taskCofigMap = LoadConfig.getTasksMap();
		Map interCofigMap = LoadConfig.getInterfaceMap();
		logger.info("-------------------------队列---------------------------");
		for(Iterator itor = queuesMap.entrySet().iterator(); itor.hasNext();){
			Entry entry = (Entry) itor.next();
			String taskType = (String) entry.getKey();
			QueueConfig config = (QueueConfig) entry.getValue();
			logger.info(taskType + "====================={}==={}-----{}", config.getTaskName(), config.getStatus(),
					config.getClassName());
		}
		logger.info("-------------------------服务端任务---------------------------");
		for(Iterator itor = taskCofigMap.entrySet().iterator(); itor.hasNext();){
			Entry entry = (Entry) itor.next();
			String taskType = (String) entry.getKey();
			TaskConfig config = (TaskConfig) entry.getValue();
			logger.info(taskType + "====================={}==={}-----{}", config.getTaskName(), config.getStatus(),
					config.getClassName());
		}
		logger.info("-------------------------接口---------------------------");
		for(Iterator itor = interCofigMap.entrySet().iterator(); itor.hasNext();){
			Entry entry = (Entry) itor.next();
			String taskType = (String) entry.getKey();
			InterfaceConfig config = (InterfaceConfig) entry.getValue();
			logger.info(taskType + "====================={}==={}-----{}", config.getTaskName(), config.getStatus(),
					config.getClassName());
		}
	}
}
