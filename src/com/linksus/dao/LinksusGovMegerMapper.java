package com.linksus.dao;

import java.util.List;

import com.linksus.entity.LinksusGovMeger;

public interface LinksusGovMegerMapper{

	/** �б��ѯ */
	public List<LinksusGovMeger> getLinksusGovMegerList();

	/** ������ѯ */
	public LinksusGovMeger getLinksusGovMegerById(Long pid);

	/** ���� */
	public Integer insertLinksusGovMeger(LinksusGovMeger entity);

	/** ���� */
	public Integer updateLinksusGovMeger(LinksusGovMeger entity);

	/** ����ɾ�� */
	public Integer deleteLinksusGovMegerById(Long pid);

	public List<LinksusGovMeger> getLinksusGovMegerListByFather(Long fatherId);

}
