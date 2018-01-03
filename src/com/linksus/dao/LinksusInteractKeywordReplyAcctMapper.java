package com.linksus.dao;

import java.util.List;

import com.linksus.entity.LinksusInteractKeywordReplyAcct;

public interface LinksusInteractKeywordReplyAcctMapper{

	/** �б��ѯ */
	public List<LinksusInteractKeywordReplyAcct> getLinksusInteractKeywordReplyAcctList();

	/** ������ѯ */
	public LinksusInteractKeywordReplyAcct getLinksusInteractKeywordReplyAcctById(Long pid);

	/** ���� */
	public Integer insertLinksusInteractKeywordReplyAcct(LinksusInteractKeywordReplyAcct entity);

	/** ���� */
	public Integer updateLinksusInteractKeywordReplyAcct(LinksusInteractKeywordReplyAcct entity);

	/** ����ɾ�� */
	public Integer deleteLinksusInteractKeywordReplyAcctById(Long pid);

}
