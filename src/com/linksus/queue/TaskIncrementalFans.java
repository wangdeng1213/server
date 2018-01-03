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
 * ������˿����
 */
public class TaskIncrementalFans extends BaseQueue{

	private String accountType;
	private LinksusTaskIncrementUserService linksusIncrementService = (LinksusTaskIncrementUserService) ContextUtil
			.getBean("linksusTaskIncrementUserService");

	protected TaskIncrementalFans(String taskType) {
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
				.get("linksysWeiboFanslist");//��˿�б���Ϣ
		LinksusTaskIncrementUser taskIncrementReStr = (LinksusTaskIncrementUser) dataMap.get("taskIncrementRe");
		//��������������е�����
		logger.error("������˿����:�˺�[{}],������[{}],��˿��[{}],��ע��[{}]", taskIncrementReStr.getAccountId(), taskIncrementReStr
				.getInstitutionId(), taskIncrementReStr.getLastFansNum(), taskIncrementReStr.getLastFollowNum());
		linksusIncrementService.updateLinksusTaskIncrementFans(taskIncrementReStr);
		dealIncrementData(linksysWeiboFanslist, taskIncrementReStr.getAccountId(), taskIncrementReStr
				.getInstitutionId());
		return "success";
	}

	//����������˿����
	public void dealIncrementData(List<LinksusRelationWeibouser> linksysWeiboFanslist,Long accountId,Long institutionId)
			throws Exception{
		Map paramMap = new HashMap();
		//����ƽ̨�˺š�������
		paramMap.put("AccountId", accountId);
		paramMap.put("InstitutionId", institutionId);
		paramMap.put("type", accountType);
		for(int i = 0; i < linksysWeiboFanslist.size(); i++){
			try{
				//��ѯ΢���û������Ƿ���� �����ڲ�����Ա���� 
				LinksusRelationWeibouser userInfo = linksysWeiboFanslist.get(i);
				WeiboUserCommon weiboUserCommon = new WeiboUserCommon();
				//����rpsId �� �˺������ж�΢���û��Ƿ���� 
				String strResult = RedisUtil.getRedisHash("relation_weibouser", userInfo.getRpsId());
				if(StringUtils.isBlank(strResult) && userInfo.getUserType() == 2){////�����ڼ��뵽�����û�������
					weiboUserCommon.paserWeiboUserInfo(userInfo, paramMap, false, "1", 0);
					QueueFactory.addQueueTaskData("Queue010", userInfo);
				}else{
					weiboUserCommon.paserWeiboUserInfo(userInfo, paramMap, false, "1", 0);
				}
			}catch (Exception e){
				LogUtil.saveException(logger, e);
			}
		}
	}

}
