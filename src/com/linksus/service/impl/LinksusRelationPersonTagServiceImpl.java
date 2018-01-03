package com.linksus.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.dao.LinksusRelationPersonTagMapper;
import com.linksus.entity.LinksusRelationPersonTag;
import com.linksus.service.LinksusRelationPersonTagService;

@Service("linksusRelationPersonTagService")
public class LinksusRelationPersonTagServiceImpl implements LinksusRelationPersonTagService{

	@Autowired
	private LinksusRelationPersonTagMapper linksusRelationPersonTagMapper;

	/** ��ѯ�б� */
	public List<LinksusRelationPersonTag> getLinksusRelationPersonTagList(){
		return linksusRelationPersonTagMapper.getLinksusRelationPersonTagList();
	}

	/** ������ѯ */
	public LinksusRelationPersonTag getLinksusRelationPersonTagById(Long pid){
		return linksusRelationPersonTagMapper.getLinksusRelationPersonTagById(pid);
	}

	/** ���� */
	public Integer insertLinksusRelationPersonTag(LinksusRelationPersonTag entity){
		return linksusRelationPersonTagMapper.insertLinksusRelationPersonTag(entity);
	}

	/** ���� */
	public Integer updateLinksusRelationPersonTag(LinksusRelationPersonTag entity){
		return linksusRelationPersonTagMapper.updateLinksusRelationPersonTag(entity);
	}

	/** ����ɾ�� */
	public Integer deleteLinksusRelationPersonTagById(Long pid){
		return linksusRelationPersonTagMapper.deleteLinksusRelationPersonTagById(pid);
	}

	/** ɾ��personId������ */
	public Integer deleteByPersonId(Long personId){
		return linksusRelationPersonTagMapper.deleteByPersonId(personId);
	}
}