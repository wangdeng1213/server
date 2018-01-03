package com.linksus.dao;

import java.util.List;

import com.linksus.entity.LinksusRelationPersonInfo;

public interface LinksusRelationPersonInfoMapper{

	/** �б��ѯ */
	public List<LinksusRelationPersonInfo> getLinksusRelationPersonInfoList();

	/** ������ѯ */
	public LinksusRelationPersonInfo getLinksusRelationPersonInfoById(Long pid);

	/** ���� */
	public Integer insertLinksusRelationPersonInfo(LinksusRelationPersonInfo entity);

	/** ���� */
	public Integer updateLinksusRelationPersonInfo(LinksusRelationPersonInfo entity);

	/** ����ɾ�� */
	public Integer deleteLinksusRelationPersonInfoById(Long pid);

}
