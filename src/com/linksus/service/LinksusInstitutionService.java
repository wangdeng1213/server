package com.linksus.service;

import com.linksus.entity.LinksusInstitution;

public interface LinksusInstitutionService{

	/** 查询短信剩余量 */
	public LinksusInstitution getSmsNumber(Long institutionId);

	/**短信余量减1*/
	public void minusSmsNumber(LinksusInstitution linksusInstitution);

}
