package com.linksus.interfaces;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linksus.common.config.InterfaceConfig;
import com.linksus.common.config.LoadConfig;

public class InterfaceFactory{

	/** 任务实例Map */
	private static Map interfacesMap = new HashMap();
	protected static final Logger logger = LoggerFactory.getLogger(InterfaceFactory.class);

	public static BaseInterface getInstance(String type) throws Exception{
		if(interfacesMap.containsKey(type)){
			return (BaseInterface) interfacesMap.get(type);
		}else{
			Map map = LoadConfig.getInterfaceMap();
			InterfaceConfig config = (InterfaceConfig) map.get(type);
			BaseInterface task = initInterface(config);
			interfacesMap.put(type, task);
			return task;
		}
	}

	/**
	 * 实例化接口类
	 * @param config
	 * @return
	 * @throws Exception
	 */
	public static BaseInterface initInterface(InterfaceConfig config) throws Exception{
		String pkg = InterfaceFactory.class.getPackage().getName();
		String claName = pkg + "." + config.getClassName();
		Class taskClass = Class.forName(claName);
		// 初始化构造函数
		Constructor cc = taskClass.getDeclaredConstructor();
		BaseInterface task = (BaseInterface) cc.newInstance();
		initInterfaceField(task, config);
		return task;
	}

	private static void initInterfaceField(BaseInterface task,InterfaceConfig config) throws Exception{
		task.setTaskType(config.getTaskType());
		//私有属性
		Map fieldsMap = config.getFieldsMap();
		for(Iterator itor = fieldsMap.entrySet().iterator(); itor.hasNext();){
			Entry entry = (Entry) itor.next();
			String fieldName = (String) entry.getKey();
			String fieldValue = (String) entry.getValue();
			// 反射赋值
			String setMethodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
			Method setFeildMethod = task.getClass().getMethod(setMethodName, String.class);
			setFeildMethod.invoke(task, fieldValue);
		}
	}

	public static void removeInterface(String taskType){
		BaseInterface task = (BaseInterface) interfacesMap.get(taskType);
		if(task != null){
			interfacesMap.remove(taskType);
			logger.info(">>>>接口:{}停止", taskType);
		}
	}

	public static void flushInterface(InterfaceConfig interfaceConfig) throws Exception{
		String taskType = interfaceConfig.getTaskType();
		BaseInterface task = (BaseInterface) interfacesMap.get(taskType);
		if(task == null){//无接口连接,未实例化
			return;
		}else{
			if(!"1".equals(interfaceConfig.getStatus())){//1 到 0 
				removeInterface(taskType);
			}else{
				initInterfaceField(task, interfaceConfig);
			}
		}
	}
}
