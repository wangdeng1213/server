package com.linksus.data;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linksus.common.util.ContextUtil;
import com.linksus.common.util.LogUtil;
import com.linksus.service.BaseService;

public class QueueDataSaveThread implements Runnable{

	protected static final Logger logger = LoggerFactory.getLogger(QueueDataSaveThread.class);
	private BaseService service;
	private List dataList;
	private String className;
	private String operType;

	public QueueDataSaveThread(List dataList, String className, String operType) {
		String serviceName = className.substring(0, 1).toLowerCase() + className.substring(1) + "Service";
		this.service = (BaseService) ContextUtil.getBean(serviceName);
		this.className = className;
		this.dataList = dataList;
		this.operType = operType;
	}

	public void run(){
		try{
			if(dataList.size() > 0){
				service.saveEntity(dataList, operType);
			}
		}catch (Exception e){
			LogUtil.saveException(logger, e);
		}finally{
			QueueDataSave.clearSyncFlag(className + "-" + operType);
		}
	}
}
