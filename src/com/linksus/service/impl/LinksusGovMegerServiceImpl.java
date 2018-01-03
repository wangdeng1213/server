package com.linksus.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.dao.LinksusGovMegerMapper;
import com.linksus.entity.LinksusGovMeger;
import com.linksus.service.LinksusGovMegerService;

@Service("linksusGovMegerService")
public class LinksusGovMegerServiceImpl implements LinksusGovMegerService{

	@Autowired
	private LinksusGovMegerMapper linksusGovMegerMapper;

	/** ��ѯ�б� */
	public List<LinksusGovMeger> getLinksusGovMegerList(){
		return linksusGovMegerMapper.getLinksusGovMegerList();
	}

	/** ������ѯ */
	public LinksusGovMeger getLinksusGovMegerById(Long pid){
		return linksusGovMegerMapper.getLinksusGovMegerById(pid);
	}

	/** ���� */
	public Integer insertLinksusGovMeger(LinksusGovMeger entity){
		return linksusGovMegerMapper.insertLinksusGovMeger(entity);
	}

	/** ���� */
	public Integer updateLinksusGovMeger(LinksusGovMeger entity){
		return linksusGovMegerMapper.updateLinksusGovMeger(entity);
	}

	/** ����ɾ�� */
	public Integer deleteLinksusGovMegerById(Long pid){
		return linksusGovMegerMapper.deleteLinksusGovMegerById(pid);
	}

	@Override
	public List<LinksusGovMeger> getLinksusGovMegerListByFather(Long fatherId){

		return linksusGovMegerMapper.getLinksusGovMegerListByFather(fatherId);
	}
}