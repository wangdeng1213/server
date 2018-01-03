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
 * ��Ѷ��ȡ������Ϣ1
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
			//�ж��Ƿ��ǵ�һ������
			boolean firstRead = true;
			boolean msgFlag = false;
			String dayParam = LoadConfig.getString("WeiboInteractFirstSyncDay");
			//������Ѷ�˻�΢��
			if(appaccount.getMaxId() != null && appaccount.getPagetime() != null){//�Ѿ����ڻ�����ȡ��¼
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
			if(!firstRead){//���ڻ�����ȡ��¼
				pagetime = String.valueOf(appaccount.getPagetime());
				lastid = String.valueOf(appaccount.getMaxId());
				pageflag = "2"; //���Ϸ�ҳ
			}else{//��һ��ȡ������¼ ʱ��ͨ������ȡ��ǰʱ��һ��֮�ڵĻ�������
				pagetime = "0";
				lastid = "0";
				pageflag = "1"; //���·�ҳ
			}
			try{
				while (true){
					mapPara.put("lastid", lastid);
					mapPara.put("pagetime", pagetime);
					mapPara.put("pageflag", pageflag);
					//ȡ����������
					String tencentRes = HttpUtil
							.getRequest(LoadConfig.getUrlEntity("tencentMentionsTimeline"), mapPara);
					LinksusTaskErrorCode error = StringUtil.parseTencentErrorCode(tencentRes);
					if(error == null){//�ɹ�
						String dataRealStr = JsonUtil.getNodeByName(tencentRes, "data");
						if("null".equals(dataRealStr)){//������
							break;
						}
						Map commonStrMap = JsonUtil.json2map(dataRealStr, Map.class);
						int hasnext = (Integer) commonStrMap.get("hasnext");//�ж��Ƿ�����һҳ
						List<Map> infoList = (List<Map>) commonStrMap.get("info");
						//��ȡ��ȡ΢������
						if(infoList != null && infoList.size() > 0){
							if(!firstRead){//ȡ����һ������
								Map firstContent = (Map) infoList.get(0);
								pagetime = firstContent.get("timestamp") + "";
								lastid = firstContent.get("id") + "";
							}else{//ȡ�����һ�����ݽ������·�ҳ
								Map firstContent = (Map) infoList.get(0);
								newMaxId = firstContent.get("id") + "";
								newTimestamp = firstContent.get("timestamp") + "";
								Map lastContent = (Map) infoList.get(infoList.size() - 1);
								pagetime = lastContent.get("timestamp") + "";
								lastid = lastContent.get("id") + "";
							}
							for(int i = infoList.size() - 1; i >= 0; i--){
								//���µ�һ������
								Map infoContent = (Map) infoList.get(i);
								String timestamp = infoContent.get("timestamp") + "";
								if(firstRead){//���·�ҳ�Ļ�����ʱ������
									if(new Date().getTime() - Integer.valueOf(timestamp) * 1000L > Integer
											.valueOf(dayParam) * 24 * 3600 * 1000L){//����72Сʱ ���ܻظ�
										continue;
									}
								}
								int status = (Integer) infoContent.get("status");
								if(status == 0){
									//΢������ʱ����
									//����Ѷ��ȡ�û���Ϣ
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
											//���΢���û��Ƿ����
											//�жϻ�������
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
																	content = "ת��΢��";
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
										if(type == 1){ //1-ԭ������
											//�жϴ�΢����΢�������Ƿ���ڣ����ڸ��£������ڲ���
											checkWeiboIsExsit(mid, "2", infoContent, user, new Integer(0));
											//����΢��������Ϣ��		    				
											if(user != null){
												addInfoToInteractWeibo(infoContent, appaccount, user, 1);
											}
										}else if(type == 2){//ת��
											long sourceMid = Long.parseLong(source.get("id").toString());//Դ΢��
											//�ж�΢�������Ƿ���ڣ����ڸ��£������ڲ���
											checkWeiboIsExsit(mid, "2", infoContent, user, new Integer(0));
											if(sourceUser != null){
												checkWeiboIsExsit(sourceMid, "2", (Map) infoContent.get("source"),
														sourceUser, new Integer(1));
											}
											//����Ϣ����΢��������Ϣ��
											if(user != null){
												addInfoToInteractWeibo(infoContent, appaccount, user, 2);
											}
										}else if(type == 7){//����
											long sourceMid = Long.parseLong(source.get("id").toString());//Դ΢��
											//�жϴ�΢����΢�������Ƿ���ڣ����ڸ��£������ڲ���
											if(sourceUser != null){
												checkWeiboIsExsit(sourceMid, "2", (Map) infoContent.get("source"),
														sourceUser, new Integer(0));
											}
											//����Ϣ����΢��������Ϣ��
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
				if(!StringUtils.isBlank(newTimestamp) && !StringUtils.isBlank(newMaxId)){//���list �����ݽ��и���
					if(!firstRead){//�ǵ�һ�ζ�ȡ ���Ϸ�ҳ  ��һ����¼��Ϊ��ҳ
						//΢�����������¼��
						linksusTaskWeiboInteract.setAccountId(appaccount.getId());
						linksusTaskWeiboInteract.setInteractType(5);
						linksusTaskWeiboInteract.setMaxId(new Long(newMaxId));
						linksusTaskWeiboInteract.setPagetime(new Long(newTimestamp));
						linksusTaskWeiboInteractService
								.updateLinksusTaskWeiboInteractPageInfo(linksusTaskWeiboInteract);//���ǵ�һ�θ��»�����¼���е�����
					}else{//��һ�ζ�ȡ,���·�ҳ ���һ����Ϊ��ҳ 
						//΢�����������¼��
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
		checkTaskListEnd(appaccountList);//�ж������Ƿ���ѯ���
	}

	/**
	 *  ���΢���Ƿ���� 
	 */
	public void checkWeiboIsExsit(Long mid,String accountType,Map statusMap,LinksusRelationWeibouser user,
			Integer publishType){
		//�ж�΢���Ƿ����
		//   	     Map weiboPoolMap =new HashMap();
		//		 weiboPoolMap.put("mid",mid);
		//		 weiboPoolMap.put("weiboType",accountType);
		//		 LinksusWeiboPool weiboCount = linksusWeiboPoolService.checkWeiboPoolIsExsit(weiboPoolMap);
		String weiboRedis = RedisUtil.getRedisHash("weibo_pool", mid + "#" + accountType);
		if(user != null){
			LinksusWeiboPool weiboPool = new LinksusWeiboPool();
			if(!StringUtils.isBlank(weiboRedis)){//���ڸ���
				weiboPool.setMid(mid);
				weiboPool.setWeiboType(Integer.valueOf(accountType));
				weiboPool.setRepostCount(statusMap.get("count") == null ? 0 : (Integer) statusMap.get("count"));
				weiboPool.setCommentCount(statusMap.get("mcount") == null ? 0 : (Integer) statusMap.get("mcount"));
				linksusWeiboPoolService.updateLinksusWeiboPoolDataCount(weiboPool);
			}else{//�����ڲ���
				copyTencentDataToWeiboPool(statusMap, weiboPool);
				//�û�id
				weiboPool.setUid(user.getUserId());
				//�û�����
				weiboPool.setUname(user.getRpsScreenName());
				//�û�ͷ��
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
	 *  ����΢���û�������΢���û�����Ϣ
	* @throws Exception 
	 */
	public LinksusRelationWeibouser getWeiboUserInfo(String userStr,LinksusAppaccount appaccount,int interactBit)
			throws Exception{
		//public LinksusRelationWeibouser getWeiboUserInfo(Map userMap ,LinksusAppaccount appaccount,String rpsId) throws Exception{
		//String userStr =JsonUtil.map2json(userMap);
		//��ȡ�����û�������appId ��  ҵ�������ж��û��Ƿ����΢���û�����
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

		//����rpsId��accountType �ж��û��Ƿ����
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

	// ������Ѷ���ݵ�΢���ر�
	public void copyTencentDataToWeiboPool(Map weiboMap,LinksusWeiboPool weiboPool){
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
		weiboPool.setPicOriginalUrl(StringUtils.isBlank(originalUrl) ? "" : originalUrl.substring(0, originalUrl
				.length() - 1));
		// ��ͼ460
		weiboPool.setPicMiddleUrl(StringUtils.isBlank(middleUrl) ? "" : middleUrl.substring(0, middleUrl.length() - 1));
		// Сͼ120
		weiboPool.setPicThumbUrl(StringUtils.isBlank(thumbUrl) ? "" : thumbUrl.substring(0, thumbUrl.length() - 1));
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

	//��Ϣ����΢��������Ϣ��
	public String addInfoToInteractWeibo(Map infoContent,LinksusAppaccount appaccount,LinksusRelationWeibouser user,
			int type) throws Exception{
		String status = "fail";
		//΢��������Ϣ�����Ƿ��иü�¼
		boolean flag = false;
		Map weiboParams = new HashMap();
		Map source = null;
		if(infoContent != null){
			Long weiboId = 0L;
			if(infoContent.containsKey("source")){
				source = (Map) infoContent.get("source");
			}
			//��ѯ�Ƿ��иü�¼
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
						//�������û���������Ա��
						WeiboInteractCommon interactCommon = new WeiboInteractCommon();
						Long inteWeiboId = PrimaryKeyGen.getPrimaryKey("linksus_interact_weibo", "pid");//������Ϣ����
						String content = (String) infoContent.get("text");
						int interactType = 0;
						if(type == 2){
							if(StringUtils.isNotBlank(content)){
								interactType = 2;
								if(!((String) source.get("openid")).equals(appaccount.getAppid())){
									interactType = 3;
								}
							}else{
								content = "ת��΢��";
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
						LinksusInteractWeibo linksusInteractWeibo = new LinksusInteractWeibo();//���û�������
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
					//�������û���������Ա��
					WeiboInteractCommon interactCommon = new WeiboInteractCommon();
					Long inteWeiboId = PrimaryKeyGen.getPrimaryKey("linksus_interact_weibo", "pid");//������Ϣ����
					Long recordId = interactCommon.dealWeiboInteract(user.getUserId(), user.getPersonId(), user
							.getAccountId(), Integer.valueOf(2), Long.parseLong(infoContent.get("id").toString()), "3",
							inteWeiboId, Integer.parseInt(infoContent.get("timestamp").toString()));
					LinksusInteractWeibo linksusInteractWeibo = new LinksusInteractWeibo();//���û�������  ----3 @
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
				logger.info("<<<<<<<<<<<<<<<<<<<<�˻�����¼�Ѵ���<<<<<<<<<<<<<<<<<<<");
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
