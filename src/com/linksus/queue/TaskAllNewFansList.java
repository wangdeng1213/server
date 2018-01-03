package com.linksus.queue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.linksus.common.module.WeiboUserCommon;
import com.linksus.common.util.ContextUtil;
import com.linksus.common.util.JsonUtil;
import com.linksus.service.LinksusRelationPersonService;
import com.linksus.service.LinksusRelationUserAccountService;
import com.linksus.service.LinksusRelationUserTagService;
import com.linksus.service.LinksusRelationWeibouserService;

public class TaskAllNewFansList extends BaseQueue{

	LinksusRelationPersonService linksusRelationPersonService = (LinksusRelationPersonService) ContextUtil
			.getBean("linksusRelationPersonService");

	LinksusRelationUserAccountService linksusRelationUserAccountService = (LinksusRelationUserAccountService) ContextUtil
			.getBean("linksusRelationUserAccountService");

	LinksusRelationWeibouserService linksusRelationWeibouserService = (LinksusRelationWeibouserService) ContextUtil
			.getBean("linksusRelationWeibouserService");

	LinksusRelationUserTagService linksusRelationUserTagService = (LinksusRelationUserTagService) ContextUtil
			.getBean("linksusRelationUserTagService");

	public void setAccountType(String accountType){
	}

	protected TaskAllNewFansList(String taskType) {
		super(taskType);
		if(taskType.equals("1")){
		}else if(taskType.equals("2")){
		}
	}

	@Override
	protected List flushTaskQueue(){
		return null;
	}

	@Override
	protected String parseClientData(Map rsDataMap) throws Exception{
		List allUser = (List) rsDataMap.get("userList");
		if(allUser != null && allUser.size() > 0){
			for(int i = 0; i < allUser.size(); i++){
				Map users = (Map) allUser.get(i);
				Map parmMap = new HashMap();
				parmMap.put("type", "1");
				parmMap.put("AccountId", users.get("AccountId"));
				parmMap.put("InstitutionId", users.get("InstitutionId"));
				String userInfo = JsonUtil.map2json(users);
				WeiboUserCommon weiboUserCommon = new WeiboUserCommon();
				weiboUserCommon.dealWeiboUserInfo(userInfo, "1", parmMap, true, "1", 0);
			}
		}
		return "success";
	}

}
