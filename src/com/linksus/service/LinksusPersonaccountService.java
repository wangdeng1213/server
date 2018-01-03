package com.linksus.service;

import java.util.List;
import java.util.Map;

import com.linksus.entity.LinksusPersonaccount;

public interface LinksusPersonaccountService{

	/** ��ѯ�б� */
	public List<LinksusPersonaccount> getLinksusPersonaccountList();

	/** ������ѯ */
	public LinksusPersonaccount getLinksusPersonaccountById(Long pid);

	/** ���� */
	public Integer insertLinksusPersonaccount(LinksusPersonaccount entity);

	/** ���� */
	public Integer updateLinksusPersonaccount(LinksusPersonaccount entity);

	/** ����ɾ�� */
	public Integer deleteLinksusPersonaccountById(Long pid);

	public Map getManagerByInstitutionId(Long institutionId);

	public List getPersonaccountByInstitutionId(Long institutionId);

}
