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

import sun.misc.BASE64Encoder;

import com.linksus.common.Constants;
import com.linksus.common.config.LoadConfig;
import com.linksus.common.util.ContextUtil;
import com.linksus.common.util.DateUtil;
import com.linksus.common.util.HttpUtil;
import com.linksus.common.util.JsonUtil;
import com.linksus.common.util.LogUtil;
import com.linksus.common.util.QuartzUtil;
import com.linksus.common.util.StringUtil;
import com.linksus.entity.LinksusAppaccount;
import com.linksus.entity.LinksusGovMessage;
import com.linksus.entity.LinksusTaskErrorCode;
import com.linksus.entity.UrlEntity;
import com.linksus.service.LinksusAppaccountService;
import com.linksus.service.LinksusGovMessageService;
import com.linksus.service.LinksusRelationWeibouserService;

/**
 * д˽������
 */
public class GovTaskSendSinaLetter extends BaseTask{

	LinksusGovMessageService messageService = (LinksusGovMessageService) ContextUtil
			.getBean("linksusGovMessageService");
	LinksusRelationWeibouserService userService = (LinksusRelationWeibouserService) ContextUtil
			.getBean("linksusRelationWeibouserService");
	LinksusAppaccountService linksusAppaccountService = (LinksusAppaccountService) ContextUtil
			.getBean("linksusAppaccountService");

	// URL��ַ
	public static final String path = LoadConfig.getString("replyLetter");

	/**���ͷ�ʽ  ��ʱ/��ʱ */
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
	 * ��ʱ����
	 */
	private void sendAtRegularTime(){
		try{
			LinksusGovMessage queryMsg = new LinksusGovMessage();
			int startCount = (currentPage - 1) * pageSize;
			queryMsg.setPageSize(pageSize);
			queryMsg.setStartCount(startCount);
			List msgIdList = messageService.getMessagePrepareList(queryMsg);
			for(int i = 0; i < msgIdList.size(); i++){
				Map msg = (Map) msgIdList.get(i);
				addMessageTimer(msg);
			}
			checkTaskListEnd(msgIdList);//�ж������Ƿ���ѯ���
		}catch (Exception e){
			LogUtil.saveException(logger, e);
			e.printStackTrace();
		}
	}

	/**
	 * ֱ�ӷ���
	 */
	public void sendImmediate(){
		// ��ѯ��ǰ���Է��͵�˽��
		try{
			LinksusGovMessage queryMsg = new LinksusGovMessage();
			int startCount = (currentPage - 1) * pageSize;
			queryMsg.setPageSize(pageSize);
			queryMsg.setStartCount(startCount);
			List<LinksusGovMessage> messageList = messageService.getSendMessageList(queryMsg);
			for(LinksusGovMessage message : messageList){
				sendMessage(message);
			}
			this.checkTaskListEnd(messageList);
		}catch (Exception e){
			LogUtil.saveException(logger, e);
			e.printStackTrace();
		}
	}

	/**
	 * ǰ�˵��ýӿ�
	 * @param message 
	 * @param replyMessage Դ˽��
	 * @return
	 * @throws Exception
	 */
	public String sendMsgInterface(LinksusGovMessage message) throws Exception{
		String rs = "";
		if(message.getSendType() == 0){//��ʱ����
			rs = sendMessage(message);
		}else if(message.getSendType() == 1){//��ʱ����
			Map map = new HashMap();
			map.put("runId", message.getRunId());
			map.put("send_time", message.getSendTime());
			addMessageTimer(map);
			rs = "add";
		}
		return rs;
	}

	/**
	 * �ظ�����˽��
	 * @param message
	 * @throws Exception 
	 * @throws Exception 
	 */
	public String sendMessage(LinksusGovMessage message) throws Exception{
		Date date = new Date();
		Integer interactTime = DateUtil.getUnixDate(new Date());
		//�жϻظ�ʱ��
		Integer lastInteractTime = message.getLastInteractTime();
		Date currDate = new Date();
		if(currDate.getTime() - lastInteractTime * 1000L > 72 * 3600 * 1000L){//����72Сʱ ���ܻظ�
			logger.error("˽��{}�ѳ���72Сʱ,���ܱ��ظ�", message.getReplyMsgId());
			Map messages = new HashMap();
			messages.put("status", 3);
			messages.put("interactTime", message.getInteractTime());
			messages.put("runId", message.getRunId());
			messageService.updateLinksusGovMessageStatus(messages);
			return "20011";
		}
		LinksusAppaccount appcount = linksusAppaccountService.getLinksusAppaccountAndUserIdById(message.getAccountId());
		//��ѯ�˺��ǳ�
		//String userName=userService.getWeibouserNameByAccountId(message.getAccountId());
		//message.setUserName(userName);
		//��װ˽������
		Map paramMap = parseMsgData(message);
		paramMap.put("access_token", appcount.getToken());
		paramMap.put("receiver_id", message.getUserId());
		//����˽��
		String jsonStr = sendNewLetterToSina(appcount.getToken(), paramMap);
		//String jsonStr = HttpUtil.postRequest(path, paramMap,Constants.APP_TYPE_SINA);
		//���ݷ����޸�˽��״̬
		LinksusTaskErrorCode error = StringUtil.parseSinaErrorCode(jsonStr);
		if(error == null){//��ȷ
			Map messages = new HashMap();
			messages.put("status", 2);
			messages.put("interactTime", interactTime);
			messages.put("runId", message.getRunId());
			messageService.updateLinksusGovMessageStatus(messages);
		}else{
			Map messages = new HashMap();
			messages.put("status", 3);
			messages.put("interactTime", message.getInteractTime());
			messages.put("runId", message.getRunId());
			messageService.updateLinksusGovMessageStatus(messages);
			return error.getErrorCode().toString();
		}
		return "";
	}

	private String sendNewLetterToSina(String token,Map paramMap) throws Exception{
		//UrlEntity urlEntity = LoadConfig.getUrlEntity("replyLetter");
		String result = HttpUtil.postRequest(LoadConfig.getUrlEntity("replyLetter"), paramMap);
		return result;
	}

	/**
	 * ��װ˽�Žṹ
	 * @param message
	 * @return
	 */
	private Map parseMsgData(LinksusGovMessage message){
		Map rsMap = new HashMap();
		String imgs = message.getImg();
		if(message.getMsgType() == 1){//���ı�
			rsMap.put("type", "text");
			rsMap.put("data", "{\"text\":\"" + message.getContent() + "\"}");
		}else if(message.getMsgType() == 2){//ͼ��
			rsMap.put("type", "articles");
			Map map = new HashMap();
			map.put("image", message.getImg());
			map.put("summary", message.getImgName());
			map.put("url", "http://www.1510cloud.com");
			rsMap.put("data", "{\"articles\":[" + JsonUtil.map2json(map) + "]}");
		}
		return rsMap;
	}

	/**
	 * ��˽�ŵ����� �����Ӵ�������
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	private String sendLetterToSina(Map paramMap) throws Exception{
		// ����HttpClient��ʵ��
		HttpClient httpClient = new HttpClient();
		httpClient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		// ����POST������ʵ��
		UrlEntity urlEntity = LoadConfig.getUrlEntity("replyLetter");
		PostMethod postMethod = new PostMethod(urlEntity.getUrl());
		//PostMethod postMethod = new PostMethod(path);
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
			// �ͻ�������url����
			statusCode = httpClient.executeMethod(postMethod);
			/*
			 * Header[] headers = postMethod.getResponseHeaders(); for (Header
			 * header : headers) {
			 * logger.debug("{}-------{}",header.getName(),header.getValue()); }
			 */
		}catch (Exception e){
			LogUtil.saveException(logger, e);
			e.printStackTrace();
		}

		// ����ɹ�״̬-200
		if(statusCode == HttpStatus.SC_OK || statusCode == HttpStatus.SC_BAD_REQUEST){
			try{
				result = postMethod.getResponseBodyAsString();
			}catch (IOException e){
				LogUtil.saveException(logger, e);
				e.printStackTrace();
			}
		}else{
			logger.error("���󷵻�״̬��{}", statusCode);
		}
		return result;
	}

	/**
	 * ˽�Ŷ�ʱ����
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
		GovTaskSendSinaLetter letter = new GovTaskSendSinaLetter();
		letter.setSendType("1");
		letter.cal();
		// String str=new
		// String("{\"text\": \"���ı��ظ�5\"}".getBytes("UTF-8"),"UTF-8");
		// System.out.println(str);
	}

}
