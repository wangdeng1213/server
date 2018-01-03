package com.linksus.service;

import java.util.List;
import java.util.Map;

import com.linksus.entity.LinksusTaskInteractGovMid;

public interface LinksusTaskInteractGovMidService{

	/** ��ѯ�б� */
	public List<LinksusTaskInteractGovMid> getLinksusTaskInteractGovMidList();

	/** ������ѯ */
	public LinksusTaskInteractGovMid getLinksusTaskInteractGovMidById(LinksusTaskInteractGovMid govMid);

	/** ���� */
	public Integer insertLinksusTaskInteractGovMid(LinksusTaskInteractGovMid entity);

	/**
	 * ��������
	 * @param list
	 * @param operType
	 * @return
	 */
	public Integer saveLinksusInteractGovMid(List<LinksusTaskInteractGovMid> list,String operType);

	/** ���� */
	public Integer updateLinksusTaskInteractGovMid(LinksusTaskInteractGovMid entity);

	/** ����ɾ�� */
	public Integer deleteLinksusTaskInteractGovMidById(LinksusTaskInteractGovMid pid);

	public List<LinksusTaskInteractGovMid> getGovMidTempListByMap(Map map);

	public void deleteLinksusTaskInteractGovMid();

	/**
	 * ����MID���в�ѯ
	 * @param mid
	 * @return
	 */
	public LinksusTaskInteractGovMid getLinksusTaskInteractGovMidListByMid(Long mid);

	/**
	 * ����Mid����ɾ��
	 * @param mid
	 * @return
	 */
	public Integer deleteLinkSusTaskInteractGovMidByMid(Long mid);

	/**
	 * ��ȡ����΢�����ڵ�mid
	 * @return
	 */
	public List<Long> getDuoWenInteractGovMid();

	/**
	 * ȡ�����۲�ת���ļ�¼
	 * @return
	 */
	public List<LinksusTaskInteractGovMid> getLinksusCommentAndRelayGovMidList();

	/**
	 * ���»�������
	 */
	public void updateInteractType(LinksusTaskInteractGovMid govMid);

}
