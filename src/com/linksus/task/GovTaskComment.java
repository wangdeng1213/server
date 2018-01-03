package com.linksus.task;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.linksus.common.Constants;
import com.linksus.common.config.LoadConfig;
import com.linksus.common.util.ContextUtil;
import com.linksus.common.util.DateUtil;
import com.linksus.common.util.HttpUtil;
import com.linksus.common.util.JsonUtil;
import com.linksus.common.util.LogUtil;
import com.linksus.common.util.PrimaryKeyGen;
import com.linksus.common.util.QuartzUtil;
import com.linksus.common.util.StringUtil;
import com.linksus.entity.LinksusAppaccount;
import com.linksus.entity.LinksusGovDocument;
import com.linksus.entity.LinksusGovInteract;
import com.linksus.entity.LinksusGovMeger;
import com.linksus.entity.LinksusGovMessage;
import com.linksus.entity.LinksusInteractReplyCount;
import com.linksus.entity.LinksusTaskErrorCode;
import com.linksus.entity.UrlEntity;
import com.linksus.service.LinksusAppaccountService;
import com.linksus.service.LinksusGovDocumentService;
import com.linksus.service.LinksusGovInteractService;
import com.linksus.service.LinksusGovMegerService;
import com.linksus.service.LinksusGovMessageService;
import com.linksus.service.LinksusInteractMessageService;
import com.linksus.service.LinksusInteractReplyCountService;
import com.linksus.service.LinksusRelationWeibouserService;
import com.linksus.service.LinksusRemoveEventService;

/** ����滥��������������ع��ܵĽӿ�* */
public class GovTaskComment extends BaseTask{

	private LinksusGovInteractService linksusGovInteractService = (LinksusGovInteractService) ContextUtil
			.getBean("linksusGovInteractService");
	LinksusInteractMessageService linksusInteractMessageService = (LinksusInteractMessageService) ContextUtil
			.getBean("linksusInteractMessageService");
	LinksusGovMessageService linksusGovMessageService = (LinksusGovMessageService) ContextUtil
			.getBean("linksusGovMessageService");
	LinksusRemoveEventService removeService = (LinksusRemoveEventService) ContextUtil
			.getBean("linksusRemoveEventService");
	LinksusAppaccountService linksusAppaccountService = (LinksusAppaccountService) ContextUtil
			.getBean("linksusAppaccountService");
	LinksusRelationWeibouserService linksusRelationWeibouserService = (LinksusRelationWeibouserService) ContextUtil
			.getBean("linksusRelationWeibouserService");
	LinksusGovMegerService linksusGovMegerService = (LinksusGovMegerService) ContextUtil
			.getBean("linksusGovMegerService");
	LinksusInteractReplyCountService linksusInteractReplyCountService = (LinksusInteractReplyCountService) ContextUtil
			.getBean("linksusInteractReplyCountService");
	LinksusGovDocumentService linksusGovDocumentService = (LinksusGovDocumentService) ContextUtil
			.getBean("linksusGovDocumentService");
	/**���ͷ�ʽ  ��ʱ/��ʱ */
	private String sendType;

	public void setSendType(String sendType){
		this.sendType = sendType;
	}

	@Override
	public void cal(){
		if("0".equals(sendType)){ //0������
			replyCommentImmediate();
		}else if("1".equals(sendType)){//1�Ƕ�ʱ
			replyCommentAtRegularTime();
		}
	}

	/**
	 * �����ظ�����
	 */
	public void replyCommentImmediate(){
		try{
			LinksusGovInteract govInteract = new LinksusGovInteract();
			int startCount = (currentPage - 1) * pageSize;
			govInteract.setPageSize(pageSize);
			govInteract.setStartCount(startCount);
			govInteract.setSendType(Integer.valueOf(sendType));
			//��ȡ�������������б�
			List<LinksusGovInteract> recordList = linksusGovInteractService.getLinksusGovInteractTaskList(govInteract);
			if(recordList != null && recordList.size() > 0){
				for(LinksusGovInteract weiboReply : recordList){
					sendWeiboReply(weiboReply);
				}
			}
			checkTaskListEnd(recordList);//�ж������Ƿ���ѯ���
		}catch (Exception e){
			LogUtil.saveException(logger, e);
			e.printStackTrace();
		}
	}

	/**
	 * ��ʱ�ظ�
	 */
	private void replyCommentAtRegularTime(){
		LinksusGovInteract linksusWeibo = new LinksusGovInteract();
		int startCount = (currentPage - 1) * pageSize;
		linksusWeibo.setPageSize(pageSize);
		linksusWeibo.setStartCount(startCount);
		linksusWeibo.setSendType(Integer.valueOf(sendType));
		List<LinksusGovInteract> recordList = linksusGovInteractService.getLinksusGovInteractTaskList(linksusWeibo);
		if(recordList != null && recordList.size() > 0){
			for(LinksusGovInteract record : recordList){
				addWeiboTimer(record);
			}
		}
		checkTaskListEnd(recordList);//�ж������Ƿ���ѯ���
	}

	/**
	 * ֱ��΢����ʱ����
	 * 
	 * @param record
	 */
	private void addWeiboTimer(LinksusGovInteract record){
		if(Constants.STATUS_DELETE.equals(record.getStatus() + "")
				|| Constants.STATUS_PAUSE.equals(record.getStatus() + "")){
			QuartzUtil.removeJob("RECORD_" + record.getRunId().toString(), Constants.RECORD_GROUP_NAME);
		}else{
			Map dataMap = new HashMap();
			dataMap.put("pid", record.getRunId());
			Date sendTime = new Date(Long.parseLong(record.getSendTime().toString()) * 1000);
			QuartzUtil.addSimpleJob("RECORD_" + record.getRunId().toString(), Constants.RECORD_GROUP_NAME, dataMap,
					sendTime, SendReplyTimer.class);
		}
	}

	/**
	 * �ظ�˽��
	 * 
	 * sendType 
	 * msgType
	 * content
	 * articles
	 * accountId
	 */
	public String publishMsg(Map map) throws Exception{
		String rsData = "";
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
		if(appcount == null){//�����û�������
			return "10130";
		}
		map.put("institution_id", appcount.getInstitutionId());
		map.put("access_token", appcount.getToken());
		map.put("uid", appcount.getAppid());
		map.put("accountType", appcount.getType().toString());
		//map.put("openid", appcount.getAppid());
		map.put("userid", appcount.getUserId() + "");
		map.put("username", appcount.getAccountName());
		rsData = publishWeiboMsg(map);
		return rsData;
	}

	/**
	 * �������� 
	 * 
	 * @param map   ���������������
	 *  replyId 			��Ҫ�ظ��Ļ�����ϢID
	 *  mid     			΢��id
	 *  content 			��������
	 *  accountId 			ƽ̨�˺�id
	 *  instPersonId		��ά��ԱID
	 *  commonReplyIds 		���ûظ�pid �����ŷָ�
	 *  sendType          	�������� 0/1(��ʱ����/��ʱ����)
	 *  sendTime			��ʱ����ʱ��int��
	 * @return 
	 */
	public String publishComment(Map map) throws Exception{
		String rsData = "";

		//����������ͬʱΪ��
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
		int rs = 0;
		try{
			LinksusAppaccount appcount = linksusAppaccountService.getLinksusAppaccountAndUserIdById((Long) map
					.get("accountId"));
			if(appcount == null){//�����û�������
				return "10130";
			}
			map.put("institution_id", appcount.getInstitutionId());
			map.put("access_token", appcount.getToken());
			map.put("uid", appcount.getAppid());
			map.put("accountType", appcount.getType().toString());
			map.put("openid", appcount.getAppid());
			map.put("userid", appcount.getUserId() + "");
			rs = this.publishWeiboComment(map);
			//	replyCommentCount(map);// ͳ�Ƴ��ûظ�ʹ������ ��ʱֻ��΢���ṩ���ûظ�ͳ��
		}catch (Exception e){
			LogUtil.saveException(logger, e);
			e.printStackTrace();
		}
		if(rs == 1000){
			rsData = "20018";
		}
		return rsData;
	}

	/**
	 * ����΢�����۵�����
	 * 
	 *  replyId 			��Ҫ�ظ��Ļ�����ϢID
	 *  mid     			΢��id
	 *  content 			��������
	 *  accountId 			ƽ̨�˺�id
	 *  commonReplyIds 		���ûظ�pid �����ŷָ�
	 *  sendType          	�������� 0/1(��ʱ����/��ʱ����)
	 *  sendTime			��ʱ����ʱ��int��
	 * @return
	 */
	public int publishWeiboComment(Map map) throws Exception{
		Long replyId = (Long) map.get("replyId");
		Long mid = (Long) map.get("mid");
		String content = (String) map.get("content");
		Integer sendType = (Integer) map.get("sendType");
		boolean isFlag = false;
		int rs = 0;
		String result = "";
		try{
			Long recordId;
			LinksusGovInteract linksusInteractWeibo = null;
			LinksusGovInteract entity = new LinksusGovInteract();
			Long interactId = PrimaryKeyGen.getPrimaryKey("linksus_gov_interact", "interact_id");
			entity.setInteractId(interactId);
			Long runId = PrimaryKeyGen.GenerateSerialNum();
			if(replyId == null && mid != null){//����΢����������ʱ,���뻥����¼������--α����
				entity.setAccountId((Long) map.get("accountId"));
				entity.setUserId(Long.parseLong((String) map.get("userid")));
				entity.setWeiboId((Long) map.get("mid"));
				entity.setSourceWeiboId(new Long(0));
				entity.setCommentId(new Long(0));
				isFlag = true;
			}else{
				linksusInteractWeibo = linksusGovInteractService.getLinksusGovInteractById((Long) map.get("replyId"));
				if(linksusInteractWeibo != null){
					entity.setAccountId(linksusInteractWeibo.getAccountId());
					entity.setUserId(linksusInteractWeibo.getUserId());
					entity.setWeiboId(linksusInteractWeibo.getWeiboId());
					entity.setSourceWeiboId(linksusInteractWeibo.getSourceWeiboId());
					entity.setCommentId(linksusInteractWeibo.getCommentId());
					isFlag = true;
				}else{
					rs = 1000;
				}
			}
			entity.setRunId(runId);
			entity.setContent(content);
			entity.setInteractType(7);
			entity.setSendType(sendType);
			entity.setStatus(1);//������
			entity.setInteractTime(Integer.valueOf(String.valueOf(new Date().getTime() / 1000)));
			if(sendType.intValue() == 1){//Ϊ��ʱ����ʱ���ö�ʱ����ʱ��
				entity.setSendTime((Integer) map.get("sendTime"));
			}else{
				entity.setSendTime(new Integer(0));
			}
			if(isFlag){
				rs = linksusGovInteractService.insertLinksusGovInteract(entity);
				entity.setRunId(runId);
				sendImmediate(entity);
			}
		}catch (Exception e){
			LogUtil.saveException(logger, e);
			e.printStackTrace();
		}
		return rs;
	}

	/**
	 * ����˽������
	 * 
	 * @param id,account_id,comment,pids,send_type,inst_person_id
	 * @param event
	 * @return
	 */
	public String publishWeiboMsg(Map map) throws Exception{
		GovTaskSendSinaLetter taskSendSinaLetter = new GovTaskSendSinaLetter();
		Long id = Long.parseLong(map.get("replyId").toString());
		Integer msgType = (Integer) map.get("msgType");
		Long account_id = Long.parseLong(map.get("accountId").toString());
		Long instPersonId = (Long) map.get("instPersonId");
		// ΢��˽�ŵ�����
		String content = (String) map.get("content") == null ? "" : (String) map.get("content");
		String articles = (String) map.get("articles") == null ? "" : (String) map.get("articles");
		String img = "";
		String imgName = "";
		if(StringUtils.isNotBlank(articles)){
			img = JsonUtil.getNodeValueByName(articles, "image");
			imgName = JsonUtil.getNodeValueByName(articles, "display_name");
		}
		String result = "";
		try{

			LinksusGovMessage linksusGovMessage = linksusGovMessageService.getLinksusGovMessageByMsgId(id);
			if(linksusGovMessage != null){
				LinksusGovMessage entity = new LinksusGovMessage();
				Long pid = PrimaryKeyGen.getPrimaryKey("linksus_gov_message", "pid");
				Long runId = PrimaryKeyGen.GenerateSerialNum();
				entity.setPid(pid);
				entity.setRunId(runId);
				entity.setAccountId(linksusGovMessage.getAccountId());
				entity.setMsgId(linksusGovMessage.getMsgId());
				entity.setInteractType(2);
				entity.setContent(content);
				entity.setImg(img);
				entity.setImgName(imgName);
				entity.setImgThumb("");
				entity.setAttatch("");
				entity.setAttatchName("");
				entity.setSendType(0);
				entity.setUserId(linksusGovMessage.getUserId());

				entity.setReplyMsgId(linksusGovMessage.getRunId());
				entity.setStatus(1);// 1.δ����
				entity.setSendTime(0);
				entity.setInteractTime(DateUtil.getUnixDate(new Date()));
				linksusGovMessageService.insertLinksusGovMessage(entity);
				entity.setLastInteractTime(linksusGovMessage.getInteractTime());//�ϴλ���ʱ��
				entity.setReplyId(linksusGovMessage.getPid());
				entity.setMsgType(msgType);
				result = taskSendSinaLetter.sendMsgInterface(entity);
			}else{
				return "10203";
			}
		}catch (Exception e){
			LogUtil.saveException(logger, e);
			return "20028";
		}
		return result;
	}

	/**
	 * ����mid��ѯ�����б�
	 * 
	 * @param id,account_type
	 * @return
	 */
	public Map getInteractGovListByMid(Map paramMap) throws Exception{
		Map map = new HashMap();
		map.put("weibo_id", paramMap.get("mid"));
		map.put("account_type", paramMap.get("type"));
		Integer page = (Integer) paramMap.get("page");
		Integer pageSize = (Integer) paramMap.get("pageSize");
		Integer startCount = (page - 1) * pageSize;
		map.put("startCount", startCount);
		map.put("pageSize", new Integer(pageSize));
		List list = linksusGovInteractService.getInteractGovListByMid(map);
		map.clear();
		map.put("data", list);
		return map;
	}

	//΢�������ط�
	public String againReplyComment(Map map) throws Exception{
		String rs = "";
		Long replyId = (Long) map.get("replyId");
		LinksusGovInteract linksusGovInteract = linksusGovInteractService.getLinksusGovInteractById(replyId);
		if(linksusGovInteract != null){
			if(linksusGovInteract.getStatus() == 3 || linksusGovInteract.getStatus() == 1){
				sendImmediate(linksusGovInteract);
			}else{
				return "20033";
			}
		}else{
			return "20018";
		}
		return rs;
	}

	public void sendImmediate(LinksusGovInteract entity) throws Exception{
		String rsMsg = null;
		if(entity.getStatus() == 3 || entity.getStatus() == 1){
			//if(entity.getSendType() == 0){//��ʱ����
			rsMsg = sendWeiboReply(entity);
			//}
		}else{
			//����״̬��Ϊ������	
			rsMsg = "20014";
		}
	}

	/**
	 * �����ظ�
	 * @param record
	 */
	public String sendWeiboReply(LinksusGovInteract weibo) throws Exception{
		//ȡ���û���Ȩ
		String msg = "";
		Map tokenMap = getAccountTokenMap(weibo.getAccountId());
		if(tokenMap != null && tokenMap.size() > 0){
			//���Ҫ�ظ������������������������Ӱ����İ��ж�ȡĬ�ϻظ�  
			sendSubWeiboReply(weibo);
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
			return "10130";
		}
		return msg;
	}

	/**
	 *  �ظ�����
	 * @param record
	 * @param token
	 * @return
	 */
	public String publishReply(LinksusGovInteract weibo,Map authTokenMap) throws Exception{
		String msg = "";
		String rsData = "";
		boolean succFlag = true;
		String token = (String) authTokenMap.get("token");
		String appId = (String) authTokenMap.get("openid");
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
			paraMap.put("without_mention", 0);
			paraMap.put("comment_ori", 0);
			rsData = HttpUtil.postRequest(strReply, paraMap);
		}catch (Exception e){
			LogUtil.saveException(logger, e);
			succFlag = false;
		}
		//�жϷ��ؽ�� �����쳣��Ϣ
		LinksusTaskErrorCode error = StringUtil.parseSinaErrorCode(rsData);
		if(!succFlag || error != null){//����ʧ��
			LinksusGovInteract interactWeibo = new LinksusGovInteract();
			//Long interactId = PrimaryKeyGen.getPrimaryKey("linksus_gov_interact", "interact_id");
			interactWeibo.setInteractId(weibo.getInteractId());
			interactWeibo.setStatus(3);
			linksusGovInteractService.updateSendReplyStatus(interactWeibo);
			//�ظ����۷���������
			return error.getErrorCode() + "";
		}else{
			Map rsMap = (Map) JsonUtil.json2map(rsData, Map.class);
			//���»�����Ϣ��
			LinksusGovInteract interactWeibo = new LinksusGovInteract();
			interactWeibo.setInteractId(weibo.getInteractId());
			interactWeibo.setCommentId((Long) rsMap.get("id"));//���˷��ص�����id
			Date createTime = DateUtil.parse(rsMap.get("created_at").toString(), "EEE MMM dd HH:mm:ss zzz yyyy",
					Locale.US);
			interactWeibo.setInteractTime(Integer.parseInt(String.valueOf(createTime.getTime() / 1000)));
			interactWeibo.setSendTime(Integer.parseInt(String.valueOf(createTime.getTime() / 1000)));
			interactWeibo.setStatus(new Integer(2));
			linksusGovInteractService.updateSendReplySucc(interactWeibo);
			//�����û�sina�������۴���
			cache.addCurrHourWriteCountByUser(appId, Constants.LIMIT_SEND_COMMENT_PER_HOUR);
			return msg;
		}
	}

	//����accountId��ȡ�˻�token
	public Map getAccountTokenMap(Long accountId){
		Map tokenMap = new HashMap();
		//��ȡ���������û�token
		LinksusAppaccount tokenObj = linksusAppaccountService.getGovLinksusAppaccountTokenById(accountId);
		if(tokenObj != null){
			tokenMap.put("token", tokenObj.getToken());
			tokenMap.put("openid", tokenObj.getAppid());
			tokenMap.put("type", accountType);
			tokenMap.put("appkey", tokenObj.getAppKey());
			tokenMap.put("InstitutionId", tokenObj.getInstitutionId());
		}
		return tokenMap;
	}

	/**
	 * ������ظ�
	 * 
	 * 1���ж��Ƿ����������
	 * 2���������,����΢�����в������������
	 * @param weibo
	 */
	private void sendSubWeiboReply(LinksusGovInteract weibo) throws Exception{
		List<LinksusGovMeger> megerList = linksusGovMegerService.getLinksusGovMegerListByFather(weibo.getRunId());
		if(megerList != null && megerList.size() > 0){
			//��ȡ�İ�ģ������
			String templateContent = " û��Ĭ���İ���";
			List<LinksusGovDocument> docList = linksusGovDocumentService.getLinksusGovDocumentListByAccount(weibo
					.getAccountId());
			if(docList != null && docList.size() > 0){
				templateContent = docList.get(0).getDocumentContent();
			}
			for(Iterator<LinksusGovMeger> iter = megerList.iterator(); iter.hasNext();){
				LinksusGovMeger meger = iter.next();
				LinksusGovInteract oriweibo = linksusGovInteractService.getLinksusGovInteractById(new Long(meger
						.getRunId()));
				//���ɴ������ظ���¼
				if(oriweibo != null){
					LinksusGovInteract govInteract = new LinksusGovInteract();
					Long interactId = PrimaryKeyGen.getPrimaryKey("linksus_gov_interact", "interact_id");
					govInteract.setInteractId(interactId);
					govInteract.setRunId(PrimaryKeyGen.GenerateSerialNum());
					govInteract.setContent(templateContent);
					govInteract.setInteractType(7);
					govInteract.setSendType(1);
					govInteract.setStatus(1);//������
					govInteract.setInteractTime(Integer.valueOf(String.valueOf(new Date().getTime() / 1000)));
					govInteract.setAccountId(oriweibo.getAccountId());
					govInteract.setUserId(oriweibo.getUserId());
					govInteract.setWeiboId(oriweibo.getWeiboId());
					govInteract.setSourceWeiboId(oriweibo.getSourceWeiboId());
					govInteract.setCommentId(oriweibo.getCommentId());
					linksusGovInteractService.insertLinksusGovInteract(govInteract);
				}
			}
		}
	}

	public String publishWeibo(LinksusGovInteract weibo,Map authTokenMap){
		String msg = "";
		String rsData = "";
		String token = (String) authTokenMap.get("token");
		String appId = (String) authTokenMap.get("openid");

		int hcount = cache.getCurrHourWriteCountByUser(appId, Constants.LIMIT_SEND_COMMENT_PER_HOUR);
		if(hcount == -1){//��������
			return "�Ѵﵽ��������";
		}
		String content = weibo.getContent();
		UrlEntity strReplyWeibo = LoadConfig.getUrlEntity("replyWeibo");
		Map paraMap = new HashMap();
		paraMap.put("access_token", token);
		paraMap.put("id", weibo.getWeiboId());
		paraMap.put("comment", content);
		paraMap.put("comment_ori", 0);
		rsData = HttpUtil.postRequest(strReplyWeibo, paraMap);
		//�жϷ��ؽ�� �����쳣��Ϣ
		logger.info(">>>>>>>>>>>>>>rsData>>>>>>>>>>>>" + rsData);
		LinksusTaskErrorCode error = StringUtil.parseSinaErrorCode(rsData);
		if(error != null){//����ʧ��
			LinksusGovInteract interactWeibo = new LinksusGovInteract();
			interactWeibo.setInteractId(weibo.getInteractId());
			interactWeibo.setStatus(3);
			linksusGovInteractService.updateSendReplyStatus(interactWeibo);
			msg = error.getErrorCode() + "";
		}else{
			Map rsMap = (Map) JsonUtil.json2map(rsData, Map.class);
			LinksusGovInteract interactWeibo = new LinksusGovInteract();
			interactWeibo.setInteractId(weibo.getInteractId());
			interactWeibo.setCommentId((Long) rsMap.get("id"));//���˷��ص�����id
			Date createTime = DateUtil.parse(rsMap.get("created_at").toString(), "EEE MMM dd HH:mm:ss zzz yyyy",
					Locale.US);
			interactWeibo.setInteractTime(Integer.parseInt(String.valueOf(createTime.getTime() / 1000)));
			interactWeibo.setSendTime(Integer.parseInt(String.valueOf(createTime.getTime() / 1000)));
			interactWeibo.setStatus(new Integer(2));
			linksusGovInteractService.updateSendReplySucc(interactWeibo);
			//�����û�sina�������۴���
			cache.addCurrHourWriteCountByUser(appId, Constants.LIMIT_SEND_COMMENT_PER_HOUR);
			return msg;
		}
		return msg;
	}

	/**
	 * �ظ�����ͳ��
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int replyCommentCount(Map map) throws Exception{
		String rsData = "";
		boolean hasError = false;
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

			Long pid = PrimaryKeyGen.getPrimaryKey("linksus_interact_reply_count", "pid");
			for(String reply_id : rsMap.keySet()){
				paraMap.put("reply_id", reply_id);
				rsbean = linksusInteractReplyCountService.getLinksusInteractReplyCountByMap(paraMap);
				if(null == rsbean){// ������������
					pid = PrimaryKeyGen.getPrimaryKey("linksus_interact_reply_count", "pid");
					entity.setPid(pid);
					entity.setReplyId(Long.parseLong(reply_id));
					entity.setInstitutionId(institution_id);
					entity.setUseCount(rsMap.get(reply_id));
					entity.setLastUseTime(lastUseTime);
					rs = linksusInteractReplyCountService.insertLinksusInteractReplyCount(entity);
					count += 1;
				}else{// ���������
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

	// ���㳣�ûظ�id����
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
	 * �ظ�����
	 * @param paramsMap
	 *      run_id
	 *      id
	 *      institution_id
	 * @return
	 */
	public String replyPingLun(Map paramsMap) throws Exception{
		Long interactId = (Long) paramsMap.get("interactId");
		String content = (String) paramsMap.get("content");
		LinksusGovInteract govInteract = saveGovInteract(interactId, content);
		if(govInteract != null){
			return sendWeiboReply(govInteract);
		}else{
			return "10203";
		}
	}

	/**
	 * �������ۻظ���Ϣ
	 * @param paramsMap
	 */
	private LinksusGovInteract saveGovInteract(Long interactId,String content) throws Exception{
		//��ѯ������Ϣ
		LinksusGovInteract oldLinksusGovInteract = linksusGovInteractService.getLinksusGovInteractById(interactId);
		LinksusGovInteract newLinksusGovInteract = null;
		if(oldLinksusGovInteract != null){
			newLinksusGovInteract = new LinksusGovInteract();
			Long newInteractId = PrimaryKeyGen.getPrimaryKey("linksus_gov_interact", "interact_id");
			newLinksusGovInteract.setInteractId(newInteractId);
			newLinksusGovInteract.setRunId(oldLinksusGovInteract.getRunId());
			newLinksusGovInteract.setUserId(oldLinksusGovInteract.getUserId());
			newLinksusGovInteract.setAccountId(oldLinksusGovInteract.getAccountId());
			newLinksusGovInteract.setCommentId(oldLinksusGovInteract.getCommentId());
			newLinksusGovInteract.setWeiboId(oldLinksusGovInteract.getWeiboId());
			newLinksusGovInteract.setSourceWeiboId(oldLinksusGovInteract.getSourceWeiboId());
			newLinksusGovInteract.setContent(content);
			newLinksusGovInteract.setInteractType(new Integer(7));
			newLinksusGovInteract.setSendType(0);
			newLinksusGovInteract.setStatus(3);
			newLinksusGovInteract.setSendTime(0);
			newLinksusGovInteract.setReplyCommentId(0l);
			newLinksusGovInteract.setInteractTime(Integer.valueOf(String.valueOf(new Date().getTime() / 1000)));
			linksusGovInteractService.insertLinksusGovInteract(newLinksusGovInteract);
		}
		return newLinksusGovInteract;
	}

	/** 
	 *  �ظ����۲���
	 */
	public void testReplyPingLun() throws Exception{
		//8791742130	20581	10000	3685860740950419	3685814422476806	0	ͬ�Ӱ��С�������	1	0	0	0	0	1394261143
		LinksusGovInteract interact = new LinksusGovInteract();
		interact.setRunId(8791742130L);
		interact.setUserId(20581L);
		interact.setAccountId(10000L);
		interact.setCommentId(3685860740950419L);
		interact.setWeiboId(3685814422476806L);
		interact.setContent("�����㣿����");
		interact.setInteractTime(1394261143);

		Map authTokenMap = new HashMap();
		authTokenMap.put("token", "2.00NaVf3BeE2s5B2f1113d612012bdT");
		authTokenMap.put("openid", "1251105081");
		publishReply(interact, authTokenMap);
	}

	public void testReplyLetter(){
		Map map = new HashMap();
		//0��ʱ����
		map.put("sendType", 0);
		map.put("msgType", 1);
		map.put("content", "����˽�Żظ�");
		map.put("accountId", 1041L);
		map.put("replyId", 7987631024L);
		try{
			publishMsg(map);
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	public static void main(String[] args){
		GovTaskComment weiboComment = new GovTaskComment();
		//		Map paramMap = new HashMap();
		//		paramMap.put("institution_id", 10000);
		//		// map.put("material_id",2);
		//		paramMap.put("material_type", 3);
		//
		//		paramMap.put("id", 1233);// ������ϢID
		//		paramMap.put("account_id", 111);
		//		paramMap.put("content", "FUCK U2SX");
		//		paramMap.put("pids", "1,2,3");
		//		paramMap.put("send_type", 1);
		//		paramMap.put("inst_person_id", 10010);
		//		paramMap.put("type", 1);// ������Ϣ���� 1 ΢������ 2˽�� 3΢��
		//
		//		// start 2˽��
		//		paramMap.put("img", "img1");
		//		paramMap.put("img_name", "img_name1");
		//		paramMap.put("img_thumb", "img_thumb1");
		//		paramMap.put("attatch", "attatch1");
		//		paramMap.put("attatch_name", "attatch_name1");
		//		// end 2˽��
		//		// start 3΢��
		//		paramMap.put("msg_type", 2);
		//		// paramMap.put("material_id", "material_id");
		//		String articles = "[{\"title\":\"title\",\"autherName\":\"autherName\",\"summary\":\"summary\",\"picOriginalUrl\":\"picOriginalUrl\",\"picMiddleUrl\":\"picMiddleUrl\",\"picThumbUrl\":\"picThumbUrl\",\"content\":\"content\",\"conentUrl\":\"conentUrl\"},{\"title\":\"title\",\"autherName\":\"autherName\",\"summary\":\"summary\",\"picOriginalUrl\":\"picOriginalUrl\",\"picMiddleUrl\":\"picMiddleUrl\",\"picThumbUrl\":\"picThumbUrl\",\"content\":\"content\",\"conentUrl\":\"123\"}]";
		//		paramMap.put("content", "NIHAOι���");
		//		paramMap.put("articles", articles);
		//		// start 3΢��
		//		paramMap.put("id", "3678678980446378");
		//		paramMap.put("switcher", "1");
		//
		//		// paramMap.put("account_id", 1);
		//		try {
		//			weiboComment.publishComment(paramMap);
		//		} catch (Exception e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		}
		weiboComment.testReplyLetter();
	}
}
