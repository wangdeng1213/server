package com.linksus.task;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import com.linksus.entity.LinksusInteractWeibo;
import com.linksus.entity.LinksusTaskErrorCode;
import com.linksus.entity.LinksusTaskInvalidRecord;
import com.linksus.entity.UrlEntity;
import com.linksus.service.LinksusAppaccountService;
import com.linksus.service.LinksusInteractWeiboService;

/**
 * ������Ѷ�ظ��������� ȡ����Ҫ�ظ�����Ϣid �������id weiboid
 */
public class TaskReplyComments extends BaseTask{

	protected static final Logger logger = LoggerFactory.getLogger(TaskReplyComments.class);

	protected LinksusInteractWeiboService linksusInteractWeiboService = (LinksusInteractWeiboService) ContextUtil
			.getBean("linksusInteractWeiboService");
	LinksusAppaccountService linksusAppaccountService = (LinksusAppaccountService) ContextUtil
			.getBean("linksusAppaccountService");

	/**���ͷ�ʽ  ��ʱ/��ʱ */
	private String sendType;

	public void setSendType(String sendType){
		this.sendType = sendType;
	}

	public static void main(String[] args){
		//		TaskReplyComments cc = new TaskReplyComments();
		//		cc.setSendType("0");
		//		cc.setAccountType("2");
		//		cc.cal();
	}

	@Override
	public void cal(){
		if("0".equals(sendType)){ //0������
			replyCommentImmediate();
		}else if("1".equals(sendType)){//1�Ƕ�ʱ
			replyCommentAtRegularTime();
		}
	}

	public void replyCommentImmediate(){
		try{
			// ����
			LinksusInteractWeibo linksusWeibo = new LinksusInteractWeibo();
			int startCount = (currentPage - 1) * pageSize;
			linksusWeibo.setPageSize(pageSize);
			linksusWeibo.setStartCount(startCount);
			linksusWeibo.setAccountType(accountType);
			linksusWeibo.setSendType(Integer.valueOf(sendType));
			List<LinksusInteractWeibo> recordList = linksusInteractWeiboService
					.getLinksusInteractWeiboTaskList(linksusWeibo);
			if(recordList != null && recordList.size() > 0){
				for(LinksusInteractWeibo weiboReply : recordList){
					sendWeiboReply(weiboReply);
				}
			}
			checkTaskListEnd(recordList);//�ж������Ƿ���ѯ���
		}catch (Exception e){
			LogUtil.saveException(logger, e);
			e.printStackTrace();
		}
	}

	// ǰ�˵��ýӿ�,��ʱ����ʱ��������
	public String sendImmediate(LinksusInteractWeibo weiboNewReply){
		String rsMsg = null;
		if(weiboNewReply != null){
			if(weiboNewReply.getStatus() == 3 || weiboNewReply.getStatus() == 1){
				if(weiboNewReply.getSendType() == 0){//��ʱ����
					rsMsg = sendWeiboReply(weiboNewReply);
				}else if(weiboNewReply.getSendType() == 1){//��ʱ����
					addWeiboTimer(weiboNewReply);
				}
			}else{
				//����״̬��Ϊ������	
				rsMsg = "20014";
			}
		}
		return rsMsg;
	}

	public String sendImmediate1(LinksusInteractWeibo weiboNewReply,LinksusInteractWeibo weiboOldReply){
		String rsMsg = "";
		if(weiboNewReply != null && weiboOldReply != null){
			accountType = weiboNewReply.getAccountType();
			if(weiboNewReply.getSendType() == 0){//��ʱ����
				if(weiboNewReply.getStatus() == 1){
					//���϶���
					weiboNewReply.setPid(weiboOldReply.getPid());
					weiboNewReply.setCommentId(weiboOldReply.getCommentId());
					weiboNewReply.setInteractType(weiboOldReply.getInteractType());
					weiboNewReply.setWeiboId(weiboOldReply.getWeiboId());
					rsMsg = sendWeiboReply(weiboNewReply);
				}else{
					//����״̬��Ϊ������	
					rsMsg = "20014";
				}
			}else if(weiboNewReply.getSendType() == 1){//��ʱ����
				if(weiboNewReply.getStatus() == 1){
					//���϶���
					weiboNewReply.setPid(weiboOldReply.getPid());
					weiboNewReply.setCommentId(weiboOldReply.getCommentId());
					weiboNewReply.setInteractType(weiboOldReply.getInteractType());
					weiboNewReply.setWeiboId(weiboOldReply.getWeiboId());
					addWeiboTimer(weiboNewReply);
				}else{
					//����״̬��Ϊ������	
					rsMsg = "20014";
				}
			}
		}
		return rsMsg;
	}

	//////////////////////////////////////��ʱ����////////////////////////////////////////////

	private void replyCommentAtRegularTime(){
		LinksusInteractWeibo linksusWeibo = new LinksusInteractWeibo();
		int startCount = (currentPage - 1) * pageSize;
		linksusWeibo.setPageSize(pageSize);
		linksusWeibo.setStartCount(startCount);
		linksusWeibo.setAccountType(accountType);
		linksusWeibo.setSendType(Integer.valueOf(sendType));
		List<LinksusInteractWeibo> recordList = linksusInteractWeiboService
				.getLinksusInteractWeiboTaskList(linksusWeibo);
		if(recordList != null && recordList.size() > 0){
			for(LinksusInteractWeibo record : recordList){
				addWeiboTimer(record);
			}
		}
		checkTaskListEnd(recordList);//�ж������Ƿ���ѯ���
	}

	// ǰ�˵��ýӿ�,��ʱ����΢��
	public String replyCommentAtRegularTime(String sendTime,String status,Long pid){
		String rsMsg = "";
		Integer time;
		try{
			time = new Integer(sendTime);// unixʱ��
		}catch (Exception e){
			//return "���ڲ�����ʽ����ȷ,10λUnixʱ��";
			return "20005";
		}
		LinksusInteractWeibo record = new LinksusInteractWeibo();
		record.setPid(pid);
		record.setSendTime(time);
		record.setStatus(new Integer(status));
		addWeiboTimer(record);
		return rsMsg;
	}

	/**
	 * ֱ��΢����ʱ����
	 * 
	 * @param record
	 */
	private void addWeiboTimer(LinksusInteractWeibo record){
		if(Constants.STATUS_DELETE.equals(record.getStatus() + "")
				|| Constants.STATUS_PAUSE.equals(record.getStatus() + "")){
			QuartzUtil.removeJob("RECORD_" + record.getPid().toString(), Constants.RECORD_GROUP_NAME);
		}else{
			Map dataMap = new HashMap();
			dataMap.put("pid", record.getPid());
			Date sendTime = new Date(Long.parseLong(record.getSendTime().toString()) * 1000);
			QuartzUtil.addSimpleJob("RECORD_" + record.getPid().toString(), Constants.RECORD_GROUP_NAME, dataMap,
					sendTime, SendReplyTimer.class);
		}
	}

	//����accountId��ȡ�˻�token
	public Map getAccountTokenMap(Long accountId){
		Map tokenMap = new HashMap();
		//��ȡ���������û�token
		LinksusAppaccount tokenObj = linksusAppaccountService.getLinksusAppaccountTokenById(accountId);
		if(tokenObj != null){
			tokenMap.put("token", tokenObj.getToken());
			tokenMap.put("openid", tokenObj.getAppid());
			tokenMap.put("type", accountType);
			tokenMap.put("appkey", tokenObj.getAppKey());
			tokenMap.put("InstitutionId", tokenObj.getInstitutionId());
		}
		return tokenMap;
	}

	//////////////////////////////////////�����߼�////////////////////////////////////////////
	/**
	 * �����ظ�
	 * @param record
	 */
	public String sendWeiboReply(LinksusInteractWeibo weibo){
		//ȡ���û���Ȩ
		String msg = "";
		Map tokenMap = getAccountTokenMap(weibo.getAccountId());
		if(tokenMap != null && tokenMap.size() > 0){
			if(weibo.getInteractType() == 1 || weibo.getInteractType() == 4 || weibo.getInteractType() == 7){//��Ҫ�����۽��лظ�
				//����α����,��΢����������
				if(weibo.getCommentId() == 0){
					msg = publishWeibo(weibo, tokenMap);
				}else{
					msg = publishReply(weibo, tokenMap);
				}
			}else{//��Ҫ��΢�����лظ�
				msg = publishWeibo(weibo, tokenMap);
			}
		}else{
			LinksusTaskInvalidRecord invalidRecord = new LinksusTaskInvalidRecord();
			invalidRecord.setErrorCode("20006");
			invalidRecord.setErrorMsg("�������˺�û����Ӧ��Ȩ");
			invalidRecord.setInstitutionId(weibo.getAccountId());
			invalidRecord.setOperType(Constants.INVALID_RECORD_INTERACT_WEIBO);
			invalidRecord.setRecordId(weibo.getPid());
			dealInvalidRecord(invalidRecord);
			msg = "20006";
		}
		return msg;
	}

	/**
	 *  �ظ�����
	 * @param record
	 * @param token
	 * @return
	 */
	public String publishReply(LinksusInteractWeibo weibo,Map authTokenMap){
		String msg = "";
		String rsData = "";
		boolean succFlag = true;
		String token = (String) authTokenMap.get("token");
		String appId = (String) authTokenMap.get("openid");
		if(accountType == 1){//����
			try{
				//�жϸ��û�sinaд����
				int hcount = cache.getCurrHourWriteCountByUser(appId, Constants.LIMIT_SEND_COMMENT_PER_HOUR);
				if(hcount == -1){//��������
					return "�Ѵﵽ��������";
				}
				String content = weibo.getContent();//�ظ��������� 
				UrlEntity strReply = LoadConfig.getUrlEntity("replyComments");
				Map paraMap = new HashMap();
				paraMap.put("cid", weibo.getCommentId());
				paraMap.put("access_token", token);
				paraMap.put("id", weibo.getWeiboId());
				paraMap.put("comment", content);
				paraMap.put("without_mention", 1);
				paraMap.put("comment_ori", 0);
				rsData = HttpUtil.postRequest(strReply, paraMap);
			}catch (Exception e){
				LogUtil.saveException(logger, e);
				msg = LogUtil.getExceptionStackMsg(e);
				succFlag = false;
			}
			//�жϷ��ؽ�� �����쳣��Ϣ
			LinksusTaskErrorCode error = StringUtil.parseSinaErrorCode(rsData);
			if(!succFlag || error != null){//����ʧ��
				LinksusInteractWeibo interactWeibo = new LinksusInteractWeibo();
				interactWeibo.setPid(weibo.getReplyId());
				LinksusTaskInvalidRecord invalidRecord = new LinksusTaskInvalidRecord();
				if(!"".equals(msg)){
					invalidRecord.setErrorCode(Constants.INVALID_RECORD_EXCEPTION);
					invalidRecord.setErrorMsg(msg);
				}else{
					invalidRecord.setErrorCode(error.getErrorCode().toString());
					invalidRecord.setErrorMsg(error.getErrorMsg());
				}
				invalidRecord.setInstitutionId((Long) authTokenMap.get("InstitutionId"));
				invalidRecord.setOperType(Constants.INVALID_RECORD_INTERACT_WEIBO);
				invalidRecord.setRecordId(weibo.getPid());
				dealInvalidRecord(invalidRecord);

				if(Constants.COMMENTOTEXISTENT.contains(error.getSrcCode())){//���۲�����
					interactWeibo.setStatus(4);
					msg = "20039";
				}else{
					interactWeibo.setStatus(3);
				}
				linksusInteractWeiboService.updateSendReplyStatus(interactWeibo);
				//�ظ����۷���������
				return invalidRecord.getErrorCode();
			}else{
				//���»�����Ϣ��
				Map rsMap = JsonUtil.json2map(rsData, Map.class);
				LinksusInteractWeibo interactWeibo = new LinksusInteractWeibo();
				interactWeibo.setPid(weibo.getReplyId());
				interactWeibo.setCommentId((Long) rsMap.get("id"));//���˷��ص�����id
				interactWeibo.setContent(weibo.getContent());
				Date createTime = DateUtil.parse(rsMap.get("created_at").toString(), "EEE MMM dd HH:mm:ss zzz yyyy",
						Locale.US);
				interactWeibo.setInteractTime(Integer.parseInt(String.valueOf(createTime.getTime() / 1000)));
				interactWeibo.setSendTime(Integer.parseInt(String.valueOf(createTime.getTime() / 1000)));
				interactWeibo.setStatus(new Integer(2));
				linksusInteractWeiboService.updateSendReplySucc(interactWeibo);
				//�����û�sina�������۴���
				cache.addCurrHourWriteCountByUser(appId, Constants.LIMIT_SEND_COMMENT_PER_HOUR);
				return msg;
			}
		}else if(weibo.getAccountType().intValue() == 2){//��Ѷ
			//�ж���Ѷ��΢���Ĵ�������
			//		    cache.checkConnLimitPerHour(Constants.LIMIT_TENCENT_WRITE_PER_HOUR);
			try{
				UrlEntity strurl = LoadConfig.getUrlEntity("TCreplyWeibo");
				Map paraMap = new HashMap();
				paraMap.put("format", "json");
				paraMap.put("access_token", token);
				paraMap.put("content", weibo.getContent());
				paraMap.put("clientip", Constants.TENCENT_CLIENT_IP);
				paraMap.put("reid", weibo.getCommentId());
				paraMap.put("oauth_consumer_key", cache.getAppInfo(Constants.CACHE_TENCENT_APPINFO).getAppKey());
				paraMap.put("openid", appId);
				paraMap.put("oauth_version", "2.a");
				paraMap.put("compatibleflag", "32");
				rsData = HttpUtil.postRequest(strurl, paraMap);
			}catch (Exception e){
				LogUtil.saveException(logger, e);
				msg = LogUtil.getExceptionStackMsg(e);
				succFlag = false;
			}
			//�жϷ��ؽ�� �����쳣��Ϣ
			LinksusTaskErrorCode error = StringUtil.parseTencentErrorCode(rsData);
			if(succFlag == false || error != null){//����ʧ��
				LinksusInteractWeibo interactWeibo = new LinksusInteractWeibo();
				interactWeibo.setPid(weibo.getReplyId());

				LinksusTaskInvalidRecord invalidRecord = new LinksusTaskInvalidRecord();
				if(!"".equals(msg)){
					invalidRecord.setErrorCode(Constants.INVALID_RECORD_EXCEPTION);
					invalidRecord.setErrorMsg(msg);
				}else{
					invalidRecord.setErrorCode(error.getErrorCode().toString());
					invalidRecord.setErrorMsg(error.getErrorMsg());
				}
				invalidRecord.setInstitutionId((Long) authTokenMap.get("InstitutionId"));
				invalidRecord.setOperType(Constants.INVALID_RECORD_INTERACT_WEIBO);
				invalidRecord.setRecordId(weibo.getPid());
				dealInvalidRecord(invalidRecord);
				msg = invalidRecord.getErrorCode();
				interactWeibo.setStatus(3);
				if(error != null && Constants.TCWEIBONOTEXISTENT.contains(error.getSrcCode().toString())){
					interactWeibo.setStatus(4);
					msg = "20039";
				}
				linksusInteractWeiboService.updateSendReplyStatus(interactWeibo);
				return msg;
			}else{
				//���»�����Ϣ��
				Map rsMap = JsonUtil.json2map(rsData, Map.class);
				Map dataMap = (Map) rsMap.get("data");
				LinksusInteractWeibo interactWeibo = new LinksusInteractWeibo();
				interactWeibo.setPid(weibo.getReplyId());
				interactWeibo.setInteractTime((Integer) dataMap.get("time"));
				interactWeibo.setSendTime((Integer) dataMap.get("time"));
				interactWeibo.setStatus(new Integer(2));
				//��������id
				interactWeibo.setCommentId(new Long((String) dataMap.get("id")));
				//����ʱ��
				linksusInteractWeiboService.updateSendReplySucc(interactWeibo);
				return msg;
			}
		}
		return msg;
	}

	/**
	 * �ظ�΢��
	 * @param record
	 * @param token
	 * @return
	 */
	public String publishWeibo(LinksusInteractWeibo weibo,Map authTokenMap){
		String msg = "";
		String rsData = null;
		boolean succFlag;
		String token = (String) authTokenMap.get("token");
		String appId = (String) authTokenMap.get("openid");
		if(accountType == 1){//����
			//�жϸ��û�sinaд����
			int hcount = cache.getCurrHourWriteCountByUser(appId, Constants.LIMIT_SEND_COMMENT_PER_HOUR);
			if(hcount == -1){//��������
				return "�Ѵﵽ��������";
			}
			try{
				String content = weibo.getContent();
				UrlEntity strReplyWeibo = LoadConfig.getUrlEntity("replyWeibo");
				Map paraMap = new HashMap();
				paraMap.put("access_token", token);
				paraMap.put("id", weibo.getWeiboId());
				paraMap.put("comment", content);
				paraMap.put("comment_ori", 0);
				rsData = HttpUtil.postRequest(strReplyWeibo, paraMap);
			}catch (Exception e){
				LogUtil.saveException(logger, e);
				msg = LogUtil.getExceptionStackMsg(e);
				succFlag = false;
			}
			//�жϷ��ؽ�� �����쳣��Ϣ
			LinksusTaskErrorCode error = StringUtil.parseSinaErrorCode(rsData);
			if(!"".equals(msg) || error != null){//����ʧ��
				LinksusInteractWeibo interactWeibo = new LinksusInteractWeibo();
				interactWeibo.setPid(weibo.getReplyId());

				LinksusTaskInvalidRecord invalidRecord = new LinksusTaskInvalidRecord();
				if(!"".equals(msg)){
					invalidRecord.setErrorCode(Constants.INVALID_RECORD_EXCEPTION);
					invalidRecord.setErrorMsg(msg);
				}else{
					invalidRecord.setErrorCode(error.getErrorCode().toString());
					invalidRecord.setErrorMsg(error.getErrorMsg());
				}
				invalidRecord.setInstitutionId((Long) authTokenMap.get("InstitutionId"));
				invalidRecord.setOperType(Constants.INVALID_RECORD_INTERACT_WEIBO);
				invalidRecord.setRecordId(weibo.getPid());
				dealInvalidRecord(invalidRecord);
				interactWeibo.setStatus(3);
				if(error != null && Constants.WEIBONOTEXISTENT.equals(error.getSrcCode())){//΢��������
					interactWeibo.setStatus(4);
					msg = "20001";
				}
				linksusInteractWeiboService.updateSendReplyStatus(interactWeibo);
				return invalidRecord.getErrorCode();
			}else{
				Map rsMap = (Map) JsonUtil.json2map(rsData, Map.class);
				LinksusInteractWeibo interactWeibo = new LinksusInteractWeibo();
				interactWeibo.setPid(weibo.getReplyId());
				interactWeibo.setContent(weibo.getContent());
				interactWeibo.setCommentId((Long) rsMap.get("id"));//���˷��ص�����id
				Date createTime = DateUtil.parse(rsMap.get("created_at").toString(), "EEE MMM dd HH:mm:ss zzz yyyy",
						Locale.US);
				interactWeibo.setInteractTime(Integer.parseInt(String.valueOf(createTime.getTime() / 1000)));
				interactWeibo.setSendTime(Integer.parseInt(String.valueOf(createTime.getTime() / 1000)));
				interactWeibo.setStatus(new Integer(2));
				linksusInteractWeiboService.updateSendReplySucc(interactWeibo);
				//�����û�sina�������۴���
				cache.addCurrHourWriteCountByUser(appId, Constants.LIMIT_SEND_COMMENT_PER_HOUR);
				return msg;
			}
		}else if(accountType == 2){//��Ѷ
			try{
				UrlEntity strurl = LoadConfig.getUrlEntity("TCreplyWeibo");
				Map paraMap = new HashMap();
				paraMap.put("format", "json");
				paraMap.put("access_token", token);
				paraMap.put("content", weibo.getContent());
				paraMap.put("clientip", Constants.TENCENT_CLIENT_IP);
				paraMap.put("reid", weibo.getWeiboId());
				paraMap.put("oauth_consumer_key", cache.getAppInfo(Constants.CACHE_TENCENT_APPINFO).getAppKey());
				paraMap.put("openid", appId);
				paraMap.put("oauth_version", "2.a");
				paraMap.put("compatibleflag", "32");
				rsData = HttpUtil.postRequest(strurl, paraMap);
			}catch (Exception e){
				LogUtil.saveException(logger, e);
				msg = LogUtil.getExceptionStackMsg(e);
				succFlag = false;
			}
			//�жϷ��ؽ�� �����쳣��Ϣ
			LinksusTaskErrorCode error = StringUtil.parseTencentErrorCode(rsData);
			if(!"".equals(msg) || error != null){//����ʧ��
				LinksusTaskInvalidRecord invalidRecord = new LinksusTaskInvalidRecord();
				if(!"".equals(msg)){
					invalidRecord.setErrorCode(Constants.INVALID_RECORD_EXCEPTION);
					invalidRecord.setErrorMsg(msg);
				}else{
					invalidRecord.setErrorCode(error.getErrorCode().toString());
					invalidRecord.setErrorMsg(error.getErrorMsg());
				}
				invalidRecord.setInstitutionId((Long) authTokenMap.get("InstitutionId"));
				invalidRecord.setOperType(Constants.INVALID_RECORD_INTERACT_WEIBO);
				invalidRecord.setRecordId(weibo.getPid());
				dealInvalidRecord(invalidRecord);
				msg = invalidRecord.getErrorCode();

				LinksusInteractWeibo interactWeibo = new LinksusInteractWeibo();
				interactWeibo.setPid(weibo.getReplyId());
				interactWeibo.setStatus(3);
				if(error != null && Constants.TCWEIBONOTEXISTENT.contains(error.getSrcCode().toString())){
					interactWeibo.setStatus(4);
					msg = "20001";
				}
				linksusInteractWeiboService.updateSendReplyStatus(interactWeibo);
				return msg;
			}
			Map rsMap = (Map) JsonUtil.json2map(rsData, Map.class);
			Map dataMap = (Map) rsMap.get("data");
			LinksusInteractWeibo interactWeibo = new LinksusInteractWeibo();
			interactWeibo.setPid(weibo.getReplyId());
			interactWeibo.setInteractTime((Integer) dataMap.get("time"));
			interactWeibo.setSendTime((Integer) dataMap.get("time"));
			interactWeibo.setStatus(new Integer(2));
			//��������id
			interactWeibo.setCommentId(new Long((String) dataMap.get("id")));
			linksusInteractWeiboService.updateSendReplySucc(interactWeibo);
			return msg;
		}
		return msg;
	}
}
