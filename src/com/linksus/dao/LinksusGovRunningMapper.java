package com.linksus.dao;

import java.util.List;

import com.linksus.entity.LinksusGovRunning;

public interface LinksusGovRunningMapper{

	/** �б��ѯ */
	public List<LinksusGovRunning> getLinksusGovRunningList();

	/** ������ѯ */
	public LinksusGovRunning getLinksusGovRunningById(Long pid);

	/** ���� */
	public Integer insertLinksusGovRunning(LinksusGovRunning entity);

	/** ���� */
	public Integer updateLinksusGovRunning(LinksusGovRunning entity);

	/** ����ɾ�� */
	public Integer deleteLinksusGovRunningById(Long pid);

}
