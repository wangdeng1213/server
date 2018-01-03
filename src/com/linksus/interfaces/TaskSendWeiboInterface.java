package com.linksus.interfaces;

import java.util.Map;

import com.linksus.task.TaskSendWeibo;

public class TaskSendWeiboInterface extends BaseInterface{

	private String sendType;

	public String getSendType(){
		return sendType;
	}

	public void setSendType(String sendType){
		this.sendType = sendType;
	}

	public String cal(Map paramsMap) throws Exception{
		String errorCode = "";
		TaskSendWeibo task = new TaskSendWeibo();
		Long pid = (Long) paramsMap.get("pid");
		if(sendType.equals("0")){// 即时发送
			errorCode = task.sendImmediate(paramsMap);
		}else if(sendType.equals("1")){// 定时发送
			String status = (String) paramsMap.get("status");
			String sendTime = (String) paramsMap.get("sendTime");
			errorCode = task.sendAtRegularTime(sendTime, status, pid);

		}
		return errorCode;
	}
}
