package com.linksus.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.dao.LinksusTaskErrorCodeMapper;
import com.linksus.entity.LinksusTaskErrorCode;
import com.linksus.service.LinksusTaskErrorCodeService;

@Service("linksusTaskErrorCodeService")
public class LinksusTaskErrorCodeServiceImpl implements LinksusTaskErrorCodeService{

	@Autowired
	private LinksusTaskErrorCodeMapper linksusTaskErrorCodeMapper;

	/** ��ѯ�б� */
	public List<LinksusTaskErrorCode> getLinksusTaskErrorCodeList(){
		return linksusTaskErrorCodeMapper.getLinksusTaskErrorCodeList();
	}

	/** ������ѯ */
	public LinksusTaskErrorCode getLinksusTaskErrorCodeById(Long pid){
		return linksusTaskErrorCodeMapper.getLinksusTaskErrorCodeById(pid);
	}

	/** ���� */
	public Integer insertLinksusTaskErrorCode(LinksusTaskErrorCode entity){
		return linksusTaskErrorCodeMapper.insertLinksusTaskErrorCode(entity);
	}

	/** ���� */
	public Integer updateLinksusTaskErrorCode(LinksusTaskErrorCode entity){
		return linksusTaskErrorCodeMapper.updateLinksusTaskErrorCode(entity);
	}

	/** ����ɾ�� */
	public Integer deleteLinksusTaskErrorCodeById(Long pid){
		return linksusTaskErrorCodeMapper.deleteLinksusTaskErrorCodeById(pid);
	}
}