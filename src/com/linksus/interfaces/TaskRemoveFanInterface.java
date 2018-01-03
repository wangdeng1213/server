package com.linksus.interfaces;

import java.util.Map;

import com.linksus.task.TaskRemoveFan;

public class TaskRemoveFanInterface extends BaseInterface{

	// ÒÆ³ý·ÛË¿
	public Map cal(Map paramsMap) throws Exception{
		TaskRemoveFan task = new TaskRemoveFan();
		Map errorCode = null;
		try{
			errorCode = task.removeFanInterface(paramsMap);
		}catch (Exception e){
			throw e;
		}
		return errorCode;
	}
}
