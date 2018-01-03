package com.linksus.queue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.linksus.common.module.WeiboUserCommon;
import com.linksus.common.util.ContextUtil;
import com.linksus.common.util.LogUtil;
import com.linksus.common.util.RedisUtil;
import com.linksus.entity.LinksusRelationWeibouser;
import com.linksus.entity.LinksusTaskIncrementUser;
import com.linksus.service.LinksusTaskIncrementUserService;

/**
 * ������ע����
 */
public class TaskIncrementalAttention extends BaseQueue{

	private String accountType;
	private LinksusTaskIncrementUserService linksusIncrementService = (LinksusTaskIncrementUserService) ContextUtil
			.getBean("linksusTaskIncrementUserService");

	protected TaskIncrementalAttention(String taskType) {
		super(taskType);
	}

	public String getAccountType(){
		return accountType;
	}

	public void setAccountType(String accountType){
		this.accountType = accountType;
	}

	/*
	 * ��ȡ������˿�����б�
	 */
	@Override
	protected List flushTaskQueue(){
		// �ӷ������˲��÷�ҳȡ����
		int currenCount = 0;
		currenCount = (currentPage - 1) * pageSize;
		// ͨ����ҳȡ���û��б�
		LinksusTaskIncrementUser incrementUser = new LinksusTaskIncrementUser();
		incrementUser.setStartCount(currenCount);
		incrementUser.setPageSize(pageSize);
		incrementUser.setAppType(new Integer(accountType));// 1,���� 2,��Ѷ
		List<LinksusTaskIncrementUser> incrementUserList = linksusIncrementService
				.getIncrementalUserTaskList(incrementUser);
		if(incrementUserList.size() < pageSize){// ����ѭ����� �´����¿�ʼ
			currentPage = 1;
			setTaskFinishFlag();
		}else{
			currentPage++;
		}

		return incrementUserList;
	}

	@Override
	protected String parseClientData(Map dataMap) throws Exception{
		List<LinksusRelationWeibouser> linksysWeiboFanslist = (List<LinksusRelationWeibouser>) dataMap
				.get("linksysWeiboFanslist");//��ע�б���Ϣ
		LinksusTaskIncrementUser linksusWeiboAccount = (LinksusTaskIncrementUser) dataMap.get("incrementalTask");//���������
		LinksusTaskIncrementUser taskIncrementReStr = (LinksusTaskIncrementUser) dataMap.get("taskIncrementRe");//�������������Ķ���
		//��������������е�����
		linksusIncrementService.updateLinksusTaskIncrementFollow(taskIncrementReStr);
		dealIncrementData(linksysWeiboFanslist, linksusWeiboAccount);
		return "success";
	}

	//��������������ע����
	public void dealIncrementData(List<LinksusRelationWeibouser> linksysWeiboFanslist,
			LinksusTaskIncrementUser linksusWeiboAccount) throws Exception{
		String accountId = String.valueOf(linksusWeiboAccount.getAccountId());
		Long institutionId = linksusWeiboAccount.getInstitutionId();
		for(int i = 0; i < linksysWeiboFanslist.size(); i++){
			//��ѯ΢���û������Ƿ���� �����ڲ�����Ա����
			LinksusRelationWeibouser weibouser = linksysWeiboFanslist.get(i);
			//�����˿��ҵ���߼�
			Map paramMap = new HashMap();
			paramMap.put("AccountId", accountId);
			paramMap.put("InstitutionId", institutionId);
			paramMap.put("type", accountType);
			WeiboUserCommon weiboUserCommon = new WeiboUserCommon();
			//����rpsId �� �˺������ж�΢���û��Ƿ���� 
			String strResult = RedisUtil.getRedisHash("relation_weibouser", weibouser.getRpsId());
			if(StringUtils.isBlank(strResult) && weibouser.getUserType() == 2){////�����ڼ��뵽�����û�������
				try{
					weiboUserCommon.paserWeiboUserInfo(weibouser, paramMap, false, "2", 0);
					QueueFactory.addQueueTaskData("Queue010", weibouser);
				}catch (Exception e){
					LogUtil.saveException(logger, e);
				}
			}else{
				weiboUserCommon.paserWeiboUserInfo(weibouser, paramMap, false, "2", 0);
			}
		}
	}
}
