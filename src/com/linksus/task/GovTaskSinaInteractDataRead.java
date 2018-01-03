package com.linksus.task;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linksus.common.Constants;
import com.linksus.common.config.LoadConfig;
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
import com.linksus.entity.LinksusGovInteract;
import com.linksus.entity.LinksusGovRunning;
import com.linksus.entity.LinksusGovStructure;
import com.linksus.entity.LinksusRelationWeibouser;
import com.linksus.entity.LinksusTaskErrorCode;
import com.linksus.entity.LinksusTaskGovInteract;
import com.linksus.entity.LinksusTaskInteractGovMid;
import com.linksus.entity.LinksusWeiboPool;
import com.linksus.entity.UrlEntity;
import com.linksus.service.LinksusAppaccountService;
import com.linksus.service.LinksusGovInteractService;
import com.linksus.service.LinksusGovRunningService;
import com.linksus.service.LinksusGovStructureService;
import com.linksus.service.LinksusRelationWeibouserService;
import com.linksus.service.LinksusTaskGovInteractService;
import com.linksus.service.LinksusTaskInteractGovMidService;
import com.linksus.service.LinksusWeiboPoolService;

/**
 * �������˶�ȡ��������
 */
public class GovTaskSinaInteractDataRead extends BaseTask{

	protected static final Logger logger = LoggerFactory.getLogger(GovTaskSinaInteractDataRead.class);
	private LinksusAppaccountService linksusAppaccountService = (LinksusAppaccountService) ContextUtil
			.getBean("linksusAppaccountService");
	private LinksusRelationWeibouserService relationWeiboUserService = (LinksusRelationWeibouserService) ContextUtil
			.getBean("linksusRelationWeibouserService");
	private LinksusWeiboPoolService linksusWeiboPoolService = (LinksusWeiboPoolService) ContextUtil
			.getBean("linksusWeiboPoolService");

	private LinksusGovRunningService linksusGovRunningService = (LinksusGovRunningService) ContextUtil
			.getBean("linksusGovRunningService");
	private LinksusGovInteractService linksusGovInteractService = (LinksusGovInteractService) ContextUtil
			.getBean("linksusGovInteractService");
	private LinksusTaskInteractGovMidService linksusTaskInteractGovMidService = (LinksusTaskInteractGovMidService) ContextUtil
			.getBean("linksusTaskInteractGovMidService");
	private LinksusTaskGovInteractService linksusTaskGovInteractService = (LinksusTaskGovInteractService) ContextUtil
			.getBean("linksusTaskGovInteractService");
	private LinksusGovStructureService linksusGovStructureService = (LinksusGovStructureService) ContextUtil
			.getBean("linksusGovStructureService");
	private boolean flag = false;

	public static void main(String[] args){
		GovTaskSinaInteractDataRead interactDataRead = new GovTaskSinaInteractDataRead();
		interactDataRead.setAccountType("1");
		interactDataRead.cal();
	}

	@Override
	public void cal(){
		try{
			LinksusAppaccount linksusAppaccount = new LinksusAppaccount();
			int startCount = (currentPage - 1) * pageSize;
			linksusAppaccount.setPageSize(pageSize);
			linksusAppaccount.setStartCount(startCount);
			linksusAppaccount.setType(accountType);

			//Thread thread = new Thread(new QueueDataSave());
			//thread.start();

			//ȡ��������ص��ʺ�
			List<LinksusAppaccount> appaccountList = linksusAppaccountService
					.getLinksusOrgAppaccountList(linksusAppaccount);
			if(appaccountList != null && appaccountList.size() > 0){
				for(LinksusAppaccount appaccount : appaccountList){

					Long accountId = appaccount.getId();
					//��ȡĬ����֯�ṹ
					//				List<LinksusGovStructure> orgList = linksusGovStructureService.getLinksusGovStructureByOrgId();
					LinksusGovStructure govStructure = linksusGovStructureService.getMinGidByAccountId(accountId);

					//�ʺ�ͬgid��ӳ��
					//				Map<Integer, Integer> orgMap = new HashMap<Integer, Integer>();
					//				for(Iterator<LinksusGovStructure> iter = orgList.iterator(); iter.hasNext();){
					//					LinksusGovStructure org = iter.next();
					if(govStructure == null){
						LogUtil.saveException(logger, new Exception("-----��֯�����������쳣:" + "��֯����AccountId:"
								+ govStructure.getAccountId()));
						return;
					}
					//					orgMap.put(org.getAccountId(), org.getGid());
					//				}

					//����������ʺ�
					if(appaccount.getId() == 1144){
						//������ת����@����
						try{
							interactAtMeRun(appaccount);
						}catch (Exception e){
							LogUtil.saveException(logger, e);
							e.printStackTrace();
						}
						//���۲�@
						try{
							interactComAtmeRun(appaccount);
						}catch (Exception e){
							LogUtil.saveException(logger, e);
							e.printStackTrace();
						}
						//��������������
						try{
							interactComToMeRun(appaccount);
						}catch (Exception e){
							LogUtil.saveException(logger, e);
							e.printStackTrace();
						}
						//������ʱ���е�����
						if(flag == true){
							dealTempData(appaccount, govStructure.getGid());
							flag = false;
						}
					}
				}
			}
			checkTaskListEnd(appaccountList);//�ж������Ƿ���ѯ���
		}catch (Exception e){
			LogUtil.saveException(logger, e);
			e.printStackTrace();
		}
	}

	//������ʱ���е�����
	private void dealTempData(LinksusAppaccount appaccount,Integer gid){

		//�������۲�ת�� 
		dealCommentAndRelay();
		//�������
		dealDuoWen(gid);

		try{
			int currentPage = 1;
			int count = 200;
			while (currentPage > 0){
				//��temp���в�ѯ����
				//�������ŵ�Map���ٴ���sql
				int countPage = (currentPage - 1) * count;
				Map mapTemp = new HashMap();
				mapTemp.put("accountId", appaccount.getId());
				mapTemp.put("countPage", Integer.valueOf(countPage));
				List<LinksusTaskInteractGovMid> linksusTaskInteractGovMidList = linksusTaskInteractGovMidService
						.getGovMidTempListByMap(mapTemp);
				if(linksusTaskInteractGovMidList.size() == 0){
					break;
				}
				//List<LinksusGovRunning> govRuningList = new ArrayList<LinksusGovRunning>();
				//List<LinksusGovInteract> govInteractList = new ArrayList<LinksusGovInteract>();
				for(int j = 0; j < linksusTaskInteractGovMidList.size(); j++){
					//����
					LinksusTaskInteractGovMid interactGovMid = linksusTaskInteractGovMidList.get(j);
					Long runId = PrimaryKeyGen.GenerateSerialNum();//���񻥶���Ϣ����
					//					Integer gid = orgMap.get(new Integer(interactGovMid.getAccountId().intValue()));
					//������ˮ����
					saveGovRunning(interactGovMid, runId, gid, Constants.DEFAULTORG, 0);
					// ��������΢��������Ϣ����
					saveGovInteract(interactGovMid, runId);
				}
				if(linksusTaskInteractGovMidList.size() < count){//û����һҳ����
					break;
				}
				currentPage++;
			}
		}catch (Exception e){
			LogUtil.saveException(logger, e);
			e.printStackTrace();
		}
		//�����ʱ��������
		linksusTaskInteractGovMidService.deleteLinksusTaskInteractGovMid();
	}

	//���۲�@
	private void interactComAtmeRun(LinksusAppaccount appaccount) throws Exception{
		UrlEntity interactData = LoadConfig.getUrlEntity("InteractDataSyncComments");
		Long accountId = appaccount.getId();
		String strToken = appaccount.getToken();
		String appId = appaccount.getAppid();
		LinksusTaskGovInteract linksusTaskGovInteract = new LinksusTaskGovInteract();
		linksusTaskGovInteract.setAccountId(accountId);
		linksusTaskGovInteract.setInteractType(4);
		LinksusTaskGovInteract GovInteract = linksusTaskGovInteractService.getMaxIdByAccountId(linksusTaskGovInteract);
		String mId = "0";//ȡ���ϴθ��¹������maxid ��Ϊ����������ȥȡ�µ�����
		if(GovInteract != null){
			mId = GovInteract.getMaxId().toString();
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
		List<?> commentsList = (List<?>) JsonUtil.json2list(commentsFromsina, Map.class);
		if(commentsList == null || commentsList.size() == 0){
			logger.debug(">>>>>>>>>>>��������>>>>>>>>>>");
			return;
		}
		flag = true;
		Map map = (Map) commentsList.get(0);
		maxId = (String) map.get("id");
		if(GovInteract == null){//΢�����������¼���������ݣ�����������
			saveGovTaskInteract(accountId, 4, Long.valueOf(maxId));
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
			//List<LinksusTaskInteractGovMid> batchList = new ArrayList<LinksusTaskInteractGovMid>();
			for(int i = 0; i < commentsList.size(); i++){
				Map<?, ?> comMap = (Map<?, ?>) commentsList.get(i);
				Map weiboMap = (Map) comMap.get("status"); //���۵�΢����Ϣ�ֶ�
				Map userMap = (Map) comMap.get("user");
				String rpsId = userMap.get("id").toString();//ƽ̨�û�id
				Long mid = new Long(weiboMap.get("id").toString());//΢��id
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
				//				batchList.add(saveGovInteractMid(new Long(rpsId), userId, personId, accountId, new Long(commentid),
				//						replyid, mid, text, 4, createTimeInteger));
				saveGovInteractMid(new Long(rpsId), userId, personId, accountId, new Long(commentid), replyid, mid,
						text, 4, createTimeInteger);
			}
			//linksusTaskInteractGovMidService.saveLinksusInteractGovMid(batchList, Constants.OPER_TYPE_INSERT);
			if(commentsList.size() < count){//û����һҳ����
				break;
			}
			currentPage++;
		}//whileѭ������
		if(!StringUtils.isBlank(maxId)){
			updateGovTaskInteract(accountId, 4, Long.valueOf(maxId));
		}
	}

	// ��ȡ@��ǰ�û�������΢��  
	private void interactAtMeRun(LinksusAppaccount appaccount) throws Exception{
		//��ȡ@��ǰ�û�������΢��  
		UrlEntity interactData = LoadConfig.getUrlEntity("InteractDataSync");
		Long accountId = appaccount.getId();
		String strToken = appaccount.getToken();
		String appId = appaccount.getAppid();
		LinksusTaskGovInteract linksusTaskGovInteract = new LinksusTaskGovInteract();
		linksusTaskGovInteract.setAccountId(accountId);
		linksusTaskGovInteract.setInteractType(3);
		LinksusTaskGovInteract govInteract = linksusTaskGovInteractService.getMaxIdByAccountId(linksusTaskGovInteract);
		String mId = "0";//ȡ���ϴθ��¹������maxid ��Ϊ����������ȥȡ�µ�����
		if(govInteract != null){
			mId = govInteract.getMaxId().toString();
		}
		int count = 200;//��ҳ���ص������
		int currentPage = 1;
		//ȡ��max_id���µ����ݿ���� 
		String maxId = "0";
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
		List<?> statusesList = (List<?>) JsonUtil.json2list(statusesFromsina, Map.class);
		if(statusesList == null || statusesList.size() == 0){
			logger.debug(">>>>>>>>>>>��������>>>>>>>>>>");
			return;
		}
		flag = true;
		Map map = (Map) statusesList.get(0);
		maxId = (String) map.get("id");
		if(govInteract == null){//΢�����������¼���������ݣ�����������
			saveGovTaskInteract(accountId, 3, Long.valueOf(maxId));
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
			//	List<LinksusTaskInteractGovMid> batchList = new ArrayList<LinksusTaskInteractGovMid>();
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
				//Long mid = (Long) statusMap.get("id");
				//�����ת�� ���ת����΢���Ƿ����
				Map<?, ?> retweetedStatus = (Map<?, ?>) statusMap.get("retweeted_status");//��ת����΢����Ϣ�ֶ�
				Map tmpMap = new HashMap();
				String tmpId = "";
				if(retweetedStatus != null){
					/**tmpMap = (Map) retweetedStatus.get("user");//��ת��ԭ΢���û���Ϣ
					if(tmpMap != null && tmpMap.size() > 0){
						tmpId = (String) tmpMap.get("idstr"); //��ת��ԭ΢���û�id
					}
					pmid = (String) retweetedStatus.get("idstr");//��ת����΢��id
					if(appId.equals(tmpId)){
						type = "2";//����Ϊת��
						LinksusRelationWeibouser user = getWeiboUserInfo(tmpMap, appaccount, new Integer(type));
						// updateRelationTypeAndNum(user,"2",appaccount);			
						checkOldWeiboIsExsit(appaccount.getToken(), new Long(pmid), user);
					}
					}*/
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
				saveGovInteractMid(new Long(rpsId), userId, personId, accountId, 0L, pmid, weiboId, content, Integer
						.parseInt(type), createTimeInteger);
				//���뻥����Ϣ����ʱ��
				// QueueDataSave.addDataToQueue(saveGovInteractMid(new Long(rpsId),userId,personId,accountId,0L,pmid, weiboId, content,Integer.parseInt(type),createTimeInteger ), Constants.OPER_TYPE_INSERT);
				//				batchList.add(saveGovInteractMid(new Long(rpsId), userId, personId, accountId, 0L, pmid, weiboId,
				//						content, Integer.parseInt(type), createTimeInteger));
			}
			//linksusTaskInteractGovMidService.saveLinksusInteractGovMid(batchList, Constants.OPER_TYPE_INSERT);

			if(statusesList.size() < count){//û����һҳ����
				break;
			}
			currentPage++;
		}
		// �������ݻ������е����id 
		if(!StringUtils.isBlank(maxId)){
			updateGovTaskInteract(accountId, 3, Long.valueOf(maxId));
		}
	}

	//���˻�ȡ���յ��������б�
	private void interactComToMeRun(LinksusAppaccount appaccount) throws Exception{
		Long accountId = appaccount.getId();
		String strToken = appaccount.getToken();
		UrlEntity interactData = LoadConfig.getUrlEntity("InteractDataSyncComToMe");
		LinksusTaskGovInteract linksusTaskGovInteract = new LinksusTaskGovInteract();
		linksusTaskGovInteract.setAccountId(accountId);
		linksusTaskGovInteract.setInteractType(1);
		LinksusTaskGovInteract govInteract = linksusTaskGovInteractService.getMaxIdByAccountId(linksusTaskGovInteract);
		String mId = "0";
		if(govInteract != null){
			mId = govInteract.getMaxId().toString();
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
		logger.info(">>>>>>>>>>>>>>>>>>>>>��ѯmaxId:{}", interactData);
		String commentsFromsina = JsonUtil.getNodeByName(resultData, "comments");
		List<?> commentsList = (List<?>) JsonUtil.json2list(commentsFromsina, Map.class);
		if(commentsList == null || commentsList.size() == 0){
			logger.debug(">>>>>>>>>>>��������>>>>>>>>>>");
			return;
		}
		flag = true;
		Map map = (Map) commentsList.get(0);
		maxId = (String) map.get("id");
		if(govInteract == null){//΢�����������¼���������ݣ�����������
			saveGovTaskInteract(accountId, 1, Long.valueOf(maxId));
		}
		logger.debug(">>>>>>>>>>>>>>>>>>>>>ȡ��maxId{}", maxId);
		while (currentPage > 0){
			//�õ�maxId��Ϊ����������ȡ����
			Map paramsMap = new HashMap();
			paramsMap.put("access_token", strToken);
			paramsMap.put("since_id", mId);
			paramsMap.put("max_id", maxId);
			paramsMap.put("count", count);
			paramsMap.put("page", currentPage);
			String strResult = HttpUtil.getRequest(interactData, paramsMap);
			logger.debug(">>>>>>>>>>>>>>>>>>>>>to me - sina url :{}", interactData);
			//����strResult������ȡ��������
			error = StringUtil.parseSinaErrorCode(strResult);
			if(error != null){
				logger.info(">>>>>>>>>>>>>>>ComToMe1 ����error:{}", strResult);
				break;
			}
			commentsFromsina = JsonUtil.getNodeByName(strResult, "comments");
			commentsList = (List<?>) JsonUtil.json2list(commentsFromsina, Map.class);
			if(commentsList == null || commentsList.size() <= 0){
				break;
			}
			//List<LinksusTaskInteractGovMid> batchList = new ArrayList<LinksusTaskInteractGovMid>();
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
				Map weiboMap = (Map) statusMap.get("user");
				if(weiboMap != null){
					if(content.contains("@" + weiboMap.get("name") + "")
							&& !content.startsWith("�ظ�@" + weiboMap.get("name") + "")){//������û����۵�ǰ�˺�΢����@�˺ŵĲ�ִ�в��������۲�@ʱ�����ظ���
						continue;
					}
				}else{
					logger.error("userΪ��");
					continue;
				}
				//΢���û�����ȡ���û���userId 
				String rpsId = String.valueOf(userMap.get("idstr")); //ȡ��΢���û���ʶ
				//���΢���û��Ƿ����
				LinksusRelationWeibouser user = getWeiboUserInfo(userMap, appaccount, true, 1);
				//updateRelationTypeAndNum(user,"1",appaccount);
				//���ԭ΢���û��Ƿ����
				LinksusRelationWeibouser oldUser = getWeiboUserInfo(weiboMap, appaccount, false, 0);
				//���΢���Ƿ���� 
				checkWeiboIsExsit(String.valueOf(weiboId), String.valueOf(accountType), statusMap, "", oldUser);

				//΢���û����е�userIdȡ����Աid
				Long personId = 0L;
				Long userId = 0L;//�û�id
				if(user != null){
					userId = user.getUserId();
					personId = user.getPersonId();
				}
				//���뻥����Ϣ����ʱ��
				LinksusTaskInteractGovMid linksusInteractGov = new LinksusTaskInteractGovMid();
				linksusInteractGov.setRpsId(new Long(rpsId));//΢���û���ʶ
				linksusInteractGov.setUserId(userId);
				linksusInteractGov.setPersonId(personId);
				linksusInteractGov.setAccountId(accountId);//ƽ̨�˺�id
				linksusInteractGov.setCommentId(commentId);//��ǰ���۵�id
				linksusInteractGov.setReplyId(StringUtils.isBlank(replyid) ? 0L : new Long(replyid));//�ظ�����id
				linksusInteractGov.setMid(weiboId);//΢��id
				linksusInteractGov.setContent(content);//����:ת��/@/��������
				if(content.startsWith("�ظ�@" + weiboMap.get("name") + "")){
					linksusInteractGov.setInteractType(4);//��������
				}else{
					linksusInteractGov.setInteractType(1);//��������	
				}
				linksusInteractGov.setInteractTime(createTimeInteger);//����ʱ��
				linksusTaskInteractGovMidService.insertLinksusTaskInteractGovMid(linksusInteractGov);

				//batchList.add(linksusInteractGov);
				//					try{
				//					linksusTaskInteractGovMidService.insertLinksusTaskInteractGovMid(linksusInteractGov);
				//					}catch(Exception e){
				//						e.printStackTrace();
				//					}
			}
			//linksusTaskInteractGovMidService.saveLinksusInteractGovMid(batchList, Constants.OPER_TYPE_INSERT);
			if(commentsList.size() < count){//û����һҳ����
				break;
			}
			currentPage++;
		}
		if(!StringUtils.isBlank(maxId)){//���»���������е����maxid
			updateGovTaskInteract(accountId, 1, Long.valueOf(maxId));
		}
	}

	//���ԭ΢���Ƿ����
	private void checkOldWeiboIsExsit(String strToken,Long pmid,String srcMid,LinksusRelationWeibouser user){
		if(pmid != 0){
			//�ж�΢���Ƿ����
			//	        Map weiboPoolMap =new HashMap();
			//			weiboPoolMap.put("mid",pmid);
			//			weiboPoolMap.put("weiboType",accountType);
			//			LinksusWeiboPool weiboCount = linksusWeiboPoolService.checkWeiboPoolIsExsit(weiboPoolMap);
			String weiboRedis = RedisUtil.getRedisHash("weibo_pool", pmid + "#" + accountType);
			//�����ݷ��͵����˽ӿ� ��ȡ���� 
			UrlEntity transInter = LoadConfig.getUrlEntity("InteractDataSyncTrans");
			Map params = new HashMap();
			params.put("access_token", strToken);
			params.put("id", pmid);
			String strResult = HttpUtil.getRequest(transInter, params);
			logger.debug(">>>>>>>>>weibo - Sina={}", strResult);
			// ����д�������
			LinksusTaskErrorCode error = StringUtil.parseSinaErrorCode(strResult);
			if(error != null){
				return;
			}else{//����΢�����е� ����
				Map weiboMap = (Map) JsonUtil.json2map(strResult, Map.class);
				if(user != null){
					LinksusWeiboPool weiboPool = new LinksusWeiboPool();
					if(!StringUtils.isBlank(weiboRedis)){//���ڸ���
						weiboPool.setMid(new Long(pmid));
						weiboPool.setWeiboType(Integer.valueOf(accountType));
						weiboPool.setRepostCount(weiboMap.get("reposts_count") == null ? 0 : (Integer) weiboMap
								.get("reposts_count"));
						weiboPool.setCommentCount(weiboMap.get("comments_count") == null ? 0 : (Integer) weiboMap
								.get("comments_count"));
						linksusWeiboPoolService.updateLinksusWeiboPoolDataCount(weiboPool);
					}else{//�����ڲ���
						weiboPool.setMid(new Long(pmid));
						copySinaDataToWeiboPool(weiboMap, weiboPool);
						//�û�id
						weiboPool.setUid(user.getUserId());
						//�û�����
						weiboPool.setUname(user.getRpsScreenName());
						//�û�ͷ��
						weiboPool.setUprofileUrl(user.getRpsProfileImageUrl());
						weiboPool.setWeiboType(Integer.valueOf(accountType));
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
				String currUrl = "http://weibo.com/" + user.getRpsId() + "/" + WeiboUtil.Id2Mid(new Long(mid));
				weiboPool.setCurrentUrl(currUrl);
				//ԭid
				long srcMidL = 0l;
				if(StringUtils.isNotBlank(srcMid)){
					srcMidL = new Long(srcMid);
				}
				weiboPool.setSrcMid(srcMidL);
				QueueDataSave.addDataToQueue(weiboPool, Constants.OPER_TYPE_INSERT);
				//linksusWeiboPoolService.insertLinksusWeiboPool(weiboPool);
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
		//weiboUser.dealWeiboUserInfo(userStr, "2", parmMap, false, "3", interactBit);
		LinksusRelationWeibouser user = weiboUser.dealWeiboUserInfo(userStr, "2", parmMap, false, updateType,
				interactBit);
		//΢���û�����ȡ���û���userId 
		//String rpsId = String.valueOf(userMap.get("idstr"));
		//����rpsId��accountType �ж��û��Ƿ����
		//		Map paraMap = new HashMap();
		//		paraMap.put("rpsId", rpsId);
		//		paraMap.put("userType", accountType);
		//		LinksusRelationWeibouser user = relationWeiboUserService.getWeibouserByrpsIdAndType(paraMap);
		return user;
	}

	//�����������ݵ�΢���ر�
	public void copySinaDataToWeiboPool(Map weiboMap,LinksusWeiboPool weiboPool){
		//΢��id
		weiboPool.setMid(new Long(weiboMap.get("id") + ""));
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

	private void dealDuoWen(Integer gid){
		/**
		 * ����������������
		 * 1������ ������ͬʱ@����ʺţ�
		 *    ������ֶ��ʣ��򽫸�����Ϣָ����֯���ڵ㣨����������
		 *    select a.* from linksus_task_interact_gov_mid  a inner join (
				 select  mid, count(account_id) as cnum from linksus_task_interact_gov_mid  group by mid  having cnum>1 
				) b on a.mid=b.mid
		       
		                              ������ɺ󣬽�����ɾ��
		 */
		//ȡ�ö��ʵ�����
		List<Long> midList = linksusTaskInteractGovMidService.getDuoWenInteractGovMid();
		if(midList != null && midList.size() > 0){
			for(int i = 0; i < midList.size(); i++){
				LinksusTaskInteractGovMid interactMid = linksusTaskInteractGovMidService
						.getLinksusTaskInteractGovMidListByMid(midList.get(i));
				if(interactMid != null){
					Long runId = PrimaryKeyGen.GenerateSerialNum();
					//Integer gid=orgMap.get(new Integer(interactMid.getAccountId().intValue()));
					gid = Constants.ROOTORGID;
					// ���浽 linksus_gov_running
					saveGovRunning(interactMid, runId, gid, Constants.DEFAULTORG, 1);
					// ���浽 linksus_gov_interact	
					try{
						saveGovInteract(interactMid, runId);
					}catch (Exception e){
						LogUtil.saveException(logger, e);
					}
					// ����IDɾ����ʱ���е�����
					linksusTaskInteractGovMidService.deleteLinkSusTaskInteractGovMidByMid(interactMid.getMid());
				}
			}
		}
	}

	/**
	 * ���浽����״̬��ʱ��
	 */
	private LinksusGovRunning saveGovRunning(LinksusTaskInteractGovMid interactMid,Long runId,Integer gid,
			Integer orgId,Integer isMultiterm){
		LinksusGovRunning govRunning = new LinksusGovRunning();
		govRunning.setRunId(runId);
		govRunning.setGid(gid);
		govRunning.setCreateTime(Integer.valueOf(String.valueOf(new Date().getTime() / 1000)));
		govRunning.setIsFinish(0);
		govRunning.setOrgId(orgId);
		govRunning.setIsMultiterm(isMultiterm);
		//��������:1 ���� 2 ת�� 3 @ 4 ���۲�@  5���۲�ת�� 6 ˽�� 7 ƽ̨�˻��ظ�
		govRunning.setInteractType(interactMid.getInteractType());
		govRunning.setInteractMode(1);//������ʽ�� 1 ΢�� 2 ˽��
		govRunning.setUserId(interactMid.getUserId());
		govRunning.setUpdateTime(DateUtil.getUnixDate(new Date()));
		linksusGovRunningService.insertLinksusGovRunning(govRunning);
		return govRunning;
	}

	/**
	 * ���浽��ʽ��
	 */
	private LinksusGovInteract saveGovInteract(LinksusTaskInteractGovMid interactMid,Long runId) throws Exception{
		LinksusGovInteract newLinksusGovInteract = new LinksusGovInteract();
		Long interactId = PrimaryKeyGen.getPrimaryKey("linksus_gov_interact", "interact_id");
		newLinksusGovInteract.setInteractId(interactId);
		newLinksusGovInteract.setRunId(runId);
		newLinksusGovInteract.setUserId(interactMid.getUserId());
		newLinksusGovInteract.setAccountId(interactMid.getAccountId());
		newLinksusGovInteract.setCommentId(interactMid.getCommentId());
		newLinksusGovInteract.setWeiboId(interactMid.getMid());
		newLinksusGovInteract.setSourceWeiboId(interactMid.getReplyId());
		newLinksusGovInteract.setContent(interactMid.getContent());
		newLinksusGovInteract.setInteractType(interactMid.getInteractType());
		newLinksusGovInteract.setSendType(new Integer(0));
		newLinksusGovInteract.setStatus(new Integer(0));
		newLinksusGovInteract.setSendTime(0);
		newLinksusGovInteract.setInteractTime(interactMid.getInteractTime());
		newLinksusGovInteract.setReplyCommentId(0L);
		linksusGovInteractService.insertLinksusGovInteract(newLinksusGovInteract);
		return newLinksusGovInteract;
	}

	/**
	 * ���浽�м���ʱ��
	 */
	private LinksusTaskInteractGovMid saveGovInteractMid(Long rpsId,Long userId,Long personId,Long accountId,
			Long commentid,String replyid,Long mid,String text,int interactType,int createTimeInteger){
		LinksusTaskInteractGovMid linksusInteractGov = new LinksusTaskInteractGovMid();
		linksusInteractGov.setRpsId(rpsId);//΢���û���ʶ
		linksusInteractGov.setUserId(userId);
		linksusInteractGov.setPersonId(personId);
		linksusInteractGov.setAccountId(accountId);//ƽ̨�˺�id
		linksusInteractGov.setCommentId(new Long(commentid));//��ǰ���۵�id
		linksusInteractGov.setReplyId(StringUtils.isBlank(replyid) ? 0L : new Long(replyid));//�ظ�����id
		linksusInteractGov.setMid(mid);//΢��id
		linksusInteractGov.setContent(text);//����:ת��/@/��������
		linksusInteractGov.setInteractType(interactType);//��������
		linksusInteractGov.setInteractTime(createTimeInteger);//����ʱ��
		linksusTaskInteractGovMidService.insertLinksusTaskInteractGovMid(linksusInteractGov);
		return linksusInteractGov;
	}

	/**
	 * ���浽΢�����������¼��
	 */
	private void saveGovTaskInteract(Long accountId,int interactType,Long maxId){
		LinksusTaskGovInteract newTaskWeiboInteract = new LinksusTaskGovInteract();
		newTaskWeiboInteract.setAccountId(accountId);
		newTaskWeiboInteract.setInteractType(interactType);
		newTaskWeiboInteract.setMaxId(Long.valueOf(maxId));
		newTaskWeiboInteract.setPagetime(0L);
		linksusTaskGovInteractService.insertLinksusTaskGovInteract(newTaskWeiboInteract);
	}

	/**
	 * ����΢�����������¼��
	 */
	private void updateGovTaskInteract(Long accountId,int interactType,Long maxId){
		LinksusTaskGovInteract linksusTaskGov = new LinksusTaskGovInteract();
		linksusTaskGov.setAccountId(accountId);
		linksusTaskGov.setInteractType(interactType);
		linksusTaskGov.setMaxId(new Long(maxId));
		linksusTaskGov.setPagetime(new Long(0));
		linksusTaskGovInteractService.updateLinksusTaskGovInteract(linksusTaskGov);
	}

	/**
	 * �������۲�ת��
	 * ������¼����¼A��mid=��¼B��reply_id ���� A���ʺ�Id��rps_id,interact_time ����ͬ
	 * �ϲ�ԭ��
	 * SELECT a.* from linksus_task_interact_gov_mid a inner join linksus_task_interact_gov_mid b
	   on a.reply_id=b.mid  and a.account_id=b.account_id and a.rps_id=b.rps_id and a.interact_time=b.interact_time
	 *  ɾ�����е�һ������
	 */
	private void dealCommentAndRelay(){
		List<LinksusTaskInteractGovMid> list = linksusTaskInteractGovMidService.getLinksusCommentAndRelayGovMidList();
		if(list != null && list.size() > 0){
			for(Iterator<LinksusTaskInteractGovMid> iter = list.iterator(); iter.hasNext();){
				LinksusTaskInteractGovMid govMid = iter.next();
				LinksusTaskInteractGovMid taskGovMid = new LinksusTaskInteractGovMid();
				taskGovMid.setMid(govMid.getReplyId());
				taskGovMid.setCommentId(govMid.getReplyCommentId());
				taskGovMid.setAccountId(govMid.getAccountId());
				//���û�������Ϊ:���۲�ת��
				taskGovMid.setInteractType(5);
				linksusTaskInteractGovMidService.deleteLinksusTaskInteractGovMidById(govMid);
				linksusTaskInteractGovMidService.updateInteractType(taskGovMid);

			}
		}
	}
}
