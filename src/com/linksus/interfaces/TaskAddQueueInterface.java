package com.linksus.interfaces;

import java.util.Map;

import com.linksus.queue.QueueFactory;

/**
 * ���������������
 *
 */
public class TaskAddQueueInterface extends BaseInterface{

	public String cal(Map paramsMap) throws Exception{
		QueueFactory.addQueueTaskData((String) paramsMap.get("queueType"), paramsMap.get("queueObj"));
		return null;
	}
}
