package com.linksus.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.dao.LinksusRelationPersonOperMapper;
import com.linksus.entity.LinksusRelationPersonOper;
import com.linksus.service.LinksusRelationPersonOperService;

@Service("linksusRelationPersonOperService")
public class LinksusRelationPersonOperServiceImpl implements LinksusRelationPersonOperService{

	@Autowired
	private LinksusRelationPersonOperMapper linksusRelationPersonOperMapper;

	/** ��ѯ�б� */
	public List<LinksusRelationPersonOper> getLinksusRelationPersonOperList(){
		return linksusRelationPersonOperMapper.getLinksusRelationPersonOperList();
	}

	/** ������ѯ */
	public LinksusRelationPersonOper getLinksusRelationPersonOperById(Long pid){
		return linksusRelationPersonOperMapper.getLinksusRelationPersonOperById(pid);
	}

	/** ���� */
	public Integer insertLinksusRelationPersonOper(LinksusRelationPersonOper entity){
		return linksusRelationPersonOperMapper.insertLinksusRelationPersonOper(entity);
	}

	/** ���� */
	public Integer updateLinksusRelationPersonOper(LinksusRelationPersonOper entity){
		return linksusRelationPersonOperMapper.updateLinksusRelationPersonOper(entity);
	}

	/** ����ɾ�� */
	public Integer deleteLinksusRelationPersonOperById(Long pid){
		return linksusRelationPersonOperMapper.deleteLinksusRelationPersonOperById(pid);
	}
}