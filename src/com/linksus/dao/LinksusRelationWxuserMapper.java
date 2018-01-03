package com.linksus.dao;

import java.util.List;
import java.util.Map;

import com.linksus.entity.LinksusRelationWxuser;

public interface LinksusRelationWxuserMapper{

	/** �б��ѯ */
	public List<LinksusRelationWxuser> getLinksusRelationWxuserList();

	/** ������ѯ */
	public LinksusRelationWxuser getLinksusRelationWxuserById(Long pid);

	/** ���� */
	public Integer insertLinksusRelationWxuser(LinksusRelationWxuser entity);

	/** ���� */
	public Integer updateLinksusRelationWxuser(LinksusRelationWxuser entity);

	/** ����ɾ�� */
	public Integer deleteLinksusRelationWxuserById(Long pid);

	public LinksusRelationWxuser getLinksusRelationWxuserInfo(long userId);

	/** ��������ѯ���� */
	public LinksusRelationWxuser getLinksusRelationWxuserByMap(Map map);

	/** ����PersonId */
	public Integer updatePersonId(LinksusRelationWxuser entity);
}
