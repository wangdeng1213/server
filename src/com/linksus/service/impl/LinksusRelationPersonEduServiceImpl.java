package com.linksus.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.dao.LinksusRelationPersonEduMapper;
import com.linksus.entity.LinksusRelationPersonEdu;
import com.linksus.service.LinksusRelationPersonEduService;

@Service("linksusRelationPersonEduService")
public class LinksusRelationPersonEduServiceImpl implements LinksusRelationPersonEduService{

	@Autowired
	private LinksusRelationPersonEduMapper linksusRelationPersonEduMapper;

	/** ��ѯ�б� */
	public List<LinksusRelationPersonEdu> getLinksusRelationPersonEduList(){
		return linksusRelationPersonEduMapper.getLinksusRelationPersonEduList();
	}

	/** ������ѯ */
	public LinksusRelationPersonEdu getLinksusRelationPersonEduById(Long pid){
		return linksusRelationPersonEduMapper.getLinksusRelationPersonEduById(pid);
	}

	/** ���� */
	public Integer insertLinksusRelationPersonEdu(LinksusRelationPersonEdu entity){
		return linksusRelationPersonEduMapper.insertLinksusRelationPersonEdu(entity);
	}

	/** ���� */
	public Integer updateLinksusRelationPersonEdu(LinksusRelationPersonEdu entity){
		return linksusRelationPersonEduMapper.updateLinksusRelationPersonEdu(entity);
	}

	/** ����ɾ�� */
	public Integer deleteLinksusRelationPersonEduById(Long pid){
		return linksusRelationPersonEduMapper.deleteLinksusRelationPersonEduById(pid);
	}

	/** ɾ��personId������ */
	public Integer deleteByPersonId(Long personId){
		return linksusRelationPersonEduMapper.deleteByPersonId(personId);
	}
}