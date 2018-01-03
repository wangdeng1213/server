package com.linksus.task;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linksus.common.Constants;
import com.linksus.common.config.LoadConfig;
import com.linksus.common.module.WeiboInteractCommon;
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
import com.linksus.data.QueueDataSave;
import com.linksus.entity.LinksusAppaccount;
import com.linksus.entity.LinksusInteractWeibo;
import com.linksus.entity.LinksusRelationWeibouser;
import com.linksus.entity.LinksusTaskErrorCode;
import com.linksus.entity.LinksusTaskInteractWeiboMid;
import com.linksus.entity.LinksusTaskWeiboInteract;
import com.linksus.entity.LinksusWeiboPool;
import com.linksus.entity.UrlEntity;
import com.linksus.service.LinksusAppaccountService;
import com.linksus.service.LinksusInteractWeiboService;
import com.linksus.service.LinksusRelationUserAccountService;
import com.linksus.service.LinksusRelationWeibouserService;
import com.linksus.service.LinksusTaskInteractWeiboMidService;
import com.linksus.service.LinksusTaskWeiboInteractService;
import com.linksus.service.LinksusWeiboPoolService;

/**
 * 新浪读取互动数据
 */
public class TaskSinaInteractDataRead extends BaseTask{

	protected static final Logger logger = LoggerFactory.getLogger(TaskSinaInteractDataRead.class);
	private LinksusAppaccountService linksusAppaccountService = (LinksusAppaccountService) ContextUtil
			.getBean("linksusAppaccountService");
	private LinksusTaskWeiboInteractService linksusTaskWeiboInteractService = (LinksusTaskWeiboInteractService) ContextUtil
			.getBean("linksusTaskWeiboInteractService");
	private LinksusRelationUserAccountService linksusRelationUserAccountService = (LinksusRelationUserAccountService) ContextUtil
			.getBean("linksusRelationUserAccountService");
	private LinksusInteractWeiboService linksusInteractWeiboService = (LinksusInteractWeiboService) ContextUtil
			.getBean("linksusInteractWeiboService");
	private LinksusRelationWeibouserService relationWeiboUserService = (LinksusRelationWeibouserService) ContextUtil
			.getBean("linksusRelationWeibouserService");
	private LinksusWeiboPoolService linksusWeiboPoolService = (LinksusWeiboPoolService) ContextUtil
			.getBean("linksusWeiboPoolService");
	private LinksusTaskInteractWeiboMidService linksusTaskInteractWeiboMidService = (LinksusTaskInteractWeiboMidService) ContextUtil
			.getBean("linksusTaskInteractWeiboMidService");
	private boolean flag = false;

	public static void main(String[] args){
		TaskSinaInteractDataRead interactDataRead = new TaskSinaInteractDataRead();
		interactDataRead.setAccountType("1");
		interactDataRead.cal();
	}

	@Override
	public void cal(){
		LinksusAppaccount linksusAppaccount = new LinksusAppaccount();
		int startCount = (currentPage - 1) * pageSize;
		linksusAppaccount.setPageSize(pageSize);
		linksusAppaccount.setStartCount(startCount);
		linksusAppaccount.setType(accountType);
		List<LinksusAppaccount> appaccountList = linksusAppaccountService.getLinksusAppaccountList(linksusAppaccount);
		for(LinksusAppaccount appaccount : appaccountList){
			//if(appaccount.getId() == 1){

			try{
				interactAtMeRun(appaccount);//处理互动转发或@数据
			}catch (Exception e){
				LogUtil.saveException(logger, e);
				e.printStackTrace();
			}

			try{
				interactComAtmeRun(appaccount);//评论并@
			}catch (Exception e){
				LogUtil.saveException(logger, e);
				e.printStackTrace();
			}

			try{
				interactComToMeRun(appaccount);//处理互动评论数据
			}catch (Exception e){
				LogUtil.saveException(logger, e);
				e.printStackTrace();
			}
			if(flag == true){
				dealTempData(appaccount);//处理临时表中的数据
				flag = false;

			}
			//}
		}
		checkTaskListEnd(appaccountList);//判断任务是否轮询完成
	}

	//处理临时表中的数据
	private void dealTempData(LinksusAppaccount appaccount){
		try{
			int currentPage = 1;
			int count = 200;
			while (currentPage > 0){
				//从temp表中查询数据
				//将参数放到Map中再传到sql
				int countPage = (currentPage - 1) * count;
				Map mapTemp = new HashMap();
				mapTemp.put("accountId", appaccount.getId());
				long id = appaccount.getId();
				mapTemp.put("countPage", Integer.valueOf(countPage));
				List<LinksusTaskInteractWeiboMid> linksusTaskInteractWeiboMidList = linksusTaskInteractWeiboMidService
						.getWeiboMidTempListByMap(mapTemp);
				if(linksusTaskInteractWeiboMidList.size() == 0){
					break;
				}
				for(int j = 0; j < linksusTaskInteractWeiboMidList.size(); j++){
					//更新
					LinksusTaskInteractWeiboMid interactWeiboMid = linksusTaskInteractWeiboMidList.get(j);
					Long userId = interactWeiboMid.getUserId();
					Long personId = interactWeiboMid.getPersonId();
					Long accountId = interactWeiboMid.getAccountId();
					Long weiboId = interactWeiboMid.getMid();
					Long commentId = interactWeiboMid.getCommentId();
					Long sourceWeiboId = interactWeiboMid.getReplyId();
					Integer interactTime = interactWeiboMid.getInteractTime();
					String content = interactWeiboMid.getContent();
					Long inteWeiboId = PrimaryKeyGen.getPrimaryKey("linksus_interact_weibo", "pid");//互动信息主键
					WeiboInteractCommon interactCommon = new WeiboInteractCommon();
					Long recordId = interactCommon.dealWeiboInteract(userId, personId, accountId, 1, weiboId,
							interactWeiboMid.getInteractType().toString(), inteWeiboId, interactTime);
					//插入互动信息表中
					LinksusInteractWeibo linksusInteractWeibo = new LinksusInteractWeibo();
					linksusInteractWeibo.setPid(inteWeiboId);
					linksusInteractWeibo.setRecordId(recordId);
					linksusInteractWeibo.setUserId(userId);
					linksusInteractWeibo.setAccountId(accountId);
					linksusInteractWeibo.setAccountType(1);
					linksusInteractWeibo.setCommentId(commentId);
					linksusInteractWeibo.setWeiboId(weiboId);
					linksusInteractWeibo.setSourceWeiboId(sourceWeiboId);
					linksusInteractWeibo.setContent(content);
					linksusInteractWeibo.setInteractType(interactWeiboMid.getInteractType());
					linksusInteractWeibo.setReplyMsgId(new Long(0));
					linksusInteractWeibo.setSendType(new Integer(0));
					linksusInteractWeibo.setStatus(new Integer(0));
					linksusInteractWeibo.setSendTime(0);
					linksusInteractWeibo.setInstPersonId(new Long(0));
					linksusInteractWeibo.setInteractTime(interactTime);
					//linksusInteractWeiboService.insertLinksusInteractWeibo(linksusInteractWeibo);
					QueueDataSave.addDataToQueue(linksusInteractWeibo, Constants.OPER_TYPE_INSERT);

				}
				if(linksusTaskInteractWeiboMidList.size() < count){//没有下一页数据
					break;
				}
				currentPage++;
			}
		}catch (Exception e){
			LogUtil.saveException(logger, e);
			e.printStackTrace();
		}

		//清除临时表中数据
		linksusTaskInteractWeiboMidService.deleteLinksusTaskInteractWeiboMid();
	}

	//评论并@
	private void interactComAtmeRun(LinksusAppaccount appaccount) throws Exception{
		UrlEntity interactData = LoadConfig.getUrlEntity("InteractDataSyncComments");
		Long accountId = appaccount.getId();
		//String strToken = "2.00NaVf3BeE2s5B2f1113d612012bdT";//appaccount.getToken();
		String strToken = appaccount.getToken();
		String appId = appaccount.getAppid();
		LinksusTaskWeiboInteract linksusTaskWeiboInteract = new LinksusTaskWeiboInteract();
		linksusTaskWeiboInteract.setAccountId(accountId);
		linksusTaskWeiboInteract.setInteractType(4);
		LinksusTaskWeiboInteract WeiboInteract = linksusTaskWeiboInteractService
				.getMaxIdByAccountId(linksusTaskWeiboInteract);
		//String mId = "3700319760124869";//取出上次更新过的最大maxid 作为参数从新浪去取新的数据
		String mId = "0";//取出上次更新过的最大maxid 作为参数从新浪去取新的数据
		if(WeiboInteract != null){
			mId = WeiboInteract.getMaxId().toString();
		}
		int count = 200;//单页返回的最大数
		int currentPage = 1;
		//取得max_id
		String maxId = "";
		Map params = new HashMap();
		params.put("access_token", strToken);
		params.put("since_id", mId);
		params.put("count", 1);
		params.put("page", currentPage);
		String strResult = HttpUtil.getRequest(interactData, params);
		LinksusTaskErrorCode error = StringUtil.parseSinaErrorCode(strResult);
		if(error != null){
			logger.info(">>>>>>>>>>>>>>>ComAtme 出现error:{}", strResult);
			return;
		}
		String commentsFromsina = JsonUtil.getNodeByName(strResult, "comments");
		if(StringUtils.isNotBlank(commentsFromsina)){
			List<?> commentsList = (List<?>) JsonUtil.json2list(commentsFromsina, Map.class);
			if(commentsList == null || commentsList.size() == 0){
				logger.debug(">>>>>>>>>>>无新数据>>>>>>>>>>");
				return;
			}
			flag = true;
			Map map = (Map) commentsList.get(0);
			maxId = map.get("id") + "";
			//查询
			if(WeiboInteract == null){//微博互动任务记录表中无数据，插入新数据
				LinksusTaskWeiboInteract newTaskWeiboInteract = new LinksusTaskWeiboInteract();
				newTaskWeiboInteract.setAccountId(accountId);
				newTaskWeiboInteract.setInteractType(4);
				newTaskWeiboInteract.setMaxId(Long.valueOf(maxId));
				newTaskWeiboInteract.setPagetime(0L);
				linksusTaskWeiboInteractService.insertLinksusTaskWeiboInteract(newTaskWeiboInteract);
			}
			logger.debug(">>>>>>>>>>>>>>>>>>>>>取得maxId:{}", maxId);

			while (currentPage > 0){
				//得到maxId作为参数从新浪取数据
				Map paramsMap = new HashMap();
				paramsMap.put("access_token", strToken);
				paramsMap.put("since_id", mId);
				paramsMap.put("max_id", maxId);
				paramsMap.put("count", count);
				paramsMap.put("page", currentPage);
				strResult = HttpUtil.getRequest(interactData, paramsMap);
				error = StringUtil.parseSinaErrorCode(strResult);
				if(error != null){
					logger.info(">>>>>>>>>>>>>>>ComAtme1 出现error:{}", strResult);
					break;
				}
				commentsFromsina = JsonUtil.getNodeByName(strResult, "comments");
				commentsList = (List<?>) JsonUtil.json2list(commentsFromsina, Map.class);
				if(commentsList.size() <= 0){
					break;
				}
				for(int i = 0; i < commentsList.size(); i++){
					Map<?, ?> comMap = (Map<?, ?>) commentsList.get(i);
					Map weiboMap = (Map) comMap.get("status"); //评论的微博信息字段
					Map userMap = (Map) comMap.get("user");
					String rpsId = userMap.get("id").toString();//平台用户id
					Long mid = new Long((String) weiboMap.get("mid"));//微博id
					String text = (String) comMap.get("text");
					String time = (String) comMap.get("created_at");
					Date createTime = DateUtil.parse(time, "EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
					Integer createTimeInteger = Integer.parseInt(String.valueOf(createTime.getTime() / 1000));
					String replyid = "0";//原微博id
					String commentid = (String) comMap.get("idstr");//字符串型微博id
					Map weiboUserMap = (Map) weiboMap.get("user");
					if(StringUtils.equals(rpsId, appId)){//userId=companyid跳出循环 自己评论并@自己
						continue;
					}

					Map<?, ?> replyComment = (Map<?, ?>) comMap.get("reply_comment");//当本评论属于对另一评论的回复是返回该字段
					if(replyComment != null){
						//评论上一个评论的id
						replyid = (String) replyComment.get("idstr");
					}
					//检查评论并@账号的用户是否存在 
					LinksusRelationWeibouser user = getWeiboUserInfo(userMap, appaccount, true, 4);
					// updateRelationTypeAndNum(user,"4",appaccount);
					//检查原微博用户是否存在
					LinksusRelationWeibouser oldUser = getWeiboUserInfo(weiboUserMap, appaccount, false, 0);
					//检查微博是否存在
					checkWeiboIsExsit(String.valueOf(mid), String.valueOf(accountType), weiboMap, "", oldUser);
					//微博用户表中的userId取出人员id
					Long personId = 0L;
					Long userId = 0L;//用户id
					if(user != null){
						userId = user.getUserId();
						personId = user.getPersonId();
					}
					//插入互动信息表临时表
					LinksusTaskInteractWeiboMid linksusInteractWeibo = new LinksusTaskInteractWeiboMid();
					linksusInteractWeibo.setRpsId(new Long(rpsId));//微博用户标识
					linksusInteractWeibo.setUserId(userId);
					linksusInteractWeibo.setPersonId(personId);
					linksusInteractWeibo.setAccountId(accountId);//平台账号id
					linksusInteractWeibo.setCommentId(new Long(commentid));//当前评论的id
					linksusInteractWeibo.setReplyId(StringUtils.isBlank(replyid) ? 0L : new Long(replyid));//回复评论id
					linksusInteractWeibo.setMid(mid);//微博id
					linksusInteractWeibo.setContent(text);//内容:转发/@/评论内容
					linksusInteractWeibo.setInteractType(4);//互动类型
					linksusInteractWeibo.setInteractTime(createTimeInteger);//互动时间
					linksusTaskInteractWeiboMidService.insertLinksusTaskInteractWeiboMid(linksusInteractWeibo);
				}
				if(commentsList.size() < count){//没有下一页数据
					break;
				}
				currentPage++;
			}//while循环结束
			if(!StringUtils.isBlank(maxId)){
				LinksusTaskWeiboInteract linksusTaskWeibo = new LinksusTaskWeiboInteract();
				linksusTaskWeibo.setAccountId(accountId);
				linksusTaskWeibo.setInteractType(4);
				linksusTaskWeibo.setMaxId(new Long(maxId));
				linksusTaskWeibo.setPagetime(new Long(0));
				linksusTaskWeiboInteractService.updateLinksusTaskWeiboInteract(linksusTaskWeibo);
			}
		}
	}

	//新浪获取@我的微博列表 转发或@,通过内容判断为@或者是转发
	private void interactAtMeRun(LinksusAppaccount appaccount) throws Exception{
		//		int a = 1, b = 0;
		//		a = a / b;
		UrlEntity interactData = LoadConfig.getUrlEntity("InteractDataSync"); //获取@当前用户的最新微博  
		Long accountId = appaccount.getId();
		//String strToken = "2.00NaVf3BeE2s5B2f1113d612012bdT"
		String strToken = appaccount.getToken();
		String appId = appaccount.getAppid();
		LinksusTaskWeiboInteract linksusTaskWeiboInteract = new LinksusTaskWeiboInteract();
		linksusTaskWeiboInteract.setAccountId(accountId);
		linksusTaskWeiboInteract.setInteractType(3);
		LinksusTaskWeiboInteract WeiboInteract = linksusTaskWeiboInteractService
				.getMaxIdByAccountId(linksusTaskWeiboInteract);
		//String mId = "3700319760124869";//取出上次更新过的最大maxid 作为参数从新浪去取新的数据
		String mId = "0";//取出上次更新过的最大maxid 作为参数从新浪去取新的数据
		if(WeiboInteract != null){
			mId = WeiboInteract.getMaxId().toString();
		}
		int count = 200;//单页返回的最大数
		int currentPage = 1;
		//取得max_id更新到数据库表中 
		String maxId = "";
		Map params = new HashMap();
		params.put("access_token", strToken);
		params.put("since_id", mId);
		params.put("count", 1);
		params.put("page", currentPage);
		String strResult = HttpUtil.getRequest(interactData, params);
		LinksusTaskErrorCode error = StringUtil.parseSinaErrorCode(strResult);
		if(error != null){
			logger.info(">>>>>>>>>>>>>>>@ 出现error:{}", strResult);
			return;
		}
		String statusesFromsina = JsonUtil.getNodeByName(strResult, "statuses");
		if(StringUtils.isNotBlank(statusesFromsina)){
			List<?> statusesList = (List<?>) JsonUtil.json2list(statusesFromsina, Map.class);
			if(statusesList == null || statusesList.size() == 0){
				logger.debug(">>>>>>>>>>>无新数据>>>>>>>>>>");
				return;
			}
			flag = true;
			Map map = (Map) statusesList.get(0);
			maxId = map.get("id") + "";
			if(WeiboInteract == null){//微博互动任务记录表中无数据，插入新数据
				LinksusTaskWeiboInteract newTaskWeiboInteract = new LinksusTaskWeiboInteract();
				newTaskWeiboInteract.setAccountId(accountId);
				newTaskWeiboInteract.setInteractType(3);
				newTaskWeiboInteract.setMaxId(Long.valueOf(maxId));
				newTaskWeiboInteract.setPagetime(0L);
				linksusTaskWeiboInteractService.insertLinksusTaskWeiboInteract(newTaskWeiboInteract);
			}
			logger.debug(">>>>>>>>>>>>>>>>>>>>>取得maxId:{}", maxId);
			while (currentPage > 0){
				//得到maxId作为参数从新浪取数据
				Map paramsMap = new HashMap();
				paramsMap.put("access_token", strToken);
				paramsMap.put("since_id", mId);
				paramsMap.put("max_id", maxId);
				paramsMap.put("count", count);
				paramsMap.put("page", currentPage);
				strResult = HttpUtil.getRequest(interactData, paramsMap);
				logger.debug(">>>>>>>>>>>>>>>>>>>>>at me - sina:{}", interactData);
				//遍历strResult从新浪取出的数据
				error = StringUtil.parseSinaErrorCode(strResult);
				if(error != null){
					logger.info(">>>>>>>>>>>>>>>@1 出现error:{}", strResult);
					break;
				}
				statusesFromsina = JsonUtil.getNodeByName(strResult, "statuses");
				statusesList = (List<?>) JsonUtil.json2list(statusesFromsina, Map.class);
				if(statusesList == null || statusesList.size() <= 0){
					break;
				}
				for(int i = 0; i < statusesList.size(); i++){
					String pmid = "0";//原微博id
					String type = "3";//类型为@
					Map<?, ?> statusMap = (Map<?, ?>) statusesList.get(i);
					//String midStr=(String)statusMap.get("idstr");//字符串型微博id
					String content = (String) statusMap.get("text");
					String time = (String) statusMap.get("created_at");
					Date createTime = DateUtil.parse(time, "EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
					Integer createTimeInteger = Integer.parseInt(String.valueOf(createTime.getTime() / 1000));
					Map userMap = (Map) statusMap.get("user");//微博作者的用户信息字段
					String rpsId = userMap.get("id").toString();
					Long weiboId = new Long(statusMap.get("id") + "");//微博id
					if(appId.equals(userMap.get("id").toString())){//如果是自己@自己的不执行操作
						continue;
					}
					//Long mid=(Long)statusMap.get("id");
					//如果是转发 检查转发的微博是否存在
					Map<?, ?> retweetedStatus = (Map<?, ?>) statusMap.get("retweeted_status");//被转发的微博信息字段
					Map tmpMap = new HashMap();
					String tmpId = "";
					if(retweetedStatus != null){
						String deleteFlag = (String) retweetedStatus.get("deleted");
						if(!StringUtils.equals(deleteFlag, "1")){
							tmpMap = (Map) retweetedStatus.get("user");//被转发原微博用户信息
							if(tmpMap != null && tmpMap.size() > 0){
								tmpId = (String) tmpMap.get("idstr"); //被转发原微博用户id
							}
							pmid = (String) retweetedStatus.get("idstr");//被转发的微博id
							if(StringUtils.isNotBlank(pmid)){
								if(appId.equals(tmpId)){
									type = "2";//类型为转发	
								}
								LinksusRelationWeibouser user = getWeiboUserInfo(tmpMap, appaccount, false, 0);
								checkOldWeiboIsExsit(appaccount.getToken(), new Long(pmid), "", user);
							}
							//				    	if(appId.equals(tmpId)){
							//					    	 type="2";//类型为转发
							//					    	 LinksusRelationWeibouser user=getWeiboUserInfo(tmpMap ,appaccount);
							//					    	// updateRelationTypeAndNum(user,"2",appaccount);		
							//					    	 checkOldWeiboIsExsit(appaccount.getToken(),new Long(pmid),user);
							//				    	}
						}
					}
					//检查@账号的用户是否存在 
					LinksusRelationWeibouser user = getWeiboUserInfo(userMap, appaccount, true, new Integer(type));
					//updateRelationTypeAndNum(user,"3",appaccount);
					//检查微博是否存在
					checkWeiboIsExsit(String.valueOf(weiboId), String.valueOf(accountType), statusMap, pmid, user);
					//微博用户表中的userId取出人员id
					Long personId = 0L;
					Long userId = 0L;//用户id
					if(user != null){
						userId = user.getUserId();
						personId = user.getPersonId();
					}
					//插入互动信息表临时表
					LinksusTaskInteractWeiboMid linksusInteractWeibo = new LinksusTaskInteractWeiboMid();
					linksusInteractWeibo.setRpsId(new Long(rpsId));//微博用户标识
					linksusInteractWeibo.setUserId(userId);
					linksusInteractWeibo.setPersonId(personId);
					linksusInteractWeibo.setAccountId(accountId);//平台账号id
					linksusInteractWeibo.setCommentId(0L);//当前评论的id
					linksusInteractWeibo.setReplyId(new Long(pmid));//
					linksusInteractWeibo.setMid(weiboId);//微博id
					linksusInteractWeibo.setContent(content);//内容:转发/@/评论内容
					linksusInteractWeibo.setInteractType(Integer.valueOf(type));//互动类型
					linksusInteractWeibo.setInteractTime(createTimeInteger);//互动时间
					linksusTaskInteractWeiboMidService.insertLinksusTaskInteractWeiboMid(linksusInteractWeibo);
				}
				if(statusesList.size() < count){//没有下一页数据
					break;
				}
				currentPage++;
			}
			// 更新数据互动表中的最大id 
			if(!StringUtils.isBlank(maxId)){
				LinksusTaskWeiboInteract linksusTaskWeibo = new LinksusTaskWeiboInteract();
				linksusTaskWeibo.setAccountId(accountId);
				linksusTaskWeibo.setInteractType(3);
				linksusTaskWeibo.setMaxId(new Long(maxId));
				linksusTaskWeibo.setPagetime(new Long(0));
				linksusTaskWeiboInteractService.updateLinksusTaskWeiboInteract(linksusTaskWeibo);
			}
		}
	}

	//新浪获取我收到的评论列表
	private void interactComToMeRun(LinksusAppaccount appaccount) throws Exception{
		Long accountId = appaccount.getId();
		//String strToken = "2.00NaVf3BeE2s5B2f1113d612012bdT";// appaccount.getToken();
		String strToken = appaccount.getToken();
		UrlEntity interactData = LoadConfig.getUrlEntity("InteractDataSyncComToMe");
		LinksusTaskWeiboInteract linksusTaskWeiboInteract = new LinksusTaskWeiboInteract();
		linksusTaskWeiboInteract.setAccountId(accountId);
		linksusTaskWeiboInteract.setInteractType(1);
		LinksusTaskWeiboInteract WeiboInteract = linksusTaskWeiboInteractService
				.getMaxIdByAccountId(linksusTaskWeiboInteract);//获取互动临界表
		//String mId = "3700319760124869";
		String mId = "0";
		if(WeiboInteract != null){
			mId = WeiboInteract.getMaxId().toString();
		}
		int count = 200;//单页返回的最大数
		int currentPage = 1;
		//取得max_id
		String maxId = "";
		Map params = new HashMap();
		params.put("access_token", strToken);
		params.put("since_id", mId);
		params.put("count", 1);
		params.put("page", currentPage);
		String resultData = HttpUtil.getRequest(interactData, params);
		LinksusTaskErrorCode error = StringUtil.parseSinaErrorCode(resultData);
		if(error != null){
			logger.info(">>>>>>>>>>>>>>>ComToMe 出现error:{}", resultData);
			return;
		}
		logger.debug(">>>>>>>>>>>>>>>>>>>>>查询maxId:{}", interactData);
		String commentsFromsina = JsonUtil.getNodeByName(resultData, "comments");
		if(StringUtils.isNotBlank(commentsFromsina)){
			List<?> commentsList = (List<?>) JsonUtil.json2list(commentsFromsina, Map.class);
			if(commentsList == null || commentsList.size() == 0){
				logger.debug(">>>>>>>>>>>无新数据>>>>>>>>>>");
				return;
			}
			flag = true;
			Map map = (Map) commentsList.get(0);
			maxId = map.get("id") + "";
			if(WeiboInteract == null){//微博互动任务记录表中无数据，插入新数据
				LinksusTaskWeiboInteract newTaskWeiboInteract = new LinksusTaskWeiboInteract();
				newTaskWeiboInteract.setAccountId(accountId);
				newTaskWeiboInteract.setInteractType(1);
				newTaskWeiboInteract.setMaxId(Long.valueOf(maxId));
				newTaskWeiboInteract.setPagetime(0L);
				linksusTaskWeiboInteractService.insertLinksusTaskWeiboInteract(newTaskWeiboInteract);
			}
			logger.debug(">>>>>>>>>>>>>>>>>>>>>取得maxId:{}", maxId);
			while (currentPage > 0){
				//得到maxId作为参数从新浪取数据
				Map paramsMap = new HashMap();
				paramsMap.put("access_token", strToken);
				paramsMap.put("since_id", mId);
				paramsMap.put("max_id", maxId);
				paramsMap.put("count", count);
				paramsMap.put("page", currentPage);
				String strResult = HttpUtil.getRequest(interactData, paramsMap);
				//遍历strResult从新浪取出的数据
				error = StringUtil.parseSinaErrorCode(resultData);
				if(error != null){
					logger.info(">>>>>>>>>>>>>>>ComToMe1 出现error:{}", strResult);
					break;
				}
				commentsFromsina = JsonUtil.getNodeByName(strResult, "comments");
				commentsList = (List<?>) JsonUtil.json2list(commentsFromsina, Map.class);
				if(commentsList == null || commentsList.size() <= 0){
					break;
				}
				for(int i = 0; i < commentsList.size(); i++){//得到评论列表
					Map<?, ?> commentMap = (Map<?, ?>) commentsList.get(i);
					Long commentId = (Long) commentMap.get("id");//评论ID
					String content = (String) commentMap.get("text");//评论内容
					String replyid = "0";//回复评论id
					Map<?, ?> replyComment = (Map<?, ?>) commentMap.get("reply_comment");//当本评论属于对另一评论的回复是返回该字段
					if(replyComment != null){
						//评论上一个评论的id 即回复评论的id
						replyid = (String) replyComment.get("idstr");
					}
					String time = (String) commentMap.get("created_at");
					Date createTime = DateUtil.parse(time, "EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
					Integer createTimeInteger = Integer.parseInt(String.valueOf(createTime.getTime() / 1000));
					Map userMap = (Map) commentMap.get("user");//得到评论用户的信息
					Map statusMap = (Map) commentMap.get("status");
					Long weiboId = new Long(statusMap.get("id") + "");//微博id
					//获取评论微博作者的id
					Map weiboMap = (Map) statusMap.get("user");
					if(weiboMap != null){
						if(content.contains("@" + weiboMap.get("name") + "")
								&& !content.startsWith("回复@" + weiboMap.get("name") + "")){//如果是用户评论当前账号微博并@账号的不执行操作（评论并@时数据重复）
							//if(content.contains("@" + weiboMap.get("name")+"")){
							continue;
						}
					}else{
						logger.error("user为空");
						continue;
					}
					//微博用户表中取出用户的userId 
					String rpsId = String.valueOf(userMap.get("idstr")); //取得微博用户标识
					String weiboUserName = (String) userMap.get("idstr");//
					//检查评论微博用户是否存在
					LinksusRelationWeibouser user = getWeiboUserInfo(userMap, appaccount, true, 1);
					//updateRelationTypeAndNum(user,"1",appaccount);
					//检查原微博用户是否存在
					LinksusRelationWeibouser oldUser = getWeiboUserInfo(weiboMap, appaccount, false, 0);
					//检查评论的源微博是否存在 
					checkWeiboIsExsit(String.valueOf(weiboId), String.valueOf(accountType), statusMap, "", oldUser);
					//微博用户表中的userId取出人员id
					Long personId = 0L;
					Long userId = 0L;//用户id
					if(user != null){
						userId = user.getUserId();
						personId = user.getPersonId();
					}
					//插入互动信息表临时表
					LinksusTaskInteractWeiboMid linksusInteractWeibo = new LinksusTaskInteractWeiboMid();
					linksusInteractWeibo.setRpsId(new Long(rpsId));//微博用户标识
					linksusInteractWeibo.setUserId(userId);
					linksusInteractWeibo.setPersonId(personId);
					linksusInteractWeibo.setAccountId(accountId);//平台账号id
					linksusInteractWeibo.setCommentId(commentId);//当前评论的id
					linksusInteractWeibo.setReplyId(StringUtils.isBlank(replyid) ? 0L : new Long(replyid));//回复评论id
					linksusInteractWeibo.setMid(weiboId);//微博id
					linksusInteractWeibo.setContent(content);//内容:转发/@/评论内容
					if(content.startsWith("回复@" + weiboMap.get("name") + "")){
						linksusInteractWeibo.setInteractType(4);//互动类型
					}else{
						linksusInteractWeibo.setInteractType(1);//互动类型	
					}
					linksusInteractWeibo.setInteractTime(createTimeInteger);//互动时间
					linksusTaskInteractWeiboMidService.insertLinksusTaskInteractWeiboMid(linksusInteractWeibo);
				}
				if(commentsList.size() < count){//没有下一页数据
					break;
				}
				currentPage++;
			}
			if(!StringUtils.isBlank(maxId)){//更新互动任务表中的最大maxid
				LinksusTaskWeiboInteract linksusTaskWeibo = new LinksusTaskWeiboInteract();
				linksusTaskWeibo.setAccountId(accountId);
				linksusTaskWeibo.setInteractType(1);
				linksusTaskWeibo.setMaxId(new Long(maxId));
				linksusTaskWeibo.setPagetime(new Long(0));
				linksusTaskWeiboInteractService.updateLinksusTaskWeiboInteract(linksusTaskWeibo);
			}
		}
	}

	//检查原微博是否存在
	public void checkOldWeiboIsExsit(String strToken,Long pmid,String srcMid,LinksusRelationWeibouser user){
		if(pmid != 0){
			//判断微博是否存在1
			//	        Map weiboPoolMap =new HashMap();
			//			weiboPoolMap.put("mid",pmid);
			//			weiboPoolMap.put("weiboType",accountType);
			//			LinksusWeiboPool weiboCount = linksusWeiboPoolService.checkWeiboPoolIsExsit(weiboPoolMap);
			String weiboRedis = RedisUtil.getRedisHash("weibo_pool", pmid + "#" + "1");
			//将数据发送到新浪接口 获取数据 
			UrlEntity transInter = LoadConfig.getUrlEntity("InteractDataSyncTrans");
			Map params = new HashMap();
			params.put("access_token", strToken);
			params.put("id", pmid);
			String strResult = HttpUtil.getRequest(transInter, params);
			LinksusTaskErrorCode error = StringUtil.parseSinaErrorCode(strResult);
			// 如果有错不做处理
			if(error != null){
				return;
			}else{//更新微博池中的 数据
				Map weiboMap = (Map) JsonUtil.json2map(strResult, Map.class);
				if(user != null){
					LinksusWeiboPool weiboPool = new LinksusWeiboPool();
					if(!StringUtils.isBlank(weiboRedis)){//存在更新
						weiboPool.setMid(new Long(pmid));
						weiboPool.setWeiboType(1);
						weiboPool.setRepostCount(weiboMap.get("reposts_count") == null ? 0 : (Integer) weiboMap
								.get("reposts_count"));
						weiboPool.setCommentCount(weiboMap.get("comments_count") == null ? 0 : (Integer) weiboMap
								.get("comments_count"));
						linksusWeiboPoolService.updateLinksusWeiboPoolDataCount(weiboPool);
					}else{//不存在插入
						weiboPool.setMid(new Long(pmid));
						//						Map userMap = new HashMap();
						//						String weiboUser = JsonUtil.getNodeByName(strResult, "user");
						//						userMap = JsonUtil.json2map(weiboUser, Map.class);
						//						String rpsId = userMap.get("id") + "";
						copySinaDataToWeiboPool(weiboMap, weiboPool);
						//用户id
						weiboPool.setUid(user.getUserId());
						//用户名称
						weiboPool.setUname(user.getRpsScreenName());
						//用户头像
						weiboPool.setUprofileUrl(user.getRpsProfileImageUrl());
						weiboPool.setWeiboType(1);

						String currUrl = "http://weibo.com/" + user.getRpsId() + "/" + WeiboUtil.Id2Mid(new Long(pmid));
						weiboPool.setCurrentUrl(currUrl);
						//原id
						long srcMidL = 0l;
						if(StringUtils.isNotBlank(srcMid)){
							srcMidL = new Long(srcMid);
						}
						weiboPool.setSrcMid(srcMidL);
						QueueDataSave.addDataToQueue(weiboPool, Constants.OPER_TYPE_INSERT);
						// linksusWeiboPoolService.insertLinksusWeiboPool(weiboPool);
					}
				}
			}
		}
	}

	/**
	 *  检查微博是否存在 
	 * */
	public void checkWeiboIsExsit(String mid,String accountType,Map statusMap,String srcMid,
			LinksusRelationWeibouser user){
		//判断微博是否存在
		//    	 Map weiboPoolMap =new HashMap();
		//		 weiboPoolMap.put("mid",mid);
		//		 weiboPoolMap.put("weiboType",accountType);
		//		 LinksusWeiboPool weiboCount = linksusWeiboPoolService.checkWeiboPoolIsExsit(weiboPoolMap);
		String weiboRedis = RedisUtil.getRedisHash("weibo_pool", mid + "#" + accountType);
		if(user != null){
			LinksusWeiboPool weiboPool = new LinksusWeiboPool();
			if(!StringUtils.isBlank(weiboRedis)){//存在更新
				if(StringUtils.isNotBlank(statusMap.get("reposts_count") + "")
						&& StringUtils.isNotBlank(statusMap.get("comments_count") + "")
						&& (Integer) statusMap.get("reposts_count") != 0
						&& (Integer) statusMap.get("comments_count") != 0){
					weiboPool.setMid(new Long(mid));
					weiboPool.setWeiboType(Integer.valueOf(accountType));
					weiboPool.setRepostCount(statusMap.get("reposts_count") == null ? 0 : (Integer) statusMap
							.get("reposts_count"));
					weiboPool.setCommentCount(statusMap.get("comments_count") == null ? 0 : (Integer) statusMap
							.get("comments_count"));
					linksusWeiboPoolService.updateLinksusWeiboPoolDataCount(weiboPool);
				}
			}else{//不存在插入
				weiboPool.setMid(new Long(mid));
				copySinaDataToWeiboPool(statusMap, weiboPool);
				//用户id
				weiboPool.setUid(user.getUserId());
				//用户名称
				weiboPool.setUname(user.getRpsScreenName());
				//用户头像
				weiboPool.setUprofileUrl(user.getRpsProfileImageUrl());
				weiboPool.setWeiboType(Integer.valueOf(accountType));
				//if(!StringUtils.isBlank(openId) && !StringUtils.isBlank(weiboId)){
				String currUrl = "http://weibo.com/" + user.getRpsId() + "/" + WeiboUtil.Id2Mid(new Long(mid));
				weiboPool.setCurrentUrl(currUrl);
				//原id
				long srcMidL = 0l;
				if(StringUtils.isNotBlank(srcMid)){
					srcMidL = new Long(srcMid);
				}
				weiboPool.setSrcMid(srcMidL);
				QueueDataSave.addDataToQueue(weiboPool, Constants.OPER_TYPE_INSERT);
				// linksusWeiboPoolService.insertLinksusWeiboPool(weiboPool);
			}
		}
	}

	/**
	 *  处理完微博用户，查询微博用户的信息
	* @throws Exception 
	 * */
	public LinksusRelationWeibouser getWeiboUserInfo(Map userMap,LinksusAppaccount appaccount,boolean flag,
			int interactBit) throws Exception{
		String userStr = JsonUtil.map2json(userMap);
		String accountId = String.valueOf(appaccount.getId());
		Long institutionId = appaccount.getInstitutionId();
		//获取评论用户的新浪appId 和  业务类型判断用户是否存在微博用户表中
		WeiboUserCommon weiboUser = new WeiboUserCommon();
		Map parmMap = new HashMap();
		parmMap.put("token", appaccount.getToken());
		parmMap.put("openid", appaccount.getAppid());
		parmMap.put("type", String.valueOf(accountType));
		parmMap.put("appkey", appaccount.getAppKey());
		parmMap.put("AccountId", accountId);
		parmMap.put("InstitutionId", institutionId);
		String updateType = "";
		if(flag){//flag为true 更新互动
			updateType = "3";
		}else{
			updateType = "0";
		}
		LinksusRelationWeibouser user = weiboUser.dealWeiboUserInfo(userStr, "2", parmMap, false, updateType,
				interactBit);
		//			//微博用户表中取出用户的userId 
		//			String rpsId = String.valueOf(userMap.get("idstr")); 
		//			//根据rpsId和accountType 判断用户是否存在
		//			Map paraMap =new HashMap();
		//			paraMap.put("rpsId",rpsId);
		//			paraMap.put("userType",accountType);
		//		    LinksusRelationWeibouser user=relationWeiboUserService.getWeibouserByrpsIdAndType(paraMap);
		return user;
	}

	//复制新浪数据到微博池表
	public void copySinaDataToWeiboPool(Map weiboMap,LinksusWeiboPool weiboPool){
		//微博id
		//weiboPool.setMid(new Long(weiboMap.get("id")+""));
		//微博类型
		//weiboPool.setWeiboType();
		//内容
		weiboPool.setContent((String) weiboMap.get("text"));
		//读取多图
		List<Map> listPic = (List<Map>) weiboMap.get("pic_urls");
		String originalUrl = "";
		String middleUrl = "";
		String thumbUrl = "";
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
		weiboPool.setPicOriginalUrl(StringUtils.isBlank(originalUrl) ? "0" : originalUrl.substring(0, originalUrl
				.length() - 1));
		// 中图
		weiboPool
				.setPicMiddleUrl(StringUtils.isBlank(middleUrl) ? "0" : middleUrl.substring(0, middleUrl.length() - 1));
		// 小图
		weiboPool.setPicThumbUrl(StringUtils.isBlank(thumbUrl) ? "0" : thumbUrl.substring(0, thumbUrl.length() - 1));
		//音频地址
		weiboPool.setMusicUrl("0");
		//视频地址
		weiboPool.setVideoUrl("0");
		//评论数
		weiboPool.setCommentCount(Integer.valueOf(weiboMap.get("comments_count") + ""));
		//转发数
		weiboPool.setRepostCount(Integer.valueOf(weiboMap.get("reposts_count") + ""));
		//原id
		//weiboPool.setSrcMid(new Long(weiboMap.get("mid") + ""));
		//地址信息
		weiboPool.setGeo("0");
		//用户id
		// ------------weiboPool.setUid((String)weiboMap.get("id"));
		//用户名称
		// --------------weiboPool.setUname((String)weiboMap.get("id"));
		//用户头像
		//--------------weiboPool.setUprofileUrl((String)weiboMap.get("id"));
		//发布类型
		weiboPool.setPublishType(0);
		//来源
		weiboPool.setSource((String) weiboMap.get("source"));
		//创建时间
		Date createTime = DateUtil.parse(weiboMap.get("created_at").toString(), "EEE MMM dd HH:mm:ss zzz yyyy",
				Locale.US);
		weiboPool.setCreatedTime(Integer.valueOf(String.valueOf(createTime.getTime() / 1000)));
		//微博的URL
		//weiboPool.setCurrentUrl("0");

	}
}
