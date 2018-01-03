package com.linksus.task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linksus.common.Constants;
import com.linksus.common.config.LoadConfig;
import com.linksus.common.util.ContextUtil;
import com.linksus.common.util.HttpUtil;
import com.linksus.common.util.JsonUtil;
import com.linksus.common.util.LogUtil;
import com.linksus.common.util.StringUtil;
import com.linksus.entity.LinksusAppaccount;
import com.linksus.entity.LinksusInteractAttentionReply;
import com.linksus.entity.LinksusInteractAttentionReplyAcct;
import com.linksus.entity.LinksusRelationWeibouser;
import com.linksus.entity.LinksusTaskAttentionUser;
import com.linksus.entity.LinksusTaskErrorCode;
import com.linksus.entity.UrlEntity;
import com.linksus.service.LinksusAppaccountService;
import com.linksus.service.LinksusInstitutionService;
import com.linksus.service.LinksusInteractAttentionReplyAcctService;
import com.linksus.service.LinksusInteractAttentionReplyService;
import com.linksus.service.LinksusRelationPersonInfoService;
import com.linksus.service.LinksusRelationWeibouserService;
import com.linksus.service.LinksusTaskAttentionUserService;

public class TaskIncrementalAttentionAutoReply extends BaseTask{

	protected static final Logger logger = LoggerFactory.getLogger(TaskIncrementalAttentionAutoReply.class);

	LinksusAppaccountService linksusAppaccountService = (LinksusAppaccountService) ContextUtil
			.getBean("linksusAppaccountService");
	LinksusInstitutionService linksusInstitutionService = (LinksusInstitutionService) ContextUtil
			.getBean("linksusInstitutionService");
	LinksusRelationPersonInfoService linksusRelationPersonInfoService = (LinksusRelationPersonInfoService) ContextUtil
			.getBean("linksusRelationPersonInfoService");
	LinksusRelationWeibouserService linksusRelationWeibouserService = (LinksusRelationWeibouserService) ContextUtil
			.getBean("linksusRelationWeibouserService");

	LinksusTaskAttentionUserService linksusTaskAttentionUserService = (LinksusTaskAttentionUserService) ContextUtil
			.getBean("linksusTaskAttentionUserService");

	LinksusInteractAttentionReplyAcctService linksusInteractAttentionReplyAcctService = (LinksusInteractAttentionReplyAcctService) ContextUtil
			.getBean("linksusInteractAttentionReplyAcctService");

	LinksusInteractAttentionReplyService linksusInteractAttentionReplyService = (LinksusInteractAttentionReplyService) ContextUtil
			.getBean("linksusInteractAttentionReplyService");

	public static final void main(String[] args){
		TaskIncrementalAttentionAutoReply tm = new TaskIncrementalAttentionAutoReply();
		tm.attentionautoReply();

	}

	@Override
	public void cal(){
		attentionautoReply();
	}

	/**
	 * 发布自动关注内容
	 */
	public void attentionautoReply(){

		// 关注用户列表
		LinksusTaskAttentionUser attuser = new LinksusTaskAttentionUser();
		int startCount = (currentPage - 1) * pageSize;
		attuser.setPageSize(pageSize);
		attuser.setStartCount(startCount);
		// 获取新浪/腾讯新增关注账号
		List<LinksusTaskAttentionUser> attuserList = linksusTaskAttentionUserService.getAllAttentionByUserList(attuser);

		if(attuserList != null && attuserList.size() != 0){
			for(LinksusTaskAttentionUser entity : attuserList){
				//				if (entity != null) {
				if(entity.getAccountType() == 1){// 新浪
					// 新浪账号 通过新增关注用户任务表,发表评论
					this.sendSinaComment(entity);
				}
				if(entity.getAccountType() == 2){// 腾讯
					// 腾讯账号 通过新增关注用户任务表,发表评论
					this.sendTencentComment(entity);
				}
				//				}
			}
		}
		checkTaskListEnd(attuserList);// 判断任务是否轮询完成
	}

	// 新浪账号发表评论
	public Boolean sendSinaComment(LinksusTaskAttentionUser entity){

		// 获取发表评论用户token
		LinksusAppaccount linksusAppaccount = linksusAppaccountService.getLinksusAppaccountTokenById(entity
				.getAccountId());
		LinksusInteractAttentionReplyAcct linksusInteractAttentionReplyAcct = linksusInteractAttentionReplyAcctService
				.getLinksusInteractAttentionReplyAcctByAccountId(entity.getAccountId());
		LinksusInteractAttentionReply linksusInteractAttentionReply = null;
		if(linksusInteractAttentionReplyAcct != null){
			linksusInteractAttentionReply = linksusInteractAttentionReplyService
					.getLinksusInteractAttentionReplyById(linksusInteractAttentionReplyAcct.getReplyId());
		}
		LinksusRelationWeibouser linksusRelationWeibouser = linksusRelationWeibouserService
				.getRelationWeibouserByUserId(entity.getUserId());
		try{
			// 判断该用户sina写次数
			int hcount = cache.getCurrHourWriteCountByUser(entity.getUserId().toString(),
					Constants.LIMIT_SEND_COMMENT_PER_HOUR);
			if(hcount == -1){// 超出限制
				return false;
			}
			int status = 3;
			// 如果用户存在			
			if(linksusAppaccount != null && linksusInteractAttentionReply != null){
				// 关注对象存在
				if(linksusRelationWeibouser != null){
					/** 评论发表在用户最新一条微博 */
					// 获取营销对象的前5页微博列表
					Map mapGetPara = new HashMap();
					mapGetPara.put("access_token", linksusAppaccount.getToken());
					mapGetPara.put("count", 5);
					mapGetPara.put("trim_user", 0);
					mapGetPara.put("uid", linksusRelationWeibouser.getRpsId());

					String weiboData = HttpUtil.getRequest(LoadConfig.getUrlEntity("WeiBoData"), mapGetPara);
					if(StringUtils.isNotBlank(weiboData)){
						// 读取从新浪取出的数据
						String statusesFromsina = JsonUtil.getNodeByName(weiboData, "statuses");
						if(!StringUtils.isBlank(statusesFromsina)){
							List<Map> statusesList = (List<Map>) JsonUtil.json2list(statusesFromsina, Map.class);
							if(statusesList != null && statusesList.size() > 0){
								// 取得发送对象的最新微博id
								String weiboId = (String) statusesList.get(0).get("idstr");
								// 向该微博发评论
								Map mapPara = new HashMap();
								mapPara.put("access_token", linksusAppaccount.getToken());
								mapPara.put("comment", linksusInteractAttentionReply.getContent());
								mapPara.put("id", weiboId);
								String strResult = HttpUtil.postRequest(LoadConfig.getUrlEntity("replyWeibo"), mapPara);// 返回评论相关信息					
								LinksusTaskErrorCode error = StringUtil.parseSinaErrorCode(strResult);
								if(error == null){// 不存在sina返回错误代码
									status = 2;
									// 更新用户sina发表评论次数
									cache.addCurrHourWriteCountByUser(entity.getUserId().toString(),
											Constants.LIMIT_SEND_COMMENT_PER_HOUR);
								}
							}
						}else{
							logger.debug(">>>>>>>>>>>>>>>关注回复:weiboData={}", weiboData);
						}
					}
				}
			}
			// 更新关注任务表执行状态
			entity.setStatus(status);
			linksusTaskAttentionUserService.updateLinksusTaskAttentionUser(entity);
		}catch (Exception e){
			LogUtil.saveException(logger, e);
			e.printStackTrace();
		}
		return true;
	}

	// 腾讯账号发表评论
	public Boolean sendTencentComment(LinksusTaskAttentionUser entity){

		// 获取发表评论用户token
		LinksusAppaccount linksusAppaccount = linksusAppaccountService.getLinksusAppaccountTokenById(entity
				.getAccountId());
		LinksusInteractAttentionReplyAcct linksusInteractAttentionReplyAcct = linksusInteractAttentionReplyAcctService
				.getLinksusInteractAttentionReplyAcctByAccountId(entity.getAccountId());
		LinksusInteractAttentionReply linksusInteractAttentionReply = null;
		if(linksusInteractAttentionReplyAcct != null){
			linksusInteractAttentionReply = linksusInteractAttentionReplyService
					.getLinksusInteractAttentionReplyById(linksusInteractAttentionReplyAcct.getReplyId());
		}
		LinksusRelationWeibouser linksusRelationWeibouser = linksusRelationWeibouserService
				.getRelationWeibouserByUserId(entity.getUserId());
		int status = 3;
		try{
			// 如果用户存在
			if(linksusAppaccount != null && linksusInteractAttentionReply != null){
				// 关注对象存在
				if(linksusRelationWeibouser != null){
					/** 评论发表在用户最新一条微博 */
					UrlEntity strurl = LoadConfig.getUrlEntity("TCUserLastWeiboID");
					Map paraMap = new HashMap();
					paraMap.put("access_token", linksusAppaccount.getToken());
					paraMap.put("oauth_consumer_key", cache.getAppInfo(Constants.CACHE_TENCENT_APPINFO).getAppKey());
					paraMap.put("openid", linksusAppaccount.getAppid());
					paraMap.put("fopenid", linksusRelationWeibouser.getRpsId());
					paraMap.put("clientip", Constants.TENCENT_CLIENT_IP);
					paraMap.put("oauth_version", "2.a");
					paraMap.put("format", "json");
					paraMap.put("type", "3");
					paraMap.put("pageflag", 0);
					paraMap.put("pagetime", 0);
					paraMap.put("reqnum", 20);
					paraMap.put("lastid", 0);
					paraMap.put("contenttype", 0);
					boolean hasWeibo = true;
					String tencentRes = HttpUtil.getRequest(strurl, paraMap);
					LinksusTaskErrorCode error = StringUtil.parseTencentErrorCode(tencentRes);
					if(error != null){
						hasWeibo = false;
					}else{
						//校验data是否为空
						String data = JsonUtil.getNodeByName(tencentRes, "data");
						if(StringUtils.isBlank(data) || StringUtils.equals(data, "null")){
							hasWeibo = false;
						}
						//校验info是否为空
						String info = JsonUtil.getNodeByName(data, "info");
						if(StringUtils.isBlank(info) || StringUtils.equals(info, "null")){
							hasWeibo = false;
						}
						if(hasWeibo){
							List<Map> statusesList = (List<Map>) JsonUtil.json2list(info, Map.class);
							if(statusesList != null && statusesList.size() > 0){
								// 取得发送对象的最新微博id
								String weiboId = (String) statusesList.get(0).get("id");
								// 向该微博发评论
								Map mapPara = new HashMap();
								mapPara.put("oauth_consumer_key", cache.getAppInfo(Constants.CACHE_TENCENT_APPINFO)
										.getAppKey());
								mapPara.put("openid", linksusAppaccount.getAppid());
								mapPara.put("clientip", Constants.TENCENT_CLIENT_IP);
								mapPara.put("access_token", linksusAppaccount.getToken());
								mapPara.put("oauth_version", "2.a");
								mapPara.put("format", "json");
								mapPara.put("content", linksusInteractAttentionReply.getContent());
								mapPara.put("reid", weiboId);
								String strResult = HttpUtil.postRequest(LoadConfig.getUrlEntity("TCreplyWeibo"),
										mapPara);// 返回评论相关信息
								error = StringUtil.parseTencentErrorCode(strResult);
								if(error == null){
									status = 2;
								}
							}
						}
					}
				}
			}
		}catch (Exception e){
			LogUtil.saveException(logger, e);
		}
		// 更新关注任务表执行状态
		entity.setStatus(status);
		linksusTaskAttentionUserService.updateLinksusTaskAttentionUser(entity);
		return true;
	}
}