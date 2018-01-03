package com.linksus.service;

import java.util.List;

import com.linksus.entity.LinksusRemoveEvent;

public interface LinksusRemoveEventService{

	/** ��ѯ�б� 
	 * @param removeEvent */
	public List<LinksusRemoveEvent> getRemoveWeiboList(LinksusRemoveEvent removeEvent);

	/** ������ѯ */
	public LinksusRemoveEvent getLinksusRemoveEventById(Long pid);

	/** ���� */
	public Integer insertLinksusRemoveEvent(LinksusRemoveEvent entity);

	/** ���� */
	public Integer updateLinksusRemoveEvent(LinksusRemoveEvent entity);

	/** ����ɾ�� */
	public Integer deleteLinksusRemoveEventById(Long pid);

	public void updateRemoveEventStatus(LinksusRemoveEvent updtEvent);

}
