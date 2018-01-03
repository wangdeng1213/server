package com.linksus.interfaces;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linksus.common.util.CacheUtil;
import com.linksus.common.util.LogUtil;
import com.linksus.common.util.StringUtil;
import com.linksus.entity.InterfaceStatistic;

public abstract class BaseInterface{

	protected static final Logger logger = LoggerFactory.getLogger(BaseInterface.class);
	protected CacheUtil cache = CacheUtil.getInstance();
	private String taskType;
	/** �ӿڵ���ͳ��MAP */
	private static Map taskStatisticMap = new HashMap();

	public String getTaskType(){
		return taskType;
	}

	public void setTaskType(String taskType){
		this.taskType = taskType;
	}

	public abstract Object cal(Map paramMap) throws Exception;

	public String exec(Map paramMap){
		String rs = "";
		try{
			//ͳ�ƽӿڵ�����Ϣ
			updateCallCount();
			Object obj = cal(paramMap);
			if(obj == null){
				rs = StringUtil.getHttpResultErrorStr(null);
			}else if(obj instanceof String){
				String rsStr = (String) obj;
				if(rsStr.startsWith("$")){//ֱ�ӷ���
					rs = rsStr.substring(1);
				}else{
					if(!StringUtils.isBlank((String) obj)){//���ڴ���
						//ͳ�ƴ�����Ϣ
						errorStatistic(obj);
					}
					rs = StringUtil.getHttpResultErrorStr((String) obj);
				}
			}else if(obj instanceof Map){
				Object errorCode = ((Map) obj).get("errcode");
				if(errorCode != null){//���ڴ���
					//ͳ�ƴ�����Ϣ
					errorStatistic(errorCode);
				}
				rs = StringUtil.getHttpResultStr((Map) obj);
			}
			if(!"26".equals(taskType) && !"103".equals(taskType)){
				logger.info("�ӿ�taskType:{} ���ؽ��:{}", taskType, rs);
			}
		}catch (Exception e){
			LogUtil.saveException(logger, e);
			return StringUtil.getHttpResultForException(e);
		}
		return rs;
	}

	/**
	 * ��¼���ô���
	 */
	private synchronized void updateCallCount(){
		InterfaceStatistic interfaceStatistic = (InterfaceStatistic) taskStatisticMap.get(taskType);
		if(interfaceStatistic == null){
			interfaceStatistic = new InterfaceStatistic();
			interfaceStatistic.setTaskType(taskType);
			taskStatisticMap.put(taskType, interfaceStatistic);
		}
		interfaceStatistic.setTaskSize(interfaceStatistic.getTaskSize() + 1);
	}

	/**
	 * ͳ�Ƶ��ô���
	 * @param errorCode
	 */
	private synchronized void errorStatistic(Object errorCode){
		InterfaceStatistic interfaceStatistic = (InterfaceStatistic) taskStatisticMap.get(taskType);
		interfaceStatistic.setErrorCodeCount(interfaceStatistic.getErrorCodeCount() + 1);
	}

	public static void resetTaskStatisticMap(){
		taskStatisticMap.clear();
	}

	public static Map getTaskStatisticMap(){
		return taskStatisticMap;
	}

}
