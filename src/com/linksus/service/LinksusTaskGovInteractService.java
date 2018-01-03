package com.linksus.service;

import java.util.List;

import com.linksus.entity.LinksusTaskGovInteract;

public interface LinksusTaskGovInteractService{

	/** ��ѯ�б� */
	public List<LinksusTaskGovInteract> getLinksusTaskGovInteractList();

	/** ������ѯ */
	public LinksusTaskGovInteract getLinksusTaskGovInteractById(Long pid);

	/** ���� */
	public Integer insertLinksusTaskGovInteract(LinksusTaskGovInteract entity);

	/** ���� */
	public Integer updateLinksusTaskGovInteract(LinksusTaskGovInteract entity);

	/** ����ɾ�� */
	public Integer deleteLinksusTaskGovInteractById(Long pid);

	public LinksusTaskGovInteract getMaxIdByAccountId(LinksusTaskGovInteract linksusTaskGovInteract);

}
