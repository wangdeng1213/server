package com.linksus.service;

import java.util.List;

import com.linksus.entity.LinksusRelationPersonTagdef;

public interface LinksusRelationPersonTagdefService{

	/** ��ѯ�б� */
	public List<LinksusRelationPersonTagdef> getLinksusRelationPersonTagdefList();

	/** ������ѯ */
	public LinksusRelationPersonTagdef getLinksusRelationPersonTagdefById(Long pid);

	/** ���� */
	public Integer insertLinksusRelationPersonTagdef(LinksusRelationPersonTagdef entity);

	/** ���� */
	public Integer updateLinksusRelationPersonTagdef(LinksusRelationPersonTagdef entity);

	/** ����ɾ�� */
	public Integer deleteLinksusRelationPersonTagdefById(Long pid);

	/** tag_name��ѯ */
	public LinksusRelationPersonTagdef getLinksusRelationPersonTagdefByTagName(String tag_name);

}
