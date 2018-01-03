package com.linksus.dao;

import java.util.List;
import java.util.Map;

import com.linksus.entity.LinksusRelationUserTag;

public interface LinksusRelationUserTagMapper{

	/** �б��ѯ */
	public List<LinksusRelationUserTag> getLinksusRelationUserTagList();

	/** ������ѯ */
	public LinksusRelationUserTag getLinksusRelationUserTagById(Long pid);

	/** ���� */
	public Integer insertLinksusRelationUserTag(LinksusRelationUserTag entity);

	/** ���� */
	public Integer updateLinksusRelationUserTag(LinksusRelationUserTag entity);

	/** ����ɾ�� */
	public Integer deleteLinksusRelationUserTagById(Long pid);

	public Integer deleteUserTagBySet(Map str);

	/**��ѯuserId���еı�ǩ����*/
	public List<LinksusRelationUserTag> getTagsByUserId(Long userId);

	public Integer getCountByUserIdAndTagId(Map map);

	/** ����ǩʹ�ü�¼�������м��*/
	public Integer insertTagCountIntoMiddleTable();
}
