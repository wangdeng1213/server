package com.linksus.common.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linksus.common.Constants;
import com.linksus.common.util.CacheUtil;
import com.linksus.entity.UrlEntity;
import com.linksus.interfaces.InterfaceFactory;
import com.linksus.queue.QueueFactory;
import com.linksus.task.ServerTaskControl;

public class LoadConfig{

	protected static final Logger logger = LoggerFactory.getLogger(LoadConfig.class);
	private static Map<String, String> params = new HashMap<String, String>();
	private static Map<String, TaskConfig> tasksMap;
	private static Map<String, QueueConfig> queuesMap;
	private static Map<String, InterfaceConfig> interfaceMap;
	private static Map<String, UrlEntity> urlsMap;
	private static Map<String, DataConfig> datasMap;
	private static Set clientSet = new HashSet();

	static{
		load();
	}

	/**
	 * ��ȡ�����ļ�
	 */
	private static void load(){
		SAXReader reader = new SAXReader();
		try{
			//config����
			Document configDoc = reader.read(LoadConfig.class.getClassLoader().getResource("config/server-config.xml"));
			Element root = configDoc.getRootElement();
			urlsMap = initConfig(root);
			//�������������
			Document taskDoc = reader.read(LoadConfig.class.getClassLoader().getResource("config/task-config.xml"));
			root = taskDoc.getRootElement();
			tasksMap = initTask(root);
			//�����������
			Document queueDoc = reader.read(LoadConfig.class.getClassLoader().getResource("config/queue-config.xml"));
			root = queueDoc.getRootElement();
			queuesMap = initQueue(root);
			//�ӿ������ļ�
			Document interfaceDoc = reader.read(LoadConfig.class.getClassLoader().getResource(
					"config/interface-config.xml"));
			root = interfaceDoc.getRootElement();
			interfaceMap = initInterface(root);
			//�������������ļ�
			Document dataDoc = reader.read(LoadConfig.class.getClassLoader().getResource("config/data-config.xml"));
			root = dataDoc.getRootElement();
			datasMap = initData(root);
		}catch (DocumentException e){
			throw new RuntimeException(e);
		}
	}

	/**
	 * ˢ�������ļ�
	 * @throws Exception 
	 */
	public static void reload() throws Exception{
		SAXReader reader = new SAXReader();
		try{
			//config����
			Document configDoc = reader.read(LoadConfig.class.getClassLoader().getResource("config/server-config.xml"));
			Element root = configDoc.getRootElement();
			urlsMap = initConfig(root);
			//�������������
			Document taskDoc = reader.read(LoadConfig.class.getClassLoader().getResource("config/task-config.xml"));
			root = taskDoc.getRootElement();
			Map tempTasksMap = initTask(root);
			for(Iterator itor = tasksMap.entrySet().iterator(); itor.hasNext();){
				Entry entry = (Entry) itor.next();
				if(!tempTasksMap.containsKey(entry.getKey())){//�������Ƴ��÷��������
					ServerTaskControl.removeServerTask((TaskConfig) entry.getValue());
				}
			}
			for(Iterator itor = tempTasksMap.entrySet().iterator(); itor.hasNext();){
				Entry entry = (Entry) itor.next();
				String taskType = (String) entry.getKey();
				TaskConfig taskConfig = (TaskConfig) entry.getValue();
				TaskConfig oldConfig = tasksMap.get(taskType);
				if(oldConfig == null){//����������
					ServerTaskControl.initServerTask(taskConfig);
					continue;
				}
				if(taskConfig.equals(oldConfig)){//δ������
					continue;
				}else{//������������
					ServerTaskControl.flushServerTask(taskConfig);
				}
			}
			tasksMap = tempTasksMap;
			//�����������
			Document queueDoc = reader.read(LoadConfig.class.getClassLoader().getResource("config/queue-config.xml"));
			root = queueDoc.getRootElement();
			Map tempQueuesMap = initQueue(root);
			for(Iterator itor = queuesMap.entrySet().iterator(); itor.hasNext();){
				Entry entry = (Entry) itor.next();
				QueueConfig queueConfig = (QueueConfig) entry.getValue();
				if(!tempQueuesMap.containsKey(entry.getKey())){//�������Ƴ��ö���
					QueueFactory.removeQueueTask(queueConfig.getTaskType());
				}
			}
			for(Iterator itor = tempQueuesMap.entrySet().iterator(); itor.hasNext();){
				Entry entry = (Entry) itor.next();
				String taskType = (String) entry.getKey();
				QueueConfig queueConfig = (QueueConfig) entry.getValue();
				QueueConfig oldConfig = queuesMap.get(taskType);
				if(oldConfig == null){//���������� �ͻ��˵����Զ�ʵ����
					continue;
				}
				if(queueConfig.equals(oldConfig)){//δ������
					continue;
				}else{//������������
					QueueFactory.flushQueueTask(queueConfig);
					queuesMap.put(taskType, queueConfig);//��ֹ�����ڼ䱻�ͻ��˵���ʵ����
				}
			}
			queuesMap = tempQueuesMap;
			//�ӿ������ļ�
			Document interfaceDoc = reader.read(LoadConfig.class.getClassLoader().getResource(
					"config/interface-config.xml"));
			root = interfaceDoc.getRootElement();
			Map tempInterfaceMap = initInterface(root);
			for(Iterator itor = interfaceMap.entrySet().iterator(); itor.hasNext();){
				Entry entry = (Entry) itor.next();
				InterfaceConfig interfaceConfig = (InterfaceConfig) entry.getValue();
				if(!tempInterfaceMap.containsKey(entry.getKey())){//�������Ƴ��ö���
					InterfaceFactory.removeInterface(interfaceConfig.getTaskType());
				}
			}
			for(Iterator itor = tempInterfaceMap.entrySet().iterator(); itor.hasNext();){
				Entry entry = (Entry) itor.next();
				String taskType = (String) entry.getKey();
				InterfaceConfig interfaceConfig = (InterfaceConfig) entry.getValue();
				InterfaceConfig oldConfig = interfaceMap.get(taskType);
				if(oldConfig == null){//���������� �ӿڵ����Զ�ʵ����
					continue;
				}
				if(interfaceConfig.equals(oldConfig)){//δ������
					continue;
				}else{//������������
					InterfaceFactory.flushInterface(interfaceConfig);
					interfaceMap.put(taskType, interfaceConfig);//��ֹ�����ڼ䱻�ͻ��˵���ʵ����
				}
			}
			interfaceMap = tempInterfaceMap;
			//�������������ļ�
			Document dataDoc = reader.read(LoadConfig.class.getClassLoader().getResource("config/data-config.xml"));
			root = dataDoc.getRootElement();
			datasMap = initData(root);
		}catch (Exception e){
			throw e;
		}
	}

	/**
	 * ȡ�÷������������
	 * @param rootElement
	 * @return
	 */
	private static Map<String, TaskConfig> initTask(Element rootElement){
		Element tasks = rootElement.element("tasks");
		Map<String, TaskConfig> rsMap = new HashMap<String, TaskConfig>();
		for(Iterator i = tasks.elementIterator("task"); i.hasNext();){
			TaskConfig config = new TaskConfig();
			Element task = (Element) i.next();
			String className = task.elementText("className");
			String taskType = task.elementText("taskType");
			config.setTaskName(task.elementText("taskName"));
			config.setTaskType(taskType);
			config.setClassName(className);
			config.setStatus(task.elementText("status"));
			config.setPageSize(task.elementText("pageSize"));
			config.setTaskInterval(task.elementText("taskInterval"));
			config.setCronExpression(task.elementText("cronExpression"));
			config.setMethodName(task.elementText("methodName"));
			Map paramsMap = new HashMap();
			Element params = task.element("params");
			for(Iterator e = params.elementIterator("param"); e.hasNext();){
				Element param = (Element) e.next();
				paramsMap.put(param.attributeValue("name"), param.getText());
			}
			config.setParamsMap(paramsMap);
			rsMap.put(taskType, config);
		}
		return rsMap;
	}

	/**
	 * ȡ�������������
	 * @param rootElement
	 * @return
	 */
	private static Map<String, QueueConfig> initQueue(Element rootElement){
		Element queues = rootElement.element("queues");
		Map<String, QueueConfig> rsMap = new HashMap<String, QueueConfig>();
		for(Iterator i = queues.elementIterator("queue"); i.hasNext();){
			QueueConfig config = new QueueConfig();
			Element queue = (Element) i.next();
			String className = queue.elementText("className");
			String taskType = queue.elementText("taskType");
			config.setTaskName(queue.elementText("taskName"));
			config.setTaskType(taskType);
			config.setClassName(className);
			config.setStatus(queue.elementText("status"));
			config.setPageSize(queue.elementText("pageSize"));
			config.setQueueInterval(queue.elementText("queueInterval"));
			config.setTaskInterval(queue.elementText("taskInterval"));
			config.setAssignCount(queue.elementText("assignCount"));
			config.setTaskCountPerDay(queue.elementText("taskCountPerDay"));
			config.setTaskCountPerHour(queue.elementText("taskCountPerHour"));
			Map paramsMap = new HashMap();
			Element params = queue.element("params");
			if(params != null){
				for(Iterator e = params.elementIterator("param"); e.hasNext();){
					Element param = (Element) e.next();
					paramsMap.put(param.attributeValue("name"), param.getText());
				}
			}
			config.setParamsMap(paramsMap);
			rsMap.put(taskType, config);
		}
		return rsMap;
	}

	/**
	 * ȡ�ýӿڵ�������
	 * @param rootElement
	 * @return
	 */
	private static Map<String, InterfaceConfig> initInterface(Element rootElement){
		Element interfaces = rootElement.element("interfaces");
		Map<String, InterfaceConfig> rsMap = new HashMap<String, InterfaceConfig>();
		for(Iterator i = interfaces.elementIterator("interface"); i.hasNext();){
			InterfaceConfig config = new InterfaceConfig();
			Element inter = (Element) i.next();
			String className = inter.elementText("className");
			String taskType = inter.elementText("taskType");
			//String methodName=inter.elementText("methodName");
			config.setTaskName(inter.elementText("taskName"));
			config.setTaskType(taskType);
			config.setClassName(className);
			config.setMethodName(inter.elementText("methodName"));
			config.setStatus(inter.elementText("status"));

			List paramsList = new ArrayList();
			Element params = inter.element("params");
			if(params != null){
				for(Iterator e = params.elementIterator("param"); e.hasNext();){
					Element param = (Element) e.next();
					InterfaceParams interfaceParams = new InterfaceParams();
					interfaceParams.setName(param.attributeValue("name"));
					interfaceParams.setRequired(param.attributeValue("required"));
					interfaceParams.setDataType(param.attributeValue("dataType"));
					interfaceParams.setParamDisp(param.getText());
					paramsList.add(interfaceParams);
				}
			}
			Map fieldsMap = new HashMap();
			Element fields = inter.element("fields");
			if(fields != null){
				for(Iterator e = fields.elementIterator("field"); e.hasNext();){
					Element param = (Element) e.next();
					fieldsMap.put(param.attributeValue("name"), param.getText());
				}
			}
			config.setParamsList(paramsList);
			config.setFieldsMap(fieldsMap);
			rsMap.put(taskType, config);
		}
		return rsMap;
	}

	/**
	 * ȡ��������������
	 * @param rootElement
	 * @return
	 */
	private static Map<String, DataConfig> initData(Element rootElement){
		Element queues = rootElement.element("queues");
		Map<String, DataConfig> rsMap = new HashMap<String, DataConfig>();
		for(Iterator i = queues.elementIterator("queue"); i.hasNext();){
			DataConfig config = new DataConfig();
			Element queue = (Element) i.next();
			config.setQueueName(queue.elementText("queueName"));
			config.setOperType(queue.elementText("operType"));
			config.setClassName(queue.elementText("className"));
			config.setCommitNum(queue.elementText("commitNum"));
			config.setCommitInterval(queue.elementText("commitInterval"));
			Element redis = queue.element("redis");
			if(redis != null && redis.hasContent()){
				config.setRedisFlag(true);
				config.setHashKey(redis.elementText("hashKey"));
				Element fieldKey = redis.element("fieldKey");
				config.setFieldKey(fieldKey.getText());
				config.setFieldSeparator(fieldKey.attributeValue("separator"));
				Element redisValue = redis.element("redisValue");
				config.setRedisValue(redisValue.getText());
				config.setValueSeparator(redisValue.attributeValue("separator"));
			}
			rsMap.put(config.getClassName() + "-" + config.getOperType(), config);
		}
		return rsMap;
	}

	/**
	 * ȡ�û�������
	 * @param rootElement
	 * @return
	 */
	private static Map<String, UrlEntity> initConfig(Element rootElement){
		Element configs = rootElement.element("configs");
		for(Iterator i = configs.elementIterator("config"); i.hasNext();){
			Element config = (Element) i.next();
			params.put(config.attributeValue("name"), config.attributeValue("value"));
			if("1".equals(config.attributeValue("client"))){
				clientSet.add(config.attributeValue("name"));
			}
		}
		//����URL
		Map<String, UrlEntity> urlMap = new HashMap<String, UrlEntity>();
		String[] types = new String[] { "sina", "tencent", "weixin", "other" };
		Element urls = rootElement.element("urls");
		int itype = 1;
		for(String type : types){
			Element tmpUrls = urls.element(type);
			Map baseUrlMap = new HashMap();
			for(Iterator i = tmpUrls.elementIterator("baseUrl"); i.hasNext();){
				Map tempMap = new HashMap();
				Element baseUrl = (Element) i.next();
				String urlType = baseUrl.attributeValue("urlType");
				String suffix = baseUrl.attributeValue("suffix");
				String limitType = baseUrl.attributeValue("limitType");
				urlType = StringUtils.isBlank(urlType) == false ? urlType : "tempUrlType";
				suffix = StringUtils.isBlank(suffix) == false ? suffix : "";
				limitType = StringUtils.isBlank(limitType) == false ? limitType : "0";
				tempMap.put("urlValue", baseUrl.getText());
				tempMap.put("suffix", suffix + "?");
				tempMap.put("limitType", limitType);
				baseUrlMap.put(urlType, tempMap);
			}
			for(Iterator i = tmpUrls.elementIterator("url"); i.hasNext();){
				Element url = (Element) i.next();
				UrlEntity urlEntity = new UrlEntity();
				urlEntity.setDesc(url.attributeValue("desc"));
				urlEntity.setName(url.attributeValue("name"));
				urlEntity.setUrlType(itype);
				urlEntity.setUrlKey(url.getText());
				String urlType = url.attributeValue("urlType");
				urlType = StringUtils.isBlank(urlType) == false ? urlType : "tempUrlType";
				Map tempMap = (Map) baseUrlMap.get(urlType);
				urlEntity.setLimitType(Integer.parseInt((String) tempMap.get("limitType")));
				urlEntity.setUrl(tempMap.get("urlValue") + url.getText() + tempMap.get("suffix"));
				urlMap.put(url.attributeValue("name"), urlEntity);
			}
			itype++;
		}
		return urlMap;
	}

	/** ˢ��API������� 
	 * @throws DocumentException */
	public static void reflushUrlEntity() throws DocumentException{
		//config����
		SAXReader reader = new SAXReader();
		Document configDoc = reader.read(LoadConfig.class.getClassLoader().getResource("config/server-config.xml"));
		Element root = configDoc.getRootElement();
		urlsMap = initConfig(root);
	}

	/**
	 * ���ؿͻ�������
	 * @return
	 * @throws Exception 
	 */
	public static Map getClientConfigInfo() throws Exception{
		//�������
		CacheUtil cache = CacheUtil.getInstance();
		Map map = new HashMap();
		//ϵͳapp
		map.put(Constants.CACHE_TENCENT_APPINFO, cache.getAppInfo(Constants.CACHE_TENCENT_APPINFO));
		map.put(Constants.CACHE_SINA_APPINFO, cache.getAppInfo(Constants.CACHE_SINA_APPINFO));
		//Token
		map.put(Constants.TASK_APP_ACCOUNT, cache.getAccountTokenMap());
		//��������
		map.put(Constants.CACHE_SINA_AREA_CODE, cache.getCache(Constants.CACHE_SINA_AREA_CODE));
		map.put(Constants.CACHE_TENCENT_AREA_CODE, cache.getCache(Constants.CACHE_TENCENT_AREA_CODE));
		map.put(Constants.CACHE_WEIXIN_AREA_CODE, cache.getCache(Constants.CACHE_WEIXIN_AREA_CODE));
		//�������
		map.put(Constants.CACHE_ERROR_CODE_SINA, cache.getCache(Constants.CACHE_ERROR_CODE_SINA));
		map.put(Constants.CACHE_ERROR_CODE_TENCENT, cache.getCache(Constants.CACHE_ERROR_CODE_TENCENT));
		//���ؿͻ��˵�ϵͳ����
		Map configMap = new HashMap();
		for(Iterator itor = clientSet.iterator(); itor.hasNext();){
			String key = (String) itor.next();
			configMap.put(key, params.get(key));
		}
		map.put("configMap", configMap);
		//UrlEntiry
		Map urlsMap = LoadConfig.getUrlsMap();
		List urlsList = new ArrayList();
		for(Iterator itor = urlsMap.entrySet().iterator(); itor.hasNext();){
			Entry entry = (Entry) itor.next();
			urlsList.add(entry.getValue());
		}
		map.put("urlsList", urlsList);
		return map;
	}

	public static String getString(String name){
		return params.get(name);
	}

	public static UrlEntity getUrlEntity(String name){
		return urlsMap.get(name);
	}

	public static void setString(String key,String value){
		params.put(key, value);
	}

	public static Map<String, TaskConfig> getTasksMap(){
		return tasksMap;
	}

	public static Map<String, QueueConfig> getQueuesMap(){
		return queuesMap;
	}

	public static Map<String, InterfaceConfig> getInterfaceMap(){
		return interfaceMap;
	}

	public static Map<String, UrlEntity> getUrlsMap(){
		return urlsMap;
	}

	public static Map<String, DataConfig> getDatasMap(){
		return datasMap;
	}

	public static void main(String[] args){
		Map map = LoadConfig.getUrlsMap();
		for(Iterator itor = map.entrySet().iterator(); itor.hasNext();){
			Entry entry = (Entry) itor.next();
			UrlEntity url = (UrlEntity) entry.getValue();
			System.out.println(entry.getKey() + ":name=" + url.getName() + ",urlType=" + url.getUrlType()
					+ ",limitType=" + url.getLimitType() + ",desc=" + url.getDesc() + ",url=" + url.getUrl());
		}
		//System.out.println(loadConfig.getString("tasktype"));
		// loadConfig.setString("tasktype", "2");
		//System.out.println("second :  " + loadConfig.getString("tasktype"));
	}

}