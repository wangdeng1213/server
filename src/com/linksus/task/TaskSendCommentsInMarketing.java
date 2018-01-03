package com.linksus.task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linksus.common.Constants;
import com.linksus.common.config.LoadConfig;
import com.linksus.common.util.ContextUtil;
import com.linksus.common.util.HttpUtil;
import com.linksus.common.util.JsonUtil;
import com.linksus.common.util.LogUtil;
import com.linksus.common.util.StringUtil;
import com.linksus.entity.LinksusAppaccount;
import com.linksus.entity.LinksusRelationMarketing;
import com.linksus.entity.LinksusRelationMarketingItem;
import com.linksus.entity.LinksusRelationWeibouser;
import com.linksus.entity.LinksusTaskErrorCode;
import com.linksus.entity.LinksusTaskInvalidRecord;
import com.linksus.entity.UrlEntity;
import com.linksus.service.LinksusAppaccountService;
import com.linksus.service.LinksusInstitutionService;
import com.linksus.service.LinksusRelationMarketingItemService;
import com.linksus.service.LinksusRelationMarketingService;
import com.linksus.service.LinksusRelationPersonInfoService;
import com.linksus.service.LinksusRelationWeibouserService;

public class TaskSendCommentsInMarketing extends BaseTask{

	protected static final Logger logger = LoggerFactory.getLogger(TaskSendCommentsInMarketing.class);

	LinksusRelationMarketingService linksusRelationMarketingService = (LinksusRelationMarketingService) ContextUtil
			.getBean("linksusRelationMarketingService");
	LinksusRelationMarketingItemService linksusRelationMarketingItemService = (LinksusRelationMarketingItemService) ContextUtil
			.getBean("linksusRelationMarketingItemService");
	LinksusAppaccountService linksusAppaccountService = (LinksusAppaccountService) ContextUtil
			.getBean("linksusAppaccountService");
	LinksusInstitutionService linksusInstitutionService = (LinksusInstitutionService) ContextUtil
			.getBean("linksusInstitutionService");
	LinksusRelationPersonInfoService linksusRelationPersonInfoService = (LinksusRelationPersonInfoService) ContextUtil
			.getBean("linksusRelationPersonInfoService");
	LinksusRelationWeibouserService linksusRelationWeibouserService = (LinksusRelationWeibouserService) ContextUtil
			.getBean("linksusRelationWeibouserService");

	public static final void main(String[] args){
		//		TaskSendCommentsInMarketing dd = new TaskSendCommentsInMarketing();
		//		dd.accountType = 2;
		//		dd.sendContentInMarketing();
		//		System.exit(0);
	}

	@Override
	public void cal(){
		sendContentInMarketing();
	}

	/**����Ӫ����������
	 * 
	 * marketingType: Ӫ������(1@,2����,3����,4�ʼ�)
	 * accountType: �ʺ�����:1 ���� 2 ��Ѷ 
	 */
	public void sendContentInMarketing(){

		//��ȡӪ���б��������б�
		LinksusRelationMarketing getMarketing = new LinksusRelationMarketing();
		getMarketing.setMarketingType(2);
		int startCount = (currentPage - 1) * pageSize;
		getMarketing.setPageSize(pageSize);
		getMarketing.setStartCount(startCount);
		getMarketing.setAccountType(accountType);
		//��ȡ�����˺�����Ӫ������
		List<LinksusRelationMarketing> sinaMarketings = linksusRelationMarketingService
				.getSendCommentsByUserList(getMarketing);
		for(int i = 0; i < sinaMarketings.size(); i++){
			LinksusRelationMarketing linksusRelationMarketing = sinaMarketings.get(i);
			if(linksusRelationMarketing != null){
				try{
					if(linksusRelationMarketing.getAccountType() == 1){
						//�����˺� ͨ��Ӫ���������Ӫ�������,��������
						sendSinaCommentsInMarketing(linksusRelationMarketing);
					}else if(linksusRelationMarketing.getAccountType() == 2){
						//��Ѷ�˺� ͨ��Ӫ���������Ӫ�������,��������
						sendTencentCommentsInMarketing(linksusRelationMarketing);
					}
				}catch (Exception e){
					LogUtil.saveException(logger, e);
				}

			}
		}
		checkTaskListEnd(sinaMarketings);//�ж������Ƿ���ѯ���
	}

	//�����˺ŷ�������
	public void sendSinaCommentsInMarketing(LinksusRelationMarketing linksusRelationMarketing){
		//��ȡ��ǰ����
		int successCount = linksusRelationMarketing.getMarketingSuccessNum();
		int failCount = linksusRelationMarketing.getMarketingFailNum();

		//��ȡ���������û�token
		LinksusAppaccount linksusAppaccount = linksusAppaccountService
				.getLinksusAppaccountTokenById(linksusRelationMarketing.getAccountId());
		//�жϸ��û�sinaд����
		int hcount = cache.getCurrHourWriteCountByUser(linksusRelationMarketing.getAccountId().toString(),
				Constants.LIMIT_SEND_COMMENT_PER_HOUR);
		if(hcount == -1){//��������
			return;
		}
		//����û�����
		if(linksusAppaccount != null){
			//��ȡӪ�������б�
			List<LinksusRelationMarketingItem> items = linksusRelationMarketingItemService
					.getItemsByMarketingID(linksusRelationMarketing.getPid());
			for(int i = 0; i < items.size(); i++){
				LinksusRelationMarketingItem item = items.get(i);
				int status = 2;
				try{
					//ͨ��Ӫ��������ȡӪ�����������ƽ̨id����Ȩ
					LinksusRelationWeibouser weiboUser = linksusRelationWeibouserService
							.getRelationWeibouserByUserId(item.getUserId());
					//Ӫ���������
					if(weiboUser != null){
						/**���۷������û�����һ��΢��*/
						//��ȡӪ�������ǰ5ҳ΢���б�
						Map mapGetPara = new HashMap();
						mapGetPara.put("access_token", linksusAppaccount.getToken());
						mapGetPara.put("count", 5);
						mapGetPara.put("trim_user", 0);
						mapGetPara.put("uid", weiboUser.getRpsId());

						String weiboData = HttpUtil.getRequest(LoadConfig.getUrlEntity("WeiBoData"), mapGetPara);
						LinksusTaskErrorCode error = StringUtil.parseSinaErrorCode(weiboData);
						if(StringUtils.isBlank(weiboData) || error != null){
							failCount++;
							item.setStatus(2);
							linksusRelationMarketingItemService.updateMarketingItemStatus(item);
							dealExceptionInvalidRecord(linksusAppaccount.getInstitutionId(), item.getPid(),
									new Exception("����δ��ȷ������������!"));
							continue;
						}
						//��ȡ������ȡ��������
						String statusesFromsina = JsonUtil.getNodeByName(weiboData, "statuses");
						List<Map> statusesList = (List<Map>) JsonUtil.json2list(statusesFromsina, Map.class);
						if(statusesList == null || statusesList.size() == 0){
							failCount++;
							item.setStatus(2);
							linksusRelationMarketingItemService.updateMarketingItemStatus(item);
							dealExceptionInvalidRecord(linksusAppaccount.getInstitutionId(), item.getPid(),
									new Exception("δ��ѯ������!"));
							continue;
						}

						//ȡ�÷��Ͷ��������΢��id
						String weiboId = (String) statusesList.get(0).get("idstr");
						//���΢��������
						Map mapPara = new HashMap();
						mapPara.put("access_token", linksusAppaccount.getToken());
						mapPara.put("comment", linksusRelationMarketing.getMarketingContent());
						mapPara.put("id", weiboId);
						String strResult = HttpUtil.postRequest(LoadConfig.getUrlEntity("replyWeibo"), mapPara);// �������������Ϣ
						error = StringUtil.parseSinaErrorCode(strResult);
						if(error != null){// ����sina���ش������
							failCount++;
							status = 2;
							dealErrorCodeInvalidRecord(linksusAppaccount.getInstitutionId(), item.getPid(), error
									.getErrorCode().toString(), strResult);
						}else{
							successCount++;
							status = 1;
							//�����û�sina�������۴���
							cache.addCurrHourWriteCountByUser(linksusRelationMarketing.getAccountId().toString(),
									Constants.LIMIT_SEND_COMMENT_PER_HOUR);
						}
						//����Ӫ�������ִ��״̬
						item.setStatus(status);
						linksusRelationMarketingItemService.updateMarketingItemStatus(item);
					}
				}catch (Exception e){
					LogUtil.saveException(logger, e);
					item.setStatus(2);
					linksusRelationMarketingItemService.updateMarketingItemStatus(item);
					dealExceptionInvalidRecord(linksusAppaccount.getInstitutionId(), item.getPid(), e);
				}
			}
			//����Ӫ������Ӫ������
			linksusRelationMarketing.setMarketingSuccessNum(successCount);
			linksusRelationMarketing.setMarketingFailNum(failCount);
			linksusRelationMarketingService.updateLinksysUserMarketing(linksusRelationMarketing);
		}
	}

	//��Ѷ�˺ŷ�������
	public void sendTencentCommentsInMarketing(LinksusRelationMarketing linksusRelationMarketing){
		//��ȡ��ǰ����
		int successCount = linksusRelationMarketing.getMarketingSuccessNum();
		int failCount = linksusRelationMarketing.getMarketingFailNum();
		//��ȡ���������û�token
		LinksusAppaccount linksusAppaccount = linksusAppaccountService
				.getLinksusAppaccountTokenById(linksusRelationMarketing.getAccountId());
		//����û�����
		if(linksusAppaccount != null){
			//��ȡӪ�������б�
			List<LinksusRelationMarketingItem> items = linksusRelationMarketingItemService
					.getItemsByMarketingID(linksusRelationMarketing.getPid());
			for(int i = 0; i < items.size(); i++){
				LinksusRelationMarketingItem item = items.get(i);
				//ͨ��Ӫ��������ȡӪ�����������ƽ̨id����Ȩ
				LinksusRelationWeibouser weiboUser = linksusRelationWeibouserService.getRelationWeibouserByUserId(item
						.getUserId());
				//Ӫ���������
				if(weiboUser != null){
					/**���۷������û�����һ��΢��*/
					UrlEntity strurl = LoadConfig.getUrlEntity("TCUserLastWeiboID");
					Map paraMap = new HashMap();
					paraMap.put("access_token", linksusAppaccount.getToken());
					paraMap.put("oauth_consumer_key", cache.getAppInfo(Constants.CACHE_TENCENT_APPINFO).getAppKey());
					paraMap.put("openid", linksusAppaccount.getAppid());
					paraMap.put("fopenid", weiboUser.getRpsId());
					paraMap.put("clientip", Constants.TENCENT_CLIENT_IP);
					paraMap.put("oauth_version", "2.a");
					paraMap.put("format", "json");
					paraMap.put("type", "3");
					paraMap.put("pageflag", 0);
					paraMap.put("pagetime", 0);
					paraMap.put("reqnum", 20);
					paraMap.put("lastid", 0);
					paraMap.put("contenttype", 0);
					String tencentRes = HttpUtil.getRequest(strurl, paraMap);
					LinksusTaskErrorCode error = StringUtil.parseTencentErrorCode(tencentRes);
					if(error != null){
						failCount++;
						item.setStatus(2);
						linksusRelationMarketingItemService.updateMarketingItemStatus(item);
						continue;
					}
					//��ȡ����Ѷȡ��������
					//У��data�Ƿ�Ϊ��
					boolean hasWeibo = true;
					String data = JsonUtil.getNodeByName(tencentRes, "data");
					if(StringUtils.isBlank(data) || StringUtils.equals(data, "null")){
						hasWeibo = false;
					}
					//У��info�Ƿ�Ϊ��
					String infoData = JsonUtil.getNodeByName(data, "info");
					if(StringUtils.isBlank(infoData) || StringUtils.equals(infoData, "null")){
						hasWeibo = false;
					}
					if(!hasWeibo){
						failCount++;
						item.setStatus(2);
						linksusRelationMarketingItemService.updateMarketingItemStatus(item);
						continue;
					}
					List<Map> statusesList = (List<Map>) JsonUtil.json2list(infoData, Map.class);
					if(statusesList == null || statusesList.size() == 0){
						failCount++;
						item.setStatus(2);
						linksusRelationMarketingItemService.updateMarketingItemStatus(item);
						continue;
					}

					//ȡ�÷��Ͷ��������΢��id
					String weiboId = (String) statusesList.get(0).get("id");
					//���΢��������
					Map mapPara = new HashMap();
					mapPara.put("oauth_consumer_key", cache.getAppInfo(Constants.CACHE_TENCENT_APPINFO).getAppKey());
					mapPara.put("openid", linksusAppaccount.getAppid());
					mapPara.put("clientip", Constants.TENCENT_CLIENT_IP);
					mapPara.put("access_token", linksusAppaccount.getToken());
					mapPara.put("oauth_version", "2.a");
					mapPara.put("format", "json");
					mapPara.put("content", linksusRelationMarketing.getMarketingContent());
					mapPara.put("reid", weiboId);
					String strResult = HttpUtil.postRequest(LoadConfig.getUrlEntity("TCreplyWeibo"), mapPara);// �������������Ϣ
					int status = 0;
					error = StringUtil.parseTencentErrorCode(strResult);
					if(error == null){
						successCount++;
						status = 1;
						//�����û�tencentд����
						//cache.addCurrHourWriteCountByUser(linksusRelationMarketing.getAccountId().toString(), Constants.LIMIT_TENCENT_WRITE_PER_HOUR);
					}else{
						failCount++;
						status = 2;
						dealErrorCodeInvalidRecord(linksusAppaccount.getInstitutionId(), item.getPid(), error
								.getErrorCode().toString(), error.getErrorMsg());
					}
					//����Ӫ�������ִ��״̬
					item.setStatus(status);
					linksusRelationMarketingItemService.updateMarketingItemStatus(item);
				}
			}
			//����Ӫ������Ӫ������
			linksusRelationMarketing.setMarketingSuccessNum(successCount);
			linksusRelationMarketing.setMarketingFailNum(failCount);
			linksusRelationMarketingService.updateLinksysUserMarketing(linksusRelationMarketing);
		}
	}

	private void dealExceptionInvalidRecord(Long institutionId,Long pid,Exception exception){
		try{
			LinksusTaskInvalidRecord invalidRecord = new LinksusTaskInvalidRecord();
			invalidRecord.setInstitutionId(institutionId);
			invalidRecord.setErrorCode("EXCEPTION");
			invalidRecord.setErrorMsg(LogUtil.getExceptionStackMsg(exception));
			invalidRecord.setOperType(Constants.INVALID_RECORD_MARKETING_COMMENT);
			invalidRecord.setRecordId(pid);
			dealInvalidRecord(invalidRecord);
		}catch (Exception e){
			LogUtil.saveException(logger, e);
		}
	}

	private void dealErrorCodeInvalidRecord(Long institutionId,Long pid,String errcode,String errMsg){
		try{
			LinksusTaskInvalidRecord invalidRecord = new LinksusTaskInvalidRecord();
			invalidRecord.setErrorCode(errcode);
			invalidRecord.setErrorMsg(errMsg);
			invalidRecord.setInstitutionId(institutionId);
			invalidRecord.setOperType(Constants.INVALID_RECORD_MARKETING_COMMENT);
			invalidRecord.setRecordId(pid);
			dealInvalidRecord(invalidRecord);
		}catch (Exception e){
			LogUtil.saveException(logger, e);
		}
	}

}
