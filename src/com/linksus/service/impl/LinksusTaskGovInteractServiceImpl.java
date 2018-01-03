package com.linksus.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.dao.LinksusTaskGovInteractMapper;
import com.linksus.entity.LinksusTaskGovInteract;
import com.linksus.service.LinksusTaskGovInteractService;

@Service("linksusTaskGovInteractService")
public class LinksusTaskGovInteractServiceImpl implements LinksusTaskGovInteractService{

	@Autowired
	private LinksusTaskGovInteractMapper linksusTaskGovInteractMapper;

	/** ��ѯ�б� */
	public List<LinksusTaskGovInteract> getLinksusTaskGovInteractList(){
		return linksusTaskGovInteractMapper.getLinksusTaskGovInteractList();
	}

	/** ������ѯ */
	public LinksusTaskGovInteract getLinksusTaskGovInteractById(Long pid){
		return linksusTaskGovInteractMapper.getLinksusTaskGovInteractById(pid);
	}

	/** ���� */
	public Integer insertLinksusTaskGovInteract(LinksusTaskGovInteract entity){
		return linksusTaskGovInteractMapper.insertLinksusTaskGovInteract(entity);
	}

	/** ���� */
	public Integer updateLinksusTaskGovInteract(LinksusTaskGovInteract entity){
		return linksusTaskGovInteractMapper.updateLinksusTaskGovInteract(entity);
	}

	/** ����ɾ�� */
	public Integer deleteLinksusTaskGovInteractById(Long pid){
		return linksusTaskGovInteractMapper.deleteLinksusTaskGovInteractById(pid);
	}

	public LinksusTaskGovInteract getMaxIdByAccountId(LinksusTaskGovInteract linksusTaskGovInteract){
		return linksusTaskGovInteractMapper.getMaxIdByAccountId(linksusTaskGovInteract);

	}
}