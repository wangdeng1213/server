package com.linksus.task;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang.StringUtils;

import sun.misc.BASE64Encoder;

import com.linksus.common.Constants;
import com.linksus.common.config.LoadConfig;
import com.linksus.common.util.ContextUtil;
import com.linksus.common.util.JsonUtil;
import com.linksus.common.util.LogUtil;
import com.linksus.common.util.QuartzUtil;
import com.linksus.common.util.StringUtil;
import com.linksus.entity.LinksusInteractMessage;
import com.linksus.entity.LinksusTaskErrorCode;
import com.linksus.entity.LinksusTaskInvalidRecord;
import com.linksus.entity.UrlEntity;
import com.linksus.service.LinksusInteractMessageService;
import com.linksus.service.LinksusRelationWeibouserService;

/**
 * 写私信任务
 */
public class TaskSendSinaLetter extends BaseTask{

	LinksusInteractMessageService messageService = (LinksusInteractMessageService) ContextUtil
			.getBean("linksusInteractMessageService");
	LinksusRelationWeibouserService userService = (LinksusRelationWeibouserService) ContextUtil
			.getBean("linksusRelationWeibouserService");
	// URL地址
	public static final String path = LoadConfig.getString("replyLetter");

	/**发送方式  即时/定时 */
	private String sendType;

	public void setSendType(String sendType){
		this.sendType = sendType;
	}

	public void cal(){
		if("0".equals(sendType)){
			sendImmediate();
		}else if("1".equals(sendType)){
			sendAtRegularTime();
		}
	}

	/**
	 * 定时发送
	 */
	private void sendAtRegularTime(){
		try{
			LinksusInteractMessage queryMsg = new LinksusInteractMessage();
			int startCount = (currentPage - 1) * pageSize;
			queryMsg.setPageSize(pageSize);
			queryMsg.setStartCount(startCount);
			List msgIdList = messageService.getMessagePrepareList(queryMsg);
			for(int i = 0; i < msgIdList.size(); i++){
				Map msg = (Map) msgIdList.get(i);
				addMessageTimer(msg);
			}
			checkTaskListEnd(msgIdList);//判断任务是否轮询完成
		}catch (Exception e){
			LogUtil.saveException(logger, e);
			e.printStackTrace();
		}
	}

	/**
	 * 直接发送
	 */
	public void sendImmediate(){
		// 查询当前可以发送的私信
		try{
			LinksusInteractMessage queryMsg = new LinksusInteractMessage();
			int startCount = (currentPage - 1) * pageSize;
			queryMsg.setPageSize(pageSize);
			queryMsg.setStartCount(startCount);
			List<LinksusInteractMessage> messageList = messageService.getSendMessageList(queryMsg);
			for(LinksusInteractMessage message : messageList){
				sendMessage(message);
			}
			this.checkTaskListEnd(messageList);
		}catch (Exception e){
			LogUtil.saveException(logger, e);
			e.printStackTrace();
		}
	}

	/**
	 * 前端调用接口
	 * @param message 
	 * @param replyMessage 源私信
	 * @return
	 * @throws Exception
	 */
	public String sendMsgInterface(LinksusInteractMessage message) throws Exception{
		String rs = "";
		if(message.getSendType() == 0){//即时发布
			rs = sendMessage(message);
		}else if(message.getSendType() == 1){//定时发布
			Map map = new HashMap();
			map.put("pid", message.getPid());
			map.put("send_time", message.getSendTime());
			addMessageTimer(map);
		}
		return rs;
	}

	/**
	 * 回复单条私信
	 * @param message
	 * @throws Exception 
	 * @throws Exception 
	 */
	public String sendMessage(LinksusInteractMessage message) throws Exception{
		Date date = new Date();
		Integer interactTime = Integer.parseInt(String.valueOf(date.getTime() / 1000));
		//判断回复次数
		if(message.getMsgType() != 5 && message.getReplyCount() >= 1){//超出回复次数
			logger.error("私信{}超过回复次数,不能被回复", message.getReplyMsgId());
			Map messages = new HashMap();
			messages.put("status", 4);//失效
			messages.put("interactTime", message.getInteractTime());
			messages.put("pid", message.getPid());
			messageService.updateInteractMessageStatus(messages);
			//dealErrorCodeInvalidRecord();
			dealExceptionInvalidRecord(message.getAccountId(), message.getPid(), new Exception(
					"事件类型私信只能回复1次,超过回复次数,不能被回复!"));
			return "20009";
		}else if(message.getMsgType() == 5 && message.getReplyCount() >= 3){
			logger.error("私信{}超过回复次数,不能被回复", message.getReplyMsgId());
			Map messages = new HashMap();
			messages.put("status", 4);//失效
			messages.put("interactTime", message.getInteractTime());
			messages.put("pid", message.getPid());
			messageService.updateInteractMessageStatus(messages);
			dealExceptionInvalidRecord(message.getAccountId(), message.getPid(), new Exception(
					"私信、留言类型只能回复3次,超过回复次数,不能被回复!"));
			return "20010";
		}
		//判断回复时间
		LinksusInteractMessage linksusInteractMessage = messageService.getLinksusInteractMessageById(message
				.getReplyMsgId());
		if(linksusInteractMessage != null){
			Integer lastInteractTime = linksusInteractMessage.getInteractTime();
			Date currDate = new Date();
			if(currDate.getTime() - lastInteractTime * 1000L > 72 * 3600 * 1000L){//超过72小时 不能回复
				logger.error("私信{}已超过72小时,不能被回复", message.getReplyMsgId());
				Map messages = new HashMap();
				messages.put("status", 4);//失效
				messages.put("interactTime", message.getInteractTime());
				messages.put("pid", message.getPid());
				messageService.updateInteractMessageStatus(messages);
				dealExceptionInvalidRecord(message.getAccountId(), message.getPid(), new Exception("私信已超过72小时,不能被回复!"));
				return "20011";
			}
		}
		//查询账号昵称
		//String userName=userService.getWeibouserNameByAccountId(message.getAccountId());
		//message.setUserName(userName);
		//组装私信内容
		Map paramMap = parseMsgData(message);
		paramMap.put("msgId", message.getReplyId() + "");
		//发送私信
		String jsonStr = sendLetterToSina(paramMap);
		//根据返回修改私信状态
		LinksusTaskErrorCode error = StringUtil.parseSinaErrorCode(jsonStr);
		if(error == null){//正确
			Map messages = new HashMap();
			messages.put("status", 2);
			messages.put("interactTime", interactTime);
			messages.put("pid", message.getPid());
			messageService.updateInteractMessageStatus(messages);
			messageService.updateInteractMessageReplyCount(message.getReplyMsgId());//更新源私信回复次数
		}else{
			dealErrorCodeInvalidRecord(message.getAccountId(), message.getPid(), error.getSrcCode().toLowerCase(),
					jsonStr);
			Map messages = new HashMap();
			String resultStr = "";
			if(Constants.MESSAGENOTEXISTENT.contains(error.getSrcCode())){
				messages.put("status", 4);
				resultStr = "20038";
			}else{
				messages.put("status", 3);
			}
			messages.put("interactTime", message.getInteractTime());
			messages.put("pid", message.getPid());
			messageService.updateInteractMessageStatus(messages);
			return resultStr;
		}
		return "";
	}

	/**
	 * 组装私信结构
	 * @param message
	 * @return
	 */
	private Map parseMsgData(LinksusInteractMessage message){
		Map rsMap = new HashMap();
		String imgs = message.getImg();
		if(StringUtils.isBlank(imgs) || "[\"\"]".equals(imgs)){//text类型私信
			rsMap.put("msgType", "text");
			String content = message.getContent();
			//rsMap.put("msgData","{\"text\":\""+JsonUtil.string2json(content)+"\"}");
			rsMap.put("msgData", "{\"text\":\"" + content + "\"}");
		}else{
			rsMap.put("msgType", "articles");
			Map map = new HashMap();
			map.put("display_name", "来自" + message.getUserName());
			map.put("image", message.getImg());
			map.put("summary", message.getImgName());
			map.put("url", "http://www.1510cloud.com");
			rsMap.put("msgData", "{\"articles\":[" + JsonUtil.map2json(map) + "]}");
		}
		return rsMap;
	}

	/**
	 * 发私信到新浪 无连接次数限制
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	private String sendLetterToSina(Map paramMap) throws Exception{
		// 构造HttpClient的实例
		HttpClient httpClient = new HttpClient();
		httpClient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		// 创建POST方法的实例
		UrlEntity urlEntity = LoadConfig.getUrlEntity("replyLetter");
		PostMethod postMethod = new PostMethod(urlEntity.getUrl());
		//String msgData=URLEncoder.encode(msgSlave.getMsgData(), "UTF-8");
		NameValuePair[] data = {
				new NameValuePair("source", cache.getAppInfo(Constants.CACHE_SINA_APPINFO).getAppKey()),
				new NameValuePair("id", (String) paramMap.get("msgId")),
				new NameValuePair("type", (String) paramMap.get("msgType")),
				new NameValuePair("data", (String) paramMap.get("msgData")) };

		String authStr = cache.getAppInfo(Constants.CACHE_SINA_APPINFO).getName() + ":"
				+ cache.getAppInfo(Constants.CACHE_SINA_APPINFO).getPsssword();
		BASE64Encoder encoder = new BASE64Encoder();
		String headerKey = "Authorization";
		String headerValue = "Basic " + encoder.encode(authStr.getBytes());
		postMethod.setRequestHeader(headerKey, headerValue);

		postMethod.setRequestBody(data);
		int statusCode = 0;
		String result = "";
		try{
			// 客户端请求url数据
			statusCode = httpClient.executeMethod(postMethod);
			/*
			 * Header[] headers = postMethod.getResponseHeaders(); for (Header
			 * header : headers) { logger.debug("{}-------{}",header.getName() ,
			 * header.getValue()); }
			 */
		}catch (Exception e){
			LogUtil.saveException(logger, e);
			e.printStackTrace();
		}

		// 请求成功状态-200
		if(statusCode == HttpStatus.SC_OK || statusCode == HttpStatus.SC_BAD_REQUEST){
			try{
				result = postMethod.getResponseBodyAsString();
			}catch (IOException e){
				LogUtil.saveException(logger, e);
				e.printStackTrace();
			}
		}else{
			logger.error("请求返回状态：{}", statusCode);
		}
		return result;
	}

	/**
	 * 私信定时任务
	 * 
	 * @param record
	 */
	private void addMessageTimer(Map msgMap){
		Map dataMap = new HashMap();
		dataMap.put("pid", msgMap.get("pid"));
		Date sendTime = new Date(Long.parseLong(msgMap.get("send_time").toString()) * 1000);
		QuartzUtil.addSimpleJob("RECORD_" + msgMap.get("pid").toString(), Constants.RECORD_GROUP_NAME, dataMap,
				sendTime, SendLetterTimer.class);
	}

	public static void main(String[] args) throws Exception{
		TaskSendSinaLetter letter = new TaskSendSinaLetter();
		letter.setSendType("1");
		letter.cal();
		// String str=new
		// String("{\"text\": \"纯文本回复5\"}".getBytes("UTF-8"),"UTF-8");
		// System.out.println(str);
	}

	private void dealExceptionInvalidRecord(Long institutionId,Long pid,Exception exception){
		try{
			LinksusTaskInvalidRecord invalidRecord = new LinksusTaskInvalidRecord();
			invalidRecord.setInstitutionId(institutionId);
			invalidRecord.setErrorCode("EXCEPTION");
			invalidRecord.setErrorMsg(LogUtil.getExceptionStackMsg(exception));
			invalidRecord.setOperType(Constants.INVALID_RECORD_INTERACT_MESSAGE);
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
			invalidRecord.setOperType(Constants.INVALID_RECORD_INTERACT_MESSAGE);
			invalidRecord.setRecordId(pid);
			dealInvalidRecord(invalidRecord);
		}catch (Exception e){
			LogUtil.saveException(logger, e);
		}
	}

}
