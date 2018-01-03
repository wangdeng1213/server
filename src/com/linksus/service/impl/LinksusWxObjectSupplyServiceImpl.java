package com.linksus.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.dao.LinksusWxObjectSupplyMapper;
import com.linksus.entity.LinksusWxObjectSupply;
import com.linksus.service.LinksusWxObjectSupplyService;

@Service("linksusWxObjectSupplyService")
public class LinksusWxObjectSupplyServiceImpl implements LinksusWxObjectSupplyService{

	@Autowired
	private LinksusWxObjectSupplyMapper linksusWxObjectSupplyMapper;

	/** ��ѯ�б� */
	public List<LinksusWxObjectSupply> getLinksusWxObjectSupplyList(){
		return linksusWxObjectSupplyMapper.getLinksusWxObjectSupplyList();
	}

	/** ������ѯ */
	public LinksusWxObjectSupply getLinksusWxObjectSupplyById(Long pid){
		return linksusWxObjectSupplyMapper.getLinksusWxObjectSupplyById(pid);
	}

	/** ���� */
	public Integer insertLinksusWxObjectSupply(LinksusWxObjectSupply entity){
		return linksusWxObjectSupplyMapper.insertLinksusWxObjectSupply(entity);
	}

	/** ���� */
	public Integer updateLinksusWxObjectSupply(LinksusWxObjectSupply entity){
		return linksusWxObjectSupplyMapper.updateLinksusWxObjectSupply(entity);
	}

	/** ����ɾ�� */
	public Integer deleteLinksusWxObjectSupplyById(Long pid){
		return linksusWxObjectSupplyMapper.deleteLinksusWxObjectSupplyById(pid);
	}

	/** ��ѯ��������΢������ */
	public LinksusWxObjectSupply getSingleLinksusWxObjectSupplyById(Long pid){
		return linksusWxObjectSupplyMapper.getSingleLinksusWxObjectSupplyById(pid);
	}

	/** ��ѯ�������΢������*/
	public List<LinksusWxObjectSupply> getMoreLinksusWxObjectSupplyById(Long pid){
		return linksusWxObjectSupplyMapper.getMoreLinksusWxObjectSupplyById(pid);
	}
}