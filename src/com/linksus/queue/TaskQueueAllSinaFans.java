package com.linksus.queue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.linksus.common.Constants;
import com.linksus.common.module.RelationUserAccountCommon;
import com.linksus.common.util.ContextUtil;
import com.linksus.common.util.LogUtil;
import com.linksus.entity.LinksusAppaccount;
import com.linksus.entity.LinksusNewFansInfo;
import com.linksus.entity.LinksusRelationWeibouser;
import com.linksus.entity.LinksusTaskTotalUser;
import com.linksus.service.LinksusAppaccountService;
import com.linksus.service.LinksusRelationWeibouserService;
import com.linksus.service.LinksusTaskTotalUserService;

public class TaskQueueAllSinaFans extends BaseQueue{

	private String accountType;
	private LinksusTaskTotalUserService linksusTaskTotalUserService = (LinksusTaskTotalUserService) ContextUtil
			.getBean("linksusTaskTotalUserService");
	private LinksusRelationWeibouserService linksusRelationWeibouserService = (LinksusRelationWeibouserService) ContextUtil
			.getBean("linksusRelationWeibouserService");
	private LinksusAppaccountService linksusAppaccountService = (LinksusAppaccountService) ContextUtil
			.getBean("linksusAppaccountService");

	protected TaskQueueAllSinaFans(String taskType) {
		super(taskType);
	}

	public void setAccountType(String accountType){
		this.accountType = accountType;
	}

	@Override
	protected Map dealTaskData(List taskList){
		List<LinksusAppaccount> appaccounts = new ArrayList<LinksusAppaccount>();
		for(int i = 0; i < taskList.size(); i++){
			LinksusTaskTotalUser totalUser = (LinksusTaskTotalUser) taskList.get(i);
			// ��ȡ�����û�token�ӿ���֤
			Map params = new HashMap();
			params.put("id", totalUser.getAccountId());
			params.put("institutionId", totalUser.getInstitutionId());
			params.put("type", totalUser.getAccountType());
			LinksusAppaccount appcount = linksusAppaccountService.getLinksusAppaccountTokenByAppid(params);
			if(appcount != null){
				appcount.setLastUid(totalUser.getNextCursor());
				appaccounts.add(appcount);
			}else{//����Ȩ���޸�״̬Ϊʧ��
				totalUser.setStatus(2);
				linksusTaskTotalUserService.updateLinksusTaskTotalUsersStatus(totalUser);
			}
		}
		Map rsMap = new HashMap();
		if(appaccounts != null && appaccounts.size() > 0){
			rsMap.put(Constants.RETRUN_DATA_TASK_LIST, appaccounts);
		}
		return rsMap;
	}

	@Override
	protected List flushTaskQueue(){
		// �ӷ������˲��÷�ҳȡ����
		int currenCount = 0;
		currenCount = (currentPage - 1) * pageSize;
		// ͨ����ҳȡ��ֱ���б�
		LinksusTaskTotalUser totalUser = new LinksusTaskTotalUser();
		totalUser.setStartCount(currenCount);
		totalUser.setPageSize(pageSize);
		totalUser.setTaskType(1);
		totalUser.setAccountType(Integer.parseInt(accountType));

		//ȡ�˺�����Ϊ���˵������б�
		List<LinksusTaskTotalUser> relationlist = linksusTaskTotalUserService.getLinksusTaskTotalUsersByType(totalUser);
		if(relationlist != null){
			if(relationlist.size() < pageSize){// ����ѭ����� �´����¿�ʼ
				currentPage = 1;
				setTaskFinishFlag();
			}else{
				currentPage++;
			}
		}
		return relationlist;
	}

	@Override
	protected String parseClientData(Map rsDataMap) throws Exception{
		Long accountId = (Long) rsDataMap.get("AccountId");
		String institutionId = (String) rsDataMap.get("InstitutionId");
		String lastUid = (String) rsDataMap.get("LastUid");
		String status = (String) rsDataMap.get("Status");

		//����״̬Ϊ�������С�
		if(StringUtils.isNotBlank(status) && status.equals("3")){
			LinksusTaskTotalUser totalUser = new LinksusTaskTotalUser();
			totalUser.setAccountId(accountId);
			totalUser.setTaskType(1);
			totalUser.setAccountType(Integer.parseInt(accountType));
			//��ѯ״̬�Ƿ�Ϊ�����У��ǲ��޸ģ�Ϊ0ʱ�޸�Ϊ������3
			LinksusTaskTotalUser newTotalUser = linksusTaskTotalUserService.getLinksusTaskTotalUserById(totalUser);
			if(newTotalUser.getStatus() == 0){
				newTotalUser.setStatus(3);
				linksusTaskTotalUserService.updateLinksusTaskTotalUsersStatus(newTotalUser);
			}
		}

		List<LinksusNewFansInfo> newIds = new ArrayList<LinksusNewFansInfo>();
		List<String> allIds = new ArrayList<String>();
		allIds = (List<String>) rsDataMap.get("ids");
		for(int a = 0; a < allIds.size(); a++){
			String id = allIds.get(a);
			LinksusRelationWeibouser weiboUser = linksusRelationWeibouserService.getRelationWeibouserByUserId(Long
					.parseLong(id));
			if(weiboUser != null){
				RelationUserAccountCommon relationUserAccountCommon = new RelationUserAccountCommon();
				relationUserAccountCommon.dealRelationUserAccount(accountId, weiboUser.getUserId(), weiboUser
						.getPersonId(), Long.parseLong(institutionId), accountType, "1", 5);
			}else{
				LinksusNewFansInfo newFans = new LinksusNewFansInfo();
				newFans.setAccountType(1);
				newFans.setInstitutionId(institutionId);
				newFans.setCompangAppId(accountId.toString());
				newFans.setFanId(id);
				newIds.add(newFans);
			}
		}
		//����ȫ����˿�����������
		if(newIds.size() > 0){
			//���������˿��ȡ����
			try{
				QueueFactory.addQueueTaskData("Queue016", newIds);
			}catch (Exception e){
				LogUtil.saveException(logger, e);
			}
		}
		//��¼���һ����˿id�������жϴ���
		LinksusTaskTotalUser totalUserLast = new LinksusTaskTotalUser();
		totalUserLast.setAccountId(accountId);
		totalUserLast.setTaskType(1);
		totalUserLast.setAccountType(Integer.parseInt(accountType));
		totalUserLast.setNextCursor(Long.parseLong(lastUid));
		linksusTaskTotalUserService.updateLinksusTaskTotalUsersCursor(totalUserLast);

		//�޸�״̬
		if(StringUtils.isNotBlank(status) && !status.equals("3")){
			LinksusTaskTotalUser totalUser = new LinksusTaskTotalUser();
			totalUser.setAccountId(accountId);
			totalUser.setTaskType(1);
			totalUser.setAccountType(Integer.parseInt(accountType));
			totalUser.setStatus(Integer.parseInt(status));
			Calendar calendar = Calendar.getInstance();
			Integer updateTime = Integer.valueOf(String.valueOf((calendar.getTime().getTime()) / 1000));
			totalUser.setLastUpdtTime(updateTime);
			linksusTaskTotalUserService.updateLinksusTaskTotalUsersStatus(totalUser);
		}

		return null;
	}

	//���������˿������Queue45��
	public static void addFensiIdInQueue45(String rps_id,Long accountId,Long institutionId){
		List<LinksusNewFansInfo> newIds = new ArrayList<LinksusNewFansInfo>();
		LinksusNewFansInfo newFans = new LinksusNewFansInfo();
		newFans.setAccountType(1);
		newFans.setInstitutionId(institutionId + "");
		newFans.setCompangAppId(accountId + "");
		newFans.setFanId(rps_id);
		newIds.add(newFans);
		//����ȫ����˿�����������
		if(newIds.size() > 0){
			//���������˿��ȡ����
			try{
				QueueFactory.addQueueTaskData("Queue016", newIds);
			}catch (Exception e){
				LogUtil.saveException(logger, e);
			}
		}
	}

}
