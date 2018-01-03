package com.linksus.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.dao.LinksusRelationGroupMapper;
import com.linksus.entity.LinksusRelationGroup;
import com.linksus.service.LinksusRelationGroupService;

@Service("linksusRelationGroupService")
public class LinksusRelationGroupServiceImpl implements LinksusRelationGroupService{

	@Autowired
	private LinksusRelationGroupMapper linksusRelationGroupMapper;

	/** ��ѯ�б� */
	public List<LinksusRelationGroup> getLinksusRelationGroupList(){
		return linksusRelationGroupMapper.getLinksusRelationGroupList();
	}

	/** ������ѯ */
	public LinksusRelationGroup getLinksusRelationGroupById(Long pid){
		return linksusRelationGroupMapper.getLinksusRelationGroupById(pid);
	}

	/** ���� */
	public Integer insertLinksusRelationGroup(LinksusRelationGroup entity){
		return linksusRelationGroupMapper.insertLinksusRelationGroup(entity);
	}

	/** ���� */
	public Integer updateLinksusRelationGroup(LinksusRelationGroup entity){
		return linksusRelationGroupMapper.updateLinksusRelationGroup(entity);
	}

	/** ����ɾ�� */
	public Integer deleteLinksusRelationGroupById(Long pid){
		return linksusRelationGroupMapper.deleteLinksusRelationGroupById(pid);
	}

	/** ������ѯ�� */
	public LinksusRelationGroup getLinksusRelationGroup(LinksusRelationGroup dto){
		// TODO Auto-generated method stub
		return linksusRelationGroupMapper.getLinksusRelationGroup(dto);
	}

	/** ��ѯ�û����Ƿ����"δ����"�� */
	public LinksusRelationGroup getPersonGroupInfoByInstIdAndGroupType(Map paramMap){
		return linksusRelationGroupMapper.getPersonGroupInfoByInstIdAndGroupType(paramMap);
	}

	/** ��ѯ�û����Ƿ����"**"�� */
	public LinksusRelationGroup getPersonGroupInfoType(Map map){
		return linksusRelationGroupMapper.getPersonGroupInfoType(map);
	}

	/** ����δ������Ϣ */
	public Integer insertNoGroupInfo(LinksusRelationGroup entity){
		return linksusRelationGroupMapper.insertNoGroupInfo(entity);
	}
}