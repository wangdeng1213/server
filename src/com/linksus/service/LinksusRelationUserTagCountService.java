package com.linksus.service;

import java.util.List;

import com.linksus.entity.LinksusRelationUserTagCount;

public interface LinksusRelationUserTagCountService{

	/** ��ѯ�б� */
	public List<LinksusRelationUserTagCount> getLinksusRelationUserTagCountList();

	/** ������ѯ */
	public LinksusRelationUserTagCount getLinksusRelationUserTagCountById(Long pid);

	/** ���� */
	public Integer insertLinksusRelationUserTagCount(LinksusRelationUserTagCount entity);

	/** ���� */
	public Integer updateLinksusRelationUserTagCount(LinksusRelationUserTagCount entity);

	/** ����ɾ�� */
	public Integer deleteLinksusRelationUserTagCountById(Long pid);

	/** ȫ��ɾ�� */
	public Integer deleteALLLinksusRelationUserTagCount();

	/** ȫ������ */
	public Integer insertALLLinksusRelationUserTagCount();

}
