package com.linksus.service;

import java.util.List;
import java.util.Map;

import com.linksus.entity.LinksusInteractWeixin;

public interface LinksusInteractWeixinService{

	/** ��ѯ�б� */
	public List<LinksusInteractWeixin> getLinksusInteractWeixinList();

	/** ������ѯ */
	public LinksusInteractWeixin getLinksusInteractWeixinById(Long pid);

	/** ���� */
	public Integer insertLinksusInteractWeixin(LinksusInteractWeixin entity);

	/** ���� */
	public Integer updateLinksusInteractWeixin(LinksusInteractWeixin entity);

	/** ����ɾ�� */
	public Integer deleteLinksusInteractWeixinById(Long pid);

	/** �������ѯ */
	public LinksusInteractWeixin getLinksusInteractWeixinByMap(Map map);

	/** ��ѯ��ʱ�ظ�΢�������б� */
	public List<LinksusInteractWeixin> getTaskWeixinAtImmediate(LinksusInteractWeixin entity);

	/** ��ѯ��ʱ�ظ�΢�������б� */
	public List<LinksusInteractWeixin> getTaskWeixinAtRegularTime(LinksusInteractWeixin entity);

	public List getInteractWeixinListByIds(List weixinMsgs);

	/**  �޸ķ���״̬������ʱ��*/
	public Integer updateLinksusInteractWeixinStatus(LinksusInteractWeixin entity);
}
