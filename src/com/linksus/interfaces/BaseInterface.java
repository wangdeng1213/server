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
	/** 接口调用统计MAP */
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
			//统计接口调用信息
			updateCallCount();
			Object obj = cal(paramMap);
			if(obj == null){
				rs = StringUtil.getHttpResultErrorStr(null);
			}else if(obj instanceof String){
				String rsStr = (String) obj;
				if(rsStr.startsWith("$")){//直接返回
					rs = rsStr.substring(1);
				}else{
					if(!StringUtils.isBlank((String) obj)){//存在错误
						//统计错误信息
						errorStatistic(obj);
					}
					rs = StringUtil.getHttpResultErrorStr((String) obj);
				}
			}else if(obj instanceof Map){
				Object errorCode = ((Map) obj).get("errcode");
				if(errorCode != null){//存在错误
					//统计错误信息
					errorStatistic(errorCode);
				}
				rs = StringUtil.getHttpResultStr((Map) obj);
			}
			if(!"26".equals(taskType) && !"103".equals(taskType)){
				logger.info("接口taskType:{} 返回结果:{}", taskType, rs);
			}
		}catch (Exception e){
			LogUtil.saveException(logger, e);
			return StringUtil.getHttpResultForException(e);
		}
		return rs;
	}

	/**
	 * 记录调用次数
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
	 * 统计调用错误
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
