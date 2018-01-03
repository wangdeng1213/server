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
 * 增量粉丝任务
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
	 * 获取增量粉丝任务列表
	 */
	@Override
	protected List flushTaskQueue(){
		// 从服务器端采用分页取数据
		int currenCount = 0;
		currenCount = (currentPage - 1) * pageSize;
		// 通过分页取得用户列表
		LinksusTaskIncrementUser incrementUser = new LinksusTaskIncrementUser();
		incrementUser.setStartCount(currenCount);
		incrementUser.setPageSize(pageSize);
		incrementUser.setAppType(new Integer(accountType));// 1,新浪 2,腾讯
		List<LinksusTaskIncrementUser> incrementUserList = linksusIncrementService
				.getIncrementalUserTaskList(incrementUser);
		if(incrementUserList.size() < pageSize){// 任务循环完成 下次重新开始
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
				.get("linksysWeiboFanslist");//粉丝列表信息
		LinksusTaskIncrementUser taskIncrementReStr = (LinksusTaskIncrementUser) dataMap.get("taskIncrementRe");
		//更新增量任务表中的数据
		logger.error("增量粉丝调试:账号[{}],机构号[{}],粉丝数[{}],关注数[{}]", taskIncrementReStr.getAccountId(), taskIncrementReStr
				.getInstitutionId(), taskIncrementReStr.getLastFansNum(), taskIncrementReStr.getLastFollowNum());
		linksusIncrementService.updateLinksusTaskIncrementFans(taskIncrementReStr);
		dealIncrementData(linksysWeiboFanslist, taskIncrementReStr.getAccountId(), taskIncrementReStr
				.getInstitutionId());
		return "success";
	}

	//处理增量粉丝任务
	public void dealIncrementData(List<LinksusRelationWeibouser> linksysWeiboFanslist,Long accountId,Long institutionId)
			throws Exception{
		Map paramMap = new HashMap();
		//加入平台账号、机构号
		paramMap.put("AccountId", accountId);
		paramMap.put("InstitutionId", institutionId);
		paramMap.put("type", accountType);
		for(int i = 0; i < linksysWeiboFanslist.size(); i++){
			try{
				//查询微博用户表中是否存在 不存在插入人员表中 
				LinksusRelationWeibouser userInfo = linksysWeiboFanslist.get(i);
				WeiboUserCommon weiboUserCommon = new WeiboUserCommon();
				//根据rpsId 和 账号类型判断微博用户是否存在 
				String strResult = RedisUtil.getRedisHash("relation_weibouser", userInfo.getRpsId());
				if(StringUtils.isBlank(strResult) && userInfo.getUserType() == 2){////不存在加入到更新用户队列中
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
