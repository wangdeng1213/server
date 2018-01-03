package com.linksus.service;

import java.util.List;

import com.linksus.entity.LinksusGovYixin;

public interface LinksusGovYixinService{

	/** ��ѯ�б� */
	public List<LinksusGovYixin> getLinksusGovYixinList();

	/** ������ѯ */
	public LinksusGovYixin getLinksusGovYixinById(Long pid);

	/** ���� */
	public Integer insertLinksusGovYixin(LinksusGovYixin entity);

	/** ���� */
	public Integer updateLinksusGovYixin(LinksusGovYixin entity);

	/** ����ɾ�� */
	public Integer deleteLinksusGovYixinById(Long pid);

}
