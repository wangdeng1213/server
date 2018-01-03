package com.linksus.service;

import java.util.List;

import com.linksus.entity.LinksusRelationPerson;

public interface LinksusRelationPersonService{

	/**��ҳ��ѯ��ͷ���û��б� */
	public List<LinksusRelationPerson> getLinksusRelationPersonAddImageList(LinksusRelationPerson person);

	/**������Աͷ��·�� */
	public void updateLinksusRelationPersonHeadUrl(LinksusRelationPerson person);

	/** ��ѯ�б� */
	public List<LinksusRelationPerson> getLinksusRelationPersonList();

	/** ������ѯ */
	public LinksusRelationPerson getLinksusRelationPersonById(Long pid);

	/** ���� */
	//public Integer insertLinksusRelationPerson(LinksusRelationPerson entity);
	//public void saveLinksusRelationPerson(List personList,String operType);
	/** ���� */
	//public Integer updateLinksusRelationPerson(LinksusRelationPerson entity);
	/** ����ɾ�� */
	public Integer deleteLinksusRelationPersonById(Long pid);

	/** ������Ա����΢���û� */
	public void insertWeiXinUser(LinksusRelationPerson entity);

	/**������Ա�������Ϣ */
	public void updateLinksusRelationPersonInfo(LinksusRelationPerson person);

	/** ��ҳ��ѯ�û��б�  */
	public List<LinksusRelationPerson> getLinksusRelationPersons(LinksusRelationPerson person);

}
