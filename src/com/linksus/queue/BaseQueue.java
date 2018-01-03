package com.linksus.queue;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.jcs.access.exception.CacheException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linksus.common.Constants;
import com.linksus.common.util.CacheUtil;
import com.linksus.common.util.DateUtil;
import com.linksus.common.util.LogUtil;
import com.linksus.entity.LinksusAppaccount;
import com.linksus.entity.QueueStatistic;

public abstract class BaseQueue{

	protected static final Logger logger = LoggerFactory.getLogger(BaseQueue.class);
	//公共属性
	/** 队列刷新间隔 */
	private long queueInterval = 0;
	/** 任务刷新间隔 */
	private long taskInterval = 0;
	/** 每页任务数 */
	protected int pageSize = 500;
	/** 客户端每次请求任务数 */
	private int assignCount = 100;
	/** 每小时分配任务数 */
	private long taskCountPerHour = 0;
	/** 每天分配任务数 */
	private long taskCountPerDay = 0;
	//构造属性
	/** 任务类型 */
	public String taskType;

	/** 当前小时/天已分配任务数*/
	private Map taskCountMap = new HashMap();
	/** 任务队列 */
	private Queue taskQueue = new ConcurrentLinkedQueue();
	/** 任务队列统计MAP */
	private static Map taskStatisticMap = new HashMap();
	/** 队列上次刷新时间 */
	private Long queueLastFlushTime;
	/** 任务上次刷新时间(开始) */
	private Long taskLastFlushTime;
	/** 缓存对象 */
	protected CacheUtil cache = CacheUtil.getInstance();
	/** 当前分页查询页数 */
	protected int currentPage = 1;

	protected BaseQueue(String taskType) {
		this.taskType = taskType;
	}

	public void setTaskCountPerHour(long taskCountPerHour){
		this.taskCountPerHour = taskCountPerHour;
	}

	public void setTaskCountPerDay(long taskCountPerDay){
		this.taskCountPerDay = taskCountPerDay;
	}

	public void setQueueInterval(long queueInterval){
		this.queueInterval = queueInterval;
	}

	public void setTaskInterval(long taskInterval){
		this.taskInterval = taskInterval;
	}

	public void setPageSize(int pageSize){
		this.pageSize = pageSize;
	}

	public void setAssignCount(int assignCount){
		this.assignCount = assignCount;
	}

	public String getTaskType(){
		return taskType;
	}

	public Queue getTaskQueue(){
		return taskQueue;
	}

	/** 从队列取得任务列表 */
	public final Map getTaskListFromQueue(){
		if(checkTaskCount()){
			if(taskQueue.isEmpty()){
				flushQueue();
			}
		}else{
			return dealTaskData(null);
		}
		List rsList = new ArrayList();
		int taskCount = 0;
		while (true){
			if(!assignTask(rsList, taskCount)){
				break;
			}
			taskCount++;
		}
		logger.info("客户端取得任务{},任务数：{}", taskType, rsList.size());
		return dealTaskData(rsList);
	}

	/**
	 * 任务分配
	 */
	private synchronized boolean assignTask(List dataList,int taskCount){
		Object obj = null;
		if((obj = taskQueue.poll()) != null){
			taskCount++;
			dataList.add(obj);
			//分配任务数计数
			if(taskCountPerDay > 0){
				Long icount = (Long) taskCountMap.get("dayCount");
				taskCountMap.put("dayCount", new Long(icount.intValue() + 1));
			}
			if(taskCountPerHour > 0){
				Long icount = (Long) taskCountMap.get("hourCount");
				taskCountMap.put("hourCount", new Long(icount.intValue() + 1));
			}
			if(taskCount >= assignCount){
				return false;
			}
		}else{
			return false;
		}
		return true;
	}

	/**
	 * 检查当前小时/当天任务分配数是否达到上限  true 未达到上限 false 已达上限
	 * @return
	 */
	private boolean checkTaskCount(){
		if(taskCountPerHour > 0 || taskCountPerDay > 0){
			String currHour = DateUtil.getCurrDate("yyyyMMddHH");
			String currDay = DateUtil.getCurrDate("yyyyMMdd");
			try{
				if(taskCountMap.size() == 0){
					if(taskCountPerHour > 0){
						taskCountMap.put("currHour", currHour);
						taskCountMap.put("hourCount", new Long(0));
					}
					if(taskCountPerDay > 0){
						taskCountMap.put("currDay", currDay);
						taskCountMap.put("dayCount", new Long(0));
					}
				}else{
					//先检查当天任务数
					if(taskCountPerDay > 0){
						String tmpDay = (String) taskCountMap.get("currDay");
						if(currDay.equals(tmpDay)){
							Long icount = (Long) taskCountMap.get("dayCount");
							if(icount.longValue() >= taskCountPerDay){//超出当天最大限制
								logger.info("达到当天任务数上限,不再分配任务,taskCountPerDay={}", taskCountPerDay);
								return false;
							}
						}else{
							taskCountMap.put("currDay", currDay);
							taskCountMap.put("dayCount", new Long(0));
						}
					}
					//检查当前小时任务数
					if(taskCountPerHour > 0){
						String tmpHour = (String) taskCountMap.get("currHour");
						if(currHour.equals(tmpHour)){
							Long icount = (Long) taskCountMap.get("hourCount");
							if(icount.longValue() >= taskCountPerHour){//超出小时最大限制
								logger.info("达到当前小时任务数上限,不再分配任务,taskCountPerHour={}", taskCountPerHour);
								return false;
							}
						}else{
							taskCountMap.put("currHour", currHour);
							taskCountMap.put("hourCount", new Long(0));
						}
					}
				}
			}catch (Exception e){
				LogUtil.saveException(logger, e);
				return false;
			}
		}
		return true;
	}

	private synchronized final void flushQueue(){
		if(!taskQueue.isEmpty()){//已经被刷新
			return;
		}
		//1 判断刷新时间间隔
		boolean flag = checkFlushIntervalTime();
		if(!flag){//刷新频率太快
			return;
		}
		//2 修改任务刷新时间
		setFlushIntervalTime();
		//3 加载任务数据
		List dataList = flushTaskQueue();
		if(dataList != null){
			for(int i = 0; i < dataList.size(); i++){
				taskQueue.offer(dataList.get(i));//将任务放入队列
			}
		}
		//3 修改统计数据
		updateTaskStatisticData(dataList);
	}

	/**
	 * 更新队列刷新数据
	 * @param dataList
	 */
	private void updateTaskStatisticData(List dataList){
		QueueStatistic queueStatistic = (QueueStatistic) taskStatisticMap.get(taskType);
		if(queueStatistic == null){
			queueStatistic = new QueueStatistic();
			queueStatistic.setTaskType(taskType);
			taskStatisticMap.put(taskType, queueStatistic);
		}
		queueStatistic.setFlushCount(queueStatistic.getFlushCount() + 1);
		if(dataList != null && dataList.size() >= 0){
			queueStatistic.setHasTaskCount(queueStatistic.getHasTaskCount() + 1);
			queueStatistic.setTaskSize(queueStatistic.getTaskSize() + dataList.size());
		}
	}

	private void setFlushIntervalTime(){
		queueLastFlushTime = new Date().getTime();
	}

	/**
	 * 取得本次任务刷新间隔
	 * @return
	 */
	private long getTaskIntervalTime(){
		if(taskLastFlushTime == null){
			return -1;
		}else{
			return new Date().getTime() - taskLastFlushTime;
		}
	}

	/**
	 * 判断任务刷新间隔
	 * @return
	 */
	private boolean checkFlushIntervalTime(){
		if(queueLastFlushTime == null){
			if(taskLastFlushTime == null){//任务首次刷新
				taskLastFlushTime = new Date().getTime();
				return true;
			}else{//任务再次刷新
				if(taskInterval > 0){
					long currTime = new Date().getTime();
					long intervalTime = new Date().getTime() - taskLastFlushTime;
					if(taskInterval > (new Date().getTime() - taskLastFlushTime)){
						logger.debug("{}任务刷新频率太快({}-{}-{}),返回", taskType, currTime, taskLastFlushTime, intervalTime);
						return false;
					}else{//任务再次开始
						taskLastFlushTime = new Date().getTime();
						return true;
					}
				}
			}
		}else{
			if(queueInterval > 0){
				long currTime = new Date().getTime();
				long intervalTime = new Date().getTime() - queueLastFlushTime;
				if(queueInterval > (intervalTime)){
					logger.debug("{}队列刷新频率太快({}-{}-{}),返回", taskType, currTime, queueLastFlushTime, intervalTime);
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 移除该任务队列
	 */
	public void removeQueue(){
		taskQueue = null;//释放队列数据
		logger.info(">>>>任务队列:{}停止", taskType);
	}

	/**
	 * 返回统计数据map
	 * @return
	 */
	public static Map getTaskStatisticMap(){
		return taskStatisticMap;
	}

	/**
	 * 用于重置统计数据
	 * @param taskStatisticMap
	 */
	public static void resetTaskStatisticMap(){
		taskStatisticMap.clear();
	}

	/**
	 * 设置任务循环完成标准,子类判断任务循环完一次时调用,控制任务循环间隔
	 */
	protected void setTaskFinishFlag(){
		queueLastFlushTime = null;
		//taskLastFlushTime = new Date().getTime();
		QueueStatistic queueStatistic = (QueueStatistic) taskStatisticMap.get(taskType);
		if(queueStatistic == null){
			queueStatistic = new QueueStatistic();
			queueStatistic.setTaskType(taskType);
			taskStatisticMap.put(taskType, queueStatistic);
		}
		queueStatistic.setTaskCount(queueStatistic.getTaskCount() + 1);
	}

	protected Map getAccountTokenMap(String accountType){
		Map tokenMap = null;
		try{
			Map accountMap = cache.getAccountTokenMap();
			LinksusAppaccount tokenObj = (LinksusAppaccount) accountMap.get(accountType);
			if(tokenObj != null){//无相应授权
				tokenMap = new HashMap();
				tokenMap.put("token", tokenObj.getToken());
				tokenMap.put("openid", tokenObj.getAppid());
				tokenMap.put("type", accountType);
				tokenMap.put("appkey", tokenObj.getAppKey());
				tokenMap.put("AccountId", tokenObj.getId());
				tokenMap.put("InstitutionId", tokenObj.getInstitutionId());
			}
		}catch (CacheException e){
			LogUtil.saveException(logger, e);
		}
		return tokenMap;
	}

	/** 响应客户端返回数据 */
	public String respondClientData(Map dataMap){
		String rs = "";
		try{
			rs = parseClientData(dataMap);
		}catch (Exception e){
			LogUtil.saveException(logger, e);
		}
		return rs;
	}

	/** 刷新任务队列 */
	protected abstract List flushTaskQueue();

	/** 处理取得的任务列表  */
	protected Map dealTaskData(List taskList){
		Map rsMap = new HashMap();
		if(taskList != null && taskList.size() > 0){
			rsMap.put(Constants.RETRUN_DATA_TASK_LIST, taskList);
		}
		return rsMap;
	}

	/** 处理客户端返回数据 
	 * @throws Exception */
	protected abstract String parseClientData(Map dataMap) throws Exception;
}
