package com.linksus.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.dao.LinksusGovStructureMapper;
import com.linksus.entity.LinksusGovStructure;
import com.linksus.service.LinksusGovStructureService;

@Service("linksusGovStructureService")
public class LinksusGovStructureServiceImpl implements LinksusGovStructureService{

	@Autowired
	private LinksusGovStructureMapper linksusGovStructureMapper;

	/** ��ѯ�б� */
	public List<LinksusGovStructure> getLinksusGovStructureList(){
		return linksusGovStructureMapper.getLinksusGovStructureList();
	}

	/** ������ѯ */
	public LinksusGovStructure getLinksusGovStructureById(Long pid){
		return linksusGovStructureMapper.getLinksusGovStructureById(pid);
	}

	/** ���� */
	public Integer insertLinksusGovStructure(LinksusGovStructure entity){
		return linksusGovStructureMapper.insertLinksusGovStructure(entity);
	}

	/** ���� */
	public Integer updateLinksusGovStructure(LinksusGovStructure entity){
		return linksusGovStructureMapper.updateLinksusGovStructure(entity);
	}

	/** ����ɾ�� */
	public Integer deleteLinksusGovStructureById(Long pid){
		return linksusGovStructureMapper.deleteLinksusGovStructureById(pid);
	}

	@Override
	public List<LinksusGovStructure> getLinksusGovStructureByOrgId(){
		// TODO Auto-generated method stub
		return linksusGovStructureMapper.getLinksusGovStructureByOrgId();
	}

	/** ����account_id��ѯ��Ӧ��С��gid */
	public LinksusGovStructure getMinGidByAccountId(Long accountId){
		return linksusGovStructureMapper.getMinGidByAccountId(accountId);
	}
}