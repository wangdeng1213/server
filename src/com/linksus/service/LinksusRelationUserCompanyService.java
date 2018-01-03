package com.linksus.service;

import java.util.List;

import com.linksus.entity.LinksusRelationUserCompany;

public interface LinksusRelationUserCompanyService{

	/** ��ѯ�б� */
	public List<LinksusRelationUserCompany> getLinksusRelationUserCompanyList();

	/** ������ѯ */
	public LinksusRelationUserCompany getLinksusRelationUserCompanyById(Long pid);

	/** ���� */
	public Integer insertLinksusRelationUserCompany(LinksusRelationUserCompany entity);

	/** ���� */
	public Integer updateLinksusRelationUserCompany(LinksusRelationUserCompany entity);

	/** ����ɾ�� */
	public Integer deleteLinksusRelationUserCompanyById(Long pid);

	/**����userIdɾ��*/
	public Integer deleteLinksusRelationUserCompanyByUserId(Long userId);

}
