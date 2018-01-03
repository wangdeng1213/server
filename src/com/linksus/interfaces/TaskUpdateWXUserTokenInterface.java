package com.linksus.interfaces;

import java.util.HashMap;
import java.util.Map;

import com.linksus.task.TaskUpdateWXUserToken;

public class TaskUpdateWXUserTokenInterface extends BaseInterface{

	// 微信授权获取
	public Map cal(Map paramsMap) throws Exception{
		Map rsMap = new HashMap();
		TaskUpdateWXUserToken task = new TaskUpdateWXUserToken();
		String appid = paramsMap.get("appid").toString();
		String secret = paramsMap.get("secret").toString();
		rsMap = task.getWxUserToken(appid, secret);
		return rsMap;
	}
}
