package com.linksus.queue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.linksus.common.Constants;
import com.linksus.common.config.LoadConfig;
import com.linksus.common.module.WeiboUserCommon;
import com.linksus.common.util.ContextUtil;
import com.linksus.common.util.HttpUtil;
import com.linksus.common.util.JsonUtil;
import com.linksus.common.util.PrimaryKeyGen;
import com.linksus.common.util.RedisUtil;
import com.linksus.common.util.StringUtil;
import com.linksus.data.QueueDataSave;
import com.linksus.entity.LinksusAppaccount;
import com.linksus.entity.LinksusAttentionWeibo;
import com.linksus.entity.LinksusRelationWeibouser;
import com.linksus.entity.LinksusTaskErrorCode;
import com.linksus.entity.LinksusWeiboDataSync;
import com.linksus.entity.LinksusWeiboPool;
import com.linksus.entity.UrlEntity;
import com.linksus.service.LinksusAppaccountService;
import com.linksus.service.LinksusAttentionWeiboService;
import com.linksus.service.LinksusRelationWeibouserService;
import com.linksus.service.LinksusWeiboDataSyncService;

/**
 * 抓取关注用户的微博
 * */

public class TaskAttentionWeiboSync extends BaseQueue{

	private String accountType;

	private LinksusAttentionWeiboService attWeiboService = (LinksusAttentionWeiboService) ContextUtil
			.getBean("linksusAttentionWeiboService");
	private LinksusWeiboDataSyncService weiboDataSyncService = (LinksusWeiboDataSyncService) ContextUtil
			.getBean("linksusWeiboDataSyncService");
	private LinksusAppaccountService linksusAppaccountService = (LinksusAppaccountService) ContextUtil
			.getBean("linksusAppaccountService");
	private LinksusRelationWeibouserService relationWeiboUserService = (LinksusRelationWeibouserService) ContextUtil
			.getBean("linksusRelationWeibouserService");

	public void setAccountType(String accountType){
		this.accountType = accountType;
	}

	protected TaskAttentionWeiboSync(String taskType) {
		super(taskType);
	}

	@Override
	protected List flushTaskQueue(){
		// 从服务器端采用分页取平台关注用户的微博列表
		int currenCount = 0;
		currenCount = (currentPage - 1) * pageSize;
		// 通过分页取得用户列表
		LinksusAppaccount platUser = new LinksusAppaccount();
		platUser.setStartCount(currenCount);
		platUser.setPageSize(pageSize);
		platUser.setType(new Integer(accountType));// 1,新浪 2,腾讯
		List<LinksusAppaccount> appUserlist = linksusAppaccountService.getLinksusAppaccountAttentionList(platUser);
		if(appUserlist.size() < pageSize){// 任务循环完成 下次重新开始
			currentPage = 1;
			setTaskFinishFlag();
		}else{
			currentPage++;
		}
		return appUserlist;
	}

	@Override
	protected String parseClientData(Map dataMap) throws Exception{
		List<LinksusAttentionWeibo> linksusAttentionWeibolist = (List<LinksusAttentionWeibo>) dataMap
				.get("attentionList");
		List<LinksusWeiboDataSync> linksusWeiboDataSynclist = (List<LinksusWeiboDataSync>) dataMap
				.get("weiboDataSyncList");
		List<LinksusWeiboPool> linksusWeiboPoollist = (List<LinksusWeiboPool>) dataMap.get("weiboSourceList");
		updateTransferWeibo(linksusWeiboPoollist);
		updateAttentionWeibo(linksusAttentionWeibolist);
		for(int i = 0; i < linksusWeiboDataSynclist.size(); i++){
			LinksusWeiboDataSync linksusWeiboDataSync = linksusWeiboDataSynclist.get(i);
			//查询临界表中的数据
			Integer dataSyncCount = weiboDataSyncService.getLinksusWeiboDataSyncCount(linksusWeiboDataSync);
			if(dataSyncCount > 0){
				weiboDataSyncService.updateLinksusWeiboDataSyncSingle(linksusWeiboDataSync);
			}else{
				//插入
				weiboDataSyncService.insertLinksusWeiboDataSyncSingle(linksusWeiboDataSync);
			}
		}
		return "success";
	}

	//将数据更新到数据库表中
	public void updateAttentionWeibo(List<LinksusAttentionWeibo> linksusAttentionWeibolist) throws Exception{
		//将数据更新到数据库表中
		for(int i = 0; i < linksusAttentionWeibolist.size(); i++){
			LinksusAttentionWeibo linksusAttentionWeibo = linksusAttentionWeibolist.get(i);
			//取出关注微博的用户
			String accountType = linksusAttentionWeibo.getAccountType() + "";
			Long accountId = linksusAttentionWeibo.getAccountId();
			Long institutionId = linksusAttentionWeibo.getInstitutionId();
			String dataResult = linksusAttentionWeibo.getUser();
			WeiboUserCommon weibouser = new WeiboUserCommon();
			Map paramMap = new HashMap();
			paramMap.put("AccountId", String.valueOf(accountId));
			paramMap.put("InstitutionId", institutionId);
			paramMap.put("type", accountType);
			LinksusRelationWeibouser weiboUser = weibouser.dealWeiboUserInfo(dataResult, "1", paramMap, false, "2", 0);
			//如果关注用户不在微博用户表中插入
			Map commonStrMap = new HashMap();
			String screenName = "";
			String imageUrl = "";
			if("1".equals(accountType)){
				if(!StringUtils.isBlank(JsonUtil.getNodeValueByName(dataResult, "error_code"))){//存在错误
					continue;
				}
				commonStrMap = JsonUtil.json2map(dataResult, Map.class);
				screenName = (String) commonStrMap.get("nick") == null ? "" : (String) commonStrMap.get("nick");
				imageUrl = StringUtils.isBlank((String) commonStrMap.get("head")) ? "http://mat1.gtimg.com/www/mb/img/p1/head_normal_180.png"
						: (String) commonStrMap.get("head") + "/120";
			}else if("2".equals(accountType)){
				LinksusTaskErrorCode error = StringUtil.parseTencentErrorCode(dataResult);
				if(error != null){
					continue;
				}
				String data = JsonUtil.getNodeValueByName(dataResult, "data");
				if(StringUtils.equals(data, "null")){
					continue;
				}

				commonStrMap = JsonUtil.json2map(data, Map.class);
				screenName = commonStrMap.get("screen_name") == null ? "" : commonStrMap.get("screen_name") + "";
				imageUrl = commonStrMap.get("profile_image_url") == null ? "" : commonStrMap.get("profile_image_url")
						+ "";
			}
			Long id = PrimaryKeyGen.getPrimaryKey("linksus_attention_weibo", "id");
			linksusAttentionWeibo.setId(id);
			if(weiboUser != null){
				linksusAttentionWeibo.setUid(weiboUser.getUserId());
				linksusAttentionWeibo.setUname(screenName);
				linksusAttentionWeibo.setUprofileUrl(imageUrl);
			}
			//根据weiboid 和 账号id 和账号类型判断关注用户的微博是否存在
			Integer countWeibo = attWeiboService.getAttentionWeiboCount(linksusAttentionWeibo);
			if(countWeibo == 0){
				attWeiboService.insertLinksusAttentionWeibo(linksusAttentionWeibo);
			}
		}
	}

	//转发微博数据更新到微博池中
	public void updateTransferWeibo(List<LinksusWeiboPool> linksusWeiboPoollist) throws Exception{
		//转发微博数据更新到微博池中
		for(int k = 0; k < linksusWeiboPoollist.size(); k++){
			LinksusWeiboPool weiboPool = linksusWeiboPoollist.get(k);
			String userInfo = "";
			String openId = "";
			String screenName = "";
			String imageUrl = "";
			Long weiboid = weiboPool.getMid();
			Integer weiboType = weiboPool.getWeiboType();
			weiboPool.setPublishType(0);
			Map tokenMap = getAccountTokenMap(accountType);
			if(weiboType == 2){
				openId = weiboPool.getOpenId();
				userInfo = getWeiboUserInfoFromCentent(openId, tokenMap);
				LinksusTaskErrorCode error = StringUtil.parseTencentErrorCode(userInfo);
				if(error != null){
					continue;
				}
				String data = JsonUtil.getNodeValueByName(userInfo, "data");
				if(StringUtils.equals(data, "null")){
					continue;
				}
				Map commonStrMap = JsonUtil.json2map(data, Map.class);
				screenName = commonStrMap.get("screen_name") == null ? "" : commonStrMap.get("screen_name") + "";
				imageUrl = commonStrMap.get("profile_image_url") == null ? "" : commonStrMap.get("profile_image_url")
						+ "";
			}else if(weiboType == 1){
				openId = weiboPool.getOpenId();
				userInfo = weiboPool.getSourceUser();
				if(!StringUtils.isBlank(JsonUtil.getNodeValueByName(userInfo, "error_code"))){//存在错误
					continue;
				}
				Map commonStrMap = JsonUtil.json2map(userInfo, Map.class);
				screenName = (String) commonStrMap.get("nick") == null ? "" : (String) commonStrMap.get("nick");
				imageUrl = StringUtils.isBlank((String) commonStrMap.get("head")) ? "http://mat1.gtimg.com/www/mb/img/p1/head_normal_180.png"
						: (String) commonStrMap.get("head") + "/120";
			}
			//检查原微博是否在微博池中
			//			Map paraMap =new HashMap();
			//			paraMap.put("mid",weiboid);
			//			paraMap.put("weiboType", weiboType);
			//走缓存
			String weiboRedis = RedisUtil.getRedisHash("weibo_pool", weiboid + "#" + weiboType);
			//LinksusWeiboPool count =linksusWeiboPoolService.checkWeiboPoolIsExsit(paraMap);
			//if(count == null){
			if(StringUtils.isBlank(weiboRedis)){
				//LinksusRelationWeibouser user =checkUpdateWeiboUser(openId,accountType);
				WeiboUserCommon weiboUserObj = new WeiboUserCommon();
				LinksusRelationWeibouser weibouser = weiboUserObj.dealWeiboUserInfo(userInfo, "2", tokenMap, false,
						"0", 0);
				if(weibouser != null){
					//用户id
					weiboPool.setUid(weibouser.getUserId());
					//用户名称
					weiboPool.setUname(screenName);
					//用户头像
					weiboPool.setUprofileUrl(imageUrl);
					weiboPool.setWeiboType(Integer.valueOf(accountType));
					QueueDataSave.addDataToQueue(weiboPool, Constants.OPER_TYPE_INSERT);
					// linksusWeiboPoolService.insertLinksusWeiboPool(weiboPool);
				}
			}
		}
	}

	//获取用户的基本信息 从腾讯
	public String getWeiboUserInfoFromCentent(String rpsId,Map tokenMap){
		//通过配置文件从腾讯接口取最新的微博数据
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

	//根据rpsId 和 appType 判断用户是否 存在微博用户表中
	public LinksusRelationWeibouser checkUpdateWeiboUser(String rpsId,String appType){
		Map paraMap = new HashMap();
		paraMap.put("rpsId", rpsId);
		paraMap.put("userType", appType);
		//查询userid和同步时间sysTime 返回一个对象
		LinksusRelationWeibouser user = relationWeiboUserService.getWeibouserByrpsIdAndType(paraMap);
		return user;
	}

}
