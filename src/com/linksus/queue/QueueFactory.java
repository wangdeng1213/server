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
	/** ����ʵ��Map */
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
				//throw new Exception("��������в�����,��鿴����:" + taskType);
			}else if(!"1".equals(config.getStatus())){
				return null;
				//throw new Exception("����" + config.getTaskType() + "-" + config.getTaskName() + ":δ����,���޸�����");
			}
			BaseQueue queue = initQueueTask(config);
			queueTnstanceMap.put(taskType, queue);
			return queue;
		}
	}

	/**
	 * ʵ����������
	 * @param config
	 * @return
	 * @throws Exception
	 */
	public static BaseQueue initQueueTask(QueueConfig config) throws Exception{
		String pkg = QueueFactory.class.getPackage().getName();
		String claName = pkg + "." + config.getClassName();
		Class taskClass = Class.forName(claName);
		// ��ʼ�����캯��
		Constructor cc = taskClass.getDeclaredConstructor(String.class);
		BaseQueue queue = (BaseQueue) cc.newInstance(config.getTaskType());
		initQueueField(queue, config);
		return queue;
	}

	private static void initQueueField(BaseQueue queue,QueueConfig config) throws Exception{
		//��������
		if(!StringUtils.isBlank(config.getPageSize())){//ÿҳ������
			queue.setPageSize(Integer.parseInt(config.getPageSize()));
		}
		if(!StringUtils.isBlank(config.getQueueInterval())){//����ˢ�¼��
			queue.setQueueInterval(Long.parseLong(config.getQueueInterval()));
		}
		if(!StringUtils.isBlank(config.getTaskInterval())){//����ˢ�¼��
			queue.setTaskInterval(Long.parseLong(config.getTaskInterval()));
		}
		if(!StringUtils.isBlank(config.getAssignCount())){//�ͻ��˷���������
			queue.setAssignCount(Integer.parseInt(config.getAssignCount()));
		}
		if(!StringUtils.isBlank(config.getTaskCountPerHour())){//ÿСʱ����������
			queue.setTaskCountPerHour(Long.parseLong(config.getTaskCountPerHour()));
		}
		if(!StringUtils.isBlank(config.getTaskCountPerDay())){//ÿ�����������
			queue.setTaskCountPerDay(Long.parseLong(config.getTaskCountPerDay()));
		}
		//˽������
		Map paramsMap = config.getParamsMap();
		for(Iterator itor = paramsMap.entrySet().iterator(); itor.hasNext();){
			Entry entry = (Entry) itor.next();
			String fieldName = (String) entry.getKey();
			String fieldValue = (String) entry.getValue();
			// ���丳ֵ
			String setMethodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
			Method setFeildMethod = queue.getClass().getMethod(setMethodName, String.class);
			setFeildMethod.invoke(queue, fieldValue);
		}
	}

	/**
	 * �Ƴ��Ѿ����õĶ���
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
	 * ˢ�¶�����:�����ļ�����
	 * @param config
	 * @throws Exception
	 */
	public static void flushQueueTask(QueueConfig config) throws Exception{
		String taskType = config.getTaskType();
		BaseQueue queue = (BaseQueue) queueTnstanceMap.get(taskType);
		if(queue == null){//�޿ͻ�������,δʵ����
			return;
		}else{
			if(!"1".equals(config.getStatus())){//1 �� 0 
				removeQueueTask(taskType);
			}else{
				initQueueField(queue, config);
			}
		}
	}

	/**
	 * ���ض����з�����������,��Ҫȷ�Ͽͻ����Ѿ����ø�����,�������ɶ�������ֻ������,�ڴ����!!
	 * @param taskType
	 * @param dataList
	 * @throws Exception
	 */
	public static void addQueueTaskData(String taskType,Object object) throws Exception{
		if(object == null){
			throw new Exception("���в�����ӿն���");
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
			 * Exception("Զ�̶������ʧ��:" + str); }
			 */
			return;
		}
		getQueueInstance(taskType);//�״γ�ʼ��
		BaseQueue baseQueue = (BaseQueue) queueTnstanceMap.get(taskType);
		Queue queue = baseQueue.getTaskQueue();
		if(object instanceof List){
			List dataList = (List) object;
			if(dataList != null){
				for(int i = 0; i < dataList.size(); i++){
					queue.offer(dataList.get(i));//������������
				}
			}
		}else{
			queue.offer(object);
		}
	}

}
