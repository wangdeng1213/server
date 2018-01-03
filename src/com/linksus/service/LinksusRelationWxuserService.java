package com.linksus.service;

import java.util.List;
import java.util.Map;

import com.linksus.entity.LinksusRelationWxuser;

public interface LinksusRelationWxuserService{

	/** ��ѯ�б� */
	public List<LinksusRelationWxuser> getLinksusRelationWxuserList();

	/** ������ѯ */
	public LinksusRelationWxuser getLinksusRelationWxuserById(Long pid);

	/** ���� */
	public Integer insertLinksusRelationWxuser(LinksusRelationWxuser entity);

	/** ���� */
	public Integer updateLinksusRelationWxuser(LinksusRelationWxuser entity);

	/** ����ɾ�� */
	public Integer deleteLinksusRelationWxuserById(Long pid);

	/**  ����������ѯ΢���û���Ϣ*/
	public LinksusRelationWxuser getLinksusRelationWxuserInfo(long userId);

	/** ��������ѯ���� */
	public LinksusRelationWxuser getLinksusRelationWxuserByMap(Map map);

	/** ����PersonId */
	public Integer updatePersonId(LinksusRelationWxuser entity);
}
