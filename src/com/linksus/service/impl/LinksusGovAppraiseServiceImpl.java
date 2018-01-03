package com.linksus.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.dao.LinksusGovAppraiseMapper;
import com.linksus.entity.LinksusGovAppraise;
import com.linksus.service.LinksusGovAppraiseService;

@Service("linksusGovAppraiseService")
public class LinksusGovAppraiseServiceImpl implements LinksusGovAppraiseService{

	@Autowired
	private LinksusGovAppraiseMapper linksusGovAppraiseMapper;

	/** ��ѯ�б� */
	public List<LinksusGovAppraise> getLinksusGovAppraiseList(){
		return linksusGovAppraiseMapper.getLinksusGovAppraiseList();
	}

	/** ������ѯ */
	public LinksusGovAppraise getLinksusGovAppraiseById(Long pid){
		return linksusGovAppraiseMapper.getLinksusGovAppraiseById(pid);
	}

	/** ���� */
	public Integer insertLinksusGovAppraise(LinksusGovAppraise entity){
		return linksusGovAppraiseMapper.insertLinksusGovAppraise(entity);
	}

	/** ���� */
	public Integer updateLinksusGovAppraise(LinksusGovAppraise entity){
		return linksusGovAppraiseMapper.updateLinksusGovAppraise(entity);
	}

	/** ����ɾ�� */
	public Integer deleteLinksusGovAppraiseById(Long pid){
		return linksusGovAppraiseMapper.deleteLinksusGovAppraiseById(pid);
	}
}