package com.linksus.service;

import java.util.List;

import com.linksus.entity.LinksusGovStructure;

public interface LinksusGovStructureService{

	/** ��ѯ�б� */
	public List<LinksusGovStructure> getLinksusGovStructureList();

	/** ������ѯ */
	public LinksusGovStructure getLinksusGovStructureById(Long pid);

	/** ���� */
	public Integer insertLinksusGovStructure(LinksusGovStructure entity);

	/** ���� */
	public Integer updateLinksusGovStructure(LinksusGovStructure entity);

	/** ����ɾ�� */
	public Integer deleteLinksusGovStructureById(Long pid);

	/** ������֯ID��ѯ */
	public List<LinksusGovStructure> getLinksusGovStructureByOrgId();

	/** ����account_id��ѯ��Ӧ��С��gid */
	public LinksusGovStructure getMinGidByAccountId(Long accountId);

}
