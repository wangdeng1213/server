package com.linksus.interfaces;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.jcs.access.exception.CacheException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linksus.common.Constants;
import com.linksus.common.config.LoadConfig;
import com.linksus.common.module.WeiboUserCommon;
import com.linksus.common.util.ContextUtil;
import com.linksus.common.util.DateUtil;
import com.linksus.common.util.HttpUtil;
import com.linksus.common.util.JsonUtil;
import com.linksus.common.util.LogUtil;
import com.linksus.common.util.RedisUtil;
import com.linksus.common.util.StringUtil;
import com.linksus.common.util.WeiboUtil;
import com.linksus.data.QueueDataSave;
import com.linksus.entity.LinksusAppaccount;
import com.linksus.entity.LinksusRelationWeibouser;
import com.linksus.entity.LinksusTaskErrorCode;
import com.linksus.entity.LinksusWeiboPool;
import com.linksus.entity.UrlEntity;
import com.linksus.service.LinksusRelationWeibouserService;
import com.linksus.service.LinksusWeiboPoolService;

/**
 * ����΢���Ļ�����Ϣ
 */
public class TaskUpdateWeiboInfoInterface extends BaseInterface{

	protected static final Logger logger = LoggerFactory.getLogger(TaskUpdateWeiboInfoInterface.class);
	/** ������� */
	private LinksusRelationWeibouserService relationWeiboUserService = (LinksusRelationWeibouserService) ContextUtil
			.getBean("linksusRelationWeibouserService");
	private LinksusWeiboPoolService linksusWeiboPoolService = (LinksusWeiboPoolService) ContextUtil
			.getBean("linksusWeiboPoolService");

	// ͨ��΢��id����΢������ϸ��Ϣ 
	public Map cal(Map paramsMap) throws Exception{
		TaskUpdateWeiboInfoInterface task = new TaskUpdateWeiboInfoInterface();
		// ��ʽ��{accountId:accountId,accountType,accountType,institutionId:institutionId,groupName:groupName,personId:personId}
		Map map = task.getWeiboInfo(paramsMap);
		return map;
	}

	// ǰ̨�ӿڵ��÷���
	public Map getWeiboInfo(Map paraMap) throws Exception{
		Map rsMap = new HashMap();
		String rsMsg = "";
		String mid = (String) paraMap.get("mid");
		String accountType = (String) paraMap.get("accountType");
		// ���΢���Ƿ����
		paraMap.put("weiboType", accountType);
		LinksusWeiboPool weibo = linksusWeiboPoolService.checkWeiboPoolIsExsit(paraMap);
		if(weibo != null){
			//if (weibo !=null ) {
			//String[] str = weiboRedis.split("#");
			//String srcMid = str[2];
			rsMap.put("srMid", weibo.getSrcMid());
			rsMap.put("content", weibo.getContent());
			rsMap.put("uname", weibo.getUname());
			return rsMap;
		}
		// ��ѯ΢���û�
		Map tokenMap = getAccountTokenMap(accountType);
		Map returnResult = null;
		if(StringUtils.equals(accountType, "1")){
			// ͨ�����˽ӿڻ�ȡ΢������ϸ��Ϣ
			String sinaResult = getWeiboInfoFromSina(mid, tokenMap);
			LinksusTaskErrorCode error = StringUtil.parseSinaErrorCode(sinaResult);
			if(error != null){
				rsMap.put("errcode", error.getErrorCode());
				return rsMap;
			}
			// ����΢���ķ��ص�����
			returnResult = dealWeiboPoolInfo(sinaResult, paraMap, tokenMap);
		}else if(StringUtils.equals(accountType, "2")){
			// ͨ����Ѷ�ӿڻ�ȡ΢������ϸ��Ϣ
			String cententResult = getWeiboInfoFromTencent(mid, tokenMap);
			LinksusTaskErrorCode error = StringUtil.parseTencentErrorCode(cententResult);
			if(error != null){
				rsMap.put("errcode", error.getErrorCode());
				return rsMap;
			}
			// ������Ѷ΢���ķ�������
			returnResult = dealWeiboPoolInfo(cententResult, paraMap, tokenMap);
		}
		if(returnResult != null){
			Long srcMid = (Long) returnResult.get("srcMid");
			String content = (String) returnResult.get("content");
			String uname = (String) returnResult.get("uname");
			if(srcMid != 0 && !StringUtils.equals(content, "0")){
				rsMap.put("srMid", srcMid);
				rsMap.put("content", content);
				rsMap.put("uname", uname);
			}
		}else{
			rsMap.put("srMid", 0);
			rsMap.put("content", "0");
			rsMap.put("uname", "0");
		}
		return rsMap;
	}

	protected Map getAccountTokenMap(String accountType){
		Map tokenMap = null;
		try{
			Map accountMap = cache.getAccountTokenMap();
			LinksusAppaccount tokenObj = (LinksusAppaccount) accountMap.get(accountType);
			if(tokenObj != null){// ����Ӧ��Ȩ
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

	// ����΢������
	public Map dealWeiboPoolInfo(String dataResultStr,Map paraMap,Map tokenMap) throws Exception{
		Map resultMap = new HashMap();
		Map weiboMap = new HashMap();
		// ��ȡ�û�id
		String rpsId = "";
		// ��ȡ�û�����ϸ��Ϣ
		Map userMap = new HashMap();
		String uprofileUrl = "";
		String uname = "";
		String accountType = (String) paraMap.get("accountType");
		// ��΢�����ݲ��뵽΢�����ӱ���
		LinksusWeiboPool weiboPool = new LinksusWeiboPool();
		if(StringUtils.equals(accountType, "1")){
			weiboMap = JsonUtil.json2map(dataResultStr, Map.class);
			String weiboId = weiboMap.get("id") + "";
			String weiboUser = JsonUtil.getNodeByName(dataResultStr, "user");
			WeiboUserCommon weiboUserObj = new WeiboUserCommon();
			weiboUserObj.dealWeiboUserInfo(weiboUser, "2", tokenMap, false, "0", 0);
			userMap = JsonUtil.json2map(weiboUser, Map.class);
			rpsId = userMap.get("id") + "";
			uname = (String) userMap.get("name");
			uprofileUrl = (String) userMap.get("profile_image_url");
			Map retweetedStatus = (Map) weiboMap.get("retweeted_status");
			Long idsrc = 0L;
			Integer publishType = 0;
			if(retweetedStatus != null && !"1".equals(retweetedStatus.get("delete"))){
				Map userStatus = (Map) retweetedStatus.get("user");
				idsrc = (Long) retweetedStatus.get("id");
				publishType = 1;
				// ����ԭ΢����Ϣ
				String openId = "";
				openId = (String) userStatus.get("idstr");
				String sourceUserInfo = JsonUtil.map2json(userStatus);
				LinksusWeiboPool weiPool = new LinksusWeiboPool();
				weiPool.setWeiboType(1);
				weiPool.setPublishType(0);
				copySinaDataToWeiboPool(retweetedStatus, weiPool);
				WeiboUserCommon weiboObj = new WeiboUserCommon();
				LinksusRelationWeibouser weiboSourceUser = weiboObj.dealWeiboUserInfo(sourceUserInfo, "2", tokenMap,
						false, "0", 0);
				//				Map weiboPolMap = new HashMap();
				//				weiboPolMap.put("mid", idsrc);
				//				weiboPolMap.put("weiboType", accountType);
				//				LinksusWeiboPool weiboPoolsingle = linksusWeiboPoolService.checkWeiboPoolIsExsit(weiboPolMap);
				String weiboRedis = RedisUtil.getRedisHash("weibo_pool", idsrc + "#" + accountType);
				if(StringUtils.isBlank(weiboRedis)){
					if(weiboSourceUser != null){
						weiPool.setUid(weiboSourceUser.getUserId());
						// �û�����
						weiPool.setUname(userStatus.get("screen_name") == null ? "" : userStatus.get("screen_name")
								+ "");
						// �û�ͷ��
						weiPool.setUprofileUrl(userStatus.get("profile_image_url") == null ? "" : userStatus
								.get("profile_image_url")
								+ "");
						weiPool.setWeiboType(Integer.valueOf(accountType));
						weiPool.setSrcMid(0L);
						if(!StringUtils.isBlank(openId) && !StringUtils.isBlank(weiboId)){
							String currUrl = "http://weibo.com/" + openId + "/" + WeiboUtil.Id2Mid(idsrc);
							weiPool.setCurrentUrl(currUrl);
						}else{
							weiPool.setCurrentUrl("");
						}
						QueueDataSave.addDataToQueue(weiPool, Constants.OPER_TYPE_INSERT);
						//	linksusWeiboPoolService.insertLinksusWeiboPool(weiPool);
					}
				}
			}
			weiboPool.setSrcMid(idsrc);
			weiboPool.setPublishType(publishType);
			if(!StringUtils.isBlank(rpsId)){
				String currentUrl = "http://weibo.com/" + rpsId + "/" + WeiboUtil.Id2Mid(new Long(weiboId));
				weiboPool.setCurrentUrl(currentUrl);
			}else{
				weiboPool.setCurrentUrl("");
			}
			copySinaDataToWeiboPool(weiboMap, weiboPool);
		}else if(StringUtils.equals(accountType, "2")){
			String dataResult = JsonUtil.getNodeByName(dataResultStr, "data");
			weiboMap = JsonUtil.json2map(dataResult, Map.class);
			rpsId = String.valueOf(weiboMap.get("openid"));
			uprofileUrl = weiboMap.get("head") == null ? "" : weiboMap.get("head") + "";
			if(!StringUtils.isBlank(uprofileUrl)){
				uprofileUrl = uprofileUrl + "/120";
			}else{
				uprofileUrl = "http://mat1.gtimg.com/www/mb/img/p1/head_normal_180.png";
			}
			uname = String.valueOf(weiboMap.get("nick"));
			// ȡ��Ѷ�û�����ϸ��Ϣ
			String userCententInfo = getWeiboUserInfoFromCentent(rpsId, tokenMap);
			WeiboUserCommon weiboUserObj = new WeiboUserCommon();
			weiboUserObj.dealWeiboUserInfo(userCententInfo, "2", tokenMap, false, "0", 0);
			String idsrc = "";
			Integer publishType = 0;
			// ��ȡԭ΢������Ϣ
			String source = JsonUtil.getNodeByName(dataResult, "source");
			if(!StringUtils.equals(source, "null")){
				Map sourceMap = JsonUtil.json2map(source, Map.class);
				// ȡ��ԭ΢��id
				idsrc = (String) sourceMap.get("id");
				publishType = 1;
				String openid = (String) sourceMap.get("openid");
				LinksusWeiboPool weiPool = new LinksusWeiboPool();
				weiPool.setWeiboType(2);
				weiPool.setPublishType(0);
				copyCententDataToWeiboPool(sourceMap, weiPool);
				String userInfo = getWeiboUserInfoFromCentent(openid, tokenMap);
				WeiboUserCommon weiboObj = new WeiboUserCommon();
				LinksusRelationWeibouser weiboSourceUser = weiboObj.dealWeiboUserInfo(userInfo, "2", tokenMap, false,
						"0", 0);
				//				Map weiboPolMap = new HashMap();
				//				weiboPolMap.put("mid", idsrc);
				//				weiboPolMap.put("weiboType", accountType);
				//				LinksusWeiboPool count = linksusWeiboPoolService
				//						.checkWeiboPoolIsExsit(weiboPolMap);
				String weiboRedis = RedisUtil.getRedisHash("weibo_pool", idsrc + "#" + accountType);
				//if (count == null) {
				if(StringUtils.isBlank(weiboRedis)){
					if(weiboSourceUser != null){
						weiPool.setUid(weiboSourceUser.getUserId());
						// �û�����
						weiPool.setUname(weiboSourceUser.getRpsScreenName());
						// �û�ͷ��
						weiPool.setUprofileUrl(weiboSourceUser.getRpsProfileImageUrl());
						weiPool.setWeiboType(Integer.valueOf(accountType));
						weiPool.setSrcMid(0L);
						QueueDataSave.addDataToQueue(weiPool, Constants.OPER_TYPE_INSERT);
						//linksusWeiboPoolService.insertLinksusWeiboPool(weiPool);
					}
				}
			}
			weiboPool.setSrcMid(StringUtils.isBlank(idsrc) ? 0L : new Long(idsrc));
			weiboPool.setPublishType(publishType);
			copyCententDataToWeiboPool(weiboMap, weiboPool);
		}
		LinksusRelationWeibouser user = checkUpdateWeiboUser(rpsId, accountType);
		if(user != null){
			// �û�id
			weiboPool.setUid(user.getUserId());
			// �û�����
			weiboPool.setUname(uname);
			// �û�ͷ��
			weiboPool.setUprofileUrl(uprofileUrl);
			weiboPool.setWeiboType(Integer.valueOf(accountType));
			//����΢��id
			String weiboRedis = RedisUtil.getRedisHash("weibo_pool", weiboPool.getMid() + "#" + accountType);
			if(StringUtils.isBlank(weiboRedis)){
				QueueDataSave.addDataToQueue(weiboPool, Constants.OPER_TYPE_INSERT);
			}
			//	linksusWeiboPoolService.insertLinksusWeiboPool(weiboPool);
		}
		resultMap.put("srcMid", weiboPool.getSrcMid());
		resultMap.put("content", weiboPool.getContent());
		resultMap.put("uname", weiboPool.getUname());
		return resultMap;
	}

	// ������Ѷ���ݵ�΢���ر�
	public void copyCententDataToWeiboPool(Map weiboMap,LinksusWeiboPool weiboPool){
		// ΢��id
		weiboPool.setMid(new Long((String) weiboMap.get("id")));
		// ΢������
		// weiboPool.setWeiboType();
		// ����
		weiboPool.setContent((String) weiboMap.get("text"));
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
		// ԭͼ2000
		weiboPool.setPicOriginalUrl(StringUtils.isBlank(originalUrl) ? "0" : originalUrl.substring(0, originalUrl
				.length() - 1));
		// ��ͼ460
		weiboPool
				.setPicMiddleUrl(StringUtils.isBlank(middleUrl) ? "0" : middleUrl.substring(0, middleUrl.length() - 1));
		// Сͼ120
		weiboPool.setPicThumbUrl(StringUtils.isBlank(thumbUrl) ? "0" : thumbUrl.substring(0, thumbUrl.length() - 1));
		// ��Ƶ��ַ
		Map musicMap = (Map) weiboMap.get("music");
		if(musicMap != null){
			String musicUrl = (String) musicMap.get("url");
			weiboPool.setMusicUrl(musicUrl);
		}else{
			weiboPool.setMusicUrl("");
		}
		// ��Ƶ��ַ
		Map vedioMap = (Map) weiboMap.get("video");
		if(musicMap != null){
			String wedioUrl = (String) musicMap.get("realurl");
			weiboPool.setVideoUrl(wedioUrl);
		}else{
			weiboPool.setVideoUrl("");
		}
		weiboPool.setVideoUrl("");
		// ������
		weiboPool.setCommentCount(Integer.valueOf(weiboMap.get("mcount") + ""));
		// ת����
		weiboPool.setRepostCount(Integer.valueOf(weiboMap.get("count") + ""));
		// ԭid
		// weiboPool.setSrcMid(0L);
		// ��ַ��Ϣ
		weiboPool.setGeo((String) weiboMap.get("geo"));
		// �û�id
		// ------------weiboPool.setUid((String)weiboMap.get("id"));
		// �û�����
		// --------------weiboPool.setUname((String)weiboMap.get("id"));
		// �û�ͷ��
		// --------------weiboPool.setUprofileUrl((String)weiboMap.get("id"));
		// ��������
		// weiboPool.setPublishType(Integer.valueOf(weiboMap.get("type")+""));
		// ��Դ
		weiboPool.setSource((String) weiboMap.get("from"));
		// ����ʱ��
		weiboPool.setCreatedTime(Integer.parseInt(String.valueOf(weiboMap.get("timestamp"))));
		// ΢����URL
		weiboPool.setCurrentUrl("http://t.qq.com/p/t/" + new Long(weiboMap.get("id").toString()));
	}

	// �����������ݵ�΢���ر�
	public void copySinaDataToWeiboPool(Map weiboMap,LinksusWeiboPool weiboPool){
		// ΢��id
		weiboPool.setMid(new Long(weiboMap.get("id") + ""));
		// ΢������
		// weiboPool.setWeiboType();
		// ����
		weiboPool.setContent((String) weiboMap.get("text"));
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
		// ԭͼ
		weiboPool.setPicOriginalUrl(StringUtils.isBlank(originalUrl) ? "0" : originalUrl.substring(0, originalUrl
				.length() - 1));
		// ��ͼ
		weiboPool
				.setPicMiddleUrl(StringUtils.isBlank(middleUrl) ? "0" : middleUrl.substring(0, middleUrl.length() - 1));
		// Сͼ
		weiboPool.setPicThumbUrl(StringUtils.isBlank(thumbUrl) ? "0" : thumbUrl.substring(0, thumbUrl.length() - 1));
		// ��Ƶ��ַ
		weiboPool.setMusicUrl("");
		// ��Ƶ��ַ
		weiboPool.setVideoUrl("");
		// ������
		weiboPool.setCommentCount(Integer.valueOf(weiboMap.get("comments_count") + ""));
		// ת����
		weiboPool.setRepostCount(Integer.valueOf(weiboMap.get("reposts_count") + ""));
		// ԭid
		// weiboPool.setSrcMid(new Long(weiboMap.get("mid")+""));
		// ��ַ��Ϣ
		weiboPool.setGeo("");
		// �û�id
		// ------------weiboPool.setUid((String)weiboMap.get("id"));
		// �û�����
		// --------------weiboPool.setUname((String)weiboMap.get("id"));
		// �û�ͷ��
		// --------------weiboPool.setUprofileUrl((String)weiboMap.get("id"));
		// ��������
		// weiboPool.setPublishType(0);
		// ��Դ
		weiboPool.setSource((String) weiboMap.get("source"));
		// ����ʱ��
		Date createTime = DateUtil.parse(weiboMap.get("created_at").toString(), "EEE MMM dd HH:mm:ss zzz yyyy",
				Locale.US);
		weiboPool.setCreatedTime(Integer.valueOf(String.valueOf(createTime.getTime() / 1000)));
	}

	// ��ȡ�û��Ļ�����Ϣ ����Ѷ
	public String getWeiboUserInfoFromCentent(String rpsId,Map tokenMap){
		// ͨ�������ļ�����Ѷ�ӿ�ȡ���µ�΢������
		UrlEntity strTencentUrl = LoadConfig.getUrlEntity("TCUerInfo");
		String strjson = "";
		Map paraMap = new HashMap();
		paraMap.put("oauth_consumer_key", cache.getAppInfo(Constants.CACHE_TENCENT_APPINFO).getAppKey());
		paraMap.put("clientip", Constants.TENCENT_CLIENT_IP);
		paraMap.put("oauth_version", "2.a");
		paraMap.put("format", "json");
		paraMap.put("openid", tokenMap.get("openid"));
		paraMap.put("access_token", tokenMap.get("token"));
		paraMap.put("fopenid", rpsId);
		strjson = HttpUtil.getRequest(strTencentUrl, paraMap);
		return strjson;
	}

	// ͨ��΢��id��ȡ΢������ϸ��Ϣ������
	public String getWeiboInfoFromSina(String rpsId,Map tokenMap){
		String strjson = "";
		UrlEntity strUrl = LoadConfig.getUrlEntity("InteractDataSyncTrans");
		Map paraMap = new HashMap();
		paraMap.put("access_token", tokenMap.get("token"));
		paraMap.put("id", rpsId);
		strjson = HttpUtil.getRequest(strUrl, paraMap);
		return strjson;
	}

	// ͨ���û�id��ȡ΢������ϸ��Ϣ����Ѷ
	public String getWeiboInfoFromTencent(String mid,Map tokenMap){
		// ͨ�������ļ�����Ѷ�ӿ�ȡ���µ�΢������
		UrlEntity strTencentUrl = LoadConfig.getUrlEntity("TCWeiboByID");
		String strjson = "";
		Map paraMap = new HashMap();
		paraMap.put("oauth_consumer_key", cache.getAppInfo(Constants.CACHE_TENCENT_APPINFO).getAppKey());
		paraMap.put("clientip", Constants.TENCENT_CLIENT_IP);
		paraMap.put("oauth_version", "2.a");
		paraMap.put("format", "json");
		paraMap.put("openid", tokenMap.get("openid"));
		paraMap.put("access_token", tokenMap.get("token"));
		paraMap.put("id", mid);
		strjson = HttpUtil.getRequest(strTencentUrl, paraMap);
		return strjson;
	}

	// ����rpsId �� appType �ж��û��Ƿ� ����΢���û�����
	public LinksusRelationWeibouser checkUpdateWeiboUser(String rpsId,String appType){
		Map paraMap = new HashMap();
		paraMap.put("rpsId", rpsId);
		paraMap.put("userType", appType);
		// ��ѯuserid��ͬ��ʱ��sysTime ����һ������
		LinksusRelationWeibouser user = relationWeiboUserService.getWeibouserByrpsIdAndType(paraMap);
		return user;
	}
}
