package com.linksus.service;

import java.util.List;
import java.util.Map;

import com.linksus.entity.LinksusRelationUserTag;

public interface LinksusRelationUserTagService{

	/** ��ѯ�б� */
	public List<LinksusRelationUserTag> getLinksusRelationUserTagList();

	/** ������ѯ */
	public LinksusRelationUserTag getLinksusRelationUserTagById(Long pid);

	/** ���� */
	public Integer insertLinksusRelationUserTag(LinksusRelationUserTag entity);

	/** ���� */
	public Integer updateLinksusRelationUserTag(LinksusRelationUserTag entity);

	/** ����ɾ�� */
	public Integer deleteLinksusRelationUserTagById(Long pid);

	//����ɾ��
	public Integer deleteUserTagBySet(Map str);

	/**��ѯuserId���еı�ǩ����*/
	public List<LinksusRelationUserTag> getTagsByUserId(Long userId);

	/** ����ɾ�� */
	public Integer deleteLinksusRelationUserTagByUserId(Long pid);

	/**�ж�userId �� tagId �Ƿ����*/
	public Integer getCountByUserIdAndTagId(Long userId,Long tagId);

	/** ����ǩʹ�ü�¼�������м��*/
	public Integer insertTagCountIntoMiddleTable();

}
