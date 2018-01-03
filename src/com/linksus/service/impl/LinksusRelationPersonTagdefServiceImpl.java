package com.linksus.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.dao.LinksusRelationPersonTagdefMapper;
import com.linksus.entity.LinksusRelationPersonTagdef;
import com.linksus.service.LinksusRelationPersonTagdefService;

@Service("linksusRelationPersonTagdefService")
public class LinksusRelationPersonTagdefServiceImpl implements LinksusRelationPersonTagdefService{

	@Autowired
	private LinksusRelationPersonTagdefMapper linksusRelationPersonTagdefMapper;

	/** ��ѯ�б� */
	public List<LinksusRelationPersonTagdef> getLinksusRelationPersonTagdefList(){
		return linksusRelationPersonTagdefMapper.getLinksusRelationPersonTagdefList();
	}

	/** ������ѯ */
	public LinksusRelationPersonTagdef getLinksusRelationPersonTagdefById(Long pid){
		return linksusRelationPersonTagdefMapper.getLinksusRelationPersonTagdefById(pid);
	}

	/** ���� */
	public Integer insertLinksusRelationPersonTagdef(LinksusRelationPersonTagdef entity){
		return linksusRelationPersonTagdefMapper.insertLinksusRelationPersonTagdef(entity);
	}

	/** ���� */
	public Integer updateLinksusRelationPersonTagdef(LinksusRelationPersonTagdef entity){
		return linksusRelationPersonTagdefMapper.updateLinksusRelationPersonTagdef(entity);
	}

	/** ����ɾ�� */
	public Integer deleteLinksusRelationPersonTagdefById(Long pid){
		return linksusRelationPersonTagdefMapper.deleteLinksusRelationPersonTagdefById(pid);
	}

	/** tag_name��ѯ */
	public LinksusRelationPersonTagdef getLinksusRelationPersonTagdefByTagName(String tag_name){
		return linksusRelationPersonTagdefMapper.getLinksusRelationPersonTagdefByTagName(tag_name);
	}

}