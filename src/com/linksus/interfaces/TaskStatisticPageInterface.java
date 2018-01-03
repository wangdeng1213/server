package com.linksus.interfaces;

import java.util.Map;

import com.linksus.common.module.TaskStatisticCommon;

public class TaskStatisticPageInterface extends BaseInterface{

	public String cal(Map paramsMap) throws Exception{
		return "$" + TaskStatisticCommon.taskStatistic();
	}
}
