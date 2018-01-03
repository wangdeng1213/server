package com.linksus.common.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.jcs.JCS;
import org.apache.jcs.access.exception.CacheException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linksus.common.Constants;
import com.linksus.common.config.LoadConfig;
import com.linksus.entity.AppInfo;
import com.linksus.entity.LinksusAppaccount;
import com.linksus.entity.LinksusTaskErrorCode;
import com.linksus.entity.LinksusTypeAreaCode;
import com.linksus.mina.WhitelistFilter;
import com.linksus.service.LinksusAppaccountService;
import com.linksus.service.LinksusTaskErrorCodeService;
import com.linksus.service.LinksusTypeAreaCodeService;

public class CacheTable{

	protected static final Logger logger = LoggerFactory.getLogger(CacheTable.class);
	private LinksusTaskErrorCodeService errorCodeService = (LinksusTaskErrorCodeService) ContextUtil
			.getBean("linksusTaskErrorCodeService");
	private LinksusTypeAreaCodeService areaCodeService = (LinksusTypeAreaCodeService) ContextUtil
			.getBean("linksusTypeAreaCodeService");
	private LinksusAppaccountService accountService = (LinksusAppaccountService) ContextUtil
			.getBean("linksusAppaccountService");
	private LinksusAppaccountService appaccountService = (LinksusAppaccountService) ContextUtil
			.getBean("linksusAppaccountService");

	/**
	 * 缓存错误代码
	 * @param commonCache
	 * @return
	 */
	public boolean initErrorCodeMapCache(JCS commonCache){
		try{
			Map<String, LinksusTaskErrorCode> cacheMap = new HashMap<String, LinksusTaskErrorCode>();
			Map<String, LinksusTaskErrorCode> sinaCacheMap = new HashMap<String, LinksusTaskErrorCode>();
			Map<String, LinksusTaskErrorCode> tencentCacheMap = new HashMap<String, LinksusTaskErrorCode>();
			List<LinksusTaskErrorCode> list = errorCodeService.getLinksusTaskErrorCodeList();
			for(LinksusTaskErrorCode code : list){
				String mapKey = code.getErrorCode().toString();
				cacheMap.put(mapKey, code);
				if("1".equals(code.getErrorType().toString())){
					sinaCacheMap.put(code.getSrcCode(), code);
				}else if("2".equals(code.getErrorType().toString())){
					tencentCacheMap.put(code.getSrcCode(), code);
				}
			}
			commonCache.put(Constants.CACHE_ERROR_CODE, cacheMap);
			commonCache.put(Constants.CACHE_ERROR_CODE_SINA, sinaCacheMap);
			commonCache.put(Constants.CACHE_ERROR_CODE_TENCENT, tencentCacheMap);
		}catch (CacheException e){
			LogUtil.saveException(logger, e);
			return false;
		}
		return true;
	}

	/**
	 * 缓存新浪、腾讯地理信息
	 * @param commonCache
	 * @return
	 */
	public boolean initAreaCodeMapCache(JCS commonCache){
		try{
			Map<String, Long> cacheSinaMap = new HashMap<String, Long>();
			Map<String, Long> cacheTencentMap = new HashMap<String, Long>();
			Map<String, Long> cacheWeixinMap = new HashMap<String, Long>();
			List<LinksusTypeAreaCode> list = areaCodeService.getLinksusTypeAreaCodeList();
			for(LinksusTypeAreaCode code : list){
				cacheSinaMap.put(code.getSinaAreaCode(), code.getAreaCode());
				cacheTencentMap.put(code.getTencentAreaCode(), code.getAreaCode());
				cacheWeixinMap.put(code.getAreaName(), code.getAreaCode());
			}
			commonCache.put(Constants.CACHE_SINA_AREA_CODE, cacheSinaMap);
			commonCache.put(Constants.CACHE_TENCENT_AREA_CODE, cacheTencentMap);
			commonCache.put(Constants.CACHE_WEIXIN_AREA_CODE, cacheWeixinMap);
		}catch (Exception e){
			LogUtil.saveException(logger, e);
			return false;
		}
		return true;
	}

	/**
	 * 缓存系统初始化app_key
	 * @param commonCache
	 * @return
	 */
	public boolean initSystemDefaultAppKey(JCS commonCache){
		try{
			List list = accountService.getSystemDefaultAppKey();
			for(int i = 0; i < list.size(); i++){
				Map map = (Map) list.get(i);
				AppInfo appInfo = new AppInfo();
				appInfo.setAppKey((String) map.get("app_key"));
				appInfo.setAppSecret((String) map.get("app_secret"));
				appInfo.setName((String) map.get("name"));
				appInfo.setType(map.get("type") + "");
				if("1".equals(map.get("type") + "")){//新浪
					appInfo.setPsssword(LoadConfig.getString("sinaPassword"));
					commonCache.put(Constants.CACHE_SINA_APPINFO, appInfo);
				}else if("2".equals(map.get("type") + "")){//腾讯
					commonCache.put(Constants.CACHE_TENCENT_APPINFO, appInfo);
				}
			}
		}catch (Exception e){
			LogUtil.saveException(logger, e);
			return false;
		}
		return true;
	}

	public void cacheModelFile(JCS commonCache) throws Exception{
		InputStream in = CacheTable.class.getClassLoader().getResourceAsStream("config/model/taskStatistic.vm");
		BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
		StringBuffer buffer = new StringBuffer();
		while (true){
			String line = br.readLine();
			if(line == null){
				break;
			}
			buffer.append(line).append("\n");
		}
		br.close();
		commonCache.put(Constants.CACHE_TASK_STATISTIC_MODEL, buffer.toString());
	}

	/**
	 * 获取本机IP地址
	 */
	public void cacheLocalIpAddr(JCS commonCache) throws Exception{
		List list = new ArrayList();
		Enumeration allNetInterfaces = NetworkInterface.getNetworkInterfaces();
		InetAddress ip = null;
		while (allNetInterfaces.hasMoreElements()){
			NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
			Enumeration addresses = netInterface.getInetAddresses();
			while (addresses.hasMoreElements()){
				ip = (InetAddress) addresses.nextElement();
				if(ip != null && ip instanceof Inet4Address){
					String ipAddr = ip.getHostAddress();
					if(!"127.0.0.1".equals(ipAddr)){
						list.add(ipAddr);
					}
				}
			}
		}
		commonCache.put(Constants.CACHE_LOCAL_IP_ADDR, JsonUtil.list2json(list));
	}

	/** 取得读操作token
	 * @throws CacheException */
	public void initReadTokenMap(JCS commonCache) throws CacheException{
		Map<String, LinksusAppaccount> appaccount = appaccountService.getAccountTokenMap();
		commonCache.put(Constants.TASK_APP_ACCOUNT, appaccount);
	}

	/**
	 * 缓存Cookie
	 * @param commonCache
	 */
	public void initCookieMap(JCS commonCache) throws Exception{
		Map map = new HashMap();
		InputStream is = WhitelistFilter.class.getResourceAsStream("/config/cookie.txt");
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		while (true){
			String line = br.readLine();
			if(line == null){
				break;
			}
			map.put(line.substring(0, line.indexOf(":")), line.substring(line.indexOf(":") + 1));
		}
		br.close();
		commonCache.put(Constants.CACHE_COOKIE_MAP, map);
	}
}
