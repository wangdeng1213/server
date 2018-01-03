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
		// �ӷ������˲��÷�ҳȡ����
		int currenCount = 0;
		//currenCount = (currentPage - 1) * pageSize;
		//��ȡ�˺ű���΢���û�
		LinksusAppaccount appaccount = new LinksusAppaccount();
		appaccount.setStartCount(currenCount);
		appaccount.setPageSize(pageSize);
		List<LinksusAppaccount> appaccounts = linksusAppaccountService.getAllLinksusAppaccountWXUser(appaccount);
		if(appaccounts != null){
			if(appaccounts.size() < pageSize){// ����ѭ����� �´����¿�ʼ
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
