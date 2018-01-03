package com.linksus.task;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.linksus.common.Constants;
import com.linksus.common.config.LoadConfig;
import com.linksus.common.util.CacheUtil;
import com.linksus.common.util.DateUtil;
import com.linksus.common.util.HttpUtil;
import com.linksus.common.util.LogUtil;
import com.linksus.common.util.PrimaryKeyGen;
import com.linksus.common.util.StringUtil;
import com.linksus.data.QueueDataSave;
import com.linksus.entity.AttentionRelationEntrity;
import com.linksus.entity.LinksusRelationOperate;
import com.linksus.entity.LinksusRelationOperateItem;
import com.linksus.entity.LinksusRelationUserAccount;
import com.linksus.entity.LinksusTaskErrorCode;
import com.linksus.entity.LinksusTaskInvalidRecord;
import com.linksus.entity.UrlEntity;

/**
 * ���ӹ�ע
 */
public class TaskAddAttention extends BaseTask{

	public String addAttentionTask(){
		return null;
	}

	public static void main(String[] args){
		TaskAddAttention t = new TaskAddAttention();
		t.cal();
	}

	@Override
	public void cal(){
		try{
			String accountType = String.valueOf(this.accountType);
			Map paraMap = new HashMap();
			paraMap.put("appType", accountType);
			paraMap.put("taskType", "1");
			paraMap.put("pageSize", pageSize);
			int startCount = (currentPage - 1) * pageSize;
			paraMap.put("startCount", startCount);
			List<AttentionRelationEntrity> attentionList = attentionService.getAttentionRelationEntrityList(paraMap);
			if(StringUtils.equals(accountType, "1")){
				dealDataFromSina(attentionList);
			}else if(StringUtils.equals(accountType, "2")){
				dealDataFromTencent(attentionList);
			}
			checkTaskListEnd(attentionList);

		}catch (Exception e){
			LogUtil.saveException(logger, e);
			e.printStackTrace();
		}
	}

	//ǰ̨�ӿڵ��÷���
	public Map addAttentionInterface(Map paraMap) throws Exception{
		Map rsMsg = new HashMap();
		String pid = (String) paraMap.get("pid");
		//�жϹ�ϵ���д��ڲ����� ������������ӹ�ע��������б���
		LinksusRelationOperate relaOperate = relationOperateService.checkIsExsitRelationOperate(pid);
		if(relaOperate != null){
			//��ȡ��ӹ�ע������
			String accountType = relaOperate.getAccountType().toString();
			List<AttentionRelationEntrity> attentionList = attentionService.getRelationAttentionTask(paraMap);
			if(attentionList == null && attentionList.size() == 0){
				rsMsg.put("errcode", "10015");//������û������Ҫִ��
				return rsMsg;
			}
			if(StringUtils.equals(accountType, "1")){
				dealDataFromSina(attentionList);
			}else if(StringUtils.equals(accountType, "2")){
				dealDataFromTencent(attentionList);
			}
			//��ѯpid�ĳɹ�������ʧ�ܴ���
			LinksusRelationOperate resultOperate = relationOperateService.checkIsExsitRelationOperate(pid);
			rsMsg.put("succNum", resultOperate.getOperSuccessNum());
			rsMsg.put("failNum", resultOperate.getOperFailNum());
			rsMsg.put("countNum", attentionList.size());
		}else{
			rsMsg.put("errcode", "10110");//������δͨ�������򲻴���
		}
		return rsMsg;
	}

	//������Ѷ���͵��û�
	public void dealDataFromTencent(List<AttentionRelationEntrity> attentionList) throws Exception{
		CacheUtil cache = CacheUtil.getInstance();
		UrlEntity strUrl = LoadConfig.getUrlEntity("TCaddAttention");
		String returnData = "";
		Map paraMap = new HashMap();
		paraMap.put("oauth_consumer_key", cache.getAppInfo(Constants.CACHE_TENCENT_APPINFO).getAppKey());
		paraMap.put("clientip", Constants.TENCENT_CLIENT_IP);
		paraMap.put("oauth_version", "2.a");
		paraMap.put("format", "json");
		for(int i = 0; i < attentionList.size(); i++){
			AttentionRelationEntrity addAttention = attentionList.get(i);
			try{
				String strToken = addAttention.getToken();
				String openid = addAttention.getOpenId();
				String rpsId = addAttention.getRpsId();
				// ͨ��post��ʽ����õ��ַ���
				paraMap.put("openid", openid);
				paraMap.put("access_token", strToken);
				paraMap.put("fopenids", rpsId);
				returnData = HttpUtil.postRequest(strUrl, paraMap);
				LinksusTaskErrorCode error = StringUtil.parseTencentErrorCode(returnData);
				if(error != null && error.getErrorCode() != 50380){
					LinksusRelationOperateItem item = new LinksusRelationOperateItem();
					item.setOperateId(addAttention.getPid());
					item.setUserId(addAttention.getUserId());
					item.setTaskStatus(2L);
					relationOperateItemService.updateLinksusRelationOperateItem(item);
					relationOperateService.updateLinksusRelationOperFailNum(addAttention.getPid());
					//������Ч��¼��
					LinksusTaskInvalidRecord invalidRecord = new LinksusTaskInvalidRecord();
					invalidRecord.setErrorCode(error.getErrorCode().toString());
					invalidRecord.setErrorMsg(error.getErrorMsg());
					invalidRecord.setRecordId(addAttention.getPid());
					invalidRecord.setOperType(Constants.INVALID_RECORD_ADD_ATTENTION);
					invalidRecord.setAddTime(DateUtil.getUnixDate(new Date()));
					invalidRecord.setInstitutionId(addAttention.getInstitutionId());
					BaseTask.dealInvalidRecord(invalidRecord);
					continue;
				}else{
					//���¹�ϵ������
					LinksusRelationOperateItem opItem = new LinksusRelationOperateItem();
					opItem.setOperateId(addAttention.getPid());
					opItem.setUserId(addAttention.getUserId());
					opItem.setTaskStatus(3L);
					relationOperateItemService.updateLinksusRelationOperateItem(opItem);
					relationOperateService.updateLinksusRelationOperSuccessNum(addAttention.getPid());
				}
				addAttentionRelation(addAttention.getInstitutionId(), addAttention.getAccountId(), addAttention
						.getUserId(), addAttention.getPersonId(), addAttention.getAccountType());
			}catch (Exception e){
				LogUtil.saveException(logger, e);
				LinksusTaskInvalidRecord invalidRecord = new LinksusTaskInvalidRecord();
				invalidRecord.setErrorCode(Constants.INVALID_RECORD_EXCEPTION);
				invalidRecord.setErrorMsg(LogUtil.getExceptionStackMsg(e));
				invalidRecord.setRecordId(addAttention.getPid());
				invalidRecord.setOperType(Constants.INVALID_RECORD_ADD_ATTENTION);
				invalidRecord.setAddTime(DateUtil.getUnixDate(new Date()));
				invalidRecord.setInstitutionId(addAttention.getInstitutionId());
				BaseTask.dealInvalidRecord(invalidRecord);
			}
		}
	}

	//�����������͵��û�
	public void dealDataFromSina(List<AttentionRelationEntrity> attentionList) throws Exception{
		CacheUtil cache = CacheUtil.getInstance();
		UrlEntity strUrl = LoadConfig.getUrlEntity("TaskFollows");
		String returnData = "";
		for(int i = 0; i < attentionList.size(); i++){
			AttentionRelationEntrity addAttention = attentionList.get(i);
			try{
				int hcount1 = cache.getCurrHourWriteCountByUser(addAttention.getUserId().toString(),
						Constants.LIMIT_ADD_FOLLOWER_PER_HOUR);
				int hcount2 = cache.getCurrDayWriteCountByUser(addAttention.getUserId().toString(),
						Constants.LIMIT_ADD_FOLLOWER_PER_DAY);
				if(hcount1 == -1 || hcount2 == -1){//��������
					continue;
				}
				String strToken = addAttention.getToken();
				// post���������ֵ
				String uid = addAttention.getRpsId();
				// ͨ��post��ʽ����õ��ַ���
				Map paramMap = new HashMap();
				paramMap.put("access_token", strToken);
				paramMap.put("uid", uid);
				returnData = HttpUtil.postRequest(strUrl, paramMap);
				LinksusTaskErrorCode error = StringUtil.parseSinaErrorCode(returnData);
				if(error != null){
					//���²��������״̬
					LinksusRelationOperateItem item = new LinksusRelationOperateItem();
					item.setOperateId(addAttention.getPid());
					item.setUserId(addAttention.getUserId());
					item.setTaskStatus(2L);
					relationOperateItemService.updateLinksusRelationOperateItem(item);
					relationOperateService.updateLinksusRelationOperFailNum(addAttention.getPid());
					//������Ч��¼��
					LinksusTaskInvalidRecord invalidRecord = new LinksusTaskInvalidRecord();
					invalidRecord.setErrorCode(error.getErrorCode().toString());
					invalidRecord.setErrorMsg(error.getErrorMsg());
					invalidRecord.setRecordId(addAttention.getPid());
					invalidRecord.setOperType(Constants.INVALID_RECORD_ADD_ATTENTION);
					invalidRecord.setAddTime(DateUtil.getUnixDate(new Date()));
					invalidRecord.setInstitutionId(addAttention.getInstitutionId());
					BaseTask.dealInvalidRecord(invalidRecord);
					continue;
				}else{
					cache.addCurrDayWriteCountByUser(addAttention.getUserId().toString(),
							Constants.LIMIT_ADD_FOLLOWER_PER_DAY);
					cache.addCurrHourWriteCountByUser(addAttention.getUserId().toString(),
							Constants.LIMIT_ADD_FOLLOWER_PER_HOUR);
					LinksusRelationOperateItem operateItem = new LinksusRelationOperateItem();
					operateItem.setOperateId(addAttention.getPid());
					operateItem.setUserId(addAttention.getUserId());
					operateItem.setTaskStatus(3L);
					relationOperateItemService.updateLinksusRelationOperateItem(operateItem);
					relationOperateService.updateLinksusRelationOperSuccessNum(addAttention.getPid());
				}
				addAttentionRelation(addAttention.getInstitutionId(), addAttention.getAccountId(), addAttention
						.getUserId(), addAttention.getPersonId(), addAttention.getAccountType());
			}catch (Exception e){
				LinksusTaskInvalidRecord invalidRecord = new LinksusTaskInvalidRecord();
				invalidRecord.setErrorCode(Constants.INVALID_RECORD_EXCEPTION);
				invalidRecord.setErrorMsg(LogUtil.getExceptionStackMsg(e));
				invalidRecord.setRecordId(addAttention.getPid());
				invalidRecord.setOperType(Constants.INVALID_RECORD_ADD_ATTENTION);
				invalidRecord.setAddTime(DateUtil.getUnixDate(new Date()));
				invalidRecord.setInstitutionId(addAttention.getInstitutionId());
				BaseTask.dealInvalidRecord(invalidRecord);
			}
		}
	}

	private void addAttentionRelation(Long institutionId,Long accountId,Long userId,Long personId,Integer accountType)
			throws Exception{
		//�ж��û���ϵ���Ƿ���� �����ڲ�����ڸ���
		Map pamap = new HashMap();
		pamap.put("userId", userId);
		pamap.put("accountId", accountId);
		pamap.put("accountType", accountType);
		LinksusRelationUserAccount userAccount = relationUserAccountService.getRelationUser(pamap);
		if(userAccount == null){//��ϵ����û��ִ�в���
			LinksusRelationUserAccount reuserAccount = new LinksusRelationUserAccount();
			//��ȡ����
			Long id = PrimaryKeyGen.getPrimaryKey("linksus_relation_user_account", "pid");
			reuserAccount.setPid(id);
			reuserAccount.setAccountId(accountId);
			reuserAccount.setAccountType(accountType);
			reuserAccount.setInstitutionId(institutionId == null ? 0L : institutionId);
			reuserAccount.setUserId(userId);
			reuserAccount.setPersonId(personId);
			reuserAccount.setFlagRelation(2);
			reuserAccount.setUptime(Integer.parseInt(String.valueOf(new Date().getTime() / 1000)));
			reuserAccount.setInteractType("000000");
			// relationUserAccountService.insertLinksusRelationUserAccount(reuserAccount);
			QueueDataSave.addDataToQueue(reuserAccount, Constants.OPER_TYPE_INSERT);
		}else{
			if(userAccount.getFlagRelation() == 1){
				userAccount.setFlagRelation(3);
				//relationUserAccountService.updateRelationUserAccount(userAccount);
			}else{
				userAccount.setFlagRelation(2);
				//relationUserAccountService.updateRelationUserAccount(userAccount);
			}
			QueueDataSave.addDataToQueue(userAccount, Constants.OPER_TYPE_UPDATE);
		}
	}
}
