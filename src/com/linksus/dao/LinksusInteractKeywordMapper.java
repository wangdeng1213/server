package com.linksus.dao;

import java.util.List;

import com.linksus.entity.LinksusInteractKeyword;

public interface LinksusInteractKeywordMapper{

	/** �б��ѯ */
	public List<LinksusInteractKeyword> getLinksusInteractKeywordList();

	/** ������ѯ */
	public LinksusInteractKeyword getLinksusInteractKeywordById(Long pid);

	/** ���� */
	public Integer insertLinksusInteractKeyword(LinksusInteractKeyword entity);

	/** ���� */
	public Integer updateLinksusInteractKeyword(LinksusInteractKeyword entity);

	/** ����ɾ�� */
	public Integer deleteLinksusInteractKeywordById(Long pid);

}
