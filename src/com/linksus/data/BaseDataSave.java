package com.linksus.data;

import com.linksus.common.config.LoadConfig;

public abstract class BaseDataSave implements Runnable{

	/** ��С�����ύ�� */
	public static int commitNum = Integer.parseInt(LoadConfig.getString("commitNum"));
	/** ����ύ���ʱ�� ����*/
	public static long commitInterval = Long.parseLong(LoadConfig.getString("commitInterval"));
}
