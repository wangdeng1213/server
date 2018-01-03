package com.linksus.service;

import java.util.List;

import com.linksus.entity.LinksusGovTag;

public interface LinksusGovTagService{

	/** ��ѯ�б� */
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
