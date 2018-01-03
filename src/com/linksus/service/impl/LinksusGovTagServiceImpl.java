package com.linksus.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.dao.LinksusGovTagMapper;
import com.linksus.entity.LinksusGovTag;
import com.linksus.service.LinksusGovTagService;

@Service("linksusGovTagService")
public class LinksusGovTagServiceImpl implements LinksusGovTagService{

	@Autowired
	private LinksusGovTagMapper linksusGovTagMapper;

	/** ��ѯ�б� */
	public List<LinksusGovTag> getLinksusGovTagList(){
		return linksusGovTagMapper.getLinksusGovTagList();
	}

	/** ������ѯ */
	public LinksusGovTag getLinksusGovTagById(Long pid){
		return linksusGovTagMapper.getLinksusGovTagById(pid);
	}

	/** ���� */
	public Integer insertLinksusGovTag(LinksusGovTag entity){
		return linksusGovTagMapper.insertLinksusGovTag(entity);
	}

	/** ���� */
	public Integer updateLinksusGovTag(LinksusGovTag entity){
		return linksusGovTagMapper.updateLinksusGovTag(entity);
	}

	/** ����ɾ�� */
	public Integer deleteLinksusGovTagById(Long pid){
		return linksusGovTagMapper.deleteLinksusGovTagById(pid);
	}
}