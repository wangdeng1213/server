package com.linksus.interfaces;

import java.util.Map;

import com.linksus.task.TaskCancelAttention;

public class TaskCancelAttentionInterface extends BaseInterface{

	// ȡ����עǰ̨���ýӿ�
	public Map cal(Map paramsMap) throws Exception{
		TaskCancelAttention task = new TaskCancelAttention();
		Map errorCode = task.cancelAttentionInterface(paramsMap);
		return errorCode;
	}
}
