package com.linksus.service;

import java.util.List;

import com.linksus.entity.LinksusGovMeger;

public interface LinksusGovMegerService{

	/** ��ѯ�б� */
	public List<LinksusGovMeger> getLinksusGovMegerList();

	/** ������ѯ */
	public LinksusGovMeger getLinksusGovMegerById(Long pid);

	/** ���� */
	public Integer insertLinksusGovMeger(LinksusGovMeger entity);

	/** ���� */
	public Integer updateLinksusGovMeger(LinksusGovMeger entity);

	/** ����ɾ�� */
	public Integer deleteLinksusGovMegerById(Long pid);

	/**
	 * ���ݸ��ڵ��ѯ
	 * @return
	 */
	public List<LinksusGovMeger> getLinksusGovMegerListByFather(Long fatherId);

}
