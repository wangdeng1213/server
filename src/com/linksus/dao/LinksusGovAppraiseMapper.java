package com.linksus.dao;

import java.util.List;

import com.linksus.entity.LinksusGovAppraise;

public interface LinksusGovAppraiseMapper{

	/** �б��ѯ */
	public List<LinksusGovAppraise> getLinksusGovAppraiseList();

	/** ������ѯ */
	public LinksusGovAppraise getLinksusGovAppraiseById(Long pid);

	/** ���� */
	public Integer insertLinksusGovAppraise(LinksusGovAppraise entity);

	/** ���� */
	public Integer updateLinksusGovAppraise(LinksusGovAppraise entity);

	/** ����ɾ�� */
	public Integer deleteLinksusGovAppraiseById(Long pid);

}
