package com.linksus.dao;

import java.util.List;

import com.linksus.entity.LinksusRelationPerson;

public interface LinksusRelationPersonMapper{

	/**��ҳ��ѯ��ͷ���û��б� */
	public List<LinksusRelationPerson> getLinksusRelationPersonAddImageList(LinksusRelationPerson person);

	/**������Աͷ��·�� */
	public void updateLinksusRelationPersonHeadUrl(LinksusRelationPerson person);

	/** �б��ѯ */
	public List<LinksusRelationPerson> getLinksusRelationPersonList();

	/** ������ѯ */
	public LinksusRelationPerson getLinksusRelationPersonById(Long pid);

	/** ���� */
	public Integer insertLinksusRelationPerson(LinksusRelationPerson entity);

	/** ���� */
	public Integer updateLinksusRelationPerson(LinksusRelationPerson entity);

	/** ����ɾ�� */
	public Integer deleteLinksusRelationPersonById(Long pid);

	/** ������Ա����΢���û� */
	public void insertWeiXinUser(LinksusRelationPerson entity);

	/** ������Ա�������Ϣ  */
	public void updateLinksusRelationPersonInfo(LinksusRelationPerson person);

	/** ��ҳ��ѯ�û��б�  */
	public List<LinksusRelationPerson> getLinksusRelationPersons(LinksusRelationPerson person);

}
