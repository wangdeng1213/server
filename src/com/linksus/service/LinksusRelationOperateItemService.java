package com.linksus.service;

import java.util.List;

import com.linksus.entity.LinksusRelationOperateItem;

public interface LinksusRelationOperateItemService{

	/** ��ѯ�б� */
	public List<LinksusRelationOperateItem> getLinksusRelationOperateItemList();

	/** ������ѯ */
	public LinksusRelationOperateItem getLinksusRelationOperateItemById(Long pid);

	/** ���� */
	public Integer insertLinksusRelationOperateItem(LinksusRelationOperateItem entity);

	/** ���� */
	public Integer updateLinksusRelationOperateItem(LinksusRelationOperateItem entity);

	/** ����ɾ�� */
	public Integer deleteLinksusRelationOperateItemById(Long pid);

}
