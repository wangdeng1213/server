package com.linksus.dao;

import java.util.List;
import java.util.Map;

import com.linksus.entity.LinksusPersonaccount;

public interface LinksusPersonaccountMapper{

	/** �б��ѯ */
	public List<LinksusPersonaccount> getLinksusPersonaccountList();

	/** ������ѯ */
	public LinksusPersonaccount getLinksusPersonaccountById(Long pid);

	/** ���� */
	public Integer insertLinksusPersonaccount(LinksusPersonaccount entity);

	/** ���� */
	public Integer updateLinksusPersonaccount(LinksusPersonaccount entity);

	/** ����ɾ�� */
	public Integer deleteLinksusPersonaccountById(Long pid);

	/** ���ݻ���id��ȡ����Ա��Ϣ */
	public Map getManagerByInstitutionId(Long institutionId);

	public List getPersonaccountByInstitutionId(Long institutionId);

}
