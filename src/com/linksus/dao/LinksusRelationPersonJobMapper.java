package com.linksus.dao;

import java.util.List;

import com.linksus.entity.LinksusRelationPersonJob;

public interface LinksusRelationPersonJobMapper{

	/** �б��ѯ */
	public List<LinksusRelationPersonJob> getLinksusRelationPersonJobList();

	/** ������ѯ */
	public LinksusRelationPersonJob getLinksusRelationPersonJobById(Long pid);

	/** ���� */
	public Integer insertLinksusRelationPersonJob(LinksusRelationPersonJob entity);

	/** ���� */
	public Integer updateLinksusRelationPersonJob(LinksusRelationPersonJob entity);

	/** ����ɾ�� */
	public Integer deleteLinksusRelationPersonJobById(Long pid);

	/** ɾ��personId������ */
	public Integer deleteByPersonId(Long personId);
}
