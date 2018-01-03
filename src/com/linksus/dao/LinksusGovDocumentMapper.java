package com.linksus.dao;

import java.util.List;

import com.linksus.entity.LinksusGovDocument;

public interface LinksusGovDocumentMapper{

	/** �б��ѯ */
	public List<LinksusGovDocument> getLinksusGovDocumentList();

	/** ������ѯ */
	public LinksusGovDocument getLinksusGovDocumentById(Long pid);

	/** ���� */
	public Integer insertLinksusGovDocument(LinksusGovDocument entity);

	/** ���� */
	public Integer updateLinksusGovDocument(LinksusGovDocument entity);

	/** ����ɾ�� */
	public Integer deleteLinksusGovDocumentById(Long pid);

	public List<LinksusGovDocument> getLinksusGovDocumentListByAccount(Long accountId);

}
