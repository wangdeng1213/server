package com.linksus.dao;

import java.util.List;
import java.util.Map;

import com.linksus.entity.LinksusWeibo;
import com.linksus.entity.LinksusWeiboPool;

public interface LinksusWeiboPoolMapper{

	/** �б��ѯ */
	public List<LinksusWeiboPool> getLinksusWeiboPoolList();

	/** ������ѯ */
	public LinksusWeiboPool getLinksusWeiboPoolById(Long pid);

	/** ���� */
	public Integer insertLinksusWeiboPool(LinksusWeiboPool entity);

	/** ���� */
	public Integer updateLinksusWeiboPool(LinksusWeiboPool entity);

	/** ΢����������΢����������ת����*/
	public Integer updateLinksusWeiboPoolDataCount(LinksusWeiboPool entity);

	/** ����ɾ�� */
	public Integer deleteLinksusWeiboPoolById(Long pid);

	public List<LinksusWeibo> getLinksusWeiboPoolSend(LinksusWeibo entity);

	public LinksusWeibo getLinksusWeiboPoolByMap(Map map);

	public Integer updateLinksusWeiboPoolCount(LinksusWeibo entity);

	/** ���΢���Ƿ����*/
	public LinksusWeiboPool checkWeiboPoolIsExsit(Map paraMap);
}
