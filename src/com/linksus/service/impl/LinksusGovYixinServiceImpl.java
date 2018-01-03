package com.linksus.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.dao.LinksusGovYixinMapper;
import com.linksus.entity.LinksusGovYixin;
import com.linksus.service.LinksusGovYixinService;

@Service("linksusGovYixinService")
public class LinksusGovYixinServiceImpl implements LinksusGovYixinService{

	@Autowired
	private LinksusGovYixinMapper linksusGovYixinMapper;

	/** ��ѯ�б� */
	public List<LinksusGovYixin> getLinksusGovYixinList(){
		return linksusGovYixinMapper.getLinksusGovYixinList();
	}

	/** ������ѯ */
	public LinksusGovYixin getLinksusGovYixinById(Long pid){
		return linksusGovYixinMapper.getLinksusGovYixinById(pid);
	}

	/** ���� */
	public Integer insertLinksusGovYixin(LinksusGovYixin entity){
		return linksusGovYixinMapper.insertLinksusGovYixin(entity);
	}

	/** ���� */
	public Integer updateLinksusGovYixin(LinksusGovYixin entity){
		return linksusGovYixinMapper.updateLinksusGovYixin(entity);
	}

	/** ����ɾ�� */
	public Integer deleteLinksusGovYixinById(Long pid){
		return linksusGovYixinMapper.deleteLinksusGovYixinById(pid);
	}
}