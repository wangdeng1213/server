package com.linksus.service;

import java.util.List;

import com.linksus.entity.LinksusRelationPersonOper;

public interface LinksusRelationPersonOperService{

	/** ��ѯ�б� */
	public List<LinksusRelationPersonOper> getLinksusRelationPersonOperList();

	/** ������ѯ */
	public LinksusRelationPersonOper getLinksusRelationPersonOperById(Long pid);

	/** ���� */
	public Integer insertLinksusRelationPersonOper(LinksusRelationPersonOper entity);

	/** ���� */
	public Integer updateLinksusRelationPersonOper(LinksusRelationPersonOper entity);

	/** ����ɾ�� */
	public Integer deleteLinksusRelationPersonOperById(Long pid);

}
