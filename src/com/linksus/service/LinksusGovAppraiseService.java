package com.linksus.service;

import java.util.List;

import com.linksus.entity.LinksusGovAppraise;

public interface LinksusGovAppraiseService{

	/** ��ѯ�б� */
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
