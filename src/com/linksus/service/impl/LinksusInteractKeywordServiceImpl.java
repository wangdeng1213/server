package com.linksus.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.dao.LinksusInteractKeywordMapper;
import com.linksus.entity.LinksusInteractKeyword;
import com.linksus.service.LinksusInteractKeywordService;

@Service("linksusInteractKeywordService")
public class LinksusInteractKeywordServiceImpl implements LinksusInteractKeywordService{

	@Autowired
	private LinksusInteractKeywordMapper linksusInteractKeywordMapper;

	/** ��ѯ�б� */
	public List<LinksusInteractKeyword> getLinksusInteractKeywordList(){
		return linksusInteractKeywordMapper.getLinksusInteractKeywordList();
	}

	/** ������ѯ */
	public LinksusInteractKeyword getLinksusInteractKeywordById(Long pid){
		return linksusInteractKeywordMapper.getLinksusInteractKeywordById(pid);
	}

	/** ���� */
	public Integer insertLinksusInteractKeyword(LinksusInteractKeyword entity){
		return linksusInteractKeywordMapper.insertLinksusInteractKeyword(entity);
	}

	/** ���� */
	public Integer updateLinksusInteractKeyword(LinksusInteractKeyword entity){
		return linksusInteractKeywordMapper.updateLinksusInteractKeyword(entity);
	}

	/** ����ɾ�� */
	public Integer deleteLinksusInteractKeywordById(Long pid){
		return linksusInteractKeywordMapper.deleteLinksusInteractKeywordById(pid);
	}
}