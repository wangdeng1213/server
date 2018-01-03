package com.linksus.task;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linksus.common.Constants;
import com.linksus.common.config.LoadConfig;
import com.linksus.common.module.WeiboInteractCommon;
import com.linksus.common.module.WeiboUserCommon;
import com.linksus.common.util.ContextUtil;
import com.linksus.common.util.HttpUtil;
import com.linksus.common.util.JsonUtil;
import com.linksus.common.util.LogUtil;
import com.linksus.common.util.PrimaryKeyGen;
import com.linksus.common.util.RedisUtil;
import com.linksus.common.util.StringUtil;
import com.linksus.data.QueueDataSave;
import com.linksus.entity.LinksusAppaccount;
import com.linksus.entity.LinksusInteractWeibo;
import com.linksus.entity.LinksusRelationWeibouser;
import com.linksus.entity.LinksusTaskErrorCode;
import com.linksus.entity.LinksusTaskWeiboInteract;
import com.linksus.entity.LinksusWeiboPool;
import com.linksus.service.LinksusAppaccountService;
import com.linksus.service.LinksusInteractWeiboService;
import com.linksus.service.LinksusRelationWeibouserService;
import com.linksus.service.LinksusTaskWeiboInteractService;
import com.linksus.service.LinksusWeiboPoolService;

/**
 * 腾讯读取互动信息1
 */

public class TaskTencentInteractDataRead extends BaseTask{

	protected static final Logger logger = LoggerFactory.getLogger(TaskTencentInteractDataRead.class);
	private LinksusAppaccountService linksusAppaccountService = (LinksusAppaccountService) ContextUtil
			.getBean("linksusAppaccountService");
	private LinksusTaskWeiboInteractService linksusTaskWeiboInteractService = (LinksusTaskWeiboInteractService) ContextUtil
			.getBean("linksusTaskWeiboInteractService");
	private LinksusRelationWeibouserService relationWeiboUserService = (LinksusRelationWeibouserService) ContextUtil
			.getBean("linksusRelationWeibouserService");
	private LinksusWeiboPoolService linksusWeiboPoolService = (LinksusWeiboPoolService) ContextUtil
			.getBean("linksusWeiboPoolService");
	private LinksusInteractWeiboService linksusInteractWeiboService = (LinksusInteractWeiboService) ContextUtil
			.getBean("linksusInteractWeiboService");

	@Override
	public void cal(){
		LinksusAppaccount linksusAppaccount = new LinksusAppaccount();
		int startCount = (currentPage - 1) * pageSize;
		linksusAppaccount.setPageSize(pageSize);
		linksusAppaccount.setStartCount(startCount);
		linksusAppaccount.setInteractType(5);
		List<LinksusAppaccount> appaccountList = accountService.getTencentInteractAppaccount(linksusAppaccount);
		for(LinksusAppaccount appaccount : appaccountList){
			//判断是否是第一次数据
			boolean firstRead = true;
			boolean msgFlag = false;
			String dayParam = LoadConfig.getString("WeiboInteractFirstSyncDay");
			//处理腾讯账户微博
			if(appaccount.getMaxId() != null && appaccount.getPagetime() != null){//已经存在互动读取记录
				firstRead = false;
			}
			Map mapPara = new HashMap();
			mapPara.put("format", "json");
			mapPara.put("pageflag", "2");
			mapPara.put("reqnum", "70");
			mapPara.put("oauth_consumer_key", cache.getAppInfo(Constants.CACHE_TENCENT_APPINFO).getAppKey());
			mapPara.put("clientip", Constants.TENCENT_CLIENT_IP);
			mapPara.put("oauth_version", "2.a");
			mapPara.put("type", "0");
			mapPara.put("contenttype", "0");
			mapPara.put("access_token", appaccount.getToken());
			mapPara.put("openid", appaccount.getAppid());
			String lastid = "";
			String pagetime = "";
			String pageflag = "";
			String newMaxId = "";
			String newTimestamp = "";
			if(!firstRead){//存在互动读取记录
				pagetime = String.valueOf(appaccount.getPagetime());
				lastid = String.valueOf(appaccount.getMaxId());
				pageflag = "2"; //向上翻页
			}else{//第一次取互动记录 时间通过配置取当前时间一天之内的互动数据
				pagetime = "0";
				lastid = "0";
				pageflag = "1"; //向下翻页
			}
			try{
				while (true){
					mapPara.put("lastid", lastid);
					mapPara.put("pagetime", pagetime);
					mapPara.put("pageflag", pageflag);
					//取出互动数据
					String tencentRes = HttpUtil
							.getRequest(LoadConfig.getUrlEntity("tencentMentionsTimeline"), mapPara);
					LinksusTaskErrorCode error = StringUtil.parseTencentErrorCode(tencentRes);
					if(error == null){//成功
						String dataRealStr = JsonUtil.getNodeByName(tencentRes, "data");
						if("null".equals(dataRealStr)){//无数据
							break;
						}
						Map commonStrMap = JsonUtil.json2map(dataRealStr, Map.class);
						int hasnext = (Integer) commonStrMap.get("hasnext");//判断是否有下一页
						List<Map> infoList = (List<Map>) commonStrMap.get("info");
						//读取拉取微博数据
						if(infoList != null && infoList.size() > 0){
							if(!firstRead){//取出第一条数据
								Map firstContent = (Map) infoList.get(0);
								pagetime = firstContent.get("timestamp") + "";
								lastid = firstContent.get("id") + "";
							}else{//取出最后一条数据进行向下翻页
								Map firstContent = (Map) infoList.get(0);
								newMaxId = firstContent.get("id") + "";
								newTimestamp = firstContent.get("timestamp") + "";
								Map lastContent = (Map) infoList.get(infoList.size() - 1);
								pagetime = lastContent.get("timestamp") + "";
								lastid = lastContent.get("id") + "";
							}
							for(int i = infoList.size() - 1; i >= 0; i--){
								//更新第一条数据
								Map infoContent = (Map) infoList.get(i);
								String timestamp = infoContent.get("timestamp") + "";
								if(firstRead){//向下翻页的话设置时间限制
									if(new Date().getTime() - Integer.valueOf(timestamp) * 1000L > Integer
											.valueOf(dayParam) * 24 * 3600 * 1000L){//超过72小时 不能回复
										continue;
									}
								}
								int status = (Integer) infoContent.get("status");
								if(status == 0){
									//微博正常时处理
									//从腾讯获取用户信息
									LinksusRelationWeibouser user = null;
									LinksusRelationWeibouser sourceUser = null;
									Map source = new HashMap();
									Map tencentparaMap = new HashMap();
									tencentparaMap.put("oauth_consumer_key", cache.getAppInfo(
											Constants.CACHE_TENCENT_APPINFO).getAppKey());
									tencentparaMap.put("clientip", Constants.TENCENT_CLIENT_IP);
									tencentparaMap.put("oauth_version", "2.a");
									tencentparaMap.put("format", "json");
									tencentparaMap.put("access_token", appaccount.getToken());
									tencentparaMap.put("openid", appaccount.getAppid());
									tencentparaMap.put("fopenid", infoContent.get("openid").toString());
									String strjson = HttpUtil.getRequest(LoadConfig.getUrlEntity("TCUerInfo"),
											tencentparaMap);
									error = StringUtil.parseTencentErrorCode(strjson);
									if(error == null){
										if(infoContent.containsKey("source")){
											source = (Map) infoContent.get("source");
										}
										int type = (Integer) infoContent.get("type");
										String userinfo = JsonUtil.getNodeByName(strjson, "data");
										Map userInfoMap = JsonUtil.json2map(userinfo, Map.class);
										if(userInfoMap.size() > 0){
											String rpsId = (String) userInfoMap.get("openid");
											//检查微博用户是否存在
											//判断互动类型
											int interactType = 0;
											if(type == 2 || type == 7){
												if(source != null && source.size() > 0){
													Map sourcetencentparaMap = new HashMap();
													sourcetencentparaMap.put("oauth_consumer_key", cache.getAppInfo(
															Constants.CACHE_TENCENT_APPINFO).getAppKey());
													sourcetencentparaMap.put("clientip", Constants.TENCENT_CLIENT_IP);
													sourcetencentparaMap.put("oauth_version", "2.a");
													sourcetencentparaMap.put("format", "json");
													sourcetencentparaMap.put("access_token", appaccount.getToken());
													sourcetencentparaMap.put("openid", appaccount.getAppid());
													sourcetencentparaMap
															.put("fopenid", source.get("openid").toString());
													String soureUserInfo = HttpUtil.getRequest(LoadConfig
															.getUrlEntity("TCUerInfo"), sourcetencentparaMap);
													LinksusTaskErrorCode sourceError = StringUtil
															.parseTencentErrorCode(soureUserInfo);
													if(sourceError == null){
														String suserinfo = JsonUtil
																.getNodeByName(soureUserInfo, "data");
														Map suserInfoMap = JsonUtil.json2map(suserinfo, Map.class);
														if(suserInfoMap.size() > 0){
															String content = (String) infoContent.get("text");
															if(type == 2){
																if(StringUtils.isNotBlank(content)){
																	interactType = 2;
																	if(!((String) source.get("openid")).equals(appaccount
																			.getAppid())){
																		interactType = 3;
																	}
																}else{
																	content = "转发微博";
																	interactType = 2;
																}
															}else if(type == 7){
																if(StringUtils.isNotBlank(content)){
																	if(content.contains("@" + appaccount.getAccountName())){
																		interactType = 4;
																	}else{
																		interactType = 1;
																	}
																}
															}
															sourceUser = getWeiboUserInfo(soureUserInfo, appaccount, interactType);
														}
													}else{
														continue;
													}
												}
											}else{
												interactType = 1;
											}
											user = getWeiboUserInfo(strjson, appaccount, interactType);
										}

										long mid = Long.parseLong(infoContent.get("id").toString());
										if(type == 1){ //1-原创发表
											//判断此微博在微博池中是否存在，存在更新，不存在插入
											checkWeiboIsExsit(mid, "2", infoContent, user, new Integer(0));
											//插入微博互动信息表		    				
											if(user != null){
												addInfoToInteractWeibo(infoContent, appaccount, user, 1);
											}
										}else if(type == 2){//转发
											long sourceMid = Long.parseLong(source.get("id").toString());//源微博
											//判断微博池中是否存在，存在更新，不存在插入
											checkWeiboIsExsit(mid, "2", infoContent, user, new Integer(0));
											if(sourceUser != null){
												checkWeiboIsExsit(sourceMid, "2", (Map) infoContent.get("source"),
														sourceUser, new Integer(1));
											}
											//将信息插入微博互动信息表
											if(user != null){
												addInfoToInteractWeibo(infoContent, appaccount, user, 2);
											}
										}else if(type == 7){//评论
											long sourceMid = Long.parseLong(source.get("id").toString());//源微博
											//判断此微博在微博池中是否存在，存在更新，不存在插入
											if(sourceUser != null){
												checkWeiboIsExsit(sourceMid, "2", (Map) infoContent.get("source"),
														sourceUser, new Integer(0));
											}
											//将信息插入微博互动信息表
											if(user != null){
												addInfoToInteractWeibo(infoContent, appaccount, user, 7);
											}
										}
									}
								}
							}
						}
						if(hasnext == 1){
							Map firstContent = (Map) infoList.get(0);
							newTimestamp = firstContent.get("timestamp") + "";
							newMaxId = firstContent.get("id") + "";
							break;
						}
					}else{
						break;
					}
				}
				LinksusTaskWeiboInteract linksusTaskWeiboInteract = new LinksusTaskWeiboInteract();
				if(!StringUtils.isBlank(newTimestamp) && !StringUtils.isBlank(newMaxId)){//如果list 有数据进行更新
					if(!firstRead){//非第一次读取 向上翻页  第一条记录作为翻页
						//微博互动任务记录表
						linksusTaskWeiboInteract.setAccountId(appaccount.getId());
						linksusTaskWeiboInteract.setInteractType(5);
						linksusTaskWeiboInteract.setMaxId(new Long(newMaxId));
						linksusTaskWeiboInteract.setPagetime(new Long(newTimestamp));
						linksusTaskWeiboInteractService
								.updateLinksusTaskWeiboInteractPageInfo(linksusTaskWeiboInteract);//不是第一次更新互动记录表中的数据
					}else{//第一次读取,向下翻页 最后一条作为翻页 
						//微博互动任务记录表
						linksusTaskWeiboInteract.setAccountId(appaccount.getId());
						linksusTaskWeiboInteract.setInteractType(5);
						linksusTaskWeiboInteract.setMaxId(new Long(newMaxId));
						linksusTaskWeiboInteract.setPagetime(new Long(newTimestamp));
						linksusTaskWeiboInteractService.insertLinksusTaskWeiboInteract(linksusTaskWeiboInteract);
					}
				}

			}catch (Exception e){
				LogUtil.saveException(logger, e);
			}
		}
		checkTaskListEnd(appaccountList);//判断任务是否轮询完成
	}

	/**
	 *  检查微博是否存在 
	 */
	public void checkWeiboIsExsit(Long mid,String accountType,Map statusMap,LinksusRelationWeibouser user,
			Integer publishType){
		//判断微博是否存在
		//   	     Map weiboPoolMap =new HashMap();
		//		 weiboPoolMap.put("mid",mid);
		//		 weiboPoolMap.put("weiboType",accountType);
		//		 LinksusWeiboPool weiboCount = linksusWeiboPoolService.checkWeiboPoolIsExsit(weiboPoolMap);
		String weiboRedis = RedisUtil.getRedisHash("weibo_pool", mid + "#" + accountType);
		if(user != null){
			LinksusWeiboPool weiboPool = new LinksusWeiboPool();
			if(!StringUtils.isBlank(weiboRedis)){//存在更新
				weiboPool.setMid(mid);
				weiboPool.setWeiboType(Integer.valueOf(accountType));
				weiboPool.setRepostCount(statusMap.get("count") == null ? 0 : (Integer) statusMap.get("count"));
				weiboPool.setCommentCount(statusMap.get("mcount") == null ? 0 : (Integer) statusMap.get("mcount"));
				linksusWeiboPoolService.updateLinksusWeiboPoolDataCount(weiboPool);
			}else{//不存在插入
				copyTencentDataToWeiboPool(statusMap, weiboPool);
				//用户id
				weiboPool.setUid(user.getUserId());
				//用户名称
				weiboPool.setUname(user.getRpsScreenName());
				//用户头像
				weiboPool.setUprofileUrl(user.getRpsProfileImageUrl());
				weiboPool.setWeiboType(Integer.valueOf(accountType));
				weiboPool.setSrcMid(mid);
				weiboPool.setPublishType(publishType);
				QueueDataSave.addDataToQueue(weiboPool, Constants.OPER_TYPE_INSERT);
				// linksusWeiboPoolService.insertLinksusWeiboPool(weiboPool);
			}
		}
	}

	/**
	 *  处理微博用户并返回微博用户的信息
	* @throws Exception 
	 */
	public LinksusRelationWeibouser getWeiboUserInfo(String userStr,LinksusAppaccount appaccount,int interactBit)
			throws Exception{
		//public LinksusRelationWeibouser getWeiboUserInfo(Map userMap ,LinksusAppaccount appaccount,String rpsId) throws Exception{
		//String userStr =JsonUtil.map2json(userMap);
		//获取评论用户的新浪appId 和  业务类型判断用户是否存在微博用户表中
		WeiboUserCommon weiboUser = new WeiboUserCommon();
		Map parmMap = new HashMap();
		parmMap.put("token", appaccount.getToken());
		//parmMap.put("openid", appaccount.getAppid());
		parmMap.put("type", "2");
		//parmMap.put("token", "90a812849d24ebd35191e0f5e3e3ece1");
		//parmMap.put("AccountId", "180");
		parmMap.put("AccountId", appaccount.getId() + "");
		//long insId = 1013;
		//parmMap.put("InstitutionId", insId);
		parmMap.put("InstitutionId", appaccount.getInstitutionId());
		LinksusRelationWeibouser user = weiboUser.dealWeiboUserInfo(userStr, "2", parmMap, false, "3", interactBit);

		//根据rpsId和accountType 判断用户是否存在
		//			Map paraMap =new HashMap();
		//			paraMap.put("rpsId",rpsId);
		//			paraMap.put("userType","2");
		//		    LinksusRelationWeibouser user = relationWeiboUserService.getWeibouserByrpsIdAndType(paraMap);
		user.setAccountId(appaccount.getId());
		user.setInstitutionId(appaccount.getInstitutionId());
		//user.setAccountId(Long.parseLong("180"));
		//user.setInstitutionId((Long)insId);
		return user;
	}

	// 复制腾讯数据到微博池表
	public void copyTencentDataToWeiboPool(Map weiboMap,LinksusWeiboPool weiboPool){
		// 微博id
		weiboPool.setMid(new Long((String) weiboMap.get("id")));
		// 微博类型
		// weiboPool.setWeiboType();
		// 内容
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
		// 原图2000
		weiboPool.setPicOriginalUrl(StringUtils.isBlank(originalUrl) ? "" : originalUrl.substring(0, originalUrl
				.length() - 1));
		// 中图460
		weiboPool.setPicMiddleUrl(StringUtils.isBlank(middleUrl) ? "" : middleUrl.substring(0, middleUrl.length() - 1));
		// 小图120
		weiboPool.setPicThumbUrl(StringUtils.isBlank(thumbUrl) ? "" : thumbUrl.substring(0, thumbUrl.length() - 1));
		// 音频地址
		Map musicMap = (Map) weiboMap.get("music");
		if(musicMap != null){
			String musicUrl = (String) musicMap.get("url");
			weiboPool.setMusicUrl(musicUrl);
		}else{
			weiboPool.setMusicUrl("");
		}
		// 视频地址
		Map vedioMap = (Map) weiboMap.get("video");
		if(musicMap != null){
			String wedioUrl = (String) musicMap.get("realurl");
			weiboPool.setVideoUrl(wedioUrl);
		}else{
			weiboPool.setVideoUrl("");
		}
		weiboPool.setVideoUrl("");
		// 评论数
		weiboPool.setCommentCount(Integer.valueOf(weiboMap.get("mcount") + ""));
		// 转发数
		weiboPool.setRepostCount(Integer.valueOf(weiboMap.get("count") + ""));
		// 原id
		// weiboPool.setSrcMid(0L);
		// 地址信息
		weiboPool.setGeo((String) weiboMap.get("geo"));
		// 用户id
		// ------------weiboPool.setUid((String)weiboMap.get("id"));
		// 用户名称
		// --------------weiboPool.setUname((String)weiboMap.get("id"));
		// 用户头像
		// --------------weiboPool.setUprofileUrl((String)weiboMap.get("id"));
		// 发布类型
		// weiboPool.setPublishType(Integer.valueOf(weiboMap.get("type")+""));
		// 来源
		weiboPool.setSource((String) weiboMap.get("from"));
		// 创建时间
		weiboPool.setCreatedTime(Integer.parseInt(String.valueOf(weiboMap.get("timestamp"))));
		// 微博的URL
		weiboPool.setCurrentUrl("http://t.qq.com/p/t/" + new Long(weiboMap.get("id").toString()));
	}

	//信息插入微博互动信息表
	public String addInfoToInteractWeibo(Map infoContent,LinksusAppaccount appaccount,LinksusRelationWeibouser user,
			int type) throws Exception{
		String status = "fail";
		//微博互动信息表中是否有该记录
		boolean flag = false;
		Map weiboParams = new HashMap();
		Map source = null;
		if(infoContent != null){
			Long weiboId = 0L;
			if(infoContent.containsKey("source")){
				source = (Map) infoContent.get("source");
			}
			//查询是否有该记录
			weiboParams.put("userId", user.getUserId());
			weiboParams.put("accountId", user.getAccountId());
			weiboParams.put("accountType", 2);
			if(type == 1){
				weiboParams.put("commentId", new Long(0));
				weiboParams.put("weibo_id", Long.parseLong(infoContent.get("id").toString()));
				weiboParams.put("source_weibo_id", new Long(0));
				LinksusInteractWeibo interactWeibo = linksusInteractWeiboService.getInteractWeiboIsExists(weiboParams);
				if(interactWeibo == null){
					flag = true;
				}
			}else if(type == 2){
				weiboParams.put("commentId", new Long(0));
				weiboParams.put("weibo_id", Long.parseLong(infoContent.get("id").toString()));
				weiboId = Long.parseLong(infoContent.get("id").toString());
				weiboParams.put("source_weibo_id", Long.parseLong(source.get("id").toString()));
				LinksusInteractWeibo interactWeibo = linksusInteractWeiboService.getInteractWeiboIsExists(weiboParams);
				if(interactWeibo == null){
					flag = true;
				}
			}else if(type == 7){
				weiboParams.put("commentId", Long.parseLong(infoContent.get("id").toString()));
				weiboId = Long.parseLong(source.get("id").toString());
				weiboParams.put("weibo_id", Long.parseLong(source.get("id").toString()));
				weiboParams.put("source_weibo_id", new Long(0));
				LinksusInteractWeibo interactWeibo = linksusInteractWeiboService.getInteractWeiboIsExists(weiboParams);
				if(interactWeibo == null){
					flag = true;
				}
			}
			if(flag){
				if(type == 2 || type == 7){
					//if(infoContent.containsKey("source")){
					//Map source = (Map)infoContent.get("source");
					if(source != null && source.size() > 0){
						//处理互动用户表、互动人员表
						WeiboInteractCommon interactCommon = new WeiboInteractCommon();
						Long inteWeiboId = PrimaryKeyGen.getPrimaryKey("linksus_interact_weibo", "pid");//互动信息主键
						String content = (String) infoContent.get("text");
						int interactType = 0;
						if(type == 2){
							if(StringUtils.isNotBlank(content)){
								interactType = 2;
								if(!((String) source.get("openid")).equals(appaccount.getAppid())){
									interactType = 3;
								}
							}else{
								content = "转发微博";
								interactType = 2;
							}
						}else if(type == 7){
							if(StringUtils.isNotBlank(content)){
								if(content.contains("@" + appaccount.getAccountName())){
									interactType = 4;
								}else{
									interactType = 1;
								}
							}
						}
						Long recordId = interactCommon.dealWeiboInteract(user.getUserId(), user.getPersonId(), user
								.getAccountId(), Integer.valueOf(2), weiboId, interactType + "", inteWeiboId, Integer
								.parseInt(infoContent.get("timestamp").toString()));
						LinksusInteractWeibo linksusInteractWeibo = new LinksusInteractWeibo();//设置互动内容
						linksusInteractWeibo.setPid(inteWeiboId);
						linksusInteractWeibo.setRecordId(recordId);
						linksusInteractWeibo.setUserId(user.getUserId());
						linksusInteractWeibo.setAccountId(user.getAccountId());
						linksusInteractWeibo.setAccountType(2);
						if(type == 7){
							linksusInteractWeibo.setCommentId(Long.parseLong(infoContent.get("id").toString()));
							linksusInteractWeibo.setWeiboId(Long.parseLong(source.get("id").toString()));
							linksusInteractWeibo.setSourceWeiboId(new Long(0));
						}else{
							linksusInteractWeibo.setSourceWeiboId(Long.parseLong(source.get("id").toString()));
							linksusInteractWeibo.setWeiboId(Long.parseLong(infoContent.get("id").toString()));
							linksusInteractWeibo.setCommentId(new Long(0));
						}
						linksusInteractWeibo.setContent(content);
						linksusInteractWeibo.setReplyMsgId(new Long(0));
						linksusInteractWeibo.setSendType(0);
						linksusInteractWeibo.setStatus(0);
						linksusInteractWeibo.setSendTime(0);
						linksusInteractWeibo.setInstPersonId(new Long(0));
						linksusInteractWeibo.setInteractType(interactType);
						linksusInteractWeibo.setInteractTime((Integer) infoContent.get("timestamp"));
						//linksusInteractWeiboService.insertLinksusInteractWeibo(linksusInteractWeibo);
						QueueDataSave.addDataToQueue(linksusInteractWeibo, Constants.OPER_TYPE_INSERT);
						status = "success";
					}
					//}
				}else{
					//处理互动用户表、互动人员表
					WeiboInteractCommon interactCommon = new WeiboInteractCommon();
					Long inteWeiboId = PrimaryKeyGen.getPrimaryKey("linksus_interact_weibo", "pid");//互动信息主键
					Long recordId = interactCommon.dealWeiboInteract(user.getUserId(), user.getPersonId(), user
							.getAccountId(), Integer.valueOf(2), Long.parseLong(infoContent.get("id").toString()), "3",
							inteWeiboId, Integer.parseInt(infoContent.get("timestamp").toString()));
					LinksusInteractWeibo linksusInteractWeibo = new LinksusInteractWeibo();//设置互动内容  ----3 @
					linksusInteractWeibo.setPid(inteWeiboId);
					linksusInteractWeibo.setRecordId(recordId);
					linksusInteractWeibo.setUserId(user.getUserId());
					linksusInteractWeibo.setAccountId(user.getAccountId());
					linksusInteractWeibo.setAccountType(2);
					linksusInteractWeibo.setCommentId(new Long(0));
					linksusInteractWeibo.setWeiboId(Long.parseLong(infoContent.get("id").toString()));
					linksusInteractWeibo.setContent((String) infoContent.get("text"));
					linksusInteractWeibo.setReplyMsgId(new Long(0));
					linksusInteractWeibo.setSendType(0);
					linksusInteractWeibo.setStatus(0);
					linksusInteractWeibo.setSendTime(0);
					linksusInteractWeibo.setInstPersonId(new Long(0));
					linksusInteractWeibo.setInteractType(3);
					linksusInteractWeibo.setSourceWeiboId(new Long(0));
					linksusInteractWeibo.setInteractTime((Integer) infoContent.get("timestamp"));
					//linksusInteractWeiboService.insertLinksusInteractWeibo(linksusInteractWeibo);
					QueueDataSave.addDataToQueue(linksusInteractWeibo, Constants.OPER_TYPE_INSERT);
					status = "success";
				}
			}else{
				logger.info("<<<<<<<<<<<<<<<<<<<<此互动记录已存在<<<<<<<<<<<<<<<<<<<");
			}
		}
		return status;
	}

	public static void main(String[] args){
		/*
		 * String SOURCE = "Wed Jul 25 16:43:38 +0800 2012"; SimpleDateFormat
		 * sdf = new SimpleDateFormat( "EEE MMM dd HH:mm:ss Z yyyy", new
		 * Locale("ENGLISH", "CHINA")); try { Date myDate = sdf.parse(SOURCE);
		 * SimpleDateFormat sdf2 = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss",
		 * new Locale("CHINESE", "CHINA"));
		 * System.out.println(sdf2.format(myDate));
		 * 
		 * } catch (ParseException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 */
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, 5);
		//cal.getTime();
		String tokenTime = String.valueOf(cal.getTime().getTime() / 1000);
		System.out.println("tokenTime :" + tokenTime);
		//TaskTencentInteractDataRead de = new TaskTencentInteractDataRead();
		//	de.dealTencentInteractData(null);
	}

}
