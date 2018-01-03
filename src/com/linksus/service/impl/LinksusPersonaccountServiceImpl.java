package com.linksus.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.dao.LinksusPersonaccountMapper;
import com.linksus.entity.LinksusPersonaccount;
import com.linksus.service.LinksusPersonaccountService;

@Service("linksusPersonaccountService")
public class LinksusPersonaccountServiceImpl implements LinksusPersonaccountService{

	@Autowired
	private LinksusPersonaccountMapper linksusPersonaccountMapper;

	/** ��ѯ�б� */
	public List<LinksusPersonaccount> getLinksusPersonaccountList(){
		return linksusPersonaccountMapper.getLinksusPersonaccountList();
	}

	/** ������ѯ */
	public LinksusPersonaccount getLinksusPersonaccountById(Long pid){
		return linksusPersonaccountMapper.getLinksusPersonaccountById(pid);
	}

	/** ���� */
	public Integer insertLinksusPersonaccount(LinksusPersonaccount entity){
		return linksusPersonaccountMapper.insertLinksusPersonaccount(entity);
	}

	/** ���� */
	public Integer updateLinksusPersonaccount(LinksusPersonaccount entity){
		return linksusPersonaccountMapper.updateLinksusPersonaccount(entity);
	}

	/** ����ɾ�� */
	public Integer deleteLinksusPersonaccountById(Long pid){
		return linksusPersonaccountMapper.deleteLinksusPersonaccountById(pid);
	}

	public Map getManagerByInstitutionId(Long institutionId){
		return linksusPersonaccountMapper.getManagerByInstitutionId(institutionId);
	}

	public List getPersonaccountByInstitutionId(Long institutionId){
		return linksusPersonaccountMapper.getPersonaccountByInstitutionId(institutionId);

	}
}