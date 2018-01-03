package com.linksus.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.dao.LinksusRelationWxuserMapper;
import com.linksus.entity.LinksusRelationWxuser;
import com.linksus.service.LinksusRelationWxuserService;

@Service("linksusRelationWxuserService")
public class LinksusRelationWxuserServiceImpl implements LinksusRelationWxuserService{

	@Autowired
	private LinksusRelationWxuserMapper linksusRelationWxuserMapper;

	/** ��ѯ�б� */
	public List<LinksusRelationWxuser> getLinksusRelationWxuserList(){
		return linksusRelationWxuserMapper.getLinksusRelationWxuserList();
	}

	/** ������ѯ */
	public LinksusRelationWxuser getLinksusRelationWxuserById(Long pid){
		return linksusRelationWxuserMapper.getLinksusRelationWxuserById(pid);
	}

	/** ���� */
	public Integer insertLinksusRelationWxuser(LinksusRelationWxuser entity){
		return linksusRelationWxuserMapper.insertLinksusRelationWxuser(entity);
	}

	/** ���� */
	public Integer updateLinksusRelationWxuser(LinksusRelationWxuser entity){
		return linksusRelationWxuserMapper.updateLinksusRelationWxuser(entity);
	}

	/** ����ɾ�� */
	public Integer deleteLinksusRelationWxuserById(Long pid){
		return linksusRelationWxuserMapper.deleteLinksusRelationWxuserById(pid);
	}

	/**  ����������ѯ΢���û���Ϣ*/
	public LinksusRelationWxuser getLinksusRelationWxuserInfo(long userId){
		return linksusRelationWxuserMapper.getLinksusRelationWxuserInfo(userId);
	}

	/** ��������ѯ���� */
	public LinksusRelationWxuser getLinksusRelationWxuserByMap(Map map){
		return linksusRelationWxuserMapper.getLinksusRelationWxuserByMap(map);
	}

	/** ����PersonId */
	public Integer updatePersonId(LinksusRelationWxuser entity){
		return linksusRelationWxuserMapper.updatePersonId(entity);
	}
}