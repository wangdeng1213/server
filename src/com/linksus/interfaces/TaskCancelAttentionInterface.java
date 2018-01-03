package com.linksus.interfaces;

import java.util.Map;

import com.linksus.task.TaskCancelAttention;

public class TaskCancelAttentionInterface extends BaseInterface{

	// 取消关注前台调用接口
	public Map cal(Map paramsMap) throws Exception{
		TaskCancelAttention task = new TaskCancelAttention();
		Map errorCode = task.cancelAttentionInterface(paramsMap);
		return errorCode;
	}
}
