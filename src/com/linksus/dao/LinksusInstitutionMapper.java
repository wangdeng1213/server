package com.linksus.dao;

import com.linksus.entity.LinksusInstitution;

public interface LinksusInstitutionMapper{

	/** ��ѯ����ʣ���� */
	public LinksusInstitution getSmsNumber(Long institutionId);

	/**����������1*/
	public void minusSmsNumber(LinksusInstitution linksusInstitution);

}
