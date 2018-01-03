package com.linksus.service;

import java.util.List;
import java.util.Map;

import com.linksus.entity.LinksusWeibo;
import com.linksus.entity.LinksusWeiboPool;

public interface LinksusWeiboPoolService{

	/** ��ѯ�б� */
	public List<LinksusWeiboPool> getLinksusWeiboPoolList();

	/** ������ѯ */
	public LinksusWeiboPool getLinksusWeiboPoolById(Long pid);

	/** ���� */
	// public Integer insertLinksusWeiboPool(LinksusWeiboPool entity);
	/** ���� */
	//	public Integer updateLinksusWeiboPool(LinksusWeiboPool entity);

	/** ΢����������΢����������ת����*/
	public Integer updateLinksusWeiboPoolDataCount(LinksusWeiboPool entity);

	/** ����ɾ�� */
	public Integer deleteLinksusWeiboPoolById(Long pid);

	/** ��ѯ�б� */
	public List<LinksusWeibo> getLinksusWeiboPoolSend(LinksusWeibo entity);

	public LinksusWeibo getLinksusWeiboPoolByMap(Map map);

	/** ���� */
	public Integer updateLinksusWeiboPoolCount(LinksusWeibo entity);

	/** ͨ��mid �� accountType �ж�΢���Ƿ����*/
	public LinksusWeiboPool checkWeiboPoolIsExsit(Map paraMap);

}
