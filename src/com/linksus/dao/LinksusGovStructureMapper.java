package com.linksus.dao;

import java.util.List;

import com.linksus.entity.LinksusGovStructure;

public interface LinksusGovStructureMapper{

	/** �б��ѯ */
	public List<LinksusGovStructure> getLinksusGovStructureList();

	/** ������ѯ */
	public LinksusGovStructure getLinksusGovStructureById(Long pid);

	/** ���� */
	public Integer insertLinksusGovStructure(LinksusGovStructure entity);

	/** ���� */
	public Integer updateLinksusGovStructure(LinksusGovStructure entity);

	/** ����ɾ�� */
	public Integer deleteLinksusGovStructureById(Long pid);

	/**
	 * ����OrgId��ȡ��֯����
	 * @param orgId
	 * @return
	 */
	public List<LinksusGovStructure> getLinksusGovStructureByOrgId();

	/** ����account_id��ѯ��Ӧ��С��gid */
	public LinksusGovStructure getMinGidByAccountId(Long accountId);
}
