package com.linksus.interfaces;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.linksus.task.TaskWeixinMenu;

public class TaskWeixinMenuInterface extends BaseInterface{

	public Map cal(Map paramsMap) throws Exception{
		Map rsMap = new HashMap();
		String sucessId = "";
		String failId = "";
		//需要创建或修改自定义菜单的平台账号ID
		String accountIds = (String) paramsMap.get("accountIds");

		if(StringUtils.isNotBlank(accountIds)){
			TaskWeixinMenu weixinMenu = new TaskWeixinMenu();
			for(String accountId : accountIds.split(",")){
				try{
					Long id = Long.parseLong(accountId);
					String resultStr = weixinMenu.createWeixinMenu(id);
					if(StringUtils.isNotBlank(resultStr) && resultStr.equals("sucess")){
						if(StringUtils.isBlank(sucessId)){
							sucessId = accountId;
						}else{
							sucessId = sucessId + "," + accountId;
						}
					}else{
						if(StringUtils.isBlank(failId)){
							failId = accountId;
						}else{
							failId = failId + "," + accountId;
						}
					}
				}catch (Exception e){
					if(StringUtils.isBlank(failId)){
						failId = accountId;
					}else{
						failId = failId + "," + accountId;
					}
				}
			}
		}
		rsMap.put("successId", sucessId);
		rsMap.put("failId", failId);
		return rsMap;
	}

}
