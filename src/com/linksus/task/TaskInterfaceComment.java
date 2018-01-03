package com.linksus.task;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.linksus.common.config.LoadConfig;
import com.linksus.common.util.ContextUtil;
import com.linksus.common.util.DateUtil;
import com.linksus.common.util.HttpUtil;
import com.linksus.common.util.JsonUtil;
import com.linksus.common.util.LogUtil;
import com.linksus.common.util.PrimaryKeyGen;
import com.linksus.common.util.StringUtil;
import com.linksus.entity.LinksusAppaccount;
import com.linksus.entity.LinksusInteractMessage;
import com.linksus.entity.LinksusInteractRecord;
import com.linksus.entity.LinksusInteractReplyCount;
import com.linksus.entity.LinksusInteractWeibo;
import com.linksus.entity.LinksusInteractWeixin;
import com.linksus.entity.LinksusInteractWxArticle;
import com.linksus.entity.LinksusInteractWxMaterial;
import com.linksus.entity.LinksusTaskErrorCode;
import com.linksus.entity.UrlEntity;
import com.linksus.service.LinksusAppaccountService;
import com.linksus.service.LinksusInteractMessageService;
import com.linksus.service.LinksusInteractRecordService;
import com.linksus.service.LinksusInteractReplyCountService;
import com.linksus.service.LinksusInteractWeiboService;
import com.linksus.service.LinksusInteractWeixinService;
import com.linksus.service.LinksusInteractWxArticleService;
import com.linksus.service.LinksusInteractWxMaterialService;
import com.linksus.service.LinksusRelationWeibouserService;
import com.linksus.service.LinksusRelationWxaccountService;
import com.linksus.service.LinksusRelationWxuserService;
import com.linksus.service.LinksusRemoveEventService;

/** 互动功能中评论相关功能的接口* */
public class TaskInterfaceComment extends BaseTask{

	LinksusInteractWeiboService linksusInteractWeiboService = (LinksusInteractWeiboService) ContextUtil
			.getBean("linksusInteractWeiboService");

	LinksusInteractMessageService linksusInteractMessageService = (LinksusInteractMessageService) ContextUtil
			.getBean("linksusInteractMessageService");

	LinksusInteractWeixinService linksusInteractWeixinService = (LinksusInteractWeixinService) ContextUtil
			.getBean("linksusInteractWeixinService");

	LinksusRemoveEventService removeService = (LinksusRemoveEventService) ContextUtil
			.getBean("linksusRemoveEventService");

	LinksusAppaccountService linksusAppaccountService = (LinksusAppaccountService) ContextUtil
			.getBean("linksusAppaccountService");

	LinksusRelationWeibouserService linksusRelationWeibouserService = (LinksusRelationWeibouserService) ContextUtil
			.getBean("linksusRelationWeibouserService");

	LinksusInteractReplyCountService linksusInteractReplyCountService = (LinksusInteractReplyCountService) ContextUtil
			.getBean("linksusInteractReplyCountService");

	LinksusRelationWxuserService linksusRelationWxuserService = (LinksusRelationWxuserService) ContextUtil
			.getBean("linksusRelationWxuserService");

	LinksusRelationWxaccountService linksusRelationWxaccountService = (LinksusRelationWxaccountService) ContextUtil
			.getBean("linksusRelationWxaccountService");

	LinksusInteractWxArticleService linksusInteractWxArticleService = (LinksusInteractWxArticleService) ContextUtil
			.getBean("linksusInteractWxArticleService");

	LinksusInteractWxMaterialService linksusInteractWxMaterialService = (LinksusInteractWxMaterialService) ContextUtil
			.getBean("linksusInteractWxMaterialService");

	LinksusInteractRecordService linksusInteractRecordService = (LinksusInteractRecordService) ContextUtil
			.getBean("linksusInteractRecordService");

	@Override
	public void cal(){
	}

	/**
	 * 根据ID删除微博的评论,接口使用,先调用第三方接口,根据返回成功状态来进行平台库的评论数据删除
	 * 
	 * @param id,account_type
	 * @param event
	 * @return
	 */
	public String removeWeiboComment(Map map) throws Exception{
		String rsData = "";
		boolean hasError = false;
		Long id = Long.parseLong(map.get("id").toString());
		Long account_id = Long.parseLong(map.get("account_id").toString());
		String accountType = "";
		String access_token = "";
		String uid = "";
		String switcher = map.get("switcher").toString();// 系统开关
		Map paramMap = new HashMap();
		String errorCode = "";
		LinksusAppaccount appcount = linksusAppaccountService.getLinksusAppaccountById(account_id);

		uid = appcount.getAppid().toString();
		accountType = appcount.getType().toString();
		access_token = appcount.getToken().toString();
		map.put("account_type", accountType);
		LinksusTaskErrorCode error = null;
		if("1".equals(switcher)){// 第三方平台评论开关控制
			if("1".equals(accountType)){// 新浪
				UrlEntity url = LoadConfig.getUrlEntity("deletesinaWeiboComment");
				paramMap.put("uid", uid);
				paramMap.put("access_token", access_token);
				paramMap.put("cid", id);
				rsData = HttpUtil.postRequest(url, paramMap);
				error = StringUtil.parseSinaErrorCode(rsData);
				if(error != null){// 删除错误
					hasError = true;
				}
			}else if("2".equals(accountType)){// 腾讯微博评论删除暂时未提供接口

			}
			if(hasError){// 说明删除评论时出错
				// 修改执行状态
				rsData = error.getErrorCode().toString();
			}else{// 修改执行状态
				this.removeCommentByMap(map);// 第三方评论数据删除成功后删除平台评论数据
				rsData = "";
			}
		}else{// 只删除平台评论
			this.removeCommentByMap(map);// 第三方评论数据删除成功后删除平台评论数据
		}
		// 修改执行状态
		/*
		 * updtEvent.setId(id); updtEvent.setStatus(1);
		 * updtEvent.setOperateTime(DateUtil.getUnixDate(new Date()));
		 * removeService.updateRemoveEventStatus(updtEvent);
		 */
		return rsData;
	}

	/**
	 * 根据ID删除微博的评论,根据返回成功状态来进行平台库的评论数据删除
	 * 
	 * @param id,account_type
	 * 
	 * @return
	 */
	public String removeCommentByMap(Map map){
		int count = linksusInteractWeiboService.deleteLinksusInteractWeiboByMap(map);
		String restr = "";
		if(count == 0){
			restr = "success";
		}

		return restr;
	}

	/**
	 * 回复私信
	 * 
	 */
	public String publishMsg(Map map) throws Exception{
		String rsData = "";
		Integer sendType = (Integer) map.get("sendType");
		if(sendType == 0 || sendType == 1){

		}else{
			return "20032";
		}
		Integer msgType = (Integer) map.get("msgType");
		if(msgType == 1){
			String content = (String) map.get("content");
			if(StringUtils.isBlank(content)){
				return "20022";
			}
		}else if(msgType == 2){
			String articles = (String) map.get("articles");
			if(StringUtils.isBlank(articles)){
				return "20023";
			}
		}else{
			return "20034";
		}
		LinksusAppaccount appcount = linksusAppaccountService.getLinksusAppaccountById((Long) map.get("accountId"));
		if(appcount == null){//机构用户不存在
			return "10130";
		}
		map.put("institution_id", appcount.getInstitutionId());
		map.put("access_token", appcount.getToken());
		map.put("uid", appcount.getAppid());
		map.put("accountType", appcount.getType().toString());
		map.put("openid", appcount.getAppid());
		map.put("userid", appcount.getUserId() + "");
		map.put("username", appcount.getAccountName());
		rsData = publishWeiboMsg(map);
		replyCommentCount(map);// 统计常用回复使用数量
		return rsData;
	}

	/**
	 * 发布微信
	 * 
	 */
	public String publishWeixin(Map map) throws Exception{
		String rsData = "";
		Integer msgType = (Integer) map.get("msgType");
		Integer sendType = (Integer) map.get("sendType");
		if(sendType == 0 || sendType == 1){

		}else{
			return "20032";
		}
		String content = (String) map.get("content");
		if(msgType == 1 && StringUtils.isBlank(content)){
			return "20022";
		}
		String articles = (String) map.get("articles");
		if(msgType == 2 && StringUtils.isBlank(articles)){
			return "20023";
		}
		if(msgType == 3 && StringUtils.isBlank(articles)){
			return "20023";
		}
		LinksusAppaccount appcount = linksusAppaccountService.getLinksusAppaccountById((Long) map.get("accountId"));
		if(appcount == null){//机构用户不存在
			return "10130";
		}
		if(appcount.getType() != 3){
			return "20030";
		}
		if(StringUtils.isNotBlank(appcount.getToken()) && appcount.getToken().equals("0")){
			return "20006";
		}
		Integer interactTime = DateUtil.getUnixDate(new Date());
		if(appcount.getAppEtime() < interactTime){
			return "20040";
		}else if(appcount.getTokenEtime() < interactTime){
			return "20031";
		}

		map.put("institution_id", appcount.getInstitutionId());
		map.put("access_token", appcount.getToken());
		map.put("uid", appcount.getAppid());
		map.put("accountType", appcount.getType().toString());
		map.put("openid", appcount.getAppid());
		map.put("userid", appcount.getUserId() + "");
		rsData = publishWeixinContent(map);
		return rsData;
	}

	/**
	 * 发布评论 
	 * @param map   发布评论所需参数
	 *  replyId 			需要回复的互动消息ID
	 *  mid     			微博id
	 *  content 			发布内容
	 *  accountId 			平台账号id
	 *  instPersonId		运维人员ID
	 *  commonReplyIds 		常用回复pid 带逗号分隔
	 *  sendType          	发布类型 0/1(及时发布/定时发布)
	 *  sendTime			定时发送时间int型
	 *  
	 *  
	 * @return 
	 */
	public String publishComment(Map map) throws Exception{
		String rsData = "";

		//两参数不能同时为空
		if(map.get("replyId") == null && map.get("mid") == null){
			return "20015";
		}
		Integer sendType = (Integer) map.get("sendType");
		String sendTime = (String) map.get("sendTime");
		if(sendType == 0 || sendType == 1){

		}else{
			return "20032";
		}
		if(sendType == 1 && sendTime == null){
			return "20017";
		}
		LinksusAppaccount appcount = linksusAppaccountService.getLinksusAppaccountAndUserIdById((Long) map
				.get("accountId"));
		if(appcount == null){//机构用户不存在
			return "10130";
		}
		map.put("institution_id", appcount.getInstitutionId());
		map.put("access_token", appcount.getToken());
		map.put("uid", appcount.getAppid());
		map.put("accountType", appcount.getType().toString());
		map.put("openid", appcount.getAppid());
		map.put("userid", appcount.getUserId() + "");
		rsData = this.publishWeiboComment(map);
		replyCommentCount(map);// 统计常用回复使用数量
		return rsData;
	}

	/**
	 * 发布微博评论的评论
	 * 
	 *  replyId 			需要回复的互动消息ID
	 *  mid     			微博id
	 *  content 			发布内容
	 *  accountId 			平台账号id
	 *  instPersonId		运维人员ID
	 *  commonReplyIds 		常用回复pid 带逗号分隔
	 *  sendType          	发布类型 0/1(及时发布/定时发布)
	 *  sendTime			定时发送时间int型
	 * @return
	 */
	public String publishWeiboComment(Map map) throws Exception{
		TaskReplyComments taskReplyComments = new TaskReplyComments();
		Long replyId = (Long) map.get("replyId");
		Long mid = (Long) map.get("mid");
		String content = (String) map.get("content");
		Integer sendType = (Integer) map.get("sendType");

		String result = "";
		Long recordId;
		LinksusInteractWeibo linksusInteractWeibo = null;
		LinksusInteractWeibo entity = new LinksusInteractWeibo();
		Long pid = PrimaryKeyGen.getPrimaryKey("linksus_interact_weibo", "pid");
		if(replyId == null && mid != null){//当对微博进行评论时,插入互动记录表数据--伪互动
			LinksusInteractRecord inksusInteractRecord = new LinksusInteractRecord();
			recordId = PrimaryKeyGen.getPrimaryKey("linksus_interact_record", "record_id");
			inksusInteractRecord.setRecordId(recordId);
			//查询平台账户的userId
			inksusInteractRecord.setUserId(Long.parseLong((String) map.get("userid")));
			inksusInteractRecord.setAccountId((Long) map.get("accountId"));
			inksusInteractRecord.setAccountType(Integer.parseInt((String) map.get("accountType")));
			inksusInteractRecord.setInteractType(1);
			inksusInteractRecord.setWeiboId((Long) map.get("mid"));
			inksusInteractRecord.setFakeFlag(1);
			inksusInteractRecord.setNewMsgFlag(1);
			inksusInteractRecord.setMessageId(new Long(0));
			inksusInteractRecord.setUpdateTime(Integer.valueOf(String.valueOf(new Date().getTime() / 1000)));
			linksusInteractRecordService.insertLinksusInteractRecord(inksusInteractRecord);

			entity.setAccountId((Long) map.get("accountId"));
			entity.setAccountType(Integer.parseInt((String) map.get("accountType")));
			entity.setUserId(Long.parseLong((String) map.get("userid")));
			entity.setRecordId(recordId);
			entity.setWeiboId((Long) map.get("mid"));
			entity.setSourceWeiboId(new Long(0));
			entity.setCommentId(new Long(0));
			entity.setReplyMsgId(new Long(0));
		}else{
			linksusInteractWeibo = linksusInteractWeiboService.getLinksusInteractWeiboById((Long) map.get("replyId"));
			if(linksusInteractWeibo != null){
				entity.setAccountId(linksusInteractWeibo.getAccountId());
				entity.setAccountType(linksusInteractWeibo.getAccountType());
				entity.setUserId(linksusInteractWeibo.getUserId());
				entity.setRecordId(linksusInteractWeibo.getRecordId());
				entity.setWeiboId(linksusInteractWeibo.getWeiboId());
				entity.setSourceWeiboId(linksusInteractWeibo.getSourceWeiboId());
				entity.setCommentId(linksusInteractWeibo.getCommentId());
				entity.setReplyMsgId(linksusInteractWeibo.getPid());
			}else{
				return "20018";
			}
		}
		entity.setPid(pid);
		entity.setContent(content);
		entity.setInstPersonId((Long) map.get("instPersonId"));
		entity.setInteractType(7);
		entity.setSendType(sendType);
		if(sendType == 1){
			entity.setStatus(1);// 1.未发布
			entity.setSendTime((Integer) map.get("sendTime"));//为定时发布时设置定时发布时间	
		}else{
			entity.setStatus(3);// 3.发布失败
			entity.setSendTime(new Integer(0));
		}
		entity.setInteractTime(DateUtil.getUnixDate(new Date()));
		linksusInteractWeiboService.insertLinksusInteractWeibo(entity);
		//QueueDataSave.addDataToQueue(linksusInteractWeibo, "insert");
		entity.setReplyId(pid);
		taskReplyComments.accountType = Integer.parseInt(map.get("accountType").toString());
		result = taskReplyComments.sendImmediate(entity);
		return result;
	}

	/**
	 * 发布私信评论
	 * 
	 * @param id,account_id,comment,pids,send_type,inst_person_id
	 * @param event
	 * @return
	 */
	public String publishWeiboMsg(Map map) throws Exception{
		TaskSendSinaLetter taskSendSinaLetter = new TaskSendSinaLetter();
		Long id = Long.parseLong(map.get("replyId").toString());
		//Long account_id = Long.parseLong(map.get("accountId").toString());
		Integer sendType = (Integer) map.get("sendType");
		Integer sentTime = 0;
		if(sendType == 1){
			sentTime = (Integer) map.get("sentTime");
		}
		Long instPersonId = (Long) map.get("instPersonId");
		// 微博私信的类型

		String content = (String) map.get("content") == null ? "" : (String) map.get("content");
		String articles = (String) map.get("articles") == null ? "" : (String) map.get("articles");
		String img = "";
		String imgName = "";
		if(StringUtils.isNotBlank(articles)){
			img = JsonUtil.getNodeValueByName(articles, "image");
			imgName = JsonUtil.getNodeValueByName(articles, "display_name");
		}
		String result = "";
		LinksusInteractMessage linksusInteractMessage = linksusInteractMessageService.getLinksusInteractMessageById(id);
		if(linksusInteractMessage == null){
			return "20018";
		}
		//此处判断是否超出回复次数
		if(linksusInteractMessage.getMsgType() != 5 && linksusInteractMessage.getReplyCount() >= 1){//超出回复次数			
			return "20009";
		}else if(linksusInteractMessage.getMsgType() == 5 && linksusInteractMessage.getReplyCount() >= 3){
			return "20010";
		}
		Integer lastInteractTime = linksusInteractMessage.getInteractTime();
		Date currDate = new Date();
		if(currDate.getTime() - lastInteractTime * 1000L > 72 * 3600 * 1000L){//超过72小时 不能回复
			return "20011";
		}
		if(linksusInteractMessage != null){
			LinksusInteractMessage entity = new LinksusInteractMessage();
			Long pid = PrimaryKeyGen.getPrimaryKey("linksus_interact_message", "pid");
			entity.setPid(pid);
			entity.setRecordId(linksusInteractMessage.getRecordId());
			entity.setUserId(linksusInteractMessage.getUserId());
			entity.setAccountId(linksusInteractMessage.getAccountId());
			entity.setAccountType(linksusInteractMessage.getAccountType());
			entity.setMsgId(linksusInteractMessage.getMsgId());
			entity.setInteractType(2);
			entity.setMsgType(5);
			entity.setContent(content);
			entity.setImg(img);
			entity.setImgName(imgName);
			entity.setImgThumb("");
			entity.setAttatch("");
			entity.setAttatchName("");
			entity.setReplyCount(0);
			entity.setSendType(sendType);
			entity.setReplyMsgId(linksusInteractMessage.getPid());
			if(sendType == 1){
				entity.setStatus(1);// 1.未发布
			}else{
				entity.setStatus(3);// 3.发布失败
			}
			entity.setSendTime(sentTime);
			entity.setInstPersonId(instPersonId);
			entity.setInteractTime(DateUtil.getUnixDate(new Date()));
			linksusInteractMessageService.insertLinksusInteractMessage(entity);
			//QueueDataSave.addDataToQueue(entity, "insert");
			entity.setUserName((String) map.get("username"));
			entity.setLastInteractTime(linksusInteractMessage.getInteractTime());//上次互动时间
			entity.setReplyCount(linksusInteractMessage.getReplyCount());//回复次数
			entity.setMsgType(linksusInteractMessage.getMsgType());//消息类型
			entity.setReplyId(linksusInteractMessage.getMsgId());//回复私信ID
			result = taskSendSinaLetter.sendMsgInterface(entity);
		}else{
			return "10203";
		}
		return result;
	}

	/**
	 * 发布微信消息
	 * 
	 * @param id,account_id,comment,pids,send_type,inst_person_id
	 * @param event
	 * @return
	 */
	public String publishWeixinContent(Map map) throws Exception{
		String rsultStr = "";
		Long account_id = (Long) map.get("accountId");
		String content = (String) map.get("content");
		Long userId = (Long) map.get("userId");
		if(StringUtils.isBlank(content)){
			content = "";
		}
		Integer sendType = (Integer) map.get("sendType");
		Long instPersonId = (Long) map.get("instPersonId");

		int msgType = (Integer) map.get("msgType");
		String token = map.get("access_token").toString();
		String restr = "";

		Map paraMap = new HashMap();
		paraMap.put("account_id", account_id);
		paraMap.put("user_id", userId);
		paraMap.put("sort", "interact_time");
		paraMap.put("sort_type", "desc");
		paraMap.put("sort_type", "desc");
		int send_time = 0;
		try{
			LinksusInteractWeixin linksusInteractWeixin = linksusInteractWeixinService
					.getLinksusInteractWeixinByMap(paraMap);
			if(linksusInteractWeixin != null){
				// 判断回复时间
				Integer lnteractTime = linksusInteractWeixin.getInteractTime();

				if(sendType == 0){// 及时发布
					Date currDate = new Date();
					if(currDate.getTime() - lnteractTime * 1000L > 48 * 3600 * 1000L){// 超过48小时
						// 不能回复
						logger.error("微信{}已超过48小时,不能被回复", linksusInteractWeixin.getPid());
						return "20029";
					}
				}else if(sendType == 1){// 定时发布
					send_time = Integer.parseInt(map.get("send_time").toString());
					if(send_time * 1000L - lnteractTime * 1000L > 48 * 3600 * 1000L){// 超过48小时
						// 不能回复
						logger.error("微信{}已超过48小时,不能被回复", linksusInteractWeixin.getPid());
						return "20029";
					}
				}

				LinksusInteractWeixin entity = new LinksusInteractWeixin();
				Long pid = PrimaryKeyGen.getPrimaryKey("linksus_interact_weixin", "pid");

				entity.setPid(pid);
				entity.setAccountId(linksusInteractWeixin.getAccountId());
				entity.setUserId(linksusInteractWeixin.getUserId());
				entity.setContent(content);
				entity.setInstPersonId(instPersonId);
				entity.setRecordId(linksusInteractWeixin.getRecordId());
				if(msgType == 2){
					msgType = 5;
				}else if(msgType == 3){
					msgType = 6;
				}
				entity.setMsgType(msgType);

				Long materialId = 0L;
				if(msgType != 1){
					materialId = publishWeixinMaterial(map);
				}
				entity.setMaterialId(materialId);
				entity.setOpenid(linksusInteractWeixin.getOpenid());
				entity.setInteractType(2);
				entity.setSendType(sendType);
				if(sendType == 1){
					entity.setStatus(1);// 1.未发布
				}else{
					entity.setStatus(3);// 3.发布失败
				}
				entity.setInteractTime(lnteractTime);
				entity.setSendTime(send_time);
				linksusInteractWeixinService.insertLinksusInteractWeixin(entity);
				// 回复微信
				TaskReplyWeixins replyWeixins = new TaskReplyWeixins();
				restr = replyWeixins.sendReplyWeixin(entity);
				if(StringUtils.isNotBlank(restr) && restr.contains("#回复微信发布成功")){
					rsultStr = "";
				}else{
					rsultStr = "20026";
				}

			}else{
				return "20025";
			}
		}catch (Exception e){
			LogUtil.saveException(logger, e);
			throw e;
		}
		return rsultStr;
	}

	/**
	 * 发布微信消息素材
	 * 
	 * @param id,account_id,comment,pids,send_type,inst_person_id
	 * @param event
	 * @return
	 */
	public Long publishWeixinMaterial(Map map) throws Exception{
		Long institution_id = Long.parseLong(map.get("institution_id") + "");
		//String materialType = map.get("materialType").toString();
		Integer materialType = 0;
		Integer msgType = (Integer) map.get("msgType");
		if(msgType == 2){
			materialType = 1;
		}else if(msgType == 3){
			materialType = 2;
		}
		Long material_id = null;
		int lastUseTime = Integer.valueOf(String.valueOf(new Date().getTime() / 1000));
		try{
			LinksusInteractWxMaterial entity = new LinksusInteractWxMaterial();
			material_id = PrimaryKeyGen.getPrimaryKey("linksus_interact_wx_material", "material_id");
			entity.setMaterialId(material_id);
			entity.setMaterialType(materialType);
			entity.setInstitutionId(institution_id);
			entity.setStatus(1);
			entity.setUseCount(0);
			entity.setLastUseTime(lastUseTime);
			entity.setUpdateTime(lastUseTime);
			//插入微信素材表
			linksusInteractWxMaterialService.insertLinksusInteractWxMaterial(entity);
			map.put("material_id", material_id);
			publishWeixinArticle(map);
		}catch (Exception e){
			e.printStackTrace();
		}
		return material_id;
	}

	/**
	 * 发布微信消息图文消息
	 * 
	 * @param id,account_id,comment,pids,send_type,inst_person_id
	 * @param event
	 * @return
	 */
	public String publishWeixinArticle(Map map) throws Exception{
		Long material_id = Long.parseLong(map.get("material_id").toString());
		Long pid = null;

		// title varchar(100) not null default '' comment '标题',
		// auther_name varchar(30) not null default '' comment '作者',
		// summary varchar(150) not null default '' comment '摘要',
		// pic_original_url varchar(100) not null default '' comment '原图url',
		// pic_middle_url varchar(100) not null default '' comment '中图url',
		// pic_thumb_url varchar(100) not null default '' comment '略微图url',
		// content varchar(2000) not null default '' comment '内容',
		// conent_url varchar(100) not null default '' comment '内容的url',
		String articles = "[{\"title\":\"title\",\"autherName\":\"autherName\",\"summary\":\"summary\",\"picOriginalUrl\":\"picOriginalUrl\",\"picMiddleUrl\":\"picMiddleUrl\",\"picThumbUrl\":\"picThumbUrl\",\"content\":\"content\",\"conentUrl\":\"conentUrl\"},{\"title\":\"title\",\"autherName\":\"autherName\",\"summary\":\"summary\",\"picOriginalUrl\":\"picOriginalUrl\",\"picMiddleUrl\":\"picMiddleUrl\",\"picThumbUrl\":\"picThumbUrl\",\"content\":\"content\",\"conentUrl\":\"123\"}]";
		articles = map.get("articles").toString();
		try{
			List<LinksusInteractWxArticle> list = (List) JsonUtil.json2list(articles, LinksusInteractWxArticle.class);
			// LinksusInteractWxArticle entity = new LinksusInteractWxArticle();
			System.err.println("publishWeixinArticle");
			for(LinksusInteractWxArticle entity : list){
				pid = PrimaryKeyGen.getPrimaryKey("linksus_interact_wx_article", "pid");
				entity.setPid(pid);
				entity.setMaterialId(material_id);
				linksusInteractWxArticleService.insertLinksusInteractWxArticle(entity);
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return "success";
	}

	/**
	 * 发布评论,接口使用,先调用第三方接口,根据返回成功状态来进行平台库的评论数据删除 发布评论
	 * 
	 * @param id,account_id,comment,pids,send_type,inst_person_id
	 * @param event
	 *            linksus_relation_reply_count
	 * @return
	 */
	public int replyCommentCount(Map map) throws Exception{
		if(!map.containsKey("commonReplyIds")){
			return 0;
		}
		String pids = (String) map.get("commonReplyIds");
		if(null == pids || pids.equals("")){
			return 0;
		}
		Long institution_id = Long.parseLong(map.get("institution_id").toString());
		Map<String, Integer> rsMap = new HashMap();
		int rs = 0;
		int count = 0;
		try{

			int lastUseTime = Integer.valueOf(String.valueOf(new Date().getTime() / 1000));
			rsMap = this.splitPid(pids);
			LinksusInteractReplyCount rsbean = null;
			LinksusInteractReplyCount entity = new LinksusInteractReplyCount();
			Map paraMap = new HashMap();
			paraMap.put("institution_id", institution_id);

			for(String reply_id : rsMap.keySet()){
				paraMap.put("reply_id", reply_id);
				rsbean = linksusInteractReplyCountService.getLinksusInteractReplyCountByMap(paraMap);
				if(null == rsbean){// 不存在则新增
					Long pid = PrimaryKeyGen.getPrimaryKey("linksus_interact_reply_count", "pid");
					entity.setPid(pid);
					entity.setReplyId(Long.parseLong(reply_id));
					entity.setInstitutionId(institution_id);
					entity.setUseCount(rsMap.get(reply_id));
					entity.setLastUseTime(lastUseTime);
					rs = linksusInteractReplyCountService.insertLinksusInteractReplyCount(entity);
					count += 1;
				}else{// 存在则更新
					entity = rsbean;
					entity.setUseCount(rsMap.get(reply_id) + rsbean.getUseCount());
					entity.setLastUseTime(lastUseTime);
					rs = linksusInteractReplyCountService.updateLinksusInteractReplyCount(entity);
					count += 1;
				}
			}

		}catch (Exception e){
			e.printStackTrace();
		}

		return count;
	}

	// 计算常用回复id数量
	private Map splitPid(String pids){
		Map resultmap = new HashMap();
		String[] args = pids.split(",");
		int count = 0;
		for(String key : args){
			if(null != key && !key.equals("")){
				if(resultmap.containsKey(key)){
					count = (Integer) resultmap.get(key);
					resultmap.put(key, count + 1);
				}else{
					resultmap.put(key, 1);
				}
			}
		}
		return resultmap;
	}

	/**
	 * 根据mid查询评论列表
	 * 
	 * @param id,account_type
	 * @return
	 */
	public Map getInteractWeiboListByMid(Map paramMap){
		Map map = new HashMap();
		map.put("weibo_id", paramMap.get("mid"));
		map.put("account_type", paramMap.get("type"));
		Integer page = (Integer) paramMap.get("page");
		Integer pageSize = (Integer) paramMap.get("pageSize");
		Integer startCount = (page - 1) * pageSize;
		map.put("startCount", startCount);
		map.put("pageSize", new Integer(pageSize));
		List list = linksusInteractWeiboService.getInteractWeiboListByMid(map);
		map.clear();
		map.put("data", list);
		return map;
	}

	//微博评论重发
	public String againReplyComment(Map map){
		String rs = "";
		Long replyId = (Long) map.get("replyId");
		LinksusInteractWeibo linksusInteractWeibo = linksusInteractWeiboService.getLinksusInteractWeiboById(replyId);
		if(linksusInteractWeibo != null){
			if(linksusInteractWeibo.getStatus() == 3 || linksusInteractWeibo.getStatus() == 1){
				TaskReplyComments taskReplyComments = new TaskReplyComments();
				linksusInteractWeibo.setReplyId(replyId);
				taskReplyComments.accountType = linksusInteractWeibo.getAccountType();
				rs = taskReplyComments.sendImmediate(linksusInteractWeibo);
			}else{
				return "20033";
			}
		}else{
			return "20018";
		}
		return rs;
	}

	//微信重发
	public String againReplyWeixin(Map map){
		String rs = "";
		Long replyId = (Long) map.get("replyId");
		LinksusInteractWeixin linksusInteractWeixin = linksusInteractWeixinService
				.getLinksusInteractWeixinById(replyId);
		if(linksusInteractWeixin != null){
			Integer currentTime = Integer.parseInt(String.valueOf(new Date().getTime() / 1000));
			if(linksusInteractWeixin.getStatus() == 2){
				return "20033";
			}
			// 判断回复时间
			Integer lnteractTime = linksusInteractWeixin.getInteractTime();

			if(linksusInteractWeixin.getSendType() == 0){// 及时发布
				Date currDate = new Date();
				if(currDate.getTime() - lnteractTime * 1000L > 48 * 3600 * 1000L){// 超过48小时
					// 不能回复
					linksusInteractWeixin.setStatus(4);
					linksusInteractWeixin.setInteractTime(currentTime);
					linksusInteractWeixinService.updateLinksusInteractWeixinStatus(linksusInteractWeixin);
					logger.error("微信 {} 已超过48小时,不能被回复", linksusInteractWeixin.getPid());
					return "20029";
				}
			}else if(linksusInteractWeixin.getSendType() == 1){// 定时发布
				if(linksusInteractWeixin.getSendTime() * 1000L - lnteractTime * 1000L > 48 * 3600 * 1000L){// 超过48小时
					// 不能回复
					linksusInteractWeixin.setStatus(4);
					linksusInteractWeixin.setInteractTime(currentTime);
					linksusInteractWeixinService.updateLinksusInteractWeixinStatus(linksusInteractWeixin);
					logger.error("微信{},已超过48小时,不能被回复", linksusInteractWeixin.getPid());
					return "20029";
				}
			}
			// 回复微信
			TaskReplyWeixins replyWeixins = new TaskReplyWeixins();
			rs = replyWeixins.sendReplyWeixin(linksusInteractWeixin);
			if(StringUtils.isNotBlank(rs) && rs.contains("#回复微信发布成功")){
				rs = "";
			}else{
				rs = "20026";
			}
		}else{
			return "20018";
		}
		return rs;
	}

	//私信重发
	public String againReplyMessage(Map map) throws Exception{
		String rs = "";
		Long id = (Long) map.get("replyId");
		LinksusInteractMessage linksusInteractMessage = linksusInteractMessageService.getLinksusInteractMessageById(id);
		if(linksusInteractMessage == null){
			return "20018";
		}
		if(linksusInteractMessage.getStatus() == 2){
			return "20033";
		}
		TaskSendSinaLetter taskSendSinaLetter = new TaskSendSinaLetter();
		linksusInteractMessage.setUserName((String) map.get("username"));
		linksusInteractMessage.setLastInteractTime(linksusInteractMessage.getInteractTime());//上次互动时间
		linksusInteractMessage.setReplyCount(linksusInteractMessage.getReplyCount());//回复次数
		linksusInteractMessage.setMsgType(linksusInteractMessage.getMsgType());//消息类型
		linksusInteractMessage.setReplyId(linksusInteractMessage.getMsgId());//回复私信ID
		rs = taskSendSinaLetter.sendMsgInterface(linksusInteractMessage);
		return rs;
	}

	public static void main(String[] args){
		TaskInterfaceComment weiboComment = new TaskInterfaceComment();
		Map paramMap = new HashMap();
		paramMap.put("institution_id", 1102);
		// map.put("material_id",2);
		paramMap.put("material_type", 3);

		paramMap.put("id", 1233);// 互动消息ID
		paramMap.put("account_id", 111);
		paramMap.put("content", "FUCK U2SX");
		paramMap.put("pids", "1,2,3");
		paramMap.put("send_type", 1);
		paramMap.put("inst_person_id", 10010);
		paramMap.put("type", 3);// 互动消息类型 1 微博评论 2私信 3微信

		// start 2私信
		paramMap.put("img", "img1");
		paramMap.put("img_name", "img_name1");
		paramMap.put("img_thumb", "img_thumb1");
		paramMap.put("attatch", "attatch1");
		paramMap.put("attatch_name", "attatch_name1");
		// end 2私信
		// start 3微信
		paramMap.put("msg_type", 2);
		// paramMap.put("material_id", "material_id");
		String articles = "[{\"title\":\"title\",\"autherName\":\"autherName\",\"summary\":\"summary\",\"picOriginalUrl\":\"picOriginalUrl\",\"picMiddleUrl\":\"picMiddleUrl\",\"picThumbUrl\":\"picThumbUrl\",\"content\":\"content\",\"conentUrl\":\"conentUrl\"},{\"title\":\"title\",\"autherName\":\"autherName\",\"summary\":\"summary\",\"picOriginalUrl\":\"picOriginalUrl\",\"picMiddleUrl\":\"picMiddleUrl\",\"picThumbUrl\":\"picThumbUrl\",\"content\":\"content\",\"conentUrl\":\"123\"}]";
		paramMap.put("content", "NIHAO喂你好");
		paramMap.put("articles", articles);
		// start 3微信
		paramMap.put("id", "3678678980446378");
		paramMap.put("switcher", "1");

		// paramMap.put("account_id", 1);
		try{
			weiboComment.publishComment(paramMap);
		}catch (Exception e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// weiboComment.removeWeiboComment(paramMap);
		// weiboComment.publishWeixinMaterial(paramMap);
		// weiboComment.publishWeixinMaterial(paramMap);
	}
}
