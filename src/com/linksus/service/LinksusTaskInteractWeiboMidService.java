package com.linksus.service;

import java.util.List;
import java.util.Map;

import com.linksus.entity.LinksusTaskInteractWeiboMid;

public interface LinksusTaskInteractWeiboMidService{

	/** ��ѯ�б� */
	public List<LinksusTaskInteractWeiboMid> getLinksusTaskInteractWeiboMidList();

	/** ������ѯ */
	public LinksusTaskInteractWeiboMid getLinksusTaskInteractWeiboMidById(Long pid);

	/** ���� */
	public Integer insertLinksusTaskInteractWeiboMid(LinksusTaskInteractWeiboMid entity);

	/** ���� */
	public Integer updateLinksusTaskInteractWeiboMid(LinksusTaskInteractWeiboMid entity);

	/** ����ɾ�� */
	public Integer deleteLinksusTaskInteractWeiboMidById(Long pid);

	/**�������ͺ��˺Ų�ѯ����ʱ���л���������*/
	public List<LinksusTaskInteractWeiboMid> getWeiboMidTempListByMap(Map map);

	/** �����ʱ��������  */
	public void deleteLinksusTaskInteractWeiboMid();

}
