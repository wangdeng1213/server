package com.linksus.service;

import java.util.List;

import com.linksus.entity.LinksusGovRunning;

public interface LinksusGovRunningService{

	/** ��ѯ�б� */
	public List<LinksusGovRunning> getLinksusGovRunningList();

	/** ������ѯ */
	public LinksusGovRunning getLinksusGovRunningById(Long pid);

	/** ���� */
	public Integer insertLinksusGovRunning(LinksusGovRunning entity);

	/** ���� */
	public Integer updateLinksusGovRunning(LinksusGovRunning entity);

	/** ����ɾ�� */
	public Integer deleteLinksusGovRunningById(Long pid);

	public void saveLinksusGovRunning(List<LinksusGovRunning> list,String operType);

}
