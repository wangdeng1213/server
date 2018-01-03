package com.linksus.dao;

import java.util.List;

import com.linksus.entity.LinksusGovTag;

public interface LinksusGovTagMapper{

	/** �б��ѯ */
	public List<LinksusGovTag> getLinksusGovTagList();

	/** ������ѯ */
	public LinksusGovTag getLinksusGovTagById(Long pid);

	/** ���� */
	public Integer insertLinksusGovTag(LinksusGovTag entity);

	/** ���� */
	public Integer updateLinksusGovTag(LinksusGovTag entity);

	/** ����ɾ�� */
	public Integer deleteLinksusGovTagById(Long pid);

}
