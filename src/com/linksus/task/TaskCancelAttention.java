package com.linksus.task;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.linksus.common.Constants;
import com.linksus.common.config.LoadConfig;
import com.linksus.common.util.CacheUtil;
import com.linksus.common.util.ContextUtil;
import com.linksus.common.util.DateUtil;
import com.linksus.common.util.HttpUtil;
import com.linksus.common.util.LogUtil;
import com.linksus.common.util.RedisUtil;
import com.linksus.common.util.StringUtil;
import com.linksus.data.QueueDataSave;
import com.linksus.entity.AttentionRelationEntrity;
import com.linksus.entity.LinksusRelationOperate;
import com.linksus.entity.LinksusRelationOperateItem;
import com.linksus.entity.LinksusRelationUserAccount;
import com.linksus.entity.LinksusRelationWeibouser;
import com.linksus.entity.LinksusTaskErrorCode;
import com.linksus.entity.LinksusTaskInvalidRecord;
import com.linksus.entity.UrlEntity;
import com.linksus.service.LinksusRelationPersonGroupService;
import com.linksus.service.LinksusRelationWeibouserService;

/**
 * ������˿����
 */
public class TaskCancelAttention extends BaseTask{

	private static LinksusRelationWeibouserService linksusRelationWeibouserService = (LinksusRelationWeibouserService) ContextUtil
			.getBean("linksusRelationWeibouserService");
	private static LinksusRelationPersonGroupService linksusRelationPersonGroupService = (LinksusRelationPersonGroupService) ContextUtil
			.getBean("linksusRelationPersonGroupService");

	public static void main(String[] args){
		TaskCancelAttention tt = new TaskCancelAttention();
		tt.cal();
	}

	@Override
	public void cal(){
		try{
			// ȡ����ע����
			String accountType = String.valueOf(this.accountType);
			Map paraMap = new HashMap();
			paraMap.put("appType", accountType);
			paraMap.put("taskType", "2");
			paraMap.put("pageSize", pageSize);
			int startCount = (currentPage - 1) * pageSize;
			paraMap.put("startCount", startCount);
			List<AttentionRelationEntrity> attentionList = attentionService.getAttentionRelationEntrityList(paraMap);
			if(StringUtils.equals(accountType, "1")){
				dealCancelDataFromSina(attentionList);
			}else if(StringUtils.equals(accountType, "2")){
				dealCancelDataFromTentent(attentionList);
			}
			checkTaskListEnd(attentionList);
		}catch (Exception e){
			LogUtil.saveException(logger, e);
			e.printStackTrace();
		}
	}

	// ǰ̨�ӿڵ��÷���
	public Map cancelAttentionInterface(Map paraMap) throws Exception{
		Map rsMsg = new HashMap();
		String pid = (String) paraMap.get("pid");
		LinksusRelationOperate relaOperate = relationOperateService.checkIsExsitRelationOperate(pid);
		if(relaOperate != null){
			String accountType = relaOperate.getAccountType().toString();
			List<AttentionRelationEntrity> attentionList = attentionService.getRelationAttentionTask(paraMap);
			if(attentionList.size() == 0){
				rsMsg.put("errcode", "10015");//������û������Ҫִ��
				return rsMsg;
			}
			if(StringUtils.equals(accountType, "1")){
				dealCancelDataFromSina(attentionList);
			}else if(StringUtils.equals(accountType, "2")){
				dealCancelDataFromTentent(attentionList);
			}
			//��ѯpid�ĳɹ�������ʧ�ܴ���
			LinksusRelationOperate resultOperate = relationOperateService.checkIsExsitRelationOperate(pid);
			rsMsg.put("succNum", resultOperate.getOperSuccessNum());
			rsMsg.put("failNum", resultOperate.getOperFailNum());
			rsMsg.put("countNum", attentionList.size());
		}else{
			rsMsg.put("errcode", "10110");// ������δͨ�������򲻴���
		}
		return rsMsg;
	}

	// ������Ѷ���͵��û�
	public String dealCancelDataFromTentent(List<AttentionRelationEntrity> attentionList) throws Exception{
		CacheUtil cache = CacheUtil.getInstance();
		UrlEntity strUrl = LoadConfig.getUrlEntity("TCcancelAttention");
		String returnData = "";
		Map paraMap = new HashMap();
		paraMap.put("oauth_consumer_key", cache.getAppInfo(Constants.CACHE_TENCENT_APPINFO).getAppKey());
		paraMap.put("clientip", Constants.TENCENT_CLIENT_IP);
		paraMap.put("oauth_version", "2.a");
		paraMap.put("format", "json");
		for(int i = 0; i < attentionList.size(); i++){
			AttentionRelationEntrity cancleAttention = (AttentionRelationEntrity) attentionList.get(i);
			try{
				String strToken = cancleAttention.getToken();
				String openid = cancleAttention.getOpenId();
				// post���������ֵ
				String access_token = strToken;
				String rpsId = cancleAttention.getRpsId();
				// ͨ��post��ʽ����õ��ַ���
				paraMap.put("openid", openid);
				paraMap.put("access_token", strToken);
				paraMap.put("fopenid", rpsId);
				returnData = HttpUtil.postRequest(strUrl, paraMap);

				LinksusTaskErrorCode error = StringUtil.parseTencentErrorCode(returnData);
				if(error != null && error.getErrorCode() != 50370){
					LinksusRelationOperateItem item = new LinksusRelationOperateItem();
					item.setOperateId(cancleAttention.getPid());
					item.setUserId(cancleAttention.getUserId());
					item.setTaskStatus(2L);
					relationOperateItemService.updateLinksusRelationOperateItem(item);
					relationOperateService.updateLinksusRelationOperFailNum(cancleAttention.getPid());
					//������Ч��¼��
					LinksusTaskInvalidRecord invalidRecord = new LinksusTaskInvalidRecord();
					invalidRecord.setErrorCode(error.getErrorCode().toString());
					invalidRecord.setErrorMsg(error.getErrorMsg());
					invalidRecord.setRecordId(cancleAttention.getPid());
					invalidRecord.setOperType(Constants.INVALID_RECORD_CANCLE_ATTENTION);
					invalidRecord.setAddTime(DateUtil.getUnixDate(new Date()));
					invalidRecord.setInstitutionId(cancleAttention.getInstitutionId());
					dealInvalidRecord(invalidRecord);
					continue;
				}else{
					LinksusRelationOperateItem item = new LinksusRelationOperateItem();
					item.setOperateId(cancleAttention.getPid());
					item.setUserId(cancleAttention.getUserId());
					item.setTaskStatus(3L);
					relationOperateItemService.updateLinksusRelationOperateItem(item);
					relationOperateService.updateLinksusRelationOperSuccessNum(cancleAttention.getPid());
				}
				dealRelationUserAccountSingle(cancleAttention.getInstitutionId(), cancleAttention.getAccountId(),
						cancleAttention.getUserId(), "2");
			}catch (Exception e){
				LogUtil.saveException(logger, e);
				LinksusTaskInvalidRecord invalidRecord = new LinksusTaskInvalidRecord();
				invalidRecord.setErrorCode(Constants.INVALID_RECORD_EXCEPTION);
				invalidRecord.setErrorMsg(LogUtil.getExceptionStackMsg(e));
				invalidRecord.setRecordId(cancleAttention.getPid());
				invalidRecord.setOperType(Constants.INVALID_RECORD_CANCLE_ATTENTION);
				invalidRecord.setAddTime(DateUtil.getUnixDate(new Date()));
				invalidRecord.setInstitutionId(cancleAttention.getInstitutionId());
				dealInvalidRecord(invalidRecord);
			}
		}
		return "";
	}

	// �����������͵��û�
	public void dealCancelDataFromSina(List<AttentionRelationEntrity> attentionList) throws Exception{
		UrlEntity strUrl = LoadConfig.getUrlEntity("cancelAttention");
		String returnData = "";
		for(int i = 0; i < attentionList.size(); i++){
			AttentionRelationEntrity cancelAttention = (AttentionRelationEntrity) attentionList.get(i);
			try{
				String strToken = cancelAttention.getToken();
				String access_token = strToken;
				String uid = cancelAttention.getRpsId();
				Map paramMap = new HashMap();
				paramMap.put("access_token", access_token);
				paramMap.put("uid", uid);
				returnData = HttpUtil.postRequest(strUrl, paramMap);
				LinksusTaskErrorCode error = StringUtil.parseSinaErrorCode(returnData);
				if(error != null && !StringUtils.equals(error.getSrcCode(), "20522")){
					LinksusRelationOperateItem item = new LinksusRelationOperateItem();
					item.setOperateId(cancelAttention.getPid());
					item.setUserId(cancelAttention.getUserId());
					item.setTaskStatus(2L);
					relationOperateItemService.updateLinksusRelationOperateItem(item);
					relationOperateService.updateLinksusRelationOperFailNum(cancelAttention.getPid());
					//������Ч��¼��
					LinksusTaskInvalidRecord invalidRecord = new LinksusTaskInvalidRecord();
					invalidRecord.setErrorCode(error.getErrorCode().toString());
					invalidRecord.setErrorMsg(error.getErrorMsg());
					invalidRecord.setRecordId(cancelAttention.getPid());
					invalidRecord.setOperType(Constants.INVALID_RECORD_CANCLE_ATTENTION);
					invalidRecord.setAddTime(DateUtil.getUnixDate(new Date()));
					invalidRecord.setInstitutionId(cancelAttention.getInstitutionId());
					dealInvalidRecord(invalidRecord);
					continue;
				}else{
					LinksusRelationOperateItem operateItem = new LinksusRelationOperateItem();
					operateItem.setOperateId(cancelAttention.getPid());
					operateItem.setUserId(cancelAttention.getUserId());
					operateItem.setTaskStatus(new Long("3"));
					relationOperateItemService.updateLinksusRelationOperateItem(operateItem);
					relationOperateService.updateLinksusRelationOperSuccessNum(cancelAttention.getPid());
				}
				dealRelationUserAccountSingle(cancelAttention.getInstitutionId(), cancelAttention.getAccountId(),
						cancelAttention.getUserId(), "2");
			}catch (Exception e){
				LogUtil.saveException(logger, e);
				LinksusTaskInvalidRecord invalidRecord = new LinksusTaskInvalidRecord();
				invalidRecord.setErrorCode(Constants.INVALID_RECORD_EXCEPTION);
				invalidRecord.setErrorMsg(LogUtil.getExceptionStackMsg(e));
				invalidRecord.setRecordId(cancelAttention.getPid());
				invalidRecord.setOperType(Constants.INVALID_RECORD_CANCLE_ATTENTION);
				invalidRecord.setAddTime(DateUtil.getUnixDate(new Date()));
				invalidRecord.setInstitutionId(cancelAttention.getInstitutionId());
				dealInvalidRecord(invalidRecord);
			}
		}
	}

	/**
	 * �����û��˺Ź�ϵ  dealtype 1 ȥ����˿ 2 ȡ����ע
	 * @param institutionId 
	 * @param accountId
	 * @param userId
	 * @param dealtype
	 * @return
	 */
	public static String dealRelationUserAccountSingle(Long institutionId,Long accountId,Long userId,String dealtype){
		String relationValue = RedisUtil.getRedisHash("relation_user_account", accountId + "#" + userId);
		logger.error("������Ϣ:�˺�-{}#�û�-{},��ϵ{}", accountId, userId, relationValue);
		if(StringUtils.isBlank(relationValue)){
			return "fail";
		}else{
			String[] str = relationValue.split("#");
			String flagRelation = "0";
			String interactType = "000000";
			if(str != null && str.length >= 2){
				flagRelation = str[0];
				interactType = str[1];
			}
			LinksusRelationUserAccount linksusUserAccount = new LinksusRelationUserAccount();
			linksusUserAccount.setAccountId(accountId);
			linksusUserAccount.setUserId(userId);
			if("3".equals(flagRelation)){// ����Ϊ���۵Ĺ�ϵ
				// �޸���������
				if(dealtype.equals("1")){// �Ƴ���˿
					flagRelation = "2";
				}
				if(dealtype.equals("2")){// ȡ����עĳ�û�
					flagRelation = "1";
				}
				//���»����е�����
				linksusUserAccount.setFlagRelation(Integer.parseInt(flagRelation));
				linksusUserAccount.setInteractType(interactType);
				QueueDataSave.addDataToQueue(linksusUserAccount, Constants.OPER_TYPE_UPDATE);
			}else if("1".equals(flagRelation) || "2".equals(flagRelation)){// ���粻�ǻ��۵Ĺ�ϵ
				// �޸���������
				if(!"000000".equals(interactType)){// ���ڻ�����ϵ
					flagRelation = "4";// Ǳ�ڹ�ϵ
					linksusUserAccount.setFlagRelation(Integer.parseInt(flagRelation));
					linksusUserAccount.setInteractType(interactType);
					QueueDataSave.addDataToQueue(linksusUserAccount, Constants.OPER_TYPE_UPDATE);
				}else{
					//����userId��ѯ΢���û����е�personId
					LinksusRelationWeibouser linksusRelationWeibouser = linksusRelationWeibouserService
							.getRelationWeibouserByUserId(userId);
					Long personId = linksusRelationWeibouser.getPersonId();
					//ɾ�������е�������¼
					RedisUtil.delFieldKey("relation_user_account", accountId + "#" + userId);
					Map paraMap = new HashMap();
					paraMap.put("accountId", accountId);
					paraMap.put("userId", userId);
					logger.error("������Ϣ:�˺�-{}#�û�-{},��ϵ{}-->ɾ���û���ϵ", accountId, userId, flagRelation);
					relationUserAccountService.deleteLinksusRelationUserAccountByKey(paraMap);
					//�ж��û�������ϵ
					LinksusRelationUserAccount linkAccount = new LinksusRelationUserAccount();
					linkAccount.setUserId(userId);
					linkAccount.setInstitutionId(institutionId);
					Integer countAccount = relationUserAccountService.getCountLinksusRelationUserAccount(linkAccount);
					if(countAccount == 0){//���û��͸û����±���˺�û�й�ϵ ɾ����Ա����
						Map pasMap = new HashMap();
						pasMap.put("personId", personId);
						pasMap.put("institutionId", institutionId);
						linksusRelationPersonGroupService.deleteByHashMap(pasMap);
					}
				}
			}
		}
		return "success";
	}
}
