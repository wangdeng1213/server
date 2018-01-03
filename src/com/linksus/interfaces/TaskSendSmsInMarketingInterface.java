package com.linksus.interfaces;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.linksus.task.TaskSendSmsInMarketing;

public class TaskSendSmsInMarketingInterface extends BaseInterface{

	public String cal(Map paramsMap) throws Exception{
		TaskSendSmsInMarketing task = new TaskSendSmsInMarketing();
		// ������
		String institutionId = null;
		// �û��ֻ���
		String phoneNumber = (String) paramsMap.get("phoneNumber");
		// ��������
		String pContent = (String) paramsMap.get("pContent");
		// �Ƿ�ϵͳ����
		String isSystem = (String) paramsMap.get("isSystem");

		if(StringUtils.isNotBlank(isSystem) && isSystem.equals("0")){
			institutionId = (String) paramsMap.get("institutionId");
		}

		String errorCode = task.sendSinglePhoneContent(institutionId, phoneNumber, pContent, isSystem);
		return errorCode;
	}
}
