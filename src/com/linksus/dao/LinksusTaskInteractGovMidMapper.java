package com.linksus.dao;

import java.util.List;
import java.util.Map;

import com.linksus.entity.LinksusTaskInteractGovMid;

public interface LinksusTaskInteractGovMidMapper{

	/** �б��ѯ */
	public List<LinksusTaskInteractGovMid> getLinksusTaskInteractGovMidList();

	/** ������ѯ */
	public LinksusTaskInteractGovMid getLinksusTaskInteractGovMidById(LinksusTaskInteractGovMid pid);

	/** ���� */
	public Integer insertLinksusTaskInteractGovMid(LinksusTaskInteractGovMid entity);

	/** ���� */
	public Integer updateLinksusTaskInteractGovMid(LinksusTaskInteractGovMid entity);

	/** ����ɾ�� */
	public Integer deleteLinksusTaskInteractGovMidById(LinksusTaskInteractGovMid pid);

	public List<LinksusTaskInteractGovMid> getGovMidTempListByMap(Map map);

	public Integer deleteLinksusTaskInteractGovMid();

	/** ����MID��ѯ */
	public List<LinksusTaskInteractGovMid> getLinksusTaskInteractGovMidListByMid(Long mid);

	/**
	 * ����MIDɾ���м��
	 * @param mid
	 * @return
	 */
	public Integer deleteLinkSusTaskInteractGovMidByMid(Long mid);

	/**
	 * ��ȡ����΢��
	 * @return
	 */
	public List<Long> getDuoWenInteractGovMid();

	/**
	 * ȡ�����۲�ת���ļ�¼
	 * @return
	 */
	public List<LinksusTaskInteractGovMid> getLinksusCommentAndRelayGovMidList();

	/** ���»�������***/
	public void updateInteractType(LinksusTaskInteractGovMid govMid);
}
