package com.linksus.task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linksus.common.Constants;
import com.linksus.common.config.LoadConfig;
import com.linksus.common.util.ContextUtil;
import com.linksus.common.util.HttpUtil;
import com.linksus.common.util.LogUtil;
import com.linksus.common.util.StringUtil;
import com.linksus.entity.LinksusAppaccount;
import com.linksus.entity.LinksusRelationMarketing;
import com.linksus.entity.LinksusRelationMarketingItem;
import com.linksus.entity.LinksusTaskErrorCode;
import com.linksus.entity.LinksusTaskInvalidRecord;
import com.linksus.entity.UrlEntity;
import com.linksus.service.LinksusAppaccountService;
import com.linksus.service.LinksusRelationMarketingItemService;
import com.linksus.service.LinksusRelationMarketingService;
import com.linksus.service.LinksusRelationWeibouserService;

public class TaskSendMentionsInMarketing extends BaseTask{

	protected static final Logger logger = LoggerFactory.getLogger(TaskSendMentionsInMarketing.class);

	// ��ȡ��������·��
	String writeCommUrl = LoadConfig.getString("replyWeibo");

	// ��ȡ��Ѷ����·��
	UrlEntity writeContentCommUrl = LoadConfig.getUrlEntity("TCreplyWeibo");

	LinksusRelationMarketingService linksusRelationMarketingService = (LinksusRelationMarketingService) ContextUtil
			.getBean("linksusRelationMarketingService");
	LinksusRelationMarketingItemService linksusRelationMarketingItemService = (LinksusRelationMarketingItemService) ContextUtil
			.getBean("linksusRelationMarketingItemService");
	LinksusAppaccountService linksusAppaccountService = (LinksusAppaccountService) ContextUtil
			.getBean("linksusAppaccountService");
	LinksusRelationWeibouserService linksusRelationWeibouserService = (LinksusRelationWeibouserService) ContextUtil
			.getBean("linksusRelationWeibouserService");

	public static void main(String[] args){
		//TaskSendMentionsInMarketing aa = new TaskSendMentionsInMarketing();
		//aa.accountType = 1;
		//	aa.cal();
	}

	@Override
	public void cal(){
		sendContentInMarketing();
	}

	/**
	 * ����Ӫ����������
	 * 
	 * marketingType: Ӫ������(1@,2����,3����,4�ʼ�) accountType: �ʺ�����:1 ���� 2 ��Ѷ
	 */
	public void sendContentInMarketing(){

		// ��ȡӪ���б���@�б�
		LinksusRelationMarketing getMarketing = new LinksusRelationMarketing();
		getMarketing.setMarketingType(1);
		int startCount = (currentPage - 1) * pageSize;
		getMarketing.setPageSize(pageSize);
		getMarketing.setStartCount(startCount);
		getMarketing.setAccountType(accountType);
		// ��ȡ�����˺�@Ӫ������
		List<LinksusRelationMarketing> marketings = linksusRelationMarketingService
				.getSendCommentsByUserList(getMarketing);
		// ���������˺�Ӫ������
		for(int i = 0; i < marketings.size(); i++){
			LinksusRelationMarketing linksusRelationMarketing = marketings.get(i);
			if(linksusRelationMarketing != null){
				try{
					if(linksusRelationMarketing.getAccountType() == 1){
						// �����˺� ͨ��Ӫ���������Ӫ�������,��������
						sendSinaMentionsInMarketing(linksusRelationMarketing);
					}else if(linksusRelationMarketing.getAccountType() == 2){
						// ��Ѷ�˺� ͨ��Ӫ���������Ӫ�������,��������
						sendTencentMentionsInMarketing(linksusRelationMarketing);
					}
				}catch (Exception e){
					LogUtil.saveException(logger, e);
				}

			}
		}
		checkTaskListEnd(marketings);
	}

	// �����˺ŷ���@��������
	public void sendSinaMentionsInMarketing(LinksusRelationMarketing marketing){

		// ��ȡ����@���ݵ��û�
		long weiboId = marketing.getWeiboId();
		int status = 0;
		// ��ȡ���������û�
		LinksusAppaccount linksusAppaccount = linksusAppaccountService.getLinksusAppaccountTokenById(marketing
				.getAccountId());
		try{
			// �жϴ�΢���Ƿ����
			if(checkWeiboExists(weiboId, linksusAppaccount)){
				Map mapPara = new HashMap();
				mapPara.put("access_token", linksusAppaccount.getToken());
				mapPara.put("id", weiboId);
				String strResult = HttpUtil.getRequest(LoadConfig.getUrlEntity("WeiBoData"), mapPara);// �������������Ϣ
				LinksusTaskErrorCode error = StringUtil.parseSinaErrorCode(strResult);
				if(error != null){// ����sina���ش������)
					dealErrorCodeInvalidRecord(linksusAppaccount.getInstitutionId(), marketing.getPid(), error
							.getErrorCode().toString(), error.getErrorMsg());
					status = 5;
				}else{
					// ���΢��������
					mapPara = new HashMap();
					mapPara.put("access_token", linksusAppaccount.getToken());
					mapPara.put("comment", marketing.getMarketingContent());
					mapPara.put("id", weiboId);
					strResult = HttpUtil.postRequest(LoadConfig.getUrlEntity("replyWeibo"), mapPara);// �������������Ϣ
					error = StringUtil.parseSinaErrorCode(strResult);
					if(error != null){// ����sina���ش������
						dealErrorCodeInvalidRecord(linksusAppaccount.getInstitutionId(), marketing.getPid(), error
								.getErrorCode().toString(), error.getErrorMsg());
						status = 5;
					}else{
						status = 4;
						// �����û�sina�������۴���
						// cache.addCurrHourWriteCountByUser(linksusRelationMarketing.getAccountId().toString(),
						// Constants.LIMIT_SEND_COMMENT_PER_HOUR);
					}
				}
			}else{
				dealExceptionInvalidRecord(linksusAppaccount.getInstitutionId(), marketing.getWeiboId(), new Exception(
						"΢��������!"));
			}
		}catch (Exception e){
			LogUtil.saveException(logger, e);
			Long institutionId = 0l;
			if(linksusAppaccount != null){
				institutionId = linksusAppaccount.getInstitutionId();
			}
			dealExceptionInvalidRecord(institutionId, marketing.getWeiboId(), e);
		}
		// ����Ӫ������״̬
		//marketing.setStatus(status);
		//linksusRelationMarketingService.updateMentionsStatus(marketing);
		//�����ӱ�״̬
		LinksusRelationMarketingItem item = new LinksusRelationMarketingItem();
		item.setMarketingId(marketing.getPid());
		if(status == 4){
			item.setStatus(1);
		}else{
			item.setStatus(2);
		}
		linksusRelationMarketingItemService.updateMoreMarketingItemStatus(item);

	}

	// ��Ѷ�˺ŷ���@��������
	public void sendTencentMentionsInMarketing(LinksusRelationMarketing marketing){
		marketing.getMarketingSuccessNum();
		marketing.getMarketingFailNum();
		// ��ȡ����@���ݵ��û�
		long weiboId = marketing.getWeiboId();
		int status = 0;
		// ��ȡ���������û�
		LinksusAppaccount linksusAppaccount = linksusAppaccountService.getLinksusAppaccountTokenById(marketing
				.getAccountId());
		try{
			// �жϴ�΢���Ƿ����
			if(checkWeiboExists(weiboId, linksusAppaccount)){
				Map mapPara = new HashMap();
				mapPara.put("oauth_consumer_key", cache.getAppInfo(Constants.CACHE_TENCENT_APPINFO).getAppKey());
				mapPara.put("openid", linksusAppaccount.getAppid());
				mapPara.put("clientip", Constants.TENCENT_CLIENT_IP);
				mapPara.put("access_token", linksusAppaccount.getToken());
				mapPara.put("oauth_version", "2.a");
				mapPara.put("format", "json");
				mapPara.put("id", weiboId);
				String strResult = HttpUtil.getRequest(LoadConfig.getUrlEntity("TCWeiboByID"), mapPara);// �������������Ϣ
				LinksusTaskErrorCode error = StringUtil.parseTencentErrorCode(strResult);
				if(error == null){
					// ���΢��������
					mapPara = new HashMap();
					mapPara.put("oauth_consumer_key", cache.getAppInfo(Constants.CACHE_TENCENT_APPINFO).getAppKey());
					mapPara.put("openid", linksusAppaccount.getAppid());
					mapPara.put("clientip", Constants.TENCENT_CLIENT_IP);
					mapPara.put("access_token", linksusAppaccount.getToken());
					mapPara.put("oauth_version", "2.a");
					mapPara.put("format", "json");
					mapPara.put("content", marketing.getMarketingContent());
					mapPara.put("reid", weiboId);
					strResult = HttpUtil.postRequest(writeContentCommUrl, mapPara);// �������������Ϣ
					error = StringUtil.parseTencentErrorCode(strResult);
					if(error == null){
						status = 4;
						// �����û�tencentд����
						// cache.addCurrHourWriteCountByUser(linksusRelationMarketing.getAccountId().toString(),
						// Constants.LIMIT_TENCENT_WRITE_PER_HOUR);
					}else{
						status = 5;
						dealErrorCodeInvalidRecord(linksusAppaccount.getInstitutionId(), marketing.getPid(), error
								.getErrorCode().toString(), error.getErrorMsg());
					}
				}else{
					status = 5;
					dealExceptionInvalidRecord(linksusAppaccount.getInstitutionId(), marketing.getPid(), new Exception(
							"δ��ѯ������!"));
				}
			}else{
				dealExceptionInvalidRecord(linksusAppaccount.getInstitutionId(), marketing.getWeiboId(), new Exception(
						"΢��������!"));
			}
		}catch (Exception e){
			LogUtil.saveException(logger, e);
			Long institutionId = 0l;
			if(linksusAppaccount != null){
				institutionId = linksusAppaccount.getInstitutionId();
			}
			dealExceptionInvalidRecord(institutionId, marketing.getWeiboId(), e);
		}
		// ����Ӫ������״̬
		//		marketing.setStatus(status);
		//		linksusRelationMarketingService.updateMentionsStatus(marketing);
		//�����ӱ�״̬
		LinksusRelationMarketingItem item = new LinksusRelationMarketingItem();
		item.setMarketingId(marketing.getPid());
		if(status == 4){
			item.setStatus(1);
		}else{
			item.setStatus(2);
		}
		linksusRelationMarketingItemService.updateMoreMarketingItemStatus(item);
	}

	private void dealExceptionInvalidRecord(Long institutionId,Long pid,Exception exception){
		try{
			LinksusTaskInvalidRecord invalidRecord = new LinksusTaskInvalidRecord();
			invalidRecord.setInstitutionId(institutionId);
			invalidRecord.setErrorCode("EXCEPTION");
			invalidRecord.setErrorMsg(LogUtil.getExceptionStackMsg(exception));
			invalidRecord.setOperType(Constants.INVALID_RECORD_MARKETING_AT);
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
			invalidRecord.setOperType(Constants.INVALID_RECORD_MARKETING_AT);
			invalidRecord.setRecordId(pid);
			dealInvalidRecord(invalidRecord);
		}catch (Exception e){
			LogUtil.saveException(logger, e);
		}
	}

	/**
	 * �ж�΢���Ƿ����
	 * @param weiboId ΢��id
	 * @param authToken ��Ȩtoken
	 * @param type  ���� 1 ����  2 ��Ѷ
	 * @return boolean
	 */
	public boolean checkWeiboExists(Long weiboId,LinksusAppaccount linksusAppaccount){
		boolean isExists = false;
		if(linksusAppaccount.getType() == 1){//����
			UrlEntity strurl = LoadConfig.getUrlEntity("InteractDataSyncTrans");
			Map mapPara = new HashMap();
			mapPara.put("access_token", linksusAppaccount.getToken());
			mapPara.put("id", weiboId);
			String rsData = HttpUtil.getRequest(strurl, mapPara);
			LinksusTaskErrorCode error = StringUtil.parseSinaErrorCode(rsData);
			if(error == null){
				isExists = true;
			}
		}else if(linksusAppaccount.getType() == 2){//��Ѷ
			Map paraMap = new HashMap();
			paraMap.put("access_token", linksusAppaccount.getToken());
			paraMap.put("oauth_consumer_key", cache.getAppInfo(Constants.CACHE_TENCENT_APPINFO).getAppKey());
			paraMap.put("openid", linksusAppaccount.getAppid());
			paraMap.put("clientip", Constants.TENCENT_CLIENT_IP);
			paraMap.put("oauth_version", "2.a");
			paraMap.put("format", "json");
			paraMap.put("id", weiboId);
			UrlEntity strUrl = LoadConfig.getUrlEntity("TCWeiboByID");
			String rsData = HttpUtil.getRequest(strUrl, paraMap);
			LinksusTaskErrorCode error = StringUtil.parseTencentErrorCode(rsData);
			if(error == null){
				isExists = true;
			}
		}
		return isExists;
	}

}
