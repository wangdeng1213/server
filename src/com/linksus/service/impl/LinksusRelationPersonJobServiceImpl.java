package com.linksus.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.dao.LinksusRelationPersonJobMapper;
import com.linksus.entity.LinksusRelationPersonJob;
import com.linksus.service.LinksusRelationPersonJobService;

@Service("linksusRelationPersonJobService")
public class LinksusRelationPersonJobServiceImpl implements LinksusRelationPersonJobService{

	@Autowired
	private LinksusRelationPersonJobMapper linksusRelationPersonJobMapper;

	/** ��ѯ�б� */
	public List<LinksusRelationPersonJob> getLinksusRelationPersonJobList(){
		return linksusRelationPersonJobMapper.getLinksusRelationPersonJobList();
	}

	/** ������ѯ */
	public LinksusRelationPersonJob getLinksusRelationPersonJobById(Long pid){
		return linksusRelationPersonJobMapper.getLinksusRelationPersonJobById(pid);
	}

	/** ���� */
	public Integer insertLinksusRelationPersonJob(LinksusRelationPersonJob entity){
		return linksusRelationPersonJobMapper.insertLinksusRelationPersonJob(entity);
	}

	/** ���� */
	public Integer updateLinksusRelationPersonJob(LinksusRelationPersonJob entity){
		return linksusRelationPersonJobMapper.updateLinksusRelationPersonJob(entity);
	}

	/** ����ɾ�� */
	public Integer deleteLinksusRelationPersonJobById(Long pid){
		return linksusRelationPersonJobMapper.deleteLinksusRelationPersonJobById(pid);
	}

	/** ɾ��personId������ */
	public Integer deleteByPersonId(Long personId){
		return linksusRelationPersonJobMapper.deleteByPersonId(personId);
	}
}