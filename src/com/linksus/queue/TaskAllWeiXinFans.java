package com.linksus.queue;

import java.util.List;
import java.util.Map;

import com.linksus.common.util.ContextUtil;
import com.linksus.entity.LinksusAppaccount;
import com.linksus.entity.LinksusWeiXinUser;
import com.linksus.service.LinksusAppaccountService;
import com.linksus.task.WeixinAction;

public class TaskAllWeiXinFans extends BaseQueue{

	private LinksusAppaccountService linksusAppaccountService = (LinksusAppaccountService) ContextUtil
			.getBean("linksusAppaccountService");

	protected TaskAllWeiXinFans(String taskType) {
		super(taskType);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected List flushTaskQueue(){
		// 从服务器端采用分页取数据
		int currenCount = 0;
		//currenCount = (currentPage - 1) * pageSize;
		//获取账号表中微信用户
		LinksusAppaccount appaccount = new LinksusAppaccount();
		appaccount.setStartCount(currenCount);
		appaccount.setPageSize(pageSize);
		List<LinksusAppaccount> appaccounts = linksusAppaccountService.getAllLinksusAppaccountWXUser(appaccount);
		if(appaccounts != null){
			if(appaccounts.size() < pageSize){// 任务循环完成 下次重新开始
				currentPage = 1;
				setTaskFinishFlag();
			}else{
				currentPage++;
			}
		}
		return appaccounts;
	}

	@Override
	protected String parseClientData(Map rsDataMap) throws Exception{
		WeixinAction weiXinAction = new WeixinAction();

		List<LinksusWeiXinUser> uesrsInfo = (List<LinksusWeiXinUser>) rsDataMap.get("uresInfo");
		if(uesrsInfo != null && uesrsInfo.size() > 0){
			for(LinksusWeiXinUser weixinUser : uesrsInfo){
				Long id = weixinUser.getAppid();
				weiXinAction.dealWeiXinUser(weixinUser, id);
			}
		}
		return "success";
	}

}
