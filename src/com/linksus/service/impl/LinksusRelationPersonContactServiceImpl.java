package com.linksus.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.dao.LinksusRelationPersonContactMapper;
import com.linksus.entity.LinksusRelationPersonContact;
import com.linksus.service.LinksusRelationPersonContactService;

@Service("linksusRelationPersonContactService")
public class LinksusRelationPersonContactServiceImpl implements LinksusRelationPersonContactService{

	@Autowired
	private LinksusRelationPersonContactMapper linksusRelationPersonContactMapper;

	/** ��ѯ�б� */
	public List<LinksusRelationPersonContact> getLinksusRelationPersonContactList(){
		return linksusRelationPersonContactMapper.getLinksusRelationPersonContactList();
	}

	/** ������ѯ */
	public LinksusRelationPersonContact getLinksusRelationPersonContactById(Long pid){
		return linksusRelationPersonContactMapper.getLinksusRelationPersonContactById(pid);
	}

	/** ���� */
	public Integer insertLinksusRelationPersonContact(LinksusRelationPersonContact entity){
		return linksusRelationPersonContactMapper.insertLinksusRelationPersonContact(entity);
	}

	/** ���� */
	public Integer updateLinksusRelationPersonContact(LinksusRelationPersonContact entity){
		return linksusRelationPersonContactMapper.updateLinksusRelationPersonContact(entity);
	}

	/** ����ɾ�� */
	public Integer deleteLinksusRelationPersonContactById(Long pid){
		return linksusRelationPersonContactMapper.deleteLinksusRelationPersonContactById(pid);
	}

	/** ɾ��personId������ */
	public Integer deleteByPersonId(Long personId){
		return linksusRelationPersonContactMapper.deleteByPersonId(personId);
	}

	/** ͨ��person_id��ѯ��Ҫ�������û���Ϣ */
	public List<LinksusRelationPersonContact> getPersonContactMobileListByPersonId(long personId){
		return linksusRelationPersonContactMapper.getPersonContactMobileListByPersonId(personId);
	}

	/** ͨ��person_id��ѯ��Ҫ���ʼ��û���Ϣ */
	public List<LinksusRelationPersonContact> getPersonContactEmailListByPersonId(long personId){
		return linksusRelationPersonContactMapper.getPersonContactEmailListByPersonId(personId);
	}

}