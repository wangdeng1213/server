package com.linksus.interfaces;

import java.util.Map;

import com.linksus.task.TaskAddAttention;

public class TaskAddAttentionInterface extends BaseInterface{

	// 添加关注前台调用接口
	public Map cal(Map paramsMap) throws Exception{
		TaskAddAttention task = new TaskAddAttention();
		Map errorCode = task.addAttentionInterface(paramsMap);
		return errorCode;
	}
}
