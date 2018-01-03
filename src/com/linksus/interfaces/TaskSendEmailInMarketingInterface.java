package com.linksus.interfaces;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.linksus.task.TaskSendEmailInMarketing;

public class TaskSendEmailInMarketingInterface extends BaseInterface{

	public String cal(Map paramsMap) throws Exception{
		TaskSendEmailInMarketing task = new TaskSendEmailInMarketing();
		String errorCode = "";
		try{
			// ������
			String institutionId = null;
			// �û�����
			String emailAdd = (String) paramsMap.get("emailAdd");
			// �ʼ�����
			String emailTitle = (String) paramsMap.get("emailTitle");
			// �ʼ�����
			String emailContent = (String) paramsMap.get("emailContent");
			// �Ƿ�ϵͳ����
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
