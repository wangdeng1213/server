package com.linksus.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.dao.LinksusInstitutionMapper;
import com.linksus.entity.LinksusInstitution;
import com.linksus.service.LinksusInstitutionService;

@Service("linksusInstitutionService")
public class LinksusInstitutionServiceImpl implements LinksusInstitutionService{

	@Autowired
	private LinksusInstitutionMapper linksusInstitutionMapper;

	/** 查询短信剩余量 */
	public LinksusInstitution getSmsNumber(Long institutionId){
		return linksusInstitutionMapper.getSmsNumber(institutionId);
	}

	/**短信余量减1*/
	public void minusSmsNumber(LinksusInstitution linksusInstitution){
		linksusInstitutionMapper.minusSmsNumber(linksusInstitution);
	}

}