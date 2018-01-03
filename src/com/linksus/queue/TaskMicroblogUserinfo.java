package com.linksus.queue;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.linksus.common.config.LoadConfig;
import com.linksus.common.module.WeiboUserCommon;
import com.linksus.common.util.ContextUtil;
import com.linksus.common.util.DateUtil;
import com.linksus.entity.LinksusRelationWeibouser;
import com.linksus.service.LinksusRelationWeibouserService;

/**
 * 更新粉丝的基本信息(linksus_relation_weibouser)
 * */
public class TaskMicroblogUserinfo extends BaseQueue{

	private String accountType;
	
	/** 更新粉丝列表索引标识位*/
	private Long lastUserId =0L;
	
	private LinksusRelationWeibouserService relationWeiboUserService = (LinksusRelationWeibouserService) ContextUtil
			.getBean("linksusRelationWeibouserService");

	protected TaskMicroblogUserinfo(String taskType) {
		super(taskType);
	}

	public void setAccountType(String accountType){
		this.accountType = accountType;
	}

	@Override
	protected List flushTaskQueue(){
		String hours = LoadConfig.getString("updateWeiboUserHour");
		// 从服务器端采用分页取数据
		int currenCount = 0;
		currenCount = (currentPage - 1) * pageSize;
		// 通过分页取得用户列表
		LinksusRelationWeibouser weibouser = new LinksusRelationWeibouser();
		weibouser.setStartCount(currenCount);
		weibouser.setPageSize(pageSize);
		weibouser.setUserType(new Integer(accountType));// 1,新浪 2,腾讯
		Integer time = Integer.parseInt(hours) * 3600;
		Integer updateTime = DateUtil.getUnixDate(new Date()) - time;
		/*
		 * Calendar calendar = Calendar.getInstance();
		 * calendar.add(Calendar.MONTH, -1); Integer updateTime =
		 * Integer.valueOf(String.valueOf((calendar.getTime().getTime()) /
		 * 1000));
		 */
		weibouser.setSytime(updateTime);
		weibouser.setLastUserId(lastUserId);
		List<LinksusRelationWeibouser> weiboUserlist = relationWeiboUserService
				.getLinksusWeibouserListByTime(weibouser);
		if(weiboUserlist.size() < pageSize){// 任务循环完成 下次重新开始
			currentPage = 1;
			setTaskFinishFlag();
			this.lastUserId = 0L;
		}else{
			currentPage++;
			this.lastUserId = weiboUserlist.get(weiboUserlist.size() - 1).getUserId();
		}
		
		return weiboUserlist;
	}

	@Override
	protected String parseClientData(Map rsDataMap) throws Exception{
		List<Map> infosList = (List<Map>) rsDataMap.get("userList");
		if(infosList != null && infosList.size() > 0){
			for(Iterator iterator = infosList.iterator(); iterator.hasNext();){
				Map map = (Map) iterator.next();
				LinksusRelationWeibouser linkWeiboUser = (LinksusRelationWeibouser) map.get("strjson");
				WeiboUserCommon weiboCommon = new WeiboUserCommon();
				weiboCommon.paserWeiboUserInfo(linkWeiboUser, null, true, "0", 0);
			}
		}
		return "success";
	}
}
