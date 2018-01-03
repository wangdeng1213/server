package com.linksus.task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.linksus.common.Constants;
import com.linksus.common.config.LoadConfig;
import com.linksus.common.module.WeiboInteractCommon;
import com.linksus.common.module.WeiboUserCommon;
import com.linksus.common.util.CacheUtil;
import com.linksus.common.util.ContextUtil;
import com.linksus.common.util.HttpUtil;
import com.linksus.common.util.JsonUtil;
import com.linksus.common.util.LogUtil;
import com.linksus.common.util.PrimaryKeyGen;
import com.linksus.common.util.StringUtil;
import com.linksus.entity.LinksusAppaccount;
import com.linksus.entity.LinksusInteractMessage;
import com.linksus.entity.LinksusRelationWeibouser;
import com.linksus.entity.LinksusTaskErrorCode;
import com.linksus.entity.LinksusTaskWeiboInteract;
import com.linksus.entity.UrlEntity;
import com.linksus.service.LinksusAppaccountService;
import com.linksus.service.LinksusInteractMessageService;
import com.linksus.service.LinksusInteractWeiboService;
import com.linksus.service.LinksusRelationWeibouserService;
import com.linksus.service.LinksusTaskWeiboInteractService;

/**
 * 腾讯私信读取任务
 */
public class TaskLetterTencentRead extends BaseTask{

	//URL地址
	private UrlEntity readUrl = LoadConfig.getUrlEntity("TCLetterRead");
	//缓存对象
	private CacheUtil cache = CacheUtil.getInstance();

	private LinksusAppaccountService accountService = (LinksusAppaccountService) ContextUtil
			.getBean("linksusAppaccountService");
	private LinksusTaskWeiboInteractService taskService = (LinksusTaskWeiboInteractService) ContextUtil
			.getBean("linksusTaskWeiboInteractService");
	private LinksusInteractMessageService messageService = (LinksusInteractMessageService) ContextUtil
			.getBean("linksusInteractMessageService");
	private LinksusRelationWeibouserService relationWeiboUserService = (LinksusRelationWeibouserService) ContextUtil
			.getBean("linksusRelationWeibouserService");
	private LinksusInteractWeiboService linksusInteractWeiboService = (LinksusInteractWeiboService) ContextUtil
			.getBean("linksusInteractWeiboService");

	@Override
	public void cal(){
		boolean firstRead = true;
		//
		int startCount = (currentPage - 1) * pageSize;
		LinksusAppaccount linksusAppaccount = new LinksusAppaccount();
		linksusAppaccount.setStartCount(startCount);
		linksusAppaccount.setPageSize(pageSize);
		linksusAppaccount.setInteractType(6);//腾讯私信
		List<LinksusAppaccount> accounts = accountService.getTencentInteractAppaccount(linksusAppaccount);
		//测试开始
		/*
		 * List<LinksusAppaccount> accounts=new ArrayList();
		 * linksusAppaccount.setId(0l);
		 * linksusAppaccount.setMaxId(366287021626918l);
		 * linksusAppaccount.setPagetime(1392644410l);
		 * linksusAppaccount.setAppid("E4DA005947F49E50E0CF9B7940F47855");
		 * linksusAppaccount.setToken("7b1fd10ef99877894f48d92e95bbe450");
		 * accounts.add(linksusAppaccount);
		 */
		//测试结束

		Map mapPara = new HashMap();
		mapPara.put("format", "json");
		mapPara.put("pageflag", "2");
		mapPara.put("reqnum", "2");
		mapPara.put("oauth_consumer_key", cache.getAppInfo(Constants.CACHE_TENCENT_APPINFO).getAppKey());
		mapPara.put("clientip", Constants.TENCENT_CLIENT_IP);
		mapPara.put("oauth_version", "2.a");
		for(LinksusAppaccount account : accounts){
			//if(account.getId() == 110){
			if(account.getMaxId() != null && account.getPagetime() != null){//已经存在私信读取记录
				firstRead = false;
			}
			mapPara.put("access_token", account.getToken());
			mapPara.put("openid", account.getAppid());
			if(!firstRead){//存在私信读取记录
				mapPara.put("lastid", account.getMaxId() + "");
				mapPara.put("pagetime", account.getPagetime() + "");
			}else{
				mapPara.put("lastid", "0");
				mapPara.put("pagetime", "0");
				mapPara.put("pageflag", "0");
			}
			boolean msgFlag = false;
			String lastid = "";
			String pagetime = "";
			LinksusTaskWeiboInteract interact = new LinksusTaskWeiboInteract();
			//遍历腾讯账号,读取私信
			while (true){
				try{
					String rsData = HttpUtil.getRequest(readUrl, mapPara);
					LinksusTaskErrorCode error = StringUtil.parseTencentErrorCode(rsData);
					if(error != null){//存在错误
						LogUtil.saveException(logger, new Exception("-----腾讯私信读取异常:" + "腾讯openid:" + account.getAppid()
								+ ",返回信息：" + rsData));
						break;
					}
					if(JsonUtil.getNodeByName(rsData, "data") == "null"){//没有数据
						break;
					}
					String info = JsonUtil.getNodeByName(JsonUtil.getNodeByName(rsData, "data"), "info");

					List dataList = JsonUtil.json2list(info, Map.class);
					for(int i = 0; i < dataList.size(); i++){
						Map map = (Map) dataList.get(i);
						if(!msgFlag && i == 0){//
							interact.setMaxId(new Long((String) map.get("id")));
							interact.setPagetime(new Long(map.get("timestamp") + ""));
							msgFlag = true;
						}
						if(!firstRead && i == 0){//非第一次读取 向上翻页  第一条记录作为翻页
							mapPara.put("pageflag", "2");
							lastid = (String) map.get("id");
							pagetime = map.get("timestamp") + "";
						}else if(firstRead && i == dataList.size() - 1){//第一次读取,向下翻页 最后一条作为翻页
							mapPara.put("pageflag", "1");
							lastid = (String) map.get("id");
							pagetime = map.get("timestamp") + "";
						}
						Long msgId = new Long((String) map.get("id"));
						// 判断私信是否存在 存在不再处理
						Integer scount = messageService.getLinksusInteractMessageByMsgId(msgId);
						if(scount.intValue() != 0){
							logger.info("该私信信息已存在,不做处理!");
							continue;
						}
						LinksusInteractMessage message = new LinksusInteractMessage();
						Long inteWeiboId = PrimaryKeyGen.getPrimaryKey("linksus_interact_message", "pid");
						message.setPid(inteWeiboId);
						message.setAccountId(account.getId());
						message.setAccountType(2);//腾讯
						message.setContent((String) map.get("origtext"));
						message.setMsgId(msgId);
						message.setInteractTime((Integer) map.get("timestamp"));
						message.setInteractType(1);//用户发布
						message.setImgName("");
						message.setAttatch("");
						message.setAttatchName("");
						//处理图片
						if(map.get("image") != null){
							//List tempList=new ArrayList();
							//List tempList1=new ArrayList();
							String imgs = "";
							String tempImgs = "";
							List<String> imgList = (List) map.get("image");
							int start = 0;
							for(String image : imgList){
								if(start == 0){
									imgs = image + "/160";
									tempImgs = image + "/2000";
								}else{
									imgs = imgs + "," + image + "/160";
									tempImgs = tempImgs + "," + image + "/2000";
								}
								start++;
								//tempList.add(image+"/160");
								//tempList1.add(image+"/2000");
							}
							message.setImg(tempImgs);
							message.setImgThumb(imgs);
						}else{
							message.setImg("");
							message.setImgThumb("");
						}

						//判断发信人是否存在
						//long userId = new Long((String)map.get("openid"));
						//检查微博用户是否存在
						LinksusRelationWeibouser user = null;
						Map tencentparaMap = new HashMap();
						tencentparaMap.put("oauth_consumer_key", cache.getAppInfo(Constants.CACHE_TENCENT_APPINFO)
								.getAppKey());
						tencentparaMap.put("clientip", Constants.TENCENT_CLIENT_IP);
						tencentparaMap.put("oauth_version", "2.a");
						tencentparaMap.put("format", "json");
						tencentparaMap.put("access_token", account.getToken());
						tencentparaMap.put("openid", account.getAppid());
						tencentparaMap.put("fopenid", map.get("openid").toString());

						String strjson = HttpUtil.getRequest(LoadConfig.getUrlEntity("TCUerInfo"), tencentparaMap);
						error = StringUtil.parseTencentErrorCode(strjson);
						if(error == null){
							String userinfo = JsonUtil.getNodeByName(strjson, "data");
							Map userInfoMap = JsonUtil.json2map(userinfo, Map.class);
							if(userInfoMap.size() > 0){
								String rpsId = (String) userInfoMap.get("openid");
								//检查微博用户是否存在
								user = getWeiboUserInfo(strjson, account, rpsId);
							}
						}
						if(user != null){
							message.setUserId(user.getUserId());
							//判断私信是否入私信互动信息表
							Map privateParams = new HashMap();
							privateParams.put("user_id", user.getUserId());
							privateParams.put("account_id", account.getId());
							privateParams.put("account_type", 2);
							privateParams.put("msg_id", (String) map.get("id"));
							privateParams.put("interact_type", 1);
							LinksusInteractMessage interactMessage = messageService.getMessageIsExists(privateParams);
							if(interactMessage == null){
								WeiboInteractCommon interactCommon = new WeiboInteractCommon();
								Long recordId = interactCommon.dealWeiboInteract(user.getUserId(), user.getPersonId(),
										user.getAccountId(), Integer.valueOf(2), new Long(0), "5", inteWeiboId, Integer
												.parseInt(map.get("timestamp").toString()));
								message.setRecordId(recordId);
								message.setMsgType(5);
								//插入私信互动信息表
								messageService.addInteractReadMessage(message);
							}
						}
					}
					if("1".equals(JsonUtil.getNodeValueByName(JsonUtil.getNodeByName(rsData, "data"), "hasnext"))){//拉取完成
						break;
					}
					mapPara.put("lastid", lastid);
					mapPara.put("pagetime", pagetime);
				}catch (Exception e){
					LogUtil.saveException(logger, e);
				}
			}
			if(msgFlag){//读取到私信
				interact.setAccountId(account.getId());
				interact.setInteractType(6);//腾讯私信
				//更新互动任务记录表
				if(!firstRead){//已经存在私信读取记录
					//更新
					taskService.updateLinksusTaskWeiboInteract(interact);
				}else{
					//新增 
					taskService.insertLinksusTaskWeiboInteract(interact);
				}
			}
			//}
		}
		checkTaskListEnd(accounts);//判断任务是否轮询完成
	}

	/**
	 *  处理微博用户并返回微博用户的信息
	* @throws Exception 
	 */
	public LinksusRelationWeibouser getWeiboUserInfo(String userStr,LinksusAppaccount appaccount,String rpsId)
			throws Exception{
		//String userStr =JsonUtil.map2json(userMap);
		//获取评论用户的新浪appId 和  业务类型判断用户是否存在微博用户表中
		WeiboUserCommon weiboUser = new WeiboUserCommon();
		Map parmMap = new HashMap();
		parmMap.put("token", appaccount.getToken());
		parmMap.put("type", "2");
		parmMap.put("AccountId", appaccount.getId() + "");
		parmMap.put("InstitutionId", appaccount.getInstitutionId());
		LinksusRelationWeibouser user = weiboUser.dealWeiboUserInfo(userStr, "2", parmMap, false, "3", 5);

		//根据rpsId和accountType 判断用户是否存在
		/*
		 * Map paraMap =new HashMap(); paraMap.put("rpsId",rpsId);
		 * paraMap.put("userType","2"); LinksusRelationWeibouser user =
		 * relationWeiboUserService.getWeibouserByrpsIdAndType(paraMap);
		 */
		user.setAccountId(appaccount.getId());
		user.setInstitutionId(appaccount.getInstitutionId());
		return user;
	}

	public static void main(String[] args){
		TaskLetterTencentRead read = new TaskLetterTencentRead();
		read.cal();
	}
}
