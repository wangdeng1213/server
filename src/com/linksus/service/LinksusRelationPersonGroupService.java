package com.linksus.service;

import java.util.List;
import java.util.Map;

import com.linksus.entity.LinksusRelationPersonGroup;

public interface LinksusRelationPersonGroupService{

	/** ��ѯ�б� */
	public List<LinksusRelationPersonGroup> getLinksusRelationPersonGroupList();

	/** ������ѯ */
	public LinksusRelationPersonGroup getLinksusRelationPersonGroupById(Map paramMap);

	/** ���� */
	public Integer insertLinksusRelationPersonGroup(LinksusRelationPersonGroup entity);

	/** ���� */
	public Integer updateLinksusRelationPersonGroup(LinksusRelationPersonGroup entity);

	/** ����ɾ�� */
	public Integer deleteLinksusRelationPersonGroupById(Long pid);

	/** ɾ��personId������ */
	public Integer deleteByPersonId(Long personId);

	/**ɾ��person_group ����personId ��institutionId*/
	public Integer deleteByHashMap(Map map);

	/**Map��ѯ */
	public List<LinksusRelationPersonGroup> getLinksusRelationPersonGroupByMap(Map map);

	public LinksusRelationPersonGroup searchRelationPersonGroupByMap(Map map);

	/** �������ɾ�� */
	public Integer deleteLinksusRelationPersonGroupByMap(Map map);
}
