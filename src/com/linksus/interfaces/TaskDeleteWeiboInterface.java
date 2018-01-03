package com.linksus.interfaces;

import java.util.Map;

import com.linksus.task.TaskDeleteWeibo;

public class TaskDeleteWeiboInterface extends BaseInterface{

	//  É¾³ýÎ¢²©
	public String cal(Map paramsMap) throws Exception{
		TaskDeleteWeibo task = new TaskDeleteWeibo();
		String pid = (String) paramsMap.get("pid");
		String errorCode = task.removeWeiboById(pid);
		return errorCode;
	}
}
