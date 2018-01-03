package com.linksus.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.dao.LinksusRelationUserTagCountMapper;
import com.linksus.entity.LinksusRelationUserTagCount;
import com.linksus.service.LinksusRelationUserTagCountService;

@Service("linksusRelationUserTagCountService")
public class LinksusRelationUserTagCountServiceImpl implements LinksusRelationUserTagCountService{

	@Autowired
	private LinksusRelationUserTagCountMapper linksusRelationUserTagCountMapper;

	/** ��ѯ�б� */
	public List<LinksusRelationUserTagCount> getLinksusRelationUserTagCountList(){
		return linksusRelationUserTagCountMapper.getLinksusRelationUserTagCountList();
	}

	/** ������ѯ */
	public LinksusRelationUserTagCount getLinksusRelationUserTagCountById(Long pid){
		return linksusRelationUserTagCountMapper.getLinksusRelationUserTagCountById(pid);
	}

	/** ���� */
	public Integer insertLinksusRelationUserTagCount(LinksusRelationUserTagCount entity){
		return linksusRelationUserTagCountMapper.insertLinksusRelationUserTagCount(entity);
	}

	/** ���� */
	public Integer updateLinksusRelationUserTagCount(LinksusRelationUserTagCount entity){
		return linksusRelationUserTagCountMapper.updateLinksusRelationUserTagCount(entity);
	}

	/** ����ɾ�� */
	public Integer deleteLinksusRelationUserTagCountById(Long pid){
		return linksusRelationUserTagCountMapper.deleteLinksusRelationUserTagCountById(pid);
	}

	/** ȫ��ɾ�� */
	public Integer deleteALLLinksusRelationUserTagCount(){
		return linksusRelationUserTagCountMapper.deleteALLLinksusRelationUserTagCount();
	}

	/** ȫ������ */
	public Integer insertALLLinksusRelationUserTagCount(){
		return linksusRelationUserTagCountMapper.insertALLLinksusRelationUserTagCount();
	}
}