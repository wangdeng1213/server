package com.linksus.interfaces;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.jcs.access.exception.CacheException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linksus.common.Constants;
import com.linksus.common.config.LoadConfig;
import com.linksus.common.module.RelationUserAccountCommon;
import com.linksus.common.module.WeiboUserCommon;
import com.linksus.common.util.ContextUtil;
import com.linksus.common.util.HttpUtil;
import com.linksus.common.util.LogUtil;
import com.linksus.common.util.StringUtil;
import com.linksus.entity.LinksusAppaccount;
import com.linksus.entity.LinksusRelationWeibouser;
import com.linksus.entity.LinksusTaskErrorCode;
import com.linksus.entity.UrlEntity;
import com.linksus.service.LinksusRelationGroupService;
import com.linksus.service.LinksusRelationPersonGroupService;
import com.linksus.service.LinksusRelationWeibouserService;

/**
 * 搜索的用户与账号的关系接口
 */
public class TaskSearchUserAddInterface extends BaseInterface{

	protected static final Logger logger = LoggerFactory.getLogger(TaskSearchUserAddInterface.class);
	/** 缓存对象 */
	private LinksusRelationWeibouserService relationWeiboUserService = (LinksusRelationWeibouserService) ContextUtil
			.getBean("linksusRelationWeibouserService");
	private LinksusRelationGroupService linksusRelationGroupService = (LinksusRelationGroupService) ContextUtil
			.getBean("linksusRelationGroupService");
	private LinksusRelationPersonGroupService linksusRelationPersonGroupService = (LinksusRelationPersonGroupService) ContextUtil
			.getBean("linksusRelationPersonGroupService");

	public Map cal(Map paramsMap) throws Exception{
		TaskSearchUserAddInterface task = new TaskSearchUserAddInterface();
		Map rsMap = task.searchUserDealInterface(paramsMap);
		return rsMap;
	}

	// 前台接口调用方法
	public Map searchUserDealInterface(Map paraMap) throws Exception{
		Map rsMap = new HashMap();
		String rpsId = (String) paraMap.get("rpsId");
		String userType = (String) paraMap.get("userType");
		String institutionId = (String) paraMap.get("institutionId");
		// 查询微博用户
		Map tokenMap = getAccountTokenMap(userType);
		// 根据rpsId和userType 判断用户是否存在
		Map parasMap = new HashMap();
		parasMap.put("rpsId", rpsId);
		parasMap.put("userType", userType);
		LinksusRelationWeibouser user = relationWeiboUserService.getWeibouserByrpsIdAndType(parasMap);
		if(StringUtils.equals(userType, "1")){// 用户等于1从新浪取出用户信息
			if(user == null){// 用户不存在 从平台查询用户的详细信息
				String strData = getWeiboUserDataFromSina(paraMap, tokenMap);// 新浪返回结果
				LinksusTaskErrorCode error = StringUtil.parseSinaErrorCode(strData);
				if(error != null){
					rsMap.put("errcode", error.getErrorCode().toString());
				}else{
					WeiboUserCommon weiboUserCommon = new WeiboUserCommon();
					user = weiboUserCommon.dealWeiboUserInfo(strData, "2", tokenMap, false, "0", 0);
					dealGroupInfo(user, institutionId);
				}
			}else{
				dealGroupInfo(user, institutionId);
			}
		}else if(StringUtils.equals(userType, "2")){// 用户类型等于2从腾讯取出数据
			if(user == null){// 用户不存在 从平台查询用户的详细信息
				String strData = getWeiboUserDataFromTencent(paraMap, tokenMap);
				LinksusTaskErrorCode error = StringUtil.parseTencentErrorCode(strData);
				if(error != null){
					rsMap.put("errcode", error.getErrorCode());
				}else{
					WeiboUserCommon weiboUserCommon = new WeiboUserCommon();
					user = weiboUserCommon.dealWeiboUserInfo(strData, "2", tokenMap, false, "0", 0);
					dealGroupInfo(user, institutionId);
				}
			}else{
				dealGroupInfo(user, institutionId);
			}
		}
		if(user != null){
			rsMap.put("person_id", user.getPersonId());
		}
		return rsMap;
	}

	protected Map getAccountTokenMap(String accountType){
		Map tokenMap = null;
		try{
			Map accountMap = cache.getAccountTokenMap();
			LinksusAppaccount tokenObj = (LinksusAppaccount) accountMap.get(accountType);
			if(tokenObj != null){// 无相应授权
				tokenMap = new HashMap();
				tokenMap.put("token", tokenObj.getToken());
				tokenMap.put("openid", tokenObj.getAppid());
				tokenMap.put("type", accountType);
				tokenMap.put("appkey", tokenObj.getAppKey());
			}
		}catch (CacheException e){
			LogUtil.saveException(logger, e);
		}
		return tokenMap;
	}

	// 根据机构 微博用户处理我添加的分组信息
	private void dealGroupInfo(LinksusRelationWeibouser user,String institutionId) throws Exception{
		if(user != null){
			Long id = new Long(institutionId);
			RelationUserAccountCommon.dealPersonGroup(user.getPersonId(), id, "2");//加入未分组
			RelationUserAccountCommon.dealPersonGroup(user.getPersonId(), id, "4");//加入我的添加
		}
	}

	// 获取用户的基本信息 从腾讯
	public String getWeiboUserDataFromTencent(Map parmMap,Map tokenMap){
		// 通过配置文件从腾讯接口取最新的微博数据
		UrlEntity strTencentUrl = LoadConfig.getUrlEntity("TCUerInfo");
		String strjson = "";
		Map paraMap = new HashMap();
		paraMap.put("oauth_consumer_key", cache.getAppInfo(Constants.CACHE_TENCENT_APPINFO).getAppKey());
		paraMap.put("clientip", Constants.TENCENT_CLIENT_IP);
		paraMap.put("oauth_version", "2.a");
		paraMap.put("format", "json");
		paraMap.put("openid", tokenMap.get("openid"));
		paraMap.put("access_token", tokenMap.get("token"));
		if(!StringUtils.isBlank((String) parmMap.get("rpsId"))){
			paraMap.put("fopenid", parmMap.get("rpsId"));
		}
		if(!StringUtils.isBlank((String) parmMap.get("name"))){
			paraMap.put("name", parmMap.get("name"));
		}
		strjson = HttpUtil.getRequest(strTencentUrl, paraMap);
		return strjson;
	}

	// 获取用户的基本信息 从新浪
	public String getWeiboUserDataFromSina(Map parmMap,Map tokenMap){
		String strjson = "";
		UrlEntity strUrl = LoadConfig.getUrlEntity("fensiinfo");
		parmMap.get("rpsId");
		Map paraMap = new HashMap();
		paraMap.put("access_token", tokenMap.get("token"));
		paraMap.put("uid", parmMap.get("rpsId"));
		strjson = HttpUtil.getRequest(strUrl, paraMap);
		return strjson;
	}
}
