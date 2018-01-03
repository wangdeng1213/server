package com.linksus.service;

import java.util.List;

import com.linksus.entity.LinksusTaskErrorCode;

public interface LinksusTaskErrorCodeService{

	/** ��ѯ�б� */
	public List<LinksusTaskErrorCode> getLinksusTaskErrorCodeList();

	/** ������ѯ */
	public LinksusTaskErrorCode getLinksusTaskErrorCodeById(Long pid);

	/** ���� */
	public Integer insertLinksusTaskErrorCode(LinksusTaskErrorCode entity);

	/** ���� */
	public Integer updateLinksusTaskErrorCode(LinksusTaskErrorCode entity);

	/** ����ɾ�� */
	public Integer deleteLinksusTaskErrorCodeById(Long pid);

}