package com.linksus.data;

import com.linksus.common.config.LoadConfig;

public abstract class BaseDataSave implements Runnable{

	/** 最小批量提交数 */
	public static int commitNum = Integer.parseInt(LoadConfig.getString("commitNum"));
	/** 最大提交间隔时间 毫秒*/
	public static long commitInterval = Long.parseLong(LoadConfig.getString("commitInterval"));
}
