package com.linksus.interfaces;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.jcs.access.exception.CacheException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linksus.common.Constants;
import com.linksus.common.config.LoadConfig;
import com.linksus.common.util.ContextUtil;
import com.linksus.common.util.HttpUtil;
import com.linksus.common.util.JsonUtil;
import com.linksus.common.util.LogUtil;
import com.linksus.common.util.StringUtil;
import com.linksus.data.QueueDataSave;
import com.linksus.entity.LinksusAppaccount;
import com.linksus.entity.LinksusAttentionWeibo;
import com.linksus.entity.LinksusTaskErrorCode;
import com.linksus.entity.LinksusWeibo;
import com.linksus.entity.LinksusWeiboPool;
import com.linksus.entity.ResponseAndRecordDTO;
import com.linksus.entity.UrlEntity;
import com.linksus.service.LinksusAttentionWeiboService;
import com.linksus.service.LinksusWeiboPoolService;
import com.linksus.service.LinksusWeiboService;

/**
 * 搜索的用户与账号的关系接口
 */
public class TaskWeiboDynaDataInterface extends BaseInterface{

	protected static final Logger logger = LoggerFactory.getLogger(TaskWeiboDynaDataInterface.class);
	/** 缓存对象 */
	private LinksusWeiboService linksusWeiboService = (LinksusWeiboService) ContextUtil.getBean("linksusWeiboService");
	private LinksusAttentionWeiboService linksusAttentionWeiboService = (LinksusAttentionWeiboService) ContextUtil
			.getBean("linksusAttentionWeiboService");
	private LinksusWeiboPoolService linksusWeiboPoolService = (LinksusWeiboPoolService) ContextUtil
			.getBean("linksusWeiboPoolService");
	private UrlEntity strUrl = LoadConfig.getUrlEntity("WeiBoDynamicData");
	private UrlEntity strQQUrl = LoadConfig.getUrlEntity("TCWeibocount");

	public Map cal(Map paramsMap) throws Exception{
		TaskWeiboDynaDataInterface task = new TaskWeiboDynaDataInterface();
		Map rsMap = task.getWeiboDynaData(paramsMap);
		return rsMap;
	}

	// 前台接口调用方法
	public Map getWeiboDynaData(Map paraMap) throws Exception{
		Map rsMap = new HashMap();
		String weiboType = (String) paraMap.get("weiboType");
		String tableType = (String) paraMap.get("tableType");
		Map tokenMap = getAccountTokenMap(weiboType);
		//根据参数判断微博是否存在微博表中
		LinksusWeibo linksusWeibo = null;
		if(StringUtils.equals(tableType, "1")){//微博发布表
			if("0".equals(paraMap.get("mid"))){
				rsMap.put("errcode", "#微博mid不能为0");
				return rsMap;
			}
			linksusWeibo = linksusWeiboService.getLinksusWeiboByMap(paraMap);
		}else if(StringUtils.equals(tableType, "2")){//微博关注表
			linksusWeibo = linksusAttentionWeiboService.getLinksusAttentionWeiboByMap(paraMap);
		}else if(StringUtils.equals(tableType, "3")){//微博池 
			linksusWeibo = linksusWeiboPoolService.getLinksusWeiboPoolByMap(paraMap);
		}
		if(linksusWeibo != null){
			//更新微博评论数转发数
			Integer sourceCommentNum = linksusWeibo.getCommentCount();
			Integer sourceRepostsNum = linksusWeibo.getRepostCount();
			String rsData = "";
			ResponseAndRecordDTO weiboData = null;
			if(StringUtils.equals(weiboType, "1")){
				Map parasMap = new HashMap();
				parasMap.put("access_token", tokenMap.get("token"));
				parasMap.put("ids", linksusWeibo.getMid());
				rsData = HttpUtil.getRequest(strUrl, parasMap);
				LinksusTaskErrorCode error = StringUtil.parseSinaErrorCode(rsData);
				if(error != null){
					logger.error(rsData);
					rsMap.put("errcode", error.getErrorCode());
					return rsMap;
				}
				List<ResponseAndRecordDTO> backData = (List<ResponseAndRecordDTO>) JsonUtil.json2list(rsData,
						ResponseAndRecordDTO.class);
				if(backData != null && backData.size() > 0){
					weiboData = backData.get(0);
				}else{
					rsMap.put("errcode", "30002");
					return rsMap;
				}
			}else if(StringUtils.equals(weiboType, "2")){
				Map paraCtMap = new HashMap();
				paraCtMap.put("access_token", tokenMap.get("token"));
				paraCtMap.put("ids", linksusWeibo.getMid());
				paraCtMap.put("oauth_consumer_key", cache.getAppInfo(Constants.CACHE_TENCENT_APPINFO).getAppKey());
				paraCtMap.put("flag", 2);
				paraCtMap.put("format", "json");
				paraCtMap.put("oauth_version", "2.a");
				paraCtMap.put("openid", tokenMap.get("openid"));
				rsData = HttpUtil.getRequest(strQQUrl, paraCtMap);
				LinksusTaskErrorCode error = StringUtil.parseTencentErrorCode(rsData);
				if(error != null){
					logger.error(rsData);
					rsMap.put("errcode", error.getErrorCode());
					return rsMap;
				}
				String getData = JsonUtil.getNodeByName(rsData, "data");
				Map dataMap = JsonUtil.json2map(getData, Map.class);
				// 遍历dataMap
				if(dataMap != null){
					for(Iterator itor = dataMap.entrySet().iterator(); itor.hasNext();){
						Entry entry = (Entry) itor.next();
						String id = entry.getKey().toString();
						Map valueMap = (Map) entry.getValue();
						Integer returnComments = (Integer) valueMap.get("mcount");
						Integer returnReposts = (Integer) valueMap.get("count");
						ResponseAndRecordDTO rdDTO = new ResponseAndRecordDTO();
						rdDTO.setReposts(returnReposts);
						rdDTO.setComments(returnComments);
						rdDTO.setId(new Long(id));
						weiboData = rdDTO;
					}
				}else{
					rsMap.put("errcode", "30002");
					return rsMap;
				}
			}
			if(weiboData != null){
				Integer commentsNum = weiboData.getComments();
				Integer repostsNum = weiboData.getReposts();
				if(!sourceCommentNum.equals(commentsNum) || !sourceRepostsNum.equals(repostsNum)){
					//更新表中的评论数转发数
					if(StringUtils.equals(tableType, "1")){//微博发布表
						linksusWeibo.setCommentCount(commentsNum);
						linksusWeibo.setRepostCount(repostsNum);
						//linksusWeiboService.updateSendWeiboCount(linksusWeibo);
						QueueDataSave.addDataToQueue(linksusWeibo, Constants.OPER_TYPE_UPDATE);
					}else if(StringUtils.equals(tableType, "2")){//微博关注表
						LinksusAttentionWeibo linksusAttentionWeibo = new LinksusAttentionWeibo();
						linksusAttentionWeibo.setCommentCount(commentsNum);
						linksusAttentionWeibo.setRepostCount(repostsNum);
						linksusAttentionWeibo.setMid(linksusWeibo.getMid());
						//linksusAttentionWeiboService.updateSendWeiboCount(linksusWeibo);
						QueueDataSave.addDataToQueue(linksusAttentionWeibo, Constants.OPER_TYPE_UPDATE);
					}else if(StringUtils.equals(tableType, "3")){//微博池
						LinksusWeiboPool linksusWeiboPool = new LinksusWeiboPool();
						linksusWeiboPool.setCommentCount(commentsNum);
						linksusWeiboPool.setRepostCount(repostsNum);
						linksusWeiboPool.setMid(linksusWeibo.getMid());
						QueueDataSave.addDataToQueue(linksusWeibo, Constants.OPER_TYPE_UPDATE);
						//linksusWeiboPoolService.updateLinksusWeiboPoolCount(linksusWeibo);
					}
				}
				rsMap.put("reposts", linksusWeibo.getRepostCount());//评论数转发数为空
				rsMap.put("comments", linksusWeibo.getCommentCount());
				rsMap.put("mid", linksusWeibo.getMid());
			}
		}else{
			rsMap.put("errcode", "20001");
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
}
