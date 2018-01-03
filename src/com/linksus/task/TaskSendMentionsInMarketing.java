package com.linksus.task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linksus.common.Constants;
import com.linksus.common.config.LoadConfig;
import com.linksus.common.util.ContextUtil;
import com.linksus.common.util.HttpUtil;
import com.linksus.common.util.LogUtil;
import com.linksus.common.util.StringUtil;
import com.linksus.entity.LinksusAppaccount;
import com.linksus.entity.LinksusRelationMarketing;
import com.linksus.entity.LinksusRelationMarketingItem;
import com.linksus.entity.LinksusTaskErrorCode;
import com.linksus.entity.LinksusTaskInvalidRecord;
import com.linksus.entity.UrlEntity;
import com.linksus.service.LinksusAppaccountService;
import com.linksus.service.LinksusRelationMarketingItemService;
import com.linksus.service.LinksusRelationMarketingService;
import com.linksus.service.LinksusRelationWeibouserService;

public class TaskSendMentionsInMarketing extends BaseTask{

	protected static final Logger logger = LoggerFactory.getLogger(TaskSendMentionsInMarketing.class);

	// 获取新浪评论路径
	String writeCommUrl = LoadConfig.getString("replyWeibo");

	// 获取腾讯评论路径
	UrlEntity writeContentCommUrl = LoadConfig.getUrlEntity("TCreplyWeibo");

	LinksusRelationMarketingService linksusRelationMarketingService = (LinksusRelationMarketingService) ContextUtil
			.getBean("linksusRelationMarketingService");
	LinksusRelationMarketingItemService linksusRelationMarketingItemService = (LinksusRelationMarketingItemService) ContextUtil
			.getBean("linksusRelationMarketingItemService");
	LinksusAppaccountService linksusAppaccountService = (LinksusAppaccountService) ContextUtil
			.getBean("linksusAppaccountService");
	LinksusRelationWeibouserService linksusRelationWeibouserService = (LinksusRelationWeibouserService) ContextUtil
			.getBean("linksusRelationWeibouserService");

	public static void main(String[] args){
		//TaskSendMentionsInMarketing aa = new TaskSendMentionsInMarketing();
		//aa.accountType = 1;
		//	aa.cal();
	}

	@Override
	public void cal(){
		sendContentInMarketing();
	}

	/**
	 * 发布营销对象内容
	 * 
	 * marketingType: 营销类型(1@,2评论,3短信,4邮件) accountType: 帐号类型:1 新浪 2 腾讯
	 */
	public void sendContentInMarketing(){

		// 获取营销列表——@列表
		LinksusRelationMarketing getMarketing = new LinksusRelationMarketing();
		getMarketing.setMarketingType(1);
		int startCount = (currentPage - 1) * pageSize;
		getMarketing.setPageSize(pageSize);
		getMarketing.setStartCount(startCount);
		getMarketing.setAccountType(accountType);
		// 获取新浪账号@营销类型
		List<LinksusRelationMarketing> marketings = linksusRelationMarketingService
				.getSendCommentsByUserList(getMarketing);
		// 处理新浪账号营销对象
		for(int i = 0; i < marketings.size(); i++){
			LinksusRelationMarketing linksusRelationMarketing = marketings.get(i);
			if(linksusRelationMarketing != null){
				try{
					if(linksusRelationMarketing.getAccountType() == 1){
						// 新浪账号 通过营销主表关联营销对象表,发表评论
						sendSinaMentionsInMarketing(linksusRelationMarketing);
					}else if(linksusRelationMarketing.getAccountType() == 2){
						// 腾讯账号 通过营销主表关联营销对象表,发表评论
						sendTencentMentionsInMarketing(linksusRelationMarketing);
					}
				}catch (Exception e){
					LogUtil.saveException(logger, e);
				}

			}
		}
		checkTaskListEnd(marketings);
	}

	// 新浪账号发表@内容评论
	public void sendSinaMentionsInMarketing(LinksusRelationMarketing marketing){

		// 获取发表@内容的用户
		long weiboId = marketing.getWeiboId();
		int status = 0;
		// 获取发表评论用户
		LinksusAppaccount linksusAppaccount = linksusAppaccountService.getLinksusAppaccountTokenById(marketing
				.getAccountId());
		try{
			// 判断此微博是否存在
			if(checkWeiboExists(weiboId, linksusAppaccount)){
				Map mapPara = new HashMap();
				mapPara.put("access_token", linksusAppaccount.getToken());
				mapPara.put("id", weiboId);
				String strResult = HttpUtil.getRequest(LoadConfig.getUrlEntity("WeiBoData"), mapPara);// 返回评论相关信息
				LinksusTaskErrorCode error = StringUtil.parseSinaErrorCode(strResult);
				if(error != null){// 存在sina返回错误代码)
					dealErrorCodeInvalidRecord(linksusAppaccount.getInstitutionId(), marketing.getPid(), error
							.getErrorCode().toString(), error.getErrorMsg());
					status = 5;
				}else{
					// 向该微博发评论
					mapPara = new HashMap();
					mapPara.put("access_token", linksusAppaccount.getToken());
					mapPara.put("comment", marketing.getMarketingContent());
					mapPara.put("id", weiboId);
					strResult = HttpUtil.postRequest(LoadConfig.getUrlEntity("replyWeibo"), mapPara);// 返回评论相关信息
					error = StringUtil.parseSinaErrorCode(strResult);
					if(error != null){// 存在sina返回错误代码
						dealErrorCodeInvalidRecord(linksusAppaccount.getInstitutionId(), marketing.getPid(), error
								.getErrorCode().toString(), error.getErrorMsg());
						status = 5;
					}else{
						status = 4;
						// 更新用户sina发表评论次数
						// cache.addCurrHourWriteCountByUser(linksusRelationMarketing.getAccountId().toString(),
						// Constants.LIMIT_SEND_COMMENT_PER_HOUR);
					}
				}
			}else{
				dealExceptionInvalidRecord(linksusAppaccount.getInstitutionId(), marketing.getWeiboId(), new Exception(
						"微博不存在!"));
			}
		}catch (Exception e){
			LogUtil.saveException(logger, e);
			Long institutionId = 0l;
			if(linksusAppaccount != null){
				institutionId = linksusAppaccount.getInstitutionId();
			}
			dealExceptionInvalidRecord(institutionId, marketing.getWeiboId(), e);
		}
		// 更新营销主表状态
		//marketing.setStatus(status);
		//linksusRelationMarketingService.updateMentionsStatus(marketing);
		//更新子表状态
		LinksusRelationMarketingItem item = new LinksusRelationMarketingItem();
		item.setMarketingId(marketing.getPid());
		if(status == 4){
			item.setStatus(1);
		}else{
			item.setStatus(2);
		}
		linksusRelationMarketingItemService.updateMoreMarketingItemStatus(item);

	}

	// 腾讯账号发表@内容评论
	public void sendTencentMentionsInMarketing(LinksusRelationMarketing marketing){
		marketing.getMarketingSuccessNum();
		marketing.getMarketingFailNum();
		// 获取发表@内容的用户
		long weiboId = marketing.getWeiboId();
		int status = 0;
		// 获取发表评论用户
		LinksusAppaccount linksusAppaccount = linksusAppaccountService.getLinksusAppaccountTokenById(marketing
				.getAccountId());
		try{
			// 判断此微博是否存在
			if(checkWeiboExists(weiboId, linksusAppaccount)){
				Map mapPara = new HashMap();
				mapPara.put("oauth_consumer_key", cache.getAppInfo(Constants.CACHE_TENCENT_APPINFO).getAppKey());
				mapPara.put("openid", linksusAppaccount.getAppid());
				mapPara.put("clientip", Constants.TENCENT_CLIENT_IP);
				mapPara.put("access_token", linksusAppaccount.getToken());
				mapPara.put("oauth_version", "2.a");
				mapPara.put("format", "json");
				mapPara.put("id", weiboId);
				String strResult = HttpUtil.getRequest(LoadConfig.getUrlEntity("TCWeiboByID"), mapPara);// 返回评论相关信息
				LinksusTaskErrorCode error = StringUtil.parseTencentErrorCode(strResult);
				if(error == null){
					// 向该微博发评论
					mapPara = new HashMap();
					mapPara.put("oauth_consumer_key", cache.getAppInfo(Constants.CACHE_TENCENT_APPINFO).getAppKey());
					mapPara.put("openid", linksusAppaccount.getAppid());
					mapPara.put("clientip", Constants.TENCENT_CLIENT_IP);
					mapPara.put("access_token", linksusAppaccount.getToken());
					mapPara.put("oauth_version", "2.a");
					mapPara.put("format", "json");
					mapPara.put("content", marketing.getMarketingContent());
					mapPara.put("reid", weiboId);
					strResult = HttpUtil.postRequest(writeContentCommUrl, mapPara);// 返回评论相关信息
					error = StringUtil.parseTencentErrorCode(strResult);
					if(error == null){
						status = 4;
						// 更新用户tencent写次数
						// cache.addCurrHourWriteCountByUser(linksusRelationMarketing.getAccountId().toString(),
						// Constants.LIMIT_TENCENT_WRITE_PER_HOUR);
					}else{
						status = 5;
						dealErrorCodeInvalidRecord(linksusAppaccount.getInstitutionId(), marketing.getPid(), error
								.getErrorCode().toString(), error.getErrorMsg());
					}
				}else{
					status = 5;
					dealExceptionInvalidRecord(linksusAppaccount.getInstitutionId(), marketing.getPid(), new Exception(
							"未查询到数据!"));
				}
			}else{
				dealExceptionInvalidRecord(linksusAppaccount.getInstitutionId(), marketing.getWeiboId(), new Exception(
						"微博不存在!"));
			}
		}catch (Exception e){
			LogUtil.saveException(logger, e);
			Long institutionId = 0l;
			if(linksusAppaccount != null){
				institutionId = linksusAppaccount.getInstitutionId();
			}
			dealExceptionInvalidRecord(institutionId, marketing.getWeiboId(), e);
		}
		// 更新营销主表状态
		//		marketing.setStatus(status);
		//		linksusRelationMarketingService.updateMentionsStatus(marketing);
		//更新子表状态
		LinksusRelationMarketingItem item = new LinksusRelationMarketingItem();
		item.setMarketingId(marketing.getPid());
		if(status == 4){
			item.setStatus(1);
		}else{
			item.setStatus(2);
		}
		linksusRelationMarketingItemService.updateMoreMarketingItemStatus(item);
	}

	private void dealExceptionInvalidRecord(Long institutionId,Long pid,Exception exception){
		try{
			LinksusTaskInvalidRecord invalidRecord = new LinksusTaskInvalidRecord();
			invalidRecord.setInstitutionId(institutionId);
			invalidRecord.setErrorCode("EXCEPTION");
			invalidRecord.setErrorMsg(LogUtil.getExceptionStackMsg(exception));
			invalidRecord.setOperType(Constants.INVALID_RECORD_MARKETING_AT);
			invalidRecord.setRecordId(pid);
			dealInvalidRecord(invalidRecord);
		}catch (Exception e){
			LogUtil.saveException(logger, e);
		}
	}

	private void dealErrorCodeInvalidRecord(Long institutionId,Long pid,String errcode,String rsData){
		try{
			LinksusTaskInvalidRecord invalidRecord = new LinksusTaskInvalidRecord();
			invalidRecord.setErrorCode(errcode);
			invalidRecord.setErrorMsg(rsData);
			invalidRecord.setInstitutionId(institutionId);
			invalidRecord.setOperType(Constants.INVALID_RECORD_MARKETING_AT);
			invalidRecord.setRecordId(pid);
			dealInvalidRecord(invalidRecord);
		}catch (Exception e){
			LogUtil.saveException(logger, e);
		}
	}

	/**
	 * 判断微博是否存在
	 * @param weiboId 微博id
	 * @param authToken 授权token
	 * @param type  类型 1 新浪  2 腾讯
	 * @return boolean
	 */
	public boolean checkWeiboExists(Long weiboId,LinksusAppaccount linksusAppaccount){
		boolean isExists = false;
		if(linksusAppaccount.getType() == 1){//新浪
			UrlEntity strurl = LoadConfig.getUrlEntity("InteractDataSyncTrans");
			Map mapPara = new HashMap();
			mapPara.put("access_token", linksusAppaccount.getToken());
			mapPara.put("id", weiboId);
			String rsData = HttpUtil.getRequest(strurl, mapPara);
			LinksusTaskErrorCode error = StringUtil.parseSinaErrorCode(rsData);
			if(error == null){
				isExists = true;
			}
		}else if(linksusAppaccount.getType() == 2){//腾讯
			Map paraMap = new HashMap();
			paraMap.put("access_token", linksusAppaccount.getToken());
			paraMap.put("oauth_consumer_key", cache.getAppInfo(Constants.CACHE_TENCENT_APPINFO).getAppKey());
			paraMap.put("openid", linksusAppaccount.getAppid());
			paraMap.put("clientip", Constants.TENCENT_CLIENT_IP);
			paraMap.put("oauth_version", "2.a");
			paraMap.put("format", "json");
			paraMap.put("id", weiboId);
			UrlEntity strUrl = LoadConfig.getUrlEntity("TCWeiboByID");
			String rsData = HttpUtil.getRequest(strUrl, paraMap);
			LinksusTaskErrorCode error = StringUtil.parseTencentErrorCode(rsData);
			if(error == null){
				isExists = true;
			}
		}
		return isExists;
	}

}
