package com.linksus.dao;

import java.util.List;

import com.linksus.entity.LinksusRelationPersonContact;

public interface LinksusRelationPersonContactMapper{

	/** �б��ѯ */
	public List<LinksusRelationPersonContact> getLinksusRelationPersonContactList();

	/** ������ѯ */
	public LinksusRelationPersonContact getLinksusRelationPersonContactById(Long pid);

	/** ���� */
	public Integer insertLinksusRelationPersonContact(LinksusRelationPersonContact entity);

	/** ���� */
	public Integer updateLinksusRelationPersonContact(LinksusRelationPersonContact entity);

	/** ����ɾ�� */
	public Integer deleteLinksusRelationPersonContactById(Long pid);

	/** ɾ��personId������ */
	public Integer deleteByPersonId(Long personId);

	/** ͨ��person_id��ѯ��Ҫ�������û���Ϣ */
	public List<LinksusRelationPersonContact> getPersonContactMobileListByPersonId(long personId);

	/** ͨ��person_id��ѯ��Ҫ���ʼ��û���Ϣ */
	public List<LinksusRelationPersonContact> getPersonContactEmailListByPersonId(long personId);

}
