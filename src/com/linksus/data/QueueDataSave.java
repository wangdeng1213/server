package com.linksus.data;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linksus.common.Constants;
import com.linksus.common.config.DataConfig;
import com.linksus.common.config.LoadConfig;
import com.linksus.common.util.CacheUtil;
import com.linksus.common.util.ContextUtil;
import com.linksus.common.util.LogUtil;
import com.linksus.common.util.RedisUtil;
import com.linksus.entity.LinksusRelationUserAccount;
import com.linksus.entity.LinksusRelationWeibouser;
import com.linksus.service.BaseService;

public class QueueDataSave extends BaseDataSave{

	protected static final Logger logger = LoggerFactory.getLogger(QueueDataSave.class);
	private static Map queueMap = new HashMap();
	private static Map dealTimeMap = new HashMap();
	private static Map syncMap = new HashMap();
	private static int threadPoolNum = Integer.parseInt(LoadConfig.getString("dataSaveThreadPoolNum"));
	private static ExecutorService threadPool;
	//缓存对象
	protected static CacheUtil cache = CacheUtil.getInstance();

	static{
		threadPool = Executors.newFixedThreadPool(threadPoolNum);
	}

	/**
	 * 将需要操作的对象放入相应队列中
	 * @param obj
	 * @param operType
	 */
	public static void addDataToQueue(Object obj,String operType){
		//判断如果是接口服务器地址不走队列 
		String interfaceServer = LoadConfig.getString("interfaceServer");
		String mapKey = obj.getClass().getSimpleName() + "-" + operType;
		Map datasMap = LoadConfig.getDatasMap();
		DataConfig config = (DataConfig) datasMap.get(mapKey);
		if(StringUtils.equals(interfaceServer, "1")){//如果是接口服务器，直接调用service方法进行保存
			String serviceName = obj.getClass().getSimpleName().substring(0, 1).toLowerCase()
					+ obj.getClass().getSimpleName().substring(1) + "Service";
			BaseService service = (BaseService) ContextUtil.getBean(serviceName);
			List listObj = new ArrayList();
			listObj.add(obj);
			try{
				service.saveEntity(listObj, operType);
			}catch (Exception e){
				LogUtil.saveException(logger, e);
				e.printStackTrace();
			}
		}else{
			if(config == null){
				LogUtil.saveException(logger, new Exception("未配置相应批量提交队列:" + mapKey));
				return;
			}
			Queue queue = (Queue) queueMap.get(mapKey);
			if(queue == null){
				queue = new ConcurrentLinkedQueue();
				queueMap.put(mapKey, queue);
			}
			Long lastTime = (Long) dealTimeMap.get(mapKey);
			if(lastTime == null){
				lastTime = new Date().getTime();
				dealTimeMap.put(mapKey, lastTime);
			}

			//调试代码
			if(obj instanceof LinksusRelationUserAccount){
				LinksusRelationUserAccount userAccount = (LinksusRelationUserAccount) obj;
				if(userAccount.getFlagRelation() == 0){
					LogUtil.saveException(logger, new Exception("用户账号关系flagRelation=0:" + userAccount.getUserId() + "-"
							+ userAccount.getAccountId()));
				}else if(userAccount.getFlagRelation() == 4 && "000000".equals(userAccount.getInteractType())){
					LogUtil.saveException(logger, new Exception("用户账号关系flagRelation=4,interactType=000000:"
							+ userAccount.getUserId() + "-" + userAccount.getAccountId()));
				}
			}

			if(obj instanceof LinksusRelationWeibouser){
				LinksusRelationWeibouser user = (LinksusRelationWeibouser) obj;
				if("2".equals(user.getAccountType()) && "0".equals(user.getRpsCreatedAt() + "")){
					LogUtil.saveException(logger, new Exception("腾讯用户创建时间为0:" + user.getRpsAvatarLarge() + "-"
							+ user.getRpsStatusesCount()));
				}
			}

			queue.offer(obj);
		}
		//加入缓存中
		if(config.isRedisFlag()){
			Map map;
			try{
				map = parseRedisValues(obj, config);
			}catch (Exception e){
				LogUtil.saveException(logger, e);
				return;
			}
			//logger.info(".......加入缓存 hKey={},fkey={},val={}",config.getHashKey(),map.get("redisKey"),map.get("redisValue"));
			RedisUtil.setRedisHash(config.getHashKey(), (String) map.get("redisKey"), (String) map.get("redisValue"));
		}
		//logger.info(".......mapKey={},id={}",mapKey,id);
	}

	@Override
	public void run(){
		while (true){
			// 最小批量提交数
			int commitNum = Integer.parseInt(LoadConfig.getString("commitNum"));
			// 最大提交间隔时间 毫秒
			long commitInterval = Long.parseLong(LoadConfig.getString("commitInterval"));
			try{
				Map tempMap = LoadConfig.getDatasMap();
				for(Iterator itor = tempMap.keySet().iterator(); itor.hasNext();){
					String mapKey = (String) itor.next();
					Queue queue = (Queue) queueMap.get(mapKey);
					if(queue == null || queue.size() == 0){//无数据
						continue;
					}
					if("1".equals(syncMap.get(mapKey))){//该队列处理中,防止死锁
						continue;
					}
					Long lastTime = (Long) dealTimeMap.get(mapKey);
					if(queue.size() >= commitNum || commitInterval <= getIntervalTime(lastTime)){//达到提交数/或最大间隔时间
						//logger.info(">>>>>>>>>>>>>>>>提交数据:{}",queue.size());
						Object obj = null;
						List list = new ArrayList();
						while ((obj = queue.poll()) != null){
							list.add(obj);
						}
						//获得线程 执行方法
						String className = mapKey.substring(0, mapKey.indexOf("-"));
						String operType = mapKey.substring(mapKey.indexOf("-") + 1);
						QueueDataSaveThread thread = new QueueDataSaveThread(list, className, operType);
						syncMap.put(mapKey, "1");//执行中标志
						threadPool.execute(thread);
						dealTimeMap.put(mapKey, new Date().getTime());//操作时间
					}
				}
				Thread.sleep(5);
			}catch (Exception e){
				LogUtil.saveException(logger, e);
			}
		}
	}

	/**
	 * 根据配置取得缓存Key,value
	 * @param obj
	 * @param config
	 * @return
	 * @throws Exception
	 */
	private static Map parseRedisValues(Object obj,DataConfig config) throws Exception{
		Map map = new HashMap();
		String[] fieldKeys = config.getFieldKey().split(",");
		String[] redisValues = config.getRedisValue().split(",");
		StringBuffer keyValue = new StringBuffer("");
		if(fieldKeys.length > 1){
			for(int i = 0; i < fieldKeys.length; i++){
				String fieldKey = fieldKeys[i];
				String getMethodName = "get" + fieldKey.substring(0, 1).toUpperCase() + fieldKey.substring(1);
				Method getFeildMethod = obj.getClass().getMethod(getMethodName, null);
				Object rsObj = getFeildMethod.invoke(obj, null);
				if(rsObj != null){
					keyValue.append(rsObj.toString()).append(config.getFieldSeparator());
				}
			}
			if(keyValue.length() > 0){
				keyValue.deleteCharAt(keyValue.length() - 1);
			}
		}else{
			String getMethodName = "get" + fieldKeys[0].substring(0, 1).toUpperCase() + fieldKeys[0].substring(1);
			Method getFeildMethod = obj.getClass().getMethod(getMethodName, null);
			Object rsObj = getFeildMethod.invoke(obj, null);
			if(rsObj != null){
				keyValue.append(rsObj.toString());
			}
		}
		StringBuffer redisBuff = new StringBuffer("");
		if(redisValues.length > 1){
			for(int i = 0; i < redisValues.length; i++){
				String redisValue = redisValues[i];
				String getMethodName = "get" + redisValue.substring(0, 1).toUpperCase() + redisValue.substring(1);
				Method getFeildMethod = obj.getClass().getMethod(getMethodName, null);
				Object rsObj = getFeildMethod.invoke(obj, null);
				if(rsObj != null){
					redisBuff.append(rsObj.toString()).append(config.getValueSeparator());
				}
			}
			if(redisBuff.length() > 0){
				redisBuff.deleteCharAt(redisBuff.length() - 1);
			}
		}else{
			String getMethodName = "get" + redisValues[0].substring(0, 1).toUpperCase() + redisValues[0].substring(1);
			Method getFeildMethod = obj.getClass().getMethod(getMethodName, null);
			Object rsObj = getFeildMethod.invoke(obj, null);
			if(rsObj != null){
				redisBuff.append(rsObj.toString());
			}
		}

		map.put("redisKey", keyValue.toString());
		map.put("redisValue", redisBuff.toString());
		return map;
	}

	public static void clearSyncFlag(String mapKey){
		syncMap.put(mapKey, "0");
	}

	/**
	 * 取得队列提交间隔时间
	 * @param lastTime
	 * @return
	 */
	private long getIntervalTime(Long lastTime){
		long currTime = new Date().getTime();
		return currTime - lastTime;
	}

	public static void main(String[] args){
		Thread thread = new Thread(new QueueDataSave());
		thread.start();
		//		LinksusRelationWeibouser user=new LinksusRelationWeibouser();
		//		user.setAccountId(123l);
		//		user.setUserId(1213l);
		//		QueueDataSave.addDataToQueue(user, Constants.OPER_TYPE_INSERT);
		//		
		//		LinksusRelationWeibouser user1=new LinksusRelationWeibouser();
		//		user1.setAccountId(124l);
		//		user1.setUserId(1214l);
		//		QueueDataSave.addDataToQueue(user1, Constants.OPER_TYPE_INSERT);
		//		
		//		LinksusRelationPerson person=new LinksusRelationPerson();
		//		person.setPersonId(12311l);
		//		person.setGender("m");
		//		QueueDataSave.addDataToQueue(person, Constants.OPER_TYPE_INSERT);
		//		
		//		LinksusRelationPerson person1=new LinksusRelationPerson();
		//		person1.setPersonId(12312l);
		//		person1.setGender("m");
		//		QueueDataSave.addDataToQueue(person1, Constants.OPER_TYPE_INSERT);

		LinksusRelationUserAccount userAccount = new LinksusRelationUserAccount();
		userAccount.setPid(12312123l);
		userAccount.setAccountId(10012l);
		QueueDataSave.addDataToQueue(userAccount, Constants.OPER_TYPE_INSERT);
		LinksusRelationUserAccount userAccount1 = new LinksusRelationUserAccount();
		userAccount1.setPid(12312222l);
		userAccount1.setAccountId(1101l);
		QueueDataSave.addDataToQueue(userAccount1, Constants.OPER_TYPE_INSERT);
	}
}
