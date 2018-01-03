package com.linksus.dao;

import java.util.List;
import java.util.Map;

import com.linksus.entity.LinksusTaskInteractWeiboMid;

public interface LinksusTaskInteractWeiboMidMapper{

	/** �б��ѯ */
	public List<LinksusTaskInteractWeiboMid> getLinksusTaskInteractWeiboMidList();

	/** ������ѯ */
	public LinksusTaskInteractWeiboMid getLinksusTaskInteractWeiboMidById(Long pid);

	/** ���� */
	public Integer insertLinksusTaskInteractWeiboMid(LinksusTaskInteractWeiboMid entity);

	/** ���� */
	public Integer updateLinksusTaskInteractWeiboMid(LinksusTaskInteractWeiboMid entity);

	/** ����ɾ�� */
	public Integer deleteLinksusTaskInteractWeiboMidById(Long pid);

	public List<LinksusTaskInteractWeiboMid> getWeiboMidTempListByMap(Map map);

	/** �����ʱ��������  */
	public void deleteLinksusTaskInteractWeiboMid();

}
