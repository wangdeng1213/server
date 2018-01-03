package com.linksus.queue;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linksus.common.Constants;
import com.linksus.common.config.LoadConfig;
import com.linksus.common.config.QueueConfig;
import com.linksus.common.util.HttpUtil;
import com.linksus.entity.UrlEntity;
import com.linksus.mina.TransferEntity;

public class QueueFactory{

	protected static final Logger logger = LoggerFactory.getLogger(QueueFactory.class);
	/** 任务实例Map */
	private static Map queueTnstanceMap = new HashMap();
	private static String queueServer = LoadConfig.getString("queueServer");
	private static UrlEntity remoteUrl = LoadConfig.getUrlEntity("remoteUrl");

	public static BaseQueue getQueueInstance(String taskType) throws Exception{
		if(queueTnstanceMap.containsKey(taskType)){
			return (BaseQueue) queueTnstanceMap.get(taskType);
		}else{
			Map map = LoadConfig.getQueuesMap();
			QueueConfig config = (QueueConfig) map.get(taskType);
			if(config == null){
				return null;
				//throw new Exception("该任务队列不存在,请查看配置:" + taskType);
			}else if(!"1".equals(config.getStatus())){
				return null;
				//throw new Exception("任务" + config.getTaskType() + "-" + config.getTaskName() + ":未启用,请修改配置");
			}
			BaseQueue queue = initQueueTask(config);
			queueTnstanceMap.put(taskType, queue);
			return queue;
		}
	}

	/**
	 * 实例化队列类
	 * @param config
	 * @return
	 * @throws Exception
	 */
	public static BaseQueue initQueueTask(QueueConfig config) throws Exception{
		String pkg = QueueFactory.class.getPackage().getName();
		String claName = pkg + "." + config.getClassName();
		Class taskClass = Class.forName(claName);
		// 初始化构造函数
		Constructor cc = taskClass.getDeclaredConstructor(String.class);
		BaseQueue queue = (BaseQueue) cc.newInstance(config.getTaskType());
		initQueueField(queue, config);
		return queue;
	}

	private static void initQueueField(BaseQueue queue,QueueConfig config) throws Exception{
		//公共属性
		if(!StringUtils.isBlank(config.getPageSize())){//每页任务数
			queue.setPageSize(Integer.parseInt(config.getPageSize()));
		}
		if(!StringUtils.isBlank(config.getQueueInterval())){//队列刷新间隔
			queue.setQueueInterval(Long.parseLong(config.getQueueInterval()));
		}
		if(!StringUtils.isBlank(config.getTaskInterval())){//任务刷新间隔
			queue.setTaskInterval(Long.parseLong(config.getTaskInterval()));
		}
		if(!StringUtils.isBlank(config.getAssignCount())){//客户端分配任务数
			queue.setAssignCount(Integer.parseInt(config.getAssignCount()));
		}
		if(!StringUtils.isBlank(config.getTaskCountPerHour())){//每小时分配任务数
			queue.setTaskCountPerHour(Long.parseLong(config.getTaskCountPerHour()));
		}
		if(!StringUtils.isBlank(config.getTaskCountPerDay())){//每天分配任务数
			queue.setTaskCountPerDay(Long.parseLong(config.getTaskCountPerDay()));
		}
		//私有属性
		Map paramsMap = config.getParamsMap();
		for(Iterator itor = paramsMap.entrySet().iterator(); itor.hasNext();){
			Entry entry = (Entry) itor.next();
			String fieldName = (String) entry.getKey();
			String fieldValue = (String) entry.getValue();
			// 反射赋值
			String setMethodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
			Method setFeildMethod = queue.getClass().getMethod(setMethodName, String.class);
			setFeildMethod.invoke(queue, fieldValue);
		}
	}

	/**
	 * 移除已经配置的队列
	 * @param config
	 * @return
	 * @throws Exception
	 */
	public static void removeQueueTask(String taskType) throws Exception{
		BaseQueue queue = (BaseQueue) queueTnstanceMap.get(taskType);
		if(queue != null){
			queueTnstanceMap.remove(taskType);
			queue.removeQueue();
		}
	}

	/**
	 * 刷新队列类:配置文件更新
	 * @param config
	 * @throws Exception
	 */
	public static void flushQueueTask(QueueConfig config) throws Exception{
		String taskType = config.getTaskType();
		BaseQueue queue = (BaseQueue) queueTnstanceMap.get(taskType);
		if(queue == null){//无客户端连接,未实例化
			return;
		}else{
			if(!"1".equals(config.getStatus())){//1 到 0 
				removeQueueTask(taskType);
			}else{
				initQueueField(queue, config);
			}
		}
	}

	/**
	 * 向特定队列放入任务数据,需要确认客户端已经启用该任务,否则会造成队列数据只增不减,内存溢出!!
	 * @param taskType
	 * @param dataList
	 * @throws Exception
	 */
	public static void addQueueTaskData(String taskType,Object object) throws Exception{
		if(object == null){
			throw new Exception("队列不能添加空对象");
		}
		if(!"1".equals(queueServer)){
			IoSession session = HttpUtil.getIoSession("addQueue");
			/*
			 * byte[] objBytes = ZLibUtil.ObjectToByte(object); BASE64Encoder
			 * encoder = new BASE64Encoder(); String objStr =
			 * encoder.encode(objBytes);
			 */
			Map paramMap = new HashMap();
			//paramMap.put("taskType", "99");
			paramMap.put("queueType", taskType);
			paramMap.put("queueObj", object);

			TransferEntity entity = new TransferEntity();
			entity.setTaskType(Constants.REMOTE_QUEUE_TASK);
			entity.setRequestType(Constants.REQUEST_TYPE_DATA_RETURN);
			entity.setTransferData(paramMap);
			session.write(entity);
			/*
			 * String str = HttpUtil.getRequest(remoteUrl, paramMap); String
			 * errcode = JsonUtil.getNodeValueByName(str, "errcode");
			 * if(!"0".equals(errcode)){ logger.error(str); throw new
			 * Exception("远程队列添加失败:" + str); }
			 */
			return;
		}
		getQueueInstance(taskType);//首次初始化
		BaseQueue baseQueue = (BaseQueue) queueTnstanceMap.get(taskType);
		Queue queue = baseQueue.getTaskQueue();
		if(object instanceof List){
			List dataList = (List) object;
			if(dataList != null){
				for(int i = 0; i < dataList.size(); i++){
					queue.offer(dataList.get(i));//将任务放入队列
				}
			}
		}else{
			queue.offer(object);
		}
	}

}
