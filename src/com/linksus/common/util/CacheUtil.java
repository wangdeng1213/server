package com.linksus.common.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.jcs.JCS;
import org.apache.jcs.access.exception.CacheException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linksus.common.Constants;
import com.linksus.common.config.LoadConfig;
import com.linksus.common.module.TaskStatisticCommon;
import com.linksus.entity.AppInfo;
import com.linksus.entity.LinksusAppaccount;
import com.linksus.entity.LinksusTaskConnLimit;
import com.linksus.entity.LinksusTaskErrorCode;
import com.linksus.service.LinksusTaskConnLimitService;

public class CacheUtil{

	protected static final Logger logger = LoggerFactory.getLogger(CacheUtil.class);
	private static CacheUtil instance;
	private static int checkedOut = 0;
	/** 默认通用Cache */
	private static JCS commonCache;

	/**
	 * 构造函数 实例化全部的CATCH
	 */
	private CacheUtil() {
		try{
			commonCache = JCS.getInstance("serverCache");
		}catch (Exception e){
			logger.error("实例化CATCHE失败！");
			e.printStackTrace();
			LogUtil.saveException(logger, e);
		}
	}

	/**
	 * 取得CATCH对象 Singleton access point to the manager.
	 * @return CatchManager
	 */
	public static CacheUtil getInstance(){
		synchronized (CacheUtil.class){
			if(instance == null){
				instance = new CacheUtil();
				loadAllCache();
			}
		}
		synchronized (instance){
			CacheUtil.checkedOut++;
		}
		return instance;
	}

	/**
	 * 初始化/加载所有缓存
	 */
	public static void loadAllCache(){
		CacheTable codeTable = new CacheTable();
		try{
			//错误代码
			codeTable.initErrorCodeMapCache(commonCache);
			//地区代码
			codeTable.initAreaCodeMapCache(commonCache);
			//app信息
			codeTable.initSystemDefaultAppKey(commonCache);
			//监控页面
			codeTable.cacheModelFile(commonCache);
			//本机地址
			codeTable.cacheLocalIpAddr(commonCache);
			//读API使用的token
			codeTable.initReadTokenMap(commonCache);
			//读取cookie
			codeTable.initCookieMap(commonCache);
		}catch (Exception e){
			LogUtil.saveException(logger, e);
			e.printStackTrace();
		}
	}

	/**
	 * 取得错误代码描述
	 * @param code
	 * @param type
	 * @return
	 */
	public LinksusTaskErrorCode getErrorCode(String code){
		Map errorCodeMap = (Map) commonCache.get(Constants.CACHE_ERROR_CODE);
		LinksusTaskErrorCode errorCode = (LinksusTaskErrorCode) errorCodeMap.get(code);
		return errorCode;
	}

	/**
	 * 取得新浪地域代码描述
	 * @param code
	 * @param type
	 * @return
	 */
	public String getSinaAreaCode(String code){
		Map sinaCodeMap = (Map) commonCache.get(Constants.CACHE_SINA_AREA_CODE);
		String areaInfo = "";
		if(sinaCodeMap != null){
			if((Long) sinaCodeMap.get(code) != null){
				areaInfo = String.valueOf(sinaCodeMap.get(code));
			}
		}
		if(StringUtils.isBlank(areaInfo)){
			areaInfo = "";
		}
		return areaInfo;
	}

	/**
	 * 取得腾讯地域代码描述
	 * @param code
	 * @param type
	 * @return
	 */
	public String getTencentAreaCode(String code){
		Map tencentCodeMap = (Map) commonCache.get(Constants.CACHE_TENCENT_AREA_CODE);
		String areaInfo = "";
		if(tencentCodeMap != null){
			areaInfo = tencentCodeMap.get(code) + "";
		}
		if(StringUtils.isBlank(areaInfo)){
			areaInfo = "";
		}
		return areaInfo;
	}

	/**
	 * 根据中文名取得微信系统地区代码
	 * @param code
	 * @param type
	 * @return
	 */
	public String getWeixinAreaCode(String areaName){
		Map weixinCodeMap = (Map) commonCache.get(Constants.CACHE_WEIXIN_AREA_CODE);
		String areaInfo = "";
		if(weixinCodeMap != null && StringUtils.isNotBlank(areaName)){
			if(weixinCodeMap.containsKey(areaName)){
				areaInfo = weixinCodeMap.get(areaName) + "";
			}
		}
		return areaInfo;
	}

	/**
	 * 取得系统APP信息
	 * @return
	 */
	public AppInfo getAppInfo(String type){
		return (AppInfo) commonCache.get(type);
	}

	/**
	 * 取得当前小时内接口总连接次数 
	 * @param tablename String
	 * @param fromCatch boolean
	 * @return TreeMap
	 */
	public synchronized int getCurrHourConnCount(String limitType){
		Map countMap = null;
		String cacheName = limitType + "_COUNT";
		countMap = (Map) commonCache.get(cacheName);
		String currHour = DateUtil.getCurrDate("yyyyMMddHH");
		String maxCount = LoadConfig.getString(limitType);
		try{
			if(countMap == null){
				Map tmpMap = new HashMap();
				tmpMap.put("currHour", currHour);
				tmpMap.put("currCount", new Integer(1));
				commonCache.put(cacheName, tmpMap);
				return 1;
			}else{
				String tmpHour = (String) countMap.get("currHour");
				if(currHour.equals(tmpHour)){
					Integer icount = (Integer) countMap.get("currCount");
					if(icount.intValue() >= Integer.parseInt(maxCount)){//超出最大限制
						return -1;
					}
					countMap.put("currCount", new Integer(icount.intValue() + 1));
					if((icount.intValue() + 1) == Integer.parseInt(maxCount)){
						Map tempMap = new HashMap();
						tempMap.put("limitType", limitType);
						tempMap.put("currHour", currHour);
						tempMap.put("currTime", DateUtil.formatDate(new Date(), "HH:mm:ss"));
						TaskStatisticCommon.saveClientLimitTime(tempMap, Constants.LOCAL_IP);
						logger.info("==================服务端本小时{}操作总量达到频次限制：{}", limitType, maxCount);
					}
					return icount.intValue() + 1;
				}else{
					countMap.put("currHour", currHour);
					countMap.put("currCount", new Integer(1));
					return 1;
				}
			}
		}catch (Exception e){
			LogUtil.saveException(logger, e);
			e.printStackTrace();
		}
		return 1;
	}

	/**
	 * 判断SINA接口访问总量，如果超过限制线程将休眠等待，直至下一小时
	 */
	public void checkConnLimitPerHour(String limitType){
		int connCount = getCurrHourConnCount(limitType);
		while (connCount == -1){// 超出当前限制 线程等待1分钟
			logger.info("当前小时{}接口调用达到最大限制，等待1分钟后重试！", limitType);
			try{
				Thread.sleep(60000);
				connCount = getCurrHourConnCount(limitType);
			}catch (InterruptedException e){
				e.printStackTrace();
			}
		}
	}

	/**
	 * 判断用户新浪接口写次数，当超出限制时写入数据库中，夜间批量任务删除当天以前的超限数据
	 * @return
	 */
	public int getCurrHourWriteCountByUser(String userId,String type){
		//判断总接口限制
		int count = -1;
		String cacheName = "write_count" + type;
		Map cacheMap = (Map) commonCache.get(cacheName);
		String currHour = DateUtil.getCurrDate("yyyyMMddHH");
		String maxCount = LoadConfig.getString(type);
		try{
			if(cacheMap == null){//应用首次写
				Map userMap = new HashMap();
				Map countMap = new HashMap();
				countMap.put("currHour", currHour);
				countMap.put("currCount", new Integer(0));
				userMap.put(userId, countMap);
				commonCache.put(cacheName, userMap);
				count = 0;
			}else{
				Map countMap = (Map) cacheMap.get(userId);
				if(countMap == null){//该用户首次写
					countMap = new HashMap();
					countMap.put("currHour", currHour);
					countMap.put("currCount", new Integer(0));
					cacheMap.put(userId, countMap);
					count = 0;
				}else{
					String cacheHour = (String) countMap.get("currHour");
					if(currHour.equals(cacheHour)){
						Integer icount = (Integer) countMap.get("currCount");
						if(icount.intValue() >= Integer.parseInt(maxCount)){//超出最大限制
							count = -1;
						}else{
							count = icount.intValue();
						}
					}else{
						cacheMap.put("currHour", currHour);
						cacheMap.put("currCount", new Integer(0));
						count = 0;
					}
				}
			}
		}catch (Exception e){
			LogUtil.saveException(logger, e);
			e.printStackTrace();
		}
		return count;
	}

	/**
	 * 判断用户新浪接口写次数，当超出限制时写入数据库中，夜间批量任务删除当天以前的超限数据
	 * @return
	 */
	public int getCurrDayWriteCountByUser(String userId,String type){
		//判断总接口限制
		int count = -1;
		String cacheName = "day_write_count" + type;
		Map cacheMap = (Map) commonCache.get(cacheName);
		String currDay = DateUtil.getCurrDate("yyyyMMdd");
		String maxCount = LoadConfig.getString(type);
		try{
			if(cacheMap == null){//应用首次写
				Map userMap = new HashMap();
				Map countMap = new HashMap();
				countMap.put("currDay", currDay);
				countMap.put("currCount", new Integer(0));
				userMap.put(userId, countMap);
				commonCache.put(cacheName, userMap);
				count = 0;
			}else{
				Map countMap = (Map) cacheMap.get(userId);
				if(countMap == null){//该用户首次写
					countMap = new HashMap();
					countMap.put("currDay", currDay);
					countMap.put("currCount", new Integer(0));
					cacheMap.put(userId, countMap);
					count = 0;
				}else{
					String cacheDay = (String) countMap.get("currDay");
					if(currDay.equals(cacheDay)){
						Integer icount = (Integer) countMap.get("currCount");
						if(icount.intValue() >= Integer.parseInt(maxCount)){//超出最大限制
							count = -1;
						}else{
							count = icount.intValue();
						}
					}else{
						cacheMap.put("currDay", currDay);
						cacheMap.put("currCount", new Integer(0));
						count = 0;
					}
				}
			}
		}catch (Exception e){
			LogUtil.saveException(logger, e);
			e.printStackTrace();
		}
		return count;
	}

	/**
	 * 增加当前小时写限制次数
	 * @param userId
	 * @param type
	 */
	public void addCurrHourWriteCountByUser(String userId,String type){
		String cacheName = "write_count" + type;
		Map cacheMap = (Map) commonCache.get(cacheName);
		String currHour = DateUtil.getCurrDate("yyyyMMddHH");
		String maxCount = LoadConfig.getString(type);
		try{
			Map userMap = (Map) cacheMap.get(userId);
			String cacheHour = (String) userMap.get("currHour");
			if(currHour.equals(cacheHour)){
				Integer icount = (Integer) userMap.get("currCount");
				userMap.put("currCount", new Integer(icount.intValue() + 1));
				if((icount.intValue() + 1) == Integer.parseInt(maxCount)){//达到最大限制
					logger.info("==================用户：{}，{}本小时操作达到频次限制：{}", userId, type, maxCount);
					//写入数据库次数限制表
					String limitType = type.substring(type.length() - 1);
					LinksusTaskConnLimit limit = new LinksusTaskConnLimit();
					limit.setAppid(new Long(userId));
					limit.setLimitDate(currHour);
					limit.setLimitType(limitType);
					LinksusTaskConnLimitService limitService = (LinksusTaskConnLimitService) ContextUtil
							.getBean("linksysTaskConnLimitService");
					limitService.addLinksusTaskConnLimit(limit);
				}
			}else{
				userMap.put("currHour", currHour);
				userMap.put("currCount", new Integer(0));
			}
		}catch (Exception e){
			LogUtil.saveException(logger, e);
			e.printStackTrace();
		}
	}

	/**
	 * 增加当天写限制次数
	 * @param userId
	 * @param type
	 */
	public void addCurrDayWriteCountByUser(String userId,String type){
		String cacheName = "day_write_count" + type;
		Map cacheMap = (Map) commonCache.get(cacheName);
		String currDay = DateUtil.getCurrDate("yyyyMMdd");
		String maxCount = LoadConfig.getString(type);
		try{
			Map userMap = (Map) cacheMap.get(userId);
			String cacheDay = (String) userMap.get("currDay");
			if(currDay.equals(cacheDay)){
				Integer icount = (Integer) userMap.get("currCount");
				userMap.put("currCount", new Integer(icount.intValue() + 1));
				if((icount.intValue() + 1) == Integer.parseInt(maxCount)){//达到最大限制
					logger.info("==================用户：{}，{}当日操作达到频次限制：{}", userId, type, maxCount);
					//写入数据库次数限制表
					String limitType = type.substring(type.length() - 1);
					LinksusTaskConnLimit limit = new LinksusTaskConnLimit();
					limit.setAppid(new Long(userId));
					limit.setLimitDate(currDay);
					limit.setLimitType(limitType);
					LinksusTaskConnLimitService limitService = (LinksusTaskConnLimitService) ContextUtil
							.getBean("linksysTaskConnLimitService");
					limitService.addLinksusTaskConnLimit(limit);
				}
			}else{
				userMap.put("currDay", currDay);
				userMap.put("currCount", new Integer(0));
			}
		}catch (Exception e){
			LogUtil.saveException(logger, e);
			e.printStackTrace();
		}
	}

	public Object getCache(String cacheName){
		return commonCache.get(cacheName);
	}

	/**
	 * 设置cache
	 * @param cacheKey catche编码
	 * @param obj Object
	 */
	public void putCache(String cacheKey,Object obj){
		try{
			// commonCatch.remove(catchKey);
			commonCache.put(cacheKey, obj);
		}catch (CacheException e){
			e.printStackTrace();
		}
	}

	public static void clearCache() throws CacheException{
		commonCache.clear();
	}

	/**
	 * 取得Appaccount
	 * @return
	 * @throws CacheException 
	 */
	public Map<String, LinksusAppaccount> getAccountTokenMap() throws CacheException{
		Map<String, LinksusAppaccount> obj = (Map<String, LinksusAppaccount>) commonCache
				.get(Constants.TASK_APP_ACCOUNT);
		return obj;
	}

}
