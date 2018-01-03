package com.linksus.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.dao.LinksusRelationPersonGroupMapper;
import com.linksus.entity.LinksusRelationPersonGroup;
import com.linksus.service.LinksusRelationPersonGroupService;

@Service("linksusRelationPersonGroupService")
public class LinksusRelationPersonGroupServiceImpl implements LinksusRelationPersonGroupService{

	@Autowired
	private LinksusRelationPersonGroupMapper linksusRelationPersonGroupMapper;

	/** ��ѯ�б� */
	public List<LinksusRelationPersonGroup> getLinksusRelationPersonGroupList(){
		return linksusRelationPersonGroupMapper.getLinksusRelationPersonGroupList();
	}

	/** ������ѯ */
	public LinksusRelationPersonGroup getLinksusRelationPersonGroupById(Map paramMap){
		return linksusRelationPersonGroupMapper.getLinksusRelationPersonGroupById(paramMap);
	}

	/** ���� */
	public Integer insertLinksusRelationPersonGroup(LinksusRelationPersonGroup entity){
		return linksusRelationPersonGroupMapper.insertLinksusRelationPersonGroup(entity);
	}

	/** ���� */
	public Integer updateLinksusRelationPersonGroup(LinksusRelationPersonGroup entity){
		return linksusRelationPersonGroupMapper.updateLinksusRelationPersonGroup(entity);
	}

	/** ����ɾ�� */
	public Integer deleteLinksusRelationPersonGroupById(Long pid){
		return linksusRelationPersonGroupMapper.deleteLinksusRelationPersonGroupById(pid);
	}

	/**ɾ��person_group ����personId ��institutionId*/
	public Integer deleteByHashMap(Map map){
		return linksusRelationPersonGroupMapper.deleteByHashMap(map);
	}

	/** ɾ��personId������ */
	public Integer deleteByPersonId(Long personId){
		return linksusRelationPersonGroupMapper.deleteByPersonId(personId);
	}

	/**Map��ѯ */
	public List<LinksusRelationPersonGroup> getLinksusRelationPersonGroupByMap(Map map){
		// TODO Auto-generated method stub
		return linksusRelationPersonGroupMapper.getLinksusRelationPersonGroupByMap(map);
	}

	public LinksusRelationPersonGroup searchRelationPersonGroupByMap(Map map){

		return linksusRelationPersonGroupMapper.searchRelationPersonGroupByMap(map);
	}

	/** �������ɾ�� */
	public Integer deleteLinksusRelationPersonGroupByMap(Map map){
		return linksusRelationPersonGroupMapper.deleteLinksusRelationPersonGroupByMap(map);
	}
}