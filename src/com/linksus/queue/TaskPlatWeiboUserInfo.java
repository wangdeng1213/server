package com.linksus.queue;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.linksus.common.module.WeiboUserCommon;
import com.linksus.common.util.ContextUtil;
import com.linksus.common.util.JsonUtil;
import com.linksus.entity.LinksusAppaccount;
import com.linksus.entity.LinksusRelationWeibouser;
import com.linksus.service.LinksusAppaccountService;

public class TaskPlatWeiboUserInfo extends BaseQueue{

	private String accountType;
	private LinksusAppaccountService linksusAppaccountService = (LinksusAppaccountService) ContextUtil
			.getBean("linksusAppaccountService");

	protected TaskPlatWeiboUserInfo(String taskType) {
		super(taskType);
	}

	public void setAccountType(String accountType){
		this.accountType = accountType;
	}

	@Override
	protected List flushTaskQueue(){
		// 从服务器端采用分页取数据
		int currenCount = 0;
		currenCount = (currentPage - 1) * pageSize;
		// 通过分页取得用户列表
		LinksusAppaccount platUser = new LinksusAppaccount();
		platUser.setStartCount(currenCount);
		platUser.setPageSize(pageSize);
		platUser.setType(new Integer(accountType));// 1,新浪 2,腾讯
		List<LinksusAppaccount> appUserlist = linksusAppaccountService.getLinksusAppaccountList(platUser);
		if(appUserlist.size() < pageSize){// 任务循环完成 下次重新开始
			currentPage = 1;
			setTaskFinishFlag();
		}else{
			currentPage++;
		}
		return appUserlist;
	}

	@Override
	protected String parseClientData(Map rsDataMap) throws Exception{
		List<Map> infosList = (List<Map>) rsDataMap.get("userList");
		for(Iterator iterator = infosList.iterator(); iterator.hasNext();){
			Map map = (Map) iterator.next();
			LinksusRelationWeibouser linkWeiboUser = new LinksusRelationWeibouser();
			Map infoMap = (Map) map.get("strjson");
			String appType = map.get("appType") + "";
			linkWeiboUser.setUserType(Integer.valueOf(appType));
			Map parmMap = new HashMap();
			parmMap.put("accountType", appType);
			WeiboUserCommon weibouser = new WeiboUserCommon();
			weibouser.dealWeiboUserInfo(JsonUtil.map2json(infoMap), "1", parmMap, true, "0", 0);
		}
		return "success";
	}
}
