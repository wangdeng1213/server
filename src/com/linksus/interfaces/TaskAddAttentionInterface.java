package com.linksus.interfaces;

import java.util.Map;

import com.linksus.task.TaskAddAttention;

public class TaskAddAttentionInterface extends BaseInterface{

	// ��ӹ�עǰ̨���ýӿ�
	public Map cal(Map paramsMap) throws Exception{
		TaskAddAttention task = new TaskAddAttention();
		Map errorCode = task.addAttentionInterface(paramsMap);
		return errorCode;
	}
}
