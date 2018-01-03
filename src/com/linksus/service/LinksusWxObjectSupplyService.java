package com.linksus.service;

import java.util.List;

import com.linksus.entity.LinksusWxObjectSupply;

public interface LinksusWxObjectSupplyService{

	/** ��ѯ�б� */
	public List<LinksusWxObjectSupply> getLinksusWxObjectSupplyList();

	/** ������ѯ */
	public LinksusWxObjectSupply getLinksusWxObjectSupplyById(Long pid);

	/** ���� */
	public Integer insertLinksusWxObjectSupply(LinksusWxObjectSupply entity);

	/** ���� */
	public Integer updateLinksusWxObjectSupply(LinksusWxObjectSupply entity);

	/** ����ɾ�� */
	public Integer deleteLinksusWxObjectSupplyById(Long pid);

	/** ��ѯ��������΢������ */
	public LinksusWxObjectSupply getSingleLinksusWxObjectSupplyById(Long pid);

	/** ��ѯ�������΢������*/
	public List<LinksusWxObjectSupply> getMoreLinksusWxObjectSupplyById(Long pid);

}