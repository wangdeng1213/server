package com.linksus.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linksus.dao.LinksusGovDocumentMapper;
import com.linksus.entity.LinksusGovDocument;
import com.linksus.service.LinksusGovDocumentService;

@Service("linksusGovDocumentService")
public class LinksusGovDocumentServiceImpl implements LinksusGovDocumentService{

	@Autowired
	private LinksusGovDocumentMapper linksusGovDocumentMapper;

	/** ��ѯ�б� */
	public List<LinksusGovDocument> getLinksusGovDocumentList(){
		return linksusGovDocumentMapper.getLinksusGovDocumentList();
	}

	/** ������ѯ */
	public LinksusGovDocument getLinksusGovDocumentById(Long pid){
		return linksusGovDocumentMapper.getLinksusGovDocumentById(pid);
	}

	/** ���� */
	public Integer insertLinksusGovDocument(LinksusGovDocument entity){
		return linksusGovDocumentMapper.insertLinksusGovDocument(entity);
	}

	/** ���� */
	public Integer updateLinksusGovDocument(LinksusGovDocument entity){
		return linksusGovDocumentMapper.updateLinksusGovDocument(entity);
	}

	/** ����ɾ�� */
	public Integer deleteLinksusGovDocumentById(Long pid){
		return linksusGovDocumentMapper.deleteLinksusGovDocumentById(pid);
	}

	@Override
	public List<LinksusGovDocument> getLinksusGovDocumentListByAccount(Long accountId){
		return linksusGovDocumentMapper.getLinksusGovDocumentListByAccount(accountId);
	}
}