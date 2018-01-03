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

/** ����������������ع��ܵĽӿ�* */
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
	 * ����IDɾ��΢��������,�ӿ�ʹ��,�ȵ��õ������ӿ�,���ݷ��سɹ�״̬������ƽ̨�����������ɾ��
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
		String switcher = map.get("switcher").toString();// ϵͳ����
		Map paramMap = new HashMap();
		String errorCode = "";
		LinksusAppaccount appcount = linksusAppaccountService.getLinksusAppaccountById(account_id);

		uid = appcount.getAppid().toString();
		accountType = appcount.getType().toString();
		access_token = appcount.getToken().toString();
		map.put("account_type", accountType);
		LinksusTaskErrorCode error = null;
		if("1".equals(switcher)){// ������ƽ̨���ۿ��ؿ���
			if("1".equals(accountType)){// ����
				UrlEntity url = LoadConfig.getUrlEntity("deletesinaWeiboComment");
				paramMap.put("uid", uid);
				paramMap.put("access_token", access_token);
				paramMap.put("cid", id);
				rsData = HttpUtil.postRequest(url, paramMap);
				error = StringUtil.parseSinaErrorCode(rsData);
				if(error != null){// ɾ������
					hasError = true;
				}
			}else if("2".equals(accountType)){// ��Ѷ΢������ɾ����ʱδ�ṩ�ӿ�

			}
			if(hasError){// ˵��ɾ������ʱ����
				// �޸�ִ��״̬
				rsData = error.getErrorCode().toString();
			}else{// �޸�ִ��״̬
				this.removeCommentByMap(map);// ��������������ɾ���ɹ���ɾ��ƽ̨��������
				rsData = "";
			}
		}else{// ֻɾ��ƽ̨����
			this.removeCommentByMap(map);// ��������������ɾ���ɹ���ɾ��ƽ̨��������
		}
		// �޸�ִ��״̬
		/*
		 * updtEvent.setId(id); updtEvent.setStatus(1);
		 * updtEvent.setOperateTime(DateUtil.getUnixDate(new Date()));
		 * removeService.updateRemoveEventStatus(updtEvent);
		 */
		return rsData;
	}

	/**
	 * ����IDɾ��΢��������,���ݷ��سɹ�״̬������ƽ̨�����������ɾ��
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
	 * �ظ�˽��
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
		if(appcount == null){//�����û�������
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
		replyCommentCount(map);// ͳ�Ƴ��ûظ�ʹ������
		return rsData;
	}

	/**
	 * ����΢��
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
		if(appcount == null){//�����û�������
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
	 * �������� 
	 * @param map   ���������������
	 *  replyId 			��Ҫ�ظ��Ļ�����ϢID
	 *  mid     			΢��id
	 *  content 			��������
	 *  accountId 			ƽ̨�˺�id
	 *  instPersonId		��ά��ԱID
	 *  commonReplyIds 		���ûظ�pid �����ŷָ�
	 *  sendType          	�������� 0/1(��ʱ����/��ʱ����)
	 *  sendTime			��ʱ����ʱ��int��
	 *  
	 *  
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
		rsData = this.publishWeiboComment(map);
		replyCommentCount(map);// ͳ�Ƴ��ûظ�ʹ������
		return rsData;
	}

	/**
	 * ����΢�����۵�����
	 * 
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
		if(replyId == null && mid != null){//����΢����������ʱ,���뻥����¼������--α����
			LinksusInteractRecord inksusInteractRecord = new LinksusInteractRecord();
			recordId = PrimaryKeyGen.getPrimaryKey("linksus_interact_record", "record_id");
			inksusInteractRecord.setRecordId(recordId);
			//��ѯƽ̨�˻���userId
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
			entity.setStatus(1);// 1.δ����
			entity.setSendTime((Integer) map.get("sendTime"));//Ϊ��ʱ����ʱ���ö�ʱ����ʱ��	
		}else{
			entity.setStatus(3);// 3.����ʧ��
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
	 * ����˽������
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
		LinksusInteractMessage linksusInteractMessage = linksusInteractMessageService.getLinksusInteractMessageById(id);
		if(linksusInteractMessage == null){
			return "20018";
		}
		//�˴��ж��Ƿ񳬳��ظ�����
		if(linksusInteractMessage.getMsgType() != 5 && linksusInteractMessage.getReplyCount() >= 1){//�����ظ�����			
			return "20009";
		}else if(linksusInteractMessage.getMsgType() == 5 && linksusInteractMessage.getReplyCount() >= 3){
			return "20010";
		}
		Integer lastInteractTime = linksusInteractMessage.getInteractTime();
		Date currDate = new Date();
		if(currDate.getTime() - lastInteractTime * 1000L > 72 * 3600 * 1000L){//����72Сʱ ���ܻظ�
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
				entity.setStatus(1);// 1.δ����
			}else{
				entity.setStatus(3);// 3.����ʧ��
			}
			entity.setSendTime(sentTime);
			entity.setInstPersonId(instPersonId);
			entity.setInteractTime(DateUtil.getUnixDate(new Date()));
			linksusInteractMessageService.insertLinksusInteractMessage(entity);
			//QueueDataSave.addDataToQueue(entity, "insert");
			entity.setUserName((String) map.get("username"));
			entity.setLastInteractTime(linksusInteractMessage.getInteractTime());//�ϴλ���ʱ��
			entity.setReplyCount(linksusInteractMessage.getReplyCount());//�ظ�����
			entity.setMsgType(linksusInteractMessage.getMsgType());//��Ϣ����
			entity.setReplyId(linksusInteractMessage.getMsgId());//�ظ�˽��ID
			result = taskSendSinaLetter.sendMsgInterface(entity);
		}else{
			return "10203";
		}
		return result;
	}

	/**
	 * ����΢����Ϣ
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
				// �жϻظ�ʱ��
				Integer lnteractTime = linksusInteractWeixin.getInteractTime();

				if(sendType == 0){// ��ʱ����
					Date currDate = new Date();
					if(currDate.getTime() - lnteractTime * 1000L > 48 * 3600 * 1000L){// ����48Сʱ
						// ���ܻظ�
						logger.error("΢��{}�ѳ���48Сʱ,���ܱ��ظ�", linksusInteractWeixin.getPid());
						return "20029";
					}
				}else if(sendType == 1){// ��ʱ����
					send_time = Integer.parseInt(map.get("send_time").toString());
					if(send_time * 1000L - lnteractTime * 1000L > 48 * 3600 * 1000L){// ����48Сʱ
						// ���ܻظ�
						logger.error("΢��{}�ѳ���48Сʱ,���ܱ��ظ�", linksusInteractWeixin.getPid());
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
					entity.setStatus(1);// 1.δ����
				}else{
					entity.setStatus(3);// 3.����ʧ��
				}
				entity.setInteractTime(lnteractTime);
				entity.setSendTime(send_time);
				linksusInteractWeixinService.insertLinksusInteractWeixin(entity);
				// �ظ�΢��
				TaskReplyWeixins replyWeixins = new TaskReplyWeixins();
				restr = replyWeixins.sendReplyWeixin(entity);
				if(StringUtils.isNotBlank(restr) && restr.contains("#�ظ�΢�ŷ����ɹ�")){
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
	 * ����΢����Ϣ�ز�
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
			//����΢���زı�
			linksusInteractWxMaterialService.insertLinksusInteractWxMaterial(entity);
			map.put("material_id", material_id);
			publishWeixinArticle(map);
		}catch (Exception e){
			e.printStackTrace();
		}
		return material_id;
	}

	/**
	 * ����΢����Ϣͼ����Ϣ
	 * 
	 * @param id,account_id,comment,pids,send_type,inst_person_id
	 * @param event
	 * @return
	 */
	public String publishWeixinArticle(Map map) throws Exception{
		Long material_id = Long.parseLong(map.get("material_id").toString());
		Long pid = null;

		// title varchar(100) not null default '' comment '����',
		// auther_name varchar(30) not null default '' comment '����',
		// summary varchar(150) not null default '' comment 'ժҪ',
		// pic_original_url varchar(100) not null default '' comment 'ԭͼurl',
		// pic_middle_url varchar(100) not null default '' comment '��ͼurl',
		// pic_thumb_url varchar(100) not null default '' comment '��΢ͼurl',
		// content varchar(2000) not null default '' comment '����',
		// conent_url varchar(100) not null default '' comment '���ݵ�url',
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
	 * ��������,�ӿ�ʹ��,�ȵ��õ������ӿ�,���ݷ��سɹ�״̬������ƽ̨�����������ɾ�� ��������
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
				if(null == rsbean){// ������������
					Long pid = PrimaryKeyGen.getPrimaryKey("linksus_interact_reply_count", "pid");
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
	 * ����mid��ѯ�����б�
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

	//΢�������ط�
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

	//΢���ط�
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
			// �жϻظ�ʱ��
			Integer lnteractTime = linksusInteractWeixin.getInteractTime();

			if(linksusInteractWeixin.getSendType() == 0){// ��ʱ����
				Date currDate = new Date();
				if(currDate.getTime() - lnteractTime * 1000L > 48 * 3600 * 1000L){// ����48Сʱ
					// ���ܻظ�
					linksusInteractWeixin.setStatus(4);
					linksusInteractWeixin.setInteractTime(currentTime);
					linksusInteractWeixinService.updateLinksusInteractWeixinStatus(linksusInteractWeixin);
					logger.error("΢�� {} �ѳ���48Сʱ,���ܱ��ظ�", linksusInteractWeixin.getPid());
					return "20029";
				}
			}else if(linksusInteractWeixin.getSendType() == 1){// ��ʱ����
				if(linksusInteractWeixin.getSendTime() * 1000L - lnteractTime * 1000L > 48 * 3600 * 1000L){// ����48Сʱ
					// ���ܻظ�
					linksusInteractWeixin.setStatus(4);
					linksusInteractWeixin.setInteractTime(currentTime);
					linksusInteractWeixinService.updateLinksusInteractWeixinStatus(linksusInteractWeixin);
					logger.error("΢��{},�ѳ���48Сʱ,���ܱ��ظ�", linksusInteractWeixin.getPid());
					return "20029";
				}
			}
			// �ظ�΢��
			TaskReplyWeixins replyWeixins = new TaskReplyWeixins();
			rs = replyWeixins.sendReplyWeixin(linksusInteractWeixin);
			if(StringUtils.isNotBlank(rs) && rs.contains("#�ظ�΢�ŷ����ɹ�")){
				rs = "";
			}else{
				rs = "20026";
			}
		}else{
			return "20018";
		}
		return rs;
	}

	//˽���ط�
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
		linksusInteractMessage.setLastInteractTime(linksusInteractMessage.getInteractTime());//�ϴλ���ʱ��
		linksusInteractMessage.setReplyCount(linksusInteractMessage.getReplyCount());//�ظ�����
		linksusInteractMessage.setMsgType(linksusInteractMessage.getMsgType());//��Ϣ����
		linksusInteractMessage.setReplyId(linksusInteractMessage.getMsgId());//�ظ�˽��ID
		rs = taskSendSinaLetter.sendMsgInterface(linksusInteractMessage);
		return rs;
	}

	public static void main(String[] args){
		TaskInterfaceComment weiboComment = new TaskInterfaceComment();
		Map paramMap = new HashMap();
		paramMap.put("institution_id", 1102);
		// map.put("material_id",2);
		paramMap.put("material_type", 3);

		paramMap.put("id", 1233);// ������ϢID
		paramMap.put("account_id", 111);
		paramMap.put("content", "FUCK U2SX");
		paramMap.put("pids", "1,2,3");
		paramMap.put("send_type", 1);
		paramMap.put("inst_person_id", 10010);
		paramMap.put("type", 3);// ������Ϣ���� 1 ΢������ 2˽�� 3΢��

		// start 2˽��
		paramMap.put("img", "img1");
		paramMap.put("img_name", "img_name1");
		paramMap.put("img_thumb", "img_thumb1");
		paramMap.put("attatch", "attatch1");
		paramMap.put("attatch_name", "attatch_name1");
		// end 2˽��
		// start 3΢��
		paramMap.put("msg_type", 2);
		// paramMap.put("material_id", "material_id");
		String articles = "[{\"title\":\"title\",\"autherName\":\"autherName\",\"summary\":\"summary\",\"picOriginalUrl\":\"picOriginalUrl\",\"picMiddleUrl\":\"picMiddleUrl\",\"picThumbUrl\":\"picThumbUrl\",\"content\":\"content\",\"conentUrl\":\"conentUrl\"},{\"title\":\"title\",\"autherName\":\"autherName\",\"summary\":\"summary\",\"picOriginalUrl\":\"picOriginalUrl\",\"picMiddleUrl\":\"picMiddleUrl\",\"picThumbUrl\":\"picThumbUrl\",\"content\":\"content\",\"conentUrl\":\"123\"}]";
		paramMap.put("content", "NIHAOι���");
		paramMap.put("articles", articles);
		// start 3΢��
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
