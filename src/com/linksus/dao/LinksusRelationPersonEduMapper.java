package com.linksus.dao;

import java.util.List;

import com.linksus.entity.LinksusRelationPersonEdu;

public interface LinksusRelationPersonEduMapper{

	/** �б��ѯ */
	public List<LinksusRelationPersonEdu> getLinksusRelationPersonEduList();

	/** ������ѯ */
	public LinksusRelationPersonEdu getLinksusRelationPersonEduById(Long pid);

	/** ���� */
	public Integer insertLinksusRelationPersonEdu(LinksusRelationPersonEdu entity);

	/** ���� */
	public Integer updateLinksusRelationPersonEdu(LinksusRelationPersonEdu entity);

	/** ����ɾ�� */
	public Integer deleteLinksusRelationPersonEduById(Long pid);

	/** ɾ��personId������ */
	public Integer deleteByPersonId(Long personId);
}
