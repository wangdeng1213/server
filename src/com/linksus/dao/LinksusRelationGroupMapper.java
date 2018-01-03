package com.linksus.dao;

import java.util.List;
import java.util.Map;

import com.linksus.entity.LinksusRelationGroup;

public interface LinksusRelationGroupMapper{

	/** �б��ѯ */
	public List<LinksusRelationGroup> getLinksusRelationGroupList();

	/** ������ѯ */
	public LinksusRelationGroup getLinksusRelationGroupById(Long pid);

	/** ���� */
	public Integer insertLinksusRelationGroup(LinksusRelationGroup entity);

	/** ���� */
	public Integer updateLinksusRelationGroup(LinksusRelationGroup entity);

	/** ����ɾ�� */
	public Integer deleteLinksusRelationGroupById(Long pid);

	/** ������ѯ�� */
	public LinksusRelationGroup getLinksusRelationGroup(LinksusRelationGroup dto);

	/** ��ѯ�û����Ƿ����"δ����"�� */
	public LinksusRelationGroup getPersonGroupInfoByInstIdAndGroupType(Map paramMap);

	/** ��ѯ�û����Ƿ����"***"�� */
	public LinksusRelationGroup getPersonGroupInfoType(Map map);

	/** ����δ������Ϣ */
	public Integer insertNoGroupInfo(LinksusRelationGroup entity);
}
