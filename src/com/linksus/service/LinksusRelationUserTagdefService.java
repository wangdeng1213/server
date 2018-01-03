package com.linksus.service;

import java.util.List;
import java.util.Map;

import com.linksus.entity.LinksusRelationUserTagdef;

public interface LinksusRelationUserTagdefService{

	/** ��ѯ�б� */
	public List<LinksusRelationUserTagdef> getLinksusRelationUserTagdefList();

	/** ������ѯ */
	public LinksusRelationUserTagdef getLinksusRelationUserTagdefById(Long pid);

	/** ���� */
	public Integer insertLinksusRelationUserTagdef(LinksusRelationUserTagdef entity);

	/** ���� */
	public Integer updateLinksusRelationUserTagdef(LinksusRelationUserTagdef entity);

	/** ����ɾ�� */
	public Integer deleteLinksusRelationUserTagdefById(Long pid);

	/** ��ѯ����list�еı�ǩ���� */
	public List<LinksusRelationUserTagdef> getUserTagdefBySet(Map tagParaMap);

	/**ͨ����ǩ �� �û������жϱ�ǩ�������Ƿ����*/

	public LinksusRelationUserTagdef checkIsExsitByTagAndaccoutType(Map paraMap);

	/** ��ǩ���Ʋ�ѯ */
	public LinksusRelationUserTagdef getLinksusRelationUserTagdefByTagName(String tagname);

	/** ��ǩʹ�ô���ͳ�Ƹ��� */
	public Integer updateLinksusRelationUserTagdefUseCount();

	/** ����û���ǩͳ��ʹ�õ��м�� */
	public Integer clearUserTagMiddleTable();
}
