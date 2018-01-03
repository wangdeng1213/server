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
	//��������
	/** ����ˢ�¼�� */
	private long queueInterval = 0;
	/** ����ˢ�¼�� */
	private long taskInterval = 0;
	/** ÿҳ������ */
	protected int pageSize = 500;
	/** �ͻ���ÿ������������ */
	private int assignCount = 100;
	/** ÿСʱ���������� */
	private long taskCountPerHour = 0;
	/** ÿ����������� */
	private long taskCountPerDay = 0;
	//��������
	/** �������� */
	public String taskType;

	/** ��ǰСʱ/���ѷ���������*/
	private Map taskCountMap = new HashMap();
	/** ������� */
	private Queue taskQueue = new ConcurrentLinkedQueue();
	/** �������ͳ��MAP */
	private static Map taskStatisticMap = new HashMap();
	/** �����ϴ�ˢ��ʱ�� */
	private Long queueLastFlushTime;
	/** �����ϴ�ˢ��ʱ��(��ʼ) */
	private Long taskLastFlushTime;
	/** ������� */
	protected CacheUtil cache = CacheUtil.getInstance();
	/** ��ǰ��ҳ��ѯҳ�� */
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

	/** �Ӷ���ȡ�������б� */
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
		logger.info("�ͻ���ȡ������{},��������{}", taskType, rsList.size());
		return dealTaskData(rsList);
	}

	/**
	 * �������
	 */
	private synchronized boolean assignTask(List dataList,int taskCount){
		Object obj = null;
		if((obj = taskQueue.poll()) != null){
			taskCount++;
			dataList.add(obj);
			//��������������
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
	 * ��鵱ǰСʱ/��������������Ƿ�ﵽ����  true δ�ﵽ���� false �Ѵ�����
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
					//�ȼ�鵱��������
					if(taskCountPerDay > 0){
						String tmpDay = (String) taskCountMap.get("currDay");
						if(currDay.equals(tmpDay)){
							Long icount = (Long) taskCountMap.get("dayCount");
							if(icount.longValue() >= taskCountPerDay){//���������������
								logger.info("�ﵽ��������������,���ٷ�������,taskCountPerDay={}", taskCountPerDay);
								return false;
							}
						}else{
							taskCountMap.put("currDay", currDay);
							taskCountMap.put("dayCount", new Long(0));
						}
					}
					//��鵱ǰСʱ������
					if(taskCountPerHour > 0){
						String tmpHour = (String) taskCountMap.get("currHour");
						if(currHour.equals(tmpHour)){
							Long icount = (Long) taskCountMap.get("hourCount");
							if(icount.longValue() >= taskCountPerHour){//����Сʱ�������
								logger.info("�ﵽ��ǰСʱ����������,���ٷ�������,taskCountPerHour={}", taskCountPerHour);
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
		if(!taskQueue.isEmpty()){//�Ѿ���ˢ��
			return;
		}
		//1 �ж�ˢ��ʱ����
		boolean flag = checkFlushIntervalTime();
		if(!flag){//ˢ��Ƶ��̫��
			return;
		}
		//2 �޸�����ˢ��ʱ��
		setFlushIntervalTime();
		//3 ������������
		List dataList = flushTaskQueue();
		if(dataList != null){
			for(int i = 0; i < dataList.size(); i++){
				taskQueue.offer(dataList.get(i));//������������
			}
		}
		//3 �޸�ͳ������
		updateTaskStatisticData(dataList);
	}

	/**
	 * ���¶���ˢ������
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
	 * ȡ�ñ�������ˢ�¼��
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
	 * �ж�����ˢ�¼��
	 * @return
	 */
	private boolean checkFlushIntervalTime(){
		if(queueLastFlushTime == null){
			if(taskLastFlushTime == null){//�����״�ˢ��
				taskLastFlushTime = new Date().getTime();
				return true;
			}else{//�����ٴ�ˢ��
				if(taskInterval > 0){
					long currTime = new Date().getTime();
					long intervalTime = new Date().getTime() - taskLastFlushTime;
					if(taskInterval > (new Date().getTime() - taskLastFlushTime)){
						logger.debug("{}����ˢ��Ƶ��̫��({}-{}-{}),����", taskType, currTime, taskLastFlushTime, intervalTime);
						return false;
					}else{//�����ٴο�ʼ
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
					logger.debug("{}����ˢ��Ƶ��̫��({}-{}-{}),����", taskType, currTime, queueLastFlushTime, intervalTime);
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * �Ƴ����������
	 */
	public void removeQueue(){
		taskQueue = null;//�ͷŶ�������
		logger.info(">>>>�������:{}ֹͣ", taskType);
	}

	/**
	 * ����ͳ������map
	 * @return
	 */
	public static Map getTaskStatisticMap(){
		return taskStatisticMap;
	}

	/**
	 * ��������ͳ������
	 * @param taskStatisticMap
	 */
	public static void resetTaskStatisticMap(){
		taskStatisticMap.clear();
	}

	/**
	 * ��������ѭ����ɱ�׼,�����ж�����ѭ����һ��ʱ����,��������ѭ�����
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
			if(tokenObj != null){//����Ӧ��Ȩ
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

	/** ��Ӧ�ͻ��˷������� */
	public String respondClientData(Map dataMap){
		String rs = "";
		try{
			rs = parseClientData(dataMap);
		}catch (Exception e){
			LogUtil.saveException(logger, e);
		}
		return rs;
	}

	/** ˢ��������� */
	protected abstract List flushTaskQueue();

	/** ����ȡ�õ������б�  */
	protected Map dealTaskData(List taskList){
		Map rsMap = new HashMap();
		if(taskList != null && taskList.size() > 0){
			rsMap.put(Constants.RETRUN_DATA_TASK_LIST, taskList);
		}
		return rsMap;
	}

	/** ����ͻ��˷������� 
	 * @throws Exception */
	protected abstract String parseClientData(Map dataMap) throws Exception;
}
