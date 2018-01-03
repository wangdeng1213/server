package com.linksus.service;

import java.util.List;

import com.linksus.entity.LinksusRelationPersonTag;

public interface LinksusRelationPersonTagService{

	/** ��ѯ�б� */
	public List<LinksusRelationPersonTag> getLinksusRelationPersonTagList();

	/** ������ѯ */
	public LinksusRelationPersonTag getLinksusRelationPersonTagById(Long pid);

	/** ���� */
	public Integer insertLinksusRelationPersonTag(LinksusRelationPersonTag entity);

	/** ���� */
	public Integer updateLinksusRelationPersonTag(LinksusRelationPersonTag entity);

	/** ����ɾ�� */
	public Integer deleteLinksusRelationPersonTagById(Long pid);

	/** ɾ��personId������ */
	public Integer deleteByPersonId(Long personId);

}
