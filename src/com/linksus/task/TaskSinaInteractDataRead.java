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
 * ���˶�ȡ��������
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
				interactAtMeRun(appaccount);//������ת����@����
			}catch (Exception e){
				LogUtil.saveException(logger, e);
				e.printStackTrace();
			}

			try{
				interactComAtmeRun(appaccount);//���۲�@
			}catch (Exception e){
				LogUtil.saveException(logger, e);
				e.printStackTrace();
			}

			try{
				interactComToMeRun(appaccount);//��������������
			}catch (Exception e){
				LogUtil.saveException(logger, e);
				e.printStackTrace();
			}
			if(flag == true){
				dealTempData(appaccount);//������ʱ���е�����
				flag = false;

			}
			//}
		}
		checkTaskListEnd(appaccountList);//�ж������Ƿ���ѯ���
	}

	//������ʱ���е�����
	private void dealTempData(LinksusAppaccount appaccount){
		try{
			int currentPage = 1;
			int count = 200;
			while (currentPage > 0){
				//��temp���в�ѯ����
				//�������ŵ�Map���ٴ���sql
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
					//����
					LinksusTaskInteractWeiboMid interactWeiboMid = linksusTaskInteractWeiboMidList.get(j);
					Long userId = interactWeiboMid.getUserId();
					Long personId = interactWeiboMid.getPersonId();
					Long accountId = interactWeiboMid.getAccountId();
					Long weiboId = interactWeiboMid.getMid();
					Long commentId = interactWeiboMid.getCommentId();
					Long sourceWeiboId = interactWeiboMid.getReplyId();
					Integer interactTime = interactWeiboMid.getInteractTime();
					String content = interactWeiboMid.getContent();
					Long inteWeiboId = PrimaryKeyGen.getPrimaryKey("linksus_interact_weibo", "pid");//������Ϣ����
					WeiboInteractCommon interactCommon = new WeiboInteractCommon();
					Long recordId = interactCommon.dealWeiboInteract(userId, personId, accountId, 1, weiboId,
							interactWeiboMid.getInteractType().toString(), inteWeiboId, interactTime);
					//���뻥����Ϣ����
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
				if(linksusTaskInteractWeiboMidList.size() < count){//û����һҳ����
					break;
				}
				currentPage++;
			}
		}catch (Exception e){
			LogUtil.saveException(logger, e);
			e.printStackTrace();
		}

		//�����ʱ��������
		linksusTaskInteractWeiboMidService.deleteLinksusTaskInteractWeiboMid();
	}

	//���۲�@
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
		//String mId = "3700319760124869";//ȡ���ϴθ��¹������maxid ��Ϊ����������ȥȡ�µ�����
		String mId = "0";//ȡ���ϴθ��¹������maxid ��Ϊ����������ȥȡ�µ�����
		if(WeiboInteract != null){
			mId = WeiboInteract.getMaxId().toString();
		}
		int count = 200;//��ҳ���ص������
		int currentPage = 1;
		//ȡ��max_id
		String maxId = "";
		Map params = new HashMap();
		params.put("access_token", strToken);
		params.put("since_id", mId);
		params.put("count", 1);
		params.put("page", currentPage);
		String strResult = HttpUtil.getRequest(interactData, params);
		LinksusTaskErrorCode error = StringUtil.parseSinaErrorCode(strResult);
		if(error != null){
			logger.info(">>>>>>>>>>>>>>>ComAtme ����error:{}", strResult);
			return;
		}
		String commentsFromsina = JsonUtil.getNodeByName(strResult, "comments");
		if(StringUtils.isNotBlank(commentsFromsina)){
			List<?> commentsList = (List<?>) JsonUtil.json2list(commentsFromsina, Map.class);
			if(commentsList == null || commentsList.size() == 0){
				logger.debug(">>>>>>>>>>>��������>>>>>>>>>>");
				return;
			}
			flag = true;
			Map map = (Map) commentsList.get(0);
			maxId = map.get("id") + "";
			//��ѯ
			if(WeiboInteract == null){//΢�����������¼���������ݣ�����������
				LinksusTaskWeiboInteract newTaskWeiboInteract = new LinksusTaskWeiboInteract();
				newTaskWeiboInteract.setAccountId(accountId);
				newTaskWeiboInteract.setInteractType(4);
				newTaskWeiboInteract.setMaxId(Long.valueOf(maxId));
				newTaskWeiboInteract.setPagetime(0L);
				linksusTaskWeiboInteractService.insertLinksusTaskWeiboInteract(newTaskWeiboInteract);
			}
			logger.debug(">>>>>>>>>>>>>>>>>>>>>ȡ��maxId:{}", maxId);

			while (currentPage > 0){
				//�õ�maxId��Ϊ����������ȡ����
				Map paramsMap = new HashMap();
				paramsMap.put("access_token", strToken);
				paramsMap.put("since_id", mId);
				paramsMap.put("max_id", maxId);
				paramsMap.put("count", count);
				paramsMap.put("page", currentPage);
				strResult = HttpUtil.getRequest(interactData, paramsMap);
				error = StringUtil.parseSinaErrorCode(strResult);
				if(error != null){
					logger.info(">>>>>>>>>>>>>>>ComAtme1 ����error:{}", strResult);
					break;
				}
				commentsFromsina = JsonUtil.getNodeByName(strResult, "comments");
				commentsList = (List<?>) JsonUtil.json2list(commentsFromsina, Map.class);
				if(commentsList.size() <= 0){
					break;
				}
				for(int i = 0; i < commentsList.size(); i++){
					Map<?, ?> comMap = (Map<?, ?>) commentsList.get(i);
					Map weiboMap = (Map) comMap.get("status"); //���۵�΢����Ϣ�ֶ�
					Map userMap = (Map) comMap.get("user");
					String rpsId = userMap.get("id").toString();//ƽ̨�û�id
					Long mid = new Long((String) weiboMap.get("mid"));//΢��id
					String text = (String) comMap.get("text");
					String time = (String) comMap.get("created_at");
					Date createTime = DateUtil.parse(time, "EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
					Integer createTimeInteger = Integer.parseInt(String.valueOf(createTime.getTime() / 1000));
					String replyid = "0";//ԭ΢��id
					String commentid = (String) comMap.get("idstr");//�ַ�����΢��id
					Map weiboUserMap = (Map) weiboMap.get("user");
					if(StringUtils.equals(rpsId, appId)){//userId=companyid����ѭ�� �Լ����۲�@�Լ�
						continue;
					}

					Map<?, ?> replyComment = (Map<?, ?>) comMap.get("reply_comment");//�����������ڶ���һ���۵Ļظ��Ƿ��ظ��ֶ�
					if(replyComment != null){
						//������һ�����۵�id
						replyid = (String) replyComment.get("idstr");
					}
					//������۲�@�˺ŵ��û��Ƿ���� 
					LinksusRelationWeibouser user = getWeiboUserInfo(userMap, appaccount, true, 4);
					// updateRelationTypeAndNum(user,"4",appaccount);
					//���ԭ΢���û��Ƿ����
					LinksusRelationWeibouser oldUser = getWeiboUserInfo(weiboUserMap, appaccount, false, 0);
					//���΢���Ƿ����
					checkWeiboIsExsit(String.valueOf(mid), String.valueOf(accountType), weiboMap, "", oldUser);
					//΢���û����е�userIdȡ����Աid
					Long personId = 0L;
					Long userId = 0L;//�û�id
					if(user != null){
						userId = user.getUserId();
						personId = user.getPersonId();
					}
					//���뻥����Ϣ����ʱ��
					LinksusTaskInteractWeiboMid linksusInteractWeibo = new LinksusTaskInteractWeiboMid();
					linksusInteractWeibo.setRpsId(new Long(rpsId));//΢���û���ʶ
					linksusInteractWeibo.setUserId(userId);
					linksusInteractWeibo.setPersonId(personId);
					linksusInteractWeibo.setAccountId(accountId);//ƽ̨�˺�id
					linksusInteractWeibo.setCommentId(new Long(commentid));//��ǰ���۵�id
					linksusInteractWeibo.setReplyId(StringUtils.isBlank(replyid) ? 0L : new Long(replyid));//�ظ�����id
					linksusInteractWeibo.setMid(mid);//΢��id
					linksusInteractWeibo.setContent(text);//����:ת��/@/��������
					linksusInteractWeibo.setInteractType(4);//��������
					linksusInteractWeibo.setInteractTime(createTimeInteger);//����ʱ��
					linksusTaskInteractWeiboMidService.insertLinksusTaskInteractWeiboMid(linksusInteractWeibo);
				}
				if(commentsList.size() < count){//û����һҳ����
					break;
				}
				currentPage++;
			}//whileѭ������
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

	//���˻�ȡ@�ҵ�΢���б� ת����@,ͨ�������ж�Ϊ@������ת��
	private void interactAtMeRun(LinksusAppaccount appaccount) throws Exception{
		//		int a = 1, b = 0;
		//		a = a / b;
		UrlEntity interactData = LoadConfig.getUrlEntity("InteractDataSync"); //��ȡ@��ǰ�û�������΢��  
		Long accountId = appaccount.getId();
		//String strToken = "2.00NaVf3BeE2s5B2f1113d612012bdT"
		String strToken = appaccount.getToken();
		String appId = appaccount.getAppid();
		LinksusTaskWeiboInteract linksusTaskWeiboInteract = new LinksusTaskWeiboInteract();
		linksusTaskWeiboInteract.setAccountId(accountId);
		linksusTaskWeiboInteract.setInteractType(3);
		LinksusTaskWeiboInteract WeiboInteract = linksusTaskWeiboInteractService
				.getMaxIdByAccountId(linksusTaskWeiboInteract);
		//String mId = "3700319760124869";//ȡ���ϴθ��¹������maxid ��Ϊ����������ȥȡ�µ�����
		String mId = "0";//ȡ���ϴθ��¹������maxid ��Ϊ����������ȥȡ�µ�����
		if(WeiboInteract != null){
			mId = WeiboInteract.getMaxId().toString();
		}
		int count = 200;//��ҳ���ص������
		int currentPage = 1;
		//ȡ��max_id���µ����ݿ���� 
		String maxId = "";
		Map params = new HashMap();
		params.put("access_token", strToken);
		params.put("since_id", mId);
		params.put("count", 1);
		params.put("page", currentPage);
		String strResult = HttpUtil.getRequest(interactData, params);
		LinksusTaskErrorCode error = StringUtil.parseSinaErrorCode(strResult);
		if(error != null){
			logger.info(">>>>>>>>>>>>>>>@ ����error:{}", strResult);
			return;
		}
		String statusesFromsina = JsonUtil.getNodeByName(strResult, "statuses");
		if(StringUtils.isNotBlank(statusesFromsina)){
			List<?> statusesList = (List<?>) JsonUtil.json2list(statusesFromsina, Map.class);
			if(statusesList == null || statusesList.size() == 0){
				logger.debug(">>>>>>>>>>>��������>>>>>>>>>>");
				return;
			}
			flag = true;
			Map map = (Map) statusesList.get(0);
			maxId = map.get("id") + "";
			if(WeiboInteract == null){//΢�����������¼���������ݣ�����������
				LinksusTaskWeiboInteract newTaskWeiboInteract = new LinksusTaskWeiboInteract();
				newTaskWeiboInteract.setAccountId(accountId);
				newTaskWeiboInteract.setInteractType(3);
				newTaskWeiboInteract.setMaxId(Long.valueOf(maxId));
				newTaskWeiboInteract.setPagetime(0L);
				linksusTaskWeiboInteractService.insertLinksusTaskWeiboInteract(newTaskWeiboInteract);
			}
			logger.debug(">>>>>>>>>>>>>>>>>>>>>ȡ��maxId:{}", maxId);
			while (currentPage > 0){
				//�õ�maxId��Ϊ����������ȡ����
				Map paramsMap = new HashMap();
				paramsMap.put("access_token", strToken);
				paramsMap.put("since_id", mId);
				paramsMap.put("max_id", maxId);
				paramsMap.put("count", count);
				paramsMap.put("page", currentPage);
				strResult = HttpUtil.getRequest(interactData, paramsMap);
				logger.debug(">>>>>>>>>>>>>>>>>>>>>at me - sina:{}", interactData);
				//����strResult������ȡ��������
				error = StringUtil.parseSinaErrorCode(strResult);
				if(error != null){
					logger.info(">>>>>>>>>>>>>>>@1 ����error:{}", strResult);
					break;
				}
				statusesFromsina = JsonUtil.getNodeByName(strResult, "statuses");
				statusesList = (List<?>) JsonUtil.json2list(statusesFromsina, Map.class);
				if(statusesList == null || statusesList.size() <= 0){
					break;
				}
				for(int i = 0; i < statusesList.size(); i++){
					String pmid = "0";//ԭ΢��id
					String type = "3";//����Ϊ@
					Map<?, ?> statusMap = (Map<?, ?>) statusesList.get(i);
					//String midStr=(String)statusMap.get("idstr");//�ַ�����΢��id
					String content = (String) statusMap.get("text");
					String time = (String) statusMap.get("created_at");
					Date createTime = DateUtil.parse(time, "EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
					Integer createTimeInteger = Integer.parseInt(String.valueOf(createTime.getTime() / 1000));
					Map userMap = (Map) statusMap.get("user");//΢�����ߵ��û���Ϣ�ֶ�
					String rpsId = userMap.get("id").toString();
					Long weiboId = new Long(statusMap.get("id") + "");//΢��id
					if(appId.equals(userMap.get("id").toString())){//������Լ�@�Լ��Ĳ�ִ�в���
						continue;
					}
					//Long mid=(Long)statusMap.get("id");
					//�����ת�� ���ת����΢���Ƿ����
					Map<?, ?> retweetedStatus = (Map<?, ?>) statusMap.get("retweeted_status");//��ת����΢����Ϣ�ֶ�
					Map tmpMap = new HashMap();
					String tmpId = "";
					if(retweetedStatus != null){
						String deleteFlag = (String) retweetedStatus.get("deleted");
						if(!StringUtils.equals(deleteFlag, "1")){
							tmpMap = (Map) retweetedStatus.get("user");//��ת��ԭ΢���û���Ϣ
							if(tmpMap != null && tmpMap.size() > 0){
								tmpId = (String) tmpMap.get("idstr"); //��ת��ԭ΢���û�id
							}
							pmid = (String) retweetedStatus.get("idstr");//��ת����΢��id
							if(StringUtils.isNotBlank(pmid)){
								if(appId.equals(tmpId)){
									type = "2";//����Ϊת��	
								}
								LinksusRelationWeibouser user = getWeiboUserInfo(tmpMap, appaccount, false, 0);
								checkOldWeiboIsExsit(appaccount.getToken(), new Long(pmid), "", user);
							}
							//				    	if(appId.equals(tmpId)){
							//					    	 type="2";//����Ϊת��
							//					    	 LinksusRelationWeibouser user=getWeiboUserInfo(tmpMap ,appaccount);
							//					    	// updateRelationTypeAndNum(user,"2",appaccount);		
							//					    	 checkOldWeiboIsExsit(appaccount.getToken(),new Long(pmid),user);
							//				    	}
						}
					}
					//���@�˺ŵ��û��Ƿ���� 
					LinksusRelationWeibouser user = getWeiboUserInfo(userMap, appaccount, true, new Integer(type));
					//updateRelationTypeAndNum(user,"3",appaccount);
					//���΢���Ƿ����
					checkWeiboIsExsit(String.valueOf(weiboId), String.valueOf(accountType), statusMap, pmid, user);
					//΢���û����е�userIdȡ����Աid
					Long personId = 0L;
					Long userId = 0L;//�û�id
					if(user != null){
						userId = user.getUserId();
						personId = user.getPersonId();
					}
					//���뻥����Ϣ����ʱ��
					LinksusTaskInteractWeiboMid linksusInteractWeibo = new LinksusTaskInteractWeiboMid();
					linksusInteractWeibo.setRpsId(new Long(rpsId));//΢���û���ʶ
					linksusInteractWeibo.setUserId(userId);
					linksusInteractWeibo.setPersonId(personId);
					linksusInteractWeibo.setAccountId(accountId);//ƽ̨�˺�id
					linksusInteractWeibo.setCommentId(0L);//��ǰ���۵�id
					linksusInteractWeibo.setReplyId(new Long(pmid));//
					linksusInteractWeibo.setMid(weiboId);//΢��id
					linksusInteractWeibo.setContent(content);//����:ת��/@/��������
					linksusInteractWeibo.setInteractType(Integer.valueOf(type));//��������
					linksusInteractWeibo.setInteractTime(createTimeInteger);//����ʱ��
					linksusTaskInteractWeiboMidService.insertLinksusTaskInteractWeiboMid(linksusInteractWeibo);
				}
				if(statusesList.size() < count){//û����һҳ����
					break;
				}
				currentPage++;
			}
			// �������ݻ������е����id 
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

	//���˻�ȡ���յ��������б�
	private void interactComToMeRun(LinksusAppaccount appaccount) throws Exception{
		Long accountId = appaccount.getId();
		//String strToken = "2.00NaVf3BeE2s5B2f1113d612012bdT";// appaccount.getToken();
		String strToken = appaccount.getToken();
		UrlEntity interactData = LoadConfig.getUrlEntity("InteractDataSyncComToMe");
		LinksusTaskWeiboInteract linksusTaskWeiboInteract = new LinksusTaskWeiboInteract();
		linksusTaskWeiboInteract.setAccountId(accountId);
		linksusTaskWeiboInteract.setInteractType(1);
		LinksusTaskWeiboInteract WeiboInteract = linksusTaskWeiboInteractService
				.getMaxIdByAccountId(linksusTaskWeiboInteract);//��ȡ�����ٽ��
		//String mId = "3700319760124869";
		String mId = "0";
		if(WeiboInteract != null){
			mId = WeiboInteract.getMaxId().toString();
		}
		int count = 200;//��ҳ���ص������
		int currentPage = 1;
		//ȡ��max_id
		String maxId = "";
		Map params = new HashMap();
		params.put("access_token", strToken);
		params.put("since_id", mId);
		params.put("count", 1);
		params.put("page", currentPage);
		String resultData = HttpUtil.getRequest(interactData, params);
		LinksusTaskErrorCode error = StringUtil.parseSinaErrorCode(resultData);
		if(error != null){
			logger.info(">>>>>>>>>>>>>>>ComToMe ����error:{}", resultData);
			return;
		}
		logger.debug(">>>>>>>>>>>>>>>>>>>>>��ѯmaxId:{}", interactData);
		String commentsFromsina = JsonUtil.getNodeByName(resultData, "comments");
		if(StringUtils.isNotBlank(commentsFromsina)){
			List<?> commentsList = (List<?>) JsonUtil.json2list(commentsFromsina, Map.class);
			if(commentsList == null || commentsList.size() == 0){
				logger.debug(">>>>>>>>>>>��������>>>>>>>>>>");
				return;
			}
			flag = true;
			Map map = (Map) commentsList.get(0);
			maxId = map.get("id") + "";
			if(WeiboInteract == null){//΢�����������¼���������ݣ�����������
				LinksusTaskWeiboInteract newTaskWeiboInteract = new LinksusTaskWeiboInteract();
				newTaskWeiboInteract.setAccountId(accountId);
				newTaskWeiboInteract.setInteractType(1);
				newTaskWeiboInteract.setMaxId(Long.valueOf(maxId));
				newTaskWeiboInteract.setPagetime(0L);
				linksusTaskWeiboInteractService.insertLinksusTaskWeiboInteract(newTaskWeiboInteract);
			}
			logger.debug(">>>>>>>>>>>>>>>>>>>>>ȡ��maxId:{}", maxId);
			while (currentPage > 0){
				//�õ�maxId��Ϊ����������ȡ����
				Map paramsMap = new HashMap();
				paramsMap.put("access_token", strToken);
				paramsMap.put("since_id", mId);
				paramsMap.put("max_id", maxId);
				paramsMap.put("count", count);
				paramsMap.put("page", currentPage);
				String strResult = HttpUtil.getRequest(interactData, paramsMap);
				//����strResult������ȡ��������
				error = StringUtil.parseSinaErrorCode(resultData);
				if(error != null){
					logger.info(">>>>>>>>>>>>>>>ComToMe1 ����error:{}", strResult);
					break;
				}
				commentsFromsina = JsonUtil.getNodeByName(strResult, "comments");
				commentsList = (List<?>) JsonUtil.json2list(commentsFromsina, Map.class);
				if(commentsList == null || commentsList.size() <= 0){
					break;
				}
				for(int i = 0; i < commentsList.size(); i++){//�õ������б�
					Map<?, ?> commentMap = (Map<?, ?>) commentsList.get(i);
					Long commentId = (Long) commentMap.get("id");//����ID
					String content = (String) commentMap.get("text");//��������
					String replyid = "0";//�ظ�����id
					Map<?, ?> replyComment = (Map<?, ?>) commentMap.get("reply_comment");//�����������ڶ���һ���۵Ļظ��Ƿ��ظ��ֶ�
					if(replyComment != null){
						//������һ�����۵�id ���ظ����۵�id
						replyid = (String) replyComment.get("idstr");
					}
					String time = (String) commentMap.get("created_at");
					Date createTime = DateUtil.parse(time, "EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
					Integer createTimeInteger = Integer.parseInt(String.valueOf(createTime.getTime() / 1000));
					Map userMap = (Map) commentMap.get("user");//�õ������û�����Ϣ
					Map statusMap = (Map) commentMap.get("status");
					Long weiboId = new Long(statusMap.get("id") + "");//΢��id
					//��ȡ����΢�����ߵ�id
					Map weiboMap = (Map) statusMap.get("user");
					if(weiboMap != null){
						if(content.contains("@" + weiboMap.get("name") + "")
								&& !content.startsWith("�ظ�@" + weiboMap.get("name") + "")){//������û����۵�ǰ�˺�΢����@�˺ŵĲ�ִ�в��������۲�@ʱ�����ظ���
							//if(content.contains("@" + weiboMap.get("name")+"")){
							continue;
						}
					}else{
						logger.error("userΪ��");
						continue;
					}
					//΢���û�����ȡ���û���userId 
					String rpsId = String.valueOf(userMap.get("idstr")); //ȡ��΢���û���ʶ
					String weiboUserName = (String) userMap.get("idstr");//
					//�������΢���û��Ƿ����
					LinksusRelationWeibouser user = getWeiboUserInfo(userMap, appaccount, true, 1);
					//updateRelationTypeAndNum(user,"1",appaccount);
					//���ԭ΢���û��Ƿ����
					LinksusRelationWeibouser oldUser = getWeiboUserInfo(weiboMap, appaccount, false, 0);
					//������۵�Դ΢���Ƿ���� 
					checkWeiboIsExsit(String.valueOf(weiboId), String.valueOf(accountType), statusMap, "", oldUser);
					//΢���û����е�userIdȡ����Աid
					Long personId = 0L;
					Long userId = 0L;//�û�id
					if(user != null){
						userId = user.getUserId();
						personId = user.getPersonId();
					}
					//���뻥����Ϣ����ʱ��
					LinksusTaskInteractWeiboMid linksusInteractWeibo = new LinksusTaskInteractWeiboMid();
					linksusInteractWeibo.setRpsId(new Long(rpsId));//΢���û���ʶ
					linksusInteractWeibo.setUserId(userId);
					linksusInteractWeibo.setPersonId(personId);
					linksusInteractWeibo.setAccountId(accountId);//ƽ̨�˺�id
					linksusInteractWeibo.setCommentId(commentId);//��ǰ���۵�id
					linksusInteractWeibo.setReplyId(StringUtils.isBlank(replyid) ? 0L : new Long(replyid));//�ظ�����id
					linksusInteractWeibo.setMid(weiboId);//΢��id
					linksusInteractWeibo.setContent(content);//����:ת��/@/��������
					if(content.startsWith("�ظ�@" + weiboMap.get("name") + "")){
						linksusInteractWeibo.setInteractType(4);//��������
					}else{
						linksusInteractWeibo.setInteractType(1);//��������	
					}
					linksusInteractWeibo.setInteractTime(createTimeInteger);//����ʱ��
					linksusTaskInteractWeiboMidService.insertLinksusTaskInteractWeiboMid(linksusInteractWeibo);
				}
				if(commentsList.size() < count){//û����һҳ����
					break;
				}
				currentPage++;
			}
			if(!StringUtils.isBlank(maxId)){//���»���������е����maxid
				LinksusTaskWeiboInteract linksusTaskWeibo = new LinksusTaskWeiboInteract();
				linksusTaskWeibo.setAccountId(accountId);
				linksusTaskWeibo.setInteractType(1);
				linksusTaskWeibo.setMaxId(new Long(maxId));
				linksusTaskWeibo.setPagetime(new Long(0));
				linksusTaskWeiboInteractService.updateLinksusTaskWeiboInteract(linksusTaskWeibo);
			}
		}
	}

	//���ԭ΢���Ƿ����
	public void checkOldWeiboIsExsit(String strToken,Long pmid,String srcMid,LinksusRelationWeibouser user){
		if(pmid != 0){
			//�ж�΢���Ƿ����1
			//	        Map weiboPoolMap =new HashMap();
			//			weiboPoolMap.put("mid",pmid);
			//			weiboPoolMap.put("weiboType",accountType);
			//			LinksusWeiboPool weiboCount = linksusWeiboPoolService.checkWeiboPoolIsExsit(weiboPoolMap);
			String weiboRedis = RedisUtil.getRedisHash("weibo_pool", pmid + "#" + "1");
			//�����ݷ��͵����˽ӿ� ��ȡ���� 
			UrlEntity transInter = LoadConfig.getUrlEntity("InteractDataSyncTrans");
			Map params = new HashMap();
			params.put("access_token", strToken);
			params.put("id", pmid);
			String strResult = HttpUtil.getRequest(transInter, params);
			LinksusTaskErrorCode error = StringUtil.parseSinaErrorCode(strResult);
			// ����д�������
			if(error != null){
				return;
			}else{//����΢�����е� ����
				Map weiboMap = (Map) JsonUtil.json2map(strResult, Map.class);
				if(user != null){
					LinksusWeiboPool weiboPool = new LinksusWeiboPool();
					if(!StringUtils.isBlank(weiboRedis)){//���ڸ���
						weiboPool.setMid(new Long(pmid));
						weiboPool.setWeiboType(1);
						weiboPool.setRepostCount(weiboMap.get("reposts_count") == null ? 0 : (Integer) weiboMap
								.get("reposts_count"));
						weiboPool.setCommentCount(weiboMap.get("comments_count") == null ? 0 : (Integer) weiboMap
								.get("comments_count"));
						linksusWeiboPoolService.updateLinksusWeiboPoolDataCount(weiboPool);
					}else{//�����ڲ���
						weiboPool.setMid(new Long(pmid));
						//						Map userMap = new HashMap();
						//						String weiboUser = JsonUtil.getNodeByName(strResult, "user");
						//						userMap = JsonUtil.json2map(weiboUser, Map.class);
						//						String rpsId = userMap.get("id") + "";
						copySinaDataToWeiboPool(weiboMap, weiboPool);
						//�û�id
						weiboPool.setUid(user.getUserId());
						//�û�����
						weiboPool.setUname(user.getRpsScreenName());
						//�û�ͷ��
						weiboPool.setUprofileUrl(user.getRpsProfileImageUrl());
						weiboPool.setWeiboType(1);

						String currUrl = "http://weibo.com/" + user.getRpsId() + "/" + WeiboUtil.Id2Mid(new Long(pmid));
						weiboPool.setCurrentUrl(currUrl);
						//ԭid
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
	 *  ���΢���Ƿ���� 
	 * */
	public void checkWeiboIsExsit(String mid,String accountType,Map statusMap,String srcMid,
			LinksusRelationWeibouser user){
		//�ж�΢���Ƿ����
		//    	 Map weiboPoolMap =new HashMap();
		//		 weiboPoolMap.put("mid",mid);
		//		 weiboPoolMap.put("weiboType",accountType);
		//		 LinksusWeiboPool weiboCount = linksusWeiboPoolService.checkWeiboPoolIsExsit(weiboPoolMap);
		String weiboRedis = RedisUtil.getRedisHash("weibo_pool", mid + "#" + accountType);
		if(user != null){
			LinksusWeiboPool weiboPool = new LinksusWeiboPool();
			if(!StringUtils.isBlank(weiboRedis)){//���ڸ���
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
			}else{//�����ڲ���
				weiboPool.setMid(new Long(mid));
				copySinaDataToWeiboPool(statusMap, weiboPool);
				//�û�id
				weiboPool.setUid(user.getUserId());
				//�û�����
				weiboPool.setUname(user.getRpsScreenName());
				//�û�ͷ��
				weiboPool.setUprofileUrl(user.getRpsProfileImageUrl());
				weiboPool.setWeiboType(Integer.valueOf(accountType));
				//if(!StringUtils.isBlank(openId) && !StringUtils.isBlank(weiboId)){
				String currUrl = "http://weibo.com/" + user.getRpsId() + "/" + WeiboUtil.Id2Mid(new Long(mid));
				weiboPool.setCurrentUrl(currUrl);
				//ԭid
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
	 *  ������΢���û�����ѯ΢���û�����Ϣ
	* @throws Exception 
	 * */
	public LinksusRelationWeibouser getWeiboUserInfo(Map userMap,LinksusAppaccount appaccount,boolean flag,
			int interactBit) throws Exception{
		String userStr = JsonUtil.map2json(userMap);
		String accountId = String.valueOf(appaccount.getId());
		Long institutionId = appaccount.getInstitutionId();
		//��ȡ�����û�������appId ��  ҵ�������ж��û��Ƿ����΢���û�����
		WeiboUserCommon weiboUser = new WeiboUserCommon();
		Map parmMap = new HashMap();
		parmMap.put("token", appaccount.getToken());
		parmMap.put("openid", appaccount.getAppid());
		parmMap.put("type", String.valueOf(accountType));
		parmMap.put("appkey", appaccount.getAppKey());
		parmMap.put("AccountId", accountId);
		parmMap.put("InstitutionId", institutionId);
		String updateType = "";
		if(flag){//flagΪtrue ���»���
			updateType = "3";
		}else{
			updateType = "0";
		}
		LinksusRelationWeibouser user = weiboUser.dealWeiboUserInfo(userStr, "2", parmMap, false, updateType,
				interactBit);
		//			//΢���û�����ȡ���û���userId 
		//			String rpsId = String.valueOf(userMap.get("idstr")); 
		//			//����rpsId��accountType �ж��û��Ƿ����
		//			Map paraMap =new HashMap();
		//			paraMap.put("rpsId",rpsId);
		//			paraMap.put("userType",accountType);
		//		    LinksusRelationWeibouser user=relationWeiboUserService.getWeibouserByrpsIdAndType(paraMap);
		return user;
	}

	//�����������ݵ�΢���ر�
	public void copySinaDataToWeiboPool(Map weiboMap,LinksusWeiboPool weiboPool){
		//΢��id
		//weiboPool.setMid(new Long(weiboMap.get("id")+""));
		//΢������
		//weiboPool.setWeiboType();
		//����
		weiboPool.setContent((String) weiboMap.get("text"));
		//��ȡ��ͼ
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
		// ԭͼ
		weiboPool.setPicOriginalUrl(StringUtils.isBlank(originalUrl) ? "0" : originalUrl.substring(0, originalUrl
				.length() - 1));
		// ��ͼ
		weiboPool
				.setPicMiddleUrl(StringUtils.isBlank(middleUrl) ? "0" : middleUrl.substring(0, middleUrl.length() - 1));
		// Сͼ
		weiboPool.setPicThumbUrl(StringUtils.isBlank(thumbUrl) ? "0" : thumbUrl.substring(0, thumbUrl.length() - 1));
		//��Ƶ��ַ
		weiboPool.setMusicUrl("0");
		//��Ƶ��ַ
		weiboPool.setVideoUrl("0");
		//������
		weiboPool.setCommentCount(Integer.valueOf(weiboMap.get("comments_count") + ""));
		//ת����
		weiboPool.setRepostCount(Integer.valueOf(weiboMap.get("reposts_count") + ""));
		//ԭid
		//weiboPool.setSrcMid(new Long(weiboMap.get("mid") + ""));
		//��ַ��Ϣ
		weiboPool.setGeo("0");
		//�û�id
		// ------------weiboPool.setUid((String)weiboMap.get("id"));
		//�û�����
		// --------------weiboPool.setUname((String)weiboMap.get("id"));
		//�û�ͷ��
		//--------------weiboPool.setUprofileUrl((String)weiboMap.get("id"));
		//��������
		weiboPool.setPublishType(0);
		//��Դ
		weiboPool.setSource((String) weiboMap.get("source"));
		//����ʱ��
		Date createTime = DateUtil.parse(weiboMap.get("created_at").toString(), "EEE MMM dd HH:mm:ss zzz yyyy",
				Locale.US);
		weiboPool.setCreatedTime(Integer.valueOf(String.valueOf(createTime.getTime() / 1000)));
		//΢����URL
		//weiboPool.setCurrentUrl("0");

	}
}
