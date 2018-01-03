package com.linksus.interfaces;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.jcs.access.exception.CacheException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linksus.common.Constants;
import com.linksus.common.config.LoadConfig;
import com.linksus.common.module.CopyMapToBean;
import com.linksus.common.module.WeiboUserCommon;
import com.linksus.common.util.ContextUtil;
import com.linksus.common.util.DateUtil;
import com.linksus.common.util.HttpUtil;
import com.linksus.common.util.JsonUtil;
import com.linksus.common.util.LogUtil;
import com.linksus.common.util.PrimaryKeyGen;
import com.linksus.common.util.RedisUtil;
import com.linksus.common.util.StringUtil;
import com.linksus.common.util.WeiboUtil;
import com.linksus.entity.LinksusAppaccount;
import com.linksus.entity.LinksusRelationWeibouser;
import com.linksus.entity.LinksusTaskErrorCode;
import com.linksus.entity.LinksusTaskIncrementUser;
import com.linksus.entity.LinksusWeibo;
import com.linksus.entity.UrlEntity;
import com.linksus.queue.QueueFactory;
import com.linksus.service.LinksusAppaccountService;
import com.linksus.service.LinksusPersonaccountService;
import com.linksus.service.LinksusTaskIncrementUserService;
import com.linksus.service.LinksusWeiboService;
import com.linksus.task.TaskSinaInteractDataRead;
import com.linksus.task.TaskTencentInteractDataRead;

/**
 * 更新用户的基本信息
 */
public class TaskUpdateWeiboUserInterface extends BaseInterface{

	protected static final Logger logger = LoggerFactory.getLogger(TaskUpdateWeiboUserInterface.class);
	/** 缓存对象 */
	private LinksusAppaccountService linksusAppaccountService = (LinksusAppaccountService) ContextUtil
			.getBean("linksusAppaccountService");
	private LinksusTaskIncrementUserService increUserService = (LinksusTaskIncrementUserService) ContextUtil
			.getBean("linksusTaskIncrementUserService");
	private LinksusWeiboService linksusWeiboService = (LinksusWeiboService) ContextUtil.getBean("linksusWeiboService");

	private LinksusPersonaccountService linksusPersonaccountService = (LinksusPersonaccountService) ContextUtil
			.getBean("linksusPersonaccountService");

	private String jobType;

	public String getJobType(){
		return jobType;
	}

	public void setJobType(String jobType){
		this.jobType = jobType;
	}

	// 6 获取微博用户的详细信息 17根据名称查询新浪/腾讯用户信息
	public Map cal(Map paramsMap) throws Exception{
		TaskUpdateWeiboUserInterface task = new TaskUpdateWeiboUserInterface();
		Map info = null;
		try{
			if(jobType.equals("6")){
				info = task.updateWeiboUserInterface(paramsMap);
			}
			if(jobType.equals("17")){
				String userType = (String) paramsMap.get("userType");
				String name = (String) paramsMap.get("userName");
				info = task.getWeiboUserInfoInterface(userType, name);
			}
		}catch (Exception e){
			throw e;
		}
		return info;
	}

	// 前台接口调用方法
	public Map updateWeiboUserInterface(Map paraMap) throws Exception{
		Map rsMap = new HashMap();
		String rpsId = (String) paraMap.get("rpsId");
		String name = (String) paraMap.get("name");
		String accountType = (String) paraMap.get("accountType");
		String authorize = (String) paraMap.get("authorize");// 是否是新增用户
		if(StringUtils.isBlank(rpsId) && StringUtils.isBlank(name)){
			rsMap.put("errcode", "10120");
			return rsMap;// rpsId name 必须有一个存在
		}
		// 查询微博用户
		// 关联查询更新到增量任务表中
		LinksusAppaccount linksusAppaccount = null;
		if(StringUtils.equals(authorize, "1")){
			linksusAppaccount = linksusAppaccountService.getAppaccountByIdOrName(paraMap);
			if(linksusAppaccount == null){
				rsMap.put("errcode", "10130");
				return rsMap;// 平台账号不存在
			}else{
				// 向增量任务表中插入数据
				// 判断增量表中是否存在
				Integer increcount = increUserService.getCountByaccountId(linksusAppaccount.getId());
				if(increcount == 0){
					LinksusTaskIncrementUser increUser = new LinksusTaskIncrementUser();
					increUser.setRpsId(rpsId);
					increUser.setToken(linksusAppaccount.getToken());
					increUser.setAccountId(linksusAppaccount.getId());
					increUser.setAppType(Integer.valueOf(accountType));
					increUser.setInstitutionId(linksusAppaccount.getInstitutionId());
					increUser.setInitialFansNum(0);
					increUser.setLastFansNum(0);
					increUser.setUpdtFansTime(0);
					increUser.setInitialFollowNum(0);
					increUser.setLastFollowNum(0);
					increUser.setUpdtFollowTime(0);
					increUser.setIsSystemUser(1);
					increUserService.insertLinksusTaskIncrementUser(increUser);

					//新授权用户即时加入增量粉丝/增量关注队列,保证新授权用户的关系数据即时更新
					try{
						if(StringUtils.equals(accountType, "1")){
							QueueFactory.addQueueTaskData("Queue011", increUser);
							QueueFactory.addQueueTaskData("Queue007", increUser);
						}else{
							QueueFactory.addQueueTaskData("Queue012", increUser);
							QueueFactory.addQueueTaskData("Queue008", increUser);
						}
					}catch (Exception e){
						LogUtil.saveException(logger, e);
					}

				}
			}
		}
		Map tokenMap = getAccountTokenMap(accountType);
		if(StringUtils.equals(accountType, "1")){
			// 通过accountType查询列表 通过 新浪 更新用户 数据
			String strData = getWeiboUserDataFromSina(paraMap, tokenMap);// 新浪返回结果
			LinksusTaskErrorCode error = StringUtil.parseSinaErrorCode(strData);
			if(error != null){
				rsMap.put("errcode", error.getErrorCode());
			}else{
				WeiboUserCommon weiboUserCommon = new WeiboUserCommon();
				weiboUserCommon.dealWeiboUserInfo(strData, "2", tokenMap, true, "0", 0);
			}
		}else if(StringUtils.equals(accountType, "2")){
			String strData = getWeiboUserDataFromTencent(paraMap, tokenMap);// 腾讯返回结果
			LinksusTaskErrorCode error = StringUtil.parseTencentErrorCode(strData);
			if(error != null){
				rsMap.put("errcode", error.getErrorCode());
			}else{
				WeiboUserCommon weiboUserCommon = new WeiboUserCommon();
				weiboUserCommon.dealWeiboUserInfo(strData, "2", tokenMap, true, "0", 0);
			}
		}
		// 最后判断抓取微博发布数据
		if(StringUtils.equals(authorize, "1")){
			// 判断账号存不存在
			Long accountId = linksusAppaccount.getId();
			Integer appCount = linksusWeiboService.getCountByaccount(accountId);
			if(appCount == 0){
				String errorCode = insertWeiboExists(linksusAppaccount);
				if(!StringUtils.isBlank(errorCode)){
					rsMap.put("errcode", errorCode);
				}
			}
		}
		return rsMap;
	}

	private String insertWeiboExists(LinksusAppaccount linksusAppaccount) throws Exception{
		// 判断该用户微博发布表是否存在数据(只首次授权抓取微博数据)
		String weiboCount = LoadConfig.getString("weiboCount");
		Map managerMap = linksusPersonaccountService.getManagerByInstitutionId(linksusAppaccount.getInstitutionId());
		if(linksusAppaccount.getType() == 1){// 新浪  
			UrlEntity strurl = LoadConfig.getUrlEntity("WeiBoData");
			Map mapPara = new HashMap();
			mapPara.put("access_token", linksusAppaccount.getToken());
			mapPara.put("uid", linksusAppaccount.getAppid());
			mapPara.put("trim_user", 1);
			mapPara.put("count", weiboCount);
			String rsData = HttpUtil.getRequest(strurl, mapPara);
			LinksusTaskErrorCode error = StringUtil.parseSinaErrorCode(rsData);
			if(error != null){
				return error.getErrorCode().toString();
			}
			rsData = JsonUtil.getNodeByName(rsData, "statuses");
			List weiboList = JsonUtil.json2list(rsData, Map.class);
			if(weiboList == null || weiboList.size() == 0){
				return null;
			}
			for(int i = 0; i < weiboList.size(); i++){
				Map map = (Map) weiboList.get(i);
				// 插入到 linksus_weibo数据表中
				LinksusWeibo linksusWeibo = new LinksusWeibo();
				Long id = PrimaryKeyGen.getSequencesPrimaryKey("linksus_weibo", "weibo_id");
				linksusWeibo.setId(id);
				linksusWeibo.setInstitutionId(linksusAppaccount.getInstitutionId());
				linksusWeibo.setAccountId(linksusAppaccount.getId());
				linksusWeibo.setAccountName(linksusAppaccount.getAccountName());
				linksusWeibo.setAccountType(1);
				linksusWeibo.setAuthorId(new Long(managerMap.get("id").toString()));
				linksusWeibo.setAuthorName(managerMap.get("name").toString());
				copyMapToLinksusWeibo(map, linksusWeibo, linksusAppaccount);
				if(!StringUtils.isBlank(linksusAppaccount.getAppid())
						&& !StringUtils.isBlank(linksusWeibo.getMid().toString())){
					String currUrl = "http://weibo.com/" + linksusAppaccount.getAppid() + "/"
							+ WeiboUtil.Id2Mid(linksusWeibo.getMid());
					linksusWeibo.setCurrentUrl(currUrl);
				}else{
					linksusWeibo.setCurrentUrl("");
				}
				// 插入到微博发布表中
				linksusWeiboService.insertLinksusWeibo(linksusWeibo);
			}
		}else if(linksusAppaccount.getType() == 2){// 腾讯
			Map paraMap = new HashMap();
			paraMap.put("access_token", linksusAppaccount.getToken());
			paraMap.put("oauth_consumer_key", cache.getAppInfo(Constants.CACHE_TENCENT_APPINFO).getAppKey());
			paraMap.put("openid", linksusAppaccount.getAppid());
			paraMap.put("clientip", Constants.TENCENT_CLIENT_IP);
			paraMap.put("oauth_version", "2.a");
			paraMap.put("format", "json");
			paraMap.put("pageflag", "0");
			paraMap.put("pagetime", "0");
			paraMap.put("reqnum", weiboCount);
			paraMap.put("lastid", "0");
			paraMap.put("type", "3");
			paraMap.put("contenttype", "0");
			UrlEntity strurl = LoadConfig.getUrlEntity("TCWeiboList");// 取出最近发送的微博
			String rsData = HttpUtil.postRequest(strurl, paraMap);
			LinksusTaskErrorCode error = StringUtil.parseTencentErrorCode(rsData);
			if(error != null){
				return error.getErrorCode().toString();
			}
			//校验data是否为空
			String rsRealData = JsonUtil.getNodeByName(rsData, "data");
			if(StringUtils.isBlank(rsRealData) || StringUtils.equals(rsRealData, "null")){
				return null;
			}
			//校验info是否为空
			String infoData = JsonUtil.getNodeByName(rsRealData, "info");
			if(StringUtils.isBlank(infoData) || StringUtils.equals(infoData, "null")){
				return null;
			}
			List weiboList = JsonUtil.json2list(infoData, Map.class);
			if(weiboList == null || weiboList.size() == 0){
				return null;
			}
			for(int i = 0; i < weiboList.size(); i++){
				Map map = (Map) weiboList.get(i);
				// 插入到 linksus_weibo数据表中
				LinksusWeibo linksusWeibo = new LinksusWeibo();
				Long id = PrimaryKeyGen.getSequencesPrimaryKey("linksus_weibo", "weibo_id");
				linksusWeibo.setId(id);
				linksusWeibo.setInstitutionId(linksusAppaccount.getInstitutionId());
				linksusWeibo.setAccountId(linksusAppaccount.getId());
				linksusWeibo.setAccountName(linksusAppaccount.getAccountName());
				linksusWeibo.setAccountType(2);
				linksusWeibo.setAuthorId(new Long(managerMap.get("id").toString()));
				linksusWeibo.setAuthorName(managerMap.get("name").toString());
				copyTCMapToLinksusWeibo(map, linksusWeibo, linksusAppaccount);
				linksusWeibo.setCurrentUrl("http://t.qq.com/p/t/" + new Long(map.get("id").toString()));
				linksusWeiboService.insertLinksusWeibo(linksusWeibo);
			}
		}
		return null;
	}

	public void copyTCMapToLinksusWeibo(Map weiboMap,LinksusWeibo linksusWeibo,LinksusAppaccount linksusAppaccount){
		Integer timeTemp = Integer.parseInt(String.valueOf(weiboMap.get("timestamp")));
		linksusWeibo.setCreatedTime(timeTemp);
		linksusWeibo.setSendTime(timeTemp);
		linksusWeibo.setContent((String) weiboMap.get("text"));
		String originalUrl = "";
		String middleUrl = "";
		String thumbUrl = "";
		List<String> listPic = (List<String>) weiboMap.get("image");
		if(listPic != null && listPic.size() > 0){
			for(int i = 0; i < listPic.size(); i++){
				String imagePic = listPic.get(i);
				originalUrl += imagePic + "/2000|";
				middleUrl += imagePic + "/460|";
				thumbUrl += imagePic + "/120|";
			}
		}
		// 原图2000
		linksusWeibo.setPicOriginalUrl(StringUtils.isBlank(originalUrl) ? "0" : originalUrl.substring(0, originalUrl
				.length() - 1));
		// 中图460
		linksusWeibo.setPicMiddleUrl(StringUtils.isBlank(middleUrl) ? "0" : middleUrl.substring(0,
				middleUrl.length() - 1));
		// 小图120
		linksusWeibo.setPicThumbUrl(StringUtils.isBlank(thumbUrl) ? "0" : thumbUrl.substring(0, thumbUrl.length() - 1));
		// 音频地址
		Map musicMap = (Map) weiboMap.get("music");
		if(musicMap != null){
			String musicUrl = (String) musicMap.get("url");
			linksusWeibo.setMusicUrl(musicUrl);
		}else{
			linksusWeibo.setMusicUrl("");
		}
		// 视频地址
		Map vedioMap = (Map) weiboMap.get("video");
		if(musicMap != null){
			String wedioUrl = (String) musicMap.get("realurl");
			linksusWeibo.setVideoUrl(wedioUrl);
		}else{
			linksusWeibo.setVideoUrl("");
		}
		linksusWeibo.setStatus(3);
		linksusWeibo.setApplyStatus("11");
		linksusWeibo.setPublishStauts(0);
		// 判断直发转发
		linksusWeibo.setContentType("00000");
		linksusWeibo.setToFile(0);
		Map<?, ?> source = (Map<?, ?>) weiboMap.get("source");// 被转发的微博信息字段
		if(source != null){
			// linksusWeibo.setSrcid(new Long(pmid));
			// linksusWeibo.setSrcurl("");
			String srcid = (String) source.get("id") + "";
			linksusWeibo.setSrcid(new Long(srcid));
			linksusWeibo.setSrcurl("http://t.qq.com/p/t/" + new Long(source.get("id").toString()));
			//判断微博池中是否 存在
			LinksusRelationWeibouser user = null;
			Map tencentparaMap = new HashMap();
			tencentparaMap.put("oauth_consumer_key", cache.getAppInfo(Constants.CACHE_TENCENT_APPINFO).getAppKey());
			tencentparaMap.put("clientip", Constants.TENCENT_CLIENT_IP);
			tencentparaMap.put("oauth_version", "2.a");
			tencentparaMap.put("format", "json");
			tencentparaMap.put("access_token", linksusAppaccount.getToken());
			tencentparaMap.put("openid", linksusAppaccount.getAppid());
			tencentparaMap.put("fopenid", weiboMap.get("openid").toString());
			String strjson = HttpUtil.getRequest(LoadConfig.getUrlEntity("TCUerInfo"), tencentparaMap);
			LinksusTaskErrorCode error = StringUtil.parseTencentErrorCode(strjson);
			if(error == null){
				//
				Map tokenMap = new HashMap();
				tokenMap.put("type", 2);
				WeiboUserCommon weibouser = new WeiboUserCommon();
				try{
					user = weibouser.dealWeiboUserInfo(strjson, "2", tokenMap, false, "0", 0);
					String weiboRedis = RedisUtil.getRedisHash("weibo_pool", srcid + "#" + "2");
					if(StringUtils.isBlank(weiboRedis)){//不存在插入 
						TaskTencentInteractDataRead tencentInte = new TaskTencentInteractDataRead();
						tencentInte.checkWeiboIsExsit(new Long(srcid), "2", source, user, new Integer(1));
					}
				}catch (Exception e){
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			linksusWeibo.setPublishType(1);
		}else{
			linksusWeibo.setSrcid(0L);
			linksusWeibo.setSrcurl("");
			linksusWeibo.setPublishType(0);
		}
		//linksusWeibo.setPublishType((Integer) weiboMap.get("self") == 0 ? 1 : 0);
		linksusWeibo.setMid(new Long((String) weiboMap.get("id")));
		linksusWeibo.setRepostCount(Integer.valueOf(weiboMap.get("count") + ""));
		linksusWeibo.setCommentCount(Integer.valueOf(weiboMap.get("mcount") + ""));
		linksusWeibo.setSourceId(2L);
		linksusWeibo.setSourceName("腾讯微博");
	}

	public void copyMapToLinksusWeibo(Map weiboMap,LinksusWeibo linksusWeibo,LinksusAppaccount linksusAppaccount){
		Date createTime = DateUtil.parse(weiboMap.get("created_at").toString(), "EEE MMM dd HH:mm:ss zzz yyyy",
				Locale.US);
		Integer timeTemp = Integer.valueOf(String.valueOf(createTime.getTime() / 1000));
		linksusWeibo.setCreatedTime(timeTemp);
		linksusWeibo.setSendTime(timeTemp);
		linksusWeibo.setContent((String) weiboMap.get("text"));
		String originalUrl = "";
		String middleUrl = "";
		String thumbUrl = "";
		List<Map> listPic = (List<Map>) weiboMap.get("pic_urls");
		if(listPic != null && listPic.size() > 0){
			for(int i = 0; i < listPic.size(); i++){
				Map imagePicMap = (Map) listPic.get(i);
				String imagePic = (String) imagePicMap.get("thumbnail_pic");
				originalUrl += imagePic.replace("/thumbnail/", "/large/") + "|";
				middleUrl += imagePic.replace("/thumbnail/", "/bmiddle/") + "|";
				thumbUrl += imagePic + "|";
			}
		}
		// 原图
		linksusWeibo.setPicOriginalUrl(StringUtils.isBlank(originalUrl) ? "0" : originalUrl.substring(0, originalUrl
				.length() - 1));
		// 中图
		linksusWeibo.setPicMiddleUrl(StringUtils.isBlank(middleUrl) ? "0" : middleUrl.substring(0,
				middleUrl.length() - 1));
		// 小图
		linksusWeibo.setPicThumbUrl(StringUtils.isBlank(thumbUrl) ? "0" : thumbUrl.substring(0, thumbUrl.length() - 1));
		linksusWeibo.setMusicUrl("");
		linksusWeibo.setVideoUrl("");
		linksusWeibo.setStatus(3);
		linksusWeibo.setApplyStatus("11");
		linksusWeibo.setPublishStauts(0);
		// 判断直发转发
		Map<?, ?> retweetedStatus = (Map<?, ?>) weiboMap.get("retweeted_status");// 被转发的微博信息字段
		Map tmpMap = new HashMap();
		String tmpId = "0";
		String pmid = "0";
		if(retweetedStatus != null){
			String deleteFlag = (String) retweetedStatus.get("deleted");
			if(!StringUtils.equals(deleteFlag, "1")){
				tmpMap = (Map) retweetedStatus.get("user");// 被转发原微博用户信息
				if(tmpMap != null && tmpMap.size() > 0){
					tmpId = (String) tmpMap.get("idstr"); // 被转发原微博用户id
				}
				pmid = (String) retweetedStatus.get("idstr");// 被转发的微博id
				if(StringUtils.isNotBlank(pmid)){
					TaskSinaInteractDataRead ss = new TaskSinaInteractDataRead();
					LinksusRelationWeibouser user;
					try{
						user = ss.getWeiboUserInfo(tmpMap, linksusAppaccount, false, 0);
						ss.checkOldWeiboIsExsit(linksusAppaccount.getToken(), new Long(pmid), "", user);
					}catch (Exception e){
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		linksusWeibo.setContentType("00000");
		linksusWeibo.setToFile(0);
		linksusWeibo.setSrcid(new Long(pmid));
		if(!"0".equals(tmpId) && !"0".equals(pmid)){
			String currUrl = "http://weibo.com/" + tmpId + "/" + WeiboUtil.Id2Mid(new Long(pmid));
			linksusWeibo.setSrcurl(currUrl);
			linksusWeibo.setPublishType(1);
		}else{
			linksusWeibo.setSrcurl("");
			linksusWeibo.setPublishType(0);
		}
		linksusWeibo.setMid(new Long(weiboMap.get("id") + ""));
		linksusWeibo.setRepostCount(Integer.valueOf(weiboMap.get("reposts_count") + ""));
		linksusWeibo.setCommentCount(Integer.valueOf(weiboMap.get("comments_count") + ""));
		linksusWeibo.setSourceId(1L);
		linksusWeibo.setSourceName("新浪微博");
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
		// String parms = "access_token=" + "2.00NaVf3BeE2s5B3e00b5f459JTMBjB" +
		// "&uid="
		// + "1251105081";
		Map paraMap = new HashMap();
		String name = (String) parmMap.get("name");
		String rpsId = (String) parmMap.get("rpsId");
		paraMap.put("access_token", tokenMap.get("token"));
		if(!StringUtils.isBlank(rpsId)){
			paraMap.put("uid", parmMap.get("rpsId"));
		}
		if(!StringUtils.isBlank(name)){
			paraMap.put("screen_name", parmMap.get("name"));
		}
		strjson = HttpUtil.getRequest(strUrl, paraMap);
		return strjson;
	}

	/**
	 * 通过名称查询新浪/腾讯用户信息
	 * 
	 * @param userType
	 * @param name
	 * @return
	 */
	public Map getWeiboUserInfoInterface(String userType,String name){
		Map rsMap = new HashMap();
		List list = new ArrayList();
		Map paraMap = new HashMap();
		paraMap.put("name", name);// 用户昵称
		if(StringUtils.equals(userType, "1") || userType == null){
			// 判断用户是否存在
			// 通过accountType查询列表 通过 新浪 更新用户 数据
			Map tokenMap = getAccountTokenMap("1");
			String strData = getWeiboUserDataFromSina(paraMap, tokenMap);// 新浪返回结果
			LinksusTaskErrorCode error = StringUtil.parseSinaErrorCode(strData);
			if(error != null){
				rsMap.put("errcode1", error.getErrorCode());
			}else{
				LinksusRelationWeibouser linkWeiboUser = new LinksusRelationWeibouser();
				Map dataMap = JsonUtil.json2map(strData, Map.class);
				String rpsId = String.valueOf(dataMap.get("idstr"));
				Map tagParaMap = new HashMap();
				tagParaMap.put("token", tokenMap.get("token"));
				tagParaMap.put("uid", rpsId);
				List tagsList = getSinaWeiBoUserTags(tagParaMap);
				linkWeiboUser.setTagList(tagsList);
				linkWeiboUser.setUserType(1);
				CopyMapToBean.copySinaUserMapToBean(dataMap, linkWeiboUser);
				list.add(linkWeiboUser);
			}
		}
		if(StringUtils.equals(userType, "2") || userType == null){
			Map tokenMap = getAccountTokenMap("2");
			String strData = getWeiboUserDataFromTencent(paraMap, tokenMap);// 腾讯返回结果
			LinksusTaskErrorCode error = StringUtil.parseTencentErrorCode(strData);
			if(error != null){
				rsMap.put("errcode2", error.getErrorCode());
			}else{
				LinksusRelationWeibouser linkWeiboUser = new LinksusRelationWeibouser();
				String strResult = JsonUtil.getNodeByName(strData, "data");
				Map dataMap = JsonUtil.json2map(strResult, Map.class);
				List tagsList = (List) dataMap.get("tag");
				List tagsTentList = (List) getCenTentWeiBoUserTags(tagsList);
				linkWeiboUser.setTagList(tagsTentList);
				linkWeiboUser.setUserType(2);
				CopyMapToBean.copyTencentUserMapToBean(dataMap, linkWeiboUser);
				list.add(linkWeiboUser);
			}
		}
		rsMap.put("data", list);
		return rsMap;
	}

	// 取新浪微博用户标签
	public List getSinaWeiBoUserTags(Map paraMap){
		UrlEntity strUrlTag = LoadConfig.getUrlEntity("WeiBoTagData");
		Map paramsTag = new HashMap();
		paramsTag.put("access_token", paraMap.get("token").toString());
		paramsTag.put("uid", paraMap.get("uid").toString());
		paramsTag.put("count", 200);
		String resultTag = HttpUtil.getRequest(strUrlTag, paramsTag);
		List tagResultList = (List) JsonUtil.json2list(resultTag, Map.class);
		// 标签处理未处理
		List userTagsList = new ArrayList();
		String value = "";
		for(int j = 0; j < tagResultList.size(); j++){
			Map map = (Map) tagResultList.get(j);
			Set keySet = map.keySet();
			Iterator iterator = keySet.iterator();
			while (iterator.hasNext()){
				String key = (String) iterator.next();
				if(!"weight".equals(key) && !"flag".equals(key)){
					value = map.get(key) + "";
				}
			}
			userTagsList.add(value);
		}
		return userTagsList;
	}

	// 获取腾讯用户标签
	public List getCenTentWeiBoUserTags(List<Map> list){
		List tagValueList = new ArrayList();
		if(list != null && list.size() > 0){
			for(int i = 0; i < list.size(); i++){
				Map nameMap = list.get(i);
				String nameStr = nameMap.get("name").toString();
				tagValueList.add(nameStr);
			}
		}
		return tagValueList;
	}
}
