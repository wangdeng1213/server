package com.linksus.service;

import com.linksus.entity.LinksusInstitution;

public interface LinksusInstitutionService{

	/** ��ѯ����ʣ���� */
	public LinksusInstitution getSmsNumber(Long institutionId);

	/**����������1*/
	public void minusSmsNumber(LinksusInstitution linksusInstitution);

}
