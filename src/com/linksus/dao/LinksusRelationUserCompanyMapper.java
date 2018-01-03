package com.linksus.dao;

import java.util.List;

import com.linksus.entity.LinksusRelationUserCompany;

public interface LinksusRelationUserCompanyMapper{

	/** �б��ѯ */
	public List<LinksusRelationUserCompany> getLinksusRelationUserCompanyList();

	/** ������ѯ */
	public LinksusRelationUserCompany getLinksusRelationUserCompanyById(Long pid);

	/** ���� */
	public Integer insertLinksusRelationUserCompany(LinksusRelationUserCompany entity);

	/** ���� */
	public Integer updateLinksusRelationUserCompany(LinksusRelationUserCompany entity);

	/** ����ɾ�� */
	public Integer deleteLinksusRelationUserCompanyById(Long pid);

	/**�����û�idɾ��ְҵ��Ϣ*/
	public Integer deleteLinksusRelationUserCompanyByUserId(Long userId);

}
