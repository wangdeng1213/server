package com.linksus.interfaces;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.linksus.task.TaskSendEmailInMarketing;

public class TaskSendEmailInMarketingInterface extends BaseInterface{

	public String cal(Map paramsMap) throws Exception{
		TaskSendEmailInMarketing task = new TaskSendEmailInMarketing();
		String errorCode = "";
		try{
			// 机构号
			String institutionId = null;
			// 用户邮箱
			String emailAdd = (String) paramsMap.get("emailAdd");
			// 邮件标题
			String emailTitle = (String) paramsMap.get("emailTitle");
			// 邮件内容
			String emailContent = (String) paramsMap.get("emailContent");
			// 是否系统发送
			String isSystem = (String) paramsMap.get("isSystem");

			if(StringUtils.isNotBlank(isSystem) && isSystem.equals("0")){
				institutionId = (String) paramsMap.get("institutionId");
			}

			errorCode = task.sendSingleEmailContent(institutionId, emailAdd, emailTitle, emailContent, isSystem);
		}catch (Exception e){
			throw e;
		}
		return errorCode;
	}
}
